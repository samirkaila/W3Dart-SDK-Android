package com.w3dartsdk.utilitysdk.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.w3dartsdk.utilitysdk.SDKConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class NetworkUtility {

    static String tag = NetworkUtility.class.getSimpleName();

    final ConnectivityManager connectivityManager;

    static boolean isNetworkAvailable = false;
    static boolean isWifiEnable = false;
    static boolean isMobileNetworkAvailable = false;


    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    public void setIsWifiEnable(boolean isWifiEnable) {
        this.isWifiEnable = isWifiEnable;
    }

    public boolean isMobileNetworkAvailable() {
        return isMobileNetworkAvailable;
    }

    public void setIsMobileNetworkAvailable(boolean isMobileNetworkAvailable) {
        this.isMobileNetworkAvailable = isMobileNetworkAvailable;
    }


    public NetworkUtility(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            Log.e(tag, "getExtraInfo: " + activeNetworkInfo);

            if (activeNetworkInfo != null) {
//                Log.e(tag, "getExtraInfo: " + activeNetworkInfo.getExtraInfo());
//                Log.e(tag, "getType: " + activeNetworkInfo.getType());
//                Log.e(tag, "getReason: " + activeNetworkInfo.getReason());
//                Log.e(tag, "getSubtypeName: " + activeNetworkInfo.getSubtypeName());
//                Log.e(tag, "getTypeName: " + activeNetworkInfo.getTypeName());
//                Log.e(tag, "getSubtype: " + activeNetworkInfo.getSubtype());
//                Log.e(tag, "getDetailedState: " + activeNetworkInfo.getDetailedState());
//                Log.e(tag, "getState: " + activeNetworkInfo.getState());

                //should check null because in airplane mode it will be null
                /*NetworkCapabilities nc = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                    int downSpeed = nc.getLinkDownstreamBandwidthKbps();
                    int upSpeed = nc.getLinkUpstreamBandwidthKbps();
                    Log.e(tag, "downSpeed: " + downSpeed);
                    Log.e(tag, "upSpeed: " + upSpeed);
                }*/

                setIsWifiEnable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                setIsMobileNetworkAvailable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());

                if (isWifiEnable() || isMobileNetworkAvailable()) {
                    isNetworkAvailable = true;
                    /*Sometime wifi is connected but service provider never connected to internet so cross check one more time*/
                    if (isOnline())
                        isNetworkAvailable = true;
                }

                /*if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else
                        return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;*/
            }
        } else {
            isNetworkAvailable = false;
        }

        boolean wifiState;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        Log.e(tag, "wifiManager: " + wifiManager);

        if (wifiManager != null) {
//            Log.e(tag, "toString: " + wifiManager.getConnectionInfo().toString());
            wifiState = wifiManager.isWifiEnabled();
//            Log.e(tag, "wifiState: " + wifiState);
//            Log.e(tag, "getBSSID: " + wifiManager.getConnectionInfo().getBSSID());
////                Log.e(tag, "getMacAddress: " + wifiManager.getConnectionInfo().getMacAddress());
//            Log.e(tag, "getSSID: " + wifiManager.getConnectionInfo().getSSID());
//            Log.e(tag, "getHiddenSSID: " + wifiManager.getConnectionInfo().getHiddenSSID());
//            Log.e(tag, "getLinkSpeed: " + wifiManager.getConnectionInfo().getLinkSpeed());
//            Log.e(tag, "getIpAddress: " + wifiManager.getConnectionInfo().getIpAddress());
//            Log.e(tag, "getIpAddress: " + getIPAddress(true));

        }
    }

    public static synchronized String getDetail() {
        return "NetworkDetails { \"" + SDKConstants.KEY_NETWORK_AVAILABLE + "\" : \"" + isNetworkAvailable + "\"" +
                " , " + " \"" + SDKConstants.KEY_NETWORK_WIFI_ENABLED + "\" : \"" + isWifiEnable + "\"" +
                " , " + " \"" + SDKConstants.KEY_NETWORK_MOBILE_DATA_ENABLED + "\" : \"" + isMobileNetworkAvailable + "\"" +
                "}";
    }


    public boolean isOnline() {
        /*Just to check Time delay*/
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        Runtime runtime = Runtime.getRuntime();
        try {
            /*Pinging to Google server*/
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long t2 = Calendar.getInstance().getTimeInMillis();
//            Log.e(tag, "NetWork check Time: " + (t2 - timeInMillis) + "");
        }
        return false;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        Log.e(tag, "NetWork IP: " + sAddr);
//                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = addr instanceof InetAddress;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
