package com.mini.paddling.minicard.main;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MiniApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
