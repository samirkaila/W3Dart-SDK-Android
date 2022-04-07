package com.w3dartsdk.utilitysdk.screen;

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

import com.w3dartsdk.utilitysdk.SDKConstants;

/*
    This is the class for getting device's screen related information from the device
 */
public class ScreenDisplayUtility {

    static String tag = ScreenDisplayUtility.class.getSimpleName();
    static final float CM_PER_INCH = 2.54f;

    int deviceWidth = 0;
    int deviceHeight = 0;
    //    double deviceInch = 0;
//    double deviceCM = 0;
    double deviceDensity = 0;
    //    double deviceDensityDpi = 0;
//    double refreshRate = 0;
    String deviceType = "Phone";
    String orientation = "Portrait";
    int brightness = -1;

    public ScreenDisplayUtility(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
//        refreshRate = display.getRefreshRate();
        deviceDensity = displayMetrics.density;
//        deviceDensityDpi = displayMetrics.densityDpi;

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
//        deviceInch = diagonalInches;
//        deviceCM = diagonalInches * CM_PER_INCH;
        deviceType = getDeviceType(context);
        orientation = getOrientation(context);
        brightness(context);

       /* System.out.println("----------------------------------Screen Display Start-------------------------------------------------");
        Log.e(tag, "Display Detail: Device xdpi: " + displayMetrics.xdpi + " ydpi: " + displayMetrics.ydpi);
        Log.e(tag, "Display Detail: Device getRefreshRate: " + display.getRefreshRate());
        Log.e(tag, "Display Detail: Device Orientation: " + display.getRotation());

        Log.e(tag, "Display Detail: Device Width Pixels: " + deviceWidth + " Device Height Pixels: " + deviceHeight);
//        Log.e(tag, "Display Detail: Device Width: " + (deviceWidth / deviceDensity) + " Device Height: " + (deviceHeight / deviceDensity));
//        Log.e(tag, "Display Detail: Device Inch: " + deviceInch + " Device CM: " + deviceCM);
        Log.e(tag, "Display Density: " + deviceDensity);
//        Log.e(tag, "Display densityDpi: " + deviceDensityDpi);
//        Log.e(tag, "Display density Text: " + getDeviceDensityScreenText(deviceDensity));
        Log.e(tag, "Display deviceType: " + deviceType);
        Log.e(tag, "Display orientation: " + orientation);
        double ratio = deviceHeight / deviceWidth;
        Log.e(tag, "Display Aspect Ratio: " + ratio);
        Log.e(tag, "Display AndroidDeviceType: " + getAndroidDeviceType(context));
        Log.e(tag, "Display brightness: " + brightness);
        System.out.println("----------------------------------Screen Display END-------------------------------------------------");
*/
    }

    Size getDeviceScreenSizePixels() {
        return new Size(deviceWidth, deviceHeight);
    }

//    Size getDeviceScreenSize() {
//        return new Size((int) (deviceWidth / deviceDensity), (int) (deviceHeight / deviceDensity));
//    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    //    public double getDeviceSizeInch() {
//        return deviceInch;
//    }
//
//    public double getDeviceSizeCM() {
//        return deviceCM;
//    }
//
    public double getDeviceDensity() {
        return deviceDensity;
    }
//
//    public double getDeviceDensityDpi() {
//        return deviceDensityDpi;
//    }
//
//    public double getRefreshRate() {
//        return refreshRate;
//    }

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



        if (density == 0.75) {
            return " (ldpi)";
        } else if (density == 1.0) {
            return " (mdpi)";
        } else if (density == 1.5) {
            return " (hdpi)";
        } else if (density == 2.0) {
            return " (xhdpi)";
        } else if (density == 3.0) {
            return " (xxhdpi)";
        } else if (density == 4.0) {
            return " (xxxhdpi)";
        } else {
            return "";
        }
    }

    public final String getDeviceType(Context context) {

        int WATCH = 0;
        int PHONE = 1;
        int PHABLET = 2;
        int TABLET = 3;
        int TV = 4;

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
        if (diagonalInches > 14) {
            return "Tv";
        } else if (diagonalInches <= 10.1 && diagonalInches > 7) {
            return "Tablet";
        } else if (diagonalInches <= 7 && diagonalInches > 6.5) {
            return "Phablet";
        } else if (diagonalInches <= 6.5 && diagonalInches >= 2.5) {
            return "Phone";
        } else {
            return "Watch";
        }
    }

    public final String getAndroidDeviceType(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Log.e(tag, "TelephonyManager.PHONE_TYPE_NONE: " + manager.getPhoneType());

        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return "Tablet";
        } else {
            return "Mobile";
        }
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

    private void brightness(Context context) {

//        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        try {
            brightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//            System.out.println("Current Brightness level curBrightnessValue: " + brightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (brightness == -1) {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(context)) {
                        //Enable write permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        // Get system brightness
                        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); // enable auto brightness
                        brightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);  // in the range [0, 255]
                    }
                } else {
                    // Get system brightness
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); // enable auto brightness
                    brightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);  // in the range [0, 255]
                }

//                System.out.println("Current Brightness level " + brightness);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public String getDetail() {

        return "Display { \"" + SDKConstants.KEY_SCREEN_RESOLUTION + "\" : \"" + getDeviceScreenSizePixels() + " [width x height]" + "\"" +
                /*" , " + " \"screen_size_inch\" : \"" + getDeviceSizeInch() + "\"" +
                " , " + " \"screen_size_cm\" : \"" + getDeviceSizeCM() + "\"" +*/
                " , " + " \"" + SDKConstants.KEY_SCREEN_DENSITY + "\" : \"" + getDeviceDensity() + "\"" +
               /* " , " + " \"density_dpi\" : \"" + getDeviceDensityDpi() + getDeviceDensityScreenText(getDeviceDensity()) + "\"" +
                " , " + " \"refresh_rate\" : \"" + getRefreshRate() + " Hz\"" +*/
                " , " + " \"" + SDKConstants.KEY_SCREEN_DEVICE_TYPE + "\" : \"" + deviceType + "\"" +
                " , " + " \"" + SDKConstants.KEY_SCREEN_ORIENTATION + "\" : \"" + orientation + "\"" +
                " , " + " \"" + SDKConstants.KEY_SCREEN_BRIGHTNESS_LEVEL + "\" : \"" + brightness + "\"" +
                "}";
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
