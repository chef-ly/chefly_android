package com.se491.chef_ly.http;


import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.RecipeList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

//manage request with the use of IntentService
public class MyService extends IntentService {
    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public static final String REQUEST_PACKAGE = "requestPackage";

    public MyService() {
        super("MyService");
    }
    //called automaticly when server starts, receives data as an argument
    @Override
    protected void onHandleIntent(Intent intent) {

        RequestMethod requestPackage =
                intent.getParcelableExtra(REQUEST_PACKAGE);

        String response;
        try {
            response = HttpConnection.downloadFromFeed(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Uri.class, new JsonDeserializer<Uri>() {
            @Override
            public Uri deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return Uri.parse(json.toString());
            }
        });
        Gson gson = builder.create();
        Type type = new TypeToken<RecipeList>(){}.getType();

        RecipeList dataItems = gson.fromJson(response, type);
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        // messageIntent.putExtra(MY_SERVICE_PAYLOAD, "Service all done!"); //pass data back, set key value and message
        if(dataItems != null){
            messageIntent.putParcelableArrayListExtra(MY_SERVICE_PAYLOAD, dataItems.getRecipes()); //pass back the data
        }else{
            messageIntent.putParcelableArrayListExtra(MY_SERVICE_PAYLOAD, new ArrayList<Recipe>()); //pass back the data
        }



        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
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
