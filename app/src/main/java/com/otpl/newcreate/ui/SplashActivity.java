package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.otpl.newcreate.MyApplication;
import com.otpl.newcreate.R;
import com.otpl.newcreate.databinding.ActivitySplashBinding;
import com.otpl.newcreate.utils.CommonUtils;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Context mContext = SplashActivity.this;
    private ActivitySplashBinding binding;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!CommonUtils.isNullOrEmpty(MyApplication.getUserId())) {
                    Intent i = new Intent(mContext, DashboardActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}



