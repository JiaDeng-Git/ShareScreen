package com.dengjia.lib_share_asr.grammer;

public enum Action {

    OPEN(1, "打开"),
    CLOSE(2, "关闭"),
    TRUN_DOWM(3, "调小"),
    TRUN_UP(4, "调暗");

    private int number;
    private String action;

    Action(int number, String action) {
        this.number = number;
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public String getAction() {
        return action;
    }
}
