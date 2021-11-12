package com.foo.garosero.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;
import com.foo.garosero.mywebview.WebviewHelper;

public class TreeTipFragment extends Fragment implements View.OnClickListener {
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // page explain text
        HomeViewModel.setExplain("우리 나무를 지키기 위한 팁들!");
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
                startWebView(WebviewHelper.URL_TreeTip_ginkgo);
                break;
            case R.id.TreeTip_CardView_2:
                startWebView(WebviewHelper.URL_TreeTip_pine);
                break;
            case R.id.TreeTip_CardView_3:
                startWebView(WebviewHelper.URL_TreeTip_zelkova);
                break;
            case R.id.TreeTip_CardView_4:
                startWebView(WebviewHelper.URL_TreeTip_cherry);
                break;
            case R.id.TreeTip_CardView_5:
                startWebView(WebviewHelper.URL_TreeTip_poplar);
                break;
            case R.id.TreeTip_CardView_6:
                startWebView(WebviewHelper.URL_TreeTip_apricot);
                break;
        }
    }

    private void startWebView(String mURL){
        Intent intent = new Intent(getActivity(),TreeExplainActivity.class);
        intent.putExtra("mURL", mURL);
        startActivity(intent);
    }

}