package com.se491.chef_ly;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.se491.chef_ly.activity.MainActivity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//functional / blackbox


@RunWith(AndroidJUnit4.class)
public class FunctionalityTest {
    @Rule
    public MainActivityTestRule<MainActivity> mainActivityActivityTestRule = new MainActivityTestRule<MainActivity>(MainActivity.class);

        @Test
        public void testPressBackButton() {
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack();
        }

    @Test

    public void testUiDevice() throws RemoteException {
        UiDevice device = UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation());
        if (device.isScreenOn()) {
            device.setOrientationLeft();
            device.openNotification();
        }
    }

}
