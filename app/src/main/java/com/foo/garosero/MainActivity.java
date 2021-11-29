package com.foo.garosero;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
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

import com.foo.garosero.myUtil.ServerHelper;
import com.foo.garosero.data.UserData;
import com.foo.garosero.ui.application.ApplicationFragment;
import com.foo.garosero.ui.home.HomeFragment;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.ui.home.diary.ReportActivity;
import com.foo.garosero.ui.treetip.TreeTipFragment;
import com.foo.garosero.ui.information.InformationFragment;
import com.foo.garosero.ui.visualization.VisualizationFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton menuButton;

    private long lastTimeBackPressed; //뒤로가기 버튼이 클릭된 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        menuButton = findViewById(R.id.memu);

        // permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한이 승인되지 않은 경우, 예기치 않은 오류가 발생할 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.INTERNET)
                .check();

        // 서버에서 정보 받아오기
        //ServerHelper.initServer();

        // live data
        final Observer<UserData> userDataObserver = new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                if (userData.name!="") {}
                // 로그인 중이면 뷰 로딩
                // initView();
            }
        };
        HomeViewModel.getUserData().observe(MainActivity.this, userDataObserver);

        // 프래그먼트 초기설정
        replaceFragment(new HomeFragment());

        // 메뉴 아이콘 클릭 시
        menuButton.setOnClickListener(new View.OnClickListener() {
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
                        UserData empty_ud = new UserData();
                        HomeViewModel.setUserData(empty_ud);

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

    /*public void initView(){
        View header = navigationView.getHeaderView(0);
        TextView tv_name = header.findViewById(R.id.tv_name);
        TextView tv_info = header.findViewById(R.id.tv_info);

        UserData ud = HomeViewModel.getUserData().getValue();
        Log.e("MainActivity", ud.toString());
        if (ud.isEmpty()) {
            tv_name.setText("");
        } else {
            //tv_name.setText(ud.getKind());
        }
        //tv_info.setText(ud.getName());
    }*/

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_main, fragment).commit();
    }

}