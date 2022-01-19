package com.foo.garosero.ui.home.empty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;
import com.foo.garosero.ui.application.ApplicationActivity;

public class EmptyFragment extends Fragment {
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_empty, container, false);

        // 버튼 클릭시 신청 페이지로 연결
        root.findViewById(R.id.empty_Button_application).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}