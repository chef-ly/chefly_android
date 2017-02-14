package com.se491.chef_ly.Databases;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//http://stackoverflow.com/questions/26375184/inserting-and-deleting-values-in-with-my-dbhelper-class
public class Handler extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "chefly.db";
    public static final int DB_VERSION = 1;

    public Handler(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeTable.SQL_CREATE);
        db.execSQL(RepiceDetailTable.SQL_CREATE);
        db.execSQL( ShoppingList.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RecipeTable.SQL_DELETE);
        db.execSQL(RepiceDetailTable.SQL_DELETE);
        db.execSQL(ShoppingList.SQL_DELETE);
        onCreate(db);
    }
}