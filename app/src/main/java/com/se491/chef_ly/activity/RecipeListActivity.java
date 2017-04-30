package com.se491.chef_ly.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.TextView;
import android.widget.Toast;

import com.se491.chef_ly.Databases.DatabaseHandler;
import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.nav_activities.ShoppingListActivity;
import com.se491.chef_ly.activity.nav_activities.UserProfileActivity;
import com.se491.chef_ly.model.RecipeInformation;
import com.se491.chef_ly.model.RecipeList;
import com.se491.chef_ly.utils.CredentialsManager;

public class RecipeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                        ListViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "RecipeListActivity";
    private final int CREATE_RECIPE_CODE = 7212;
    private static RecipeList favoriteRecipes;
    private static RecipeList serverRecipes;
    private ListViewFragment favs;
    private ListViewFragment server;
    private ViewPager pager;
    private TextView favoritesHeader;
    private TextView recipesHeader;
    private TextView ingredientsHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialize recipe lists
        serverRecipes = new RecipeList();
        favoriteRecipes = new RecipeList();

        // PageViewer
        pager = (ViewPager) findViewById(R.id.viewpager);
        favs = ListViewFragment.newInstance("Favorites", "1");

        server = ListViewFragment.newInstance("Recipes", "2");

        ListViewFragment[] frags = {server, favs};
        pager.setAdapter(new RecipeListPagerAdapter(getSupportFragmentManager(), frags));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2) {
                    ingredientsHeader.setPaintFlags(ingredientsHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                    favoritesHeader.setPaintFlags(0);
                }
                else if (position == 1) {
                    favoritesHeader.setPaintFlags(favoritesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                    ingredientsHeader.setPaintFlags(0);
                } else {
                    recipesHeader.setPaintFlags(recipesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    favoritesHeader.setPaintFlags(0);
                    ingredientsHeader.setPaintFlags(0);
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
        ingredientsHeader = (TextView) findViewById(R.id.ingredientsHeader);
        favoritesHeader = (TextView) findViewById(R.id.favortiesHeader);
        recipesHeader = (TextView) findViewById(R.id.recipesHeader);

        View.OnClickListener headerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == ingredientsHeader.getId()) {
                    ingredientsHeader.setPaintFlags(ingredientsHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                    favoritesHeader.setPaintFlags(0);
                    //TODO change page to ingedietns
                    pager.setCurrentItem(2);
                }
                else if (v.getId() == favoritesHeader.getId()) {
                    favoritesHeader.setPaintFlags(favoritesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    recipesHeader.setPaintFlags(0);
                    ingredientsHeader.setPaintFlags(0);
                    //TODO change page to favs
                    pager.setCurrentItem(1);
                } else {
                    recipesHeader.setPaintFlags(recipesHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    favoritesHeader.setPaintFlags(0);
                    ingredientsHeader.setPaintFlags(0);
                    //TODO change page to recipes
                    pager.setCurrentItem(0);
                }
            }
        };
        ingredientsHeader.setOnClickListener(headerListener);
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
        if(list != null){
            serverRecipes = list;
            server.updateListAdapter(serverRecipes);
        }else{
            Log.d(TAG, "Error - No recipes loaded from server");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            //TODO handle case where no recipes are retrieved from server
        }

        // Get recipes from local db
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if (favoriteRecipes != null) {
            favoriteRecipes.addAll(db.getRecipes());
        } else {
            favoriteRecipes = new RecipeList(db.getRecipes());
        }

        // Notify list view adapter that the list has changed
        favs.updateListAdapter(favoriteRecipes);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Intent does not equal action search");
        }
    }

    // PageViewer
    @Override
    public void onFragmentInteraction(Uri uri) {

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
        switch (id) {
            case R.id.nav_profile:
                Intent i = new Intent(this, UserProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_shopping_list:
                Intent intent = new Intent(this.getApplicationContext(), ShoppingListActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_us:
                Toast.makeText(this, "Contact Us!", Toast.LENGTH_SHORT).show();
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