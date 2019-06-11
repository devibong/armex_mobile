package com.qsystem.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class GlobalApplication extends Application {
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }
    public static Context getAppContext() {
        return appContext;
    }
}
