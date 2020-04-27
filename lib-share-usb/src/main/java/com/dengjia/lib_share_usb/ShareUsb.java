package com.dengjia.lib_share_usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShareUsb {

    private static final String TAG = "ShareUsb";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private IntentFilter timeIntentFilter;
    private TimeBroadcastReceiver timeBroadcastReceiver;

    private IntentFilter usbIntentFilter;
    private UsbBroadcastReceiver usbBroadcastReceiver;

    private UsbManager usbManager;
    private PendingIntent mPermissionIntent;
    private UsbInterface usbInterface;
    private UsbEndpoint usbEndpoint;
    private UsbDeviceConnection usbDeviceConnection;
    private HashMap<String, UsbDevice> deviceHashMap = new HashMap<>();
    private List<UsbDevice> deviceList = new ArrayList<>();

    private byte[] receiveBuf;

    private Context context;

    class TimeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(Intent.ACTION_TIME_TICK)) {
                Toast.makeText(context, "接收到了每分钟时间计时器的广播", Toast.LENGTH_LONG).show();
            }
        }
    }

    class UsbBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)) {
                Toast.makeText(context, "USB设备插入", Toast.LENGTH_LONG).show();
                System.out.println("设备插入");
            } else if (intent.getAction().contains(UsbManager.ACTION_USB_ACCESSORY_DETACHED)) {
                Toast.makeText(context, "USB设备拔出", Toast.LENGTH_LONG).show();
                System.out.println("设备拔出");
            }
            else if (intent.getAction().contains(ACTION_USB_PERMISSION)) {
                synchronized (this) {
                    if (deviceList.get(0) != null) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (null != deviceList.get(0)) {
                                Toast.makeText(context, "设备权限获取成功", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "设备权限获取失败", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    public void run(Context context) {
        this.context = context;

        timeBroadcastReceiver = new TimeBroadcastReceiver();
        timeIntentFilter = new IntentFilter();
        timeIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        this.context.registerReceiver(timeBroadcastReceiver, timeIntentFilter);

        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbBroadcastReceiver = new UsbBroadcastReceiver();
        usbIntentFilter = new IntentFilter();
        usbIntentFilter.addAction(ACTION_USB_PERMISSION);
        usbIntentFilter.addAction(UsbManager.EXTRA_DEVICE);
        usbIntentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        usbIntentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        this.context.registerReceiver(usbBroadcastReceiver, usbIntentFilter);

        device();
    }

    public void end() {
        if (timeBroadcastReceiver != null) {
            context.unregisterReceiver(timeBroadcastReceiver);
        }
        if (usbBroadcastReceiver != null) {
            context.unregisterReceiver(usbBroadcastReceiver);
        }
    }

    public void device() {
        if (usbManager != null) {
            deviceHashMap = usbManager.getDeviceList();
            System.out.println("已连接设备数为：" + deviceHashMap.size());
            Iterator<UsbDevice> deviceIterator = deviceHashMap.values().iterator();
            if (deviceHashMap.size() != 0){
                while (deviceIterator.hasNext()) {
                    if (usbManager.hasPermission(deviceIterator.next())) {
                        deviceList.add(deviceIterator.next());
                    } else {
                        if (deviceList.size() != 0)
                            usbManager.requestPermission(deviceIterator.next(), mPermissionIntent);
                    }

                }
                if (deviceList.size() != 0) {
                    usbInterface = deviceList.get(0).getInterface(0);
                    usbEndpoint = usbInterface.getEndpoint(0);
                    usbDeviceConnection = usbManager.openDevice(deviceList.get(0));
                    usbDeviceConnection.claimInterface(usbInterface, true);
                    usbDeviceConnection.bulkTransfer(usbEndpoint, receiveBuf, receiveBuf.length, 0);
                    Toast.makeText(context, receiveBuf.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
