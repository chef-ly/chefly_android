package com.se491.chef_ly.Databases;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.ShoppingListItem;

import java.util.ArrayList;
//http://stackoverflow.com/questions/26375184/inserting-and-deleting-values-in-with-my-dbhelper-class
public class DatabaseHandler extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHandler";
    private static final String DB_FILE_NAME = "chefly.db";
    private static final int DB_VERSION = 11;

    public DatabaseHandler(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL(RecipeTable.SQL_CREATE);
            db.execSQL(RecipeDetailTable.SQL_CREATE);
            db.execSQL( ShoppingList.SQL_CREATE);
            db.execSQL(UserTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(RecipeTable.SQL_DELETE);
        db.execSQL(RecipeDetailTable.SQL_DELETE);
        db.execSQL(ShoppingList.SQL_DELETE);
        db.execSQL(UserTable.SQL_DELETE);
        onCreate(db);

    }

    public void createDetailedRecipe(RecipeInformation recipe){
        SQLiteDatabase db = null;
        ContentValues values;

        try {
            db = this.getWritableDatabase();
            values = new ContentValues();
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_ID, recipe.getId());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_NAME, recipe.getTitle());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_AUTHOR, recipe.getCreditText());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_SERVES, recipe.getServings());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_TIME, recipe.getReadyInMinutes());
            Gson gson = new Gson();
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_IMAGE, recipe.getImage().toString());
            String json = gson.toJson(recipe.getInstructions());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_DIRECTIONS, json);
            json = gson.toJson(recipe.getExtendedIngredients());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_INGREDIENTS, json);

            db.insert(RecipeDetailTable.RECIPE_DETAIL_TABLE_ITEMS, "", values);
        }finally {
            if (db != null)
                db.close();
        }



    }
    public ArrayList<RecipeInformation> getRecipes(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<RecipeInformation> list = new ArrayList<>();
        String[] columns = {RecipeDetailTable.COLUMN_RECIPE_DETAIL_ID, RecipeDetailTable.COLUMN_RECIPE_DETAIL_NAME,RecipeDetailTable.COLUMN_RECIPE_DETAIL_AUTHOR,
                RecipeDetailTable.COLUMN_RECIPE_DETAIL_CATEGORIES,RecipeDetailTable.COLUMN_RECIPE_DETAIL_LEVEL, RecipeDetailTable.COLUMN_RECIPE_DETAIL_TIME,
                RecipeDetailTable.COLUMN_RECIPE_DETAIL_RATING,RecipeDetailTable.COLUMN_RECIPE_DETAIL_IMAGE };
        try {
            db = this.getWritableDatabase();
            cursor = db.query(RecipeDetailTable.RECIPE_DETAIL_TABLE_ITEMS, columns , null, null, null, null, RecipeDetailTable.COLUMN_RECIPE_DETAIL_NAME);
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_ID));
                    String name = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_NAME));
                    String author = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_AUTHOR));
                    String cats = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_CATEGORIES));
                    String level = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_LEVEL));
                    int time = cursor.getInt(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_TIME));
                    int rating = cursor.getInt(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_RATING));
                    String image = cursor.getString(cursor.getColumnIndex(RecipeDetailTable.COLUMN_RECIPE_DETAIL_IMAGE));

                    Gson gson = new Gson();
                    String[] categories = gson.fromJson(cats, String[].class);
                    //list.add(new RecipeInformation(id,name,author,image, rating, time, categories, level));

                } while (cursor.moveToNext());

            }
        }finally {
            if(cursor != null)
                cursor.close();

            if(db != null)
                db.close();
        }


        return list;
    }

    public long getRecipeItemsCount() {
        SQLiteDatabase db = null;
        try{
            db = this.getReadableDatabase();
            return DatabaseUtils.queryNumEntries(db, RecipeTable.RECIPE_TABLE_ITEMS);
        }finally {
            if(db != null)
                db.close();
        }

    }

    public ArrayList<ShoppingListItem> getShoppingList(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ShoppingListItem> listItems = new ArrayList<>();

        try{
            db = this.getReadableDatabase();

            cursor = db.query(ShoppingList.TABLE_LIST_ITEMS, ShoppingList.ALL_COLUMNS , null, null, null, null, ShoppingList.COLUMN_NAME);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ShoppingList.COLUMN_LIST_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ShoppingList.COLUMN_NAME));
                    boolean purchased = cursor.getInt(cursor.getColumnIndex(ShoppingList.COLUMN_PURCHASED)) > 0;

                    listItems.add(new ShoppingListItem(id, name,purchased));
                } while (cursor.moveToNext());
                return listItems;
            }

        }finally {
            if(cursor != null){
                cursor.close();
            }
            if(db != null){
                db.close();
            }

        }


        return listItems;
    }


    public void addItemToShoppingList(String name, boolean purchased){
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ShoppingList.COLUMN_NAME, name);
            values.put(ShoppingList.COLUMN_PURCHASED, purchased);

            db.insert(ShoppingList.TABLE_LIST_ITEMS, null, values);
        }finally {
            if(db != null)
                db.close();
        }



    }
    public void updateShoppingListItem(ShoppingListItem i){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ShoppingList.COLUMN_NAME, i.getName());
            values.put(ShoppingList.COLUMN_PURCHASED, i.isPurchased());
            db.update(ShoppingList.TABLE_LIST_ITEMS, values, ShoppingList.COLUMN_LIST_ID + " = " + i.getId(), null);
        }finally {
            if(db != null)
                db.close();
        }

    }

    public void recipeUpdate(RecipeInformation recipe){
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_ID, recipe.getId());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_NAME, recipe.getTitle());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_AUTHOR, recipe.getCreditText());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_SERVES, recipe.getServings());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_TIME, recipe.getReadyInMinutes());
            Gson gson = new Gson();
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_IMAGE, recipe.getImage());
            String json = gson.toJson(recipe.getInstructions());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_DIRECTIONS, json);
            json = gson.toJson(recipe.getExtendedIngredients());
            values.put(RecipeDetailTable.COLUMN_RECIPE_DETAIL_INGREDIENTS, json);

            db.update(RecipeDetailTable.RECIPE_DETAIL_TABLE_ITEMS, values,RecipeDetailTable.COLUMN_RECIPE_DETAIL_ID + " = " + recipe.getId(), null);

        }finally {
            if(db != null)
                db.close();
        }

    }
    public void deleteItemFromShoppingList(ArrayList<ShoppingListItem> items){
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            for(ShoppingListItem item : items){
                long result = db.delete(ShoppingList.TABLE_LIST_ITEMS, ShoppingList.COLUMN_LIST_ID + " = " + item.getId(), null);
                Log.d(TAG, "id -> " +item.getId() + " result ->" + result);
            }

        }finally {
            if(db != null)
                db.close();
        }

    }

}