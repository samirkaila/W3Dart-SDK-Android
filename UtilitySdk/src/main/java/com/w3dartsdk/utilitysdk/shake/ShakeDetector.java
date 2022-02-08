package com.w3dartsdk.utilitysdk.shake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeDetector implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
        Log.e("ShakeDetector", "onAccuracyChanged called");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//		Log.e("ShakeDetector", "onSensorChanged called");
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;
                Log.e("ShakeDetector", "onSensorChanged called Shake Count : " + mShakeCount);
                mListener.onShake(mShakeCount);
            }
        }
    }

//
//	public interface OnShakeListener {
//		public void onShake(int count);
//	}
//
//
//	private OnShakeListener mListener;
//	int count = 1;
//	private boolean init;
//	private Sensor mAccelerometer;
//	private SensorManager mSensorManager;
//	private float x1, x2, x3;
//	private static final float ERROR = (float) 7.0;
//
//    public void setOnShakeListener(OnShakeListener listener) {
//        this.mListener = listener;
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent e) {
//
////        Log.e("ShakeDetector", "onSensorChanged called");
//        //Get x,y and z values
//        float x, y, z;
//        x = e.values[0];
//        y = e.values[1];
//        z = e.values[2];
//
//
//        if (!init) {
//            x1 = x;
//            x2 = y;
//            x3 = z;
//            init = true;
//        } else {
//
//            float diffX = Math.abs(x1 - x);
//            float diffY = Math.abs(x2 - y);
//            float diffZ = Math.abs(x3 - z);
//
//            //Handling ACCELEROMETER Noise
//            if (diffX < ERROR) {
//
//                diffX = (float) 0.0;
//            }
//            if (diffY < ERROR) {
//                diffY = (float) 0.0;
//            }
//            if (diffZ < ERROR) {
//
//                diffZ = (float) 0.0;
//            }
//
//
//            x1 = x;
//            x2 = y;
//            x3 = z;
//
//
//            //Horizontal Shake Detected!
//            if (diffX > diffY) {
//
//                Log.e("ShakeDetector", "onSensorChanged called Shake Count : " + count);
////				counter.setText("Shake Count : "+ count);
//                count = count + 1;
////				Toast.makeText(MainActivity.this, "Shake Detected!", Toast.LENGTH_SHORT).show();
//				mListener.onShake(count);
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }


}