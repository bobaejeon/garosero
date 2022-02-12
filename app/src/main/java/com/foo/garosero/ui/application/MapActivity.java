package com.foo.garosero.ui.application;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.foo.garosero.MainActivity;
import com.foo.garosero.R;
import com.foo.garosero.data.TreeApiData;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.ApiHelper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    static public NaverMap naverMap;
    static public Spinner spinner;
    public TextView tv_message;

    public ArrayList<Marker> markerList = new ArrayList<Marker>();
    public ArrayList<TreeApiData> treeApiDataList; // api data
    static ArrayList<TreeInfo> approveData; // approve data
    static ArrayList<TreeInfo> candidateData; // candidate data

    MyThread thread = new MyThread();
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 초기값
        treeApiDataList = new ArrayList<>();
        approveData = HomeViewModel.getApproveInfo().getValue();
        candidateData = HomeViewModel.getCandidateInfo().getValue();

        // live data
        final Observer<ArrayList<TreeInfo>> approveDataObserver = new Observer<ArrayList<TreeInfo>>() {
            @Override
            public void onChanged(ArrayList<TreeInfo> treeData) {
                approveData = treeData;
            }
        };
        HomeViewModel.getApproveInfo().observe(MapActivity.this, approveDataObserver);

        final Observer<ArrayList<TreeInfo>> candidateDataObserver = new Observer<ArrayList<TreeInfo>>() {
            @Override
            public void onChanged(ArrayList<TreeInfo> treeData) {
                candidateData = treeData;
            }
        };
        HomeViewModel.getCandidateInfo().observe(MapActivity.this, candidateDataObserver);

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
        Message message = Message.obtain();
        message.what = 0;
        thread.handler.sendMessage(message); // 메세지 전송
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

            // 공공 데이터 가져오기
            Integer index = msg.what;
            String GU_NM = (String) adapter.getItem(index);
            Thread getDataThread = new Thread() {
                @Override
                public void run() {
                    String key = getString(R.string.SEOUL_GAROSU_API_KEY);
                    ApiHelper apiHelper = new ApiHelper();
                    treeApiDataList = apiHelper.getApiData(key, GU_NM);
                }
            };

            try {
                // 지연
                getDataThread.start();
                getDataThread.join();

                showMarker();

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

            marker.setCaptionText(apiData.getTRE_IDN()); // todo : 확인용
            marker.setIconTintColor(Color.YELLOW);

            // check available for maker color
            // 승인된 마커 표시하기
            if (canNotApply(apiData, approveData)){
                Toast.makeText(getApplicationContext(), "다른 나무를 선택하세요",Toast.LENGTH_SHORT).show();
                marker.setIconTintColor(Color.GRAY);
            }

            // 대기중인 마커 표시하기
            else if (canNotApply(apiData, candidateData)){
                Toast.makeText(getApplicationContext(), "다른 나무를 선택하세요",Toast.LENGTH_SHORT).show();
                marker.setIconTintColor(Color.GREEN);
            }

            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    // 대기중인 나무를 클릭
                    if (canNotApply(apiData, candidateData)){
                        setMessage("심사 중인 나무입니다. 다른 나무를 선택하세요");
                        return false;
                    }

                    // 승인된 나무를 클릭(주인이 있는)
                    if (canNotApply(apiData, approveData)){
                        setMessage("주인이 있는 나무입니다. 다른 나무를 선택하세요");
                        return false;
                    }

                    // 선택 가능한 나무
                    setMessage("");
                    ApplicationActivity.apiData = (TreeApiData) marker.getTag();
                    finish();
                    return true;
                }
            });

            markerList.add(marker);
        }
    }

    private Boolean canNotApply(TreeApiData apiData, ArrayList<TreeInfo> treeInfoArrayList){
        Double id = -1.0;
        try{
            id = Double.valueOf(apiData.getTRE_IDN());
        } catch (Exception e){
            e.printStackTrace();
            Log.d("MAP", apiData.getTRE_IDN());
        }

        for (TreeInfo treeData : treeInfoArrayList) {
            try{
                Integer data_id = Integer.parseInt(treeData.getTree_id());
                if (Math.abs(data_id-id)<1) {
                    return true;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private void setMessage(String message){
        tv_message = findViewById(R.id.map_tv_message);
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_message.setVisibility(View.GONE);
            }
        });

        if (message == ""){
            tv_message.setVisibility(View.GONE);
        } else {
            tv_message.setText(message);
            tv_message.setVisibility(View.VISIBLE);
        }
    }
}