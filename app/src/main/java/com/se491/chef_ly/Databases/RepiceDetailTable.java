package com.se491.chef_ly.Databases;

public class RepiceDetailTable {

    public static final String RECIPE_DETAIL_TABLE_ITEMS = "RecipeDetailItems";
    public static final String COLUMN_RECIPE_DETAIL_ID = "RecipeDetailId";
    public static final String COLUMN_RECIPE_DETAIL_NAME = "RecipeDetailName";
    public static final String COLUMN_RECIPE_DETAIL_AUTHOR = "RecipeDetailAuthor";
    public static final String COLUMN_RECIPE_DETAIL_DESCRIPTION = "RecipeDetailDescription";
    public static final String  COLUMN__RECIPE_DETAIL_SERVES= "RecipeDetailServes";
    public static final String  COLUMN__RECIPE_DETAIL_TIME= "RecipeDetailTime";
    public static final String COLUMN_RECIPE_DETAIL_IMAGE = "RecipeImage";
    public static final String COLUMN_RECIPE_DETAIL_INGREDIENTS = "ListIngredients";
    public static final String COLUMN_RECIPE_DETAIL_DIRECTIONS = "ListDirections";

    public static final String  COLUMN_TIME= "RecipeTime";
    public static final String  COLUMN_CATEGORIES= "RecipeCategories";
    public static final String  COLUMN_LEVEL= "RecipeLevel";

    public static final String[] ALL_COLUMNS =
            {COLUMN_RECIPE_DETAIL_ID ,COLUMN_RECIPE_DETAIL_NAME,COLUMN_RECIPE_DETAIL_AUTHOR,COLUMN_RECIPE_DETAIL_DESCRIPTION,COLUMN__RECIPE_DETAIL_SERVES,
                    COLUMN__RECIPE_DETAIL_TIME, COLUMN_RECIPE_DETAIL_IMAGE, COLUMN_RECIPE_DETAIL_INGREDIENTS,COLUMN_RECIPE_DETAIL_DIRECTIONS};

    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_DETAIL_TABLE_ITEMS + "(" +
                    COLUMN_RECIPE_DETAIL_ID  + " INTEGER PRIMARY KEY," +
                    COLUMN_RECIPE_DETAIL_NAME + " TEXT," +
                    COLUMN_RECIPE_DETAIL_AUTHOR + " TEXT," +
                    COLUMN_RECIPE_DETAIL_DESCRIPTION + " TEXT," +
                    COLUMN__RECIPE_DETAIL_SERVES + " INTEGER," +
                    COLUMN__RECIPE_DETAIL_TIME + " INTEGER," +
                    COLUMN_RECIPE_DETAIL_IMAGE + " URI," +
                    COLUMN_RECIPE_DETAIL_INGREDIENTS + " LIST" +
                    COLUMN_RECIPE_DETAIL_DIRECTIONS + " LIST" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + RECIPE_DETAIL_TABLE_ITEMS;
}
