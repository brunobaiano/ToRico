package com.brunobaiano.torico.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.brunobaiano.torico.MainActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ToRicoService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    long start;
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ToRicoService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ToRicoService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TORICO SERVICE", "ON CREATE ");
        start = System.nanoTime();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TORICO SERVICE", "DESTROY");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public long getRandomNumber() {
        return (System.nanoTime() - start)/1000;
    }
}
