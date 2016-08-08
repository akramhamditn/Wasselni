package com.esprit.wasselni.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.esprit.wasselni.Client.MainActivity;
import com.esprit.wasselni.Entities.Client;
import com.esprit.wasselni.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DemandeService extends Service {
    final String TAG = "TGtracker";

    @Override
    public void onCreate() {
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Demande");
                        query.whereEqualTo("CIN", Client.id);
                        query.whereEqualTo("Etat","accepter");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> clientList, ParseException e) {

                                if ((e == null) && (clientList.size() != 0)) {
                                    //########################################
                                    //########################################
                                    Log.e("oui clic OK2", "");
                                    Toast.makeText(getApplicationContext(),"Votre demande est accepter " ,
                                            Toast.LENGTH_LONG).show();
                                    //MainActivity.serviceEtat = true;
                                    Notification notification = new Notification(R.drawable.check, "Wasselni",0);

                                    NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                                    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.activity_notif);
                                    contentView.setImageViewResource(R.id.imageN, R.drawable.markertaxi);
                                    contentView.setTextViewText(R.id.titleN, "Votre demande est accepter ");
                                    //contentView.setTextViewText(R.id.textN, "This is a custom layout");
                                    notification.contentView = contentView;
                                    Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    notificationIntent.putExtra("SQLITE","non");
                                    //notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
                                    notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                                    notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
                                    notification.defaults |= Notification.DEFAULT_SOUND; // Sound
                                    // Cancel the notification after its selected
                                    notification.flags |= Notification.FLAG_AUTO_CANCEL; // Delete
                                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                                    notification.contentIntent = contentIntent;
                                    mNotificationManager.notify(1, notification);
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Demande");
                                    // Retrieve the object by id
                                    query.getInBackground(clientList.get(0).getObjectId(), new GetCallback<ParseObject>() {
                                        public void done(ParseObject gameScore, ParseException e) {
                                            if (e == null) {
                                                // Now let's update it with some new data. In this case, only cheatMode and score
                                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                                gameScore.put("Etat", "vue");
                                                gameScore.saveInBackground();
                                            }
                                        }
                                    });
                                }
                            }


                        });
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 10000);
        this.stopSelf();

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

}
