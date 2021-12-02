package com.foo.garosero.ui.application;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.foo.garosero.R;
import com.foo.garosero.myUtil.WebviewHelper;

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

    }
}