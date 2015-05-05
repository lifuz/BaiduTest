package com.prd.testweb;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView) findViewById(R.id.wv);
        wv.loadUrl("http://www.baidu.com");

        //获取WebSetting设置Webview属性和状态
        WebSettings ws = wv.getSettings();
        //使webview直接Javascript
        ws.setJavaScriptEnabled(true);

        //初始化Webviewclient
        MyWebViewClient mv = new MyWebViewClient();



        //设置网页在自己的WebView中显示
        wv.setWebViewClient(mv);
        wv.setWebChromeClient(new WebChromeClient());

    }

    private class MyWebViewClient extends WebViewClient {

        //重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }


}
