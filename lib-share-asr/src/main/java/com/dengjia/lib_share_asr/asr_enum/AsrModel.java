package com.dengjia.lib_share_asr.asr_enum;

public enum AsrModel {

    WAKEUP(0, "唤醒词模式"),
    USUAL(1, "普通识别模式"),
    FILE(2, "音频文件读取识别模式"),
    WAKEUP_USUAL_MIX(3, "唤醒词与普通混合识别模式");

    private int num;
    private String model;

    AsrModel(int num, String model) {
        this.num = num;
        this.model = model;
    }

    public int getNum() {
        return num;
    }

    public String getModel() {
        return model;
    }

}
