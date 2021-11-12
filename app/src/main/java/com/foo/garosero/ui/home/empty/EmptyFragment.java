package com.foo.garosero.ui.home.empty;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.MainActivity;
import com.foo.garosero.R;
import com.foo.garosero.ui.application.ApplicationFragment;
import com.foo.garosero.ui.home.TreeExplainActivity;

public class EmptyFragment extends Fragment {
    View root;

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
        root = inflater.inflate(R.layout.fragment_empty, container, false);

        // 버튼 클릭시 신청 페이지로 연결
        root.findViewById(R.id.empty_Button_application).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(new ApplicationFragment());
            }
        });
        return root;
    }
}