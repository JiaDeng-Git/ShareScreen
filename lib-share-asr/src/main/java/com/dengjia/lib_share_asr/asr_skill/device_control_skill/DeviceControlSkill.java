/**
 * Copyright 2020 JiaDeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dengjia.lib_share_asr.asr_skill.device_control_skill;

import com.dengjia.lib_share_asr.asr_skill.BaseSkill;
import com.dengjia.lib_share_asr.asr_entity.ActionEntity;
import com.dengjia.lib_share_asr.asr_entity.BaseEntity;
import com.dengjia.lib_share_asr.asr_entity.DeviceEntity;
import com.dengjia.lib_share_asr.asr_entity.PlaceEntity;

import java.util.HashMap;

public class DeviceControlSkill extends BaseSkill {

    private static HashMap<Integer, BaseEntity> entityList = new HashMap<>();

    private DeviceControlSkill(){
        ActionEntity actionEntity = new ActionEntity();
        PlaceEntity placeEntity = new PlaceEntity();
        DeviceEntity deviceEntity = new DeviceEntity();
        entityList.put(0, actionEntity);
        entityList.put(1, placeEntity);
        entityList.put(2, deviceEntity);
    }

    private volatile static DeviceControlSkill deviceControlSkill;
    public static DeviceControlSkill getInstance() {
        if (deviceControlSkill == null) {
            synchronized (DeviceControlSkill.class) {
                if (deviceControlSkill == null) {
                    deviceControlSkill = new DeviceControlSkill();
                }
            }
        }
        return deviceControlSkill;
    }

    @Override
    public HashMap<Integer, BaseEntity> getEntityList() {
        return entityList;
    }
}
