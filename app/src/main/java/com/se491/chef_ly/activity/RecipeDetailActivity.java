package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.compat.BuildConfig;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.application.CheflyApplication;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.ExtendedIngredient;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.ShoppingListItem;
import com.se491.chef_ly.model.Step;
import com.se491.chef_ly.utils.NetworkHelper;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitle;
    private ImageView imageView;
    private RecipeInformation recipeDetail;
    private ExtendedIngredient[] ingredients;
    private String[] directionsForCooking;
    private ArrayList<ShoppingListItem> shoppingList;
    private static final String TAG = "RecipeDetailActivity";
    private static final String urlString = "https://chefly-prod.herokuapp.com/recipe/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Button getCookingBtn;
        //EditText editTextDesciption;

        recipeTitle = (TextView) findViewById(R.id.recipeName);
        imageView = (ImageView) findViewById(R.id.image);
        //editTextDesciption = (EditText) findViewById(R.id.hidden_edit_view);

        getCookingBtn = (Button) findViewById(R.id.getCookingBtn);
        getCookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cookingIntent = new Intent(RecipeDetailActivity.this, GetCookingActivity.class);
                if (directionsForCooking != null) {
                    cookingIntent.putExtra("directions", directionsForCooking);
                    ArrayList<String> ingredList = new ArrayList<String>();
                    for(ExtendedIngredient ingre : ingredients){
                        ingredList.add(ingre.getOriginalString());
                    }
                    cookingIntent.putExtra("ingredients", ingredList);
                    startActivity(cookingIntent);
                    finish();
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Could not find recipe directions", Toast.LENGTH_SHORT).show();
                }


            }
        });

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        shoppingList = db.getShoppingList();

    }
/*
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
*/
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
                            HttpConnection http = new HttpConnection();
                            resp = http.downloadFromFeed(r);

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

    @Override
    protected void onPostResume() {
        super.onPostResume();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        shoppingList = db.getShoppingList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Remove an item from shopping list if removed by clicking on ingredient
        ArrayList<ShoppingListItem> existing = db.getShoppingList();
        ArrayList<ShoppingListItem> toDelete = new ArrayList<>();
        for(ShoppingListItem item: existing){
            if(!shoppingList.contains(item)){
                toDelete.add(item);
            }
        }
        db.deleteItemFromShoppingList(toDelete);

        // Add new items to shopping list
        for(ShoppingListItem item : shoppingList){
            if(item.getId() == -9999){
                db.addItemToShoppingList(item.getName(),item.isPurchased());
            }
        }

        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }

    private void setRecipeInfo(){
        final Context c = getApplicationContext();
        TextView author = (TextView) findViewById(R.id.recipeAuthor);
       // TextView description = (TextView) findViewById(R.id.recipeDescription);
        TextView serves = (TextView) findViewById(R.id.recipeServings);
        TextView time = (TextView) findViewById(R.id.recipeTime);

        String  recipeName;
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
            if(recipeDetail.getAnalyzedInstructions().length > 0){
                directions = recipeDetail.getAnalyzedInstructions()[0].getSteps();
            }else{
                directions = new Step[]{new Step(recipeDetail.getInstructions())};
            }


//            checkBoxes = new CheckBox[ingredients.length];
//            int states[][] = {{android.R.attr.state_checked}, {}};
            TableLayout table = (TableLayout) findViewById(R.id.ingredientGroup);

            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin=30;
            int topMargin=1;
            int rightMargin=30;
            int bottomMargin=1;

            tableRowParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);

            int color1 = getColor(c, R.color.table_color1);
            int color2 = getColor(c, R.color.table_color2);

            int count = 0;
            for (ExtendedIngredient s : ingredients) {
                final TableRow row = new TableRow(c);
                row.setLayoutParams(tableRowParams);
                row.setBackgroundColor(count%2 == 0 ? color1 : color2);
                row.setPadding(10,5,10,5);
                TextView text = new TextView(c);
                text.setText(s.getOriginalString());
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                text.setTextSize((getResources().getDimension(R.dimen.text_small) / getResources().getDisplayMetrics().density));
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                text.setPadding(10,5,10,5);

                text.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ImageView image = (ImageView) row.getChildAt(1);
                        String text = String.valueOf(((TextView) row.getChildAt(0)).getText());
                        if(row.getChildAt(1).getVisibility() == View.GONE){
                            image.setVisibility(View.VISIBLE);
                            shoppingList.add(new ShoppingListItem(text, false));
                            Toast.makeText(c, text + " added to shopping list", Toast.LENGTH_SHORT).show();
                        }else{
                            image.setVisibility(View.GONE);
                            shoppingList.remove(new ShoppingListItem(text,false));
                            Toast.makeText(c, text + " removed from shopping list", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                row.addView(text);
                ImageView check = new ImageView(c);
                check.setImageResource(R.drawable.shoppinglist);
                check.setLayoutParams(new TableRow.LayoutParams(60,60, 0.1f));
                if(shoppingList.contains(new ShoppingListItem(s.getOriginalString(),false))){
                    check.setVisibility(View.VISIBLE);
                }else{
                    check.setVisibility(View.GONE);
                }


                row.addView(check);

                table.addView(row);
                count++;
            }
            //
            final TableLayout tableDirec = (TableLayout) findViewById(R.id.directionGroup);
            tableDirec.setColumnShrinkable(0,true);
            tableDirec.setVisibility(View.GONE);
            directionsForCooking = new String[directions.length];

            count = 1;
            for (Step s : directions) {
                String step = count + ")  " + s.getStep();

                TableRow row = new TableRow(c);
                row.setLayoutParams(tableRowParams);
                row.setBackgroundColor(count%2 == 0 ? color1 : color2);
                TextView text = new TextView(c);
                text.setText(step);
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                text.setTextSize((getResources().getDimension(R.dimen.text_small) / getResources().getDisplayMetrics().density));
                text.setPadding(15,1,15,1);
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(text);
                tableDirec.addView(row);

                directionsForCooking[count-1] = s.getStep();
                count++;
            }
            final ScrollView sv = (ScrollView) findViewById(R.id.scrollView);

            final ImageButton dropdown = (ImageButton) findViewById(R.id.directionsDropdown);
            final RotateAnimation rotatedown = new RotateAnimation(0, 180,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotatedown.setDuration(250);
            rotatedown.setFillAfter(true);
            final RotateAnimation rotateup = new RotateAnimation(180, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateup.setDuration(250);
            rotateup.setFillAfter(true);

            dropdown.setOnClickListener(new View.OnClickListener() {
                private boolean isClicked = false;
                @Override
                public void onClick(View v) {
                    if(!isClicked){
                        tableDirec.setVisibility(View.VISIBLE);
                        dropdown.startAnimation(rotatedown);
                        isClicked = true;
                        sv.post(new Runnable() {
                            @Override
                            public void run() {
                                //sv.fullScroll(ScrollView.FOCUS_DOWN);
                                sv.smoothScrollBy(0, 500);
                            }
                        });
                    }else{
                        dropdown.startAnimation(rotateup);
                        tableDirec.setVisibility(View.GONE);
                        isClicked = false;
                        //sv.fullScroll(ScrollView.FOCUS_DOWN);

                    }
                }
            });
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

