package pe.edu.tecsup.tecsupverify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pe.edu.tecsup.tecsupverify.R;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = SimpleScannerActivity.class.getSimpleName();

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.CODE_128);
        mScannerView.setFormats(formats);
        mScannerView.startCamera();
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d(TAG, "Scan completed!");

        Log.d(TAG, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        // Toast.makeText(this, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("DNI", rawResult.getText());

        setResult(RESULT_OK, intent);

        finish();

    }
}
