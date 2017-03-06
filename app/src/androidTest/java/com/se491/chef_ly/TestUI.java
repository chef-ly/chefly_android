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
                        1),
                        isDisplayed()));
        linearLayout.perform(click());



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
                allOf(withId(R.id.repeat), withText("Repeat"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.prev), withText("Prev"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.exit), withText("X"),
                        withParent(allOf(withId(R.id.activity_get_cooking),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button4.perform(click());

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
