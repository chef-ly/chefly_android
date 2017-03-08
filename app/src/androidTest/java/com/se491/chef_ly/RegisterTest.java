package com.se491.chef_ly;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.se491.chef_ly.activity.RegisterActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest // filter for >2s execution time
public class RegisterTest {

    @Rule
    public MainActivityTestRule<RegisterActivity>RegisterTestRule = new MainActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void clickSignUpButton_opensRegisterScreen() {
        //Locate the Signup button using the withId ViewMatcher
        // onView(withId(R.id.signUp)).perform(click()); //click on it to perform action


        //Assert that the register screen is displayed
        onView(withId(R.id.registerInput)).check(matches(allOf(isDescendantOfA(withId(R.id.activity_register)), isDisplayed()))).perform(replaceText("a"), closeSoftKeyboard());;

        onView(allOf(withId(R.id.registerButton), withParent(allOf(withId(R.id.activity_register),
                withParent(withId(android.R.id.content)))),
                isDisplayed())).perform(click());


        ViewInteraction editText2 = onView(
                allOf(withId(R.id.registerInput),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText2.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.registerButton),
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
                allOf(withId(R.id.registerButton),
                        withParent(allOf(withId(R.id.activity_register),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button3.perform(click());

    }


}

//    @Test
//    public void clickNextButton_UsernameInput() { //continue to recipe screen
//        String username = "a";
//        String password = "a";
//        onView(withId(R.id.registerInput)).perform(typeText(username), closeSoftKeyboard());//ViewMatcher
//        onView(withId(R.id.registerButton)).perform(click()); //click on it to perform action
//        //onView(withId(R.id.registerButton)).check(matches(isDisplayed()));
//         }

