package com.se491.chef_ly;
import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
//create my own rules
public class MainActivityTestRule<A extends Activity> extends ActivityTestRule<A> {

    public MainActivityTestRule(Class<A> activityClass) {
        super(activityClass);
    }
    @Override
    protected Intent getActivityIntent() { //set up the intent
        Log.e("MainActivityTestRule", "set up intent");
        return super.getActivityIntent();
    }

    @Override
    protected void beforeActivityLaunched() { //code that runs bfore the activity is created and launched
        Log.e("MainActivityTestRule", "before launched");
        super.beforeActivityLaunched();
    }

    @Override
    protected void afterActivityLaunched() { //after launched but before the @before/tests execution
        Log.e("MainActivityTestRule", "after launched");
        super.afterActivityLaunched();
    }

    @Override
    protected void afterActivityFinished() { //code that run after the activity is finished
        Log.e("MainActivityTestRule", "cleanup");
        super.afterActivityFinished();
    }

    @Override
    public A launchActivity(Intent startIntent) { //launch the activity being tested
        Log.e("MainActivityTestRule", "launching");
        return super.launchActivity(startIntent);
    }

}
