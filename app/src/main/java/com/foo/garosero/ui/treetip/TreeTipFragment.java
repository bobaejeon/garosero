package com.foo.garosero.ui.treetip;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;
import com.foo.garosero.myUtil.TreeTipHelper;

public class TreeTipFragment extends Fragment implements View.OnClickListener {
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tree_tip, container, false);

        // init veiw
        root.findViewById(R.id.TreeTip_CardView_1).setOnClickListener(this);
        root.findViewById(R.id.TreeTip_CardView_2).setOnClickListener(this);
        root.findViewById(R.id.TreeTip_CardView_3).setOnClickListener(this);
        root.findViewById(R.id.TreeTip_CardView_4).setOnClickListener(this);
        root.findViewById(R.id.TreeTip_CardView_5).setOnClickListener(this);
        root.findViewById(R.id.TreeTip_CardView_6).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.TreeTip_CardView_1:
                startTipActivity(TreeTipHelper.STRING_ginkgo);
                break;
            case R.id.TreeTip_CardView_2:
                startTipActivity(TreeTipHelper.STRING_pine);
                break;
            case R.id.TreeTip_CardView_3:
                startTipActivity(TreeTipHelper.STRING_zelkova);
                break;
            case R.id.TreeTip_CardView_4:
                startTipActivity(TreeTipHelper.STRING_cherry);
                break;
            case R.id.TreeTip_CardView_5:
                startTipActivity(TreeTipHelper.STRING_poplar);
                break;
            case R.id.TreeTip_CardView_6:
                startTipActivity(TreeTipHelper.STRING_apricot);
                break;
        }
    }

    private void startTipActivity(String mKey){
        Intent intent = new Intent(getActivity(), TreeExplainActivity.class);
        intent.putExtra("mKey", mKey);
        startActivity(intent);
    }

}