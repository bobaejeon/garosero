package com.foo.garosero.ui.home.treeinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.foo.garosero.mviewmodel.myViewModel;
import com.foo.garosero.ui.home.empty.EmptyFragment;

public class TreeInfoFragment extends Fragment {
    View root;

    ImageView treeCharacter;
    TableLayout tableLayout;
    FrameLayout frameLayout;
    TextView tv_tree_name, tv_carbon_amt;

    UserData ud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tree_info, container, false);

        tv_tree_name = root.findViewById(R.id.tv_tree_name);
        tv_carbon_amt = root.findViewById(R.id.tv_carbon_amt);
        treeCharacter = root.findViewById(R.id.treeInfo_ImageView_treeCharacter);
        tableLayout = root.findViewById(R.id.treeInfo_TableLayout_treeinfo);
        frameLayout = root.findViewById(R.id.treeInfo_FrameLayout);

        // live data
        final Observer<UserData> userDataObserver = new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                // 유저 정보 가져오기
                ud = myViewModel.getUserData().getValue();
                // 뷰 로딩
                initView();
            }
        };
        myViewModel.getUserData().observe(getActivity(), userDataObserver);

        return root;
    }

    private void initView() {
        tv_tree_name.setText(ud.getTree_name());
        tv_carbon_amt.setText(ud.getCarbon_amt());

        if (ud.isEmpty()){
            // 1. 나무 정보 없을때
            setBackgroundImageview(treeCharacter, R.drawable.empty_tree);
            EmptyFragment emptyFragment = new EmptyFragment();
            replaceFragment(emptyFragment);
        }
        else {
            // 2. 나무 정보 있을 때
            setBackgroundImageview(treeCharacter, R.drawable.mid_tree);
            DetailFragment detailFragment = new DetailFragment();
            replaceFragment(detailFragment);
        }
    }

    // 이미지 뷰 채우기
    public void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(root.getContext(), source));
    }

    // 프래그먼트 바꾸기
    public void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.treeInfo_FrameLayout, fragment).commit();
        } catch (IllegalStateException illegalStateException){}
        catch (Exception e){
            e.printStackTrace();
        }
    }
}