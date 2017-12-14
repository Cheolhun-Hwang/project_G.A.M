package com.hchooney.qewqs.gam;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.hchooney.qewqs.gam.Net.SendPostReq;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.State.RUNNABLE;

public class MainActivity extends AppCompatActivity {
    private final static int SIG_LOGOUT = 4004;

    private static boolean isGPS;
    private double nowLon;
    private double nowLat;
    private String nowSpot;
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

    /*
     * DTO를 따로 두지 않으며 MainActivity 자체가 DTO 역할을 하도록 함.
     * 좋지 않은 방식이나 빠른 개발을 위해 진행.
    */
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

    private ImageButton Map;
    private ImageButton Gift;
    private ImageButton Setting;

    private boolean ishomeandmap; //true : home, false : map

    private netWaitDailog netWaitDailog;

    private Handler handler;

    Thread data = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                SetData();
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            netWaitDailog.dismiss();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");
        if(isGPS){
            startLocationService();
        }
        data.start();


        if(user.getUnickname().equals("null")){
            Toast.makeText(getApplicationContext(), "환영합니다. "+ user.getUname()+" !!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "환영합니다. "+ user.getUnickname()+" !!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init(){
        if(getIntent().getStringExtra("nick") != null){
            user.setUnickname(getIntent().getStringExtra("nick").toString());
        }
        isGPS = true;
        gpsListener = null;

        if(isGPS){
            startLocationService();
        }else{
            Log.d("GPS", "GPS END");
        }

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
                    MainFragment fragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lat", nowLat);
                    bundle.putDouble("lon", nowLon);
                    fragment.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.Main_container, fragment).commit();
                    Map.setImageResource(R.drawable.ic_map_24dp);
                    ishomeandmap = false;
                }else{
                    MapFragment fragment = new MapFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lat", nowLat);
                    bundle.putDouble("lon", nowLon);
                    fragment.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.Main_container, fragment).commit();
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
                    MainFragment mainFragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lat", nowLat);
                    bundle.putDouble("lon", nowLon);
                    mainFragment.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.Main_container, mainFragment).commit();
                }
                return true;
            }
        });

    }

    private void SetData(){
        if(nowLon == 0.0){
            //최초 값 지속시
            nowLat = 37.450933;
            nowLon =  127.127045;
        }

        //notify
        notifylist = new ArrayList<NotifyItem>();
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
        guidelist = new ArrayList<GuideItem>();
        String res_guide = new SendGet("search/guide", ("?dist=2&gpsx="+nowLon+"&gpsy="+nowLat)).SendGet();
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
                item.setGpsx(Double.parseDouble(obj.get("GGPSX").toString()));
                item.setGpsy(Double.parseDouble(obj.get("GGPSY").toString()));
                item.setSpot(obj.get("GWHERE").toString());
                guidelist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //event
        eventlist = new ArrayList<EventItem>();
        String res_event = new SendGet("search/event", ("?dist=2&gpsx="+nowLon+"&gpsy="+nowLat)).SendGet();
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
                item.seteGpsx(Double.parseDouble(obj.get("EGPSX").toString()));
                item.seteGpsy(Double.parseDouble(obj.get("EGPSY").toString()));
                item.seteNum(Integer.parseInt(obj.get("ENUM").toString()));
                item.setePhoto(obj.get("GPHOTO").toString());
                item.seteSpot(obj.get("EWHERE").toString());
                eventlist.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //coupon
        couponlist = new ArrayList<CouponItem>();
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
        Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다.", Toast.LENGTH_SHORT).show();
        // 위치 관리자 객체 참조
        gmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 위치 리스너 객체 생성
        gpsListener = new GPSListener();
        long minTime = 24000;//GPS정보 전달 시간 지정 - 4분마다 위치정보 전달
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
                nowLat = lastLocation.getLatitude();
                nowLon = lastLocation.getLongitude();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
    }

    // 위치 리스너 클래스 정의
    private class GPSListener implements LocationListener {
        //위치 정보가 확인(수신)될 때마다 자동 호출되는 메소드
        @Override
        public void onLocationChanged(Location location) {
            nowLat = location.getLatitude();
            nowLon = location.getLongitude();

            Log.e("GPS", "GPS Listener isGPS : " + isGPS);
            if(isGPS) {
                String msg = "Latitude : " + location.getLatitude() + "\nLongitude:" + location.getLongitude();
                searchLocation(location.getLatitude(), location.getLongitude());
                Log.i("GPSListener", msg);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void searchLocation(double lat, double lon){
        List<Address> addressList = null;
        try{
            Geocoder gc = new Geocoder(getApplicationContext(), Locale.KOREAN);
            addressList = gc.getFromLocation(lat, lon, 1);
            if(addressList != null){
                for(int i = 0; i<addressList.size();i++){
                    Address outAddr = addressList.get(i);
                    int addrCount = outAddr.getMaxAddressLineIndex()+1;
                    StringBuffer outAddrStr = new StringBuffer();

                    for(int k=0;k<addrCount;k++){
                        nowSpot = outAddr.getAddressLine(k);
                        Log.d("Location", "Location Name : " + nowSpot);

                    }
                }
            }

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
                        Date currentTime = new Date( );
                        String dTime = formatter.format ( currentTime );
                        Log.d("Time", "Current Time : " + dTime);

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("uid", user.getUid());
                        postDataParams.put("spot", nowSpot);
                        postDataParams.put("date", dTime);
                        postDataParams.put("gpsy", nowLat);
                        postDataParams.put("gpsx", nowLon);
                        Log.e("params",postDataParams.toString());
                        new SendPostReq("gps/add", postDataParams).post();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    netWaitDailog.dismiss();
                }
            });

            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIG_LOGOUT){
            //로그아웃 요청 시 응답에 따른 처리
            if(resultCode == RESULT_OK){
                if(mGoogleApiClient.isConnected()){
                    signout();
                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }else if(resultCode == 2002){
                //GPS 값 전송 설정값에 따른 처리
                boolean isGPS_setting =(boolean) data.getBooleanExtra("gps", false);
                user=(Account) data.getSerializableExtra("user");
                Log.d("isGPS", "GPS : " + isGPS );

                if(isGPS_setting){
                    if((isGPS!=isGPS_setting)){
                        Log.e("GPS", "isGPS : " + isGPS);
                        startLocationService();
                    }
                }else{
                    Log.d("GPS", "GPS END");
                    gmanager.removeUpdates(gpsListener);
                    gpsListener=null;
                    gmanager=null;
                }
                isGPS = isGPS_setting;
            }
        }
    }

    private void signout(){
        mGoogleApiClient.disconnect();
    }
}
