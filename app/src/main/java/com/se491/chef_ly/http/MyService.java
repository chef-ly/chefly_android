package com.se491.chef_ly.http;


import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.se491.chef_ly.model.Example;


import java.io.IOException;
//manage request with the use of IntentService
public class MyService extends IntentService {
    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";



    public MyService() {
        super("MyService");
    }
    //called automaticly when server starts, receives data as an argument
    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
        try {
            response = HttpConnection.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        Example[] dataItems = gson.fromJson(response, Example[].class);
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
       // messageIntent.putExtra(MY_SERVICE_PAYLOAD, "Service all done!"); //pass data back, set key value and message
       messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems); //pass back the data
        //package the data
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent); //send / broadcast the message
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
