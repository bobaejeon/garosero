package com.foo.garosero.myUtil;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.foo.garosero.data.DiaryData;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
                for(DataSnapshot snap : snapshot.child("Diaries").getChildren()){
                    try{
                        DiaryData diary = snap.getValue(DiaryData.class);
                        if(diary.getUid().equals(uid)){
                            arr_diary.add(diary);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
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
    DatabaseReference ref;

    public void insertDiaryToServer(DiaryData diaryData){
        if (diaryData==null) return;

        String uid = FirebaseAuth.getInstance().getUid();

        // 현재 시간을 아이디로 사용
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-ss-SSS");
        String now = format.format(date);

        // diaryData에도 ID 업데이트(BB)
        diaryData.setDiaryID(now);
        diaryData.setUid(uid);

        Map<String, DiaryData> diaries = new HashMap<String, DiaryData>();
        diaries.put("new", diaryData);
        uploadDiarytoServer(diaries);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDiaryToServer(DiaryData oldDiary, DiaryData newDiary){
        if (oldDiary==null || newDiary==null || oldDiary.equals(newDiary)) return;

        String uid = FirebaseAuth.getInstance().getUid();

        newDiary.setUid(uid);
        newDiary.setDiaryID(oldDiary.getDiaryID());

        Map<String, DiaryData> diaries = new HashMap<String, DiaryData>();
        diaries.put("new", newDiary);
        diaries.put("old", oldDiary);
        uploadDiarytoServer(diaries);
    }

    public void deleteDiaryFromServer(DiaryData diaryData){
        if (diaryData==null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        ref.child("Diaries").child(diaryData.getDiaryID()).removeValue();
        // tree xp update
        updateTreeXp(diaryData.getTrees().keySet(), -5);

        // if image in storage ? delete image in firebase storage
        if (!diaryData.getPicture().equals("")) FirebaseStorage.getInstance().getReferenceFromUrl(diaryData.getPicture()).delete();
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

    private void updateTreeXp(Map<String, Object> oldTrees, Map<String, Object> newTrees){
        if (oldTrees.equals(newTrees)) return;
        Set<String> oldDataSet = oldTrees.keySet();
        Set<String> newDataSet = newTrees.keySet();
        oldDataSet.removeAll(newTrees.keySet());
        newDataSet.removeAll(oldTrees.keySet());

        updateTreeXp(oldDataSet, -5); // 제거된 tree에서 xp 제거
        updateTreeXp(newTrees.keySet(), 5);  // 추가된 tree에서 xp 추가
    }

    private void uploadDiarytoServer(Map<String,DiaryData> diaries){
        ref = FirebaseDatabase.getInstance().getReference();
        int flag = diaries.size();
        String fileName = System.currentTimeMillis()+".jpg";

        // if img is not updated or not selected, just update realtime db
        if(diaries.get("new").getPicture().equals("")
                || diaries.get("new").getPicture().contains("firebasestorage")){
            ref = ref.child("Diaries").child(diaries.get("new").getDiaryID());
            ref.updateChildren(diaries.get("new").getHash());

            if (flag == 2) updateTreeXp(diaries.get("old").getTrees(), diaries.get("new").getTrees());
            return;
        }
        Uri file = Uri.parse(diaries.get("new").getPicture());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("Diaries/");

        UploadTask uploadTask = storageReference.child(fileName).putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw  task.getException();
                }
                return storageReference.child(fileName).getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    diaries.get("new").setPicture(downloadUri.toString());

                    if(flag == 1){ // insert
                        Map<String, Object> childUpdate = new HashMap<String, Object>();
                        childUpdate.put("Diaries/"+diaries.get("new").getDiaryID(), diaries.get("new").getHash());
                        childUpdate.put("Users/"+diaries.get("new").getUid()+"/last_activity/",diaries.get("new").getDiaryID());
                        ref.updateChildren(childUpdate);
                        updateTreeXp(diaries.get("new").getTrees().keySet(), 5);
                    }else if(flag == 2){ // update
                        // delete previous image in firebase storage
                        storage.getReferenceFromUrl(diaries.get("old").getPicture()).delete();

                        ref = ref.child("Diaries").child(diaries.get("new").getDiaryID());
                        ref.updateChildren(diaries.get("new").getHash());

                        updateTreeXp(diaries.get("old").getTrees(), diaries.get("new").getTrees());
                    }

                    Log.d("DiaryHelper","사진 업로드 성공" );
                }
            }
        });
    }
}