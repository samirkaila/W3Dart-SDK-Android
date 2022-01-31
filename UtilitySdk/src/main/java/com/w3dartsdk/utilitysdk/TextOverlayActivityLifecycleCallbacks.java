package com.w3dartsdk.utilitysdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.w3dartsdk.drawover.ShowHudService;
import com.w3dartsdk.utilitysdk.screenrecord.ScreenCapture;

/**
 * Activity lifecycle callbacks that handle {@link TextOverlayService} interactions.
 * <p>
 * Handles start / stop of the service as well as asking for the required permissions on
 * API level 23 or later.
 *
 * @see <a href="https://developer.android.com/reference/android/Manifest.permission.html#SYSTEM_ALERT_WINDOW">SYSTEM_ALERT_WINDOW</a>
 * @see <a href="https://developer.android.com/reference/android/provider/Settings.html#ACTION_MANAGE_OVERLAY_PERMISSION">ACTION_MANAGE_OVERLAY_PERMISSION</a>
 */
public final class TextOverlayActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = TextOverlayActivityLifecycleCallbacks.class.getSimpleName();
    int numStarted = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        Log.e(TAG, "onActivityCreated");
        /*if (!activity.getPackageName().equals("com.w3dartsdk")) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(activity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                }
            }
        }*/
    }

    @Override
    public void onActivityStarted(Activity activity) {
        numStarted++;
        Log.e(TAG, "onActivityStarted: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName() + " getLocalClassName:" + activity.getLocalClassName() + " numStarted:" + numStarted);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG, "onActivityResumed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName() + " getLocalClassName:" + activity.getLocalClassName() + " numStarted:" + numStarted);
        if (!activity.getLocalClassName().contains("com.w3dartsdk")) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(activity)) {
                    Log.i(TAG, "onActivityResumed: API level 23 and can draw overlays");
                    activity.startService(new Intent(activity, ShowHudService.class));
                }
            } else {
                Log.i(TAG, "onActivityResumed: Can draw overlays");
                activity.startService(new Intent(activity, ShowHudService.class));
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityPaused: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName() + " getLocalClassName:" + activity.getLocalClassName() + " SDKConstants.ENABLE_SCREEN_SHOT: " + SDKConstants.ENABLE_SCREEN_SHOT + " numStarted:" + numStarted);
        if (!activity.getLocalClassName().contains("com.w3dartsdk")) {
            if (SDKConstants.ENABLE_SCREEN_SHOT) {
                ScreenCapture.setActivityScreenCapture(activity);
            }
        } else {
            activity.stopService(new Intent(activity, ShowHudService.class));
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        --numStarted;
        Log.e(TAG, "onActivityStopped: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName() + " getLocalClassName:" + activity.getLocalClassName() + " numStarted:" + numStarted);

        if (numStarted == 0) {
            activity.stopService(new Intent(activity, ShowHudService.class));
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "onActivityDestroyed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName() + " getLocalClassName:" + activity.getLocalClassName() + " numStarted:" + numStarted);
    }


//    @Override
//    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//        Log.e(TAG, "onActivityPreCreated: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//        Log.e(TAG, "onActivityPostCreated: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPreStarted(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPreStarted: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostStarted(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPostStarted: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPreResumed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPreResumed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostResumed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPostResumed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPrePaused(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPrePaused: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostPaused(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPostPaused: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPreStopped(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPreStopped: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostStopped(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPostStopped: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//        Log.e(TAG, "onActivityPreSaveInstanceState: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//        Log.e(TAG, "onActivityPostSaveInstanceState: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPreDestroyed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPreDestroyed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }
//
//    @Override
//    public void onActivityPostDestroyed(@NonNull Activity activity) {
//        Log.e(TAG, "onActivityPostDestroyed: " + activity.getClass().getSimpleName() + " Package name:" + activity.getPackageName());
//    }


}