package com.example.putrosw.touristpartner;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.eyro.mesosfer.Mesosfer;

/**
 * Created by Putro SW on 21-Nov-16.
 */
public class MesosferApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Mesosfer.setPushNotification(true);
        Mesosfer.initialize(this, "YbEGFa6emT", "uHASt2zVyRekuCf5aTYXn5SY1594Sdl3");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
