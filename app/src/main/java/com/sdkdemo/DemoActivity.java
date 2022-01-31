package com.sdkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.w3dartsdk.utilitysdk.battery.BatteryUtility;

import java.util.Calendar;

public class DemoActivity extends AppCompatActivity /*implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener*/ {

    static String tag = DemoActivity.class.getSimpleName();
    long currentMilliSec;
//    GestureLibrary mLibrary;

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        try {
//            BatteryUtility.getBatteryStatus(getApplicationContext());
            BatteryUtility batteryUtility = new BatteryUtility(DemoActivity.this);
//            Log.e(tag, "batteryUtility Detail: " + batteryUtility.getDetail());
//            detail.setText(detail.getText() + " \n\n" + batteryUtility.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentMilliSec = Calendar.getInstance().getTimeInMillis();

//        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
//        if (!mLibrary.load()) {
//            finish();
//        }
//
//        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
//        gestures.addOnGesturePerformedListener(this);

       /* // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);*/

        layout = findViewById(R.id.relativeLayout);
        /*layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Toast.makeText(getApplicationContext(), "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Toast.makeText(getApplicationContext(), "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }

        });*/

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onClick() {
                super.onClick();
                Toast.makeText(getApplicationContext(), "gesture click", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeTop() {
                Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();

            }

            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), " getResources().getString(R.string.toastRight)", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "getResources().getString(R.string.toastLeft)", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "getResources().getString(R.string.toastBottom)", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
            String result = predictions.get(0).name;

            if ("open".equalsIgnoreCase(result)) {
                Toast.makeText(this, "Opening the document", Toast.LENGTH_LONG).show();
            } else if ("save".equalsIgnoreCase(result)) {
                Toast.makeText(this, "Saving the document", Toast.LENGTH_LONG).show();
            }
        }
    }*/


//    @Override
//    public boolean onDown(MotionEvent event) {
//        Log.e(tag, "onDown: " + event.toString());
//        return true;
//    }
//
//    @Override
//    public boolean onFling(MotionEvent event1, MotionEvent event2,
//                           float velocityX, float velocityY) {
//        Log.e(tag, "onFling: " + event1.toString() + event2.toString());
//        return true;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent event) {
//        Log.e(tag, "onLongPress: " + event.toString());
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
//                            float distanceY) {
//        Log.e(tag, "onScroll: " + event1.toString() + event2.toString());
//        return true;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent event) {
//        Log.e(tag, "onShowPress: " + event.toString());
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent event) {
//        Log.e(tag, "onSingleTapUp: " + event.toString());
//        return true;
//    }
//
//    @Override
//    public boolean onDoubleTap(MotionEvent event) {
//        Log.e(tag, "onDoubleTap: " + event.toString());
//        return true;
//    }
//
//    @Override
//    public boolean onDoubleTapEvent(MotionEvent event) {
//        Log.e(tag, "onDoubleTapEvent: " + event.toString());
//        return true;
//    }
//
//    @Override
//    public boolean onSingleTapConfirmed(MotionEvent event) {
//        Log.e(tag, "onSingleTapConfirmed: " + event.toString());
//        return true;
//    }

}