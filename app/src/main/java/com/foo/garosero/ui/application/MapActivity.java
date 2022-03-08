package com.foo.garosero.ui.application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeApiData;
import com.foo.garosero.mviewmodel.MapViewModel;
import com.foo.garosero.myUtil.ApiHelper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    static public NaverMap naverMap;
    static public Spinner spinner;

    public static ArrayList<Marker> markerList = new ArrayList<Marker>();
    public static ArrayList<TreeApiData> treeApiDataList = new ArrayList<TreeApiData>();
    public static Marker marker;

    MyThread thread = new MyThread();
    ArrayAdapter<CharSequence> adapter;

    public FusedLocationSource locationSource;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // naver map
        String naver_client_id = getString(R.string.NAVER_CLIENT_ID); // id 가져오기
        NaverMapSdk.getInstance(this).setClient( //id 등록
                new NaverMapSdk.NaverCloudPlatformClient(naver_client_id));

        // 지도 객체
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        // user location
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        // spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.loc_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.map_spinner_loc);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message message = Message.obtain();
                message.what = position;
                thread.handler.sendMessage(message); // 메세지 전송
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // thread
        thread = new MyThread();

        // get tree-taken
        MapViewModel.getTreeDataArrayList();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(locationSource);
        ActivityCompat.requestPermissions(MapActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        marker = new Marker();

        // 현위치 gps 버튼
        UiSettings uiSettings = this.naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);


        Message message = Message.obtain();
        message.what = -1; // current location
        thread.handler.sendMessage(message); // 메세지 전송

        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                marker.setPosition(latLng); // 새로운 마커 표시
                marker.setMap(naverMap);
                Toast.makeText(MapActivity.this, latLng.latitude + ", " + latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // request code와 권한획득 여부 확인
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }else{
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // thread
    class MyThread implements Runnable {
        MyHandler handler = new MyHandler();
        int index = 0;

        @Override
        public void run() {
        }
    }

    // handler
    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (naverMap == null) return;

            Integer index = msg.what;
            Thread getDataThread = new Thread() {
                @Override
                public void run() {
                }
            };

            try {
                // 지연
                getDataThread.start();
                getDataThread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMarker(){
        // 카메라 초기 위치 설정
        ArrayList<TreeApiData> apiDataList = treeApiDataList;
        LatLng initialPosition = new LatLng(
                apiDataList.get(0).getLAT(), apiDataList.get(0).getLNG());
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);

        // 지금 활성화 된 마커 지우기
        for (Marker marker : markerList) {
            marker.setMap(null);
        }

        // 마커 표시하기
        markerList.clear();
        for (TreeApiData apiData : apiDataList) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(apiData.getLAT(), apiData.getLNG()));
            marker.setMap(naverMap);
            marker.setTag(apiData);

            marker.setCaptionText(apiData.getOBJECTID());

            // check available for maker color
            Boolean available = true;
            ArrayList<String> treeTakenArrayList = MapViewModel.getTreeDataArrayList();
            for (String tree_id : treeTakenArrayList) {
                if (tree_id.equals(apiData.getOBJECTID())) {
                    available = false;
                    break;
                }
            }

            if (available==false){
                marker.setIconTintColor(Color.BLUE);
            } else {
                marker.setIconTintColor(Color.RED);
            }

            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    // check available
                    // todo : change
                    Boolean available = true;
                    ArrayList<String> treeTakenArrayList = MapViewModel.getTreeDataArrayList();
                    for (String tree_id : treeTakenArrayList) {
                        if (tree_id.equals(apiData.getOBJECTID())) {
                            available = false;
                            break;
                        }
                    }

                    // finish activity
                    if (available == true){
                        // todo : change
                        //ApplicationActivity.apiData = (TreeApiData) marker.getTag();
                        finish();
                    }

                    return true;
                }
            });

            markerList.add(marker);
        }
    }
}