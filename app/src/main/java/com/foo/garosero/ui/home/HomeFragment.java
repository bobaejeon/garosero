package com.foo.garosero.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foo.garosero.R;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.ui.home.diary.MyDiaryFragment;
import com.foo.garosero.ui.empty.EmptyFragment;
import com.foo.garosero.ui.home.treeinfo.TreeInfoFragment;

public class HomeFragment extends Fragment {

    View root;
    HomeViewModel model;
    Button home_Button_treeInfo, home_Button_quests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* 내 나무 정보 -> main activity에서 받은 argment를 tree info에 전달
           initial activity(나무 일러스트 페이지)에서 선택한 나무 정보를 보여주기 위함 */
        TreeInfoFragment treeInfoFragment = new TreeInfoFragment();
        treeInfoFragment.setArguments(getArguments());
        replaceFragment(treeInfoFragment);

        // init view
        home_Button_treeInfo = root.findViewById(R.id.home_Button_treeInfo);
        home_Button_quests = root.findViewById(R.id.home_Button_quests);

        // LiveData
//        model = new ViewModelProvider(this).get(HomeViewModel.class);
//        final Observer<String> titleObserver = new Observer<String>() { // page title
//            @Override
//            public void onChanged(@Nullable final String newtext) {
//                home_TextView_pageTitle.setText(newtext);
//
//                // focus Button
//                home_Button_treeInfo.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));
//                home_Button_treeManagement.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));
//                home_Button_quests.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));
//
//                switch (newtext){
//                    case "내 나무 관리" :
//                        home_TextView_explain.setText("입양한 나무는 어떤 상태일까요?");
//                        home_Button_treeManagement.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
//                        break;
//                    case "내 기록" :
//                        home_TextView_explain.setText("나무와 어떤 추억을 쌓았을까요?");
//                        home_Button_quests.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
//                        break;
//                    case "내 나무 정보":
//                        home_TextView_explain.setText("입양한 나무의 정보를 확인하세요!");
//                        home_Button_treeInfo.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
//                        break;
//                }
//            }
//        };
//        model.getPageTitle().observe(getActivity(), titleObserver);

        // tab button : change Fragment
        home_Button_treeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new TreeInfoFragment());
//                model.getPageTitle().setValue(home_Button_treeInfo.getText().toString());
            }
        });
        home_Button_quests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                home_Button_quests.setBackgroundResource(R.drawable.button_background);
                if (HomeViewModel.getUserInfo().getValue().isEmpty()){
                    replaceFragment(new EmptyFragment());
                } else{
                    // 내 기록
                    replaceFragment(new MyDiaryFragment());
                }
//                model.getPageTitle().setValue(home_Button_quests.getText().toString());
            }
        });

        // 초기설정
//        model.getPageTitle().setValue("내 나무 정보"); //초기값
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_home, fragment).commit();
    }

}