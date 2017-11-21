package com.hchooney.qewqs.gam;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity {

    private ImageView LogoImage;
    private SignInButton googleSign;

    private Thread Loading;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Loading.start();
        startAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init(){
        LogoImage = (ImageView) findViewById(R.id.Login_Logo_Img);
        googleSign = (SignInButton) findViewById(R.id.Login_googleAuthBTN);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        googleSign.startAnimation(fadeInAnimation);

        Loading = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message msg = Message.obtain();
                    msg.what =1;
                    handler.sendMessage(msg);

                    Message msg2 = Message.obtain();
                    msg2.what =2;
                    handler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    translateAnim(LogoImage.getX(), LogoImage.getX(), LogoImage.getY(), (float) ( LogoImage.getY()-0.8), 8000, LogoImage);
                }else if(msg.what == 2){
                    googleSign.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    private void startAnim(){
        translateAnim(LogoImage.getX(), LogoImage.getX(), LogoImage.getY(), (float) ( LogoImage.getY()-0.8), 2000, LogoImage);
        googleSign.setVisibility(View.VISIBLE);
    }
    private void translateAnim(float xStart, float xEnd, float yStart, float yEnd, int duration, ImageView imageView) {

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,xStart,
                Animation.RELATIVE_TO_SELF, xEnd,
                Animation.RELATIVE_TO_SELF,yStart,
                Animation.RELATIVE_TO_SELF,yEnd);

        translateAnimation.setDuration(duration);

        imageView.startAnimation(translateAnimation);
        translateAnimation.setFillAfter(true);

    }
}
