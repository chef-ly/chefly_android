package com.se491.chef_ly.Databases;

class RecipeDetailTable {

    private static final String RECIPE_DETAIL_TABLE_ITEMS = "RecipeDetailItems";
    private static final String COLUMN_RECIPE_DETAIL_ID = "RecipeDetailId";
    private static final String COLUMN_RECIPE_DETAIL_NAME = "RecipeDetailName";
    private static final String COLUMN_RECIPE_DETAIL_AUTHOR = "RecipeDetailAuthor";
    private static final String COLUMN_RECIPE_DETAIL_DESCRIPTION = "RecipeDetailDescription";
    private static final String  COLUMN_RECIPE_DETAIL_SERVES= "RecipeDetailServes";
    private static final String  COLUMN_RECIPE_DETAIL_TIME= "RecipeDetailTime";
    private static final String COLUMN_RECIPE_DETAIL_IMAGE = "RecipeImage";
    private static final String COLUMN_RECIPE_DETAIL_INGREDIENTS = "ListIngredients";
    private static final String COLUMN_RECIPE_DETAIL_DIRECTIONS = "ListDirections";

    public static final String  COLUMN_TIME= "RecipeTime";
    public static final String  COLUMN_CATEGORIES= "RecipeCategories";
    public static final String  COLUMN_LEVEL= "RecipeLevel";

    public static final String[] ALL_COLUMNS =
            {COLUMN_RECIPE_DETAIL_ID ,COLUMN_RECIPE_DETAIL_NAME,COLUMN_RECIPE_DETAIL_AUTHOR,COLUMN_RECIPE_DETAIL_DESCRIPTION,COLUMN_RECIPE_DETAIL_SERVES,
                    COLUMN_RECIPE_DETAIL_TIME, COLUMN_RECIPE_DETAIL_IMAGE, COLUMN_RECIPE_DETAIL_INGREDIENTS,COLUMN_RECIPE_DETAIL_DIRECTIONS};

    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_DETAIL_TABLE_ITEMS + "(" +
                    COLUMN_RECIPE_DETAIL_ID  + " INTEGER PRIMARY KEY," +
                    COLUMN_RECIPE_DETAIL_NAME + " TEXT," +
                    COLUMN_RECIPE_DETAIL_AUTHOR + " TEXT," +
                    COLUMN_RECIPE_DETAIL_DESCRIPTION + " TEXT," +
                    COLUMN_RECIPE_DETAIL_SERVES + " INTEGER," +
                    COLUMN_RECIPE_DETAIL_TIME + " INTEGER," +
                    COLUMN_RECIPE_DETAIL_IMAGE + " TEXT," +
                    COLUMN_RECIPE_DETAIL_INGREDIENTS + " LIST," +
                    COLUMN_RECIPE_DETAIL_DIRECTIONS + " LIST" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + RECIPE_DETAIL_TABLE_ITEMS;
}
