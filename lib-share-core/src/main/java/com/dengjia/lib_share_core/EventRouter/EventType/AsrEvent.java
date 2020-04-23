package com.dengjia.lib_share_core.EventRouter.EventType;

public enum AsrEvent {

    // 语音识别初始化完成
    INIT_DONE("asr_initDone"),

    // 语音识别中间结果
    PARTIAL_RESULT("asr_partialResult"),

    // 语音识别最终结果
    FINAL_RESULT("asr_finalResult"),

    // 语音识别发生错误
    ERROR("asr_error"),

    // 语音识别超时
    TIME_OUT("asr_timeout");

    private String event;

    private AsrEvent(String event){
        this.event = event;
    }

    public String getEvent(){
        return event;
    }
}
