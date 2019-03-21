package com.jennifer.jennifer.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.util.AppFileUtils;

public class WebViewActivity extends AppCompatActivity {
    private static final String LOADING_URL = "loading_url";
    private static final String LOCAL_URL = "file:///android_asset/index.html";
    private LinearLayout llCommonProblem;
    private ProgressBar pbLoading;
    private RelativeLayout rlLoading;
    private TextView tvLoading;
    private WebView webView;
    private WebSettings settings;
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            rlLoading.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            tvLoading.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pbLoading.setProgress(0);
            rlLoading.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            tvLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webView.loadUrl(LOCAL_URL);
        }
    };

    public WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pbLoading.setProgress(newProgress);
        }
    };
    private String loadingUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initData();
        initView();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(LOADING_URL) != null) {
            loadingUrl = bundle.getString(LOADING_URL);
        }
        if (loadingUrl == null)
            loadingUrl = LOCAL_URL;
    }

    private void initView() {
        llCommonProblem = findViewById(R.id.ll_common_problem);
        pbLoading = findViewById(R.id.pb_loading);
        rlLoading = findViewById(R.id.rl_loading);
        tvLoading = findViewById(R.id.tv_loading);
        webView = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        llCommonProblem.addView(webView);
        webView.loadUrl(loadingUrl);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        initWebSetting();
    }

    private void initWebSetting() {
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js代码
        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);//设置自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置默认编码
        // 缓存模式说明:
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 1. 设置缓存路径
        String cacheDirPath = AppFileUtils.CACHE;
        settings.setAppCachePath(cacheDirPath);
        // 2. 设置缓存大小
        settings.setAppCacheMaxSize(20 * 1024 * 1024);
        // 3. 开启Application Cache存储机制
        settings.setAppCacheEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击返回时判断是否有上一页，有上一页返回上一页
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //释放资源避免内存泄漏
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public static void LaunchHtml5Activity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LOADING_URL, url);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
