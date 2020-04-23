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

package com.dengjia.lib_share_asr;

import android.content.Context;
import android.media.MediaPlayer;

import android.util.Log;

import com.dengjia.lib_share_asr.asr_enum.AsrModel;
import com.dengjia.lib_share_asr.asr_skill.BaseSkill;
import com.dengjia.lib_share_asr.grammer.Wakeup;
import com.dengjia.lib_share_asr.utils.JudgeAllSkillsUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.kaldi.Assets;
import org.kaldi.Model;
import org.kaldi.RecognitionListener;
import org.kaldi.SpeechRecognizer;

import java.io.File;

import java.io.IOException;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShareAsr implements RecognitionListener {

    private static final String TAG = "AsrTest";

    static {
        // 加载libkaldi_jni.so资源
        System.loadLibrary("kaldi_jni");
    }

    // 声学及语言模型
    private Model model;
    // 拾音器
    private SpeechRecognizer recognizer;

    private AsrModel asrModel;
    private List<BaseSkill> skillsList = new ArrayList<>();

    private Context context;
    // 构造函数，这里传入context是关键
    public ShareAsr(Context context, AsrModel asrModel, List<BaseSkill> skillsList) {
        this.context = context;
        this.asrModel = asrModel;
        this.skillsList = skillsList;
        run();
    }

    // 运行ShareAsr
    public void run(){

        LoadModelsThread loadModelsThread = new LoadModelsThread(context, this);
        loadModelsThread.run();

        recognizeMicrophone();
    }

    // 在线程中加载声学及语言模型
    private class LoadModelsThread extends Thread{
        WeakReference<Context> contextReference;
        WeakReference<ShareAsr> asrTestReference;
        LoadModelsThread(Context context, ShareAsr shareAsr){
            contextReference = new WeakReference<>(context);
            asrTestReference = new WeakReference<>(shareAsr);
        }

        @Override
        public void run() {
            try {
                Assets assets = new Assets(contextReference.get());
                File assetDir = assets.syncAssets();
                Log.e(TAG, "查看是否获取到了Models" + assetDir.toString());
                asrTestReference.get().model = new Model(assetDir.toString() + "/model-android");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 从麦克风拾音，进行语音识别
    public void recognizeMicrophone() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
        } else {
            try {
                recognizer = new SpeechRecognizer(model);
                recognizer.addListener(this);
                recognizer.startListening();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onPartialResult(String hypothesis) {

    }

    /**
     * 得到最终识别结果回调函数
     * @param hypothesis
     */
    @Override
    public void onResult(String hypothesis) {

        // 设置时间格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        // 获取当前时间
        Date date = new Date(System.currentTimeMillis());

        // Log.e(TAG, "\n" + simpleDateFormat.format(date) + "\n onResult：\n" + hypothesis);

        if (hypothesis != null) {
            JSONObject object = null;
            try {
                object = new JSONObject(hypothesis);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = null;
            try {
                result = object.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                // 这里是拿到识别结果后的处理逻辑
                if (result != null) {
                    // 唤醒词唤醒后，语音回应
                    if (result.contains(Wakeup.WAKEUP.getWakeUpWord())) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, WakeupReplyHolder.getInstance().getReplyHash().get((int)(Math.random() * WakeupReplyHolder.getInstance().getReplyHash().size() + 1)));
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.release();
                                }
                            });
                        }
                    }else {
                        if (JudgeAllSkillsUtil.judgeAllSkillsUtil(result, skillsList)) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.action_done);
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mediaPlayer.release();
                                    }
                                });
                            }
                        }
                    }
                    // 向监听者传递事件与数据
                    for (AsrResultListener listener : mAsrResultListener) {
                        listener.onAsrResult(result);
                    }
                }
            }

        }
    }

    /**
     * 识别出错回调函数
     * @param error
     */
    @Override
    public void onError(Exception error) {
        Log.e(TAG, "识别发生错误！！！" + error.toString());
    }

    /**
     * 识别超时回调函数
     */
    @Override
    public void onTimeout() {
        Log.e(TAG, "识别超时！！！");
    }

    // 监听语音识别结果者
    private List<AsrResultListener> mAsrResultListener = new ArrayList<>();
    // 添加监听者
    public void addAsrResultListener(AsrResultListener listener){
        mAsrResultListener.add(listener);
    }
    // 内部接口类，使用监听者模式，解耦。传递事件和数据。
    public interface AsrResultListener{
        void onAsrResult(String result);
    }

}
