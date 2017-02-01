package com.se491.chef_ly;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class HttpConnection extends AsyncTask<URL, Integer, Long> {

    private final String urlStringGet = "https://pure-fortress-13559.herokuapp.com/list";
    private final String urlStringPost = "https://pure-fortress-13559.herokuapp.com/";
    private final String TAG = "HttpConnection";
    private Context context;
    private String responseCode;
    private String responseMessage;

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
//        Long request= doPostRequest(params);
//        return request;
        doGetRequest(params);
        //doPostRequest(urlStringPost);
        return (long)0;
    }


    public void doGetRequest(URL... params) {
        HttpURLConnection urlConnection = null;
        try {
            for(URL url : params){  //we why need for?
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                Log.d(TAG,urlConnection.getURL().toString());
                responseCode =  String.valueOf(urlConnection.getResponseCode());
                Log.d(TAG,responseCode + " " + urlConnection.getResponseMessage());

                // read data in from connection
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                responseMessage = sb.toString(); //should we return that?
                Log.d(TAG,responseMessage);
            }

        } catch(IOException e){
            responseCode = "Error -> " + e.getMessage();
        }
        finally{
            urlConnection.disconnect();
        }

        // return urlConnection.getLastModified();
    }

    public void doPostRequest(String urlString) { //should we pass the post data as parameter?
        HttpURLConnection urlConnection = null;
        String code = null;
        try {

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            //urlConnection.setRequestProperty("Content-Type", "application/json");


            //String postData = "{\"id\":2,\"name\":\"Pepperoni Pizza\"}";
            StringBuilder result = new StringBuilder();
            result.append(URLEncoder.encode("id", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode("2", "UTF-8"));

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(result.toString());
            wr.flush();
            wr.close();

            code = String.valueOf(urlConnection.getResponseCode());
            //urlConnection.connect();
            // read data in from connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            responseMessage = sb.toString();
            Log.d(TAG,responseMessage);
            reader.close();


        } catch(IOException e){
            responseCode = "Error -> " + code + " " + e.getMessage();
        }
        finally{
            urlConnection.disconnect();
        }
        // return urlConnection.getLastModified();

    }

    public HttpConnection(Context c){
        context = c;
        try{
            execute(new URL(urlStringGet));
        }catch(MalformedURLException e){
            Log.d(TAG, "Malformed URL -> " + e.getMessage());
        }

    }
    public String getResponseCode(){
        return responseCode;
    }
    public String getResponseMessage(){
        return responseMessage;
    }

}
