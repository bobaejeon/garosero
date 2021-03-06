package com.foo.garosero;

import android.Manifest;
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
import com.foo.garosero.ui.application.ApplicationActivity;
import com.foo.garosero.ui.home.HomeFragment;
import com.foo.garosero.ui.information.InformationFragment;
import com.foo.garosero.ui.treetip.TreeTipFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton bt_menu, bt_qrcode, bt_all;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;

    DatabaseReference ref;
    FirebaseAuth firebaseAuth;
    String uid;

    public UserInfo ud;
    private TextToSpeech tts;

    private long lastTimeBackPressed; //???????????? ????????? ????????? ??????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        bt_menu = findViewById(R.id.memu);
        bt_qrcode = findViewById(R.id.main_ImageButton_qrcode);
        lottieAnimationView = findViewById(R.id.main_lottie);

        bt_all = findViewById(R.id.main_ImageButton_all); // ????????????

        // ?????? ??????
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("???????????? ???????????? ???????????? ?????? ????????? ????????????")
                .setDeniedMessage("[??????] > [??????] ?????? ????????? ????????? ??? ?????????.")
                .setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET})
                .check();

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                // ?????? ?????? ????????????
                ud = HomeViewModel.getUserInfo().getValue();
                // ??? ??????
                initView();
            }
        };
        HomeViewModel.getUserInfo().observe(MainActivity.this, userDataObserver);

        // ??????????????? ????????????
        /* initial activity?????? ????????? ?????? ???????????? ??? ??????????????? ?????? */
        HomeFragment homeFrag = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index",getIntent().getIntExtra("index", 0));
        homeFrag.setArguments(bundle);

        replaceFragment(homeFrag);

        // ???????????? click
        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InitialActivity.class));
            }
        });

        // QR code Icon click ???
        bt_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator qrScan = new IntentIntegrator(MainActivity.this);
                qrScan.setOrientationLocked(false); // ?????? /?????? ?????? ??????
                qrScan.initiateScan();
            }
        });

        // ?????? ????????? ?????? ???
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // ???????????? Navigation Item ?????? ???
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_application:
                        // go to Application map Activity -> 5????????? ????????? ??? ?????? ?????? ????????????
                        if(ud.getTreeList().size() == 5){
                            Toast.makeText(MainActivity.this,"?????? ??????????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.item_information:
                        replaceFragment(new InformationFragment());
                        break;
                    case R.id.item_treeTip:
                        replaceFragment(new TreeTipFragment());
                        break;
                    case R.id.item_logout:
                        //logout ??? login activity??? redirect
                        FirebaseAuth.getInstance().signOut();

                        // ????????? ?????? ?????????
                        UserInfo empty_ud = new UserInfo();
                        HomeViewModel.setUserInfo(empty_ud);

                        // token ?????????
                        ref.child("Users").child(uid).child("token").setValue("");

                        // intent
                        Toast.makeText(MainActivity.this, "???????????????????????????.", Toast.LENGTH_SHORT).show();
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
                swipeRefreshLayout.setRefreshing(false); // ???????????? ??????
            }
        });
    }

    // QR code Reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message = "";
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            //qrcode ??? ?????????
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode ????????? ?????????
                String answer = result.getContents().trim();
                ArrayList<TreeInfo> treeList = ud.getTreeList();

                for (TreeInfo tree : treeList){
                    String treeId = tree.getTree_id().trim();
                    if (treeId.equals(answer)){
                        message = ud.getName()+"?????? "+tree.getTree_name()+"?????????";
                        SpeechHelper speechHelper = new SpeechHelper(getApplicationContext(), lottieAnimationView, tts);
                        speechHelper.speak(message);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                message = ud.getName()+"?????? ????????? ????????????";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() { //???????????? ?????? ???
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) { // drawer??? ?????? ?????? ???
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            // ?????? ????????? ??????
            if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
                finishAffinity();           // ?????? ????????????????????? ?????? ??????????????? ??????
                System.runFinalization();   // ????????? ??????
                System.exit(0);       // ????????? ??????????????? ??????
                return;
            }
            Toast.makeText(this, "'??????' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
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