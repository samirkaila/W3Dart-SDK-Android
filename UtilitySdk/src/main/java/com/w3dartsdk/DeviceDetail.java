package com.w3dartsdk;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.w3dartsdk.utilitysdk.SDKConstants;
import com.w3dartsdk.utilitysdk.screen.ScreenDisplayUtility;
import com.w3dartsdk.utilitysdk.screenrecord.ScreenCapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DeviceDetail {

    static String tag = DeviceDetail.class.getSimpleName();

    //battery
    static int percentage = 0;
    static String chargingStatus = "";

    //cpu
    String[] abis;
    String unparsed_CPU_INFO;
    String cpuName;

    // GPU
    static String gpu_version;
    static String gpu_renderer;
    static String gpu_vendor;

    // device detail
    static String manufacturer;
    static String deviceModelName;
    static String androidOsVersion;
    static int sdkVersion;
    static String defaultLanguage = "en";
    static String defaultCountry = "US";
    static String defaultDateTime = "";
    static boolean isDeviceRooted = false;

    // screen
    static final float CM_PER_INCH = 2.54f;
    int deviceWidth = 0;
    int deviceHeight = 0;
    double deviceDensity = 0;
    String deviceType = "Android Phone";
    String orientation = "Portrait";
    int brightness = -1;

    //hardware
    private FingerprintManager fingerprintManager = null;
    static boolean is_fingerprint_supported;
    static boolean has_fingerprints_enrolled;
    static boolean is_face_scanning_supported;
    static boolean is_camera_supported = true;

    //memory storage
    static long totalMemory;
    static long availMemory;
    static long internalMemory;

    //network
    ConnectivityManager connectivityManager;
    static boolean isNetworkAvailable = false;
    static boolean isWifiEnable = false;
    static boolean isMobileNetworkAvailable = false;

    // Screen name and screenshot
    static String screenShotFilePath = "";
    static String appTaskScreenName = "";

    public DeviceDetail(Context context, View containerView) {

        // battery
        initializeBattery(context);
        //cpu
        initializeCpuGpu(context, containerView);

        //device detail
        initializeDeviceScreen(context);

        // hardware
        initializeHardware(context);

        //storage memory detail
        initializeMemoryStorage(context);

        //network detail
        initializeNetwork(context);

        // Screen name and screenshot
        initializeScreenShot(context);
    }

    // Battery detail
    private void initializeBattery(Context context) {
        // battery
        IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batFilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percentage = (int) ((level / (float) scale) * 100);

            int extra_status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (extra_status == BatteryManager.BATTERY_STATUS_CHARGING || extra_status == BatteryManager.BATTERY_STATUS_FULL) {
                chargingStatus = SDKConstants.VALUE_BATTERY_PLUGGED;
            } else if (extra_status == BatteryManager.BATTERY_STATUS_DISCHARGING || extra_status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                chargingStatus = SDKConstants.VALUE_BATTERY_UNPLUGGED;
            }
        }
    }

    public int getBatteryPercentage() {
        return percentage;
    }

    public String getBatteryChargingStatus() {
        return chargingStatus;
    }

    // Cpu Gpu detail
    private void initializeCpuGpu(Context context, View containerView) {
        byte[] byteArry = new byte[1024];
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            result = cmdexe.run(args, "/system/bin/");
//            Log.i("result", "result=" + result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        unparsed_CPU_INFO = result;
        cpuName = getCPUName();
        if (Build.SUPPORTED_ABIS != null) {
            abis = Build.SUPPORTED_ABIS;
        }

        //gpu
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
//         StringBuilder sb;
//        sb = new StringBuilder();
//        sb.append("GL version:").append(configurationInfo.getGlEsVersion()).append("\n");

//        Log.e(tag, "SB: " + sb);
        GLSurfaceView glSurfaceView = new GLSurfaceView(context);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                gpu_version = gl.glGetString(GL10.GL_VERSION);
                gpu_renderer = gl.glGetString(GL10.GL_RENDERER);
                gpu_vendor = gl.glGetString(GL10.GL_VENDOR);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        };

        glSurfaceView.setRenderer(renderer);
        if (containerView != null) {
            if (containerView instanceof LinearLayout) {
                ((LinearLayout) containerView).addView(glSurfaceView);
            } else if (containerView instanceof RelativeLayout) {
                ((RelativeLayout) containerView).addView(glSurfaceView);
            } else if (containerView instanceof FrameLayout) {
                ((FrameLayout) containerView).addView(glSurfaceView);
            } else if (containerView instanceof ConstraintLayout) {
                ((ConstraintLayout) containerView).addView(glSurfaceView);
            }
        }
    }

    class CMDExecute {

        public synchronized String run(String[] cmd, String workDirectory) throws IOException {
            String result = "";

            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                // set working directory
                if (workDirectory != null)
                    builder.directory(new File(workDirectory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
//                    System.out.println(new String(re));
                    result = result + new String(re);
                }
                in.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

    }

    private synchronized String getCPUName() {
        String CPUName = "";
        String[] lines = unparsed_CPU_INFO.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String temp = lines[i];
            if (lines[i].contains("Processor\t:")) {

                CPUName = lines[i].replace("Processor\t: ", "");
                break;
            }
        }
        return CPUName;
    }

    public String getCpuName() {
        return cpuName;
    }

    public String getCpuInstructionSetAbis() {
        return Arrays.toString(abis);
    }

    public String getGpuVersion() {
        return gpu_version;
    }

    public String getGpuRenderer() {
        return gpu_renderer;
    }

    public String getGpuVendor() {
        return gpu_vendor;
    }

    // device detail
    private void initializeDeviceScreen(Context context) {
        manufacturer = Build.MANUFACTURER;
        deviceModelName = Build.MODEL;
        androidOsVersion = Build.VERSION.RELEASE;
        sdkVersion = Build.VERSION.SDK_INT;
        isDeviceRooted = executeShellCommand();

        // screen detail
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
        deviceType = calculateScreenType(context);
        orientation = calculateOrientation(context);
        calculateBrightnessLevel(context);

    }

    private boolean executeShellCommand() {
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

    private String calculateScreenType(Context context) {

        int WATCH = 0;
        int PHONE = 1;
        int PHABLET = 2;
        int TABLET = 3;
        int TV = 4;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

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
            return "Android Tv";
        } else if (diagonalInches <= 10.1 && diagonalInches > 7) {
            return "Android Tablet";
        } else if (diagonalInches <= 7 && diagonalInches > 6.5) {
            return "Android Phablet";
        } else if (diagonalInches <= 6.5 && diagonalInches >= 2.5) {
            return "Android Phone";
        } else {
            return "Android Watch";
        }
    }

    private String calculateOrientation(final Context context) {
        switch (context.getResources().getConfiguration().orientation) {
            case ORIENTATION_PORTRAIT:
                return "Portrait";
            case ORIENTATION_LANDSCAPE:
                return "Landscape";
            default:
                return "Unknown";
        }
    }

    private void calculateBrightnessLevel(Context context) {

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


    public String getDeviceManufacturer() {
        return manufacturer = Build.MANUFACTURER;
    }

    public String getDeviceModelName() {
        return deviceModelName = Build.MODEL;
    }

    public String getDeviceAndroidOsVersion() {
        return androidOsVersion = Build.VERSION.RELEASE;
    }

    public int getDeviceSdkVersion() {
        return sdkVersion = Build.VERSION.SDK_INT;
    }

    public String getDeviceDefaultLanguage() {
        return defaultLanguage = Locale.getDefault().getLanguage();
    }

    public String getDeviceDefaultCountry() {
        return defaultCountry = Locale.getDefault().getCountry();
    }

    public String getDeviceDefaultDateTime() {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        String localDateTimePattern = ((SimpleDateFormat) format).toLocalizedPattern();

        try {
            SimpleDateFormat sdfLocale = new SimpleDateFormat(localDateTimePattern);
            defaultDateTime = sdfLocale.format(Calendar.getInstance().getTime());
//            Log.e(tag, "Default defaultDateTime: " + defaultDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultDateTime;
    }

    public boolean getDeviceRooted() {
        return isDeviceRooted;
    }

    // screen detail
    private Size getDeviceScreenSizePixels() {
        return new Size(deviceWidth, deviceHeight);
    }

    public int getDeviceScreenWidth() {
        return deviceWidth;
    }

    public int getDeviceScreenHeight() {
        return deviceHeight;
    }

    public double getDeviceScreenDensity() {
        return deviceDensity;
    }

    public String getDeviceScreenDeviceType() {
        return deviceType;
    }

    public String getDeviceScreenOrientation() {
        return orientation;
    }

    public int getDeviceScreenBrightnessLevel() {
        return brightness;
    }


    // hardware
    private void initializeHardware(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try {
                fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                is_fingerprint_supported = fingerprintManager.isHardwareDetected();
                has_fingerprints_enrolled = fingerprintManager.hasEnrolledFingerprints();
            } catch (Exception e) {
                e.printStackTrace();
            }

            PackageManager packageManager = context.getPackageManager();
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    is_camera_supported = true;
//                    Log.e("camera", "This device has camera!");
                } else {
                    is_camera_supported = false;
//                    Log.e("camera", "This device has no camera!");
                }
            } else {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 50);
            }

            //                Log.e("camera", "This device has face auth!");
            is_face_scanning_supported = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE);

        } else {
            is_fingerprint_supported = false;
            has_fingerprints_enrolled = false;
            is_camera_supported = true;
            is_face_scanning_supported = false;
        }

    }

    public boolean getHardWareFingerprintSupported() {
        return is_fingerprint_supported;
    }

    public boolean getHardWareFingerprintEnrolled() {
        return has_fingerprints_enrolled;
    }

    public boolean getHardWareFaceScanningSupported() {
        return is_face_scanning_supported;
    }

    public boolean getHardWareCameraSupported() {
        return is_camera_supported;
    }

    //storage memory detail
    private void initializeMemoryStorage(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager.RecentTaskInfo recentTaskInfo = new ActivityManager.RecentTaskInfo();
        actManager.getMemoryInfo(memInfo);
        totalMemory = memInfo.totalMem;
        availMemory = memInfo.availMem;
        internalMemory = getTotalInternalMemorySize();
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long BlockSize = stat.getBlockSizeLong();
        long TotalBlocks = stat.getBlockCountLong();
//        return bytesToHuman(TotalBlocks * BlockSize);
        return (TotalBlocks * BlockSize);
    }

    private String bytesToHuman(long size) {
        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "0";
    }

    private String floatForm(double d) {
        return String.format(java.util.Locale.US, "%.2f", d);
    }

    public String getMemoryStorageTotalMemory() {
        return bytesToHuman(totalMemory);
    }

    public String getMemoryStorageAvailableMemory() {
        return bytesToHuman(availMemory);
    }

    public String getMemoryStorageInternalMemory() {
        return bytesToHuman(internalMemory);
    }

    //network detail
    private void initializeNetwork(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            Log.e(tag, "getExtraInfo: " + activeNetworkInfo);

            if (activeNetworkInfo != null) {

                isWifiEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
                isMobileNetworkAvailable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

                if (isWifiEnable || isMobileNetworkAvailable) {
                    isNetworkAvailable = true;
                    /*Sometime wifi is connected but service provider never connected to internet so cross check one more time*/
                    if (isOnline())
                        isNetworkAvailable = true;
                }

                /*if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else
                        return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;*/
            }
        } else {
            isNetworkAvailable = false;
        }
    }

    private boolean isOnline() {
        /*Just to check Time delay*/
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        Runtime runtime = Runtime.getRuntime();
        try {
            /*Pinging to Google server*/
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long t2 = Calendar.getInstance().getTimeInMillis();
//            Log.e(tag, "NetWork check Time: " + (t2 - timeInMillis) + "");
        }
        return false;
    }

    public boolean getNetworkAvailable() {
        return isNetworkAvailable;
    }

    public boolean getNetworkWifiEnabled() {
        return isWifiEnable;
    }

    public boolean getNetworkMobileDataEnabled() {
        return isMobileNetworkAvailable;
    }


    // Screen name and screenshot
    private void initializeScreenShot(Context context) {
        Activity activity;
        if (ScreenCapture.getActivityScreenCapture() != null) {
            activity = ScreenCapture.getActivityScreenCapture();
            takeScreenShot(activity);
            appTaskScreenName = activity.getPackageName() + "." + activity.getLocalClassName();
        }

     /*   ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();

        for (ActivityManager.AppTask task : tasks) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appTaskScreenName = task.getTaskInfo().topActivity.getClassName();
//                Log.e(tag, "M > stackId: " + appTaskScreenName);
            } else {
                ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
                appTaskScreenName = cn.getClassName();
//                Log.e(tag, "M < getClassName: " + appTaskScreenName);
            }
        }*/
    }

    public String getAppScreenName() {
        return appTaskScreenName;
    }

    public String getScreenCapturedPath() {
        return screenShotFilePath;
    }

    private static void takeScreenShot(Activity activity) {

        try {
            File cacheDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), activity.getResources().getString(R.string.app_name));
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            screenShotFilePath = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), activity.getResources().getString(R.string.app_name)) + "/screenshot.jpg";

            Log.e(tag, "SDK Screen capture file path:" + screenShotFilePath + " Package Name:" + activity.getPackageName() + " Activity Name: " + activity.getLocalClassName());

        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        ScreenDisplayUtility displayUtility = new ScreenDisplayUtility(activity);
        int screenWidth = displayUtility.getDeviceWidth();
        int screenHeight = displayUtility.getDeviceHeight();

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int navigationHeight = (screenHeight - frame.bottom);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        Log.e(tag, "statusBarHeight: " + statusBarHeight + " navigationHeight:" + navigationHeight
                + " screenWidth: " + screenWidth + " screenHeight:" + screenHeight
                + " width: " + width + " height:" + height);

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height);
        view.destroyDrawingCache();
        savePic(b, screenShotFilePath);
    }

    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            SDKConstants.ENABLE_SCREEN_SHOT = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized String getDetail() {
        String data = "";
        data = "Battery { \"" + SDKConstants.KEY_BATTERY_PERCENTAGE + "\" : \"" + percentage + "\"" +
                " , " + " \"" + SDKConstants.KEY_BATTERY_CHARGING_STATUS + "\" : \"" + chargingStatus + "\"" +
                "}";

        data = data + "\n\nCPU { \"" + SDKConstants.KEY_CPU + "\" : \"" + getCpuName() + "\"" +
                " , " + " \"" + SDKConstants.KEY_CPU_INSTRUCTION_SET + "\" : \"" + getCpuInstructionSetAbis() + "\"" +
                "}";

        data = data + "\nGpuDetails { \"" + SDKConstants.KEY_GPU_VERSION + "\" : \"" + gpu_version + "\"" +
                " , " + " \"" + SDKConstants.KEY_GPU_RENDER + "\" : \"" + gpu_renderer + "\"" +
                " , " + " \"" + SDKConstants.KEY_GPU_VENDOR + "\" : \"" + gpu_vendor + "\"" +
                "}";

        data = data + "\n\nDeviceDetails { \"" + SDKConstants.KEY_DEVICE_MANUFACTURE_NAME + "\" : \"" + getDeviceManufacturer() + "\"" +
                /*" , " + " \"brand_name\" : \"" + getBrand() + "\"" +*/
                " , " + " \"" + SDKConstants.KEY_DEVICE_MODEL + "\" : \"" + getDeviceModelName() + "\"" +
                " , " + " \" " + SDKConstants.KEY_DEVICE_OS + "\" : \" Android " + getDeviceAndroidOsVersion() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_OS_SDK_NUMBER + "\" : \"" + getDeviceSdkVersion() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_LANGUAGE + "\" : \"" + getDeviceDefaultLanguage() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_COUNTRY + "\" : \"" + getDeviceDefaultCountry() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_DATE_TIME + "\" : \"" + getDeviceDefaultDateTime() + "\"" +
                " , " + " \"" + SDKConstants.KEY_DEVICE_ROOTED + "\" : \"" + isDeviceRooted + "\"" +
                "}";

        data = data + "\nDisplay { \"" + SDKConstants.KEY_SCREEN_RESOLUTION + "\" : \"" + getDeviceScreenSizePixels() + " [width x height]" + "\"" +
                /*" , " + " \"screen_size_inch\" : \"" + getDeviceSizeInch() + "\"" +
                " , " + " \"screen_size_cm\" : \"" + getDeviceSizeCM() + "\"" +*/
                " , " + " \"" + SDKConstants.KEY_SCREEN_DENSITY + "\" : \"" + getDeviceScreenDensity() + "\"" +
               /* " , " + " \"density_dpi\" : \"" + getDeviceDensityDpi() + getDeviceDensityScreenText(getDeviceDensity()) + "\"" +
                " , " + " \"refresh_rate\" : \"" + getRefreshRate() + " Hz\"" +*/
                " , " + " \"" + SDKConstants.KEY_SCREEN_DEVICE_TYPE + "\" : \"" + deviceType + "\"" +
                " , " + " \"" + SDKConstants.KEY_SCREEN_ORIENTATION + "\" : \"" + orientation + "\"" +
                " , " + " \"" + SDKConstants.KEY_SCREEN_BRIGHTNESS_LEVEL + "\" : \"" + brightness + "\"" +
                "}";


        data = data + "\n\nHardwareDetails { \"" + SDKConstants.KEY_HARDWARE_FINGER_PRINT_SUPPORTED + "\" : \"" + is_fingerprint_supported + "\"" +
                " , " + " \"" + SDKConstants.KEY_HARDWARE_FINGER_PRINT_ENROLLED + "\" : \"" + has_fingerprints_enrolled + "\"" +
                " , " + " \"" + SDKConstants.KEY_HARDWARE_FACE_SCANNING_SUPPORTED + "\" : \"" + is_face_scanning_supported + "\"" +
                " , " + " \"" + SDKConstants.KEY_HARDWARE_CAMERA_AVAILABLE + "\" : \"" + is_camera_supported + "\"" +
                "}";

        data = data + "\n\nMemory { \"" + SDKConstants.KEY_STORAGE_TOTAL_MEMORY + "\" : \"" + bytesToHuman(totalMemory) + "\"" +
                " , " + " \"" + SDKConstants.KEY_STORAGE_AVAILABLE_RAM + "\" : \"" + bytesToHuman(availMemory) + "\"" +
                " , " + " \"" + SDKConstants.KEY_STORAGE_INTERNAL_MEMORY + "\" : \"" + bytesToHuman(internalMemory) + "\"" +
                "}";


        data = data + "\n\nNetworkDetails { \"" + SDKConstants.KEY_NETWORK_AVAILABLE + "\" : \"" + isNetworkAvailable + "\"" +
                " , " + " \"" + SDKConstants.KEY_NETWORK_WIFI_ENABLED + "\" : \"" + isWifiEnable + "\"" +
                " , " + " \"" + SDKConstants.KEY_NETWORK_MOBILE_DATA_ENABLED + "\" : \"" + isMobileNetworkAvailable + "\"" +
                "}";

        data = data + "\n\nAppTask { \"" + SDKConstants.KEY_APP_TASK_SCREEN_NAME + "\" : \"" + appTaskScreenName + "\"" +
                "}";

        return data;
    }
}
