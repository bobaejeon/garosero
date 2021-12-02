package com.foo.garosero.ui.application;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;

import com.foo.garosero.R;
import com.foo.garosero.myUtil.WebviewHelper;
import com.google.firebase.auth.FirebaseAuth;

public class ApplicationMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_map);
        CookieManager cookieManager=CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String uid = FirebaseAuth.getInstance().getUid();
        String cookieValue="login="+uid;
        cookieManager.setCookie("https://garosero.netlify.app/form",cookieValue);

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