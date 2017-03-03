package com.se491.chef_ly.test;

import com.se491.chef_ly.activity.MainActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class login extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;

  	public login() {
		super(MainActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}

   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}

	public void testRun() {
        //Wait for activity: 'com.se491.chef_ly.activity.MainActivity'
		solo.waitForActivity(com.se491.chef_ly.activity.MainActivity.class, 2000);
        //Enter the text: 'a'
		solo.clearEditText((android.widget.EditText) solo.getView(com.se491.chef_ly.R.id.username));
		solo.enterText((android.widget.EditText) solo.getView(com.se491.chef_ly.R.id.username), "a");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.password));
        //Enter the text: 'a'
		solo.clearEditText((android.widget.EditText) solo.getView(com.se491.chef_ly.R.id.password));
		solo.enterText((android.widget.EditText) solo.getView(com.se491.chef_ly.R.id.password), "a");
        //Click on Sign In
		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.signInBtn));
        //Wait for activity: 'com.se491.chef_ly.activity.RecipeListActivity'
		assertTrue("com.se491.chef_ly.activity.RecipeListActivity is not found!", solo.waitForActivity(com.se491.chef_ly.activity.RecipeListActivity.class));
	}
}
