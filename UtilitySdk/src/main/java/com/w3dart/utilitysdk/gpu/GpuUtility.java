
package com.w3dart.utilitysdk.gpu;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GpuUtility {

    static String tag = GpuUtility.class.getSimpleName();
    private GLSurfaceView glSurfaceView;
    private StringBuilder sb;

    static String gpu_version;
    static String gpu_renderer;
    static String gpu_vendor;
//    static String gpu_extensions;

    public GpuUtility(Context activity, FrameLayout frameLayout) {

        final ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        sb = new StringBuilder();
        sb.append("GL version:").append(configurationInfo.getGlEsVersion()).append("\n");

//        Log.e(tag, "SB: " + sb);
        this.glSurfaceView = new GLSurfaceView(activity);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                gpu_version = gl.glGetString(GL10.GL_VERSION);
                gpu_renderer = gl.glGetString(GL10.GL_RENDERER);
                gpu_vendor = gl.glGetString(GL10.GL_VENDOR);
//                gpu_extensions = gl.glGetString(GL10.GL_EXTENSIONS);

//                Log.e(tag, "GL_VERSION = " + gl.glGetString(GLES32.GL_VERSION));
//                Log.e(tag, "GL_RENDERER = " + gl.glGetString(GLES32.GL_RENDERER));
//                Log.e(tag, "GL_VENDOR = " + gl.glGetString(GLES32.GL_VENDOR));
//                Log.e(tag, "GL_ACTIVE_PROGRAM = " + gl.glGetString(GLES32.GL_ACTIVE_PROGRAM));

//                Log.e(tag, "GL_VERSION = " + gpu_version);
//                Log.e(tag, "GL_RENDERER = " + gpu_renderer);
//                Log.e(tag, "GL_VENDOR = " + gpu_vendor);
//                Log.e(tag, "GL_EXTENSIONS = " + gpu_extensions);

//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        glSurfaceView.setVisibility(View.GONE);
//                    }
//                });
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        };

        this.glSurfaceView.setRenderer(renderer);
        if(frameLayout!=null)
        frameLayout.addView(glSurfaceView);

        /*
        System.out.println("----------------------------------GPU Utility Start-------------------------------------------------");
        Log.e(tag, "GL_VERSION = " + gpu_version);
        Log.e(tag, "GL_RENDERER = " + gpu_renderer);
        Log.e(tag, "GL_VENDOR = " + gpu_vendor);
//        Log.e(tag, "GL_EXTENSIONS = " + gpu_extensions);
        System.out.println("----------------------------------GPU Utility END-------------------------------------------------");
        */
    }

    public static synchronized String getDetail() {
        return "GpuDetails { \"gpu_version\" : \"" + gpu_version + "\"" +
                " , " + " \"gpu_renderer\" : \"" + gpu_renderer + "\"" +
                " , " + " \"gpu_vendor\" : \"" + gpu_vendor + "\"" +
                /*" , " + " \"gpu_extensions\" : \"" + gpu_extensions + "\"" +*/
                "}";
    }


}