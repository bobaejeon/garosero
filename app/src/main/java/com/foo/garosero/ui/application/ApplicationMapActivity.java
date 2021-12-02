package com.foo.garosero.ui.application;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.foo.garosero.R;
import com.foo.garosero.myUtil.WebviewHelper;
import com.foo.garosero.ui.treetip.TreeExplainActivity;

public class ApplicationMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_map);

        // WebView 초기화
        WebviewHelper webviewHelper = new WebviewHelper();
        webviewHelper.initWebView(
                findViewById(R.id.map_WebView),
                findViewById(R.id.map_ProgressBar),
                WebviewHelper.URL_form);

        // 뒤로가기 버튼
        findViewById(R.id.map_ImageButton_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationMapActivity.super.onBackPressed();
            }
        });
    }
}