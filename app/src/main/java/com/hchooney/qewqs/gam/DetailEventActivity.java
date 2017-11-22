package com.hchooney.qewqs.gam;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.RecyclerList.DetailEvent.DetailEventAdapter;
import com.hchooney.qewqs.gam.RecyclerList.DetailEvent.DetailEventItem;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailEventActivity extends AppCompatActivity {
    private final String TAG = "DetailEventActivity";
    private final static int SIGNAL_JOINEVENT = 3001;

    private TextView ETitle;
    private TextView ECordination;
    private TextView EProfit;
    private TextView ELimitDate;
    private TextView ENum;
    private ImageButton JoinEvent;
    private RecyclerView joinListView;

    //리소스
    private ArrayList<DetailEventItem> list;
    private EventItem eventItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        init();
        LoadDate();
        setUI();
        ListSetup();
    }

    private void init() {

        eventItem = (EventItem) getIntent().getSerializableExtra("Event");
        if (eventItem == null) {
            Toast.makeText(getApplicationContext(), "세부이벤트 정보를 받지 못했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d(TAG, "Guide Information Load OK. guideID : " + this.eventItem.getEid());
        }

        ETitle = (TextView) findViewById(R.id.DetailEvent_Title);
        ECordination = (TextView) findViewById(R.id.DetailEvent_Cordination);
        EProfit = (TextView) findViewById(R.id.DetailEvent_profit);
        ELimitDate = (TextView) findViewById(R.id.DetailEvent_limitdate);
        ENum = (TextView) findViewById(R.id.DetailEvent_Num);
        JoinEvent = (ImageButton) findViewById(R.id.DetailEvent_floatBTN);
        JoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinEventActivity.class);
                intent.putExtra("eid", eventItem.getEid());
                startActivityForResult(intent, SIGNAL_JOINEVENT);
            }
        });
        joinListView = (RecyclerView) findViewById(R.id.DetailEvent_showUpListView);
        joinListView.setHasFixedSize(true);

    }

    private void setUI(){
        ETitle.setText(eventItem.geteSpot());
        EProfit.setText("혜택 : " + eventItem.geteProfit());
        ECordination.setText("조건 : " + eventItem.geteCordination());
        ELimitDate.setText("기간 : " + eventItem.geteLimitDate());
        ENum.setText("인원 : " + eventItem.geteNum());
    }

    private void ListSetup(){
        joinListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        joinListView.setAdapter(new DetailEventAdapter(getApplicationContext(), list));
    }


    private void LoadDate(){
        list = new ArrayList<DetailEventItem>();
        list.add(new DetailEventItem(1, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));
        list.add(new DetailEventItem(2, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));
        list.add(new DetailEventItem(3, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));
        list.add(new DetailEventItem(4, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));
        list.add(new DetailEventItem(5, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));
        list.add(new DetailEventItem(6, "임시 이벤트 참가 제목", "2017.00.00 00:00", "eventPhotoURL", new ArrayList<String>(Arrays.asList("홍길동", "홍길홍", "홍당무"))));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNAL_JOINEVENT){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "이벤트 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "이벤트 등록에 실패하였습니다.\n잠시 후 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
            }

        }
   }
}
