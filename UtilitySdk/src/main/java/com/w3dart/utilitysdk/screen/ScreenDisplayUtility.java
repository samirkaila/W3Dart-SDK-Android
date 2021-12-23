package com.w3dart.utilitysdk.screen;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

public class ScreenDisplayUtility {

    static String tag = ScreenDisplayUtility.class.getSimpleName();
    static final float CM_PER_INCH = 2.54f;

    int WATCH = 0;
    int PHONE = 1;
    int PHABLET = 2;
    int TABLET = 3;
    int TV = 4;


    int deviceWidth = 0;
    int deviceHeight = 0;
    double deviceInch = 0;
    double deviceCM = 0;
    double deviceDensity = 0;
    double deviceDensityDpi = 0;
    double refreshRate = 0;
    String deviceType = "Phone";

    public ScreenDisplayUtility(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        refreshRate = display.getRefreshRate();
        deviceDensity = displayMetrics.density;
        deviceDensityDpi = displayMetrics.densityDpi;

        // includes window decorations (statusbar bar/menu bar)
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            widthPixels = realSize.x;
            heightPixels = realSize.y;
//            Log.e(tag, "Display realSize.x: " + realSize.x + " realSize.y: " + realSize.y);

            deviceWidth = widthPixels;
            deviceHeight = heightPixels;

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        double diagonalInches = Math.sqrt(Math.pow(widthPixels / displayMetrics.xdpi, 2) + Math.pow(heightPixels / displayMetrics.ydpi, 2));
//        Log.e(tag, "diagonalInches: " + diagonalInches);
//        TextView diameter = findViewById(R.id.diagonal);
//        diameter.setText(String.format(Locale.getDefault(), "~ %.1f\"  (%.1f cm)", diagonalInches, diagonalInches * CM_PER_INCH));

//        deviceInch = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", diagonalInches));
//        deviceCM = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", deviceInch * CM_PER_INCH));
        deviceInch = diagonalInches;
        deviceCM = diagonalInches * CM_PER_INCH;

        System.out.println("----------------------------------Screen Display Start-------------------------------------------------");
        Log.e(tag, "Display Detail: Device xdpi: " + displayMetrics.xdpi + " ydpi: " + displayMetrics.ydpi);
        Log.e(tag, "Display Detail: Device getRefreshRate: " + display.getRefreshRate());
        Log.e(tag, "Display Detail: Device Orientation: " + display.getRotation());

        Log.e(tag, "Display Detail: Device Width Pixels: " + deviceWidth + " Device Height Pixels: " + deviceHeight);
        Log.e(tag, "Display Detail: Device Width: " + (deviceWidth / deviceDensity) + " Device Height: " + (deviceHeight / deviceDensity));
        Log.e(tag, "Display Detail: Device Inch: " + deviceInch + " Device CM: " + deviceCM);
        Log.e(tag, "Display Density: " + deviceDensity);
        Log.e(tag, "Display densityDpi: " + deviceDensityDpi);
        Log.e(tag, "Display density Text: " + getDeviceDensityScreenText(deviceDensity));
        Log.e(tag, "Display getDeviceType: " + getDeviceType(context));
        Log.e(tag, "Display getOrientation: " + getOrientation(context));
        System.out.println("----------------------------------Screen Display END-------------------------------------------------");

    }

    String getDeviceDensityScreenText(double density) {

      /*  int densityDpi = getResources().getDisplayMetrics().densityDpi;

        switch (densityDpi)
        {
            case DisplayMetrics.DENSITY_LOW:
                // LDPI
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                // MDPI
                break;

            case DisplayMetrics.DENSITY_TV:
            case DisplayMetrics.DENSITY_HIGH:
                // HDPI
                break;

            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_280:
                // XHDPI
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_360:
            case DisplayMetrics.DENSITY_400:
            case DisplayMetrics.DENSITY_420:
                // XXHDPI
                break;

            case DisplayMetrics.DENSITY_XXXHIGH:
            case DisplayMetrics.DENSITY_560:
                // XXXHDPI
                break;
        }*/

         /* 0.75 - ldpi

        1.0 - mdpi

        1.5 - hdpi

        2.0 - xhdpi

        3.0 - xxhdpi

        4.0 - xxxhdpi*/

        String screen = "unknown";

        if (density == 0.75) {
            screen = "ldpi";
        } else if (density == 1.0) {
            screen = "mdpi";
        } else if (density == 1.5) {
            screen = "hdpi";
        } else if (density == 2.0) {
            screen = "xhdpi";
        } else if (density == 3.0) {
            screen = "xxhdpi";
        } else if (density == 4.0) {
            screen = "xxxhdpi";
        }
        return screen;
    }

    public final String getDeviceType(Context context) {


        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(metrics);

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
        float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;

        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            yInches = realSize.y / displayMetrics.ydpi;
            xInches = realSize.x / displayMetrics.xdpi;
//            Log.e(tag, "getDeviceType yInches:" + yInches + " xInches:" + xInches);

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
//        Log.e(tag, "getDeviceType diagonalInches:" + diagonalInches);
//        double diagonalInches = Math.sqrt(Math.pow(widthPixels / displayMetrics.xdpi, 2) + Math.pow(heightPixels / displayMetrics.ydpi, 2));

//        if (diagonalInches > 10.1) {
        if (diagonalInches > 13) {
            deviceType = "Tv";
        } else if (diagonalInches <= 10.1 && diagonalInches > 7) {
            deviceType = "Tablet";
        } else if (diagonalInches <= 7 && diagonalInches > 6.5) {
            deviceType = "Phablet";
        } else if (diagonalInches <= 6.5 && diagonalInches >= 2) {
            deviceType = "Phone";
        } else {
            deviceType = "Watch";
        }
        return deviceType;
    }

    public final String getOrientation(final Context context) {
        switch (context.getResources().getConfiguration().orientation) {
            case ORIENTATION_PORTRAIT:
                return "Portrait";
            case ORIENTATION_LANDSCAPE:
                return "Landscape";
            default:
                return "Unknown";
        }
    }


    public String getDetail() {
        return "Display {density=" + deviceDensity + " (" + getDeviceDensityScreenText(deviceDensity) + ") Refresh rate= " + refreshRate + ", width=" + deviceWidth +
                ", height=" + deviceHeight + ", deviceSize(inch)=" + deviceInch + ", deviceSize(cm)=" + deviceCM + "}";
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }




    /*public  void setRealDeviceSizeInPixels(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        deviceDensity = displayMetrics.density;
        deviceDensityDpi = displayMetrics.densityDpi;


//        Log.e(tag, "widthPixels: " + widthPixels + " heightPixels: " + heightPixels);
//        Log.e(tag, "Display Name: " + display.getName());
//        Log.e(tag, "Display scaledDensity: " + displayMetrics.scaledDensity);
//        Log.e(tag, "Display xdpi: " + displayMetrics.xdpi);
//        Log.e(tag, "Display ydpi: " + displayMetrics.ydpi);

        // includes window decorations (statusbar bar/menu bar)
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            widthPixels = realSize.x;
            heightPixels = realSize.y;

            deviceWidth = widthPixels;
            deviceHeight = heightPixels;

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        double diagonalInches = Math.sqrt(Math.pow(widthPixels / displayMetrics.xdpi, 2) + Math.pow(heightPixels / displayMetrics.ydpi, 2));
//        Log.e(tag, "diagonalInches: " + diagonalInches);
//        TextView diameter = findViewById(R.id.diagonal);
//        diameter.setText(String.format(Locale.getDefault(), "~ %.1f\"  (%.1fcm)", diagonalInches, diagonalInches * CM_PER_INCH));

        deviceInch = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", diagonalInches));
        deviceCM = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", deviceInch * CM_PER_INCH));


        Log.e(tag, "Display Detail: Device Width: " + deviceWidth + " Device Height: " + deviceHeight);
        Log.e(tag, "Display Detail: Device Inch: " + deviceInch + " Device CM: " + deviceCM);
        Log.e(tag, "Display Density: " + deviceDensity);
        Log.e(tag, "Display densityDpi: " + deviceDensityDpi);
        Log.e(tag, "Display density Text: " + getDeviceDensityScreenText(deviceDensity));
        Log.e(tag, "Display density Text: " + DisplayMetrics.DENSITY_LOW);

//        Log.e(tag, "Display Detail: widthPixels: " + widthPixels + " heightPixels: " + heightPixels);
//        Log.e(tag, "Display Detail: " + String.format(Locale.getDefault(), "~ %.2f\"  (%.2f cm)", diagonalInches, diagonalInches * CM_PER_INCH));
    }*/


    void getDeviceUtility(Context context) {

//        Log.e(tag, "Device FINGERPRINT: " + Build.FINGERPRINT);
//        Log.e(tag, "Device USER: " + Build.USER);
//        Log.e(tag, "Device HOST: " + Build.HOST);
        try {
            Log.e(tag, "Device Android Device ID: " + getDeviceID(context));
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
    }

    public static String getDeviceID(Context p_context) throws Throwable {
        // needed phone state permission
        String m_deviceID = null;
        TelephonyManager m_telephonyManager = null;
        m_telephonyManager = (TelephonyManager) p_context.getSystemService(Context.TELEPHONY_SERVICE);

        m_deviceID = m_telephonyManager.getDeviceId().toString();

        if (m_deviceID == null || "00000000000000".equalsIgnoreCase(m_deviceID)) {
            m_deviceID = "AAAAAAA";
        }

        return m_deviceID;
    }

}
