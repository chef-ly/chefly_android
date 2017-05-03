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
    public GetRecipesFromServer(Context c, RequestMethod method){
        super(c);
        this.requestPackage = method;
    }
    @Override
    public RecipeList loadInBackground() {
        String response;
        try {
            HttpConnection http = new HttpConnection();
            response = http.downloadFromFeed(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
            return new RecipeList();
        }
        Log.d("AsyncTaskLoader","Response -> " + response);
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        Type type = new TypeToken<RecipeList>(){}.getType();
        return  gson.fromJson(response, type);
    }
}