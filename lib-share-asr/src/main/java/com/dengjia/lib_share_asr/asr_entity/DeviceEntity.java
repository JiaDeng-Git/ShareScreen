package com.dengjia.lib_share_asr.asr_entity;

import java.util.ArrayList;
import java.util.List;

public class DeviceEntity extends BaseEntity {
    private static List<String> devices;

    public DeviceEntity(){
        devices = new ArrayList<>();
        devices.add("灯");
        devices.add("电视");
        devices.add("风扇");
        devices.add("加湿器");
        devices.add("灯带");
        devices.add("台灯");
        devices.add("空调");
    }

    @Override
    public List<String> getEntitiess(){
        return devices;
    }
}
