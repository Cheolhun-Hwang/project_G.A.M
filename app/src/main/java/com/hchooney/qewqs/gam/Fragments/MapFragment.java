package com.hchooney.qewqs.gam.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hchooney.qewqs.gam.CustomItem.SpinnerAdapter01;
import com.hchooney.qewqs.gam.MainActivity;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
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
    private int SearchDistance;

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
        guidelist = ((MainActivity) getActivity()).getGuidelist();
        eventlist = ((MainActivity) getActivity()).getEventlist();
        SearchDistance = 2;

        filter = (Spinner) v.findViewById(R.id.MapSpinner_Filter);
        filterAdapter = new SpinnerAdapter01(getContext(), new ArrayList<String>(Arrays.asList("전  체", "가이드", "이벤트")));
        filter.setAdapter(filterAdapter);
        distance = (Spinner) v.findViewById(R.id.MapSpinner_Distance);
        distanceAdapter = new SpinnerAdapter01(getContext(), new ArrayList<>(Arrays.asList("2 km", "5 km", "10 km", "20 km")));
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
                SearchDistance = Integer.parseInt(distance.getSelectedItem().toString().replace(" km", ""));
                loadList(SearchDistance);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadList(int Search){
        Toast.makeText(getContext(), "다음 반경으로 서버와 통신합니다.\n반경 : " + SearchDistance, Toast.LENGTH_LONG).show();
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

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getGpsy(), item.getGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.guidemarker_48))
                    .title((marker_count+1) +". " +item.getSpot())
                    .zIndex((float)marker_count)
            );

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

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.geteGpsy(), item.geteGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.eventmarker_48))
                    .title((marker_count+1) +". " +item.geteSpot())
                    .zIndex((float)marker_count)
            );

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            //this.mMap.setOnMarkerClickListener(markerClickListener);
        }
    }

    private void setMarkersTypeTwo(){
        for(int marker_count = 0 ; marker_count < guidelist.size() ; marker_count ++){
            GuideItem item = guidelist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.getGpsy(), item.getGpsx()), 14));
            }

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.getGpsy(), item.getGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.guidemarker_48))
                    .title((marker_count+1) +". " +item.getSpot())
                    .zIndex((float)marker_count)
            );

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            //this.mMap.setOnMarkerClickListener(markerClickListener);
        }
    }

    private void setMarkerTypeThree(){
        for(int marker_count = 0 ; marker_count < eventlist.size() ; marker_count ++){
            EventItem item = eventlist.get(marker_count);

            //첫번째 마커 리스트가 먼저 맵의 줌 기준이 된다.
            if (marker_count == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.geteGpsy(), item.geteGpsx()), 14));
            }

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(item.geteGpsy(), item.geteGpsx()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.eventmarker_48))
                    .title((marker_count+1) +". " +item.geteSpot())
                    .zIndex((float)marker_count)
            );

            //정보창 클릭 리스너
            //mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            //마커 클릭 리스너
            //this.mMap.setOnMarkerClickListener(markerClickListener);
        }
    }

}
