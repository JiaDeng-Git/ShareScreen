package com.dengjia.lib_share_usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
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

    private List<ReceiveDataListener> receiveDataListenerList = new ArrayList<>();

    private byte[] receiveBuf;
    private byte[] sendBuf;
    private int maxPacketSize;

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
            Log.i("ShareUsb.onReceive", intent.getAction().toString());
            String action = intent.getAction();
            Log.i("ShareUsb.onReceive", action);

            if (action.contains(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(context, "USB设备插入", Toast.LENGTH_LONG).show();
                searchDevices();
            } else if (action.contains(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(context, "USB设备拔出", Toast.LENGTH_LONG).show();
                deviceList.clear();
                searchDevices();
            } else if (action.contains(ACTION_USB_PERMISSION)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        searchDevices();
                        Log.i("ShareUsb", "获取设备权限成功");
                    } else {
                        Toast.makeText(context, "设备权限获取失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void run(Context context) {
        this.context = context;

        initBroadcastReceiver();
        searchDevices();
        enableReceiveData();
    }

    // 创建广播接收器
    public void initBroadcastReceiver() {
        // 申请USB设备操作权限
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

        // 时间相关广播接收器
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        timeIntentFilter = new IntentFilter();
        timeIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        this.context.registerReceiver(timeBroadcastReceiver, timeIntentFilter);

        // USB相关广播接收器
        usbBroadcastReceiver = new UsbBroadcastReceiver();
        usbIntentFilter = new IntentFilter();
        usbIntentFilter.addAction(ACTION_USB_PERMISSION);
        usbIntentFilter.addAction(UsbManager.EXTRA_DEVICE);
        usbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        this.context.registerReceiver(usbBroadcastReceiver, usbIntentFilter);
    }

    private void searchDevices() {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        if (usbManager != null) {
            deviceHashMap = usbManager.getDeviceList();
            System.out.println("已连接设备数为：" + deviceHashMap.size());
            Iterator<UsbDevice> deviceIterator = deviceHashMap.values().iterator();
            if (deviceHashMap.size() != 0) {
                while (deviceIterator.hasNext()) {
                    UsbDevice usbDevice = deviceIterator.next();
                    if (usbManager.hasPermission(usbDevice)) {
                        deviceList.add(usbDevice);
                        System.out.println("\n 设备信息：设备名字：" + usbDevice.getDeviceName() + ";  设备描述符" + usbDevice.describeContents());
                    } else {
                        usbManager.requestPermission(usbDevice, mPermissionIntent);
                    }
                }
            }
        }

        if (deviceList.size() != 0){
            initDevices();
        }
    }

    // 初始化设备
    public void initDevices() {
        Log.i("initDevices()", "已连接设备数：" + deviceHashMap.size() + "| 已获授权设备数：" + deviceList.size());
        if (deviceHashMap.size() != 0 && deviceList.size() != 0) {
            usbInterface = deviceList.get(0).getInterface(0);
            System.out.println("该接口具有的端点：" + usbInterface.getEndpointCount());
            usbOutEndpoint = usbInterface.getEndpoint(1);
            System.out.println("端点方向：" + usbOutEndpoint.getDirection() + "; 端点类型：" + usbOutEndpoint.getType());
            usbInEndpoint = usbInterface.getEndpoint(0);
            maxPacketSize = usbInEndpoint.getMaxPacketSize();
            System.out.println("端点方向：" + usbInEndpoint.getDirection() + "; 端点类型：" + usbOutEndpoint.getType());

            usbDeviceConnection = usbManager.openDevice(deviceList.get(0));
            usbDeviceConnection.claimInterface(usbInterface, true);
        }
    }

    // 发送数据
    public boolean sendData(String data) {
        System.out.println("开始发送数据");

        // 装配数据
        try {
            sendBuf = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 发送数据
        if (usbDeviceConnection != null) {
            if (usbDeviceConnection.bulkTransfer(usbOutEndpoint, sendBuf, sendBuf.length, 0) > 0) {
                Log.i("ShareUsb.sendDta()", "数据发送成功");
                return true;
            } else {
                Log.e("ShareUsb.sendDta()", "数据发送失败");
                return false;
            }
        } else {
            Log.e("ShareUsb.sendDta()", "未连接任何设备！");
            return false;
        }
    }

    // 接收数据
    private void enableReceiveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (this) {
                        if (usbDeviceConnection != null) {
                            receiveBuf = new byte[usbInEndpoint.getMaxPacketSize()];
                            int resultCode = usbDeviceConnection.bulkTransfer(usbInEndpoint, receiveBuf, receiveBuf.length, 0);
                            byte[] date = null;
                            if (resultCode > 0) {
                                date = ByteBuffer.allocate(resultCode).array();
                                for (int i = 0; i < resultCode; i++) {
                                    date[i] = receiveBuf[i];
                                }

                                // 将数据传输给观察者
                                if (receiveDataListenerList != null) {
                                    for (ReceiveDataListener receiveDataListener : receiveDataListenerList) {
                                        receiveDataListener.receiveData(new String(date), resultCode);
                                    }

                                }
                            }
                        }
//                        else {
//                            Log.e("ShareUsb.enableReceiveData()", "未连接任何设备！");
//                        }
                    }
                }
            }
        }).start();
    }


    public void addReceiveDataListener(ReceiveDataListener receiveDataListener) {
        receiveDataListenerList.add(receiveDataListener);
    }

    public interface ReceiveDataListener {
        void receiveData(String data, int dataLength);
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

}
