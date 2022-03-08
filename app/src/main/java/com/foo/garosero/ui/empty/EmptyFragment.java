package com.foo.garosero.ui.empty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;

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
                // todo : change application frag
            }
        });
        return root;
    }
}