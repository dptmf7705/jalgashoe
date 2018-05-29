package com.dankook.jalgashoe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.dankook.jalgashoe.navi.NaviActivity;
import com.skt.Tmap.TMapPoint;

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private static final int LOCATION_INTERVAL = 1000; // 위치변경 인식 최소시간
    private static final float LOCATION_DISTANCE = 5; // 위치변경 인식 최소거리

    private LocationListener[] mLocationListeners = new LocationListener[] { // 위치정보 프로바이더
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private LocationManager mLocationManager = null; // 위치정보 매니저

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    // 현재 위치 변경 콜백 받아서 액티비티에 전달
    private void processLocationCallback(Location location) {
        Intent showIntent = new Intent(getApplicationContext(), NaviActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        showIntent.putExtra(NaviActivity.EXTRA_FROM_SERVICE_LATITUDE, location.getLatitude());
        showIntent.putExtra(NaviActivity.EXTRA_FROM_SERVICE_LONGITUDE, location.getLongitude());
        startActivity(showIntent);
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        // 위치변경 콜백 등록
        for(LocationListener listener : mLocationListeners){
            listener.setLocationChangedListener(new LocationListener.LocationChangedListener() {
                @Override
                public void onLocationChanged(Location location) {
                    processLocationCallback(location);
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private static class LocationListener implements android.location.LocationListener {
        LocationChangedListener locationChangedListener;
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        public void setLocationChangedListener(LocationChangedListener locationChangedListener) {
            this.locationChangedListener = locationChangedListener;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            if(locationChangedListener != null){
                locationChangedListener.onLocationChanged(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }

        public interface LocationChangedListener{
            void onLocationChanged(Location location);
        }
    }

}
