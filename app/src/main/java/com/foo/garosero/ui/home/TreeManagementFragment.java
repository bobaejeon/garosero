package com.foo.garosero.ui.home;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.foo.garosero.ui.home.sub.EmptyFragment;
import com.foo.garosero.ui.home.sub.TodoFragment;

public class TreeManagementFragment extends Fragment {
    View root;

    // db에서 값을 받아오고 표시하기 위함
    DatabaseReference database;
    String uid;
    TextView tv_tree_name, tv_tree_day;

    TextView ans1;
    TextView ans2;
    TextView ans3;
    ImageView icon1;
    ImageView icon2;
    ImageView icon3;
    ImageView treeCharacter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel.setExplain("입양한 나무는 어떤 상태일까요?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_management, container, false);
        treeCharacter = root.findViewById(R.id.treeManagement_ImageView_treeCharacter);
        FrameLayout frameLayout = root.findViewById(R.id.treeManagement_FrameLayout);

        // 데이터 전달받아 표시하기 시작 (BB)
        tv_tree_name = root.findViewById(R.id.treeManagement_TextView_treeName);
        tv_tree_day = root.findViewById(R.id.treeManagement_TextView_treeDay);

        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/"+uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    tv_tree_name.setText(ud.getTree_name());

                    // 1. 나무정보 데이터가 없는 경우
                    if(ud.getStart_date().equals("")){
                        setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
                        replaceFragment(new EmptyFragment());
                    }

                    // 2. 나무정보 데이터가 있는 경우
                    else{
                        setBackgroundImageview(treeCharacter, R.drawable.mid_tree);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long calDateDays = 0;

                        try {
                            Date currDate = calendar.getTime();
                            Date lastDate = sdf.parse(ud.getStart_date());
                            long calDate = currDate.getTime() - lastDate.getTime();
                            calDateDays = calDate / ( 24*60*60*1000);
                            String str = ud.getName()+"님과 함께한지 "+Math.abs(calDateDays)+"일째";
                            tv_tree_day.setText(str);
                            replaceFragment(new TodoFragment());
                        } catch (ParseException e) {
                            Log.e("TreeManagementFrag",e.toString());
                        }
                    }

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
        // 데이터 전달받아 표시하기 끝

        return root;
    }

    // 이미지 뷰 채우기
    private void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), source));
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.treeManagement_FrameLayout, fragment).commit();
    }
}