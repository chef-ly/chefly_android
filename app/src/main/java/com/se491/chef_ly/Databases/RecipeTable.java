package com.se491.chef_ly.Databases;

public class RecipeTable {

    public static final String RECIPE_TABLE_ITEMS = "RecipeItems";
    public static final String COLUMN_RECIPE_ID = "RecipeId";
    public static final String COLUMN_RECIPE_NAME = "RecipeName";
    public static final String COLUMN_RECIPE_AUTHOR = "RecipeAuthor";
    public static final String COLUMN_RECIPE_IMAGE = "RecipeImage";
    public static final String  COLUMN_RATING= "RecipeRating";
    public static final String  COLUMN_TIME= "RecipeTime";
    public static final String  COLUMN_CATEGORIES= "RecipeCategories";
    public static final String  COLUMN_LEVEL= "RecipeLevel";

    public static final String[] ALL_COLUMNS =
            {COLUMN_RECIPE_ID, COLUMN_RECIPE_NAME,COLUMN_RECIPE_AUTHOR, COLUMN_RECIPE_IMAGE,  COLUMN_RATING,
                    COLUMN_TIME,COLUMN_CATEGORIES,COLUMN_LEVEL};

    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_TABLE_ITEMS + "(" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_RECIPE_NAME + " TEXT," +
                    COLUMN_RECIPE_AUTHOR + " TEXT," +
                    COLUMN_RECIPE_IMAGE + " URI," +
                    COLUMN_RATING + " DOUBLE," +
                    COLUMN_TIME + " INTEGER" +
                    COLUMN_CATEGORIES + " TABLE TEXT" +
                    COLUMN_LEVEL+ " TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + RECIPE_TABLE_ITEMS;
}


