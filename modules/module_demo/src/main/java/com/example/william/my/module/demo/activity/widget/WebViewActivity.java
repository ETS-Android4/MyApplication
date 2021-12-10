package com.example.william.my.module.demo.activity.widget;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.HashMap;
import java.util.Map;

@Route(path = ARouterPath.Demo.Demo_WebView)
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_webview);

        mWebView = findViewById(R.id.webView);
        //启用JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        //隐藏滚动条
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        //启用视图支持
        mWebView.getSettings().setUseWideViewPort(true);
        //适应屏幕宽度
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //手势缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        //隐藏缩放按钮
        mWebView.getSettings().setDisplayZoomControls(false);
        // 关闭file域访问，禁止file域对http域进行访问
        // setAllowFileAccessFromFileURLs&setAllowUniversalAccessFromFileURLs
        // Android 4.1版本之前这两个API默认是true，需要显式设置为false

        Map<String, String> headers = new HashMap<>();
        mWebView.loadUrl("https://www.baidu.com/", headers);//添加HTTP头信息

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 拦截资源请求
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 拦截资源请求
             */
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            /**
             * 忽略SSL验证
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.addJavascriptInterface(new WebViewInterface(new WebViewJsCallback() {
            @Override
            public void closeWebViewPage() {

            }
        }) {

        }, "interfaceName");
    }

    public static class WebViewInterface {

        private final WebViewJsCallback webViewJsCallback;

        public WebViewInterface(WebViewJsCallback webViewJsCallback) {
            this.webViewJsCallback = webViewJsCallback;
        }

        @JavascriptInterface
        public void closeWebViewPage() {
            if (webViewJsCallback != null) {
                webViewJsCallback.closeWebViewPage();
            }
        }
    }

    public static abstract class WebViewJsCallback {

        public abstract void closeWebViewPage();

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
