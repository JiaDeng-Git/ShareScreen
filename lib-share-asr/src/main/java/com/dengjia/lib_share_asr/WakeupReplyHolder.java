package com.dengjia.lib_share_asr;

import java.util.HashMap;

public class WakeupReplyHolder {

    private volatile static WakeupReplyHolder wakeupReplyHolder;

    private static HashMap<Integer, Integer> replyHash = new HashMap<>();

    private WakeupReplyHolder(){
        replyHash.put(1, R.raw.tone_wake_1);
        replyHash.put(2, R.raw.tone_wake_2);
        replyHash.put(3, R.raw.tone_wake_3);
        replyHash.put(4, R.raw.tone_wake_4);
        replyHash.put(5, R.raw.tone_wake_5);
        replyHash.put(6, R.raw.tone_wake_6);
        replyHash.put(7, R.raw.tone_wake_7);
        replyHash.put(8, R.raw.tone_wake_8);
        replyHash.put(9, R.raw.tone_wake_9);
    }
    public static WakeupReplyHolder getInstance() {
        if (wakeupReplyHolder == null) {
            synchronized (WakeupReplyHolder.class) {
                if (wakeupReplyHolder == null) {
                    wakeupReplyHolder = new WakeupReplyHolder();
                }
            }
        }
        return wakeupReplyHolder;
    }

    public HashMap<Integer, Integer> getReplyHash() {
        return replyHash;
    }
}
