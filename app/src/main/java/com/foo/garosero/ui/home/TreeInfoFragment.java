package com.foo.garosero.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TreeInfoFragment extends Fragment {
    View root;

    // db에서 값을 받아오고 표시하기 위함
    DatabaseReference database;
    String uid;

    TextView tv_tree_name, tv_carbon_amt, tv_kind, tv_road, tv_dist, tv_loc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel.setExplain("입양한 나무의 정보를 확인하세요!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        root = inflater.inflate(R.layout.fragment_tree_info, container, false);

        // 데이터 전달받아 표시하기 시작
        tv_tree_name = root.findViewById(R.id.tv_tree_name);
        tv_carbon_amt = root.findViewById(R.id.tv_carbon_amt);
        tv_kind = root.findViewById(R.id.tv_kind);
        tv_road = root.findViewById(R.id.tv_road);
        tv_dist = root.findViewById(R.id.tv_dist);
        tv_loc = root.findViewById(R.id.tv_loc);

        uid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/"+uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData ud = dataSnapshot.getValue(UserData.class);
                    tv_tree_name.setText(ud.getTree_name());
                    tv_road.setText(ud.getRoad());
                    tv_dist.setText(ud.getDistrict());
                    tv_loc.setText(ud.getLocation());

                    if (ud.getKind().endsWith("나무")) {
                        tv_kind.setText(ud.getKind());
                    } else {
                        tv_kind.setText("");
                    }

                    String carbon_amt;
                    switch (ud.getKind()){
                        case "은행나무":
                            carbon_amt = "33.7kg";
                            break;
                        case "소나무":
                            carbon_amt = "47.5kg";
                            break;
                        case "양버즘나무":
                            carbon_amt = "361.6kg";
                            break;
                        default:
                            carbon_amt = "???";
                    }
                    tv_carbon_amt.setText(carbon_amt);

                } else {
                    Log.e("MainActivity", "no data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MainActivity", "onCancelled", databaseError.toException());
            }
        };
        database.addListenerForSingleValueEvent(postListener);

        // 데이터 전달받아 표시하기 끝

        return root;
    }
}