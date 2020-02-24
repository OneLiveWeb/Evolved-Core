package org.netevolved.testbench_mobile;


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
import androidx.webkit.WebViewAssetLoader;

import com.example.testbenchmobile.MainActivity;

import org.apache.commons.io.IOUtils;
import org.netevolved.android.CameraActivity;
import org.netevolved.android.EvolvedCoreActivity;
import org.netevolved.testbench_mobile.R;
import org.openedit.BaseWebPageRequest;
import org.openedit.BaseWebServer;
import org.openedit.WebServer;
import org.openedit.page.Page;
import org.openedit.servlet.OpenEditEngine;
import org.openedit.users.BaseUser;
import org.openedit.util.RequestUtils;
import org.openedit.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;




public class TestBenchMain extends MainActivity implements EvolvedCoreActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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

