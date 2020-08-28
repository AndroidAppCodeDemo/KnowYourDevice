package com.letv.deviceinformation;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();


    private static final int REQUEST_READ_PHONE_STATE = 0x1001;

    private TextView textView01;
    private TextView button01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // 获取权限
        int permissionCheck = ContextCompat.checkSelfPermission(MyActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        //
        textView01 = (TextView) findViewById(R.id.textView01);
        button01 = (TextView) findViewById(R.id.button01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取设备信息
                getDeviceInfo();
            }
        });


    }


    private void getDeviceInfo() {

        int permissionCheck = ContextCompat.checkSelfPermission(MyActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MyActivity.this, "需要开启读取设备信息权限", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("IMSI: ");
        sb.append(getSimIMSI(MyActivity.this));
        sb.append("\n");
        sb.append("IMEI: ");
        sb.append(getDeviceIMEI(MyActivity.this));
        sb.append("\n");
        sb.append("\n");
        sb.append("SN: ");
        sb.append(DeviceUtil.getSN());
        sb.append("\n");
        sb.append("Manufacturer: ");
        sb.append(DeviceUtil.getManufacturer());
        sb.append("\n");
        sb.append("Brand: ");
        sb.append(DeviceUtil.getBrand());
        sb.append("\n");
        sb.append("Model: ");
        sb.append(DeviceUtil.getModel());
        sb.append("\n");
        sb.append("SystemVersion: ");
        sb.append(DeviceUtil.getSystemVersion());
        sb.append("\n");
        sb.append("\n");
        sb.append("WifiMac: ");
        sb.append(DeviceUtil.getWifiMac(getApplicationContext()));
        sb.append("\n");
        sb.append("WireMac: ");
        sb.append(DeviceUtil.getWireMac());
        sb.append("\n");
        sb.append("\n");
        sb.append("TotalMemory: ");
        sb.append(DeviceUtil.getTotalMemory(getApplicationContext()));
        sb.append("\n");
        sb.append("AvailMemory: ");
        sb.append(DeviceUtil.getAvailMemory(getApplicationContext()));
        sb.append("\n");
        sb.append("\n");
        sb.append(DeviceUtil.getPixels(this));
        sb.append("\n");
        sb.append("CpuName: ");
        sb.append(CPUInforUtil.getCpuName());
        sb.append("\n");
        sb.append("CpuMaxFreq: ");
        sb.append(CPUInforUtil.getCpuMaxFreq(getApplicationContext()));
        sb.append("\n");
        sb.append("CpuMinFreq: ");
        sb.append(CPUInforUtil.getCpuMinFreq(getApplicationContext()));
        sb.append("\n");
        sb.append("CpuCurFreq: ");
        sb.append(CPUInforUtil.getCpuCurFreq(getApplicationContext()));
        sb.append("\n");
        sb.append("CPUCoresNum: ");
        sb.append(CPUInforUtil.getCPUCoresNum());
        sb.append("\n");
        textView01.setText(sb.toString());

        /**
         * 复制到剪切板
         */
        copy(MyActivity.this, sb.toString());
    }


    public void copy(Activity activity, String desc) {
        ClipboardManager clipboard = (ClipboardManager) activity
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (null != clipboard) {
            if (android.os.Build.VERSION.SDK_INT < 11) {
                clipboard.setText(desc);
            } else {
                ClipData clip = ClipData.newPlainText("label", desc);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(MyActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 手机卡：国际移动用户识别码
     */
    public static String getSimIMSI(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                //获取IMSI号
                String imsi = telephonyManager.getSubscriberId();
                if (null == imsi) {
                    imsi = "";
                }
                return imsi;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }


    /**
     * 设备:国际移动设备识别码
     *
     * @param activity
     * @return
     */
    public static final String getDeviceIMEI(Activity activity) {

        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            try {
                //实例化TelephonyManager对象
                TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                //获取IMEI号
                String imei = telephonyManager.getDeviceId();
                //在次做个验证，也不是什么时候都能获取到的啊
                if (imei == null) {
                    imei = "";
                }
                return imei;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

}
