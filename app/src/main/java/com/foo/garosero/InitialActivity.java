package com.foo.garosero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.ServerHelper;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {
    private long lastTimeBackPressed; //뒤로가기 버튼이 클릭된 시간

    UserInfo ud;
    Button btn_care, btn_start;
    TextView tv_tree_name, tv_tree_level;
    ImageView iv_tree1, iv_tree2, iv_tree3, iv_tree4, iv_tree5;
    ConstraintLayout iv_background;
    ImageView[] iv_list;
    LinearLayout ll_info;

    int idx; // 선택한 나무의 index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // 서버에서 정보 받아오기
//        ServerHelper.initServer();

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                // 뷰 로딩
                initView();
            }
        };
        HomeViewModel.getUserInfo().observe(InitialActivity.this, userDataObserver);

    }
    public void initView(){
        ud = HomeViewModel.getUserInfo().getValue();
        btn_start = findViewById(R.id.btn_start);
        ll_info = findViewById(R.id.linearLayout_info);
        btn_care = findViewById(R.id.btn_care);
        tv_tree_name = findViewById(R.id.textView_tree_name);
        tv_tree_level = findViewById(R.id.textView_tree_level);

        iv_background = findViewById(R.id.imageView);
        iv_tree1 = findViewById(R.id.imageView_tree1);
        iv_tree2 = findViewById(R.id.imageView_tree2);
        iv_tree3 = findViewById(R.id.imageView_tree3);
        iv_tree4 = findViewById(R.id.imageView_tree4);
        iv_tree5 = findViewById(R.id.imageView_tree5);

        if(ud.isEmpty()){
            btn_start.setVisibility(View.VISIBLE);
            btn_start.setOnClickListener(this);
        }
        else{
            btn_start.setVisibility(View.GONE);
            iv_list = new ImageView[]{iv_tree1,iv_tree2,iv_tree3,iv_tree4,iv_tree5};

            // 나무의 갯수만큼만 표시
            for(int i=0 ; i<ud.getTreeList().size() ; i++){
                iv_list[i].setVisibility(View.VISIBLE);
                iv_list[i].setOnClickListener(this);
            }

            btn_care.setOnClickListener(this);
            iv_background.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btn_start){ // 처음 시작하는 경우
            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v == btn_care){ //선택한 나무의 index를 intent로 전달
            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            intent.putExtra("index", idx);
            startActivity(intent);
            finish();
        }else if(v == iv_tree1){
            setTreeText(0);
        }else if(v == iv_tree2){
            setTreeText(1);
        }else if(v == iv_tree3){
            setTreeText(2);
        }else if(v == iv_tree4){
            setTreeText(3);
        }else if(v == iv_tree5){
            setTreeText(4);
        }else if(v == iv_background){
            ll_info.setVisibility(View.GONE);
        }
    }

    private void setTreeText(int i){
        idx = i;
        ll_info.setVisibility(View.VISIBLE);
        tv_tree_name.setText(ud.getTreeList().get(idx).getTree_name());
        String lev = "Level. "+(ud.getTreeList().get(idx).getXp()/10+1);
        tv_tree_level.setText(lev);
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
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