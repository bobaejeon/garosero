package com.foo.garosero.ui.home.treemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;
import com.foo.garosero.ui.home.diary.ReportActivity;

public class TodoFragment extends Fragment{
    View root;

    TextView ans1, ans2, ans3;
    ImageView icon1, icon2, icon3;
    Button bt_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =inflater.inflate(R.layout.fragment_todo, container, false);

        /* todo 결과 보고버튼 클릭시 isChecked에 따라 다른 내용을 활동 결과 폼으로 보내기 & 카드뷰를 눌러도 체크, uncheck되도록? */
        bt_submit = root.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("report_mode","create");
                startActivity(intent);
            }
        });
        // init view
//        ans1 = root.findViewById(R.id.treeManagement_TextView_ans1);
//        ans2 = root.findViewById(R.id.treeManagement_TextView_ans2);
//        ans3 = root.findViewById(R.id.treeManagement_TextView_ans3);
//        icon1 = root.findViewById(R.id.treeManagement_imageView_ans1);
//        icon2 = root.findViewById(R.id.treeManagement_imageView_ans2);
//        icon3 = root.findViewById(R.id.treeManagement_imageView_ans3);
//        root.findViewById(R.id.treeManagement_CardView_todo1).setOnClickListener(this);
//        root.findViewById(R.id.treeManagement_CardView_todo2).setOnClickListener(this);
//        root.findViewById(R.id.treeManagement_CardView_todo3).setOnClickListener(this);

        return root;
    }

    // cardView 클릭 이벤트 지정
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.treeManagement_CardView_todo1 :
//                showDialog("나무 물주기", ans1, icon1);
//                break;
//            case R.id.treeManagement_CardView_todo2 :
//                showDialog("낙엽 치우기", ans2, icon2);
//                break;
//            case R.id.treeManagement_CardView_todo3 :
//                showDialog("훼손시설물 신고하기", ans3, icon3);
//                break;
//        }
//    }
//
//    // 다이어로그 띄우기
//    private void showDialog(String title, TextView textView, ImageView imageView){
//        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
//        dlg.setTitle(title);
//        final String[] option = {"양호", "보통", "미흡"};
//
//        dlg.setItems(option, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                textView.setText(option[which]);
//                switch (textView.getText().toString()){
//                    case "양호" :
//                        setBackgroundImageview(imageView, R.drawable.circle_green);
//                        break;
//                    case "보통" :
//                        setBackgroundImageview(imageView, R.drawable.circle_yellow);
//                        break;
//                    case "미흡" :
//                        setBackgroundImageview(imageView, R.drawable.circle_red);
//                        break;
//
//                }
//                Toast.makeText(getActivity(),"응답이 제출되었습니다.",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss(); //다이어로그 없애기
//            }
//        });
//        dlg.show();
//    }
//
//    // 이미지 뷰 채우기
//    private void setBackgroundImageview(ImageView imageView, int source){
//        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), source));
//    }
}