package com.foo.garosero;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foo.garosero.data.UserData;
import com.foo.garosero.ui.application.ApplicationFragment;
import com.foo.garosero.ui.home.HomeFragment;
import com.foo.garosero.ui.home.HomeViewModel;
import com.foo.garosero.ui.information.InformationFragment;
import com.foo.garosero.ui.visualization.VisualizationFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton menuButton;

    // db에서 값을 받아오고 표시하기 위함
    DatabaseReference database;
    String uid;


    private long lastTimeBackPressed; //뒤로가기 버튼이 클릭된 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        menuButton = findViewById(R.id.memu);

        // nav header의 이름, 나무를 유저db에서 불러옴
        getUserData();

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
                        replaceFragment(new ApplicationFragment());
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
                    case R.id.item_logout: //logout 후 login activity로 redirect
                        FirebaseAuth.getInstance().signOut();

                        // 싱글턴 객체 초기화
                        HomeViewModel.setDatabase(null);
                        HomeViewModel.setUserData(null);

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

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_main, fragment).commit();
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

    public void getUserData(){
        View header = navigationView.getHeaderView(0);
        TextView tv_name = header.findViewById(R.id.tv_name);
        TextView tv_info = header.findViewById(R.id.tv_info);

        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    if (ud.getKind().endsWith("나무")) {
                        tv_name.setText(ud.getKind());
                    } else {
                        tv_name.setText("");
                    }
                    tv_info.setText(ud.getName());
                } else {
                    Log.e("MainActivity", "no data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MainActivity", "onCancelled", databaseError.toException());
            }
        };
        database.addListenerForSingleValueEvent(postListener);
    }

}