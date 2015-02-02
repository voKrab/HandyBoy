package com.vallverk.handyboy.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationManager {

    public static Location imHere;
    private static LocationManager locationManager;
    private static LocationListener locationListener;

    public interface OnLocationChangeListener{
        public void onLoacationChange(Location location);
    }

    public static OnLocationChangeListener onLocationChangeListener;

    public static void setOnLocationChangeListener(OnLocationChangeListener onLocationChange){
        onLocationChangeListener = onLocationChange;
    }

    private static LocationManager getLocationManager(Context context){
        if(locationManager == null){
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        return locationManager;
    }

    private static LocationListener getLocationListener(){
        if(locationListener == null){
            locationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }

                @Override
                public void onLocationChanged(Location location) {
                    imHere = location;
                    if(onLocationChangeListener != null){
                        onLocationChangeListener.onLoacationChange(location);
                    }
                }
            };
        }
        return locationListener;
    }

    public static void setUpLocationListener(Context context) {
        Location lastKnownLocation = getLocationManager(context).getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation != null){
            imHere = lastKnownLocation;
        }else{
            imHere = getLocationManager(context).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        getLocationManager(context).requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, getLocationListener());
        getLocationManager(context).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, getLocationListener());
    }

    public static void removeLocationListener(Context context) {
        getLocationManager(context).removeUpdates(getLocationListener());
    }

    public static boolean checkEnabled(Context context) {
        if(getLocationManager(context).isProviderEnabled(LocationManager.GPS_PROVIDER) || getLocationManager(context).isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true;
        }
        return false;
    }

    public static boolean checkGPSEnabled(Context context) {
        return getLocationManager(context).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean checkNetworkEnabled(Context context) {
        return getLocationManager(context).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}