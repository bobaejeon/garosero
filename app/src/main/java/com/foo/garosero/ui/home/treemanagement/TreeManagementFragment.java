package com.foo.garosero.ui.home.treemanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.foo.garosero.R;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;
import com.foo.garosero.ui.home.empty.EmptyFragment;


public class TreeManagementFragment extends Fragment {
    View root;

    UserInfo ud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_management, container, false);

        // live data
        final Observer<UserInfo> userDataObserver = new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userData) {
                // 유저 정보 가져오기
                ud = HomeViewModel.getUserInfo().getValue();
                // 뷰 로딩
                initView(ud);
            }
        };
        HomeViewModel.getUserInfo().observe(getActivity(), userDataObserver);

        return root;
    }

    private void initView(UserInfo ud) {
        Log.d("treemanage", ud.toString());
        if (ud.isEmpty()) {
            // 1. 나무 정보 없을때
            EmptyFragment emptyFragment = new EmptyFragment();
            replaceFragment(emptyFragment);
        }
        else {
            // 2. 나무 정보 있을 때
            TodoFragment todoFragment = new TodoFragment();
            replaceFragment(todoFragment);
        }
    }

//    private String getTreeDay(){
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        long calDateDays = 0;
//        String tree_day = "";
//
//        if (ud.getStart_date().equals("")){
//            return "";
//        }
//
//        try {
//            Date currDate = calendar.getTime();
//            Date lastDate = sdf.parse(ud.getStart_date());
//            long calDate = currDate.getTime() - lastDate.getTime();
//            calDateDays = calDate / ( 24*60*60*1000);
//            tree_day = ud.getName()+"님과 함께한지 "+Math.abs(calDateDays)+"일째";
//
//        } catch (ParseException e) {
//            Log.e("TreeManagementFrag",e.toString());
//        }
//        return tree_day;
//    }

    // 프래그먼트 바꾸기
    public void replaceFragment(Fragment fragment) {
        try{
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.treeManagement_FrameLayout, fragment).commit();
        } catch (IllegalStateException illegalStateException){}
        catch (Exception e){
            e.printStackTrace();
        }
    }
}