package com.foo.garosero.myUtil;

import android.util.Log;

import com.foo.garosero.data.UserData;
import com.foo.garosero.mviewmodel.myViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServerHelper {
    // db에서 값을 받아오고 표시하기 위함
    static DatabaseReference database;
    static String uid;

    // db 초기화
    static public void initServer(){
        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    myViewModel.setUserData(ud);

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
}
