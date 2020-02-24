package org.netevolved.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.netevolved.EvolvedCore;

public class LocationManager implements LocationListener {

    protected EvolvedCore fieldEvolvedCore;
    protected Location fieldLastLocation;
    protected boolean fieldInitialized;

    public boolean isInitialized() {
        return fieldInitialized;
    }

    public void setInitialized(boolean inInitialized) {
        fieldInitialized = inInitialized;
    }

    public Location getLastLocation() {
       if(!isInitialized()){

           if (ContextCompat.checkSelfPermission(getEvolvedCore().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                   != PackageManager.PERMISSION_GRANTED) {
               // Permission is not granted
               ActivityCompat.requestPermissions(getEvolvedCore().getActivity(),
                       new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                       0);
           }


           android.location.LocationManager  lm = (android.location.LocationManager)getEvolvedCore().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
           try {
               HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
               handlerThread.start();
               // Now get the Looper from the HandlerThread
               // NOTE: This call will block until the HandlerThread gets control and initializes its Looper
               Looper looper = handlerThread.getLooper();

               lm.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 5000, 10, this,looper);
           } catch (SecurityException e){
               Log.println(Log.INFO, "gps", "GPS not enabled!");
           }
       }
       return fieldLastLocation;
    }

    public void setLastLocation(Location inLastLocation) {
        fieldLastLocation = inLastLocation;
    }

    public EvolvedCore getEvolvedCore() {
        return fieldEvolvedCore;
    }

    public void setEvolvedCore(EvolvedCore inEvolvedCore) {
        fieldEvolvedCore = inEvolvedCore;
    }


    @Override
    public void onLocationChanged(Location location) {
        fieldLastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
