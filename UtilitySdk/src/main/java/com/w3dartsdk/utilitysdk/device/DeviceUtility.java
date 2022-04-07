package com.w3dartsdk.utilitysdk.device;

import android.content.Context;
import android.os.Build;

import com.w3dartsdk.utilitysdk.SDKConstants;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
    This is the class for getting device related information from the device
 */
public class DeviceUtility {

    static String tag = DeviceUtility.class.getSimpleName();
    //    static String brand;
    static String manufacturer;
    static String deviceModelName;
    static String androidOsVersion;
    static int sdkVersion;

    static String defaultLanguage = "en";
    static String defaultCountry = "US";
    static String defaultDateTime = "";
    static boolean isDeviceRooted = false;

    public DeviceUtility(Context context) {

//        brand = Build.BRAND;
        manufacturer = Build.MANUFACTURER;
        deviceModelName = Build.MODEL;
        androidOsVersion = Build.VERSION.RELEASE;
        sdkVersion = Build.VERSION.SDK_INT;

        /*System.out.println("----------------------------------Device Utility Start-------------------------------------------------");

        Log.e(tag, "Device MANUFACTURER: " + Build.MANUFACTURER);
//        Log.e(tag, "Device BRAND: " + Build.BRAND);
//        Log.e(tag, "Device PRODUCT: " + Build.PRODUCT);
//        Log.e(tag, "Device DEVICE: " + Build.DEVICE);
        Log.e(tag, "Device MODEL: " + Build.MODEL);

//        Log.e(tag, "Device VERSION.SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH);
        Log.e(tag, "Device VERSION.RELEASE: " + Build.VERSION.RELEASE);
        Log.e(tag, "Device VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
//        Log.e(tag, "Device VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL);
        Log.e(tag, "Device VERSION.INCREMENTAL: " + Build.FINGERPRINT);
        Log.e(tag, "Device VERSION.INCREMENTAL: " + Build.FINGERPRINT);

        Log.e(tag, "Default Country: " + getDefaultCountry());
        Log.e(tag, "Default Language: " + getDefaultLanguage());
        Log.e(tag, "Current DateTime: " + getDefaultDateTime());
        isDeviceRooted = executeShellCommand();
        Log.e(tag, "isDevice Rooted: " + isDeviceRooted);
        System.out.println("----------------------------------Device Utility END-------------------------------------------------");
*/
    }

//    public static String getBrand() {
//        return brand = Build.BRAND;
//    }

    public static String getManufacturer() {
        return manufacturer = Build.MANUFACTURER;
    }

    public static String getDeviceModelName() {
        return deviceModelName = Build.MODEL;
    }

    public static String getAndroidOsVersion() {
        return androidOsVersion = Build.VERSION.RELEASE;
    }

    public static int getSdkVersion() {
        return sdkVersion = Build.VERSION.SDK_INT;
    }


    public static String getDefaultLanguage() {
        return defaultLanguage = Locale.getDefault().getLanguage();
    }

    public static String getDefaultCountry() {
//        Log.e(tag, "Default getDisplayCountry: " + Locale.getDefault().getDisplayCountry());
//        Log.e(tag, "Default getDisplayName: " + Locale.getDefault().getDisplayName());
        return defaultCountry = Locale.getDefault().getCountry();
    }

    public static String getDefaultDateTime() {
//        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
//        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
//        Log.e(tag, "Default getDateTimeFromCalender pattern: " + pattern);

//        Log.e(tag, "Default getDateTimeFromCalender Locale getDefault: " + Locale.getDefault());

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        String localDateTimePattern = ((SimpleDateFormat) format).toLocalizedPattern();
//        Log.e(tag, "Default getDateTimeFromCalender localDateTimePattern: " + localDateTimePattern);
//        Log.e(tag, "Default getDateTimeFromCalender: " + format.format(Calendar.getInstance(Locale.getDefault())));

        try {
            SimpleDateFormat sdfLocale = new SimpleDateFormat(localDateTimePattern);
            defaultDateTime = sdfLocale.format(Calendar.getInstance().getTime());
//            Log.e(tag, "Default defaultDateTime: " + defaultDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultDateTime;
    }

    public static String getDetail() {
        return "DeviceDetails { \"" + SDKConstants.KEY_DEVICE_MANUFACTURE_NAME + "\" : \"" + getManufacturer() + "\"" +
                /*" , " + " \"brand_name\" : \"" + getBrand() + "\"" +*/
                " , " + " \"" + SDKConstants.KEY_DEVICE_MODEL + "\" : \"" + getDeviceModelName() + "\"" +
                " , " + " \" " + SDKConstants.KEY_DEVICE_OS + "\" : \" Android " + getAndroidOsVersion() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_OS_SDK_NUMBER + "\" : \"" + getSdkVersion() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_LANGUAGE + "\" : \"" + getDefaultLanguage() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_COUNTRY + "\" : \"" + getDefaultCountry() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_DATE_TIME + "\" : \"" + getDefaultDateTime() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_ROOTED + "\" : \"" + isDeviceRooted + "\"" +
                "}";
    }


    /*    public  void getDeviceInfo(Context context) {
        brand = Build.BRAND;
        manufacturer = Build.MANUFACTURER;
        deviceModelName = Build.MODEL;
        androidOsVersion = Build.VERSION.RELEASE;
        sdkVersion = Build.VERSION.SDK_INT;
        if (Build.SUPPORTED_ABIS != null) {
            abis = Build.SUPPORTED_ABIS;
        }
        Log.e(tag, "Device ID: " + Build.ID);
        Log.e(tag, "Device DISPLAY: " + Build.DISPLAY);
        Log.e(tag, "Device MANUFACTURER: " + Build.MANUFACTURER);
        Log.e(tag, "Device BRAND: " + Build.BRAND);
        Log.e(tag, "Device PRODUCT: " + Build.PRODUCT);
        Log.e(tag, "Device DEVICE: " + Build.DEVICE);
        Log.e(tag, "Device MODEL: " + Build.MODEL);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Log.e(tag, "Device SERIAL: " + Build.getSerial());
//        }else{
//            Log.e(tag, "Device SERIAL: " + Build.SERIAL);
//        }


        Log.e(tag, "Device VERSION.SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH);

        Log.e(tag, "Device VERSION.RELEASE: " + Build.VERSION.RELEASE);
        Log.e(tag, "Device VERSION.PREVIEW_SDK_INT: " + Build.VERSION.PREVIEW_SDK_INT);
        Log.e(tag, "Device VERSION.CODENAME: " + Build.VERSION.CODENAME);
        Log.e(tag, "Device VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
        Log.e(tag, "Device VERSION.BASE_OS: " + Build.VERSION.BASE_OS);
        Log.e(tag, "Device VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL);

        Log.e(tag, "Device FINGERPRINT: " + Build.FINGERPRINT);
        Log.e(tag, "Device USER: " + Build.USER);
        Log.e(tag, "Device HOST: " + Build.HOST);
        try {
            Log.e(tag, "Device getDeviceID: " + getDeviceID(context));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String android_name = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.NAME);
            Log.e(tag, "Device android_id: " + android_id);
            Log.e(tag, "Device android_name: " + android_name);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

//        Log.e(tag, "ANDROID_ID: " + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        Log.e(tag, "Default Country: " + getDefaultCountry());
        Log.e(tag, "Default Language: " + getDefaultLanguage());
        Log.e(tag, "Current DateTime: " + getDefaultDateTime());
        Log.e(tag, "isDevice Rooted: " + executeShellCommand());

    }*/


    public static boolean executeShellCommand() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
//            rootFinder.setText("It is rooted device");
            return true;
        } catch (Exception e) {
//            rootFinder.setText("It is not rooted device");
            return false;
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                }
            }
        }
    }


    public final boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {
                "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }



}
