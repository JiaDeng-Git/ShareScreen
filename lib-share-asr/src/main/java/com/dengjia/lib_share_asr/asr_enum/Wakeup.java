package com.dengjia.lib_share_asr.asr_enum;

public enum Wakeup {

    WAKEUP("小菲");

    private String wakeUpWord;

    Wakeup(String wakeUpWord) {
        this.wakeUpWord =wakeUpWord;
    }

    public String getWakeUpWord() {
        return wakeUpWord;
    }

    public void setWakeUpWord(String wakeUpWord) {
        WAKEUP.wakeUpWord = wakeUpWord;
    }
}
