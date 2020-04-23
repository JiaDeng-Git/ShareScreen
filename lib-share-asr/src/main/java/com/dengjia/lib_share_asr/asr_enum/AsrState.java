package com.dengjia.lib_share_asr.asr_enum;

public enum AsrState {

    INIT(0, "初始化"),
    READY(1, "准备"),
    RUN(2, "运行"),
    STOP(3, "暂停");

    private int num;
    private String state;

    AsrState(int num, String state) {
        this.num = num;
        this.state = state;
    }

    public int getNum() {
        return num;
    }

    public String getState() {
        return state;
    }
}
