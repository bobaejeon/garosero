package com.foo.garosero.ui.treetip;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;
import com.foo.garosero.myUtil.WebviewHelper;

public class TreeExplainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_explain);

        //웹 뷰 띄우기
        String mURL = getIntent().getStringExtra("mURL");
        new WebviewHelper().initWebView(
                findViewById(R.id.explain_WebView),
                findViewById(R.id.explain_ProgressBar),
                mURL);

        // 뒤로가기 버튼
        findViewById(R.id.explain_ImageButton_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeExplainActivity.super.onBackPressed();
            }
        });
    }
}