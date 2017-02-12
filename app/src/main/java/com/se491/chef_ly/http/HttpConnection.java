package com.se491.chef_ly.http;

import java.io.IOException;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Helper class for working with a remote server
//okhttp is better for failure recoveries

public class HttpConnection {


    public static String downloadFromFeed(RequestMethod requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();
//check for get request
        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }
//create request object
        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder()
                .url(address);
//check for post request
        if (requestPackage.getMethod().equals("POST")) {
            MultipartBody.Builder builder = new MultipartBody.Builder() //simulate a web form
                    .setType(MultipartBody.FORM);
            //extract the parameters from the request
            Map<String, String> params = requestPackage.getParams();//get the reference of the pair
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
            RequestBody requestBody = builder.build();
            requestBuilder.method("POST", requestBody);
        }

        Request request = requestBuilder.build();
        //get responce with the use of the method newCall
        Response response = client.newCall(request).execute(); //synchornous request
        //check if the request is successful
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception: response code " + response.code());
        }
    }



}