package com.hchooney.qewqs.gam;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.JoinEventDialogFragment;
import com.hchooney.qewqs.gam.Dialog.WarningDialogFragment;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.Dialog.netWaitDailog;
import com.hchooney.qewqs.gam.Net.SendGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private final String TAG = "SettingActivity";
    private ImageButton Back;
    private TextView ChangShowNickName;
    private Button ChangNickName;
    private TextView Warning;
    private Button EventJoinList;
    private Button Logout;

    private Switch gps;
    private Switch push;

    private ArrayList<WarningItem> wlist;

    private Handler handler;
    private com.hchooney.qewqs.gam.Dialog.netWaitDailog netWaitDailog;

    private Account user;

    private boolean isGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

        netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    dataLoad();
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


        setUI();

    }

    private void init(){
        user = (Account) getIntent().getSerializableExtra("user");
        isGPS = (boolean) getIntent().getBooleanExtra("gps", false);
        if(user == null){
            Toast.makeText(getApplicationContext(), "세부이벤트 정보를 받지 못했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d(TAG, "Guide Information Load OK. guideID : " + this.user.getUid());
        }
        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setMessage("서버에서 서비스 정보를 받아오는 중입니다.");
        netWaitDailog.setTitle("정보 받아 오는 중");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what ==1){
                    Warning.setText(wlist.size()+"");
                }

                return true;
            }
        });

        Back = (ImageButton)findViewById(R.id.Setting_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("gps", isGPS);
                setResult(2002, intent);
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
                bundle.putParcelableArrayList("list", wlist);
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

        gps = (Switch) findViewById(R.id.Setting_switch_GPS);
        gps.setChecked(isGPS);
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isGPS = true;
                }else{
                    isGPS = false;
                }
            }
        });

    }

    private void setUI(){
        ChangShowNickName.setText(this.user.getUnickname());
    }

    private void dataLoad(){
        wlist = new ArrayList<WarningItem>();

        //임시
        /*list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));*/
        String res_warning = new SendGet("set/warning", ("?uid="+user.getUid())).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_warning);

        JSONObject warning_data = null;
        try {
            warning_data = new JSONObject(res_warning);
            JSONArray warningArray = warning_data.getJSONArray("warning");
            for (int i=0;i<warningArray.length();i++){
                JSONObject obj = (JSONObject) warningArray.get(i);
                WarningItem item = new WarningItem();
                item.setWWhy(((String) obj.get("WWHY")));
                item.setWWhen((String) obj.get("WWHEN").toString().substring(0, 10));
                wlist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
