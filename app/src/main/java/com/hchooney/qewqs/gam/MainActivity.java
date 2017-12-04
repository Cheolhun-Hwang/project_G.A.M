package com.hchooney.qewqs.gam;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.hchooney.qewqs.gam.Dialog.netWaitDailog;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.CouponDialogFragment;
import com.hchooney.qewqs.gam.Dialog.RecyclerList.CouponItem;
import com.hchooney.qewqs.gam.Fragments.MainFragment;
import com.hchooney.qewqs.gam.Fragments.MapFragment;
import com.hchooney.qewqs.gam.Net.SendGet;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int SIG_LOGOUT = 4004;

    private boolean isGPS;
    private Location nowLocation;
    private GPSListener gpsListener;
    private LocationManager gmanager;

    private final static String TAG = "MainActivity";
    private static Account user;
    private static ArrayList<NotifyItem> notifylist;
    private static ArrayList<GuideItem> guidelist;
    private static ArrayList<EventItem> eventlist;
    private static ArrayList<CouponItem> couponlist;

    private static int SearchDistance;
    private static GoogleApiClient mGoogleApiClient;

    private FragmentManager manager;

    public static void setUser(Account u) {
        user = u;
    }
    public static Account getUser(){return user;}
    public static void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        MainActivity.mGoogleApiClient = mGoogleApiClient;
    }
    public static ArrayList<NotifyItem> getNotifylist() {
        return notifylist;
    }
    public static void setNotifylist(ArrayList<NotifyItem> notifylist) {
        MainActivity.notifylist = notifylist;
    }
    public static ArrayList<GuideItem> getGuidelist() {
        return guidelist;
    }
    public static void setGuidelist(ArrayList<GuideItem> guidelist) {
        MainActivity.guidelist = guidelist;
    }
    public static ArrayList<EventItem> getEventlist() {
        return eventlist;
    }
    public static void setEventlist(ArrayList<EventItem> eventlist) {
        MainActivity.eventlist = eventlist;
    }
    public static ArrayList<CouponItem> getCouponlist() {
        return couponlist;
    }
    public static void setCouponlist(ArrayList<CouponItem> couponlist) {
        MainActivity.couponlist = couponlist;
    }
    public boolean isGPS() {
        return isGPS;
    }
    public void setGPS(boolean GPS) {
        isGPS = GPS;
    }

    private ImageButton Map;
    private ImageButton Gift;
    private ImageButton Setting;

    private boolean ishomeandmap; //true : home, false : map

    private netWaitDailog netWaitDailog;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    SetData();

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



        Toast.makeText(getApplicationContext(), "환영합니다. "+ user.getUname()+"!!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "USER ID : " + user.getUid() +"\nUSER NAME : " + user.getUname() + "\nUSER EMAIL : " + user.getUemail());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init(){
        isGPS = true;

        if(isGPS){
            startLocationService();
        }else{
            Log.d("GPS", "GPS END");
            gpsListener = null;
        }

        nowLocation = null;
        gpsListener = null;


        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setMessage("서버에서 서비스 정보를 받아오는 중입니다.");
        netWaitDailog.setTitle("정보 받아 오는 중");

        if(user == null){
            user = new Account();
        }
        SearchDistance = 2;
        ishomeandmap = false;

        Map = (ImageButton) findViewById(R.id.MainMapBTN);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ishomeandmap){
                    manager.beginTransaction().replace(R.id.Main_container, new MainFragment()).commit();
                    Map.setImageResource(R.drawable.ic_map_24dp);
                    ishomeandmap = false;
                }else{
                    manager.beginTransaction().replace(R.id.Main_container, new MapFragment()).commit();
                    Map.setImageResource(R.drawable.ic_home);
                    ishomeandmap = true;
                }
            }
        });

        Gift = (ImageButton) findViewById(R.id.MainGiftBTN);
        Gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), CouponActivity.class));
                Bundle bundle = new Bundle();
                bundle.putSerializable("items", couponlist);
                CouponDialogFragment fragment = new CouponDialogFragment();
                fragment.setArguments(bundle);

                fragment.show(manager, "CouponDialogFragment");

            }
        });

        Setting = (ImageButton) findViewById(R.id.MainSettingBTN);
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("gps", isGPS);
                startActivityForResult(intent, SIG_LOGOUT);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.Main_container, new MainFragment()).commit();
                }
                return true;
            }
        });

    }

    private void SetData(){
        //notify
        //temp
        notifylist = new ArrayList<NotifyItem>();
        /*notifylist.add(new NotifyItem("0001", "공지사항 제목 1", "2017-00-00 00:00", "공지사항 1의 공지내용입니다.", "관리자"));
        notifylist.add(new NotifyItem("0002", "공지사항 제목 2", "2017-00-00 00:00", "공지사항 2의 공지내용입니다.", "관리자"));
        notifylist.add(new NotifyItem("0003", "공지사항 제목 3", "2017-00-00 00:00", "공지사항 3의 공지내용입니다.", "관리자"));
        notifylist.add(new NotifyItem("0004", "공지사항 제목 4", "2017-00-00 00:00", "공지사항 4의 공지내용입니다.", "관리자"));
        notifylist.add(new NotifyItem("0005", "공지사항 제목 5", "2017-00-00 00:00", "공지사항 5의 공지내용입니다.", "관리자"));*/

        String res_notice = new SendGet("notice/list", "").SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_notice);

        JSONObject notice_data = null;
        try {
            notice_data = new JSONObject(res_notice);
            JSONArray noticeArray = notice_data.getJSONArray("notice");
            for (int i=0;i<noticeArray.length();i++){
                JSONObject obj = (JSONObject) noticeArray.get(i);
                NotifyItem item = new NotifyItem();
                item.setDate(((String) obj.get("NMODIFY")).substring(0, 10));
                item.setTItle((String) obj.get("NTITLE"));
                item.setWho((String) obj.get("ADNAME"));
                item.setContext((String) obj.get("NCONTEXT"));
                item.setNid((String) (obj.get("NID")+""));
                notifylist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //guide
        //temp
        guidelist = new ArrayList<GuideItem>();
        /*guidelist.add(new GuideItem(1, "임시 장소 이름", 127.128876, 37.455127, "None", "None", "2017-00-00 00:00"));
        guidelist.add(new GuideItem(2, "임시 장소 이름", 127.128876, 37.459136, "None", "None", "2017-00-00 00:00"));
        guidelist.add(new GuideItem(3, "임시 장소 이름", 127.128876, 37.457115, "None", "None", "2017-00-00 00:00"));
        guidelist.add(new GuideItem(4, "임시 장소 이름", 127.128876, 37.461104, "None", "None", "2017-00-00 00:00"));
        guidelist.add(new GuideItem(5, "임시 장소 이름", 127.128876, 37.471153, "None", "None", "2017-00-00 00:00"));
        guidelist.add(new GuideItem(6, "임시 장소 이름", 127.128876, 37.450162, "None", "None", "2017-00-00 00:00"));*/

        String res_guide = new SendGet("search/guide", ("?dist=1&gpsx=127.127163&gpsy=37.450881")).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_guide);

        JSONObject guide_data = null;
        try {
            guide_data = new JSONObject(res_guide);
            JSONArray guideArray = guide_data.getJSONArray("guide");
            for (int i=0;i<guideArray.length();i++){
                JSONObject obj = (JSONObject) guideArray.get(i);
                GuideItem item = new GuideItem();
                item.setGid((Integer.parseInt(obj.get("GID").toString())));
                item.setgAudio((String) obj.get("GAUDIO"));
                item.setgImage(obj.get("GPHOTO").toString());
                item.setgModifyDate(obj.get("GMODIFY").toString().substring(0, 10));
                item.setGpsx(Double.parseDouble(obj.get("GGPSY").toString()));
                item.setGpsy(Double.parseDouble(obj.get("GGPSX").toString()));
                item.setSpot(obj.get("GWHERE").toString());
                guidelist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //event
        //temp
        eventlist = new ArrayList<EventItem>();
        /*eventlist.add(new EventItem(1, "임시 이벤트 이름", "2017.00.00 00:00", "이벤트 조건 문자열", "임시 이벤트 장소 이름", 0, 127.128875, 37.452197, "이벤트 당첨 혜택"));
        eventlist.add(new EventItem(2, "임시 이벤트 이름", "2017.00.00 00:00", "이벤트 조건 문자열", "임시 이벤트 장소 이름", 0, 127.128874, 37.431127, "이벤트 당첨 혜택"));
        eventlist.add(new EventItem(3, "임시 이벤트 이름", "2017.00.00 00:00", "이벤트 조건 문자열", "임시 이벤트 장소 이름", 0, 127.128873, 37.451247, "이벤트 당첨 혜택"));
        eventlist.add(new EventItem(4, "임시 이벤트 이름", "2017.00.00 00:00", "이벤트 조건 문자열", "임시 이벤트 장소 이름", 0, 127.128872, 37.450067, "이벤트 당첨 혜택"));*/
        String res_event = new SendGet("search/event", ("?dist=1&gpsx=127.127163&gpsy=37.450881")).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_event);

        JSONObject event_data = null;
        try {
            event_data = new JSONObject(res_event);
            JSONArray eventArray = event_data.getJSONArray("events");
            for (int i=0;i<eventArray.length();i++){
                JSONObject obj = (JSONObject) eventArray.get(i);
                EventItem item = new EventItem();
                item.setEid(Integer.parseInt(obj.get("EID").toString()));
                item.seteName(((String) obj.get("ENAME")));
                item.seteProfit((String) obj.get("EPROFIT"));
                item.seteLimitDate(obj.get("EDEADLINE").toString().substring(0, 10));
                item.seteCordination((String) obj.get("ECORDI"));
                item.seteGpsx(Double.parseDouble(obj.get("EGPSY").toString()));
                item.seteGpsy(Double.parseDouble(obj.get("EGPSX").toString()));
                item.seteNum(Integer.parseInt(obj.get("ENUM").toString()));
                item.setePhoto(obj.get("GPHOTO").toString());
                item.seteSpot(obj.get("EWHERE").toString());
                eventlist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //coupon
        //temp
        couponlist = new ArrayList<CouponItem>();
        /*couponlist.add(new CouponItem(1, "임시 이벤트 명", "쿠폰 혜택 / 쿠폰 명", "2017.00.00 00:00", "None"));
        couponlist.add(new CouponItem(2, "임시 이벤트 명", "쿠폰 혜택 / 쿠폰 명", "2017.00.00 00:00", "None"));
        couponlist.add(new CouponItem(3, "임시 이벤트 명", "쿠폰 혜택 / 쿠폰 명", "2017.00.00 00:00", "None"));
        couponlist.add(new CouponItem(4, "임시 이벤트 명", "쿠폰 혜택 / 쿠폰 명", "2017.00.00 00:00", "None"));
        couponlist.add(new CouponItem(5, "임시 이벤트 명", "쿠폰 혜택 / 쿠폰 명", "2017.00.00 00:00", "None"));*/

        String res_coupon = new SendGet("coupon/list", ("?uid="+user.getUid())).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_coupon);

        JSONObject coupon_data = null;
        try {
            coupon_data = new JSONObject(res_coupon);
            JSONArray couponArray = coupon_data.getJSONArray("coupon");
            for (int i=0;i<couponArray.length();i++){
                JSONObject obj = (JSONObject) couponArray.get(i);
                CouponItem item = new CouponItem();
                item.setEName(((String) obj.get("ENAME")));
                item.setCPhotourl((String) obj.get("CPHOTO"));
                item.setEdaedlinedate(obj.get("EDEADLINE").toString().substring(0, 10));
                item.setEProfit((String) obj.get("EPROFIT"));
                couponlist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // 위치 정보 확인을 위해 정의한 메소드(위치 정보 갱신)
    private void startLocationService() {
        // 위치 관리자 객체 참조
        gmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 위치 리스너 객체 생성
        gpsListener = new GPSListener();
        long minTime = 5000;//GPS정보 전달 시간 지정 - 5초마다 위치정보 전달
        float minDistance = 0;//이동거리 지정

        //위치정보는 위치 프로바이더(Location Provider)를 통해 얻는다
        try {
            // GPS를 이용한 위치 요청
            gmanager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,//위치 확인 방법
                    minTime,//GPS 정보 전달 시간- 위치 정보 갱신 시간 설정
                    minDistance,//위치 정보 갱신을 위한 이동거리
                    gpsListener);//위치 정보가 갱신되는 시점에 호출될 리스너 등록 - 위치 정보 전달

            // 네트워크(기지국)를 이용한 위치 요청
            gmanager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = gmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();
                Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : "
                        + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 위치 리스너 클래스 정의
    private class GPSListener implements LocationListener {
        //위치 정보가 확인(수신)될 때마다 자동 호출되는 메소드
        @Override
        public void onLocationChanged(Location location) {
            nowLocation = location;
            Double latitude = location.getLatitude();//위도
            Double longitude = location.getLongitude();//경도

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;

            Log.i("GPSListener", msg);

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIG_LOGOUT){
            if(resultCode == RESULT_OK){
                if(mGoogleApiClient.isConnected()){
                    signout();
                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }else if(resultCode == 2002){
                isGPS = data.getBooleanExtra("gps", false);
                Log.d("isGPS", "GPS : " + isGPS );

                if(isGPS){
                    startLocationService();
                }else{
                    Log.d("GPS", "GPS END");

                }
            }
        }
    }

    private void signout(){
        mGoogleApiClient.disconnect();
    }
}
