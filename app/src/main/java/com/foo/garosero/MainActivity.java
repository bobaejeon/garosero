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

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.ServerHelper;
import com.foo.garosero.ui.application.ApplicationFragment;
import com.foo.garosero.ui.home.HomeFragment;
import com.foo.garosero.ui.information.InformationFragment;
import com.foo.garosero.ui.treetip.TreeTipFragment;
import com.foo.garosero.ui.visualization.VisualizationFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.speech.tts.TextToSpeech.ERROR;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton bt_menu, bt_qrcode;

    private long lastTimeBackPressed; //뒤로가기 버튼이 클릭된 시간

    private TextToSpeech tts;
    public UserInfo ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        bt_menu = findViewById(R.id.memu);
        bt_qrcode = findViewById(R.id.main_ImageButton_qrcode);

        // 서버에서 정보 받아오기
        ServerHelper.initServer();

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                if (!userData.getName().equals(""))
                // 로그인 중이면 뷰 로딩
                initView();
            }
        };
        HomeViewModel.getUserInfo().observe(MainActivity.this, userDataObserver);

        // 프래그먼트 초기설정
        replaceFragment(new HomeFragment());

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
                        ApplicationFragment applicationFrag = new ApplicationFragment();
                        replaceFragment(applicationFrag);
                        break;
                    case R.id.item_home:
                        HomeFragment homeFrag = new HomeFragment();
                        replaceFragment(homeFrag);
                        break;
                    case R.id.item_information:
                        InformationFragment informationFrag = new InformationFragment();
                        replaceFragment(informationFrag);
                        break;
                    case R.id.item_visualization:
                        VisualizationFragment visualizationFrag = new VisualizationFragment();
                        replaceFragment(visualizationFrag);
                        break;
                    case R.id.item_treeTip:
                        TreeTipFragment treeTipFragment = new TreeTipFragment();
                        replaceFragment(treeTipFragment);
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
    }

    // QR code Reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        speak(ud.getName()+"님의 "+tree.getTree_name()+"입니다");
                        return;
                    }
                    Toast.makeText(MainActivity.this, ud.getName()+"님의 나무가 아닙니다", Toast.LENGTH_SHORT).show();
                    //Log.e("MainActivity-QR", answer+"/"+treeId);
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) { // drawer가 열려 있을 때
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            // 두번 클릭시 종료
            if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
                finishAffinity();           // 해당 어플리케이션의 루트 액티비티를 종료
                System.runFinalization();   // 쓰레드 종료
                System.exit(0);       // 현재의 액티비티를 종료
                return;
            }
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
        }
    }

    public void initView(){
        View header = navigationView.getHeaderView(0);
        TextView tv_name = header.findViewById(R.id.tv_name);
        TextView tv_info = header.findViewById(R.id.tv_info);

        ud = HomeViewModel.getUserInfo().getValue();
        Log.e("MainActivity", ud.toString());

        // 나무가 여러 그루일 수 있으므로 나무종류는 안쓰는 게 좋겠다?!
//        if (ud.isEmpty()) {
//            tv_name.setText("");
//        } else {
//            tv_name.setText(ud.getKind());
//        }
        tv_name.setText(ud.getName());
        tv_info.setText("");
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_main, fragment).commit();
    }

    private void speak(String text) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR){
                    int result = tts.setLanguage(Locale.KOREA); // 언어 선택
                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "This Language is not supported");
                    }else{
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }else{
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
    }

}