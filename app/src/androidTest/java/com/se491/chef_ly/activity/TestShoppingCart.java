//package com.se491.chef_ly.activity;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import com.se491.chef_ly.R;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
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
//public class TestShoppingCart {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    public void testShoppingCart() {
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
//        ViewInteraction button = onView(
//                allOf(withId(R.id.clearPurchasedBtn), withText("Remove Purchased"), isDisplayed()));
//        button.perform(click());
//
//        ViewInteraction button2 = onView(
//                allOf(withId(R.id.finishedBtn), withText("Finished"), isDisplayed()));
//        button2.perform(click());
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
