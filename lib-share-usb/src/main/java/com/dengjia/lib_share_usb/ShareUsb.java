package com.dengjia.lib_share_usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class ShareUsb {

    private IntentFilter intentFilter;
    private MyBroadcastReceiver myBroadcastReceiver;

    private Context context;

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(Intent.ACTION_POWER_CONNECTED)) {
                Toast.makeText(context, "在USB组件动态广播接收器：接收到了连接充电器的广播", Toast.LENGTH_SHORT).show();
            }else if (intent.getAction().contains(Intent.ACTION_TIME_TICK)) {
                Toast.makeText(context, "在USB组件动态广播接收器：接收到了每分钟时间计时器的广播", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void run(Context context) {
        this.context = context;
        myBroadcastReceiver = new MyBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        this.context.registerReceiver(myBroadcastReceiver, intentFilter);
    }

    public void end() {
        if (myBroadcastReceiver != null) {
            context.unregisterReceiver(myBroadcastReceiver);
        }
    }

}
