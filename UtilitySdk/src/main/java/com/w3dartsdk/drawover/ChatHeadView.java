package com.w3dartsdk.drawover;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.w3dartsdk.DartBug;
import com.w3dartsdk.R;
import com.w3dartsdk.utilitysdk.SDKConstants;


public class ChatHeadView {

    // constants
    public static final String tag = ChatHeadView.class.getName();

    // variables
    private Vibrator mVibrator;
    private WindowManager mWindowManager;

    private WindowManager.LayoutParams mPaperParams;
    private WindowManager.LayoutParams mRecycleBinParams;
    private int windowHeight;
    private int windowWidth;

    // UI
    private ImageView ivCrumpledPaper;
    private ImageView ivRecycleBin;
    private View removeView;

    private Context context;
    ShowHudService serviceIntent;

    public ChatHeadView(Context context, ShowHudService intent) {
        this.context = context;
        serviceIntent = intent;

        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (ivCrumpledPaper != null) {
            mWindowManager.removeView(ivCrumpledPaper);
            ivCrumpledPaper = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mPaperParams = new WindowManager.LayoutParams(
                    150,150,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {

            mPaperParams = new WindowManager.LayoutParams(
                    150,150,
                    /*WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,*/
                    WindowManager.LayoutParams.TYPE_PHONE,
                    /*WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | */WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

        Log.e("Service", "Width: " + windowWidth + " Height: " + windowHeight);

        ivCrumpledPaper = new ImageView(context);
        ivCrumpledPaper.setImageResource(R.drawable.ic_floating_icon);

        mPaperParams.x = 0;
//        mPaperParams.y = ivCrumpledPaper.getHeight()+getStatusBarHeight();
        mPaperParams.y = 0;
        mPaperParams.gravity = Gravity.TOP | Gravity.RIGHT;

        mWindowManager.addView(ivCrumpledPaper, mPaperParams);
        addCrumpledPaperOnTouchListener();
    }

    private void addCrumpledPaperOnTouchListener() {
        ivCrumpledPaper.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            private static final int MAX_CLICK_DURATION = 300;
            private static final int LONG_CLICK_WAIT_DURATION = 600;
            private long startClickTime;
            boolean isLongClick = false;
            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    Log.e(tag, "Into runnable_longClick");

                    isLongClick = true;
                    performLongClick();
                }
            };


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, LONG_CLICK_WAIT_DURATION);

                        initialX = mPaperParams.x;
                        initialY = mPaperParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        return true;

                    case MotionEvent.ACTION_UP:
                        isLongClick = false;
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        if (clickDuration < MAX_CLICK_DURATION) {
                            //click event has occurred
                            performClick();
                        }
                        handler_longClick.removeCallbacks(runnable_longClick);

                        int centerOfScreenByX = windowWidth / 2;

                        if (removeView != null) {

                            // remove crumpled paper when the it is in the recycle bin's area
                            if ((mPaperParams.y > windowHeight - ivRecycleBin.getHeight() - ivCrumpledPaper.getHeight()) &&
                                    ((mPaperParams.x > centerOfScreenByX - ivRecycleBin.getWidth() - ivCrumpledPaper.getWidth() / 2) && (mPaperParams.x < centerOfScreenByX + ivRecycleBin.getWidth() / 2))) {
                                mVibrator.vibrate(200);
//                            stopSelf();
                                serviceIntent.stopService();
                            }

                            // always remove recycle bin ImageView when paper is dropped
//                        mWindowManager.removeView(ivRecycleBin);
//                        ivRecycleBin = null;
                            mWindowManager.removeView(removeView);
                            removeView = null;
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
//                        if (isLongClick) {
                        // move paper ImageView
                        mPaperParams.x = initialX + (int) (initialTouchX - event.getRawX());
                        mPaperParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(ivCrumpledPaper, mPaperParams);
//                        }
                        return true;
                }
                return false;
            }
        });
    }


    private void performClick() {
//        Intent it = new Intent(this, MyDialog.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(it);
        SDKConstants.ENABLE_SCREEN_SHOT = true;
        context.startActivity(new Intent(context, AddBugReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        serviceIntent.stopService();

    }

    private void performLongClick() {
        // add recycle bin when moving crumpled paper
        addRecycleBinView();
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

        removeView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.remove, null);
        ivRecycleBin = removeView.findViewById(R.id.remove_img);

        /*ivRecycleBin = new ImageView(context);
        ivRecycleBin.setMaxWidth(50);
        ivRecycleBin.setMaxHeight(50);
        ivRecycleBin.setImageResource(R.drawable.remove);*/

        mRecycleBinParams.x = 0;
        mRecycleBinParams.y = 0;

        mRecycleBinParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
//        mWindowManager.addView(ivRecycleBin, mRecycleBinParams);
        mWindowManager.addView(removeView, mRecycleBinParams);
    }

    public void close() {

       /* try {
            // remove the view from the window
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
            ((ViewGroup) mView.getParent()).removeAllViews();

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("Error2", e.toString());
        }*/


        // remove views on destroy!
        if (ivCrumpledPaper != null) {
            mWindowManager.removeView(ivCrumpledPaper);
            ivCrumpledPaper = null;
        }

//        if (ivRecycleBin != null) {
//            mWindowManager.removeView(ivRecycleBin);
//            ivRecycleBin = null;
//        }
        if (removeView != null) {
            mWindowManager.removeView(removeView);
            removeView = null;
        }
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

}