package com.foo.garosero.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.garosero.R;
import com.foo.garosero.ui.home.sub.EmptyFragment;
import com.foo.garosero.ui.home.sub.TodoFragment;

public class TreeManagementFragment extends Fragment {
    View root;
    ImageView treeCharacter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel.setExplain("입양한 나무는 어떤 상태일까요?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_management, container, false);
        treeCharacter = root.findViewById(R.id.treeManagement_ImageView_treeCharacter);
        FrameLayout frameLayout = root.findViewById(R.id.treeManagement_FrameLayout);

        // 1. 나무 정보 없을때
        setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
        replaceFragment(new EmptyFragment());

        // 2. 나무 정보 있을 때
        setBackgroundImageview(treeCharacter, R.drawable.mid_tree);
        replaceFragment(new TodoFragment());

        return root;
    }

    // 이미지 뷰 채우기
    private void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), source));
    }

    // 프래그먼트 재설정
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.treeManagement_FrameLayout, fragment).commit();
    }
}