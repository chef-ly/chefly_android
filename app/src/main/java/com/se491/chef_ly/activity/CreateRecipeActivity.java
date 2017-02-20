package com.se491.chef_ly.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.create_recipe_fragments.FirstFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.FourthFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.SecondFragment;
import com.se491.chef_ly.activity.create_recipe_fragments.ThirdFragment;
import com.se491.chef_ly.model.Ingredient;
import com.se491.chef_ly.model.RecipeDetail;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity
        implements FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener {

    private FragmentPagerAdapter adapterViewPager;
    private static FragmentManager manager;
    private static FirstFragment firstFragment;
    private static SecondFragment secondFragment;
    private static ThirdFragment thirdFragment;
    private static FourthFragment fourthFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        manager = getSupportFragmentManager();

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyAdapter(manager);
        vpPager.setAdapter(adapterViewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void createRecipie(){
        RecipeDetail result = null;

        // TODO verify results not null

        String recipeTitle = firstFragment.getRecipeTitle();
        String recipeImage = firstFragment.getRecipeImage();
        String recipeDescription = firstFragment.getRecipeDescription();

        String recipeTime = secondFragment.getRecipeTime();
        String recipeServings = secondFragment.getRecipeServings();
        String[] recipeCategories = secondFragment.getRecipeCategories();

        ArrayList<Ingredient> ingredients = thirdFragment.getIngredients();
        ArrayList<String> directions = fourthFragment.getDirections();

        // result = new RecipeDetail(recipeTitle, "getUsersName", recipeDescription, recipeServings, recipeTime, recipeCategories, recipeImage, ingredients, directions);
        int id = result.hashCode(); // replace with id from server
        //TODO get username for author
        //TODO send to server
        //TODO save to local db
        Intent recipeDetail = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        recipeDetail.putExtra("recipe", id);
        startActivity(recipeDetail);
        finish();
    }

    public static class MyAdapter extends FragmentPagerAdapter{
        private static final int NUM_PAGES = 4;
        //page 1 -> FirstFragment - recipe title, image, and description
        //page 2 -> SecondFragment -time, serves, categories
        //page 3 -> ThirdFragment -ingredients
        //page 4 -> FourthFragment -directions

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return firstFragment = FirstFragment.newInstance("Create Recipe", 1);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return secondFragment = SecondFragment.newInstance("Create Recipe", 2);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return thirdFragment = ThirdFragment.newInstance("Create Recipe", 3);
                case 3: // Fragment # 1 - This will show SecondFragment
                    return fourthFragment = FourthFragment.newInstance("Create Recipe", 4);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }


    }
}
