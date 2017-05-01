package com.se491.chef_ly.activity.nav_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.se491.chef_ly.R;


public class SearchIngredients extends AppCompatActivity implements View.OnClickListener{

   private EditText ingredients;
    private TextView email;
    private Button addIn;
    private Button findRecipeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredients);
            ingredients = (EditText) findViewById(R.id.ingredient1EditText);

            //findRecipe(selectedIngredient.toString());


            addIn = (Button) findViewById(R.id.addINBtn);
            addIn.setOnClickListener(this);

            findRecipeBtn = (Button) findViewById(R.id.findRecipes);
            findRecipeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addINBtn:
                Editable selectedIngredient = ingredients.getText();
                break;
            case R.id.findRecipes:


                break;

        }
    }


}
