package com.se491.chef_ly.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.compat.BuildConfig;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.nav_activities.ContactUsActivity;
import com.se491.chef_ly.activity.nav_activities.SearchIngredients;
import com.se491.chef_ly.activity.nav_activities.ShoppingListActivity;
import com.se491.chef_ly.activity.nav_activities.UserProfileActivity;
import com.se491.chef_ly.application.CheflyApplication;
import com.se491.chef_ly.http.HttpConnection;
import com.se491.chef_ly.http.RequestMethod;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.CredentialsManager;
import com.se491.chef_ly.utils.GetRecipesFromServer;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ListViewFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<RecipeList> {

    private static final String TAG = "RecipeListActivity";
    private final int CREATE_RECIPE_CODE = 7212;

    private RecipeList favoriteRecipes;
    private RecipeList serverRecipes;

    private ListViewFragment favs;
    private ListViewFragment server;
    private ViewPager pager;
    private TextView favoritesHeader;
    private TextView recipesHeader;
    private String queryString = "";
    private ArraySet<Integer> favListAdd = new ArraySet<>();
    private ArraySet<Integer> favListRemove = new ArraySet<>();
    private SearchView searchView;
    private ProgressBar spinner;

    private final String urlStringSearch ="http://www.chef-ly.com/search?q=";
    private final String urlPageNum = "&p=";

    private final int FAVORTIESID = 601;
    private static final String urlFavsString ="http://www.chef-ly.com/user/favorites";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Progress bar for loading search
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        spinner.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.customprogressbar));

        //Initialize recipe lists
        serverRecipes = new RecipeList();
        favoriteRecipes = new RecipeList();

        //Start AsyncTaskLoader to get FavoriteRecipes
        Credentials cred = CredentialsManager.getCredentials(getApplicationContext());
        String t = cred.getAccessToken();
        Log.d(TAG, "Token -> " + t);
        if(t != null){
            RequestMethod requestPackageFavs = new RequestMethod();
            requestPackageFavs.setEndPoint(urlFavsString);
            requestPackageFavs.setMethod("GET");
            requestPackageFavs.setHeader("Authorization", "Bearer " + t);
            Bundle bundlefavs = new Bundle();
            bundlefavs.putParcelable("requestPackage", requestPackageFavs);

            getSupportLoaderManager().initLoader(FAVORTIESID, bundlefavs,this).forceLoad();
        }else{
            Toast.makeText(this, "Could not retrieve favorites, token is null", Toast.LENGTH_SHORT).show();
        }


        // PageViewer
        pager = (ViewPager) findViewById(R.id.viewpager);
        Bundle serv = new Bundle();
        serv.putString("title", "Recipes");
        serv.putString("pageNum", "1");
        serv.putString("search", "");

        server = new ListViewFragment();
        server.setArguments(serv);

        Bundle f = new Bundle();
        f.putString("title", "Favorites");
        f.putString("pageNum", "2");
        f.putString("search", "");

        favs = new ListViewFragment();
        favs.setArguments(f);


        ListViewFragment[] frags = {server, favs};
        pager.setAdapter(new RecipeListPagerAdapter(getSupportFragmentManager(), frags));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d(TAG, "Position -> " + position);
               if (position == 1) {
                    favoritesHeader.setPaintFlags(favoritesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                   // ingredientsHeader.setPaintFlags(0);
                } else {
                    recipesHeader.setPaintFlags(recipesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    favoritesHeader.setPaintFlags(0);
                   // ingredientsHeader.setPaintFlags(0);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Header links
       // ingredientsHeader = (TextView) findViewById(R.id.ingredientsHeader);
        favoritesHeader = (TextView) findViewById(R.id.favortiesHeader);
        recipesHeader = (TextView) findViewById(R.id.recipesHeader);

        View.OnClickListener headerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (v.getId() == favoritesHeader.getId()) {
                    favoritesHeader.setPaintFlags(favoritesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                  //  ingredientsHeader.setPaintFlags(0);
                    pager.setCurrentItem(1);
                } else {
                    recipesHeader.setPaintFlags(recipesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    favoritesHeader.setPaintFlags(0);
                  //  ingredientsHeader.setPaintFlags(0);
                    pager.setCurrentItem(0);
                }
            }
        };
       // ingredientsHeader.setOnClickListener(headerListener);
        favoritesHeader.setOnClickListener(headerListener);
        recipesHeader.setOnClickListener(headerListener);

        //Tool/Appbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String user = extras.getString("name");
        RecipeList list = extras.getParcelable("recipeList");
        boolean isSearch = extras.getBoolean("isSearch");
        if(isSearch){
            String search = extras.getString("search");
            queryString = search;

            server.updateSearch(search);
            server.updateListAdapter(list);
        }else if(list != null){
            serverRecipes = list;
            server.updateListAdapter(serverRecipes);
        }else{
            Log.d(TAG, "Error - No recipes loaded from server");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            //TODO handle case where no recipes are retrieved from server
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  Update favorites list for user on server
        if(favListAdd.size() > 0 || favListRemove.size() > 0){
            new AsyncTask<String, Integer, Integer>(){
                @Override
                protected Integer doInBackground(String... params) {
                    GsonBuilder builder = new GsonBuilder();

                    Gson gson = builder.create();
                    Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
                    String bodyAdd = gson.toJson(favListAdd, type);
                    String bodyRemove = gson.toJson(favListRemove, type);
                    String token = CredentialsManager.getCredentials(getApplicationContext()).getAccessToken();
                    if(token == null){
                        token = "test";
                    }
                    Log.d(TAG, bodyAdd + " " + bodyRemove);
                    RequestMethod rm = new RequestMethod();
                    rm.setEndPoint(urlFavsString);
                    rm.setMethod("POST");
                    rm.setHeader("Authorization", "Bearer " + token);
                    rm.setParam("add", bodyAdd);
                    rm.setParam("remove", bodyRemove);
                    try{
                        HttpConnection http = new HttpConnection();
                        http.downloadFromFeed(rm);
                    }catch (IOException e){
                        return 0;
                    }
                    return 200;
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                }
            }.execute();
        }
        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_RECIPE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Result from CreateRecipe");
                RecipeInformation r = data.getParcelableExtra("recipe");

                favoriteRecipes.add(r);
                favs.updateListAdapter(favoriteRecipes);
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
//search by title
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryString = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, queryString, Toast.LENGTH_SHORT).show();
            // Put Search Logic Here
            RequestMethod requestPackage = new RequestMethod();
            requestPackage.setEndPoint(urlStringSearch + queryString + urlPageNum + 0);//find the url
            requestPackage.setMethod("GET");//send the post method request
            Bundle searchRecipes = new Bundle();
            searchRecipes.putParcelable("requestPackage", requestPackage);
            searchRecipes.putString("test", "Handle Intent");

            server.updateSearch(queryString);
            //getSupportLoaderManager().initLoader(SEARCHID, searchRecipes,this).forceLoad();
            getSupportLoaderManager().initLoader(queryString.hashCode(), searchRecipes,this).forceLoad();

        } else {
            Log.d(TAG, "Intent does not equal action search");
        }
    }

    // PageViewer
    @Override
    public void onFragmentInteraction(RecipeInformation r, boolean add) {
        //  For passing recipes from fragments to parent Acitivity
        int id = r.getId();
        if(add){
            favoriteRecipes.add(r);
            favs.addRecipe(r);

            if(favListRemove.contains(id)){
                favListRemove.remove(id);
            }else{
                favListAdd.add(id);
            }
            Log.d(TAG, "Add");

        }else{
            favoriteRecipes.remove(r);
            favs.removeRecipe(r);
            server.removeFavorite(r);

            if(favListAdd.contains(id)){
                favListAdd.remove(id);
            }else{
                favListRemove.add(id);
            }
            Log.d(TAG, "Remove");

        }

        Log.d(TAG, "Fav Recipe size = " + favoriteRecipes.size());
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
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG , "SearchView -> OnClose --> " + queryString );
                menu.findItem(searchView.getId()).collapseActionView();
                queryString = "";
                server.updateSearch("");
                server.setList(serverRecipes, true);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                queryString = query;
                server.updateSearch(query);
                spinner.setVisibility(View.VISIBLE);
                spinner.bringToFront();

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
        switch (id) {
            case R.id.nav_profile:
                Intent i = new Intent(this, UserProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_shopping_list:
                Intent intent = new Intent(this.getApplicationContext(), ShoppingListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_search_ingredients:
                Intent in = new Intent(this, SearchIngredients.class);
                startActivity(in);
                break;
            case R.id.contact_us:
                Intent incontact = new Intent(this, ContactUsActivity.class);
                startActivity(incontact);
                break;
            case R.id.nav_log_out:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                CredentialsManager.deleteCredentials(this);//call the method from the util class
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            /*
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
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
             */

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //  LoaderManager callback method
    @Override
    public Loader<RecipeList> onCreateLoader(int id, Bundle args) {
        RequestMethod rm = args.getParcelable("requestPackage");
        return  new GetRecipesFromServer(getApplicationContext(), rm);
    }
    //  LoaderManager callback method
    @Override
    public void onLoadFinished(Loader<RecipeList> loader, RecipeList data) {
        int id = loader.getId();
            if(id == FAVORTIESID){
                favoriteRecipes = data;
                for(RecipeInformation r : favoriteRecipes){
                    //favorites.add(r.getId());
                    r.setFavorite(true);
                }
                favs.updateListAdapter(favoriteRecipes);


            }else if(id == queryString.hashCode()){
                spinner.setVisibility(View.GONE);
                Log.d(TAG, " Recipe Search -> " + data.size());
                if(data.size() > 0){
                    // Collect all recipes so list can be redisplayed when search is closed

                    server.setList(data, true);
                }else{
                    Log.d(TAG, "Error - No recipes loaded from server");
                    Toast.makeText(this, "Sorry we couldn't find any recipes", Toast.LENGTH_SHORT).show();
                    //TODO handle case where no recipes are retrieved from server
                }
            }
        Log.d(TAG, "OnLoadFinished " + loader.getId());
    }
    //  LoaderManager callback method
    @Override
    public void onLoaderReset(Loader<RecipeList> loader) {

        if(getTaskId() == FAVORTIESID ){
            favoriteRecipes = new RecipeList();
        }else if(getTaskId() == queryString.hashCode()){
            loader.reset();
        }
    }

    private class RecipeListPagerAdapter extends FragmentPagerAdapter {
        private ListViewFragment[] pages;

        RecipeListPagerAdapter(FragmentManager m, ListViewFragment[] p) {
            super(m);
            pages = p;
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return pages[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position].getTitle();
        }
    }

}