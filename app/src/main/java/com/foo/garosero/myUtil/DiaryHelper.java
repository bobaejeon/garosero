package com.foo.garosero.myUtil;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.foo.garosero.data.DiaryData;
import com.foo.garosero.mviewmodel.DiaryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryHelper {
    public void readDiaryFromFireBase(){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("Users").child(uid).child("diaries");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DiaryData> arr_diary = new ArrayList<DiaryData>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DiaryData diaryData = dataSnapshot.getValue(DiaryData.class);
                    arr_diary.add(diaryData);
                }
                DiaryViewModel.getDiaryDataList().setValue(arr_diary);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyDiary", error.toString());
            }
        });
    }

    public void insertDiaryToServer(DiaryData diaryData){
        if (diaryData==null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        // 현재 시간을 아이디로 사용
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-ss-SSS");
        String now = format.format(date);

        ref.child("Users").child(uid).child("diaries").child(now).setValue(diaryData);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDiaryToServer(DiaryData diaryData){
        if (diaryData==null) return;
        deleteDiaryFromServer(diaryData);
        insertDiaryToServer(diaryData);
    }

    public void deleteDiaryFromServer(DiaryData diaryData){
        if (diaryData==null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        ref.child("Users").child(uid).child("diaries").child(diaryData.getDiaryID()).removeValue();
    }

}
