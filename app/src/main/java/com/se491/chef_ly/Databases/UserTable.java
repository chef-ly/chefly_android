package com.se491.chef_ly.Databases;

/**
 * Created by Don on 4/24/2017.
 */

public class UserTable {
    protected static final String TABLE_LIST_ITEMS = "users";
    protected static final String COLUMN_LIST_ID = "userId";
    protected static final String COLUMN_NAME = "username";
    protected static final String COLUMN_IMAGE = "userImage";
    protected static final String COLUMN_EMAIL = "userEmail";

    public static final String[] ALL_COLUMNS =
            {COLUMN_LIST_ID, COLUMN_NAME ,COLUMN_IMAGE, COLUMN_EMAIL };

    public static final String SQL_CREATE =
            "CREATE TABLE " +TABLE_LIST_ITEMS + "(" +
                    COLUMN_LIST_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_IMAGE + " TEXT," +
                    COLUMN_EMAIL + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_LIST_ITEMS;
}
