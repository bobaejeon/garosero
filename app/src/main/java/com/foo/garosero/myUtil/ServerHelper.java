package com.foo.garosero.myUtil;

import android.util.Log;

import com.foo.garosero.data.TreeData;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.mviewmodel.MapViewModel;
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

    // user의 나무정보를 list로 연결
    static ArrayList<TreeInfo> list;

    // db 초기화
    static public void initServer(){
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("Users/"+uid).child("name").getValue().toString();
                    for(DataSnapshot snap : dataSnapshot.child("Trees_taken").getChildren()){
                        try{
                            TreeInfo ti = snap.getValue(TreeInfo.class);
                            if(ti.getUid().equals(uid)){
                                list.add(ti);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    UserInfo ud = new UserInfo(name,list);
                    Log.d("InitServer", ud.toString());
                    HomeViewModel.setUserInfo(ud);

                } else {
                    Log.e("ServerManager", "no data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ServerManager", "onCancelled", databaseError.toException());
            }
        };
        database.addListenerForSingleValueEvent(postListener);
    }

    // get taken tree
    static public ArrayList<TreeData> readTreeTakenFromFireBase(){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ArrayList<TreeData> arr_tree = new ArrayList<TreeData>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.child("Trees_taken").getChildren()){
                    try{
                        TreeData treeData = snap.getValue(TreeData.class);
                        arr_tree.add(treeData);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                MapViewModel.setTreeDataArrayList(arr_tree);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MAP ACTIVITY", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(postListener);
        return arr_tree;
    }
}