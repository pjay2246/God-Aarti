package com.trycatch.GodAarti.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.trycatch.GodAarti.R;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressBar);

        Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressBar.incrementProgressBy(10);
            }
        };



            progressBar.setMax(100);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (progressBar.getProgress() <= progressBar.getMax()) {
                            Thread.sleep(500);
                            handle.sendMessage(handle.obtainMessage());
                            if (progressBar.getProgress() == progressBar.getMax()) {
                                nextActivity();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();



    }

    public  void  nextActivity(){
        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);
        Thread.currentThread().interrupt();
    }

}