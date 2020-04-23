package com.dengjia.lib_share_asr.asr_entity;

import java.util.ArrayList;
import java.util.List;

public class PlaceEntity extends BaseEntity {
    private static List<String> places;

    public PlaceEntity(){
        places = new ArrayList<>();
        places.add("客厅");
        places.add("房间");
        places.add("阳台");
        places.add("厨房");
        places.add("卫生间");
        places.add("浴室");
    }

    @Override
    public List<String> getEntitiess(){
        return places;
    }
}
