package com.sdkdemo;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sdkdemo.service.ForegroundService;
import com.w3dartsdk.DeviceDetail;
import com.w3dartsdk.utilitysdk.battery.BatteryUtility;
import com.w3dartsdk.utilitysdk.cpu.CpuUtility;
import com.w3dartsdk.utilitysdk.device.DeviceUtility;
import com.w3dartsdk.utilitysdk.gpu.GpuUtility;
import com.w3dartsdk.utilitysdk.hardware.HardwareUtility;
import com.w3dartsdk.utilitysdk.memory.MemoryUtility;
import com.w3dartsdk.utilitysdk.network.NetworkUtility;
import com.w3dartsdk.utilitysdk.screen.ScreenDisplayUtility;
import com.w3dartsdk.utilitysdk.task.AppTaskUtility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

/* Screen Resolution
    Screen Resolution of the device? done

    Battery Percentage
    What is the battery percentage?

    Brightness
    What is the brightness value? done

    Current App Screen
    On which screen, the user is on?

    Date & Time
    DateTime of the device? done

    Network: Mobile Data or Wifi
    On what network the user's device is running?

    Mobile Performance: CPU, Memory
    What is the CPU and Memory values? done

    What else we can get?
    RAM and storage both.

    CURRENT VIEW > mainscreen
    */

    // Json format out put format
/*

{
  "build_version": "Build Number",
  "manufacture_name": "Asus Taiwan",
  "device_model": "Asus Phone 1",
  "os": "Android 9",
  "supported_hardware": {
    "fingerprint": false,
    "camera": true,
    "gps": true
  },
  "battery": 56,
  "charging_mode": true,
  "Language": "English OR [English,Hindi]",
  "mobile_network": [
    "Mobile",
    "WIFI"
  ],
  "date_time": "WHATEVER STANDARD IS USED LIKE (MM/DD/YYYY HH:MM:SS)",
  "screen_resolution": " '1280X720' OR [1280,720]",
  "brightness": "'High' OR 24(Value)",
  "cpu": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "total_cpu": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "memory": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "total_memory": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "storage": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "total_storage": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "gpu": "WHATEVER VALUE IS RETURNED FROM SYSTEM",
  "device_orientation": "Portrait",
  "device_location": [
    "LAT",
    "LONG"
  ],
  "device_root_status": false,
  "sdk_number": "12334"
}

    We'll use structure something like this.

- [ ] Supported Hardware: Fingerprint, Camera
- [ ] Language
- [ ] Mobile Network
- [x] Brightness
- [x] Screen Resolution
- [x] DateTime
- [x] Battery Percentage
- [x] Battery Charging Mode or Not
- [x] Device OS
- [x] CPU & Memory
- [x] Mobile Device Model
- [x] Manufacture Name


Device MANUFACTURER: vivo
Device MODEL: vivo 1920
Device VERSION.RELEASE: 11
Device VERSION.SDK_INT: 30
Default Country: US
Default Language: en
Current DateTime: Dec 23, 2021 2:58:04 PM
isDevice Rooted: false

Device Width Pixels: 1080 Device Height Pixels: 2340
Density: 3.0
deviceType: Phone
orientation: Portrait
brightness: 37

GL_VERSION = OpenGL ES-CM 1.1
GL_RENDERER = Adreno (TM) 610
GL_VENDOR = Qualcomm
*/


    static String tag = MainActivity.class.getSimpleName();
    TextView detail;
    Button buttonSaveLog;

    FrameLayout mView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.main_container);
        detail = findViewById(R.id.detail);
        buttonSaveLog = findViewById(R.id.btnSaveLog);

        DeviceDetail deviceDetail = new DeviceDetail(MainActivity.this, mView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detail.setText(detail.getText() + " \n\n" + deviceDetail.getDetail());
            }
        }, 2000);

        showBasicDeviceDetail();



        buttonSaveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    writeLog();
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 50);
//                }

               /* if (checkPermission()) {
                    Log.e(tag, "check permission");
                    writeLog();
                } else {
                    Log.e(tag, "requestion permission");
                    requestPermission();
                }*/

//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
//                    Log.e(tag, "check self permission and write log");
//                        final File path = new File(Environment.getExternalStorageDirectory(), "DartSdk");

                        /*  final File path = new File(getFilesDir(), "DartSdk");
                        if (!path.exists()) {
                            path.mkdir();
                        }
                        Runtime.getRuntime().exec("logcat  -d -f " + path + File.separator + "dbo_logcat2" + ".txt");
                        Log.e(tag, "logcat path: " + path.getPath());*/

                        /*Log.e(tag, "logcat file: " + getFilesDir());
                        Log.e(tag, "logcat file: " + getCacheDir());
                        Log.e(tag, "logcat file: " + getExternalCacheDir());
                        Log.e(tag, "logcat file: " + getExternalMediaDirs());
                        Log.e(tag, "logcat file: " + getExternalFilesDir(Environment.DIRECTORY_DCIM));
*/
//                    String fileName = "logcat3.txt";
//                    File file = new File(getExternalCacheDir(), fileName);
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    } else {
//                        file.delete();
//                        file = new File(getExternalCacheDir(), fileName);
//                        file.createNewFile();
//                    }
//                    String command = "logcat -d -f " + file.getAbsolutePath();
//                    Runtime.getRuntime().exec(command);
//                    Log.e(tag, "logcat file: " + file.getPath());

                    writeLog();

                    /*
                    Clear log
                    try {
                        Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                } else {
//                    Log.e(tag, "ask request permission for log");
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 50);
//                }

            }
        });

//        AppController.getPrefManager().setAppOpenTime(Calendar.getInstance().getTimeInMillis());


//        int brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
//        Log.e(tag, " brightness: " + brightness);
//
//
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });


//        startService(new Intent(this, HUD.class));
//        checkOverlayPermission();
//        startService();

    }

    private void showBasicDeviceDetail() {
        DeviceUtility deviceUtility = new DeviceUtility(getApplicationContext());
        Log.e(tag, "deviceUtility Detail: " + deviceUtility.getDetail());
        detail.setText(deviceUtility.getDetail());

        ScreenDisplayUtility displayUtility = new ScreenDisplayUtility(getApplicationContext());
        Log.e(tag, "displayUtility Detail: " + displayUtility.getDetail());
        detail.setText(detail.getText() + " \n\n" + displayUtility.getDetail());


        CpuUtility cpuUtility = new CpuUtility(getApplicationContext());
        Log.e(tag, "gpuUtility Detail: " + cpuUtility.getDetail());
        detail.setText(detail.getText() + " \n\n" + cpuUtility.getDetail());

        /*try {
            SystemUtils.getCpuDetail(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SystemUtils.getCPUFrequencyCurrent();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        GpuUtility gpuUtility = new GpuUtility(MainActivity.this, mView);
        Log.e(tag, "gpuUtility Detail: " + gpuUtility.getDetail());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detail.setText(detail.getText() + " \n\n" + gpuUtility.getDetail());
            }
        }, 2000);

        try {
            HardwareUtility hardwareUtility = new HardwareUtility(MainActivity.this);
            Log.e(tag, "hardwareUtility Detail: " + hardwareUtility.getDetail());
            detail.setText(detail.getText() + " \n\n" + hardwareUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NetworkUtility networkUtility = new NetworkUtility(MainActivity.this);
            Log.e(tag, "networkUtility Detail: " + networkUtility.getDetail());
            detail.setText(detail.getText() + " \n\n" + networkUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MemoryUtility memoryUtility = new MemoryUtility(MainActivity.this);
            Log.e(tag, "memoryUtility Detail: " + memoryUtility.getDetail());
            detail.setText(detail.getText() + " \n\n" + memoryUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            BatteryUtility.getBatteryStatus(getApplicationContext());
            BatteryUtility batteryUtility = new BatteryUtility(MainActivity.this);
            Log.e(tag, "batteryUtility Detail: " + batteryUtility.getDetail());
            detail.setText(detail.getText() + " \n\n" + batteryUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            AppTaskUtility appTaskUtility = new AppTaskUtility(MainActivity.this);
            Log.e(tag, "appTaskUtility Detail: " + appTaskUtility.getDetail());
            detail.setText(detail.getText() + " \n\n" + appTaskUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // method for starting the service
    public void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if (Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, ForegroundService.class));
//                    startForegroundService(new Intent(this, ShowHudService.class));
                } else {
                    startService(new Intent(this, ForegroundService.class));
//                    startService(new Intent(this, ShowHudService.class));
                }
            }
        } else {
            startService(new Intent(this, ForegroundService.class));
//            startService(new Intent(this, ShowHudService.class));
        }
    }

    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

    // check for permission again when user grants it from
    // the device settings, and start the service
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startService();
//    }

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String processId = Integer.toString(android.os.Process.myPid());


    private void writeLog() {

//        File externalStorageDir = Environment.getExternalStorageDirectory();
//        File myFile = new File(externalStorageDir, "dart_log.txt");
        File dir;
//        dir = new File(Environment.getExternalStorageDirectory() + "/" + "DartSdk");
        dir = new File(getExternalCacheDir() + "/" + "DartSdkLog");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File myFile = new File(dir.getPath(), "dart_log.txt");
        if (myFile.exists()) {
            myFile.delete();
            try {

                myFile = new File(dir.getPath(), "dart_log.txt");
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (!myFile.exists()) {
//            try {
//                myFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Log.e(tag, "Log file path: " + myFile.getPath());
        Toast.makeText(getApplicationContext(), "Log file saved " + myFile.getPath(), Toast.LENGTH_SHORT).show();
        if (myFile.exists()) {
            try {

                StringBuilder logBuilder = new StringBuilder();
                FileOutputStream fostream = null;
                OutputStreamWriter oswriter = null;
                BufferedWriter bwriter = null;

                try {
//                    String cmd = "logcat -v time -r -f " + myFile.getPath();
//                    Process process = Runtime.getRuntime().exec(command);
                    Process process = Runtime.getRuntime().exec("logcat -d");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;

                    Log.e(tag, "-------------------------------Log line: " + bufferedReader.readLine());
                    while ((line = bufferedReader.readLine()) != null) {
//                        if (line.contains("com.w3dartsdk:") || line.contains("System:"))
//                        if (line.contains("com.w3dartsdk:") || line.contains("ViewRootImpl"))
                        if (line.contains("com.w3dartsdk:"))
                            continue;
                        logBuilder.append(line + "\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Log.e(tag, "Log detail: " + logBuilder);

                fostream = new FileOutputStream(myFile);
                oswriter = new OutputStreamWriter(fostream);
                bwriter = new BufferedWriter(oswriter);
                bwriter.flush();
                bwriter.write("--------------------------START--------------------------");
                bwriter.newLine();
                bwriter.write("Hi welcome ");
                bwriter.newLine();
                bwriter.write(logBuilder.toString());
                bwriter.newLine();
                bwriter.write("--------------------------END--------------------------");


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {

                Log.e(tag, "requestPermission >R with ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Log.e(tag, "requestPermission >R with ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION");
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(tag, "onActivityResult called");
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    writeLog();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(tag, "onRequestPermissionsResult called: permissions: " + permissions[0] + " grant: " + grantResults[0]);
        if (requestCode == 50) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                writeLog();
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        switch (requestCode) {
            case 999:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        // perform action when allow permission success
                        writeLog();
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

        }

    }

}