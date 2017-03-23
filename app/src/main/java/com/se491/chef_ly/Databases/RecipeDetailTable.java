package com.se491.chef_ly.Databases;

class RecipeDetailTable {

    protected static final String RECIPE_DETAIL_TABLE_ITEMS = "RecipeDetailItems";
    protected static final String COLUMN_RECIPE_DETAIL_ID = "RecipeDetailId";
    protected static final String COLUMN_RECIPE_DETAIL_NAME = "RecipeDetailName";
    protected static final String COLUMN_RECIPE_DETAIL_AUTHOR = "RecipeDetailAuthor";
    protected static final String COLUMN_RECIPE_DETAIL_DESCRIPTION = "RecipeDetailDescription";
    protected static final String  COLUMN_RECIPE_DETAIL_SERVES= "RecipeDetailServes";
    protected static final String  COLUMN_RECIPE_DETAIL_TIME= "RecipeDetailTime";
    protected static final String COLUMN_RECIPE_DETAIL_IMAGE = "RecipeImage";
    protected static final String COLUMN_RECIPE_DETAIL_INGREDIENTS = "ListIngredients";
    protected static final String COLUMN_RECIPE_DETAIL_DIRECTIONS = "ListDirections";
    protected static final String  COLUMN_RECIPE_DETAIL_CATEGORIES= "RecipeCategories";
    protected static final String  COLUMN_RECIPE_DETAIL_LEVEL= "RecipeLevel";
    protected static final String  COLUMN_RECIPE_DETAIL_RATING= "RecipeRating";

    public static final String[] ALL_COLUMNS =
            {COLUMN_RECIPE_DETAIL_ID ,COLUMN_RECIPE_DETAIL_NAME,COLUMN_RECIPE_DETAIL_AUTHOR,COLUMN_RECIPE_DETAIL_DESCRIPTION,COLUMN_RECIPE_DETAIL_SERVES,
                    COLUMN_RECIPE_DETAIL_TIME, COLUMN_RECIPE_DETAIL_IMAGE, COLUMN_RECIPE_DETAIL_INGREDIENTS,COLUMN_RECIPE_DETAIL_DIRECTIONS,COLUMN_RECIPE_DETAIL_RATING};

    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_DETAIL_TABLE_ITEMS + "(" +
                    COLUMN_RECIPE_DETAIL_ID  + " TEXT PRIMARY KEY," +
                    COLUMN_RECIPE_DETAIL_NAME + " TEXT," +
                    COLUMN_RECIPE_DETAIL_AUTHOR + " TEXT," +
                    COLUMN_RECIPE_DETAIL_DESCRIPTION + " TEXT," +
                    COLUMN_RECIPE_DETAIL_CATEGORIES + " TEXT," +
                    COLUMN_RECIPE_DETAIL_LEVEL + " TEXT," +
                    COLUMN_RECIPE_DETAIL_SERVES + " INTEGER," +
                    COLUMN_RECIPE_DETAIL_TIME + " INTEGER," +
                    COLUMN_RECIPE_DETAIL_RATING + " REAL," +
                    COLUMN_RECIPE_DETAIL_IMAGE + " TEXT," +
                    COLUMN_RECIPE_DETAIL_INGREDIENTS + " TEXT," +
                    COLUMN_RECIPE_DETAIL_DIRECTIONS + " TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + RECIPE_DETAIL_TABLE_ITEMS;
}
