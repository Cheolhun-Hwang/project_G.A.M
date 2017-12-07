package com.hchooney.qewqs.gam.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hchooney.qewqs.gam.CustomItem.SpinnerAdapter01;
import com.hchooney.qewqs.gam.DetailEventActivity;
import com.hchooney.qewqs.gam.DetailGuideActivity;
import com.hchooney.qewqs.gam.Dialog.netWaitDailog;
import com.hchooney.qewqs.gam.MainActivity;
import com.hchooney.qewqs.gam.Net.SendGet;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private View v;
    private static ArrayList<GuideItem> guidelist;
    private static ArrayList<EventItem> eventlist;


    public GoogleMap mMap;
    private SupportMapFragment supportMapFragment;

    private Spinner distance;
    private Spinner filter;
    private SpinnerAdapter01 filterAdapter;
    private SpinnerAdapter01 distanceAdapter;

    private String beforeFilter;
    private String SearchDistance;

    private LinearLayout detail_guid_layout;
    private LinearLayout detail_event_layout;

    private ImageButton detail_guid_close;
    private ImageButton detail_event_close;

    private ImageView detail_guid_imageview;
    private TextView detail_guid_title;
    private TextView detail_guide_distance;
    private ImageButton detail_guide_audio_play;
    private ImageButton detail_guide_audio_stop;
    private SeekBar detail_guide_audio_progress;

    private ImageView detail_event_imageview;
    private TextView detail_event_profit;
    private TextView detail_event_deadline;
    private TextView detail_event_num;
    private TextView detail_event_cordination;

    private boolean isguidlayout;
    private boolean iseventlayout;

    private MediaPlayer mp;
    private boolean isprepared = false;
    private boolean isplay = false;


    private int guid_position;
    private int event_position;

    private int pausePosition;

    private Handler handler;

    private com.hchooney.qewqs.gam.Dialog.netWaitDailog netWaitDailog;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_map, container, false);

        init();
        event();

        return v;
    }

    private void init() {
        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setMessage("서버에서 서비스 정보를 받아오는 중입니다.");
        netWaitDailog.setTitle("정보 받아 오는 중");

        guidelist = ((MainActivity) getActivity()).getGuidelist();
        eventlist = ((MainActivity) getActivity()).getEventlist();
        SearchDistance = "1";
        pausePosition=0;

        filter = (Spinner) v.findViewById(R.id.MapSpinner_Filter);
        filterAdapter = new SpinnerAdapter01(getContext(), new ArrayList<String>(Arrays.asList("전  체", "가이드", "이벤트")));
        filter.setAdapter(filterAdapter);
        distance = (Spinner) v.findViewById(R.id.MapSpinner_Distance);
        distanceAdapter = new SpinnerAdapter01(getContext(), new ArrayList<>(Arrays.asList("1 km", "2.5 km", "5 km")));
        distance.setAdapter(distanceAdapter);

        beforeFilter = filter.getSelectedItem().toString();

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.Map_MapFragmetview);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.Map_Mapview, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        });

        detail_event_layout = (LinearLayout) v.findViewById(R.id.map_detail_event_info);
        detail_event_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailEventActivity.class);
                intent.putExtra("user", ((MainActivity)getActivity()).getUser());
                intent.putExtra("Event", eventlist.get(event_position));
                startActivity(intent);
            }
        });
        detail_guid_layout = (LinearLayout) v.findViewById(R.id.map_detail_guid_info);
        detail_guid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailGuideActivity.class);
                intent.putExtra("guideitem", guidelist.get(guid_position));
                startActivity(intent);
            }
        });

        iseventlayout = false;
        isguidlayout = false;

        detail_event_close = (ImageButton) v.findViewById(R.id.map_event_closeBTN);
        detail_event_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_event_layout.setVisibility(View.GONE);
            }
        });
        detail_event_cordination = (TextView) v.findViewById(R.id.map_event_Cordination);
        detail_event_imageview = (ImageView) v.findViewById(R.id.map_event_imageview);
        detail_event_deadline = (TextView)v.findViewById(R.id.map_event_limitdate);
        detail_event_num = (TextView) v.findViewById(R.id.map_event_Num);
        detail_event_profit = (TextView)v.findViewById(R.id.map_event_profit);

        detail_guid_close = (ImageButton) v.findViewById(R.id.map_guid_closeBTN);
        detail_guid_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_guid_layout.setVisibility(View.GONE);
            }
        });

        detail_guid_title = (TextView)v.findViewById(R.id.map_guid_title);
        detail_guid_imageview = (ImageView) v.findViewById(R.id.map_guid_imaview);
        detail_guide_distance = (TextView) v.findViewById(R.id.map_guid_distance);
        detail_guide_audio_play = (ImageButton)v.findViewById(R.id.map_guid_AudioPlayAndPause);
        detail_guide_audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isprepared){
                    if(isplay){
                        mp.pause();
                        isplay = false;
                        detail_guide_audio_play.setImageResource(R.drawable.ic_play_arrow);
                    }else{
                        mp.start();
                        isplay = true;
                        detail_guide_audio_play.setImageResource(R.drawable.ic_pause);
                    }
                }
            }
        });
        detail_guide_audio_stop = (ImageButton) v.findViewById(R.id.map_guid_AudioStop);
        detail_guide_audio_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                mp.reset();
                if(isplay){
                    isplay = false;
                    detail_guide_audio_play.setImageResource(R.drawable.ic_play_arrow);
                }
            }
        });
        detail_guide_audio_progress = (SeekBar) v.findViewById(R.id.map_guid_AudioSeek);
        detail_guide_audio_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getMax() == i){

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mp.pause();
                isplay = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isplay = true;
                mp.seekTo(seekBar.getProgress());
                mp.start();
            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    setMarkersOnMap(filter.getSelectedItemPosition());
                }
                return true;
            }
        });

    }

    private void event(){
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Map Filter", "이전 : " + beforeFilter + " / 선택된 값 : " + filter.getSelectedItem());
                setMarkersOnMap(position);
                beforeFilter = filter.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Map Filter", "이전 : " + SearchDistance + " / 선택된 값 : " + distance.getSelectedItem());
                SearchDistance = distance.getSelectedItem().toString().replace(" km", "");


                netWaitDailog.show(getActivity().getSupportFragmentManager(), "Net Dailog");

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            guidelist.clear();
                            eventlist.clear();
                            loadGuide(SearchDistance);
                            loadEvent(SearchDistance);

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadGuide(String dist){
        Location loc = ((MainActivity)getActivity()).getNowLocation();
        String res_guide = new SendGet("search/guide", ("?dist="+ dist + "&gpsx=" + loc.getLongitude()+ "&gpsy="+loc.getLatitude())).SendGet();
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
    }

    private void loadEvent(String dist){
        Location loc = ((MainActivity)getActivity()).getNowLocation();
        String res_event = new SendGet("search/event", ("?dist="+dist + "&gpsx=" + loc.getLongitude()+ "&gpsy="+loc.getLatitude())).SendGet();
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
    }


    private void setMarkersOnMap(int type){

        mMap.clear();

        switch (type){
            case 0:
                setMarkersTypeOne();
                break;
            case 1:
                setMarkersTypeTwo();
                break;
            case 2:
                setMarkerTypeThree();
                break;
            default:
                break;
        }

    }

    private void setMarkersTypeOne(){
        for(int marker_count = 0 ; marker_count < guidelist.size() ; marker_count ++){
            GuideItem item = guidelist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.getGpsy(), item.getGpsx()), 14));
            }

            Marker guide = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getGpsy(), item.getGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.guidemarker_48))
                    .title((marker_count) +"")
                    .zIndex((float)marker_count)
            );

            guide.setTag(0);

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            //this.mMap.setOnMarkerClickListener(markerClickListener);
        }
        for(int marker_count = 0 ; marker_count < eventlist.size() ; marker_count ++){
            EventItem item = eventlist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.geteGpsy(), item.geteGpsx()), 14));
            }

            Marker event = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.geteGpsy(), item.geteGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.eventmarker_48))
                    .title((marker_count) +"")
                    .zIndex((float)marker_count)
            );

            event.setTag(1);


            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            this.mMap.setOnMarkerClickListener(this);
        }
    }

    private void setMarkersTypeTwo(){
        for(int marker_count = 0 ; marker_count < guidelist.size() ; marker_count ++){
            GuideItem item = guidelist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.getGpsy(), item.getGpsx()), 14));
            }

             Marker guide = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getGpsy(), item.getGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.guidemarker_48))
                    .title((marker_count) +"")
                    .zIndex((float)marker_count)
            );


            guide.setTag(0);

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            this.mMap.setOnMarkerClickListener(this);
        }
    }

    private void setMarkerTypeThree(){
        for(int marker_count = 0 ; marker_count < eventlist.size() ; marker_count ++){
            EventItem item = eventlist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.geteGpsy(), item.geteGpsx()), 14));
            }

            Marker event = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.geteGpsy(), item.geteGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.eventmarker_48))
                    .title((marker_count)+"")
                    .zIndex((float)marker_count)
            );

            event.setTag(1);

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            this.mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mp!=null){
            mp.stop();
            if(isplay){
                detail_guide_audio_play.setImageResource(R.drawable.ic_play_arrow);
                isplay = false;
            }
        }

        Log.d("Map Marker ID", "Marker ID Test : "+marker.getId()+ " / marker Title : " + marker.getTitle());

        if((int)marker.getTag() == 0){
            if(iseventlayout){
                detail_event_layout.setVisibility(View.GONE);
                iseventlayout = false;
            }
            detail_guid_layout.setVisibility(View.VISIBLE);
            guid_position = Integer.parseInt(marker.getTitle());
            setGuideLayout();
            isguidlayout = true;
        }else{
            if(isguidlayout){
                detail_guid_layout.setVisibility(View.GONE);
                isguidlayout = false;
            }
            detail_event_layout.setVisibility(View.VISIBLE);
            event_position = Integer.parseInt(marker.getTitle());
            setEventLayout();
            iseventlayout = true;

        }


        return true;
    }

    private void setGuideLayout(){

        GuideItem item = guidelist.get(guid_position);
        detail_guid_title.setText(item.getSpot());

        String temp[] = item.getgAudio().split("/");
        String url = temp[temp.length-1];

        Picasso.with(getContext()).load("http://203.249.127.32:64001/mobile/search/guide/images?gid="+item.getGid()).into(detail_guid_imageview);

        mp = new MediaPlayer();
        try {
            mp.setDataSource(getContext(), Uri.parse("http://203.249.127.32:64001/mobile/search/guide/audio?file="+url.replaceAll(" ", "%20")));
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.setLooping(false);
        mp.prepareAsync();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isprepared = true;
            }
        });


    }

    private void setEventLayout(){
        EventItem item = eventlist.get(event_position);

        detail_event_num.setText("인원 : " + item.geteNum());
        detail_event_deadline.setText("기간 : ~ "+item.geteLimitDate());
        detail_event_cordination.setText("조건 : " + item.geteCordination());
        detail_event_profit.setText("혜택 : " + item.geteProfit());

        Picasso.with(getContext()).load("http://203.249.127.32:64001/mobile/search/event/images?eid="+item.getEid()).into(detail_event_imageview);


    }


    @Override
    public void onPause() {
        super.onPause();
        isplay =false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.release();
            mp=null;
        }
    }




}
