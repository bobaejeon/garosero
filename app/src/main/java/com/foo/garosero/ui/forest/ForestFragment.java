package com.foo.garosero.ui.forest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.foo.garosero.R;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.HomeViewModel;

public class ForestFragment extends Fragment implements View.OnClickListener {
    View root;

    UserInfo ud;
    Button btn_care, btn_start;
    TextView tv_tree_name, tv_tree_level;
    ImageView iv_tree1, iv_tree2, iv_tree3, iv_tree4, iv_tree5;
    ConstraintLayout cl_background;
    ImageView[] iv_list;
    LinearLayout ll_info;

    int idx; // 선택한 나무의 index
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_forest, container, false);

        initView();

        return root;
    }

    public void initView(){
        ud = HomeViewModel.getUserInfo().getValue();
        btn_start = root.findViewById(R.id.forest_btn_start);
        ll_info = root.findViewById(R.id.forest_layout_info);
        btn_care = root.findViewById(R.id.forest_btn_care);
        tv_tree_name = root.findViewById(R.id.forest_tv_tree_name);
        tv_tree_level = root.findViewById(R.id.forest_tv_tree_level);

        cl_background = root.findViewById(R.id.forest_layout_background);
        iv_tree1 = root.findViewById(R.id.forest_iv_tree1);
        iv_tree2 = root.findViewById(R.id.forest_iv_tree2);
        iv_tree3 = root.findViewById(R.id.forest_iv_tree3);
        iv_tree4 = root.findViewById(R.id.forest_iv_tree4);
        iv_tree5 = root.findViewById(R.id.forest_iv_tree5);

        if(ud.isEmpty()){
            btn_start.setVisibility(View.VISIBLE);
            btn_start.setOnClickListener(this);
        }
        else{
            btn_start.setVisibility(View.GONE);
            iv_list = new ImageView[]{iv_tree1,iv_tree2,iv_tree3,iv_tree4,iv_tree5};

            // 나무의 갯수만큼만 표시
            for(int i=0 ; i<ud.getTreeList().size() ; i++){
                iv_list[i].setVisibility(View.VISIBLE);
                iv_list[i].setOnClickListener(this);
            }

            btn_care.setOnClickListener(this);
            cl_background.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forest_btn_start:// 처음 시작하는 경우
                break;
            case R.id.forest_btn_care://선택한 나무의 index를 intent로 전달
                break;
            case R.id.forest_iv_tree1:
                setTreeText(0);
                break;
            case R.id.forest_iv_tree2:
                setTreeText(1);
                break;
            case R.id.forest_iv_tree3:
                setTreeText(2);
                break;
            case R.id.forest_iv_tree4:
                setTreeText(3);
                break;
            case R.id.forest_iv_tree5:
                setTreeText(4);
                break;
            case R.id.forest_layout_background:
                ll_info.setVisibility(View.GONE);
                break;
        }
    }

    private void setTreeText(int i){
        idx = i;
        ll_info.setVisibility(View.VISIBLE);
        tv_tree_name.setText(ud.getTreeList().get(idx).getTree_name());
        String lev = "Level. "+(ud.getTreeList().get(idx).getXp()/10+1);
        tv_tree_level.setText(lev);
    }
}