package com.qiaoshouliang.downloadpicturefromwebview;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {


        private WebView mWebView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mWebView = (WebView) findViewById(R.id.mainWebView);

            WebSettings w = mWebView.getSettings();
            w.setJavaScriptEnabled(true);
            w.setLoadsImagesAutomatically(true);
            w.setUseWideViewPort(true);
            w.setJavaScriptCanOpenWindowsAutomatically(true);

            w.setSaveFormData(true);
            w.setLoadWithOverviewMode(true);
            // w.setPageCacheCapacity();

            // WebView inside Browser doesn't want initial focus to be set.
            w.setNeedInitialFocus(false);
            // Browser does not support multiple windows
            w.setSupportMultipleWindows(false);

            // HTML5 API flags
            w.setAppCacheEnabled(true);
            w.setDatabaseEnabled(true);
            w.setDomStorageEnabled(true);
            w.setGeolocationEnabled(true);

            // HTML5 configuration parameters.
            w.setAppCacheEnabled(true);
            w.setDatabaseEnabled(true);
            w.setDomStorageEnabled(true);
            w.setGeolocationEnabled(true);

            w.setCacheMode(WebSettings.LOAD_DEFAULT);


            mWebView.setVerticalScrollbarOverlay(true);
            mWebView.setHorizontalScrollBarEnabled(false);

            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
//				return super.shouldOverrideUrlLoading(view, url);
                }
            });
            mWebView.loadUrl("http://www.58pic.com");

            mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    WebView webView = (WebView) v;
                    WebView.HitTestResult hr = webView.getHitTestResult();
//		Toast.makeText(getActivity(), "长按", Toast.LENGTH_LONG).show();

                    int type = hr.getType();
                    Toast.makeText(MainActivity.this, ""+type, Toast.LENGTH_LONG).show();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

                        String imageUrl = hr.getExtra();


                        if (!imageUrl.startsWith("http")) {
                            return false;
                        }
//			Toast.makeText(getActivity(), imageUrl, Toast.LENGTH_LONG).show();

                        File file = new File(MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getFilenameFromURL(imageUrl));

                        DownloadManager downloadManager = (DownloadManager) MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                        request.setDestinationUri(Uri.fromFile(file));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);

                        return true;
                    }

                    return false;
                }
            });

        }

    protected String getFilenameFromURL(String url) {
        String[] p = url.split("/");
        String s = p[p.length - 1];
        if (s.indexOf("?") > -1) {
            return s.substring(0, s.indexOf("?"));
        }
        return s;
    }

}
