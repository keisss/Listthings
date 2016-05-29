package com.keiss.listthings.util;

import android.app.Application;

/**
 * Created by hekai on 16/5/16.
 */
public class ContextGetter extends Application {
    private static ContextGetter instance;

    public static ContextGetter getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

}
