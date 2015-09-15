package com.zhw.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * webive 内置网页浏览器
 * Created by zhonghanwen on 2015/05/1.
 */
public class WebViewActivity extends Activity implements OnClickListener {

    private TitleBar mTitleBar;
    private WebView detailsWebView;
    private String mUrl;
    private String mTitle = "";

    private ProgressBar mProgressBar;
    private Handler mWeakHandler = new Handler();

    private LinearLayout mLlError;
    private Button mBtnReLoad;
    private boolean isError = false;
    private boolean isFinish = false;


    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        initTitleBar();
        init();
        loadurl(detailsWebView, mUrl);
    }

    // 初始化titleBar
    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titleBar);
        mTitleBar.setBackViewOnClickListener(this);
        mTitleBar.setLeftText(mTitle);
    }

    public void init() {

        mLlError = (LinearLayout) findViewById(R.id.ll_error);
        mBtnReLoad = (Button) findViewById(R.id.btn_reload);
        mBtnReLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFinish){
                    isError = false;
                    detailsWebView.reload();//刷新
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.wv_progressBar);

        detailsWebView = (WebView) findViewById(R.id.web_wv);
        // 设置WebView属性，能够执行Javascript脚本
        detailsWebView.getSettings().setJavaScriptEnabled(true);
        detailsWebView.getSettings().setDomStorageEnabled(true);
        detailsWebView.getSettings().setSupportZoom(true); ////设定支持缩放

        detailsWebView.getSettings().setLoadWithOverviewMode(true);

        //实现文件下载功能
        detailsWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent =  new  Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //设置WebView的UserAgent属性
       // String userAgent = detailsWebView.getSettings().getUserAgentString();
       // detailsWebView.getSettings().setUserAgentString(userAgent + " TgnetYwq/" + mVersionName);

        //适配
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            detailsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        detailsWebView.getSettings().setUseWideViewPort(true); ////将图片调整到适合webview的大小
        detailsWebView.getSettings().setLoadsImagesAutomatically(true);  // //支持自动加载图片
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        detailsWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持内容从新布局
        detailsWebView.requestFocus(View.FOCUS_DOWN);


        detailsWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mLlError.setVisibility(View.VISIBLE);
                detailsWebView.setVisibility(View.GONE);
                isError = true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFinish = true;
                if (isError){
                    mLlError.setVisibility(View.VISIBLE);
                    detailsWebView.setVisibility(View.GONE);
                }else {
                    mLlError.setVisibility(View.GONE);
                    detailsWebView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                mLlError.setVisibility(View.VISIBLE);
                detailsWebView.setVisibility(View.GONE);
                isError = true;
            }
        });
        // 设置web视图客户端
        detailsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发

                if (progress == 100) {
//                    if (detailsWebView.getVisibility() == View.GONE && isError) {
//                        detailsWebView.setVisibility(View.VISIBLE);
//                    }
                    mProgressBar.setProgress(progress);
                    mWeakHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    },1500);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(mTitle)) {
                    mTitleBar.setLeftText(title);
                }
            }


        });
    }

    public void loadurl(final WebView view, final String url) {
        mWeakHandler.post(new Runnable() {

            @Override
            public void run() {
                view.loadUrl(url);// 载入网页
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.backView:
                if (detailsWebView.canGoBack()) {
                    detailsWebView.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    // 设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && detailsWebView.canGoBack()) {
            detailsWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
