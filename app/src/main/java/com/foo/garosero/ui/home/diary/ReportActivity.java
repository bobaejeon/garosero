package com.foo.garosero.ui.home.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.myUtil.MyDBHelper;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_schedule, et_memo, et_persons;
    TextView tv_content;
    Button bt_insert, bt_delete, bt_cancel, bt_update;
    ImageButton ibt_back;

    MyDBHelper dbHelper;
    DiaryData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // SQL connection
        dbHelper = new MyDBHelper(ReportActivity.this);

        // init view
        tv_content = findViewById(R.id.report_TextView_content);
        et_persons = findViewById(R.id.report_EditText_persons);
        et_memo = findViewById(R.id.report_EditText_memo);
        et_schedule = findViewById(R.id.report_EditText_schedule);
        bt_cancel = findViewById(R.id.report_Button_cancel);
        bt_delete = findViewById(R.id.report_Button_delete);
        bt_insert = findViewById(R.id.report_Button_insert);
        bt_update = findViewById(R.id.report_Button_update);
        ibt_back = findViewById(R.id.report_ImageButton_back);

        // button click event
        bt_delete.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_insert.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        ibt_back.setOnClickListener(this);

        // show data
        if (DiaryViewModel.isEmpty()==false){
            // set Data
            data = DiaryViewModel.getDiaryData();
            tv_content.setText(data.getContent());
            et_memo.setText(data.getMemo());
            et_schedule.setText(data.getSchedule());
            et_persons.setText(String.valueOf(data.getPersons()));

            // setVisibility
            bt_insert.setVisibility(View.GONE);
            bt_cancel.setVisibility(View.GONE);
        } else {
            // setVisibility
            bt_update.setVisibility(View.GONE);
            bt_delete.setVisibility(View.GONE);
        }
    }

    // Button Click Event
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_Button_insert:
                insertData();
                break;
            case R.id.report_Button_update:
                updateData();
                break;
            case R.id.report_Button_delete:
                deleteData();
                break;
        }
        // empty diaryData
        DiaryViewModel.setEmpty();

        // finish Activity
        finish();
    }

    public void insertData(){
        DiaryData data = new DiaryData();
        data.setMemo(et_memo.getText().toString());
        data.setSchedule(et_schedule.getText().toString());
        dbHelper.insert(data);
    }

    public void deleteData(){
        if (data!= null){
            dbHelper.delete(data.getDiaryID());
        }
    }

    public void updateData(){
        if (data!= null){
            data.setMemo(et_memo.getText().toString());
            data.setSchedule(et_schedule.getText().toString());
            data.setContent(tv_content.getText().toString());

            int persons = 0;
            try {
                persons = Integer.parseInt(et_persons.getText().toString());
            } catch (Exception e){}
            data.setPersons(persons);
            dbHelper.update(data);
        }
    }
}