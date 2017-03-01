package com.se491.chef_ly.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.se491.chef_ly.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestRegister {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testRegister() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.signUp), withText("Sign Up Now!"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.registerInput),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText.perform(replaceText("Athina"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.registerButton), withText("Next"),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.registerInput),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText2.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.registerButton), withText("Next"),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.registerInput),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText3.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.registerButton), withText("Done"),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.perform(click());

    }

}
