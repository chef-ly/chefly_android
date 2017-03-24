package com.se491.chef_ly;
//https://google.github.io/android-testing-support-library/docs/espresso/intents/

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.se491.chef_ly.activity.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest // filter for >2s execution time
public class TestMainActivity {
    @Rule
    public MainActivityTestRule<MainActivity> mainActivityActivityTestRule = new MainActivityTestRule<>(MainActivity.class);

    @Test
    public void clickSignInButton_openRecipeScreen() { //continue to recipe screen
        String username = "a";
        String password = "a";
        //locate the username edit text and type in the email address
        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard());//ViewMatcher
        //locate the password edit text and type in the password
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());//ViewMatcher
        onView(withId(R.id.signInBtn)).perform(click()); //click on it to perform action

    }


}