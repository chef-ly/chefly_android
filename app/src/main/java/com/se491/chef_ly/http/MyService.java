package com.se491.chef_ly.http;


import android.app.IntentService;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.model.RecipeList;

import java.io.IOException;
import java.lang.reflect.Type;

//manage request with the use of IntentService
public class MyService extends IntentService {
    private static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_RESPONSE_STRING = "myServiceResponseString";
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
        String sender = intent.getStringExtra("Tag");
        Log.d(TAG, "Sender -> " + sender);
        Log.d(TAG, "Request Package Endpoint-> " + requestPackage.getEndpoint() );
        String response;
        try {
            HttpConnection http = new HttpConnection();
            response = http.downloadFromFeed(requestPackage);
        } catch (IOException e) {
            //e.printStackTrace();
            return;
        }
        Log.d(TAG,"Response -> " + response);
        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(Uri.class, new JsonDeserializer<Uri>() {
        //    @Override
        //    public Uri deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //        return Uri.parse(json.toString());
        //    }
        //});
        Gson gson = builder.create();

        RecipeList dataItems;
        Type type = new TypeToken<RecipeList>(){}.getType();
        dataItems = gson.fromJson(response, type);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_RESPONSE_STRING, response);
        // messageIntent.putExtra(MY_SERVICE_PAYLOAD, "Service all done!"); //pass data back, set key value and message
        if(dataItems != null){
            messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems); //pass back the data
        }else{
            // unidentified sender - send empty parcel back
            messageIntent.putExtra(MY_SERVICE_PAYLOAD, new Parcelable() {
                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            }); //pass back the data
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
