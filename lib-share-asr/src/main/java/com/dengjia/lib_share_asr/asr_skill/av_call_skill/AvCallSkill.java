package com.dengjia.lib_share_asr.asr_skill.av_call_skill;

import com.dengjia.lib_share_asr.asr_entity.ActionEntity;
import com.dengjia.lib_share_asr.asr_entity.BaseEntity;
import com.dengjia.lib_share_asr.asr_entity.DeviceEntity;
import com.dengjia.lib_share_asr.asr_entity.PlaceEntity;
import com.dengjia.lib_share_asr.asr_skill.BaseSkill;
import com.dengjia.lib_share_asr.asr_skill.device_control_skill.DeviceControlSkill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AvCallSkill extends BaseSkill {

    private static HashMap<Integer, BaseEntity> entityList = new HashMap<>();

    private AvCallSkill(){
        AvCallEntity avCallEntity = new AvCallEntity();
        entityList.put(0, avCallEntity);
    }

    public class AvCallEntity extends BaseEntity {
        private List<String> actiAvCall;

        public AvCallEntity(){
            actiAvCall = new ArrayList<>();
            actiAvCall.add("视频 通话");
            actiAvCall.add("结束 通话");
        }

        @Override
        public List<String> getEntitiess(){
            return actiAvCall;
        }
    }

    private volatile static AvCallSkill avCallSkill;
    public static AvCallSkill getInstance() {
        if (avCallSkill == null) {
            synchronized (AvCallSkill.class) {
                if (avCallSkill == null) {
                    avCallSkill = new AvCallSkill();
                }
            }
        }
        return avCallSkill;
    }

    @Override
    public HashMap<Integer, BaseEntity> getEntityList() {
        return entityList;
    }

}
