package com.hchooney.qewqs.gam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CouponActivity extends AppCompatActivity {

    private ImageView Coupon;
    private ImageButton Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        init();
    }
    private void init(){
        Coupon = (ImageView) findViewById(R.id.Coupon_imageview);
        Back = (ImageButton) findViewById(R.id.Coupon_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
