package com.foo.garosero.ui.home.child;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.foo.garosero.mviewmodel.myViewModel;
import com.foo.garosero.ui.home.grandchild.DetailFragment;
import com.foo.garosero.ui.home.grandchild.EmptyFragment;

public class MyDiaryFragment extends Fragment {
    View root;

    UserData ud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_diary, container, false);
        return root;
    }
}