package com.se491.chef_ly.activity;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.RecipeHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends ListActivity {
    private String user;
    private EditText search;

    private static final String TAG = "RecipeListActivity";
    private static List<Recipe> recipes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setListAdapter(new RecipeAdapter(this));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //TODO - Replace with call to server
        RecipeHolder rh = new RecipeHolder(getResources());
        recipes = rh.getRecipes();

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //TODO update listview results
            }
        });



    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipes.get(position));

        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        user = extras.getString("name");
    }



    static class RecipeAdapter extends BaseAdapter{

        private LayoutInflater inflater;
        static class ViewHolder{
            ImageView icon;
            TextView name;
        }

        RecipeAdapter(Context context){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public Object getItem(int position) {
            return recipes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if(convertView == null){
                row = inflater.inflate(R.layout.recipe_list_item, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) row.findViewById(R.id.image);
                holder.name = (TextView) row.findViewById(R.id.recipeName);


                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            Recipe r = recipes.get(position);
            holder.name.setText(r.getName());
            try{
                holder.icon.setImageBitmap(MediaStore.Images.Media.getBitmap(parent.getContext().getContentResolver(), r.getImage()));

            }catch (IOException e){
                Log.d(TAG, "IOException on load image");
                Log.d(TAG, e.getMessage());

            }
            return row;
        }
    }
}
