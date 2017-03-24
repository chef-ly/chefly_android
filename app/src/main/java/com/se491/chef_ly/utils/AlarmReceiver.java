package com.se491.chef_ly.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReciever";
    private final CheflyTimer timer;

    public AlarmReceiver(CheflyTimer timer) {
        super();
        this.timer = timer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        Toast.makeText(context, "Alarm!!! " + name, Toast.LENGTH_LONG).show();
        timer.onTimerFinished(name);
        //TODO check if ringtone is null
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
        Log.d(TAG, "Alarm Alarm Alarm");
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                r.stop();
            }
        }, 5000);

    }
}
