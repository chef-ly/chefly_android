package com.se491.chef_ly.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.create_recipe_fragments.FirstFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.FourthFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.SecondFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.ThirdFragment;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.Level;
import com.se491.chef_ly.model.RecipeDetail;
import com.se491.chef_ly.utils.NetworkHelper;
import com.se491.chef_ly.utils.RecipeDetailSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends FragmentActivity
        implements FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener {


    private MyAdapter adapter;
    private static String userName = "";
    private static final String urlString ="https://chefly-prod.herokuapp.com/recipe/";
    private final String TAG = "CreateRecipeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);

        List<Fragment> frags = new ArrayList<>();
        Fragment first = new FirstFragment(); //FirstFragment.newInstance("Create Recipe", 1);
        Bundle b = new Bundle();
        b.putString("title", "Create Recipe");
        b.putInt("pageNum", 1);
        b.putString("user", userName);
        first.setArguments(b);
        frags.add(first);
        frags.add(SecondFragment.newInstance("Create Recipe", 2));
        frags.add(ThirdFragment.newInstance("Create Recipe", 3));

        frags.add(FourthFragment.newInstance("Create Recipe", 4));
        adapter = new MyAdapter(getSupportFragmentManager(), frags);
        vpPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        userName = i.getExtras().getString("user");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void createRecipie(){
        RecipeDetail result;

        FirstFragment firstFragment = (FirstFragment)adapter.getItem(0);
        SecondFragment secondFragment = (SecondFragment) adapter.getItem(1);
        ThirdFragment thirdFragment = (ThirdFragment) adapter.getItem(2);
        FourthFragment fourthFragment = (FourthFragment) adapter.getItem(3);

        String recipeTitle = firstFragment.getRecipeTitle();
        String recipeAuthor = firstFragment.getRecipeAuthor();
        String recipeImage = firstFragment.getRecipeImage();
        String recipeDescription = firstFragment.getRecipeDescription();
        int recipeTime;
        try{
            String recipeTimeString = secondFragment.getRecipeTime();
            if(!recipeTimeString.isEmpty()){
                recipeTime = Integer.parseInt(recipeTimeString);
            }else{
                recipeTime = 0;
            }
        }catch (NumberFormatException e){
            recipeTime = 0;
        }

        int recipeServings;
        try{
            String recipeServingsString = secondFragment.getRecipeServings();
            if(recipeServingsString.isEmpty()){
                recipeServings = Integer.parseInt(recipeServingsString);
            } else{
                recipeServings = 0;
            }
        }catch (NumberFormatException e ){
            recipeServings = 0;
        }

        Level recipeLevel = secondFragment.getRecipeLevel();
        String[] recipeCategories = secondFragment.getRecipeCategories();
        try {
            ArrayList<Ingredient> ingredients = thirdFragment.getIngredients();
            ArrayList<String> directions = fourthFragment.getDirections();


            if (recipeTitle.isEmpty()) {
                Toast.makeText(this, "Recipe Title cannot be blank", Toast.LENGTH_SHORT).show();

            } else if (ingredients.isEmpty()) {
                Toast.makeText(this, "Recipe must contain at least 1 ingredient", Toast.LENGTH_SHORT).show();

            } else if (directions.isEmpty()) {
                Toast.makeText(this, "Recipe must contain at least 1 direction", Toast.LENGTH_SHORT).show();

            } else {

                result = new RecipeDetail(recipeTitle, recipeAuthor, recipeDescription, recipeServings, recipeTime, recipeLevel,
                        recipeCategories, recipeImage, ingredients.toArray(new Ingredient[ingredients.size()]), directions.toArray(new String[directions.size()]));

                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(RecipeDetail.class, new RecipeDetailSerializer());
                Gson gson = builder.create();
                String msg = gson.toJson(result);
                Log.d(TAG, msg);
                sendToServer(msg, result);
                //sendToLocalDB(result);
                Intent recipeDetailIntent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
                recipeDetailIntent.putExtra("recipeDetail", result);
                startActivity(recipeDetailIntent);


            }
        }catch (Exception e){
            Log.d(TAG, "Exception :" + e.getMessage());
        }
    }

    private void sendToServer(String msg, final RecipeDetail r) {

        if (NetworkHelper.hasNetworkAccess(CreateRecipeActivity.this)) //returns true if internet available
        {
            //register to listen the data
            RequestMethod requestPackage = new RequestMethod();

            requestPackage.setEndPoint(urlString);
            requestPackage.setMethod("POST");
            requestPackage.setParam("RecipeDetail",msg);


            new AsyncTask<RequestMethod, Integer, Long>() {
                private String resp;
                @Override
                protected Long doInBackground(RequestMethod... params) {
                    for (RequestMethod r : params) {
                        try {
                            resp = HttpConnection.downloadFromFeed(r);
                            resp = resp.substring(1,resp.length()-1); //Remove quotes
                        }catch(IOException e){
                            Log.d(TAG, "Error posting recipe -> " + e.getMessage());
                        }
                    }
                    return 1L;
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);
                    if(resp == null){
                        Log.d(TAG, "Recipe not sent to server");
                        Toast.makeText(CreateRecipeActivity.this, "There was an error processing your request, please check your recipe and try again", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d(TAG, "Response -> " + resp);
                        RecipeDetail local = new RecipeDetail(resp, r.getName(), r.getAuthor(), r.getDescription(), r.getServes(), r.getTime(), r.getLevel(), r.getCategories(), r.getImage().toString(), r.getIngredients(), r.getDirections());
                        // Save to local db
                        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
                        dbh.createDetailedRecipe(local);

                        Intent returnIntent = new Intent();
                        setResult(CreateRecipeActivity.RESULT_OK,returnIntent);
                        returnIntent.putExtra("recipe", local);
                        finish();
                    }

                }
            }.execute(requestPackage);
        }else{
            Log.d(TAG,"No internet connection");
        }
    }
    private static class MyAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragments;
        //page 1 -> FirstFragment - recipe title, image, and description
        //page 2 -> SecondFragment -time, serves, categories
        //page 3 -> ThirdFragment -ingredients
        //page 4 -> FourthFragment -directions

        MyAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }


    }
}
