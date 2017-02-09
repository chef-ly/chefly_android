//package com.se491.chef_ly.http;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//
//public class GetRecipeList extends HttpConnection {
//    private String urlString = "https://pure-fortress-13559.herokuapp.com/list";
//    private final String TAG = "GetRecipeList";
//
//    public GetRecipeList(Context c){
//        super(c);
//        try{
//            super.execute(new URL(urlString));
//
//        }catch(MalformedURLException e){
//            Log.d(TAG, "Malformed URL -> " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected Long doInBackground(URL... params) {
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setReadTimeout(5000);
//            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.connect();
//
//            Log.d(TAG,urlConnection.getURL().toString());
//            responseCode =  String.valueOf(urlConnection.getResponseCode());
//            Log.d(TAG,responseCode + " " + urlConnection.getResponseMessage());
//
//            // read data in from connection
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//            while ((line = reader.readLine()) != null)
//            {
//                sb.append(line );
//                sb.append("\n");
//            }
//            reader.close();
//            responseMessage = sb.toString(); //should we return that?
//            // TODO translate json response into array of recipes
//            // recipeList = JSONArray(responseMessage).;
//            Log.d(TAG,responseMessage);
//
//
//        } catch(IOException e){
//            responseCode = "Error -> " + e.getMessage();
//        }
//        finally{
//            if(urlConnection != null){
//                urlConnection.disconnect();
//            }
//
//
//        }
//
//        return 1L;
//    }
//
//}
