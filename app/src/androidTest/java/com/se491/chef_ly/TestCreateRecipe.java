package com.se491.chef_ly;

        import android.os.SystemClock;
        import android.support.test.espresso.ViewInteraction;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.test.suitebuilder.annotation.LargeTest;

        import com.se491.chef_ly.R;
        import com.se491.chef_ly.activity.MainActivity;

        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
        import static android.support.test.espresso.action.ViewActions.replaceText;
        import static android.support.test.espresso.action.ViewActions.scrollTo;
        import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
        import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static android.support.test.espresso.matcher.ViewMatchers.withParent;
        import static android.support.test.espresso.matcher.ViewMatchers.withText;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestCreateRecipe {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateRecipe() {

        // We need to wait for the data to be retrieved from the server. A 10 second interval
        // should be more than enough for the action to complete.
        SystemClock.sleep(15000);


        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.continueAsGuest), withText("Continue As Guest"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Create new recipe"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.recipeName), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.recipeName), isDisplayed()));
        editText2.perform(replaceText("Italian Pasta"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.recipeAuthor), isDisplayed()));
        editText3.perform(replaceText("Athina"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.recipeDescription), isDisplayed()));
        editText4.perform(replaceText("Pasta"), closeSoftKeyboard());

        ViewInteraction textViewl = onView(
                allOf(withText("Page 2"),
                        withParent(allOf(withId(R.id.pager_header),
                                withParent(withId(R.id.pager)))),
                        isDisplayed()));
        textViewl.perform(click());


        ViewInteraction editText5 = onView(
                allOf(withId(R.id.recipeTime), isDisplayed()));
        editText5.perform(click());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.recipeTime), isDisplayed()));
        editText6.perform(replaceText("30"), closeSoftKeyboard());

        ViewInteraction editText7 = onView(
                allOf(withId(R.id.recipeServings), isDisplayed()));
        editText7.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.recipeCategories), isDisplayed()));
        editText8.perform(replaceText("Italian"), closeSoftKeyboard());

        ViewInteraction textView = onView(
                allOf(withText("Page 3"),
                        withParent(allOf(withId(R.id.pager_header),
                                withParent(withId(R.id.pager)))),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction editText9 = onView(
                withId(R.id.ingredientQty));
        editText9.perform(scrollTo(), replaceText("6"), closeSoftKeyboard());

        ViewInteraction editText10 = onView(
                withId(R.id.ingredientUom));
        editText10.perform(scrollTo(), replaceText("ounces"), closeSoftKeyboard());

        ViewInteraction editText11 = onView(
                withId(R.id.ingredientName));
        editText11.perform(scrollTo(), replaceText("bucatini"), closeSoftKeyboard());
//
//        ViewInteraction button = onView(
//                allOf(withId(R.id.newLineBtn), withText("+"),
//                        withParent(allOf(withId(R.id.buttons),
//                                withParent(withId(R.id.root))))));
//        button.perform(scrollTo(), click());

//        ViewInteraction editText12 = onView(
//                withClassName(is("android.widget.EditText")));
//        editText12.perform(scrollTo(), replaceText("4"), closeSoftKeyboard());
//
//        ViewInteraction editText13 = onView(
//                withClassName(is("android.widget.EditText")));
//        editText13.perform(scrollTo(), replaceText("ounces"), closeSoftKeyboard());
//
//        ViewInteraction editText14 = onView(
//                withClassName(is("android.widget.EditText")));
//        editText14.perform(scrollTo(), replaceText("pancetta"), closeSoftKeyboard());

        ViewInteraction textView2 = onView(
                allOf(withText("Page 4"),
                        withParent(allOf(withId(R.id.pager_header),
                                withParent(withId(R.id.pager)))),
                        isDisplayed()));
        textView2.perform(click());

        ViewInteraction editText15 = onView(
                allOf(withId(R.id.step),
                        withParent(allOf(withId(R.id.root),
                                withParent(withId(R.id.scrollView))))));
        editText15.perform(scrollTo(), replaceText("boid pasta"), closeSoftKeyboard());

//        ViewInteraction button2 = onView(
//                allOf(withId(R.id.completeRecipeBtn), withText("Create Recipe"),
//                        withParent(allOf(withId(R.id.fourthFrag),
//                                withParent(withId(R.id.pager)))),
//                        isDisplayed()));
//        button2.perform(click());

    }

}

