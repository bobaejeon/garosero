package com.foo.garosero.ui.application;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeApiData;
import com.foo.garosero.mviewmodel.MapViewModel;
import com.foo.garosero.myUtil.ApiHelper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    static public NaverMap naverMap;
    static public Spinner spinner;

    MyThread thread = new MyThread();

    ArrayAdapter<CharSequence> adapter;

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
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
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

            Integer index = msg.what;
            String gu = (String) adapter.getItem(index);

            // 공공 데이터 가져오기
            Thread getDataThread = new Thread() {
                @Override
                public void run() {
                    String key = getString(R.string.SEOUL_GAROSU_API_KEY);
                    // 100개만 표시
                    ApiHelper.getApiData(key, 0, 100, gu);
                }
            };

            try {
                // 지연
                getDataThread.start();
                getDataThread.join();

                // 카메라 초기 위치 설정
                ArrayList<TreeApiData> apiDataList = MapViewModel.treeApiDataList;
                LatLng initialPosition = new LatLng(
                        apiDataList.get(0).getLAT(), apiDataList.get(0).getLNG());
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
                naverMap.moveCamera(cameraUpdate);

                // 지금 활성화 된 마커 지우기
                for (Marker marker : MapViewModel.markerList) {
                    marker.setMap(null);
                }

                // 마커 표시하기
                MapViewModel.markerList.clear();
                for (TreeApiData apiData : apiDataList) {
                    Marker marker = new Marker();
                    marker.setPosition(new LatLng(apiData.getLAT(), apiData.getLNG()));
                    marker.setMap(naverMap);

                    MapViewModel.markerList.add(marker);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}