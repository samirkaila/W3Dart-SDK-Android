package com.w3dartsdk;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.w3dart.utilitysdk.cpu.SystemUtils;
import com.w3dart.utilitysdk.device.DeviceUtility;
import com.w3dart.utilitysdk.screen.ScreenDisplayUtility;

public class MainActivity extends AppCompatActivity {

/* Screen Resolution
    Screen Resolution of the device? done

    Battery Percentage
    What is the battery percentage?

    Brightness
    What is the brightness value?

    Current App Screen
    On which screen, the user is on?

    Date & Time
    DateTime of the device? done

    Network: Mobile Data or Wifi
    On what network the user's device is running?

    Mobile Performance: CPU, Memory
    What is the CPU and Memory values?

    What else we can get?
    RAM and storage both.
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
*/


    static String tag = MainActivity.class.getSimpleName();
    TextView detail;

    FrameLayout mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.main_container);
        detail = findViewById(R.id.detail);
//        BatteryUtility.getBatteryStatus(getApplicationContext());



        ScreenDisplayUtility displayUtility = new ScreenDisplayUtility(getApplicationContext());
//        Log.e(tag, "displayUtility: Device Width: " + displayUtility.getDeviceWidth() + " Device Height: " + displayUtility.getDeviceHeight());
//        Log.e(tag, "displayUtility: Device Size inch: " + displayUtility.getDeviceSizeInch() + " Device Size cm: " + displayUtility.getDeviceSizeCM());
//        Log.e(tag, "displayUtility Device Density: " + displayUtility.getDeviceDensity());
//        Log.e(tag, "displayUtility Device Density Dpi: " + displayUtility.getDeviceDensityDpi());
//        Log.e(tag, "displayUtility density Text: " + displayUtility.getRefreshRate() + " Hz");
//        Log.e(tag, "displayUtility getDeviceType: " + displayUtility.getDeviceType(getApplicationContext()));
//        Log.e(tag, "displayUtility getOrientation: " + displayUtility.getOrientation(getApplicationContext()));


        DeviceUtility deviceUtility = new DeviceUtility(getApplicationContext());
        detail.setText(deviceUtility.getDetail());

        try {
            SystemUtils. getCpuDetail(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SystemUtils. getCPUFrequencyCurrent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getGPUInfo(getApplicationContext());
    }


    private GLSurfaceView glSurfaceView;
    private StringBuilder sb;

    private void getGPUInfo(Context context) {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        sb = new StringBuilder();
        sb.append("GL version:").append(configurationInfo.getGlEsVersion()).append("\n");

        Log.e(tag, "SB: " + sb);
        this.glSurfaceView = new GLSurfaceView(context);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                Log.e("GL", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
                Log.e("GL", "GL_RENDERER = " + gl.glGetString(GL10.GL_RENDERER));
                Log.e("GL", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
                Log.e("GL", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        glSurfaceView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        });

        mView.addView(glSurfaceView);
    }


}