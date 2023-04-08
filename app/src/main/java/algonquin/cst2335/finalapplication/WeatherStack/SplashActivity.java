package algonquin.cst2335.finalapplication.WeatherStack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalapplication.R;

public class SplashActivity extends AppCompatActivity {

    /** the Handler  **/
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        handler.postDelayed(runnable, 1000*3);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(
                    new Intent(
                            SplashActivity.this,
                            WeatherActivity.class));
            finish();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBackPressed() {
        if(handler.hasCallbacks(runnable))
            handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}