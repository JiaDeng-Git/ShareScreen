package com.dengjia.lib_share_usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShareUsb {

    private static final String TAG = "ShareUsb";
    private static final String ACTION_USB_PERMISSION = "com.dengjia.share_usb.USB_PERMISSION";

    private IntentFilter timeIntentFilter;
    private TimeBroadcastReceiver timeBroadcastReceiver;

    private IntentFilter usbIntentFilter;
    private UsbBroadcastReceiver usbBroadcastReceiver;

    private UsbManager usbManager;
    private PendingIntent mPermissionIntent;
    private UsbInterface usbInterface;
    private UsbEndpoint usbOutEndpoint;
    private UsbEndpoint usbInEndpoint;
    private UsbDeviceConnection usbDeviceConnection;
    private HashMap<String, UsbDevice> deviceHashMap = new HashMap<>();
    private List<UsbDevice> deviceList = new ArrayList<>();

    private UsbRequest usbRequest;

    private byte[] receiveBuf;
    private byte[] sendBuf;

    private Context context;

    class TimeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // System.out.println(intent.getAction().toString());
            if (intent.getAction().contains(Intent.ACTION_TIME_TICK)) {
                // Toast.makeText(context, "接收到了每分钟时间计时器的广播", Toast.LENGTH_LONG).show();
                if (usbDeviceConnection != null) {
                    receiveBuf = new byte[usbInEndpoint.getMaxPacketSize()];
                    usbDeviceConnection.controlTransfer(UsbConstants.USB_DIR_IN, 10, 20, 30, receiveBuf, receiveBuf.length, 0);
                    String data = receiveBuf.toString();
                    System.out.println("读取数据：" + data);
                    Toast.makeText(context, "读取数据" + data, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class UsbBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent.getAction().toString());
            String action = intent.getAction();
            System.out.println(action);

            if (action.contains(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(context, "USB设备插入", Toast.LENGTH_LONG).show();
                System.out.println("设备插入");
            } else if (action.contains(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(context, "USB设备拔出", Toast.LENGTH_LONG).show();
                System.out.println("设备拔出");
            } else if (action.contains(ACTION_USB_PERMISSION)) {
                synchronized (this) {
                    if (deviceList.size() != 0) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (null != deviceList.get(0)) {
                                Toast.makeText(context, "设备权限获取成功", Toast.LENGTH_LONG).show();
                                initDevice();
                            }
                        } else {
                            Toast.makeText(context, "设备权限获取失败", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //getDevice();
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
        usbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        this.context.registerReceiver(usbBroadcastReceiver, usbIntentFilter);

        getDevice();
    }

    public void end() {
        if (timeBroadcastReceiver != null) {
            context.unregisterReceiver(timeBroadcastReceiver);
        }
        if (usbBroadcastReceiver != null) {
            context.unregisterReceiver(usbBroadcastReceiver);
        }
        if (usbDeviceConnection != null) {
            usbDeviceConnection.close();
        }
    }

    public void getDevice() {
        if (usbManager != null) {
            deviceHashMap = usbManager.getDeviceList();
            System.out.println("getDevice()已连接设备数为：" + deviceHashMap.size());
            Iterator<UsbDevice> deviceIterator = deviceHashMap.values().iterator();
            if (deviceHashMap.size() != 0) {
                while (deviceIterator.hasNext()) {
                    UsbDevice usbDevice = deviceIterator.next();
                    if (usbManager.hasPermission(usbDevice)) {
                        deviceList.add(usbDevice);
                        System.out.println("getDevice()获取设备" + usbDevice.getDeviceName() + usbDevice.describeContents());
                        initDevice();
                    } else {
                        usbManager.requestPermission(usbDevice, mPermissionIntent);
                    }
                }
            }
        }
    }

    public void initDevice() {
        System.out.println("initDevice()已连接设备数为：" + deviceList.size());
        if (deviceList.size() != 0) {
            usbInterface = deviceList.get(0).getInterface(0);
            System.out.println("该接口具有的端点：" + usbInterface.getEndpointCount());
            usbOutEndpoint = usbInterface.getEndpoint(1);
            System.out.println("端点方向：" + usbOutEndpoint.getDirection() + "; 端点类型：" + usbOutEndpoint.getType());
            usbInEndpoint = usbInterface.getEndpoint(0);
            System.out.println("端点方向：" + usbInEndpoint.getDirection() + "; 端点类型：" + usbOutEndpoint.getType());
            usbDeviceConnection = usbManager.openDevice(deviceList.get(0));
            usbDeviceConnection.claimInterface(usbInterface, true);

            System.out.println("开始发送数据");
            String temp = "Android send data to device!";
            try {
                sendBuf = temp.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (usbDeviceConnection.bulkTransfer(usbOutEndpoint, sendBuf, sendBuf.length, 0) > 0) {
                System.out.println("数据发送成功");
            }

            System.out.println("开始接收数据");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    receiveBuf = new byte[usbInEndpoint.getMaxPacketSize()];
//                    usbDeviceConnection.controlTransfer(UsbConstants.USB_DIR_IN, 10, 20, 30, receiveBuf, receiveBuf.length, 0);
//                    System.out.println("读取数据：" + new String(receiveBuf));
//                }
//            }).start();
//            int inMax = usbInEndpoint.getMaxPacketSize();
//            receiveBuf = ByteBuffer.allocate(inMax);
//            usbRequest = new UsbRequest();
//            usbRequest.initialize(usbDeviceConnection, usbInEndpoint);
//            usbRequest.queue(receiveBuf, inMax);
//            if (usbDeviceConnection.requestWait() == usbRequest) {
//                byte[] retData = receiveBuf.array();
//                for (Byte byte1 : retData) {
//                    Log.e("接收到数据：", byte1.toString());
//                }
//            }

        }
    }

    // 1. 寻找设备
    // 2. 打开设备
    // 3. 数据传输

}
