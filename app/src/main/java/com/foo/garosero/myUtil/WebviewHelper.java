package com.foo.garosero.myUtil;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebviewHelper {
    // url 모음

    public static String URL_visualization = "https://618e46a416eac000078e9aa8--garosero.netlify.app/datavisualization";    // 가로수 정보 시각화
    public static String URL_information = "https://618cdf09145ae20008f961dc--garosero.netlify.app/caretreeintroduce";      // 나무돌보미란?
    public static String URL_TreeTip_ginkgo = "https://618e46a416eac000078e9aa8--garosero.netlify.app/ginkgo";   // 은행나무
    public static String URL_TreeTip_pine = "https://618e46a416eac000078e9aa8--garosero.netlify.app/pinetree";     // 소나무
    public static String URL_TreeTip_zelkova = "https://618e46a416eac000078e9aa8--garosero.netlify.app/zelkova";  // 느티나무
    public static String URL_TreeTip_cherry = "https://618e46a416eac000078e9aa8--garosero.netlify.app/kingcherry";   // 벚나무
    public static String URL_TreeTip_poplar = "https://618e46a416eac000078e9aa8--garosero.netlify.app/poplartree";   // 이팝나무
    public static String URL_TreeTip_apricot = "https://618e46a416eac000078e9aa8--garosero.netlify.app/apricottree";  // 살구나무

    // 웹뷰 초기화 함수
    public void initWebView(WebView wView, ProgressBar pBar, String mURL){
        // 1. 웹뷰클라이언트 연결 (로딩 시작/끝 받아오기)
        wView.setWebViewClient(new WebViewClient(){
            @Override                                   // 1) 로딩 시작
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pBar.setVisibility(View.VISIBLE);       // 로딩이 시작되면 로딩바 보이기
            }
            @Override                                   // 2) 로딩 끝
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pBar.setVisibility(View.GONE);          // 로딩이 끝나면 로딩바 없애기
            }
            @Override                                   // 3) 외부 브라우저가 아닌 웹뷰 자체에서 url 호출
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 2. WebSettings: 웹뷰의 각종 설정을 정할 수 있다.
        WebSettings ws = wView.getSettings();
        ws.setJavaScriptEnabled(true); // 자바스크립트 사용 허가
        // 3. 웹페이지 호출
        wView.loadUrl(mURL);
    }
}
