package com.se491.chef_ly.Databases;

public class ShoppingList {

    public static final String TABLE_LIST_ITEMS = "ShoppingListItems";
    public static final String COLUMN_LIST_ID = "shoppingId";
    public static final String COLUMN_QUANTITY = "qty";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PURCHASED = "purchased";

    public static final String[] ALL_COLUMNS =
            {COLUMN_LIST_ID, COLUMN_QUANTITY,COLUMN_UNIT , COLUMN_NAME ,
                    COLUMN_PURCHASED };

    public static final String SQL_CREATE =
            "CREATE TABLE " +TABLE_LIST_ITEMS + "(" +
                    COLUMN_LIST_ID + " TEXT PRIMARY KEY," +
                    COLUMN_QUANTITY+ " INTEGER," +
                    COLUMN_UNIT  + " TEXT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PURCHASED + " BOOLEAN" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_LIST_ITEMS;
}

