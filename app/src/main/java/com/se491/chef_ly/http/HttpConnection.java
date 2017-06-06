package com.se491.chef_ly.http;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.compat.BuildConfig;
import android.util.Base64;
import android.util.Log;

import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.MainActivity;
import com.se491.chef_ly.utils.CredentialsManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Helper class for working with a remote server
//okhttp is better for failure recoveries

public class HttpConnection {
    private final String TAG = "HttpConnection";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String statusMessage;

    public HttpConnection(){}


    public String downloadFromFeed(RequestMethod requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        //check for get request
        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        //create request object
        OkHttpClient client;
        if(BuildConfig.DEBUG){
            client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        } else {
            client = new OkHttpClient();
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(address);

        //  Add required HTTP Header
        Map<String ,String > headers = requestPackage.getHeaders();
        if(headers.size() > 0){
            for(String k : headers.keySet()){
                if(k != null){
                    requestBuilder.addHeader( k, headers.get(k) );
                }
            }
        }


        //check for post request
        if (requestPackage.getMethod().equals("POST")) {
            //extract the parameters from the request
            Map<String, String> params = requestPackage.getParams();//get the reference of the pair
            String msg = "";
            if(params.size() ==1){
                for(String key : params.keySet())
                msg = params.get(key);
            }else{
                JSONObject parameter = new JSONObject(params);
                msg = parameter.toString();
            }

            RequestBody body = RequestBody.create(JSON, msg);
            Log.d(TAG, msg);

            requestBuilder.post(body);

            //requestBuilder.addHeader("content-type", "application/json; charset=utf-8");
            requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
        }

        Request request = requestBuilder.build();

        //get response with the use of the method newCall
        Response response = client.newCall(request).execute(); //synchronous request

        //check if the request is successful
        if (response.isSuccessful()) {
            this.statusMessage = response.message();
            return response.body().string();
        } else {
            Log.d(TAG, response.toString());
            Log.d(TAG, "Response Code ------> " + response.code());

            return "Error " +response.code();
            //throw new IOException("Exception: response code " + response.code());
        }
    }


    public String getStatusMessage(){
        if(this.statusMessage != null)
            return this.statusMessage;
        else
            return null;
    }

}