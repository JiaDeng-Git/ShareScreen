package com.dengjia.lib_share_usb;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ShareUsbService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ShareUsbServiceBinder();
    }

    // 是Service对外的接口函数，通过此函数将Service实例传递给实现该接口函数
    public class ShareUsbServiceBinder extends Binder {
        public ShareUsbService getService() {
            return ShareUsbService.this;
        }
    }

}
