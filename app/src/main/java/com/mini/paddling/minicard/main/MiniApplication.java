package com.mini.paddling.minicard.main;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MiniApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Fresco.initialize(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    public static Context getContext() {
        return context;
    }
}
