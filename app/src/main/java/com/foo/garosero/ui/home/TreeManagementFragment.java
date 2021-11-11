package com.foo.garosero.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TreeManagementFragment extends Fragment implements View.OnClickListener {
    View root;

    // db에서 값을 받아오고 표시하기 위함
    DatabaseReference database;
    String uid;
    TextView tv_tree_name, tv_how_long;

    TextView ans1;
    TextView ans2;
    TextView ans3;
    ImageView icon1;
    ImageView icon2;
    ImageView icon3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel.setExplain("입양한 나무는 어떤 상태일까요?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_management, container, false);


        // init view
        ans1 = root.findViewById(R.id.treeManagement_TextView_ans1);
        ans2 = root.findViewById(R.id.treeManagement_TextView_ans2);
        ans3 = root.findViewById(R.id.treeManagement_TextView_ans3);
        icon1 = root.findViewById(R.id.treeManagement_imageView_ans1);
        icon2 = root.findViewById(R.id.treeManagement_imageView_ans2);
        icon3 = root.findViewById(R.id.treeManagement_imageView_ans3);
        root.findViewById(R.id.treeManagement_CardView_todo1).setOnClickListener(this);
        root.findViewById(R.id.treeManagement_CardView_todo2).setOnClickListener(this);
        root.findViewById(R.id.treeManagement_CardView_todo3).setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.treeManagement_CardView_todo1 :
                showDialog("나무 물주기", ans1, icon1);
                break;
            case R.id.treeManagement_CardView_todo2 :
                showDialog("낙엽 치우기", ans2, icon2);
                break;
            case R.id.treeManagement_CardView_todo3 :
                showDialog("훼손시설물 신고하기", ans3, icon3);
                break;
        }
    }

    private void showDialog(String title, TextView textView, ImageView imageView){
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle(title);
        final String[] option = {"양호", "보통", "미흡"};

        dlg.setSingleChoiceItems(option, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(option[which]);
                switch (textView.getText().toString()){
                    case "양호" :
                        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.circle_green));
                        break;
                    case "보통" :
                        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.circle_yellow));
                        break;
                    case "미흡" :
                        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.circle_red));
                        break;
                }
            }
        });

        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                //토스트 메시지
                Toast.makeText(getActivity(),"응답이 제출되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

}