package com.example.testbenchmobile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.webkit.WebViewAssetLoader;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.netevolved.android.AndroidBean;
import org.netevolved.android.CameraActivity;
import org.openedit.BaseWebPageRequest;
import org.openedit.BaseWebServer;
import org.openedit.ModuleManager;
import org.openedit.TestFixture;
import org.openedit.WebPageRequest;
import org.openedit.WebServer;
import org.openedit.page.Page;
import org.openedit.page.PageStreamer;
import org.openedit.page.manage.PageManager;
import org.openedit.profile.UserProfile;
import org.openedit.repository.filesystem.StringItem;
import org.openedit.servlet.OpenEditEngine;
import org.openedit.users.BaseUser;
import org.openedit.util.RequestUtils;
import org.openedit.util.URLUtilities;
import org.openedit.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;




public class MainActivity extends AppCompatActivity {


    public static OpenEditEngine getEngine() {
        return fieldEngine;
    }

    public void setEngine(OpenEditEngine inEngine) {
        fieldEngine = inEngine;
    }

    protected static OpenEditEngine fieldEngine;
    protected WebServer fieldWebServer;

    public WebServer getWebServer() {
        return fieldWebServer;
    }

    public void setWebServer(WebServer inWebServer) {
        fieldWebServer = inWebServer;
    }




    public void init() {


        BaseWebServer server = new BaseWebServer();  //Singleton?
        server.setActivity(this);
        server.setApplicationConext(getApplicationContext());
        server.setRootDirectory(getApplicationContext().getFilesDir());


        server.setNodeId("android-netevolved");
        server.initialize();
        server.getBeanLoader().registerSingleton("WebServer", this);
        fieldEngine = server.getOpenEditEngine();
        server.finalizeStartup();
        fieldWebServer = server;


    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        WebView webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
       // webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this))
                .build();

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
//                return assetLoader.shouldInterceptRequest(request.getUrl());


                Uri path = request.getUrl();
                String root = path.getPath();


                RequestUtils rutil = (RequestUtils) getEngine().getModuleManager().getBean("requestUtils");
                BaseWebPageRequest req = (BaseWebPageRequest) rutil.createVirtualPageRequest(root, new BaseUser(), null);


                Page map = req.getPage();
                StringWriter newwriter = new StringWriter();
                try {

                   if(map.isBinary()){
                       WebResourceResponse response = new WebResourceResponse(map.getMimeType(), "UTF-8", map.getInputStream());
                        return response;
                   }
                   else {
                       map.generate(req, newwriter);
                       //   req.getPageStreamer().getOutput().getStream()
                       String output = newwriter.toString();
                       InputStream targetStream = IOUtils.toInputStream(output);
                       WebResourceResponse response = new WebResourceResponse(map.getMimeType(), "UTF-8", targetStream);
                       return response;

                   }

                } catch (Exception e){

                    String trace = e.toString();
                    InputStream targetStream = IOUtils.toInputStream(trace);
                    WebResourceResponse response = new WebResourceResponse("text/html", "UTF-8", targetStream);
                    return response;
                }





            }
        });
       //loads the index page from the evolved-core engine

        try {
            Page old = getEngine().getPageManager().getPage("/WEB-INF/base");
            getEngine().getPageManager().removePage(old);

            ZipUtil util = new ZipUtil();
            InputStream myInput =getApplicationContext().getResources().openRawResource(R.raw.base);
            File output = new File(getApplicationContext().getFilesDir(), "WEB-INF");
            util.unzip(myInput, output);
        } catch (IOException inE) {
            inE.printStackTrace();
        }

        webview.loadUrl("https://appassets.androidplatform.net/index.html");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchActivity(String inBean) throws Exception {


        Class myclass = Class.forName("org.netevolved.android." + inBean +  "Activity");



        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(intent);

        Log.println(Log.INFO, "loading", inBean);
    }
}

