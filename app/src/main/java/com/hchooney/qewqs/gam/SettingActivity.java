package com.hchooney.qewqs.gam;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.JoinEventDialogFragment;
import com.hchooney.qewqs.gam.Dialog.WarningDialogFragment;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private final String TAG = "SettingActivity";
    private ImageButton Back;
    private TextView ChangShowNickName;
    private Button ChangNickName;
    private TextView Warning;
    private Button EventJoinList;
    private Button Logout;

    private Account user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
        setUI();
    }

    private void init(){
        user = (Account) getIntent().getSerializableExtra("user");
        if(user == null){
            Toast.makeText(getApplicationContext(), "세부이벤트 정보를 받지 못했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d(TAG, "Guide Information Load OK. guideID : " + this.user.getUid());
        }

        Back = (ImageButton)findViewById(R.id.Setting_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        ChangShowNickName = (TextView) findViewById(R.id.Setting_Nickname_show);
        ChangNickName = (Button) findViewById(R.id.Setting_Nickname_change);
        ChangNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Warning = (TextView) findViewById(R.id.Setting_Warning);
        Warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarningDialogFragment fragment = new WarningDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "WarningDialogFragment");
            }
        });
        EventJoinList = (Button) findViewById(R.id.Setting_EventJoinList);
        EventJoinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinEventDialogFragment fragment = new JoinEventDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "JoinEventDialogFragment");
            }
        });

        Logout = (Button) findViewById(R.id.Setting_Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setUI(){
        ChangShowNickName.setText(this.user.getUnickname());
    }
}
