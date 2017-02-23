package com.se491.chef_ly;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.se491.chef_ly.activity.RegisterActivity;
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
public class RegisterTest {

    @Rule
    public MainActivityTestRule<RegisterActivity>RegisterTestRule = new MainActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void clickNextButton_UsernameInput() { //continue to recipe screen
        String username = "b";
        onView(withId(R.id.registerInput)).perform(typeText(username), closeSoftKeyboard());//ViewMatcher
        onView(withId(R.id.registerButton)).perform(click()); //click on it to perform action
        //onView(withId(R.id.registerButton)).check(matches(isDisplayed()));
         }

}
