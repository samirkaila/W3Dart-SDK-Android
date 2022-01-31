package com.w3dartsdk.utilitysdk.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.w3dartsdk.utilitysdk.SDKConstants;

public class BatteryUtility {

    private static String tag = BatteryUtility.class.getSimpleName();
    private static int percentage = 0;
    private static int status;
    private static String chargingStatus = "";

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
                chargingStatus = SDKConstants.VALUE_BATTERY_PLUGGED;
            } else if (extra_status == BatteryManager.BATTERY_STATUS_DISCHARGING || extra_status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                chargingStatus = SDKConstants.VALUE_BATTERY_UNPLUGGED;
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

    public static int getPercentage() {
        return percentage;
    }

    public static String getChargingStatus() {
        return chargingStatus;
    }

    public static synchronized String getDetail() {
        return "Battery { \"" + SDKConstants.KEY_BATTERY_PERCENTAGE + "\" : \"" + percentage + "\"" +
                " , " + " \"" + SDKConstants.KEY_BATTERY_CHARGING_STATUS + "\" : \"" + chargingStatus + "\"" +
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
