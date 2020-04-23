package com.dengjia.lib_share_asr.utils;

import android.util.Log;

import com.dengjia.lib_share_asr.asr_skill.BaseSkill;

import java.util.HashMap;
import java.util.List;

public class JudgeAllSkillsUtil {
    /**
     * 判断是否匹配所有已加载的技能
     * @param data
     * @param skillsList
     * @return
     */
    public static boolean judgeAllSkillsUtil(String data, List<BaseSkill> skillsList){
        boolean flag = false;
        for (int i = 0; i < skillsList.size(); i++) {
            boolean flag2 = IsContainUtil.isContainUtil(data, skillsList.get(i));
            if (flag2){
                flag = true;
                break;
            }
        }

        return flag;
    }
}
