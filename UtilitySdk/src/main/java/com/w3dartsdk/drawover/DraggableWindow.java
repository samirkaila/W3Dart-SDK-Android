package com.w3dartsdk.drawover;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.w3dartsdk.R;

public class DraggableWindow {

    // variables
    private Vibrator mVibrator;

    // declaring required variables
    private Context context;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private WindowManager.LayoutParams mRecycleBinParams;

    private View mView;
    private View mViewRemove;

    private int windowHeight;
    private int windowWidth;


    private LayoutInflater layoutInflater;

    public DraggableWindow(Context context) {
        this.context = context;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {

            mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    /*WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | */WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }
        // getting a LayoutInflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.chathead, null);
//        mView.findViewById(R.id.chathead_img).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                close();
//            }
//        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

        // Define the position of the
        // window within the screen
        mParams.gravity = Gravity.START;
        mParams.x = 0;
        mParams.y = 100;


    }

    public void open() {

        try {
            // check if the view is already 
            // inflated or present in the window
            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {

                    mWindowManager.addView(mView, mParams);
                    addCrumpledPaperOnTouchListener();
                }
            }
        } catch (Exception e) {
            Log.d("Error1", e.toString());
        }

    }

    public void close() {

        try {
            Log.e("DraggableWindow", "View removed");
            // remove the view from the window
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
            ((ViewGroup) mView.getParent()).removeAllViews();

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.e("Error2", e.toString());
        }
    }


    private void addCrumpledPaperOnTouchListener() {
        mView.findViewById(R.id.chathead_img).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mParams.x;
                        initialY = mParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        // add recycle bin when moving crumpled paper
                        addRecycleBinView();

                        return true;
                    case MotionEvent.ACTION_UP:

                        int centerOfScreenByX = windowWidth / 2;

                        // remove crumpled paper when the it is in the recycle bin's area
//                        if ((mParams.y > windowHeight - ivRecycleBin.getHeight() - ivCrumpledPaper.getHeight()) &&
//                                ((mParams.x > centerOfScreenByX - ivRecycleBin.getWidth() - ivCrumpledPaper.getWidth() / 2) && (mParams.x < centerOfScreenByX + ivRecycleBin.getWidth() / 2))) {
                        if ((mParams.y > windowHeight - mViewRemove.getHeight() - mView.getHeight()) &&
                                ((mParams.x > centerOfScreenByX - mViewRemove.getWidth() - mView.getWidth() / 2) && (mParams.x < centerOfScreenByX + mViewRemove.getWidth() / 2))) {
                            mVibrator.vibrate(200);
//                            stopSelf();
//                            serviceIntent.stopService();
                            context.stopService(new Intent(context, ShowHudService.class));
                        }

                        // always remove recycle bin ImageView when paper is dropped
//                        mWindowManager.removeView(ivRecycleBin);
//                        ivRecycleBin = null;
                        mWindowManager.removeView(mViewRemove);
//                        ivRecycleBin = null;

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // move paper ImageView
                        mParams.x = initialX + (int) (initialTouchX - event.getRawX());
                        mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mView, mParams);
                        return true;
                }
                return false;
            }
        });
    }

    private void addRecycleBinView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mRecycleBinParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {

            mRecycleBinParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    /*WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | */WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }

        // getting a LayoutInflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflating the view with the custom layout we created
        mViewRemove = layoutInflater.inflate(R.layout.remove, null);
//        ivRecycleBin = removeView.findViewById(R.id.remove_img);

//        ivRecycleBin = new ImageView(context);
//        ivRecycleBin.setImageResource(R.drawable.remove);

        mRecycleBinParams.x = 0;
        mRecycleBinParams.y = 0;

        mRecycleBinParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        mWindowManager.addView(mViewRemove, mRecycleBinParams);
    }


}