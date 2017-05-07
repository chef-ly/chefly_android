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
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void registerTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

      //  PermissionGranter.allowPermissionsIfNeeded("RECORD_AUDIO");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.signUp), withText("Join for free"),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.registerButton), withText("Next"),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.registerInput),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText.perform(pressImeActionButton());

    }

}
