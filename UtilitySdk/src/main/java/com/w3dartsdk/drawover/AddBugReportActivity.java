package com.w3dartsdk.drawover;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.w3dartsdk.BuildConfig;
import com.w3dartsdk.DartBug;
import com.w3dartsdk.DeviceDetail;
import com.w3dartsdk.R;
import com.w3dartsdk.databinding.ActivityAddBugReportBinding;
import com.w3dartsdk.model.DeviceData;
import com.w3dartsdk.utils.LinearLayoutSpaceItemDecoration;

import java.util.ArrayList;

public class AddBugReportActivity extends AppCompatActivity {

    ActivityAddBugReportBinding binding;
    private FloatingActionButton fab;
    long addBugOpenTime;

    DeviceParametersAdapter adapter;
    ArrayList<DeviceData> deviceDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBugReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addBugOpenTime = System.currentTimeMillis();

//        long difference = TimeUnit.MILLISECONDS.toSeconds(addBugOpenTime * 10 - DartBug.appStartTime * 100);
        long difference = (addBugOpenTime - DartBug.appStartTime) / 1000;
        long seconds = difference % 60;
        difference /= 60;
        long minutes = difference % 60;
        difference /= 60;
        long hours = difference % 24;
        difference /= 24;
        long days = difference;
        System.out.println(" DartBug.appStartTime: " + DartBug.appStartTime + " addBugOpenTime: " + addBugOpenTime);
        System.out.println(" Days: " + days + " Hours: " + hours + "Minutes: " + minutes + " Seconds: " + seconds + " Difference: " + difference);

//        Toast.makeText(getApplicationContext(), "Add task", Toast.LENGTH_SHORT).show();

//        toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
//        fab = (FloatingActionButton) findViewById(R.id.fab_activity_main);

//        initListeners();
//        setUiSettings();

        binding.tvSdkVersion.setText(getResources().getString(R.string.sdk_version, BuildConfig.VERSION_NAME));

        DeviceDetail deviceDetail = new DeviceDetail(AddBugReportActivity.this, binding.getRoot());

        deviceDataArrayList.add(new DeviceData("Device Name", deviceDetail.getDeviceManufacturer()));
        deviceDataArrayList.add(new DeviceData("Model No.", deviceDetail.getDeviceModelName()));


        deviceDataArrayList.add(new DeviceData("CPU", deviceDetail.getCpuName()));
        deviceDataArrayList.add(new DeviceData("CPU Instruction set (ABIs)", deviceDetail.getCpuInstructionSetAbis()));

        deviceDataArrayList.add(new DeviceData("System Version", deviceDetail.getDeviceAndroidOsVersion()));
        if (DartBug.appVersion.length() > 0)
            deviceDataArrayList.add(new DeviceData("App Version", "" + DartBug.appVersion));
//        deviceDataArrayList.add(new DeviceData("SDK Version", "" + deviceDetail.getDeviceSdkVersion()));
        deviceDataArrayList.add(new DeviceData("Device Type", deviceDetail.getDeviceScreenDeviceType()));


        deviceDataArrayList.add(new DeviceData("Battery Level", "" + deviceDetail.getBatteryPercentage() + "%"));
//        deviceDataArrayList.add(new DeviceData("Device Charging Status", "" + deviceDetail.getBatteryChargingStatus()));

        deviceDataArrayList.add(new DeviceData("Time", "" + deviceDetail.getDeviceDefaultDateTime()));
//        deviceDataArrayList.add(new DeviceData("Device Rooted?", "" + deviceDetail.getDeviceRooted()));

        deviceDataArrayList.add(new DeviceData("Screen Width", "" + deviceDetail.getDeviceScreenWidth()));
        deviceDataArrayList.add(new DeviceData("Screen Height", "" + deviceDetail.getDeviceScreenHeight()));

//        deviceDataArrayList.add(new DeviceData("Device Resolution", deviceDetail.getDeviceScreenWidth() + " x " + deviceDetail.getDeviceScreenHeight()));
//        deviceDataArrayList.add(new DeviceData("Screen Density", "" + deviceDetail.getDeviceScreenDensity()));
        deviceDataArrayList.add(new DeviceData("Brightness", "" + deviceDetail.getDeviceScreenBrightnessLevel()));


        adapter = new DeviceParametersAdapter(getApplicationContext(), deviceDataArrayList);
        binding.rcvParameters.setAdapter(adapter);
        LinearLayoutSpaceItemDecoration itemDecorationTrial = new LinearLayoutSpaceItemDecoration((int) getResources().getDimension(R.dimen._13sdp));
        binding.rcvParameters.addItemDecoration(itemDecorationTrial);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*deviceDataArrayList.add(new DeviceData("Device Name", "iPhone 13 Max Pro"));
                deviceDataArrayList.add(new DeviceData("Model No.", "iPhone 13-SME5264"));
                deviceDataArrayList.add(new DeviceData("iOS Version", "iOS 15.10"));
                deviceDataArrayList.add(new DeviceData("Network Connectivity", "Mobile Data Connected"));
                deviceDataArrayList.add(new DeviceData("Device Language", "English - en"));
                deviceDataArrayList.add(new DeviceData("Orientation", "Portrait"));
                deviceDataArrayList.add(new DeviceData("Device Resolution", "1280 x 720"));
                deviceDataArrayList.add(new DeviceData("Brightness", "32"));
                deviceDataArrayList.add(new DeviceData("Type", "iPhone"));*/

                if (deviceDetail.getCpuName() != null && deviceDetail.getCpuName().length() > 0) {
                    deviceDataArrayList.add(new DeviceData("GPU Version", deviceDetail.getGpuVersion()));
                    deviceDataArrayList.add(new DeviceData("GPU Render", deviceDetail.getGpuRenderer()));
                    deviceDataArrayList.add(new DeviceData("GPU Vendor", deviceDetail.getGpuVendor()));
                }

                for (int i = 0; i < deviceDataArrayList.size(); i++) {
                    if (deviceDataArrayList.get(i).getKey().equals("Network Connectivity")) {
                        Log.e("AddBugReportActivity", " Network found :"+deviceDetail.getNetworkAvailable());
                        if (deviceDetail.getNetworkAvailable()) {
                            deviceDataArrayList.get(i).setValue("Connected");
                        } else {
                            deviceDataArrayList.get(i).setValue("Disconnected");
                        }
                    }
                }


                adapter.notifyDataSetChanged();
            }
        }, 2000);

//        deviceDataArrayList.add(new DeviceData("Finger print supported", deviceDetail.getHardWareFingerprintSupported() ? "Yes" : "No"));
//        deviceDataArrayList.add(new DeviceData("Face scanning supported", deviceDetail.getHardWareFaceScanningSupported() ? "Yes" : "No"));
//        deviceDataArrayList.add(new DeviceData("Camera supported", deviceDetail.getHardWareCameraSupported() ? "Yes" : "No"));
//
//        deviceDataArrayList.add(new DeviceData("Total Memory", deviceDetail.getMemoryStorageTotalMemory()));
//        deviceDataArrayList.add(new DeviceData("Available Memory", deviceDetail.getMemoryStorageAvailableMemory()));
//        deviceDataArrayList.add(new DeviceData("Total Internal Memory", deviceDetail.getMemoryStorageInternalMemory()));

//        deviceDataArrayList.add(new DeviceData("Screen", deviceDetail.getAppScreenName()));
        binding.imgScreenShot.setImageURI(Uri.parse(deviceDetail.getScreenCapturedPath()));

        if (deviceDetail.getNetworkAvailable()) {
            deviceDataArrayList.add(new DeviceData("Network Connectivity", "Connected"));
//            deviceDataArrayList.add(new DeviceData("Network", deviceDetail.getNetworkWifiEnabled() ? "Wifi" : "Mobile Data"));
        } else {
            deviceDataArrayList.add(new DeviceData("Network Connectivity", "Disconnected"));
        }

        deviceDataArrayList.add(new DeviceData("Language", "" + deviceDetail.getDeviceDefaultLanguage()));
        deviceDataArrayList.add(new DeviceData("Country", "" + deviceDetail.getDeviceDefaultCountry()));
        deviceDataArrayList.add(new DeviceData("Orientation", deviceDetail.getDeviceScreenOrientation()));

        Log.e("AddBugReportActivity", "SDK Detail: " + deviceDetail.getDetail());

        binding.etDescription.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (binding.etDescription.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        // send user to the device settings
                        Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(myIntent);
                    } else {

                        Intent i = ShowHudService.getIntent(AddBugReportActivity.this);
                        startService(i);
//                        MainActivity.this.finish();
                    }
                } else {

                    Intent i = ShowHudService.getIntent(AddBugReportActivity.this);
                    startService(i);
//                    MainActivity.this.finish();
                }
            }
        });
    }

    private void setUiSettings() {
//        setSupportActionBar(toolbar);
    }
}
