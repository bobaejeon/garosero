package com.foo.garosero.ui.home.diary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.myUtil.DiaryHelper;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_schedule, et_memo, et_persons;
    TextView tv_content;
    Button bt_insert, bt_delete, bt_cancel, bt_update;
    ImageButton ibt_back;
    ImageView iv_picture;

    DiaryData data;
    String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

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
        iv_picture = findViewById(R.id.report_ImageView_picture);

        // button click event
        bt_delete.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_insert.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        ibt_back.setOnClickListener(this);

        // 사진 가져오기
        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        // show data
        String report_mode = "create";
        try{
            report_mode = getIntent().getStringExtra("report_mode");
        }catch (Exception e){
        }
        if (report_mode.equals("update")){
            // set Data
            data = DiaryViewModel.getDiaryData();
            tv_content.setText(data.getContent());
            et_memo.setText(data.getMemo());
            et_schedule.setText(data.getSchedule());
            et_persons.setText(String.valueOf(data.getPersons()));
            if (data.getPicture().equals("")!=true)
                Glide.with(getApplicationContext()).load(data.getPicture()).into(iv_picture);

            // setVisibility
            bt_insert.setVisibility(View.GONE);
            bt_cancel.setVisibility(View.GONE);
        } else {
            // setVisibility
            bt_update.setVisibility(View.GONE);
            bt_delete.setVisibility(View.GONE);
        }
    }

    // get image
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent mdata) {
        super.onActivityResult(requestCode, resultCode, mdata);
        if (requestCode == 0 && resultCode == RESULT_OK){
            imageUri = mdata.getData().toString();
            Glide.with(getApplicationContext()).load(mdata.getData()).into(iv_picture);
        }
    }

    // Button Click Event : access db
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        data = setDiaryData();
        DiaryHelper diaryHelper = new DiaryHelper();

        switch (v.getId()){
            case R.id.report_Button_insert: // insert data
                diaryHelper.insertDiaryToServer(data);
                break;

            case R.id.report_Button_delete: // delete data
                diaryHelper.deleteDiaryFromServer(data);
                break;

            case R.id.report_Button_update: // update data
                diaryHelper.updateDiaryToServer(data);
                break;
        }

        // finish Activity
        finish();
    }

    // 폼에 적힌 내용으로 업데이트
    private DiaryData setDiaryData(){
        data = new DiaryData();
        data.setMemo(et_memo.getText().toString());
        data.setSchedule(et_schedule.getText().toString());
        data.setContent(tv_content.getText().toString());
        if (imageUri!= null) data.setPicture(imageUri);

        try {
            data.setPersons(Integer.parseInt(et_persons.getText().toString()));
        } catch (Exception e){
            data.setPersons(0);
        }

        return data;
    }
}