package com.foo.garosero.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.foo.garosero.R;
import com.foo.garosero.data.UserTreesData;
import com.foo.garosero.ui.home.empty.EmptyFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TreeInfoFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ImageView treeCharacter;

    View root;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel.setExplain("입양한 나무의 정보를 확인하세요!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tree_info, container, false);

        //String uid = firebaseAuth.getInstance().getUid();
        String uid = "GpnsOr4HRgPluWvUBqPsg4Gt8FQ2"; //테스트용
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/"+uid+"/trees");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int num = 1;
                    for(DataSnapshot snap : snapshot.getChildren()){ // 데이터 리스트 추출
                        UserTreesData userTreesData = snap.getValue(UserTreesData.class); //나무의 세부데이터
                        String name = snap.getKey(); //나무이름
//                        Log.d("treeInfo", num+") "+name+" "+userTreesData.getRoad().toString()); //잘 들어옴

                        num++;
                    }
                }
                Log.e("treeInfo", "no data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("treeInfo", String.valueOf(error.toException()));
            }
        });

        root = inflater.inflate(R.layout.fragment_tree_info, container, false);
        initView();
        return root;
    }

    private void initView() {
        // 나무 정보 없을때 빈화면 출력
        treeCharacter = root.findViewById(R.id.treeInfo_ImageView_treeCharacter);
        treeCharacter.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.empty_tree));
        FrameLayout frameLayout = root.findViewById(R.id.treeInfo_FrameLayout);
        replaceFragment(new EmptyFragment());
        // frameLayout.setVisibility(View.GONE);
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.treeInfo_FrameLayout, fragment).commit();
    }
}