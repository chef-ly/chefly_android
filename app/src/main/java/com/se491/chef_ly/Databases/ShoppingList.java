package com.se491.chef_ly.Databases;

class ShoppingList {

    protected static final String TABLE_LIST_ITEMS = "ShoppingListItems";
    protected static final String COLUMN_LIST_ID = "shoppingId";
    protected static final String COLUMN_NAME = "name";
    protected static final String COLUMN_PURCHASED = "purchased";

    public static final String[] ALL_COLUMNS =
            {COLUMN_LIST_ID, COLUMN_NAME ,COLUMN_PURCHASED };

    public static final String SQL_CREATE =
            "CREATE TABLE " +TABLE_LIST_ITEMS + "(" +
                    COLUMN_LIST_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PURCHASED + " BOOLEAN" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_LIST_ITEMS;
}

