package com.se491.chef_ly;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitle;
    private ImageView imageView;
    private TextView ingredientView;
    private TextView directionView;
    private Button backBtn;

    private List<String> ingredients;
    private List<String> directions;
    private static final String TAG = "RecipieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeTitle = (TextView) findViewById(R.id.recipeName);
        imageView = (ImageView) findViewById(R.id.image);
        ingredientView = (TextView) findViewById(R.id.ingredientView);
        directionView = (TextView) findViewById(R.id.directionView);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Recipe r = intent.getParcelableExtra("recipe");

        if(r == null){
            recipeTitle.setText("Recipe Not Found :(");
        }else{
            recipeTitle.setText(r.getName());

            try{
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), r.getImage()));

            }catch (IOException e){
                Log.d(TAG, "IOException on load image");
                Log.d(TAG, e.getMessage());

            }


            ingredients = r.getIngredients();
            directions = r.getDirections();

            StringBuilder sb = new StringBuilder();
            int count = 1;
            for(String s : ingredients){
                sb.append(count);
                sb.append(":  ");
                sb.append(s);
                sb.append("\n");
                count++;
            }
            ingredientView.setText(sb.toString());
            sb = new StringBuilder();
            count = 1;
            for(String s : directions){
                sb.append(count);
                sb.append(":  ");
                sb.append(s);
                sb.append("\n");
                count++;
            }
            directionView.setText(sb.toString());

        }



    }
}
