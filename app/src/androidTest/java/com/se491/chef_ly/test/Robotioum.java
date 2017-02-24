package com.se491.chef_ly.test;

import com.se491.chef_ly.activity.MainActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

//http://www.vogella.com/tutorials/Robotium/article.html
//http://www.softwaretestinghelp.com/robotium-tutorial-android-application-ui-testing-tool/
public class Robotioum extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;
  	
  	public Robotioum() {
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
        //Take screenshot
        solo.takeScreenshot();
        //Wait for activity: 'com.se491.chef_ly.activity.MainActivity'
		solo.waitForActivity(com.se491.chef_ly.activity.MainActivity.class, 2000);
        //Set default small timeout to 112199 milliseconds
		Timeout.setSmallTimeout(112199);
        //Click on Continue As Guest
		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.continueAsGuest));
        //Wait for activity: 'com.se491.chef_ly.activity.RecipeListActivity'
		assertTrue("com.se491.chef_ly.activity.RecipeListActivity is not found!", solo.waitForActivity(com.se491.chef_ly.activity.RecipeListActivity.class));
	}
}
