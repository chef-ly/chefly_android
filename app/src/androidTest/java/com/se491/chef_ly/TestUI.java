<<<<<<< HEAD:app/src/androidTest/java/com/se491/chef_ly/TestUI.java
package com.se491.chef_ly;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.se491.chef_ly.R;
import com.se491.chef_ly.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestUI {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testUI() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.continueAsGuest), withText("Continue As Guest"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.list),
                                withParent(withId(R.id.activity_recipe_list))),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction checkBox = onView(
                allOf(withText("linquine pasta - 4.0 ounce"),
                        withParent(withId(R.id.ingredientGroup))));
        checkBox.perform(scrollTo(), click());

        ViewInteraction checkBox2 = onView(
                allOf(withText("boneless, skinless chicken breasts - 2.0 breasts"),
                        withParent(withId(R.id.ingredientGroup))));
        checkBox2.perform(scrollTo(), click());

        ViewInteraction checkBox3 = onView(
                allOf(withText("Cajun Seasoning - 2.0 tespoons"),
                        withParent(withId(R.id.ingredientGroup))));
        checkBox3.perform(scrollTo(), click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.addToListBtn), withText("Add Selected to Grocery List")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.getCookingBtn), withText("Let's get cooking!")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction button = onView(
                allOf(withId(R.id.next), withText("Next"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.next), withText("Next"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.next), withText("Next"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.next), withText("Next"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button4.perform(click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.next), withText("Done"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button5.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Shopping List"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.list),
                                withParent(withId(R.id.activity_shopping_list))),
                        0),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction button6 = onView(
                allOf(withId(R.id.clearPurchasedBtn), withText("Remove Purchased"), isDisplayed()));
        button6.perform(click());

        ViewInteraction button7 = onView(
                allOf(withId(R.id.finishedBtn), withText("Finished"), isDisplayed()));
        button7.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
=======
//package com.se491.chef_ly;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//import android.view.WindowManager;
//
//import com.se491.chef_ly.activity.MainActivity;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.closeSoftKeyboard;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class CartTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//    @Before public void setup() { closeSoftKeyboard(); }
//    @Before
//    public void unlockScreen() {
//        final MainActivity activity = mActivityTestRule.getActivity();
//        Runnable wakeUpDevice = new Runnable() {
//            public void run() {
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            }
//        };
//        activity.runOnUiThread(wakeUpDevice);
//    }
//    @Test
//    public void cartTest() {
//        ViewInteraction appCompatTextView = onView(
//                allOf(withId(R.id.continueAsGuest), withText("Continue As Guest"), isDisplayed()));
//        appCompatTextView.perform(click());
//
//        ViewInteraction linearLayout = onView(
//                allOf(childAtPosition(
//                        allOf(withId(R.id.list),
//                                withParent(withId(R.id.activity_recipe_list))),
//                        2),
//                        isDisplayed()));
//        linearLayout.perform(click());
//
//        ViewInteraction checkBox = onView(
//                allOf(withText("bread - 2.0 slice"),
//                        withParent(withId(R.id.ingredientGroup))));
//        checkBox.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.addToListBtn), withText("Add Selected to Grocery List")));
//        appCompatButton.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.backBtn), withText("Back")));
//        appCompatButton2.perform(scrollTo(), click());
//
//        ViewInteraction appCompatImageButton = onView(
//                allOf(withContentDescription("Open navigation drawer"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed()));
//        appCompatImageButton.perform(click());
//
//        ViewInteraction appCompatCheckedTextView = onView(
//                allOf(withId(R.id.design_menu_item_text), withText("Shopping List"), isDisplayed()));
//        appCompatCheckedTextView.perform(click());
//
//        ViewInteraction linearLayout2 = onView(
//                allOf(childAtPosition(
//                        allOf(withId(android.R.id.list),
//                                withParent(withId(R.id.activity_shopping_list))),
//                        0),
//                        isDisplayed()));
//        linearLayout2.perform(click());
//
//        ViewInteraction linearLayout3 = onView(
//                allOf(childAtPosition(
//                        allOf(withId(android.R.id.list),
//                                withParent(withId(R.id.activity_shopping_list))),
//                        0),
//                        isDisplayed()));
//        linearLayout3.perform(click());
//
//        ViewInteraction button = onView(
//                allOf(withId(R.id.clearPurchasedBtn), withText("Remove Purchased"), isDisplayed()));
//        button.perform(click());
//
//        ViewInteraction button2 = onView(
//                allOf(withId(R.id.finishedBtn), withText("Finished"), isDisplayed()));
//        button2.perform(click());
//
//        ViewInteraction appCompatImageButton2 = onView(
//                allOf(withContentDescription("Open navigation drawer"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed()));
//        appCompatImageButton2.perform(click());
//
//        ViewInteraction appCompatCheckedTextView2 = onView(
//                allOf(withId(R.id.design_menu_item_text), withText("Shopping List"), isDisplayed()));
//        appCompatCheckedTextView2.perform(click());
//
//        ViewInteraction linearLayout4 = onView(
//                allOf(childAtPosition(
//                        allOf(withId(android.R.id.list),
//                                withParent(withId(R.id.activity_shopping_list))),
//                        0),
//                        isDisplayed()));
//        linearLayout4.perform(click());
//
//        ViewInteraction button3 = onView(
//                allOf(withId(R.id.clearPurchasedBtn), withText("Remove Purchased"), isDisplayed()));
//        button3.perform(click());
//
//        ViewInteraction button4 = onView(
//                allOf(withId(R.id.finishedBtn), withText("Finished"), isDisplayed()));
//        button4.perform(click());
//
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
//}
>>>>>>> 7cb257133561a3941d196eff182b969f3e372353:app/src/androidTest/java/com/se491/chef_ly/CartTest.java
