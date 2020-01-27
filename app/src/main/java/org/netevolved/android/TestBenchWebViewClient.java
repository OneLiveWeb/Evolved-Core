package org.netevolved.android;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class TestBenchWebViewClient extends WebViewClient{


    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
       // https://medium.com/@madmuc/intercept-all-network-traffic-in-webkit-on-android-9c56c9262c85


        return super.shouldInterceptRequest(view, request);
    }
}
