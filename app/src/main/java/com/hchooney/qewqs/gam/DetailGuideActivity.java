package com.hchooney.qewqs.gam;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hchooney.qewqs.gam.RecyclerList.DetailGuide.DetaiGuideItem;
import com.hchooney.qewqs.gam.RecyclerList.DetailGuide.DetailGuideAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailGuideActivity extends AppCompatActivity {
    private final String TAG = "DetailGuideActivity";

    private ImageButton Back;
    private TextView Title;
    private TextView Context;

    private RecyclerView ImageListView;


    //리소스
    private int index;
    private DetaiGuideItem detaiGuideItem;
    private GuideItem guideItem;

    //맵
    public GoogleMap mMap;
    private SupportMapFragment supportMapFragment;

    private MediaPlayer mp;
    private boolean isprepared = false;
    private boolean isplay = false;

    private ImageButton play;
    private ImageButton stop;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_guide);

        init();
        setMap();
        loadData();
        ListSetup();
        SetUI();

    }

    private void init(){
        index = getIntent().getIntExtra("index", -1);
        guideItem = (GuideItem) getIntent().getSerializableExtra("guideitem");
        if(index == -1 || guideItem ==null){
            Toast.makeText(getApplicationContext(), "세부가이드 정보를 받지 못했습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Log.d(TAG, "Guide Information Load OK. guideID : " + this.guideItem.getGid());
        }

        FragmentManager fm = getSupportFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.Map_Mapview);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.Map_Mapview, supportMapFragment).commit();
        }

        Back = (ImageButton) findViewById(R.id.Join_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageListView = (RecyclerView) findViewById(R.id.DetailGuide_Recyclerview);
        ImageListView.setHasFixedSize(true);

        play = (ImageButton) findViewById(R.id.DetailGuide_AudioPlayAndPause);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isprepared){
                    if(isplay){
                        mp.pause();
                        isplay = false;
                        play.setImageResource(R.drawable.ic_play_arrow);
                    }else{
                        mp.start();
                        isplay = true;
                        play.setImageResource(R.drawable.ic_pause);
                    }
                }
            }
        });
        stop = (ImageButton) findViewById(R.id.DetailGuide_AudioStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                mp.reset();
                if(isplay){
                    isplay = false;
                    play.setImageResource(R.drawable.ic_play_arrow);
                }
            }
        });
        seekBar = (SeekBar) findViewById(R.id.DetailGuide_AudioSeek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        String temp[] = guideItem.getgAudio().split("/");
        String url = temp[temp.length-1];

        mp = new MediaPlayer();

        try {
            mp.setDataSource(getApplicationContext(), Uri.parse("http://203.249.127.32:64001/mobile/search/guide/audio?file="+url.replaceAll(" ", "%20")));
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

    private void SetUI(){
        Title = (TextView) findViewById(R.id.DetailGuide_Title);
        Title.setText(guideItem.getSpot());
        Context = (TextView) findViewById(R.id.DetailGuide_GuideContext);
        Context.setText(detaiGuideItem.getContext());
    }

    private void setMap(){
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setScrollGesturesEnabled(false);

                float bitmapDescriptorFactory = 0;
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(guideItem.getGpsy(), guideItem.getGpsx()), 14));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.553186, 126.972013), 15));
                bitmapDescriptorFactory = BitmapDescriptorFactory.HUE_ROSE;
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.553186, 126.972013))
                        .icon(BitmapDescriptorFactory.defaultMarker(bitmapDescriptorFactory))
                        .title(guideItem.getSpot())
                        .zIndex((float) 0)
                );


                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "GPS 권한을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void ListSetup(){
        ImageListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        ImageListView.setAdapter(new DetailGuideAdapter(getApplicationContext(), detaiGuideItem.getImageList()));
    }


    public void loadData(){
        //서버에 GID를 통해 디테일 가이드를 받아야 함.
        // 1. 이미지 리스트
        // 2. 세부 가이드 정보를 받아야 한다.

        detaiGuideItem = new DetaiGuideItem();
        detaiGuideItem.setContext("임시 장소에 대한 상세 가이드 내용입니다.\n" +
                "일반적인 폼 형태는 내부적으로 컨트롤합니다.\n" +
                "최대한 심플하게 구현하여 개발시간을 단축합니다.");
        detaiGuideItem.setImageList(new ArrayList<String>(Arrays.asList("image_URL_1", "image_URL_2", "image_URL_3")));
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
