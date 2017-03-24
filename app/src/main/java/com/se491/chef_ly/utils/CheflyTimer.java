package com.se491.chef_ly.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;


public class CheflyTimer {
    private static CheflyTimer mInstance = null;
    private final AlarmManager alarmManager;
    private final ArrayList<TimerInfo> timers;
    private final String TAG = "CheflyTimer";

    private CheflyTimer(Context c){
        alarmManager = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);

        timers = new ArrayList<>();
    }

    public static CheflyTimer getInstance(Context c){
        if(mInstance == null){
            mInstance = new CheflyTimer(c);
        }
        return mInstance;
    }

    public void setTimer(String name, int timeInSeconds, Context c){
        long timeMilis = timeInSeconds * 1000;
        Intent i = new Intent("alarm");
        i.setAction("alarm");
        i.putExtra("name", name);
        PendingIntent pending = PendingIntent.getBroadcast(c, name.hashCode(), i, 0);
        long elapsedRealTime =  SystemClock.elapsedRealtime();
        timers.add(new TimerInfo(name, pending, elapsedRealTime+timeMilis ));


        alarmManager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, elapsedRealTime+timeMilis, pending );
        Log.d(TAG, name + " alarm started " + timeInSeconds + " seconds");
    }
    public void cancelTimer(String name){
        for (TimerInfo info: timers) {
            if(info.getName().equals(name)){
                alarmManager.cancel(info.getPending());
                timers.remove(info);
                break;
            }
        }
    }
    public void cancelAllTimers(){
        for (TimerInfo info: timers) {
            alarmManager.cancel(info.getPending());
            timers.clear();
        }
    }

    public long getTimerStatus(String name){
        for(TimerInfo info : timers){
            if(info.getName().equals(name)){
                return info.getRemainingTime();
            }
        }
        return 0L;
    }
    public void onTimerFinished(String name){
        for(TimerInfo info : timers){
            if(info.getName().equals(name)){
                timers.remove(info);
                break;
            }
        }
    }


    private class TimerInfo{
        private final String name;
        private final PendingIntent pending;
        private final long end;

        TimerInfo(String name, PendingIntent p, long e){
            this.name = name;
            this.pending = p;
            this.end = e;
        }

        int getRemainingTime(){
            return  Long.valueOf((end - SystemClock.elapsedRealtime())/1000).intValue();
        }

        public String getName() {
            return name;
        }

        public PendingIntent getPending() {
            return pending;
        }
    }
}
