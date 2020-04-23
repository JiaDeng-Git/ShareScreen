package com.dengjia.lib_share_asr.grammer;

public enum  Device {

    LIGHT(1, "灯"),
    AIR_CONDITIONER(2, "空调"),
    TV(3, "电视"),
    HUMIDIFIER(4, "加湿器");

    private int number;
    private String device;

    Device(int num, String action) {
        this.number = num;
        this.device = action;
    }

    public int getNumber() {
        return number;
    }

    public String getDevice() {
        return device;
    }
}
