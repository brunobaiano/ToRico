package com.brunobaiano.torico;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.brunobaiano.torico.services.ToRicoService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;


public class MainActivity extends AppCompatActivity implements ServiceConnection {
    ToRicoService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent servico = new Intent(this, ToRicoService.class);
        //servico.setData(Uri.parse(dataUrl));
// Starts the IntentService
        bindService(servico, this, Context.BIND_AUTO_CREATE);
        mBound = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
         TextView textview = (TextView) findViewById(R.id.valor_hora);
        double valorHora = Double.parseDouble(prefs.getString("edittext_salario", "0")) /
                Double.parseDouble(prefs.getString("edittext_horas_normais", "1"));
        textview.setText( String.format("%.2f",valorHora));
    }

    public void iniciarServico(View view)
    {
        Chronometer cron = (Chronometer) findViewById(R.id.chronometer);
        cron.setBase(SystemClock.elapsedRealtime());
        cron.start();
     if (mBound) {
         long num = mService.getRandomNumber();
         SimpleDateFormat df2 = new SimpleDateFormat("hh:mm:ss");

         Toast.makeText(this, "time: " + df2.format( new Date(num)), Toast.LENGTH_SHORT).show();
     }
    }

    public void pararServico(View view)
    {
        Chronometer cron = (Chronometer) findViewById(R.id.chronometer);
        cron.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBound) {
            unbindService(this);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
// We've bound to LocalService, cast the IBinder and get LocalService instance
        ToRicoService.LocalBinder binder = (ToRicoService.LocalBinder) service;
        mService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBound = false;
    }
}
