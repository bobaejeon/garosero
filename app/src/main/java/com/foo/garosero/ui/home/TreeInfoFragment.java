package com.foo.garosero.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TreeInfoFragment extends Fragment {
    ImageView treeCharacter;
    TableLayout tableLayout;
    FrameLayout frameLayout;

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

        root = inflater.inflate(R.layout.fragment_tree_info, container, false);
        initView();
        return root;
    }

    private void initView() {
        treeCharacter = root.findViewById(R.id.treeInfo_ImageView_treeCharacter);
        tableLayout = root.findViewById(R.id.treeInfo_TableLayout_treeinfo);
        frameLayout = root.findViewById(R.id.treeInfo_FrameLayout);

        if (tv_tree_name.getText().toString().equals(getString(R.string.noTree))){
            // 1. 나무 정보 없을때
            setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
            tableLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
        else {
            // 2. 나무 정보 있을 때
            setBackgroundImageview(treeCharacter, R.drawable.mid_tree);
            tableLayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    // 이미지 뷰 채우기
    private void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), source));
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.treeInfo_FrameLayout, fragment).commit();
    }
}