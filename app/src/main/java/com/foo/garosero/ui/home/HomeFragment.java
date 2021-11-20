package com.foo.garosero.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.foo.garosero.R;
import com.foo.garosero.mviewmodel.myViewModel;
import com.foo.garosero.ui.home.child.TreeInfoFragment;
import com.foo.garosero.ui.home.child.TreeManagementFragment;
import com.foo.garosero.ui.home.child.TreeTipFragment;

public class HomeFragment extends Fragment {

    View root;
    myViewModel model;
    TextView home_TextView_pageTitle, home_TextView_explain;
    Button home_Button_treeInfo, home_Button_treeManagement, home_Button_treeTip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        replaceFragment(new TreeInfoFragment());
        // init view
        home_TextView_pageTitle = root.findViewById(R.id.home_TextView_pageTitle);
        home_TextView_explain = root.findViewById(R.id.home_TextView_explain);
        home_Button_treeInfo = root.findViewById(R.id.home_Button_treeInfo);
        home_Button_treeManagement = root.findViewById(R.id.home_Button_treeManagement);
        home_Button_treeTip = root.findViewById(R.id.home_Button_treeTip);

        // LiveData
        model = new ViewModelProvider(this).get(myViewModel.class);
        final Observer<String> titleObserver = new Observer<String>() { // page title
            @Override
            public void onChanged(@Nullable final String newtext) {
                home_TextView_pageTitle.setText(newtext);

                // focus Button
                home_Button_treeInfo.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));
                home_Button_treeManagement.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));
                home_Button_treeTip.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_back));

                switch (newtext){
                    case "내 나무 관리" :
                        home_Button_treeManagement.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
                        break;
                    case "나무 관리 TIP" :
                        home_Button_treeTip.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
                        break;
                    case "내 나무 정보":
                        home_Button_treeInfo.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.button_focus));
                        break;
                }
            }
        };
        model.getPageTitle().observe(getActivity(), titleObserver);

        final Observer<String> explainObserver = new Observer<String>() { // explain
            @Override
            public void onChanged(@Nullable final String newtext) {
                home_TextView_explain.setText(newtext);
            }
        };
        myViewModel.getExplain().observe(getActivity(), explainObserver);

        // tab button : change Fragment
        home_Button_treeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내 나무 정보
                replaceFragment(new TreeInfoFragment());
                model.getPageTitle().setValue(home_Button_treeInfo.getText().toString());
            }
        });
        home_Button_treeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내 나무 관리
                replaceFragment(new TreeManagementFragment());
                model.getPageTitle().setValue(home_Button_treeManagement.getText().toString());
            }
        });
        home_Button_treeTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 나무 관리 tip
                replaceFragment(new TreeTipFragment());
                model.getPageTitle().setValue(home_Button_treeTip.getText().toString());
            }
        });

        // 초기설정
        model.getPageTitle().setValue("내 나무 정보"); //초기값
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_home, fragment).commit();
    }
}