package com.se491.chef_ly;
import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import com.se491.chef_ly.activity.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public MainActivityTestRule<MainActivity> mainActivityActivityTestRule = new MainActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testUI() {
        Activity activity = mainActivityActivityTestRule.getActivity();
        assertNotNull(activity.findViewById(R.id.continueAsGuest));
        TextView guestView = (TextView) activity.findViewById(R.id.continueAsGuest);
        assertTrue(guestView.isShown());
        //assertEquals("starts activity recipe", helloView.getText());

    }

}
