package com.w3dartsdk.utilitysdk.task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.w3dartsdk.utilitysdk.SDKConstants;

import java.util.List;

/*
    This is the class for getting most recent display screen related information from the application
 */
public class AppTaskUtility {

    static String tag = AppTaskUtility.class.getSimpleName();
    static String appTaskScreenName = "";

    public AppTaskUtility(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);


//            Log.e(tag, "getClassName: " + am.getAppTasks().get(0).getTaskInfo().topActivity.getClassName());
//            for (int i = 0; i < am.getAppTasks().size(); i++) {
//                Log.i(tag, "getClassName: " + am.getAppTasks().get(i).getTaskInfo().topActivity.getClassName());
//            }

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                cn = am.getAppTasks().get(0).getTaskInfo().topActivity;
//            } else {
        //noinspection deprecation

//                for (int i = 0; i < am.getRunningTasks(99).size(); i++) {
//                    cn = am.getRunningTasks(99).get(i).topActivity;
//                    Log.i(tag, "getClassName: " + cn.getClassName());
//                }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();

        for (ActivityManager.AppTask task : tasks) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appTaskScreenName = task.getTaskInfo().topActivity.getClassName();
//                Log.e(tag, "M > stackId: " + appTaskScreenName);
            } else {
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                appTaskScreenName = cn.getClassName();
//                Log.e(tag, "M < getClassName: " + appTaskScreenName);
            }
        }

//            }


//        isBackgroundRunning(context);

//            List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
//
//            for (ActivityManager.AppTask task : appTasks) {
//                Log.i(tag, "App screen: " + task.getTaskInfo().topActivity.getClassName());
//            }


/*
        System.out.println("----------------------------------AppTask Utility Start-------------------------------------------------");
        Log.e(tag, "appTaskScreenName: " + appTaskScreenName);
        System.out.println("----------------------------------AppTask Utility END-------------------------------------------------");
*/

    }

    public static synchronized String getDetail() {
        return "AppTask { \"" + SDKConstants.KEY_APP_TASK_SCREEN_NAME + "\" : \"" + appTaskScreenName + "\"" +
//                " , " + " \"status\" : \"" + chargingStatus + "\"" +
                "}";
//        return "";
    }


    public static boolean isBackgroundRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.e(tag, "mBroadcastReceiver chargingStatus: " + processInfo.processName);
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        //If your app is the process in foreground, then it's not in running in background
                        return false;
                    }
                }
            }
        }


        return true;
    }
}
