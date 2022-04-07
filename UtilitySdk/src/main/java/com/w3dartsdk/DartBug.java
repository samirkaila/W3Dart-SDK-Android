package com.w3dartsdk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.w3dartsdk.drawover.AddBugReportActivity;
import com.w3dartsdk.drawover.ShowHudService;
import com.w3dartsdk.utilitysdk.SDKConstants;
import com.w3dartsdk.utilitysdk.TextOverlayActivityLifecycleCallbacks;
import com.w3dartsdk.utilitysdk.battery.BatteryUtility;
import com.w3dartsdk.utilitysdk.cpu.CpuUtility;
import com.w3dartsdk.utilitysdk.device.DeviceUtility;
import com.w3dartsdk.utilitysdk.gpu.GpuUtility;
import com.w3dartsdk.utilitysdk.hardware.HardwareUtility;
import com.w3dartsdk.utilitysdk.memory.MemoryUtility;
import com.w3dartsdk.utilitysdk.network.NetworkUtility;
import com.w3dartsdk.utilitysdk.screen.ScreenDisplayUtility;
import com.w3dartsdk.utilitysdk.shake.ShakeDetector;

/*
    This is the main class of Dart sdk to be called in application level class in application
 */
public class DartBug {

//new DartBug.Builder(this, "ac205486b192e63953d9ee86588b427c")
//        .setInvocationEvents(DartBugInvocationEvent.SHAKE, DartBugInvocationEvent.FLOATING_BUTTON)
//                .build();


//        new DartBug.Builder(this, "Demo@gmail.com")
//                .setDebugEnabled(true)
//                .build();

    private static final String tag = "DartBug";

    // This is the time to store start time when app is created in memory
    public static long appStartTime;

    // This is variable to store application version
    public static String appVersion = "";

    // this is the builder class that will build and initialize the sdk
    public static class Builder {

        private static Application application;
        private static volatile boolean isBuildCalled;
        boolean isDebuggingEnable;

        //        private String applicationToken;
        //        private DartBugInvocationEvent[] dartBugInvocationEvents;


        // The following are used for the shake detection
        private SensorManager mSensorManager;
        private Sensor mAccelerometer;
        private ShakeDetector mShakeDetector;


        public Builder(@NonNull Application application, @NonNull String applicationToken, @NonNull String refAppVersion) {

            DartBug.Builder builder = this;
            this.application = application;
//            this.applicationToken = applicationToken;

            // this code is used to register activity call back for application who implement this sdk
            application.registerActivityLifecycleCallbacks(new TextOverlayActivityLifecycleCallbacks());

            //Here we take current time of device when app started for first time
            appStartTime = System.currentTimeMillis();

            //Here we take app version of application who implemented this sdk
            appVersion = refAppVersion;

            // here we initialize the shake event
            initializeShake();
        }

//        public DartBug.Builder setInvocationEvents(@NonNull DartBugInvocationEvent... DartBugInvocationEvents) {
//            this.dartBugInvocationEvents = DartBugInvocationEvents;
//            return this;
//        }

        public static Application getApplication() {
            return application;
        }

        public DartBug.Builder setDebugEnabled(boolean isDebugEnabled) {
//            SettingsManager.getInstance().setDebugEnabled(isDebugEnabled);
//            DartBugSDKLogger.i("DartBug", "setDebugEnabled " + isDebugEnabled);
            isDebuggingEnable = isDebugEnabled;
            return this;
        }


        // method for starting the service to draw over app button so user can add bug
        public void startService() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // check if the user has already granted
                // the Draw over other apps permission
                if (Settings.canDrawOverlays(application)) {
                    // start the service based on the android version
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(new Intent(this, ForegroundService.class));
                        application.startForegroundService(new Intent(application, ShowHudService.class));
                    } else {
//                    startService(new Intent(this, ForegroundService.class));
                        application.startService(new Intent(application, ShowHudService.class));
                    }
                }
            } else {
//            startService(new Intent(this, ForegroundService.class));
                application.startService(new Intent(application, ShowHudService.class));
            }
        }


        //        public DartBug.Builder setConsoleLogState(@NonNull State state) {
//            this.consoleLogState = state;
//            return this;
//        }
//
//        public DartBug.Builder setDartBugLogState(@NonNull State state) {
//            this.DartBugLogState = state;
//            return this;
//        }
//
//        public DartBug.Builder setUserDataState(@NonNull State state) {
//            this.userDataState = state;
//            return this;
//        }
//
//        public DartBug.Builder setInAppMessagingState(@NonNull State state) {
//            this.inAppMessagingState = state;
//            return this;
//        }
//
//        public DartBug.Builder setViewHierarchyState(@NonNull State state) {
//            this.viewHierarchyState = state;
//            return this;
//        }
//
//        public DartBug.Builder setUserEventsState(@NonNull State state) {
//            this.userEventsState = state;
//            return this;
//        }


        // method to ask user to grant the Overlay permission
        public void checkOverlayPermission() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(application)) {
                    // send user to the device settings
                    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivity(myIntent);
                } else {
                    startService();
                }
            } else {
                startService();
            }
        }

        // This method is used to enable to draw over app to show button to register error using sdk
        public DartBug.Builder enableFloating(boolean isEnable) {
            // Instantiate the gesture detector with the application context and an implementation of
            // GestureDetector.OnGestureListener
//            mDetector = new GestureDetectorCompat(applicationContext, this);
            // Set the gesture detector as the double tap listener.
//            mDetector.setOnDoubleTapListener(this);
            if (isEnable)
                checkOverlayPermission();
            return this;
        }

        // This is the method to initialize and enable to get all the information from the device
        @Nullable
        public void build() {
//            DartBugSDKLogger.d("DartBug", "building sdk with default state ");
            if (isBuildCalled) {
//                DartBugSDKLogger.d("DartBug", "isBuildCalled true returning..");
            } else {
                isBuildCalled = true;


//                buildBasicDetail(applicationContext);


                /*State var1;
                if (y2.c().b(DartBug.appContext) == (var1 = State.ENABLED)) {
                    DartBugSDKLogger.d("DartBug", "building sdk in BG");
                    this.buildInBG(var1);
                } else {
                    DartBugSDKLogger.d("DartBug", "building sdk in FG");
                    this.buildInFG(var1);
                }*/

            }
        }

        /*private void buildBasicDetail(Context appContext) {
            DeviceUtility deviceUtility = new DeviceUtility(appContext);
            if (isDebuggingEnable)
                Log.e(tag, "deviceUtility Detail: " + deviceUtility.getDetail());

            ScreenDisplayUtility displayUtility = new ScreenDisplayUtility(appContext);
            if (isDebuggingEnable)
                Log.e(tag, "displayUtility Detail: " + displayUtility.getDetail());


            CpuUtility cpuUtility = new CpuUtility(appContext);
            if (isDebuggingEnable)
                Log.e(tag, "cpuUtility Detail: " + cpuUtility.getDetail());

//            GpuUtility gpuUtility = new GpuUtility(appContext, mView);
            GpuUtility gpuUtility = new GpuUtility(appContext, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isDebuggingEnable)
                        Log.e(tag, "deviceUtility Detail: " + gpuUtility.getDetail());
                }
            }, 2000);

            try {
                HardwareUtility hardwareUtility = new HardwareUtility(appContext);
                if (isDebuggingEnable)
                    Log.e(tag, "hardwareUtility Detail: " + hardwareUtility.getDetail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                NetworkUtility networkUtility = new NetworkUtility(appContext);
                if (isDebuggingEnable)
                    Log.e(tag, "networkUtility Detail: " + networkUtility.getDetail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                MemoryUtility memoryUtility = new MemoryUtility(appContext);
                if (isDebuggingEnable)
                    Log.e(tag, "memoryUtility Detail: " + memoryUtility.getDetail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BatteryUtility batteryUtility = new BatteryUtility(appContext);
                if (isDebuggingEnable)
                    Log.e(tag, "batteryUtility Detail: " + batteryUtility.getDetail());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

        // This method is used to enable shake event when device is shaked
        private void initializeShake() {
            Log.e(tag, "initializeShake called");
            // ShakeDetector initialization
            mSensorManager = (SensorManager) application.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mShakeDetector = new ShakeDetector();
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

                @Override
                public void onShake(int count) {
                    Log.e(tag, "onShake  called count:" + count);
                    /*
                     * The following method, "handleShakeEvent(count):" is a stub //
                     * method you would use to setup whatever you want done once the
                     * device has been shook.
                     */
                    SDKConstants.ENABLE_SCREEN_SHOT = true;
                    application.startActivity(new Intent(application.getApplicationContext(), AddBugReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    application.stopService(new Intent(application, ShowHudService.class));

                }
            });

        }

    }


}
