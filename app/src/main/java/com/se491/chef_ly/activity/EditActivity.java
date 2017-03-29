package com.se491.chef_ly.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.create_recipe_fragments.FirstFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.FourthFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.SecondFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.ThirdFragment;
import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.Level;
import com.se491.chef_ly.model.RecipeDetail;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends FragmentActivity
        implements FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener {


    private MyAdapter adapter;
    private static String userName = "";
    private static String dir = "";
    private static Uri im ;
    private static String title = "";
    // private static final String urlString ="https://chefly-prod.herokuapp.com/recipe/";
    private final String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);

        List<Fragment> frags = new ArrayList<>();


        frags.add(ThirdFragment.newInstance("Create Recipe", 3));

        frags.add(FourthFragment.newInstance("Create Recipe", 4));
        adapter = new MyAdapter(getSupportFragmentManager(), frags);
        vpPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        //userName = i.getExtras().getString("user");
        title = i.getExtras().getString("title");
        dir= i.getExtras().getString("directions");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void createRecipie(){
        RecipeDetail result;
        String recipeTitle = title;
        String recipeAuthor = "aaa";
        String recipeImage = "";
        String recipeDescription = "salad";
        int  recipeServings=6;
        int    recipeTime=2;
        Level   recipeLevel=null;
        String[] recipeCategories =null;
        ThirdFragment thirdFragment = (ThirdFragment) adapter.getItem(2);
        FourthFragment fourthFragment = (FourthFragment) adapter.getItem(3);

        try {
            ArrayList<Ingredient> ingredients = thirdFragment.getIngredients();
            ArrayList<String> directions = fourthFragment.getDirections();
            // ArrayList<String> directions=dir;
            // directions.add(direction);

            if (ingredients.isEmpty()) {
                Toast.makeText(this, "Recipe must contain at least 1 ingredient", Toast.LENGTH_SHORT).show();

            } else if (directions.isEmpty()) {
                Toast.makeText(this, "Recipe must contain at least 1 direction", Toast.LENGTH_SHORT).show();

            } else {
                result = new RecipeDetail(recipeTitle, recipeAuthor, recipeDescription, recipeServings, recipeTime, recipeLevel.toString(),
                        recipeCategories, recipeImage, ingredients.toArray(new Ingredient[ingredients.size()]), directions.toArray(new String[directions.size()]));

//                GsonBuilder builder = new GsonBuilder();
//                builder.registerTypeAdapter(RecipeDetail.class, new RecipeDetailSerializer());
//                Gson gson = builder.create();
//                String msg = gson.toJson(result);
//                Log.d(TAG, msg);
                //sendToServer(msg);
                //sendToLocalDB(result);
                Intent recipeDetailIntent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
                recipeDetailIntent.putExtra("recipeDetail", result);
                startActivity(recipeDetailIntent);


            }
        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void sendToLocalDB(RecipeDetail r){
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        dbh.createDetailedRecipe(r);
    }
    //    private void sendToServer(String msg) {
//
//        if (NetworkHelper.hasNetworkAccess(EditActivity.this)) //returns true if internet available
//        {
//            //register to listen the data
//            RequestMethod requestPackage = new RequestMethod();
//
//            requestPackage.setEndPoint(urlString);
//            requestPackage.setMethod("POST");
//            requestPackage.setParam("RecipeDetail",msg);
//
//
//            new AsyncTask<RequestMethod, Integer, Long>() {
//                private String resp;
//                @Override
//                protected Long doInBackground(RequestMethod... params) {
//                    for (RequestMethod r : params) {
//                        try {
//                            resp = HttpConnection.downloadFromFeed(r);
//                        }catch(IOException e){
//                            Log.d(TAG, "Error posting recipe -> " + e.getMessage());
//                        }
//                    }
//                    return 1L;
//                }
//
//                @Override
//                protected void onPostExecute(Long aLong) {
//                    super.onPostExecute(aLong);
//                    if(resp == null){
//                        Log.d(TAG, "Recipe not sent to server");
//                    }else{
//                        Log.d(TAG, "Response -> " + resp);
//
//
//                        finish();
//                    }
//
//                }
//            }.execute(requestPackage);
//        }else{
//            Log.d(TAG,"No internet connection");
//        }
//    }
    public static class MyAdapter extends FragmentPagerAdapter{
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

