package com.foo.garosero.ui.home.treeinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.foo.garosero.R;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.ui.empty.EmptyFragment;

public class TreeInfoFragment extends Fragment {
    View root;

    ViewPager2 viewPager;
    TreeInfoAdapter pagerAdapter;

    UserInfo ud;
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_info, container, false);
        viewPager = root.findViewById(R.id.viewPager);
        frameLayout = root.findViewById(R.id.treeInfo_FrameLayout);

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                // 유저 정보 가져오기
                ud = HomeViewModel.getUserInfo().getValue();
                // 뷰 로딩
                 initView();
            }
        };
        HomeViewModel.getUserInfo().observe(getActivity(), userDataObserver);

        return root;
    }

    private void initView() {

        if (ud.isEmpty()){
            // 1. 나무 정보 없을때
            frameLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);

            EmptyFragment emptyFragment = new EmptyFragment();
            replaceFragment(emptyFragment);
        }
        else {
            // 2. 나무 정보 있을 때
            frameLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            pagerAdapter = new TreeInfoAdapter(ud);
            viewPager.setAdapter(pagerAdapter);
            Bundle bundle = getArguments();

            if(bundle!=null) viewPager.setCurrentItem(bundle.getInt("index"));
            else viewPager.setCurrentItem(0);
        }
    }

    // 프래그먼트 바꾸기
    public void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.treeInfo_FrameLayout, fragment).commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}