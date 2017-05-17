package com.se491.chef_ly.activity.nav_activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.ListViewFragment;
import com.se491.chef_ly.activity.MainActivity;
import com.se491.chef_ly.activity.RecipeListActivity;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.GetRecipesFromServer;
import com.se491.chef_ly.utils.NetworkHelper;


public class SearchIngredients extends AppCompatActivity implements View.OnClickListener,
     LoaderManager.LoaderCallbacks<RecipeList> {

    private static final String urlString ="http://www.chef-ly.com/search/ingredients?ingredients=";
    //http://www.chef-ly.com/search/ingredients?ingredients=butter,cookies
    private EditText ingredients;
    private final int SEARCHINGREDIENTS = 1345; //???
    private static final String TAG = "SearchIngredients";
    //private Button addIn;
    private Button findRecipeBtn;
    private Editable selectedIngredient;
    private RecipeList serverRecipes;
    private Handler splashHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverRecipes = new RecipeList();
        setContentView(R.layout.activity_search_ingredients);
        //setContentView(R.layout.splash_layout);
        ingredients = (EditText) findViewById(R.id.ingredient1EditText);
           // addIn = (Button) findViewById(R.id.addINBtn);
          //  addIn.setOnClickListener(this);
            findRecipeBtn = (Button) findViewById(R.id.findRecipes);
            findRecipeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findRecipes:
                selectedIngredient = ingredients.getText(); //take the text as ingredient1, ingredient2,..
               // findRecipe(selectedIngredient.toString());
               // String[] splited = selectedIngredient.split(",");

            // Get recipes from server
            if(NetworkHelper.hasNetworkAccess(SearchIngredients.this)) //returns true if internet available
            {

                //Start AsyncTaskLoader to get recipes from server
                RequestMethod requestPackage = new RequestMethod();
                requestPackage.setEndPoint(urlString + selectedIngredient);//find the url
                requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");
                //  serverConnection();
                Bundle searchIngredients = new Bundle();
                searchIngredients.putParcelable("requestPackage", requestPackage);
                getSupportLoaderManager().initLoader(SEARCHINGREDIENTS, searchIngredients,this).forceLoad();

//                splashHandler = new Handler();
//                splashHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //ViewGroup group = (ViewGroup) findViewById(R.id.activity_main);
//
//                        setContentView(R.layout.activity_search_ingredients);
//                    }
//                }, 5000);

            }
            else
            {
                //Toast.makeText(RecipeListActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                Log.d(TAG, "No Internet Connection");
            }

                Intent recipeListIntent = new Intent(SearchIngredients.this, RecipeListActivity.class);
                recipeListIntent.putExtra("name", "aaa");
                recipeListIntent.putExtra("recipeList", serverRecipes);
                Log.d(TAG, "ServerRecipes size = " + serverRecipes.size());
                startActivity(recipeListIntent);
                break;
        }
    }

    //  LoaderManager callback method
    @Override
    public Loader<RecipeList> onCreateLoader(int id, Bundle args) {
        RequestMethod rm = args.getParcelable("requestPackage");
        return  new GetRecipesFromServer(getApplicationContext(), rm);
    }
    //  LoaderManager callback method
    @Override
    public void onLoadFinished(Loader<RecipeList> loader, RecipeList data) {
        int id = loader.getId();
        if(id == SEARCHINGREDIENTS) {
            serverRecipes = data;
            //splashHandler.removeCallbacksAndMessages(null);
           // setContentView(R.layout.activity_search_ingredients);

        }

        Log.d(TAG, "OnLoadFinished " + loader.getId());
    }
    //  LoaderManager callback method
    @Override
    public void onLoaderReset(Loader<RecipeList> loader) {

        if(getTaskId() == SEARCHINGREDIENTS){
            serverRecipes = new RecipeList();
        }
    }


}
