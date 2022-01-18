package com.foo.garosero.myUtil;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.foo.garosero.data.DiaryData;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DiaryHelper {
    private final String TAG = "DiaryHelper";

    public void readDiaryFromFireBase(){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<DiaryData> arr_diary = new ArrayList<DiaryData>();
                for (DataSnapshot dataSnapshot : snapshot.child("Users/"+uid+"/diaries").getChildren()){
                    DiaryData diaryData = dataSnapshot.getValue(DiaryData.class);
                    arr_diary.add(diaryData);
                }
                DiaryViewModel.getDiaryDataList().setValue(arr_diary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(postListener);
    }

    public void insertDiaryToServer(DiaryData diaryData){
        if (diaryData==null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        // 현재 시간을 아이디로 사용
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-ss-SSS");
        String now = format.format(date);

        // diaryData에도 ID 업데이트(BB)
        diaryData.setDiaryID(now);

        // diaryData를 Users>UID>diaries>now 에 업데이트
        // 마지막 활동날짜 저장
        ref = ref.child("Users").child(uid);
        Map<String, Object> childUpdate = new HashMap<String, Object>();
        childUpdate.put("/diaries/"+now, diaryData.getHash());
        childUpdate.put("/last_activity/",now);
        ref.updateChildren(childUpdate);

        // tree xp update
        updateTreeXp(diaryData.getTrees().keySet(), 5);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDiaryToServer(DiaryData oldDiary, DiaryData newDiary){
        if (oldDiary==null || newDiary==null || oldDiary.equals(newDiary)) return;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        ref = ref.child("Users").child(uid).child("diaries").child(oldDiary.getDiaryID());
        ref.updateChildren(newDiary.getHash());

        if (oldDiary.getTrees().equals(newDiary.getTrees())) return; // tree 값이 변경되지 않았다면 pass
        Set<String> oldDataSet = oldDiary.getTrees().keySet();
        Set<String> newDataSet = newDiary.getTrees().keySet();
        oldDataSet.removeAll(newDiary.getTrees().keySet());
        newDataSet.removeAll(oldDiary.getTrees().keySet());

        updateTreeXp(oldDataSet, -5); // 제거된 tree에서 xp 제거
        updateTreeXp(newDiary.getTrees().keySet(), 5);  // 추가된 tree에서 xp 추가
    }

    public void deleteDiaryFromServer(DiaryData diaryData){
        if (diaryData==null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        ref.child("Users").child(uid).child("diaries").child(diaryData.getDiaryID()).removeValue();
        // tree xp update
        updateTreeXp(diaryData.getTrees().keySet(), -5);
    }

    private void updateTreeXp(Set<String> treeSet, int plus_xp){
        String uid = FirebaseAuth.getInstance().getUid();
        ArrayList<TreeInfo> treeList = HomeViewModel.getUserInfo().getValue().getTreeList();

        for (TreeInfo tree : treeList){
            if (treeSet.contains(tree.getTree_id())){

                // read xp from server - pass
                /*ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String newXP = snapshot.child("Trees_taken/"+tree.getTree_id()+"/xp").getChildren().toString();
                        try {
                            tree.setXp(Integer.parseInt(newXP));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };*/

                // update xp to server
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Integer newXP = tree.getXp()+plus_xp;
                ref.child("Trees_taken/"+tree.getTree_id()+"/xp").setValue(newXP);

                // write local data
                tree.setXp(newXP);
            }
        }

    }
}