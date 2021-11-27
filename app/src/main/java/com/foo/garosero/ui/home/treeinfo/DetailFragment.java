package com.foo.garosero.ui.home.treeinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foo.garosero.R;
import com.foo.garosero.data.UserData;
import com.foo.garosero.mviewmodel.myViewModel;

public class DetailFragment extends Fragment {
    View root;
    TextView tv_kind, tv_road, tv_dist, tv_loc;
    UserData ud;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_detail, container, false);

        tv_kind = root.findViewById(R.id.tv_kind);
        tv_road = root.findViewById(R.id.tv_road);
        tv_dist = root.findViewById(R.id.tv_dist);
        tv_loc = root.findViewById(R.id.tv_loc);

        // init view
        ud = myViewModel.getUserData().getValue();
        tv_road.setText(ud.getRoad());
        tv_dist.setText(ud.getDistrict());
        tv_loc.setText(ud.getLocation());
        tv_kind.setText(ud.getKind());
        return root;
    }
}