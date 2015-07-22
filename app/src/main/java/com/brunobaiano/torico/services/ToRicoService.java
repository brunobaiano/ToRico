package com.brunobaiano.torico.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

    //  final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.

    final Messenger mMessenger = new Messenger(new IncomingHandler());

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

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Bundle data = msg.getData();
            long time = data.getLong("time");
            Log.d("TORICO SERVICE", "long: " + TimeUnit.MILLISECONDS.toSeconds(time));

            enviarResposta(msg.replyTo, time);

        }
    }

    private void enviarResposta(Messenger replyTo, long time) {
        // pegar valores nas preferências
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(this);
        double salario = Double.parseDouble(prefs.getString("edittext_salario", "0"));
        long horas_normais = Long.parseLong(prefs.getString("edittext_horas_normais", "1"));
        double adicional = Double.parseDouble(prefs.getString("edittext_adicional", "1"));

        // valor do salario em milisegundos
        double salarioPorSegundo = salario/TimeUnit.HOURS.toSeconds(horas_normais);
        //Send data as a String
        Bundle b = new Bundle();
        b.putString("valor", salarioPorSegundo* TimeUnit.MILLISECONDS.toSeconds(time) + "");
        //cria uma nova msg pra ser enviada de volta com os cálculos
        Message msgEnviada = new Message();
        msgEnviada.setData(b);
        try {
            replyTo.send(msgEnviada);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
