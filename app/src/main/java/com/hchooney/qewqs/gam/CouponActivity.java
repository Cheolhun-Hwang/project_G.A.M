package com.hchooney.qewqs.gam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hchooney.qewqs.gam.Dialog.netWaitDailog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CouponActivity extends AppCompatActivity {

    private ImageView Coupon;
    private ImageButton Back;
    private String photourl;

    private Handler handler;
    private Bitmap bitmap;

    private com.hchooney.qewqs.gam.Dialog.netWaitDailog netWaitDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        init();

        netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    connectURL();
                    // 메시지 얻어오기
                    Message msg = handler.obtainMessage();
                    // 메시지 ID 설정
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                netWaitDailog.dismiss();
            }
        });

        t.start();





    }
    private void init(){
        String temp[] =getIntent().getStringExtra("url").toString().split("/");
        photourl = temp[temp.length-1];

        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setMessage("서버에서 서비스 정보를 받아오는 중입니다.");
        netWaitDailog.setTitle("정보 받아 오는 중");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what ==1){
                    setImage();
                }

                return true;
            }
        });

        Coupon = (ImageView) findViewById(R.id.Coupon_imageview);
        Back = (ImageButton) findViewById(R.id.Coupon_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setImage(){
        Coupon.setImageBitmap(bitmap);
    }

    private void connectURL(){
        try {
            URL url = new URL("http://203.249.127.32:64001/mobile/coupon/images?file="+photourl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
