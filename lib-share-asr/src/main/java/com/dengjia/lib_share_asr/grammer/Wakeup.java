package com.dengjia.lib_share_asr.grammer;

public enum Wakeup {

    WAKEUP(1, "小菲");

    private int number;
    private String wakeUpWord;

    Wakeup(int number, String wakeUpWord) {
        this.number = number;
        this.wakeUpWord =wakeUpWord;
    }

    public int getNumber() {
        return number;
    }

    public String getWakeUpWord() {
        return wakeUpWord;
    }

    public void setWakeUpWord(String wakeUpWord) {
        this.wakeUpWord = wakeUpWord;
    }
}
