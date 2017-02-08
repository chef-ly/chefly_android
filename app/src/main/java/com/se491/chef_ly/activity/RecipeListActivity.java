package com.se491.chef_ly.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.R;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.RecipeHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private String user;

    private static final String TAG = "RecipeListActivity";
    private static List<Recipe> recipes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ListView listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(new RecipeAdapter(this));
        final Context c = this;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                Intent intent = new Intent(c, RecipeDetailActivity.class);
                intent.putExtra("recipe", recipes.get(position));

                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO - Replace with call to server
        RecipeHolder rh = new RecipeHolder(getResources());
        recipes = rh.getRecipes();

        handleIntent(getIntent());

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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }else{
            Log.d(TAG, "Intent does not equal action search");
        }
    }

    //For navigation drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setIconified(true);

                menu.findItem(searchView.getId()).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_create_recipe:
                Toast.makeText(this, "Create Recipe", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_import_recipe:
                Toast.makeText(this, "Import Recipe", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_log_out:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
