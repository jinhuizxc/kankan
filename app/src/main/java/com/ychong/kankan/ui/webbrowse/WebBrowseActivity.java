package com.ychong.kankan.ui.webbrowse;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebBrowseActivity  extends BaseActivity {
    private EditText inputEt;
    private TextView searchTv;
    private WebView webView;

    private boolean isLoading = false;
    private ErrorFragment errorFragment;
    private FrameLayout frameLayout;

    public static void startAct(Context context){
        Intent intent = new Intent(context,WebBrowseActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_browse;
    }

    private void initListener() {
        searchTv.setOnClickListener(v -> search());
        errorFragment.setRefreshListener(() -> {
            webView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            webView.onResume();
            webView.reload();
        });
    }

    private void search() {
        String inputStr = inputEt.getText().toString();
        if (TextUtils.isEmpty(inputStr)|| isHttpUrl(inputStr)){
            inputStr = "http://www.baidu.com";
            inputEt.setText(inputStr);
        }
        webView.loadUrl(inputStr);

    }

    private void initData() {
        initFragment();
        initWebView();

    }

    private void initWebView(){
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);//支持缩放  默认true
        webSetting.setBuiltInZoomControls(false);//设置内置的缩放控件，若为false,则webview不可缩放
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        //  该页面打开更多链接
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                if (!TextUtils.isEmpty(url)&&hitTestResult==null){
                    webView.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView,url);

            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                Log.e("dddd1","加载错误");
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                webView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        });

        //监听网页加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                if (i < 100 && !isLoading) {
                    isLoading = true;
                    showProgressDialog(WebBrowseActivity.this,"加载网页中",false);
                }
                if (i == 100) {
                    isLoading = false;
                    hideProgressDialog();
                    webView.setVisibility(View.VISIBLE);

                }
            }
        });
    }
    private void initFragment() {
        errorFragment = ErrorFragment.newInstance();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.error_fragment, errorFragment);
        transaction.commit();
    }
    private void initView() {
        inputEt = findViewById(R.id.input_et);
        searchTv = findViewById(R.id.search_tv);
        frameLayout =  findViewById(R.id.error_fragment);

    }

    /**
     * 判断是否为网址
     * @param urls
     * @return
     */
    public static boolean isHttpUrl(String urls) {
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        return  mat.matches();//判断是否匹配
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView!=null){
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView!=null){
            webView.onPause();
            webView.getSettings().setLightTouchEnabled(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (this.webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
