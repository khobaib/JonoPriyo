package com.priyo.apps.jonopriyo;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.priyo.apps.jonopriyo.gcm.ServerUtilities;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";
    
    public GCMIntentService() {
        super(Constants.SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
//        Utility.displayMessage(context, "Your device registred with GCM");
        
        JonopriyoApplication appInstance = (JonopriyoApplication) getApplication();
        Long userId = appInstance.getUserId();
        boolean registered = ServerUtilities.register(context, userId, registrationId);
        if (!registered) {
            GCMRegistrar.unregister(context);
        }
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
//        Utility.displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "-------------------------------------------- Received message");

        handleMessage(context, intent);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
//        Utility.displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
//        Utility.displayMessage(context, getString(R.string.gcm_error, errorId));
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
//        Utility.displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }
    
    
    
    private void handleMessage(Context context, Intent intent) {
        Log.e("Jonopriyo App", "Message received");
        Bundle extras = intent.getExtras();
        Log.d("EXTRAS", extras.toString());
        if (extras != null) {
            Log.e("Notification received----------", "" + (String) extras.get("notification"));
            String not = "" + (String) extras.get("notification");
//            Comman.setPrefesetPreferencerence(context, "MESSAGE", not);
            createNotification(context, not);
        }

    }


    Handler handle = new Handler();
    static WakeLock mWakeLock = null;

    private void createNotification(Context context,String text) {
        try {

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!powerManager.isScreenOn()) 
            {
                mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
                mWakeLock.acquire();
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mWakeLock!=null){
                            Log.d("createNotification", "releasing wakelock from handler");
                            mWakeLock.release();
                        }
                    }
                }, 10000);
            }

        } catch (Exception e) {

        }

        playNotificationSound(context);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "New Notification";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);



        CharSequence contentTitle = "Jonopriyo";
        CharSequence contentText = "" + text;
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        notificationIntent.putExtra("message", text);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(0, notification);

    }


    private void playNotificationSound(Context context) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
         
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);     
 
    }

}
