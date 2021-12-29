package com.w3dart.utilitysdk.battery;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class BatteryUtility {

    static String tag = BatteryUtility.class.getSimpleName();
    static int percentage = 0;
    static int status;
    static String chargingStatus = "";

    public BatteryUtility(Context context) {
        IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batFilter);

        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percentage = (int) ((level / (float) scale) * 100);

            int extra_status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (extra_status == BatteryManager.BATTERY_STATUS_CHARGING || extra_status == BatteryManager.BATTERY_STATUS_FULL) {
                status = extra_status;
                chargingStatus = "Plugged";
            } else if (extra_status == BatteryManager.BATTERY_STATUS_DISCHARGING || extra_status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                chargingStatus = "unplugged";
            }

//            BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
//            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//            Log.e(tag, "BATTERY_STATUS_CHARGING: " + bm.getIntProperty(BatteryManager.BATTERY_STATUS_CHARGING));
//            Log.e(tag, "BATTERY_STATUS_FULL: " + bm.getIntProperty(BatteryManager.BATTERY_STATUS_FULL));
//            Log.e(tag, "BATTERY_STATUS_DISCHARGING: " + bm.getIntProperty(BatteryManager.BATTERY_STATUS_DISCHARGING));
//            Log.e(tag, "BATTERY_STATUS_NOT_CHARGING: " + bm.getIntProperty(BatteryManager.BATTERY_STATUS_NOT_CHARGING));
//
//            Log.e(tag, "BATTERY_PROPERTY_STATUS: " + bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS));
//            Log.e(tag, "BATTERY_PROPERTY_CHARGE_COUNTER: " + bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER));
//            Log.e(tag, "BATTERY_PROPERTY_ENERGY_COUNTER: " + bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER));
//            Log.e(tag, "BATTERY_PROPERTY_CURRENT_AVERAGE: " + bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE));

//            System.out.println("----------------------------------Device Utility Start-------------------------------------------------");

//            Log.e(tag, "percentage: " + percentage);
//            Log.e(tag, "status: " + status);

//            System.out.println("----------------------------------Device Utility END-------------------------------------------------");

        }

    }


    public static synchronized String getDetail() {
        return "Battery { \"percentage\" : \"" + percentage + "\"" +
                " , " + " \"status\" : \"" + chargingStatus + "\"" +
                "}";
    }

    /*private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // get the battery level
            int extra_status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            if (extra_status == BatteryManager.BATTERY_STATUS_CHARGING || extra_status == BatteryManager.BATTERY_STATUS_FULL) {
                chargingStatus = "Plugged";
            } else if (extra_status == BatteryManager.BATTERY_STATUS_DISCHARGING || extra_status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                chargingStatus = "unplugged";
            }
            Log.e(tag, "mBroadcastReceiver extra_status: " + extra_status);
            Log.e(tag, "mBroadcastReceiver chargingStatus: " + chargingStatus);
        }
    };*/



}
