
package com.w3dart.utilitysdk.hardware;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.fingerprint.FingerprintManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class HardwareUtility {

    static String tag = HardwareUtility.class.getSimpleName();
    private GLSurfaceView glSurfaceView;
    private StringBuilder sb;

    private FingerprintManager fingerprintManager = null;

    static boolean is_fingerprint_supported;
    static boolean has_fingerprints_enrolled;
    static boolean is_camera_supported = true;
    static boolean is_face_scanning_supported;

    CameraManager mCameraManager;

    public HardwareUtility(Context activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try {
                fingerprintManager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);
                is_fingerprint_supported = fingerprintManager.isHardwareDetected();
                has_fingerprints_enrolled = fingerprintManager.hasEnrolledFingerprints();
            } catch (Exception e) {
                e.printStackTrace();
            }

            PackageManager packageManager = activity.getPackageManager();

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    is_camera_supported = true;
//                    Log.e("camera", "This device has camera!");
                } else {
                    is_camera_supported = false;
//                    Log.e("camera", "This device has no camera!");
                }
            /*mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            Log.e(tag, "Hardware  FRONT: " + findCameraId(Facing.FRONT));
            Log.e(tag, "Hardware  BACK: " + findCameraId(Facing.BACK));*/

            } else {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 50);
            }

        /*BiometricManager biometricManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            biometricManager = (BiometricManager) activity.getSystemService(Context.BIOMETRIC_SERVICE);
             boolean has_face_biometric_supported;
            if (BiometricManager.BIOMETRIC_SUCCESS == biometricManager.canAuthenticate() || BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED == biometricManager.canAuthenticate()) {
                has_face_biometric_supported = true;
            }

            Log.e(tag, "Hardware  biometricManager.canAuthenticate(): " + biometricManager.canAuthenticate());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            is_face_scanning_supported = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE);
        }
        Log.e(tag, "Hardware  is_face_scanning_supported: " + is_face_scanning_supported);
        */

            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
                is_face_scanning_supported = true;
//                Log.e("camera", "This device has face auth!");
            } else {
                is_face_scanning_supported = false;
//                Log.e("camera", "This device has no face auth!");
            }

        } else {
            is_fingerprint_supported = false;
            has_fingerprints_enrolled = false;
            is_camera_supported = true;
            is_face_scanning_supported = false;
        }

/*        System.out.println("----------------------------------Hardware Utility Start-------------------------------------------------");
        Log.e(tag, "Hardware  is_fingerprint_supported: " + is_fingerprint_supported);
        Log.e(tag, "Hardware  has_fingerprints_enrolled: " + has_fingerprints_enrolled);
        Log.e(tag, "Hardware  is_camera_supported: " + is_camera_supported);
        Log.e(tag, "Hardware  is_face_scanning_supported: " + is_face_scanning_supported);
        System.out.println("----------------------------------Hardware Utility END-------------------------------------------------");
        */

    }

    /**
     * Which way the camera is facing.
     */
    public static enum Facing {
        FRONT, BACK;
    }

    /**
     * Returns the ID of the first camera facing the given direction.
     */
    private String findCameraId(Facing facing) {
        if (facing == Facing.FRONT) {
            return findFirstFrontCameraId();
        } else {
            return findFirstBackCameraId();
        }
    }

    /**
     * Returns the ID of the first back-facing camera.
     */
    private String findFirstBackCameraId() {
        Log.e(tag, "Getting First BACK Camera");
        String cameraId = findFirstCameraIdFacing(CameraCharacteristics.LENS_FACING_BACK);
        if (cameraId == null) {
            Log.e(tag, "No back-facing camera found.");
        }
        return cameraId;
    }

    /**
     * Returns the ID of the first front-facing camera.
     */
    private String findFirstFrontCameraId() {
        Log.e(tag, "Getting First FRONT Camera");
        String cameraId = findFirstCameraIdFacing(CameraCharacteristics.LENS_FACING_FRONT);
        if (cameraId == null) {
            Log.e(tag, "No front-facing camera found.");
        }
        return cameraId;
    }


    /**
     * Returns the ID of the first camera facing the given direction.
     */
    private String findFirstCameraIdFacing(int facing) {
        try {
            String[] cameraIds = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIds) {
                CameraCharacteristics characteristics = mCameraManager
                        .getCameraCharacteristics(cameraId);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException ex) {
            Log.e(tag, "Unable to get camera ID", ex);
        }
        return null;
    }


    public static synchronized String getDetail() {
        return "HardwareDetails { \"is_fingerprint_supported\" : \"" + is_fingerprint_supported + "\"" +
                " , " + " \"has_fingerprints_enrolled\" : \"" + has_fingerprints_enrolled + "\"" +
                " , " + " \"is_camera_supported\" : \"" + is_camera_supported + "\"" +
                " , " + " \"is_face_scanning_supported\" : \"" + is_face_scanning_supported + "\"" +
                "}";
    }


}