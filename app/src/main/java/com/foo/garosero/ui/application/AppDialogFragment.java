package com.foo.garosero.ui.application;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
/* todo fragment_dialog 레이아웃 수정 필요 -> 리사이클뷰로 구현 */
public class AppDialogFragment extends DialogFragment implements View.OnClickListener {
    TableRow row1, row2, row3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        row1 = view.findViewById(R.id.row1);
        row2 = view.findViewById(R.id.row2);
        row3 = view.findViewById(R.id.row3);

        row1.setOnClickListener(this);
        row2.setOnClickListener(this);
        row3.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(),"신청하였습니다", Toast.LENGTH_LONG).show();
        getDialog().dismiss();
        /* todo 선택한 데이터 db에 업로드하기 */
//        ApplicationFragment fragment =(ApplicationFragment) getActivity().getFragmentManager().findFragmentByTag("ApplicationFragment");
//        if (row1 == view) {
//            fragment.et_road.setText("row1");
//        } else if (row2 == view) {
//            fragment.et_road.setText("row2");
//        } else if (row3 == view) {
//
//        }
    }
}