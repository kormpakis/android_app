package com.example.user.recyclerviewjsonvolley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebView1 extends AppCompatActivity {

    private static final String TAG = "WebView1";

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("url")){
            mWebView = (WebView) findViewById(R.id.webView);
            String URLS = (String) getIntent().getSerializableExtra("url");
            mWebView.loadUrl(URLS);
        }
    }

}
