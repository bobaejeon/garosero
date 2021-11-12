package com.foo.garosero.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.foo.garosero.R;
import com.foo.garosero.mywebview.WebviewHelper;

public class InformationFragment extends Fragment {
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_information, container, false);

        //웹 뷰 띄우기
        new WebviewHelper().initWebView(
                root.findViewById(R.id.information_WebView),
                root.findViewById(R.id.information_ProgressBar),
                WebviewHelper.URL_information);
        return root;
    }
}