package com.example.rj.learning;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;


public class BackgrndService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    public BackgrndService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private static final String TAG = "LocationActivity";


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationManager mlocationmanger;
    int bool = 0;
    CountDownTimer cnt;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onCreate() {
        super.onCreate();

        if (!isGooglePlayServicesAvailable()) {
            stopLocationService();
        }
        mlocationmanger = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        cnt = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                stopLocationService();

            }
        }.start();
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.connect();
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//
//
//        }


    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.w("hhhaaha", "onStart is called");


    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("hhaahaa", "Connection suspended stop here ");


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w("hhaahaa", "Connection failed stop here ");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.w(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            SharedPreferences pref = getSharedPreferences("Location", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences getdata = getSharedPreferences("Login_boolean", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putFloat("longitude", (float) mCurrentLocation.getLongitude());
            editor.putFloat("latitude", (float) mCurrentLocation.getLatitude());
            editor.commit();
            JSONObject obj = new JSONObject();
            try {


                obj.put("longitude", Float.toString((float) mCurrentLocation.getLongitude()));
                obj.put("latitude", Float.toString((float) mCurrentLocation.getLatitude()));
                obj.put("email", getdata.getString("email", ""));
                obj.put("token", getdata.getString("hashkey", ""));
                new LocationUpdateTask(Constant.baseip + "users/positionchange", obj).execute();


            } catch (Exception e) {
            }
            ;


            Log.w("hi", mCurrentLocation.getLatitude() + "  " + mCurrentLocation.getLongitude());

        }
        stopLocationService();
    }


    public void stopLocationService() {

        Log.w("haha", "killing begins");

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        cnt.cancel();
        cnt = null;
        Intent alarm = new Intent(this, BackgrndService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarm, 0);
        if (bool == 0) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                    + 1000 * 300, pendingIntent);
            bool = 1;
        }
        stopSelf();
    }

}
