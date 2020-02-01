package org.netevolved;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.testbenchmobile.MainActivity;
import com.example.testbenchmobile.R;

import org.netevolved.android.CameraActivity;
import org.openedit.ModuleManager;
import org.openedit.WebServer;
import org.openedit.data.SearcherManager;
import org.openedit.page.manage.PageManager;
import org.openedit.servlet.OpenEditEngine;

public class EvolvedCore {

    public ModuleManager fieldModuleManager;
    protected String fieldCatalogId;
    protected String fieldApplicationid;
    protected OpenEditEngine fieldEngine;
    protected WebServer fieldWebServer;


    public WebServer getWebServer() {
        if (fieldWebServer == null) {
            fieldWebServer = (WebServer) getModuleManager().getBean("WebServer");
        }
        return fieldWebServer;
    }

    public SearcherManager getSearcherManager(){
        return (SearcherManager)getEngine().getModuleManager().getBean(getCatalogId(), "searcherManager");
    }



    public OpenEditEngine getEngine() {
        return fieldEngine;
    }

    public void setEngine(OpenEditEngine inEngine) {
        fieldEngine = inEngine;
    }

    public String getCatalogId() {
        return fieldCatalogId;
    }

    public void setCatalogId(String inCatalogId) {
        fieldCatalogId = inCatalogId;
    }

    public String getApplicationid() {
        return fieldApplicationid;
    }

    public void setApplicationid(String inApplicationid) {
        fieldApplicationid = inApplicationid;
    }

    public ModuleManager getModuleManager() {
        return fieldModuleManager;
    }

    public void setModuleManager(ModuleManager inModuleManager) {
        fieldModuleManager = inModuleManager;
    }


    public Object getBean(String inBean){
        return getModuleManager().getBean(inBean);
    }


    public Context getApplicationContext(){
       return getWebServer().getApplicationConext();
    }


    public Activity getActivity(){
        return getWebServer().getActivity();
    }


    public void toast(final String inToast){
        Context context = getApplicationContext();
        CharSequence text = inToast;
        int duration = Toast.LENGTH_SHORT;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), inToast, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void createNotification(String inTitle, String inText){
createNotificationChannel();
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "EVOLVEDCORE")
         //   .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(inTitle)
            .setContentText(inText)
//            .setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_HIGH);

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Evolved Core";
            String description = "Evolved Core";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("EVOLVEDCORE", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public void launchActivity(String inBean) throws Exception{
       MainActivity activity = (MainActivity) getActivity();
       activity.launchActivity( inBean);
    }


    public PageManager getPageManager() {
       return getEngine().getPageManager();
    }
}
