package com.foo.garosero.ui.home.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.myUtil.DiaryHelper;

import java.util.ArrayList;

public class MyDiaryFragment extends Fragment {
    View root;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_diary, container, false);
        recyclerView = root.findViewById(R.id.diary_RecyclerView);

        Button button = root.findViewById(R.id.temp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("report_mode","create");
                startActivity(intent);
            }
        });

        // read diary from server
        DiaryHelper diaryHelper = new DiaryHelper();
        diaryHelper.readDiaryFromFireBase();

        // live data
        final Observer<ArrayList<DiaryData>> diaryDataObserver = new Observer<ArrayList<DiaryData>>() {
            @Override
            public void onChanged(ArrayList<DiaryData> newDates) {
                // 뷰 띄우기
                initRecyclerView(newDates);
            }
        };
        DiaryViewModel.getDiaryDataList().observe(getActivity(), diaryDataObserver);
        return root;
    }

    public void initRecyclerView(ArrayList<DiaryData> myData){
        // Recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DiaryAdapter adapter = new DiaryAdapter(myData);
        recyclerView.setAdapter(adapter);
    }
}