package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.RecipeDetail;
import com.se491.chef_ly.utils.NetworkHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitle;
    private ImageView imageView;
    private TextView directionView;
    private Button backBtn;
    private LinearLayout ingredientGroup;
    private Button addToListBtn;
    private CheckBox[] checkBoxes;
    private Button getCookingBtn;

    private RecipeDetail recipeDetail;
    private Ingredient[] ingredients;
    private String[] directions;
    private String[] directionsForCooking;
    private static final String TAG = "RecipeDetailActivity";
    private static final String urlString ="https://chefly-prod.herokuapp.com/recipe/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        final Context c = getApplicationContext();
        recipeTitle = (TextView) findViewById(R.id.recipeName);
        imageView = (ImageView) findViewById(R.id.image);
        directionView = (TextView) findViewById(R.id.directionView);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ingredientGroup = (LinearLayout) findViewById(R.id.ingredientGroup);
        addToListBtn = (Button) findViewById(R.id.addToListBtn);
        addToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                DatabaseHandler handler = new DatabaseHandler(c);
                for(CheckBox cb : checkBoxes){
                    if(cb.isChecked()){
                        handler.addItemToShoppingList(ingredients[cb.getId()], false);
                        count++;
                        cb.setChecked(false);
                        Log.d(TAG,"Added to list -> " +String.valueOf(cb.getText()));
                    }
                }
                Toast.makeText(RecipeDetailActivity.this, count + " items added to list", Toast.LENGTH_SHORT).show();
            }
        });
        getCookingBtn = (Button) findViewById(R.id.getCookingBtn);
        getCookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cookingIntent = new Intent(RecipeDetailActivity.this, GetCookingActivity.class);
                if(directionsForCooking != null){
                    cookingIntent.putExtra("directions", directionsForCooking);
                    startActivity(cookingIntent);
                    finish();
                }else{
                    Toast.makeText(RecipeDetailActivity.this, "Could not find recipe directions", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final String recipeID = intent.getStringExtra("recipe");
        final Context c = getApplicationContext();
        if(NetworkHelper.hasNetworkAccess(RecipeDetailActivity.this)) //returns true if internet available
        {
            //register to listen the data
            RequestMethod requestPackage = new RequestMethod();

            requestPackage.setEndPoint(urlString + recipeID);
            requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");


            new AsyncTask<RequestMethod, Integer, Long>(){
                String resp = "";
                @Override
                protected Long doInBackground(RequestMethod... params) {
                    for(RequestMethod r : params){
                        try {
                            resp = HttpConnection.downloadFromFeed(r);
                            //resp = "{\"_id\":\"58b34ba5b4bc390004204d78\",\"name\":\"Cajun Chicken Pasta\",\"author\":\"Tom\",\"description\":\"Chicken, Pasta, Cajun\",\"feeds\":1,\"time\":3,\"rating\":5,\"level\":\"Easy\",\"__v\":0,\"instructions\":[{\"step\":1,\"instruction\":\"Bring a large pot of lightly salted water to a boil. Add linguini pasta, and cook for 8 to 10 minutes, or until al dente; drain.\",\"_id\":\"58b34ba5b4bc390004204d7c\",\"verbs\":[],\"nouns\":[]},{\"step\":2,\"instruction\":\"Meanwhile, place chicken and Cajun seasoning in a bowl, and toss to coat.\",\"_id\":\"58b34ba5b4bc390004204d7b\",\"verbs\":[],\"nouns\":[]},{\"step\":3,\"instruction\":\"In a large skillet over medium heat, saute chicken in butter until no longer pink and juices run clear, about 5 to 7 minutes. Add green and red bell peppers, sliced mushrooms and green onions; cook for 2 to 3 minutes. Reduce heat, and stir in heavy cream. Season the sauce with basil, lemon pepper, salt, garlic powder and ground black pepper, and heat through.\",\"_id\":\"58b34ba5b4bc390004204d7a\",\"verbs\":[],\"nouns\":[]},{\"step\":4,\"instruction\":\"In a large bowl, toss linguini with sauce. Sprinkle with grated Parmesan cheese.\",\"_id\":\"58b34ba5b4bc390004204d79\",\"verbs\":[],\"nouns\":[]}],\"ingredients\":[{\"name\":\"toast\", \"uom\":\"slice\", \"qty\": 2 },{\"name\":\"jelly\", \"uom\":\"tbsp\", \"qty\": 1}],\"categories\":[\"snack\",\"lunch\",\"tasty\",\"easy\"]}";
                            //TODO parse json into recipe detail update UI
                            GsonBuilder builder = new GsonBuilder();

                            Gson gson = builder.create();
                            Type type;
                            type = new TypeToken<RecipeDetail>(){}.getType();
                            recipeDetail = gson.fromJson(resp, type);
                            Log.d(TAG, recipeDetail.toString());
                        } catch (Exception e){ //IOException e) {
                            //e.printStackTrace();
                            return -1L;
                        }
                        Log.d(TAG, resp);
                    }
                    return 1L;
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);

                    if(recipeDetail == null){
                        recipeTitle.setText(R.string.recipeNotFound);
                    }else{
                        recipeTitle.setText(recipeDetail.getName());

                        try{
                            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), recipeDetail.getImage()));

                        }catch (IOException e){
                            Log.d(TAG, "IOException on load image");
                            Log.d(TAG, e.getMessage());

                        }

                        ingredients = recipeDetail.getIngredients();
                        directions = recipeDetail.getDirections();
                        if(ingredients == null){
                            ingredients = new Ingredient[0];
                        }
                        if(directions == null){
                            directions = new String[0];
                        }

                        checkBoxes = new CheckBox[ingredients.length];
                        int states[][] = {{android.R.attr.state_checked}, {}};
                        int white = getColor(c,R.color.white);
                        int colors[] = {white, white};
                        int count = 0;
                        for(Ingredient s : ingredients){
                            CheckBox temp  = new CheckBox(c);
                            temp.setId(count);
                            temp.setText(s.toString());
                            temp.setTextColor(white);
                            temp.setTextSize(20);
                            CompoundButtonCompat.setButtonTintList(temp ,new ColorStateList(states,colors));
                            checkBoxes[count] = temp;
                            ingredientGroup.addView(temp);
                            count++;
                        }
                        //
                        directionsForCooking = new String[directions.length];
                        StringBuilder sb = new StringBuilder();
                        count = 1;
                        for(String s : directions){
                            sb.append(count);
                            sb.append(":  ");
                            sb.append(s);
                            sb.append("\n");

                            directionsForCooking[count-1] = s;
                            count++;
                        }
                        directionView.setText(sb.toString());

                    }


                }
            }.execute(requestPackage);
        }
        else
        {
            //Toast.makeText(RecipeListActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            Log.d(TAG, "No Internet Connection");
        }



    }


    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
