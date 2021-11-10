package com.foo.garosero.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;

public class HomeFragment extends Fragment {

    View root;

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

        // tab button
        root.findViewById(R.id.button_tree_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내 나무 정보
                replaceFragment(new TreeInfoFragment());
            }
        });
        root.findViewById(R.id.button_tree_management).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내 나무 관리
                replaceFragment(new TreeManagementFragment());
            }
        });
        root.findViewById(R.id.button_tree_tip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 나무 관리 tip
                replaceFragment(new TreeTipFragment());
            }
        });

    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mfragment_home, fragment).commit();
    }
}