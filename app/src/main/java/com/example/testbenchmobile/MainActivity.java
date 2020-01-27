package com.example.testbenchmobile;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
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

import java.io.InputStream;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {





    public OpenEditEngine getEngine() {
        return fieldEngine;
    }

    public void setEngine(OpenEditEngine inEngine) {
        fieldEngine = inEngine;
    }

    protected OpenEditEngine fieldEngine;
    protected WebServer fieldWebServer;

    public WebServer getWebServer() {
        return fieldWebServer;
    }

    public void setWebServer(WebServer inWebServer) {
        fieldWebServer = inWebServer;
    }

    public VelocityEngine getVelocityEngine() {
        if (fieldVelocityEngine == null) {
            fieldVelocityEngine = new VelocityEngine();
            initEngine();
        }
        return fieldVelocityEngine;
    }

    private void initEngine() {
        Velocity.setProperty( "runtime.log.logsystem.class", getClass().getName() );



    }

    public void setVelocityEngine(VelocityEngine inVelocityEngine) {
        fieldVelocityEngine = inVelocityEngine;
    }

    protected VelocityEngine fieldVelocityEngine;



    public void init()
    {


        BaseWebServer server = new BaseWebServer();  //Singleton?
        server.setActivity(this);
        server.setApplicationConext(getApplicationContext());
        server.setRootDirectory(getApplicationContext().getFilesDir());
        server.setNodeId("android-netevolved");
        server.initialize();
        server.getBeanLoader().registerSingleton("WebServer",this);
        fieldEngine = server.getOpenEditEngine();
        server.finalizeStartup();
        fieldWebServer = server;



    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        setupApp();

        WebView webview = findViewById(R.id.webview);

        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this))
                .build();

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
//                return assetLoader.shouldInterceptRequest(request.getUrl());

                VelocityContext context = new VelocityContext();
                context.put("ian", "washere");
                String content = "This is a test of $ian" +  getApplicationContext().getFilesDir();
                StringWriter writer = new StringWriter();
                //getVelocityEngine().mergeTemplate(content, "UTF-8",context, writer);
                //  getVelocityEngine().evaluate(context, writer, inPage.getPath(), in);
                getVelocityEngine().evaluate(context, writer, "UTF-8", content);
                String result = writer.toString();


                Uri path = request.getUrl();
                String root = path.getPath();



                RequestUtils rutil = (RequestUtils) getEngine().getModuleManager().getBean("requestUtils");
                BaseWebPageRequest req = (BaseWebPageRequest) rutil.createVirtualPageRequest(root,new BaseUser(),null);





                Page map = req.getPage();
                StringWriter newwriter = new StringWriter();

                map.generate(req, newwriter);
                String output = newwriter.toString();





                InputStream targetStream = IOUtils.toInputStream(output);

                WebResourceResponse response = new WebResourceResponse("text/html", "UTF-8", targetStream);



                return response;


            }
        });
        // Assets are hosted under http(s)://appassets.androidplatform.net/assets/... .
        // If the application's assets are in the "main/assets" folder this will read the file
        // from "main/assets/www/index.html" and load it as if it were hosted on:
        // https://appassets.androidplatform.net/assets/www/index.html
        webview.loadUrl("https://appassets.androidplatform.net/index.html");




    }

    private void setupApp() {
        PageManager pm = getEngine().getPageManager();
        Page page = pm.getPage("index.html");
        StringItem content = new StringItem("/test/saveme.html", "I am some content" ,"UTF-8");
        //item.setContent("A test of the page manager");
        page.setContentItem(content);
        pm.putPage(page);


        page = null;
        page = pm.getPage("/test/saveme.html");
        String result = page.getContent();

        Log.println(Log.INFO,"something", result);


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
}

