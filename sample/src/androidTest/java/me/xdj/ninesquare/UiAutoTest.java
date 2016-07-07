package me.xdj.ninesquare;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiAutomatorTestCase;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Created by xdj on 16/7/7.
 */
@RunWith(AndroidJUnit4.class)
public class UiAutoTest extends UiAutomatorTestCase {
    private static final String PACKAGE_NAME = "me.xdj.ninesquare.sample";

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() throws IOException, RemoteException {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        final String launcherPackage = mDevice.getLauncherPackageName();
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000);

        Context context = InstrumentationRegistry.getContext();
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_NAME);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5000);
    }

    @Test
    public void autoTest() throws UiObjectNotFoundException, IOException {
//        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        mDevice.pressHome();

//        final String launcherPackage = mDevice.getLauncherPackageName();
//        Runtime.getRuntime().exec("am start me.xdj.ninesquare.sample/me.xdj.ninesquare.sample.MainActivity");
        UiSelector uiSelector = new UiSelector().resourceId("me.xdj.ninesquare.sample:id/image");
        UiObject uiObject = mDevice.findObject(uiSelector);
        if (uiObject.exists()) {
            uiObject.click();
        }
//        SystemClock.sleep(5000);
        mDevice.waitForIdle();
//        uiObject.swipeLeft(1);
        for (int i = 0; i < 11; i++) {
            // 1步5毫秒
            mDevice.swipe(900, 1000, 10, 900, 100);
//        UiObject uiObject1 = mDevice.findObject(new UiSelector().resourceId("me.xdj.ninesquare.sample:id"));
//        uiObject1.swipeLeft(100);
            SystemClock.sleep(900);
        }
        mDevice.click(900, 100);
        SystemClock.sleep(3000);
    }
}
