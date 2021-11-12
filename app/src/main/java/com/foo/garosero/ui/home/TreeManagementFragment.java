package com.foo.garosero.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.garosero.R;
import com.foo.garosero.ui.home.empty.EmptyFragment;

public class TreeManagementFragment extends Fragment implements View.OnClickListener {
    View root;

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

        // 나무 정보 없을때 빈화면 출력
        treeCharacter = root.findViewById(R.id.treeManagement_ImageView_treeCharacter);
        setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
        FrameLayout frameLayout = root.findViewById(R.id.treeManagement_FrameLayout);
        replaceFragment(new EmptyFragment());
        // frameLayout.setVisibility(View.GONE);
        return root;
    }

    // cardView 클릭 이벤트 지정
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

    // 다이어로그 띄우기
    private void showDialog(String title, TextView textView, ImageView imageView){
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle(title);
        final String[] option = {"양호", "보통", "미흡"};

        dlg.setItems(option, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(option[which]);
                switch (textView.getText().toString()){
                    case "양호" :
                        setBackgroundImageview(imageView, R.drawable.circle_green);
                        break;
                    case "보통" :
                        setBackgroundImageview(imageView, R.drawable.circle_yellow);
                        break;
                    case "미흡" :
                        setBackgroundImageview(imageView, R.drawable.circle_red);
                        break;

                }
                Toast.makeText(getActivity(),"응답이 제출되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //다이어로그 없애기
            }
        });
        dlg.show();
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