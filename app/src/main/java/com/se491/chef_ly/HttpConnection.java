package com.se491.chef_ly;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Don on 1/29/2017.
 */

public class HttpConnection extends AsyncTask<URL, Integer, Long> {

    private final String urlString = "http://www.google.com";
    private final String TAG = "HttpConnection";
    private Context context;
    private String responseCode;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Long aLong) {
        super.onCancelled(aLong);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Long doInBackground(URL... params) {

        HttpURLConnection urlConnection = null;
        try {
            for(URL url : params){
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d(TAG,urlConnection.getURL().toString());
                responseCode =  String.valueOf(urlConnection.getResponseCode());
                Log.d(TAG,responseCode);
                /* TODO  read data in from connection
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                int line;
                while ((line = in.read()) != 0) {
                    line.append(line).append('\n');
                }
                */
            }

        } catch(IOException e){
            responseCode = "Error -> " + e.getMessage();
        }
        finally{
            urlConnection.disconnect();
        }

        return null;
    }

    public HttpConnection(Context c){
        context = c;
        try{
            execute(new URL(urlString));
        }catch(MalformedURLException e){
            Log.d(TAG, "Malformed URL -> " + e.getMessage());
        }

    }
    public String getResponseCode(){
        return responseCode;
    }

}
