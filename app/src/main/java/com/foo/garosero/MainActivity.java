package com.foo.garosero;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.ServerHelper;
import com.foo.garosero.myUtil.SpeechHelper;
import com.foo.garosero.ui.application.ApplicationMapActivity;
import com.foo.garosero.ui.home.HomeFragment;
import com.foo.garosero.ui.information.InformationFragment;
import com.foo.garosero.ui.treetip.TreeTipFragment;
import com.foo.garosero.ui.visualization.VisualizationFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton bt_menu, bt_qrcode;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;

    public UserInfo ud;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        bt_menu = findViewById(R.id.memu);
        bt_qrcode = findViewById(R.id.main_ImageButton_qrcode);
        lottieAnimationView = findViewById(R.id.main_lottie);

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                // 유저 정보 가져오기
                ud = HomeViewModel.getUserInfo().getValue();
                // 뷰 로딩
                initView();
            }
        };
        HomeViewModel.getUserInfo().observe(MainActivity.this, userDataObserver);

        // 프래그먼트 초기설정
        /* initial activity에서 선택한 나무 인덱스를 홈 프래그먼트 전달 */
        HomeFragment homeFrag = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index",getIntent().getIntExtra("index", 0));
        homeFrag.setArguments(bundle);

        replaceFragment(homeFrag);

        // QR code Icon click 시
        bt_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator qrScan = new IntentIntegrator(MainActivity.this);
                qrScan.setOrientationLocked(false); // 가로 /세로 변경 가능
                qrScan.initiateScan();
            }
        });

        // 메뉴 아이콘 클릭 시
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 드로어블 Navigation Item 클릭 시
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_application:
                        // go to Application map Activity -> 5그루가 있으면 더 이상 신청 안되도록
                        if(ud.getTreeList().size() == 5){
                            Toast.makeText(MainActivity.this,"다섯 그루까지만 신청 가능합니다.",Toast.LENGTH_LONG);
                            return false;
                        }
                        Intent intent = new Intent(MainActivity.this, ApplicationMapActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.item_information:
                        replaceFragment(new InformationFragment());
                        break;
                    case R.id.item_visualization:
                        replaceFragment(new VisualizationFragment());
                        break;
                    case R.id.item_treeTip:
                        replaceFragment(new TreeTipFragment());
                        break;
                    case R.id.item_logout:
                        //logout 후 login activity로 redirect
                        FirebaseAuth.getInstance().signOut();

                        // 싱글턴 객체 초기화
                        UserInfo empty_ud = new UserInfo();
                        HomeViewModel.setUserInfo(empty_ud);

                        // intent
                        Toast.makeText(MainActivity.this, "로그아웃되었습니다.", Toast.LENGTH_LONG);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.END);
                return false;
            }
        });

        // SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ServerHelper.initServer();
                swipeRefreshLayout.setRefreshing(false); // 새로고침 완료
            }
        });
    }

    // QR code Reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message = "";
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                String answer = result.getContents().trim();
                ArrayList<TreeInfo> treeList = ud.getTreeList();

                for (TreeInfo tree : treeList){
                    String treeId = tree.getTree_id().trim();
                    if (treeId.equals(answer)){
                        message = ud.getName()+"님의 "+tree.getTree_name()+"입니다";
                        SpeechHelper speechHelper = new SpeechHelper(getApplicationContext(), lottieAnimationView, tts);
                        speechHelper.speak(message);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                message = ud.getName()+"님의 나무가 아닙니다";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) { // drawer가 열려 있을 때
            drawerLayout.closeDrawer(GravityCompat.END);
        } else { // drawer가 닫혀있다면 메인페이지(일러스트)로 이동
            Intent intent = new Intent(MainActivity.this, InitialActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void initView(){
        View header = navigationView.getHeaderView(0);
        TextView tv_name = header.findViewById(R.id.tv_name);
        TextView tv_info = header.findViewById(R.id.tv_info);

        ud = HomeViewModel.getUserInfo().getValue();
        Log.d("MainActivity", ud.toString());
        tv_name.setText(ud.getName());
        tv_info.setText("");
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_main, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}