package com.se491.chef_ly.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;

import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.nav_activities.ShoppingListActivity;
import com.se491.chef_ly.http.MyService;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.Recipe;
import com.se491.chef_ly.model.RecipeDetail;
import com.se491.chef_ly.utils.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ListView listview;
    private String user;

    private static final String TAG = "RecipeListActivity";
    private final int CREATE_RECIPE_CODE = 7212;
    private static final List<Recipe> recipes = new ArrayList<>();

    private static final String urlString ="https://chefly-prod.herokuapp.com/list";


    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<Recipe> fromServer;
            // Get recipes from server
            fromServer = intent.getParcelableArrayListExtra(MyService.MY_SERVICE_PAYLOAD);
            // Only add the recipes we do not have in our list already
            for(Recipe r : fromServer){
                if(!recipes.contains(r)){
                    recipes.add(r);
                }
            }
            // Notify the list view adapter that the list has changed
            ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listview = (ListView) findViewById(R.id.list);

        final Context c = this;
        listview.setAdapter(new RecipeAdapter(listview.getContext()));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                Intent intent = new Intent(c, RecipeDetailActivity.class);
                String recipeID = ((Recipe) l.getAdapter().getItem(position)).getId();
                intent.putExtra("recipe", recipeID);

                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(NetworkHelper.hasNetworkAccess(RecipeListActivity.this)) //returns true if internet available
        {
            //Toast.makeText(RecipeListActivity.this,"Internet Connection",Toast.LENGTH_LONG).show();
                      //register to listen the data
            RequestMethod requestPackage = new RequestMethod();

            requestPackage.setEndPoint(urlString);
            //requestPackage.setParam("name", "Pepperoni Pizza");//filter data if i want
            requestPackage.setMethod("GET"); //  or requestPackage.setMethod("POST");
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            intent.putExtra("Tag", TAG);
            startService(intent);
        }
        else
        {
            //Toast.makeText(RecipeListActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            Log.d(TAG, "No Internet Connection");
        }
        //listen to the message
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        handleIntent(getIntent());

    }
    @Override
    protected void onDestroy() { //unregister to listen the data
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        user = extras.getString("name");

        // Get recipes from local db
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        recipes.addAll(db.getRecipes());
        // Notify list view adapter that the list has changed
        ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_RECIPE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Result from CreateRecipe");
                RecipeDetail temp = data.getParcelableExtra("recipe");
                Recipe r = new Recipe(temp.getId(), temp.getName(), temp.getAuthor(),temp.getImage().toString(), 0.0, temp.getTime(), temp.getCategories(), temp.getLevel().toString());
                recipes.add(r);
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
            }
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
                Toast.makeText(this, "Create RecipeDetail", Toast.LENGTH_SHORT).show();
                Intent createRecipeIntent = new Intent(getApplicationContext(), CreateRecipeActivity.class);
                createRecipeIntent.putExtra("user",user);
                startActivityForResult(createRecipeIntent, CREATE_RECIPE_CODE);

                break;
            case R.id.nav_import_recipe:
                Toast.makeText(this, "Import RecipeDetail", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_shopping_list:
                Intent intent = new Intent(this.getApplicationContext(), ShoppingListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_interpreter:
                Intent interpreter_intent = new Intent(this.getApplicationContext(), TestInterpreterActivity.class);
                startActivity(interpreter_intent);
                break;
            case R.id.nav_log_out:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    static private class RecipeAdapter extends BaseAdapter{
        private final Context context;
        private final LayoutInflater inflater;
        static class ViewHolder{
            ImageView icon;
            TextView name;
            TextView author;
            TextView time;
            TextView level;
            TextView rating;
        }

        RecipeAdapter(Context context){
            this.context = context;
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
            final ViewHolder holder;
            if(convertView == null){
                row = inflater.inflate(R.layout.recipe_list_item, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) row.findViewById(R.id.image);
                holder.name = (TextView) row.findViewById(R.id.recipeName);
                holder.author = (TextView) row.findViewById(R.id.recipeAuthor);
                holder.time = (TextView) row.findViewById(R.id.recipeTime);
                holder.level = (TextView) row.findViewById(R.id.recipeLevel);
                holder.rating = (TextView) row.findViewById(R.id.recipeRating);


                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            Recipe r = recipes.get(position);
            holder.name.setText(r.getName());
            holder.author.setText(r.getAuthor());
            int time = r.getTime();
            int hour = 0;
            while(time >= 60){
                hour++;
                time = time - 60;
            }
            String newTime = (hour != 0)? hour + " hrs ": ""  + ((time != 0) ? time + " min" : "") ;
            holder.time.setText(String.valueOf(newTime));
            holder.level.setText(String.valueOf(r.getLevel()));
            holder.rating.setText(String.valueOf(r.getRating()));


            try{
                Uri uri=r.getImage(); //take the url
                String image = uri.toString(); //make the url into a string
                if(!image.isEmpty()){
                    image = image.substring(1, image.length()-1);  //remove the quotes from uri string
                    final String tag = "ListView";
                    Glide.with(context)
                            .load(image).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    Log.d(tag, e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d(tag, "Image resource ready");
                                    return false;
                                }
                            })
                            .error(R.drawable.noimageavailable)
                            .into(holder.icon);
                }else{
                    holder.icon.setImageResource(R.drawable.noimageavailable);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }
    }

}