package com.se491.chef_ly;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

abstract class HttpConnection extends AsyncTask<URL, Integer, Long> {

    private final String TAG = "HttpConnection";
    private Context context;
    protected String responseCode;
    protected String responseMessage;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        Toast.makeText(context, responseCode, Toast.LENGTH_LONG).show();
        Log.d(TAG,responseCode);
    }

    HttpConnection(Context c){
        context = c;
    }

    public String getResponseCode(){
        return responseCode;
    }
    public String getResponseMessage(){
        return responseMessage;
    }

}
