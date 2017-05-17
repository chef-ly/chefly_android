package com.se491.chef_ly.activity.nav_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.RequestMethod;


public class SearchIngredients extends AppCompatActivity implements View.OnClickListener{
    private static final String urlString ="http://www.chef-ly.com/search/ingredients?ingredients=";
   private EditText ingredients;
    //private Button addIn;
    private Button findRecipeBtn;
    private Editable selectedIngredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredients);
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
                RequestMethod requestPackage = new RequestMethod();
                requestPackage.setEndPoint(urlString + selectedIngredient);//find the url
                requestPackage.setMethod("GET");//send the post method request
                Bundle searchIngredients = new Bundle();
                searchIngredients.putParcelable("requestPackage", requestPackage);

                //getSupportLoaderManager().initLoader(SEARCHID, searchRecipes,this).forceLoad();
                break;

        }
    }
    private void findRecipe(String selectedIngredient) {
        //send that sting to the server
        //get the responce as a list
        //show the list below the find recipe
    }

}
