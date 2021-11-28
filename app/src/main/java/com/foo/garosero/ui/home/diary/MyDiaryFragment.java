package com.foo.garosero.ui.home.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.myUtil.MyDBHelper;

import java.util.ArrayList;


public class MyDiaryFragment extends Fragment {
    View root;
    ArrayList<DiaryData> arr_diary;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_diary, container, false);
        recyclerView = root.findViewById(R.id.diary_RecyclerView);

        connectSQLIte();
        initRecyclerView();

        return root;
    }

    // connect SQLite
    private void connectSQLIte(){
        MyDBHelper dbHelper = new MyDBHelper(root.getContext());
        arr_diary = dbHelper.getResult();
    }

    // Recyclerview
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DiaryAdapter adapter = new DiaryAdapter(arr_diary);
        recyclerView.setAdapter(adapter);

        Button button = root.findViewById(R.id.temp_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });

    }
}