package com.letv.deviceinformation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

public class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();

    /*
     * 设备序列号 SN
     */
    public static String getSN() {
        return Build.SERIAL;
    }


    /**
     * 设备制造商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 品牌
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /*
     * 设备型号
     */
    public static String getModel() {
        return Build.MODEL;
    }





    /*
     * system version
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * mac wifi address
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {

        Log.i(TAG, "-----------getWIFIMac-----------");

        String str = null;
        try {
            WifiManager localWifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
            str = localWifiInfo.getMacAddress();
        } catch (Exception e) {
            Log.i("TAG", "Could not read MAC");
            e.printStackTrace();
        }
        Log.i(TAG, "wifimac: " + str + "");

        return str;
    }

    /**
     * @return
     */
    public static String getWireMac() {

        Log.i(TAG, "-----------getWireMac-----------");

        String macSerial = null;
        try {

            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String str = input.readLine();
            if (str != null) {
                macSerial = str.trim();
            }

        } catch (IOException ex) {
            macSerial = "00:00:00:00";
            ex.printStackTrace();
        }
        Log.i(TAG, "WireMac: " + macSerial);

        return macSerial;

    }


    //
    public static String getTotalMemory(Context mContext) {
        long mTotal;
        //
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        //

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content) * 1024;

        return Formatter.formatFileSize(mContext, mTotal);

    }

    //
    public static String getAvailMemory(Context mContext) {
        //
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE); //
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //
        return Formatter.formatFileSize(mContext, mi.availMem);
    }

    //
    public static String getPixels(MyActivity activity) {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //
        StringBuilder sb = new StringBuilder();
        sb.append("ScreenWidth: ");
        sb.append(dm.widthPixels);
        sb.append("\n");
        sb.append("ScreenHeight: ");
        sb.append(dm.heightPixels);
        sb.append("\n");
        sb.append("ScreenDensity：");
        sb.append(dm.density);
        sb.append("\n");
        sb.append("ScreenDpi: ");
        sb.append(dm.densityDpi);
        sb.append("\n");

        return sb.toString();
    }

    /*
     * MD5
     */
    private static String MD5(String inStr) {

        String md5Str = null;

        if (inStr == null) {
            return null;
        }

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println("get MessageDigest Instance Error");
            e.printStackTrace();
            return null;
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        md5Str = hexValue.toString();

        Log.i(TAG, "MD5: " + md5Str + "");

        return md5Str;
    }


}
