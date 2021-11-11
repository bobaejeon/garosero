package com.foo.garosero.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.garosero.R;

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
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
            case R.id.TreeTip_CardView_2:
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
            case R.id.TreeTip_CardView_3:
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
            case R.id.TreeTip_CardView_4:
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
            case R.id.TreeTip_CardView_5:
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
            case R.id.TreeTip_CardView_6:
                startWebVeiw("https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce");
                break;
        }
    }

    private void startWebVeiw(String mURL){
        Intent intent = new Intent(getActivity(),TreeExplainActivity.class);
        intent.putExtra("mURL", mURL);
        startActivity(intent);
    }

}