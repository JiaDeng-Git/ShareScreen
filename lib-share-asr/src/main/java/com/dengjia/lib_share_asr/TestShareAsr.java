package com.dengjia.lib_share_asr;

import android.util.Log;

public class TestShareAsr implements ShareAsr.AsrResultListener {

    private String result;

    @Override
    public void onAsrResult(String result) {
        this.result = result;
        Log.e("TestShareAsr：", result);
    }
}
