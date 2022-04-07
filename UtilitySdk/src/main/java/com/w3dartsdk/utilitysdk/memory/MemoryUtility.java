package com.w3dartsdk.utilitysdk.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.w3dartsdk.utilitysdk.SDKConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    This is the class for getting memory and storage related information from the device
 */
public class MemoryUtility {

    static String tag = MemoryUtility.class.getSimpleName();
    static long totalMemory;
    static long availMemory;
    static long internalMemory;

    public MemoryUtility(Context context) {

        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager.RecentTaskInfo recentTaskInfo = new ActivityManager.RecentTaskInfo();
        actManager.getMemoryInfo(memInfo);
        totalMemory = memInfo.totalMem;
        availMemory = memInfo.availMem;
        internalMemory = getTotalInternalMemorySize();

        /*System.out.println("----------------------------------Device Utility Start-------------------------------------------------");

//        Log.e(tag, "recentTaskInfo: " + recentTaskInfo.toString());
        Log.e(tag, "totalMemory: " + bytesToHuman(totalMemory));
        Log.e(tag, "lowMemory: " + memInfo.lowMemory);
        Log.e(tag, "availMem: " + bytesToHuman(availMemory));
        Log.e(tag, "Internal Memory: " + bytesToHuman(internalMemory));

        //        Log.e(tag, "getTotalRAM(): " + getTotalRAM());
//        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        long bytesAvailable;
//        bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
//        long megAvailable = bytesAvailable / (1024 * 1024);
//        Log.e(tag, "Available Internal/External free Space: " + megAvailable + "MB");
//
//        Log.e(tag, " getTotalInternalMemorySize: " + getTotalInternalMemorySize());
//        Log.e(tag, " getTotalExternalMemorySize: " + getTotalExternalMemorySize());
//        Log.e(tag, " getExternalPaths: " + getExternalPaths().toString());
//        Log.e(tag, " availableProcessors: " + Runtime.getRuntime().availableProcessors());
//        Log.e(tag, " freeMemory: " + bytesToHuman(Runtime.getRuntime().freeMemory()));
//        Log.e(tag, " totalMemory: " + bytesToHuman(Runtime.getRuntime().totalMemory()));
//        Log.e(tag, " maxMemory: " + bytesToHuman(Runtime.getRuntime().maxMemory()));

        System.out.println("----------------------------------Device Utility END-------------------------------------------------");
        */
    }

    public static synchronized String getDetail() {
        return "Memory { \"" + SDKConstants.KEY_STORAGE_TOTAL_MEMORY + "\" : \"" + bytesToHuman(totalMemory) + "\"" +
                " , " + " \"" + SDKConstants.KEY_STORAGE_AVAILABLE_RAM + "\" : \"" + bytesToHuman(availMemory) + "\"" +
                " , " + " \"" + SDKConstants.KEY_STORAGE_INTERNAL_MEMORY + "\" : \"" + bytesToHuman(internalMemory) + "\"" +
                "}";
    }


    private static String bytesToHuman(long size) {
        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "0";
    }

    private static String floatForm(double d) {
        return String.format(java.util.Locale.US, "%.2f", d);
    }

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long BlockSize = stat.getBlockSizeLong();
        long TotalBlocks = stat.getBlockCountLong();
//        return bytesToHuman(TotalBlocks * BlockSize);
        return (TotalBlocks * BlockSize);
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long BlockSize = stat.getBlockSize();
            long TotalBlocks = stat.getBlockCount();
            return bytesToHuman(TotalBlocks * BlockSize);
        } else {
            return "";
        }
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static String formatSize(long size) {
        String suffixSize = null;

        if (size >= 1024) {
            suffixSize = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffixSize = "MB";
                size /= 1024;
            }
        }

        StringBuilder BufferSize = new StringBuilder(
                Long.toString(size));

        int commaOffset = BufferSize.length() - 3;
        while (commaOffset > 0) {
            BufferSize.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffixSize != null) BufferSize.append(suffixSize);
        return BufferSize.toString();
    }


    /**
     * This method returns the list of removable storage and sdcard paths.
     * I have no USB OTG so can not test it. Is anybody can test it, please let me know
     * if working or not. Assume 0th index will be removable sdcard path if size is
     * greater than 0.
     *
     * @return the list of removable storage paths.
     */
    public static HashSet<String> getExternalPaths() {
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount").redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/")) {
                            if (!part.toLowerCase(Locale.US).contains("vold")) {
                                out.add(part.replace("/media_rw", "").replace("mnt", "storage"));
                            }
                        }
                    }
                }
            }
        }
        //Phone's external storage path (Not removal SDCard path)
        String phoneExternalPath = Environment.getExternalStorageDirectory().getPath();

        //Remove it if already exist to filter all the paths of external removable storage devices
        //like removable sdcard, USB OTG etc..
        //When I tested it in ICE Tab(4.4.2), Swipe Tab(4.0.1) with removable sdcard, this method includes
        //phone's external storage path, but when i test it in Moto X Play (6.0) with removable sdcard,
        //this method does not include phone's external storage path. So I am going to remvoe the phone's
        //external storage path to make behavior consistent in all the phone. Ans we already know and it easy
        // to find out the phone's external storage path.
        out.remove(phoneExternalPath);

        return out;
    }


}