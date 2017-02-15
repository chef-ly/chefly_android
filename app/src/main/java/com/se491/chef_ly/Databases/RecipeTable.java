package com.se491.chef_ly.Databases;

class RecipeTable {

    protected static final String RECIPE_TABLE_ITEMS = "RecipeItems";
    private static final String COLUMN_RECIPE_ID = "RecipeId";
    private static final String COLUMN_RECIPE_NAME = "RecipeName";
    private static final String COLUMN_RECIPE_AUTHOR = "RecipeAuthor";
    private static final String COLUMN_RECIPE_IMAGE = "RecipeImage";
    private static final String  COLUMN_RATING= "RecipeRating";
    private static final String  COLUMN_TIME= "RecipeTime";
    private static final String  COLUMN_CATEGORIES= "RecipeCategories";
    private static final String  COLUMN_LEVEL= "RecipeLevel";

    public static final String[] ALL_COLUMNS =
            {COLUMN_RECIPE_ID, COLUMN_RECIPE_NAME,COLUMN_RECIPE_AUTHOR, COLUMN_RECIPE_IMAGE,  COLUMN_RATING,
                    COLUMN_TIME,COLUMN_CATEGORIES,COLUMN_LEVEL};

    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_TABLE_ITEMS + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_RECIPE_NAME + " TEXT, " +
                    COLUMN_RECIPE_AUTHOR + " TEXT, " +
                    COLUMN_RECIPE_IMAGE + " TEXT, " +
                    COLUMN_RATING + " DOUBLE, " +
                    COLUMN_TIME + " INTEGER, " +
                    COLUMN_CATEGORIES + " LIST, " +
                    COLUMN_LEVEL+ " TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + RECIPE_TABLE_ITEMS;
}


