/**
 * Copyright 2020 JiaDeng.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dengjia.lib_share_asr.utils;

import android.util.Log;

import com.dengjia.lib_share_asr.asr_skill.BaseSkill;

import java.util.ArrayList;
import java.util.List;

public class IsContainUtil {
    /**
     * 判断是否匹配某技能
     *
     * @param data      识别文本
     * @param baseSkill 技能
     * @return
     */
    public static boolean isContainUtil(String data, BaseSkill baseSkill) {

        // 技能的实体匹配标志
        List<Boolean> flags = new ArrayList<>();
        // 技能是否匹配标志
        boolean flag = false;

        // 外层循环，遍历所有实体
        for (int i = 0; i < baseSkill.getEntityList().size(); i++) {
            boolean isJump = false;
            // 内层循环遍历实体所有词条
            for (int j = 0; j < baseSkill.getEntityList().get(i).getEntitiess().size(); j++) {
                if (data.contains(baseSkill.getEntityList().get(i).getEntitiess().get(j))) {
                    flags.add(true);
                    isJump = true;
                    continue;
                }
            }

            if (!isJump) {
                flags.add(false);
            }else {
                isJump = false;
            }
        }

        if (flags.size() > 0) {
            flag = true;
            for (int i = 0; i < flags.size(); i++) {
                flag = flag && flags.get(i);
            }
        }


        Log.e("isContainUtil", "实体匹配情况：" + flags + "; 本技能匹配结果Flag：" + flag + "\n");
        return flag;
    }
}
