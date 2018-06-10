package pe.edu.tecsup.tecsupverify.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pe.edu.tecsup.tecsupverify.R;
import pe.edu.tecsup.tecsupverify.adapters.UsersRVAdapter;
import pe.edu.tecsup.tecsupverify.models.APIError;
import pe.edu.tecsup.tecsupverify.models.User;
import pe.edu.tecsup.tecsupverify.services.TecsupService;
import pe.edu.tecsup.tecsupverify.services.TecsupServiceGenerator;
import pe.edu.tecsup.tecsupverify.util.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText searchInput;
    private ImageButton clearButton;
    private RecyclerView usersRecyclerview;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearButton = findViewById(R.id.clear_button);

        searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    clearButton.setVisibility(View.GONE);
                }else{
                    clearButton.setVisibility(View.VISIBLE);
                }
            }
        });

        emptyMessage = findViewById(R.id.empty_message);

        usersRecyclerview = findViewById(R.id.users_recyclerview);
        usersRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerview.setAdapter(new UsersRVAdapter());

    }

    public void callClear(View view){
        Log.d(TAG, "callClear");
        searchInput.setText("");
    }

    public void callSearch(View view) {
        Log.d(TAG, "callSearch");

        String dni = searchInput.getText().toString();
        Log.d(TAG, "dni: " + dni);

        if(dni.isEmpty()){
            Toast.makeText(this, R.string.dni_required, Toast.LENGTH_SHORT).show();
            return;
        }

        search(dni);
    }

    public void callScan(View view){
        Log.d(TAG, "callScan");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        startActivityForResult(new Intent(this, SimpleScannerActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK &&  data.getExtras() != null){
            String dni = data.getExtras().getString("DNI");
            Log.d(TAG, "dni: " + dni);

            search(dni);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            callScan(null);
        }
    }

    private void search(String dni) {
        Log.d(TAG, "search: " + dni);

        if (!NetworkUtils.isConnected(this)) {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_empty).setTitle(R.string.connection_error).setMessage(R.string.disconnection_error)/*.setPositiveButton(android.R.string.ok, null)*/.create().show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getString(R.string.search_information));
        progressDialog.show();

        TecsupService service = TecsupServiceGenerator.createService(TecsupService.class);

        Call<List<User>> call = service.searchUsers(dni);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<User> users = response.body();
                        Log.d(TAG, "users: " + users);

                        UsersRVAdapter adapter = (UsersRVAdapter) usersRecyclerview.getAdapter();
                        adapter.setUsers(users);
                        adapter.notifyDataSetChanged();

                        if (users != null && users.isEmpty()) {
                            emptyMessage.setVisibility(View.VISIBLE);
                        }else{
                            emptyMessage.setVisibility(View.GONE);
                        }

                    } else {
                        APIError error = TecsupServiceGenerator.parseError(response);
                        Log.e(TAG, "onError: " + error);
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.toString(), t);
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        });
    }
}
