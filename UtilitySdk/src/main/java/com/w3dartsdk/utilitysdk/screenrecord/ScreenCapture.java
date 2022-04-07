package com.w3dartsdk.utilitysdk.screenrecord;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.w3dartsdk.R;
import com.w3dartsdk.utilitysdk.SDKConstants;
import com.w3dartsdk.utilitysdk.screen.ScreenDisplayUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    This is the class for storing the screen shot activity to take screen shot of the screen
 */
public class ScreenCapture {

    private static final String tag = ScreenCapture.class.getSimpleName();
    private static Activity activityScreenCapture = null;


    public static Activity getActivityScreenCapture() {
        return activityScreenCapture;
    }

    public static void setActivityScreenCapture(Activity activityScreenCapture) {
        ScreenCapture.activityScreenCapture = activityScreenCapture;
    }


}
