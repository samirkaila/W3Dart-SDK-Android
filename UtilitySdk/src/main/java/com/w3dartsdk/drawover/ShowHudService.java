package com.w3dartsdk.drawover;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.w3dartsdk.R;

public class ShowHudService extends Service {

    // constants
    public static final String BASIC_TAG = ShowHudService.class.getName();
    ChatHeadView chatHeadView;
//    DraggableWindow draggableWindow;

    // get intent methods
    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ShowHudService.class);
        return intent;
    }

    // methods
    @Override
    public void onCreate() {
        super.onCreate();
        // create the custom or default notification
        // based on the android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        try {
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            pm.reboot(null);
            /*Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
//            Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot now"});
//            Runtime.getRuntime().exec(new String[]{"/system/xbin/su","-c","reboot now"});
//            DeviceManager deviceManager = DeviceManager.getInstance();
//            deviceManager.reboot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatHeadView = new ChatHeadView(this, this);
//        chatHeadView.show();

//        draggableWindow = new DraggableWindow(this);
//        draggableWindow.open();
    }


    // for android version >=O we need to create
    // custom notification stating
    // foreground service is running
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Service running")
                .setContentText("Displaying over other apps")

                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.drawable.ic_floating_icon)

                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        showHud();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      /*  // remove views on destroy!
        if (ivCrumpledPaper != null) {
            mWindowManager.removeView(ivCrumpledPaper);
            ivCrumpledPaper = null;
        }

        if (ivRecycleBin != null) {
            mWindowManager.removeView(ivRecycleBin);
            ivRecycleBin = null;
        }*/
        chatHeadView.close();
//        draggableWindow.close();
    }

    public void stopService() {
        stopSelf();
    }

}
