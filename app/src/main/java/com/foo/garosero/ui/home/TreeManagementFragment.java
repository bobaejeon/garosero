package com.foo.garosero.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.foo.garosero.ui.home.sub.EmptyFragment;
import com.foo.garosero.ui.home.sub.TodoFragment;
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

public class TreeManagementFragment extends Fragment {
    View root;
    ImageView treeCharacter;

    // db에서 값을 받아오고 표시하기 위함
    DatabaseReference database;
    String uid;
    TextView tv_tree_name, tv_how_long;

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

        // init view - BB (데이터 전달받아 표시하기 시작)
        tv_tree_name = root.findViewById(R.id.tv_tree_name);
        tv_how_long = root.findViewById(R.id.tv_how_long);

        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/"+uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    tv_tree_name.setText(ud.getTree_name());

                    if(!ud.getStart_date().equals("")){
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long calDateDays = 0;

                        try {
                            Date currDate = calendar.getTime();
                            Date lastDate = sdf.parse(ud.getStart_date());
                            long calDate = currDate.getTime() - lastDate.getTime();
                            calDateDays = calDate / ( 24*60*60*1000);
                            String str = ud.getName()+"님과 함께한지 "+Math.abs(calDateDays)+"일째";
                            tv_how_long.setText(str);
                        } catch (ParseException e) {
                            Log.e("TreeManagementFrag",e.toString());
                        }
                    } else {
                        tv_how_long.setText("첫 나무를 입양해주세요");
                    }

                } else {
                    Log.e("MainActivity", "no data");
                }

                // 나무정보 있을때 OR 나무 정보 없을때
                initView();
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

    private void initView() {
        if (tv_tree_name.getText().toString().equals(getString(R.string.noTree))) {
            // 1. 나무 정보 없을때
            setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
            replaceFragment(new EmptyFragment());
        }
        else {
            // 2. 나무 정보 있을 때
            setBackgroundImageview(treeCharacter, R.drawable.mid_tree);
            replaceFragment(new TodoFragment());
        }
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