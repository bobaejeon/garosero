package com.foo.garosero.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;

public class TreeTipFragment extends Fragment {
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // page explain text
        HomeViewModel.setExplain("우리 나무를 지키기 위한 팁들!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tree_tip, container, false);

        //
        root.findViewById(R.id.TreeTip_CardView_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TreeExplainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}