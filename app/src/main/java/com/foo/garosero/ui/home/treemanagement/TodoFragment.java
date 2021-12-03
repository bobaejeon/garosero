package com.foo.garosero.ui.home.treemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;
import com.foo.garosero.ui.home.diary.ReportActivity;

import java.util.HashMap;

public class TodoFragment extends Fragment{
    View root;

    Button bt_submit;
    HashMap<CheckBox, TextView> checkBoxTextViewHashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =inflater.inflate(R.layout.fragment_todo, container, false);

        // init view
        bt_submit = root.findViewById(R.id.bt_submit);

        // 체크박스-텍스트뷰 묶음
        checkBoxTextViewHashMap = new HashMap<>();
        checkBoxTextViewHashMap.put(root.findViewById(R.id.todo_cb_1), root.findViewById(R.id.todo_tv_1));
        checkBoxTextViewHashMap.put(root.findViewById(R.id.todo_cb_2), root.findViewById(R.id.todo_tv_2));
        checkBoxTextViewHashMap.put(root.findViewById(R.id.todo_cb_3), root.findViewById(R.id.todo_tv_3));

        /* todo 카드뷰를 눌러도 체크, uncheck되도록? */

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get check box checked > set diary
                String content = "";
                for (CheckBox checkBox : checkBoxTextViewHashMap.keySet()){
                   if (checkBox.isChecked()==true){
                       content += checkBoxTextViewHashMap.get(checkBox).getText().toString()+"\n";
                   }
                }

                // goto report Activity
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("report_mode","create");
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });

        return root;
    }
}