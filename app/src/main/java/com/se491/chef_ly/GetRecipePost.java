package com.se491.chef_ly;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class GetRecipePost extends HttpConnection {
    private String urlString = "https://pure-fortress-13559.herokuapp.com/";
    private final String TAG = "GetRecipePost";
    private int id;

    public GetRecipePost(Context c, int id){
        super(c);
        this.id =id;
        try{
            super.execute(new URL(urlString));

        }catch(MalformedURLException e){
            Log.d(TAG, "Malformed URL -> " + e.getMessage());
        }

    }

    @Override
    protected Long doInBackground(URL... params) {
        HttpURLConnection urlConnection = null;
        String code = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);  // sets method to POST

            String result = URLEncoder.encode("id", "UTF-8") +
                    "=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(result);
            wr.flush();
            wr.close();

            code = String.valueOf(urlConnection.getResponseCode());

            // read data in from connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            responseMessage = sb.toString();
            Log.d(TAG,responseMessage);

        } catch(IOException e){
            responseCode = "Error -> " + code + " " + e.getMessage();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }

        }

        return 1L;
    }

}
