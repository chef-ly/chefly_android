//package com.se491.chef_ly.test;
//
//import com.se491.chef_ly.activity.MainActivity;
//import com.robotium.solo.*;
//import android.test.ActivityInstrumentationTestCase2;
//
//
//public class RecipeDetailTest extends ActivityInstrumentationTestCase2<MainActivity> {
//  	private Solo solo;
//
//  	public RecipeDetailTest() {
//		super(MainActivity.class);
//  	}
//
//  	public void setUp() throws Exception {
//        super.setUp();
//		solo = new Solo(getInstrumentation());
//		getActivity();
//  	}
//
//   	@Override
//   	public void tearDown() throws Exception {
//        solo.finishOpenedActivities();
//        super.tearDown();
//  	}
//
//	public void testRun() {
//        //Wait for activity: 'com.se491.chef_ly.activity.MainActivity'
//		solo.waitForActivity(com.se491.chef_ly.activity.MainActivity.class, 2000);
//        //Set default small timeout to 211188 milliseconds
//		Timeout.setSmallTimeout(211188);
//        //Click on Continue As Guest
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.continueAsGuest));
//        //Wait for activity: 'com.se491.chef_ly.activity.RecipeListActivity'
//		assertTrue("com.se491.chef_ly.activity.RecipeListActivity is not found!", solo.waitForActivity(com.se491.chef_ly.activity.RecipeListActivity.class));
//        //Click on Ricotta Meatballs by Athena Difficulty: Easy Time: 3 min Rating: 5.0
//		solo.clickInList(2, 0);
//        //Wait for activity: 'com.se491.chef_ly.activity.RecipeDetailActivity'
//		assertTrue("com.se491.chef_ly.activity.RecipeDetailActivity is not found!", solo.waitForActivity(com.se491.chef_ly.activity.RecipeDetailActivity.class));
//        //Click on olive Oil - 2.0 tablespoon
//		solo.clickOnView(solo.getView(0x1));
//        //Click on Let's get cooking!
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.getCookingBtn));
//        //Take screenshot
//        solo.takeScreenshot();
//        //Wait for activity: 'com.se491.chef_ly.activity.GetCookingActivity'
//		assertTrue("com.se491.chef_ly.activity.GetCookingActivity is not found!", solo.waitForActivity(com.se491.chef_ly.activity.GetCookingActivity.class));
//        //Click on Next
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.next));
//        //Click on Next
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.next));
//        //Click on Next
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.next));
//        //Click on Next
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.next));
//        //Click on Repeat
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.repeat));
//        //Click on Done
//		solo.clickOnView(solo.getView(com.se491.chef_ly.R.id.next));
//	}
//}
