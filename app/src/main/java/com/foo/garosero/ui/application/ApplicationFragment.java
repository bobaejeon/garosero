package com.foo.garosero.ui.application;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;


public class ApplicationFragment extends Fragment implements View.OnClickListener {
    CheckBox cb_indi, cb_team, cb_school;
    EditText et_name, et_number, et_loc, et_road;
    Button bt_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);
        bt_search = view.findViewById(R.id.bt_search);
        et_road = view.findViewById(R.id.et_road);

        bt_search.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == bt_search){
            // 검색버튼 누르면 dialog 나와서 나무를 선택할 수 있음 -> 선택하면 바로 신청됨
            Log.d("Appfrag", "OpeningDialog");
            AppDialogFragment dialog = new AppDialogFragment();
            dialog.show(getChildFragmentManager(), "AppDialogFragment");

        }
    }

}