package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.*;
import com.se491.chef_ly.utils.NetworkHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitle;
    private ImageView imageView;
    private TextView directionView;

    private LinearLayout ingredientGroup;

    private CheckBox[] checkBoxes;
    private RecipeInformation recipeDetail;
    private ExtendedIngredient[] ingredients;
    private String  recipeName;
    private Uri recipeIm;
    private String[] directionsForCooking;
    private String steps;
    private static final String TAG = "RecipeDetailActivity";
    private static final String urlString = "https://chefly-prod.herokuapp.com/recipe/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        final Context c = getApplicationContext();
        Button backBtn;
        Button editBtn;
        Button addToListBtn;
        Button getCookingBtn;
        //EditText editTextDesciption;

        recipeTitle = (TextView) findViewById(R.id.recipeName);
        imageView = (ImageView) findViewById(R.id.image);
        //editTextDesciption = (EditText) findViewById(R.id.hidden_edit_view);
        directionView = (TextView) findViewById(R.id.directionView);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
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
                for (CheckBox cb : checkBoxes) {
                    if (cb.isChecked()) {
                        handler.addItemToShoppingList(ingredients[cb.getId()], false);
                        count++;
                        cb.setChecked(false);
                        Log.d(TAG, "Added to list -> " + String.valueOf(cb.getText()));
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
                if (directionsForCooking != null) {
                    cookingIntent.putExtra("directions", directionsForCooking);
                    startActivity(cookingIntent);
                    finish();
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Could not find recipe directions", Toast.LENGTH_SHORT).show();
                }


            }
        });

        editBtn = (Button) findViewById(R.id.edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewClicked();
            }
        });

    }

    private void textViewClicked() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        View currentView = switcher.getCurrentView();
        //switcher.showNext(); //or switcher.showPrevious();
        TextView viewText = (TextView) switcher.findViewById(R.id.directionView);
        EditText editText = (EditText) findViewById(R.id.hidden_edit_view);

        if (currentView instanceof EditText) {
            //Save steps and prepare next view (Text view)
            directionsForCooking = editText.getText().toString().split("\n");
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String s : directionsForCooking) {
                sb.append(count);
                sb.append(":  ");
                sb.append(s);
                sb.append("\n");

                directionsForCooking[count - 1] = s;
                count++;
            }
            steps = sb.toString();
            viewText.setText(steps);
        }
        else if (currentView instanceof TextView) {
            // Prepare next view (Edit view)
            String tmpSteps = implode("\n", directionsForCooking);
            editText.setText(tmpSteps);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        switcher.showNext();

    }

    private static String implode(String separator, String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1].trim());
        return sb.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final String recipeID = intent.getStringExtra("recipe");
        RecipeInformation result = intent.getParcelableExtra("recipeDetail");
        if (result != null) {
            recipeDetail = result;
            setRecipeInfo();
        } else if (NetworkHelper.hasNetworkAccess(RecipeDetailActivity.this)) //returns true if internet available
        {
            //register to listen the data
            RequestMethod requestPackage = new RequestMethod();

            requestPackage.setEndPoint(urlString + recipeID);
            requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");

            new AsyncTask<RequestMethod, Integer, Long>() {
                String resp = "";

                @Override
                protected Long doInBackground(RequestMethod... params) {
                    for (RequestMethod r : params) {
                        try {
                            resp = HttpConnection.downloadFromFeed(r);

                            GsonBuilder builder = new GsonBuilder();

                            Gson gson = builder.create();
                            Type type;
                            type = new TypeToken<RecipeInformation>() {
                            }.getType();
                            recipeDetail = gson.fromJson(resp, type);
                            Log.d(TAG, recipeDetail.toString());
                        } catch (Exception e) { //IOException e) {
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
                    setRecipeInfo();
                }
            }.execute(requestPackage);
        } else {
            //Toast.makeText(RecipeListActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            Log.d(TAG, "No Internet Connection");
        }


    }
    private void setRecipeInfo(){
        Context c = getApplicationContext();
        TextView author = (TextView) findViewById(R.id.recipeAuthor);
        TextView description = (TextView) findViewById(R.id.recipeDescription);
        TextView serves = (TextView) findViewById(R.id.recipeServings);
        TextView time = (TextView) findViewById(R.id.recipeTime);
        TextView level = (TextView) findViewById(R.id.recipeLevel);

        Step[] directions;
        if (recipeDetail == null) {
            recipeTitle.setText(R.string.recipeNotFound);
        } else {
            recipeName=recipeDetail.getTitle();
            recipeTitle.setText(recipeName);

            author.setText(recipeDetail.getCreditText());
            int servings = recipeDetail.getServings();
            Log.d(TAG, "Serves -> " + servings);
            if(servings <= 0){
                serves.setText(R.string.unknown);
            }else{
                serves.setText(String.valueOf(servings));
            }

            int cookTime = recipeDetail.getReadyInMinutes();
            int hour = 0;
            while(cookTime >= 60){
                hour++;
                cookTime = cookTime - 60;
            }
            String newTime;
            if(hour < 2){
                newTime = (hour != 0)? hour + " hr ": "";
            }else{
                newTime = (hour != 0)? hour + " hrs ": "";
            }
            newTime  += ((cookTime > 0) ? cookTime + " min" : "");
            if(newTime.isEmpty()){
                time.setText(R.string.unknown);
            }else{
                time.setText(newTime);
            }

            new AsyncTask<RequestMethod, Integer, Long>() {
                Bitmap image = null;
                @Override
                protected Long doInBackground(RequestMethod... params) {
                    try {
                        URL url = new URL(recipeDetail.getImage());
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        Log.d(TAG, "IOException on load image");
                        Log.d(TAG, e.getMessage());
                    }
                    return 1L;
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    if (image != null) {
                        imageView.setImageBitmap(image);
                    }
                }
            }.execute();


            ingredients = recipeDetail.getExtendedIngredients();
            directions = recipeDetail.getAnalyzedInstructions()[0].getSteps();

            checkBoxes = new CheckBox[ingredients.length];
            int states[][] = {{android.R.attr.state_checked}, {}};
            int textColor = getColor(c, R.color.color_text);
            int colors[] = {textColor, textColor};
            int count = 0;
            for (ExtendedIngredient s : ingredients) {
                CheckBox temp = new CheckBox(c);
                temp.setId(count);
                temp.setText(s.getOriginalString());
                temp.setTextColor(textColor);
                temp.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_small));
                CompoundButtonCompat.setButtonTintList(temp, new ColorStateList(states, colors));
                checkBoxes[count] = temp;
                ingredientGroup.addView(temp);
                count++;
            }
            //
            directionsForCooking = new String[directions.length];
            StringBuilder sb = new StringBuilder();
            count = 1;
            for (Step s : directions) {
                sb.append(count);
                sb.append(":  ");
                sb.append(s.getStep());
                sb.append("\n");

                directionsForCooking[count - 1] = s.getStep();
                count++;
            }
            steps = sb.toString();
            directionView.setText(steps);

        }
    }

    @SuppressWarnings("deprecation")
    private static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}

