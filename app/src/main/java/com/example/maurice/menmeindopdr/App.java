package com.example.maurice.menmeindopdr;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.maurice.menmeindopdr.GPS.GpsService;

import java.lang.ref.WeakReference;

public class App extends Application {

    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, GpsService.class));
//        } else {
//            startService(new Intent(this, GpsService.class));
//        }

        context = new WeakReference<>(this);
    }

    public static Context getContext(){ return context.get(); }
}
