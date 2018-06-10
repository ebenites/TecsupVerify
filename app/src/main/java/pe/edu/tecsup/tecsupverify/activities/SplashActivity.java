package pe.edu.tecsup.tecsupverify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import pe.edu.tecsup.tecsupverify.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                verifyLogged();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void verifyLogged(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}
