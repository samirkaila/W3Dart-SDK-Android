package com.sdkdemo;

import android.app.Application;
import android.util.Log;

import com.w3dartsdk.DartBug;

public class AppController extends Application {

//    private static PrefManager prefManager;

    static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
//        new Instabug.Builder(this, "ac205486b192e63953d9ee86588b427c")
//                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.FLOATING_BUTTON)
//                .build();


        new DartBug.Builder(this, "Demo@gmail.com")
                .setDebugEnabled(true)
                .enableFloating(true)
                .build();

        instance = this;
        Log.e("AppController", " AppController on create");
//        registerActivityLifecycleCallbacks(new TextOverlayActivityLifecycleCallbacks());

    }

//    public static PrefManager getPrefManager() {
//        if (prefManager == null) {
//            prefManager = new PrefManager(instance);
//        }
//        return prefManager;
//    }


    /*@Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        Log.e("AppController", " AppController startActivity called");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("AppController", " AppController onTerminate called ----------------");
    }



 @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
        Log.e("AppController", " AppController registerComponentCallbacks called: " + callback.getClass().getSimpleName());
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
        Log.e("AppController", " AppController unregisterComponentCallbacks called: " + callback.getClass().getSimpleName());
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
        Log.e("AppController", " AppController registerActivityLifecycleCallbacks called: " + callback.getClass().getSimpleName());
    }

    @Override
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.unregisterActivityLifecycleCallbacks(callback);
        Log.e("AppController", " AppController unregisterActivityLifecycleCallbacks called: " + callback.getClass().getSimpleName());
        unregisterActivityLifecycleCallbacks(textOverlayActivityLifecycleCallbacks);
    }*/
}