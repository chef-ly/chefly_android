package com.se491.chef_ly.Databases;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.DatabaseUtils;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import com.se491.chef_ly.model.Recipe;


public class DataBase {

    private Context mContext;
    private SQLiteDatabase db;
    SQLiteOpenHelper bdHandler;

    public DataBase(Context context) {
        this.mContext = context;
        bdHandler = new Handler(mContext);
        db = bdHandler.getWritableDatabase();
    }

    public void open() {
        db = bdHandler.getWritableDatabase();
    }

    public void close() {
        bdHandler.close();
    }

    public Recipe createRecipeItem(Recipe item) {
        //ContentValues values = item.getRecipes();
       // db.insert(RecipeTable.RECIPE_TABLE_ITEMS, null, values);
        return item;
    }

    public long getRecipeItemsCount() {
        return DatabaseUtils.queryNumEntries(db, RecipeTable.RECIPE_TABLE_ITEMS);
    }


}
