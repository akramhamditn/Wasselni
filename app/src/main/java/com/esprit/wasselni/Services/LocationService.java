package com.esprit.wasselni.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.Entities.Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    final String TAG = "TGtracker";
    private static final int ERROR_DIALOG_REQUEST = 1 ;
    GoogleApiClient mGoogleApiClient;
    //Location object used for getting latitude and longitude
    Location mLastLocation;
    @Override
    public void onCreate() {
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        buildGoogleApiClient();
                        mGoogleApiClient.connect();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 10000);
        this.stopSelf();

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    /*
    Checking the google play services is available
     */
    private boolean checkServices() {
        //returns a integer value
        int isAvailable= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        //if connection is available
        if (isAvailable== ConnectionResult.SUCCESS){
            return true;
        }else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            /*Dialog dialog=GooglePlayServicesUtil.getErrorDialog(isAvailable,getApplication().,ERROR_DIALOG_REQUEST);
            dialog.show();*/
        }else {
            //Toast.makeText(MapActivity.this, "Cannot connnect to mapping Service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i(TAG, "Received start id " + startId + ": " + intent);
        Log.e(TAG, "call me redundant BABY!  onStartCommand service");
        // this service is NOT supposed to execute anything when it is called
        // because it may be called inumerous times in repetition
        // all of its action is in the onCreate - so as to force it to happen ONLY once
        return 1;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //getting the latitude value
            final double latitudeValue=mLastLocation.getLatitude();
            Log.e("latitude", String.valueOf(latitudeValue));
            //getting the longitude value
            final double longitudeValue=mLastLocation.getLongitude();
            Log.e("latitude", String.valueOf(longitudeValue));
            if(checkServices()){
                //***********Defin latitude and longitude*************
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
                // Retrieve the object by id
                query.getInBackground(Chauffeur.idObj, new GetCallback<ParseObject>() {
                    public void done(ParseObject testObject, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            testObject.put("Latitude", latitudeValue);
                            testObject.put("Longitude", longitudeValue);
                            testObject.saveInBackground();
                        }
                    }
                });
                Chauffeur.latitude=latitudeValue;
                Chauffeur.longitude=longitudeValue;
                //***********Defin latitude and longitude**************
                //***********Defin latitude and longitude*************
                 ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Utilisateur");
                 // Retrieve the object by id
                 query1.getInBackground(Client.idObj, new GetCallback<ParseObject>() {
                 public void done(ParseObject testObject, ParseException e) {
                 if (e == null) {
                 // Now let's update it with some new data. In this case, only cheatMode and score
                 // will get sent to the Parse Cloud. playerName hasn't changed.
                 testObject.put("Latitude", latitudeValue);
                 testObject.put("Longitude", longitudeValue);
                 testObject.saveInBackground();
                 }
                 }
                 });
                 Client.latitude=latitudeValue;
                 Client.longitude=longitudeValue;
                 //***********Defin latitude and longitude**************
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GettingLocation", "onConnectionFailed");
    }
}
