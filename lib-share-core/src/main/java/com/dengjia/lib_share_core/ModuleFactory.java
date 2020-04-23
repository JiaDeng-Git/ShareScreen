//package com.dengjia.lib_share_core;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//
//import com.dengjia.lib_share_asr.ShareASR;
//import com.dengjia.lib_share_core.EventRouter.EventRouter;
//import com.dengjia.lib_share_core.EventRouter.EventType.AsrEvent;
//
//public class ModuleFactory {
//
//    private static final String TAG = "ModuleFactory";
//
//    private ShareASR shareASR;
//
//    private Context context;
//
//    public ModuleFactory(Context context) {
//        this.context = context;
//    }
//
//    public void run() {
////        shareASR = new ShareASR();
////        shareASR.init(context);
////        shareASR.addListenner(asrEventListener);
//    }
//
//    public void jump(Activity activity){
//        Intent intent = new Intent(activity, ShareASR.class);
//        activity.startActivity(intent);
//    }
//
//    private ShareASR.AsrEventListener asrEventListener = new ShareASR.AsrEventListener() {
//        @Override
//        public void asr_initDone(Object data) {
//            EventRouter.putEvent("AsrEvent", AsrEvent.INIT_DONE.getEvent(), data);
//        }
//    };
//
//}
