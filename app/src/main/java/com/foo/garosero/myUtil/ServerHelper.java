package com.foo.garosero.myUtil;

import android.util.Log;

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServerHelper {
    // db에서 값을 받아오고 표시하기 위함
    static DatabaseReference database;
    static String uid;

    // 초기값
    static String TAG = "serverHelper";

    static UserInfo userData;
    static ArrayList<TreeInfo> treeData; // user data
    static ArrayList<TreeInfo> approveData; // approve data
    static ArrayList<TreeInfo> candidateData; // candidate data

    // db 초기화
    static public void initServer(){
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference();

        treeData = new ArrayList<>();
        approveData = new ArrayList<>();
        candidateData = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // get user data
                    String name = dataSnapshot.child("Users/"+uid).child("name").getValue().toString();

                    // get tree data (approve)
                    for(DataSnapshot snap : dataSnapshot.child("Approve").getChildren()){
                        try{
                            TreeInfo ti = snap.getValue(TreeInfo.class);
                            approveData.add(ti);

                            if(ti.getUid().equals(uid)){
                                treeData.add(ti);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    // get tree data (tree taken)
                    // treeData에 저장해둔 값 탐색해서 비워진 값 채우기
                    for (TreeInfo td : treeData) {
                        try{
                            String tree_id = td.getTree_id();
                            String tree_name = dataSnapshot.child("Trees_taken/"+tree_id).child("tree_name").getValue().toString();
                            Integer xp = Integer.parseInt(dataSnapshot.child("Trees_taken/"+tree_id).child("xp").getValue().toString());

                            td.setTree_name(tree_name);
                            td.setXp(xp);

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    // get candidate data
                    for(DataSnapshot snap : dataSnapshot.child("Candidates").getChildren()){
                        try{
                            TreeInfo ti = snap.getValue(TreeInfo.class);
                            candidateData.add(ti);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    userData = new UserInfo(name,treeData);
                    HomeViewModel.setUserInfo(userData);
                    HomeViewModel.setApproveInfo(approveData);
                    HomeViewModel.setCandidateInfo(candidateData);

                } else {
                    Log.e(TAG, "no data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        };
        database.addListenerForSingleValueEvent(postListener);
    }
}