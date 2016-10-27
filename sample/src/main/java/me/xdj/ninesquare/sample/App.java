package me.xdj.ninesquare.sample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * Created by xdj on 16/7/3.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
