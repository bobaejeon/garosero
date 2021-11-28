package com.foo.garosero.ui.home.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.myUtil.MyDBHelper;

public class ReportActivity extends AppCompatActivity {
    TextView tv_schedule, tv_memo;
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // SQL connection
        MyDBHelper dbHelper = new MyDBHelper(ReportActivity.this);

        // init view
        bt_submit = findViewById(R.id.report_Button_submit);
        tv_memo = findViewById(R.id.report_EditText_memo);
        tv_schedule = findViewById(R.id.report_EditText_schedule);

        // click event
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert Data
                DiaryData data = new DiaryData();
                data.setMemo(tv_memo.getText().toString());
                data.setSchedule(tv_schedule.getText().toString());
                dbHelper.insert(data);

                // finish Activity
                finish();
            }
        });
    }
}