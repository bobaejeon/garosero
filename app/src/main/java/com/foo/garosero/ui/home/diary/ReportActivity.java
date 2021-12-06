package com.foo.garosero.ui.home.diary;

import static com.foo.garosero.myUtil.ImageHelper.showImage;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.myUtil.DiaryHelper;

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

    DiaryData oldDiary, newDiary;

    Map<CheckBox, TreeInfo> checkTreeMap;
    String imageUrl = "";

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

        // set content (to-do)
        try {
            String content = getIntent().getStringExtra("content");
            tv_content.setText(content);
        }catch (Exception e){
        }

        // 가져온 데이터 저장
        oldDiary = DiaryViewModel.getDiaryData();

        // show data
        String report_mode = "create";
        try{
            report_mode = getIntent().getStringExtra("report_mode");
        }catch (Exception e){
        }
        if (report_mode.equals("update")){
            // set Data on form
            tv_content.setText(oldDiary.getContent());
            et_memo.setText(oldDiary.getMemo());
            et_schedule.setText(oldDiary.getSchedule());
            et_persons.setText(String.valueOf(oldDiary.getPersons()));
            if (oldDiary.getPicture().equals("")!=true){
                // 이미지 띄우기
                imageUrl = oldDiary.getPicture();
                showImage(getApplicationContext(), imageUrl, iv_picture);
            }

            // set tree check box
            Set<String> chosenTrees = oldDiary.getTrees().keySet();
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
            // 이미지 띄우기
            imageUrl = mdata.getData().toString();
            showImage(getApplicationContext(), imageUrl, iv_picture);
            Log.e("Report", mdata.getData().toString());
        }
    }

    // Button Click Event : access db
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        DiaryHelper diaryHelper = new DiaryHelper();

        switch (v.getId()){
            case R.id.report_Button_insert: // insert data
                newDiary = setDiaryData();
                diaryHelper.insertDiaryToServer(newDiary);
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
                break;

            case R.id.report_Button_delete: // delete data
                diaryHelper.deleteDiaryFromServer(oldDiary);
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                break;

            case R.id.report_Button_update: // update data
                newDiary = setDiaryData();
                diaryHelper.updateDiaryToServer(oldDiary, newDiary);
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                break;
        }

        // finish Activity
        finish();
    }

    // 폼에 적힌 내용으로 업데이트 -> diary id는 변하지 않음
    private DiaryData setDiaryData(){
        newDiary = new DiaryData();
        newDiary.setDiaryID(oldDiary.getDiaryID());
        newDiary.setMemo(et_memo.getText().toString());
        newDiary.setSchedule(et_schedule.getText().toString());
        newDiary.setContent(tv_content.getText().toString());
        newDiary.getPicture();
        if (imageUrl!=null) newDiary.setPicture(imageUrl);

        try {
            newDiary.setPersons(Integer.parseInt(et_persons.getText().toString()));
        } catch (Exception e){
            newDiary.setPersons(0);
        }

        // 나무 등록 : 나무 아이디 값만 저장
        ArrayList<TreeInfo> chosenTree = getChosenTree();
        Map<String, Object> treeMap = new HashMap<String, Object>();
        for (TreeInfo tree : chosenTree) {
            treeMap.put(tree.tree_id, tree.tree_id);
        }
        newDiary.setTrees(treeMap);

        return newDiary;
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