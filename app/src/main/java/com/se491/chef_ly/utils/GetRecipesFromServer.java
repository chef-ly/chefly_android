package com.se491.chef_ly.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.RecipeList;

import java.io.IOException;
import java.lang.reflect.Type;

public class GetRecipesFromServer extends AsyncTaskLoader<RecipeList> {
    private RequestMethod requestPackage;
    private final String TAG = "GETRECIPESFROMSERVER";
    public GetRecipesFromServer(Context c, RequestMethod method){
        super(c);
        this.requestPackage = method;
    }
    @Override
    public RecipeList loadInBackground() {
        String response = "";
        Log.d(TAG, requestPackage.getMethod() + " " + requestPackage.getEndpoint());
        try {
            response = HttpConnection.downloadFromFeed(requestPackage);
            Log.d(TAG, response);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.d(TAG, response);
            return new RecipeList();
        }

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        Type type = new TypeToken<RecipeList>(){}.getType();
        Log.d("AsyncTaskLoader","Response " + getId() + " -> " + response);
        try{
            return  gson.fromJson(response, type);
        }catch(IllegalStateException e){
            Log.d(TAG, "Error - RecipeListObject Required: " + e.getMessage());
            return new RecipeList();
        }

    }
}