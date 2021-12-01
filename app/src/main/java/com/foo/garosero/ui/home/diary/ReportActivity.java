package com.foo.garosero.ui.home.diary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.DiaryHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_schedule, et_memo, et_persons;
    TextView tv_content;
    Button bt_insert, bt_delete, bt_cancel, bt_update;
    ImageButton ibt_back;
    ImageView iv_picture;

    DiaryData data;
    String imageUri;

    Map<CheckBox, TreeInfo> checkTreeMap;

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

        // show Tree
        showTree();

        // show data
        String report_mode = "create";
        try{
            report_mode = getIntent().getStringExtra("report_mode");
        }catch (Exception e){
        }
        if (report_mode.equals("update")){
            // set Data on form
            data = DiaryViewModel.getDiaryData();
            tv_content.setText(data.getContent());
            et_memo.setText(data.getMemo());
            et_schedule.setText(data.getSchedule());
            et_persons.setText(String.valueOf(data.getPersons()));
            if (data.getPicture().equals("")!=true){
                try{
                    Glide.with(getApplicationContext()).load(data.getPicture()).into(iv_picture);
                }catch (Exception e){
                    Log.w("ReportActivity", "해당 경로에 파일이 존재하지 않습니다.");
                }
            }

            // set tree check box
            Set<String> chosenTrees = data.getTrees().keySet();
            for (CheckBox checkBox : checkTreeMap.keySet()){
                TreeInfo tree = checkTreeMap.get(checkBox);
                if (chosenTrees.contains(tree.getTree_id())){
                    checkBox.setChecked(true); // 체크박스에 표시하기
                }
            }

            // setVisibility
            bt_update.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.VISIBLE);

        } else {
            // report_mode == create ? set new data
            data = new DiaryData();
            // setVisibility
            bt_insert.setVisibility(View.VISIBLE);
            bt_cancel.setVisibility(View.VISIBLE);
        }
    }

    // get image
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent mdata) {
        super.onActivityResult(requestCode, resultCode, mdata);
        if (requestCode == 0 && resultCode == RESULT_OK){
            imageUri = mdata.getData().toString();
            try{
                Glide.with(getApplicationContext()).load(mdata.getData()).into(iv_picture);
            }catch (Exception e){
                Log.w("ReportActivity", "해당 경로에 파일이 존재하지 않습니다.");
            }
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
                getChosenTree();
                //diaryHelper.insertDiaryToServer(data);
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

    // 폼에 적힌 내용으로 업데이트 -> diary id는 변하지 않음
    private DiaryData setDiaryData(){
        data.setMemo(et_memo.getText().toString());
        data.setSchedule(et_schedule.getText().toString());
        data.setContent(tv_content.getText().toString());
        if (imageUri!= null) data.setPicture(imageUri);

        try {
            data.setPersons(Integer.parseInt(et_persons.getText().toString()));
        } catch (Exception e){
            data.setPersons(0);
        }

        // 나무 등록 : 나무 아이디 값만 저장
        ArrayList<TreeInfo> chosenTree = getChosenTree();
        Map<String, Object> treeMap = new HashMap<String, Object>();
        for (TreeInfo tree : chosenTree) {
            treeMap.put(tree.tree_id, tree.tree_id);
        }
        data.setTrees(treeMap);

        return data;
    }

    // 소유하고 있는 나무 표시하기
    private void showTree(){
        CheckBox[] cbList = {
                findViewById(R.id.report_CheckBox_tree1),
                findViewById(R.id.report_CheckBox_tree2),
                findViewById(R.id.report_CheckBox_tree3),
                findViewById(R.id.report_CheckBox_tree4),
                findViewById(R.id.report_CheckBox_tree5)};

        checkTreeMap  = new HashMap<>();
        ArrayList<TreeInfo> treeList = HomeViewModel.getUserInfo().getValue().getTreeList();
        for (int i = 0;i<treeList.size();i++){
            TreeInfo tree = treeList.get(i);
            CheckBox cb = cbList[i];
            checkTreeMap.put(cb, tree);

            String message = tree.getRoad()+"에 있는 "+tree.getTree_name()+"("+tree.getTree_id()+")";
            cb.setVisibility(View.VISIBLE); // 화면에 표시하기
            cb.setText(message);
        }
    }

    // 체크된 나무 가져오기
    private ArrayList<TreeInfo> getChosenTree(){
        ArrayList<TreeInfo> chosenTreeList = new ArrayList<TreeInfo>();
        for (CheckBox cb : checkTreeMap.keySet()){
            if (cb.isChecked() == true) {
                chosenTreeList.add(checkTreeMap.get(cb));
            }
        }
        return  chosenTreeList;
    }
}