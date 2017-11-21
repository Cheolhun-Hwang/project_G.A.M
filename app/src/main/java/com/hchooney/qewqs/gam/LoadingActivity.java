package com.hchooney.qewqs.gam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {
    private Thread Loading;
    private Thread Setdata;
    private boolean isSetdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        init();

        Loading.start();
    }

    private void init(){
        isSetdata = false;


        Loading = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (!isSetdata){
                        if(!Setdata.isAlive()){
                            Setdata.start();
                        }
                        Thread.sleep(3000);
                    }

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Setdata = new Thread(new Runnable() {
            @Override
            public void run() {
                setDatabase();
            }
        });
    }

    private void setDatabase(){
        isSetdata = true;
    }

    @Override
    public void finish() {
        super.finish();
    }
}
