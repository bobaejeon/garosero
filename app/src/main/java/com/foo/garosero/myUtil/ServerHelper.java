package com.foo.garosero.myUtil;

import android.util.Log;

import androidx.annotation.NonNull;

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
    static DatabaseReference ref;
    static String uid;
    static String name;
    static ArrayList<String> arr_tree;

    // user의 나무정보를 list로 연결
    static ArrayList<TreeInfo> list;

    // db 초기화
    static public void initServer(){
        uid = FirebaseAuth.getInstance().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    list = new ArrayList<>();
                    arr_tree = new ArrayList<String>();
                    String name = dataSnapshot.child("Users/"+uid).child("name").getValue().toString();
//                    readTreeTakenFromFireBase();
                    for(DataSnapshot snap : dataSnapshot.child("Approve").getChildren()){
                        try{
                            String tree_id = snap.child("tree_id").getValue().toString();
                            String tree_uid = snap.child("uid").getValue().toString();

                            if(tree_uid.equals(uid)) {
                                String tree_name = dataSnapshot.child("Trees_taken/"+tree_id).child("tree_name").getValue().toString();
                                int xp = Integer.parseInt(dataSnapshot.child("Trees_taken/"+tree_id).child("xp").getValue().toString());

                                TreeInfo treeInfo = new TreeInfo();

                                treeInfo.setTree_id(tree_id);
                                treeInfo.setTree_name(tree_name);
                                treeInfo.setXp(xp);

                                treeInfo.setTree_type(snap.child("tree_type").getValue().toString());
                                treeInfo.setLocation(snap.child("location").getValue().toString());
                                treeInfo.setStart_date(snap.child("date").getValue().toString());

                                list.add(treeInfo);
                            }
                            arr_tree.add(tree_id);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    UserInfo ud = new UserInfo(name,list);
                    Log.d("InitServer", ud.toString());
                    HomeViewModel.setUserInfo(ud);
                    Log.d("treelist", arr_tree.toString());
                    MapViewModel.setTreeDataArrayList(arr_tree);
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
        ref.addValueEventListener(postListener);
    }

    // get taken tree
    static public void readTreeTakenFromFireBase(){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ArrayList<String> arr_tree = new ArrayList<String>();
        Log.d("readf","dssfd");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.child("Approve").getChildren()){
                        try{
                            String tree_id = snap.child("tree_id").getValue().toString();
                            String tree_uid = snap.child("uid").getValue().toString();

                            if(tree_uid.equals(uid)) {
                                ref.child("Trees_taken").orderByKey().equalTo(tree_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        try{
                                            TreeInfo treeInfo = new TreeInfo();

                                            treeInfo.setTree_id(snap.child("tree_id").getValue().toString());
                                            treeInfo.setTree_type(snap.child("tree_type").getValue().toString());
                                            treeInfo.setLocation(snap.child("location").getValue().toString());
                                            treeInfo.setStart_date(snap.child("date").getValue().toString());
                                            treeInfo.setTree_name(datasnap.child(tree_id+"/tree_name").getValue().toString());
                                            treeInfo.setXp(Integer.parseInt(datasnap.child(tree_id+"/xp").getValue().toString()));
                                            list.add(treeInfo);
                                            Log.d("TreeInfo", treeInfo.toString());
                                        } catch (Exception e) {
                                            Log.e("Trees_taken", e.toString());
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Trees_taken", "on cancelled");
                                    }
                                });
                            }
                            arr_tree.add(tree_id);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    UserInfo ud = new UserInfo(name,list);
                    Log.d("InitServer", ud.toString());
                    HomeViewModel.setUserInfo(ud);
                    MapViewModel.setTreeDataArrayList(arr_tree);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ServerHelper", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(postListener);

//        return arr_tree;
    }
}