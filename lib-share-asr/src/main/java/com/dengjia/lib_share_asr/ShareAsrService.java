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
package com.dengjia.lib_share_asr;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dengjia.lib_share_asr.asr_enum.Wakeup;
import com.dengjia.lib_share_asr.asr_skill.BaseSkill;
import com.dengjia.lib_share_asr.asr_skill.av_call_skill.AvCallSkill;
import com.dengjia.lib_share_asr.asr_skill.device_control_skill.DeviceControlSkill;
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

/**
 * ShareASR功能开启基本流程：
 *
 * 1. 加载.so资源
 * 2. 在线程中加载models
 * 3. 加载唤醒词回复资源
 * 4. 加载Skills资源
 * 5. 运行拾音器
 *
 */
public class ShareAsrService extends Service implements RecognitionListener {

    private static final String TAG = "ShareAsrService";

    private List<BaseSkill> skillsList = new ArrayList<>();

    private Model model;
    private SpeechRecognizer recognizer;

    static {
        System.loadLibrary("kaldi_jni");
    }

    public ShareAsrService() {
    }

    @Override
    public void onPartialResult(String hypothesis) {
        // Log.e(TAG, hypothesis + "\n");
    }

    @Override
    public void onResult(String hypothesis) {
        Log.e(TAG, hypothesis + "\n");
        // 这里是获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

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
                Log.e(TAG, "查看JSON转换后的结果：" + result + "\n");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // 这里是拿到识别结果后的处理逻辑
                if (result != null) {
                    if (result.contains(Wakeup.WAKEUP.getWakeUpWord())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), WakeupReplyHolder.getInstance().getReplyHash().get((int) (Math.random() * WakeupReplyHolder.getInstance().getReplyHash().size() + 1)));
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
                        }).start();

                        Log.e(TAG, "查看唤醒词识别Flag：true \n");
                        // Log.e(TAG, "识别结果\n" + simpleDateFormat.format(date) + "\n onResult：\n" + hypothesis);
                        asrResultListener.onGetAsrResult(result);
                    } else {
                        boolean flag = JudgeAllSkillsUtil.judgeAllSkillsUtil(result, skillsList);
                        if (flag) {
                            // Log.e(TAG, "识别结果\n" + simpleDateFormat.format(date) + "\n onResult：\n" + hypothesis);
                            asrResultListener.onGetAsrResult(result);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.action_done);
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
                            }).start();

                        }
                    }
                }
            }

        }
    }

    @Override
    public void onError(Exception error) {
        Log.d(TAG, "onError: " + error);
    }

    @Override
    public void onTimeout() {
        recognizer.cancel();
        recognizer = null;
    }

    private class LoadModels extends Thread {
        WeakReference<ShareAsrService> serviceReference;

        LoadModels(ShareAsrService service) {
            serviceReference = new WeakReference<>(service);
        }

        @Override
        public void run() {
            try {
                Assets assets = new Assets(serviceReference.get());
                File assetDir = assets.syncAssets();
                Log.e(TAG, "查看是否获取到了Models" + assetDir.toString());
                serviceReference.get().model = new Model(assetDir.toString() + "/model-android");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 开启语音识别
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
    public IBinder onBind(Intent intent) {
        return new AsrResultBinder();
    }

    public class AsrResultBinder extends Binder {
        public ShareAsrService getService() {
            return ShareAsrService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 在线程中加载声学与语言模型
        LoadModels loadModels = new LoadModels(this);
        loadModels.run();

        // 添加所需语音技能
        skillsList = new ArrayList<>();
        // 设备控制技能
        skillsList.add(DeviceControlSkill.getInstance());
        // 跳转到音视频通信技能
        skillsList.add(AvCallSkill.getInstance());

        // 开始语音识别
        recognizeMicrophone();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "我被执行了！onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service!销毁");
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }

        Intent intent = new Intent(this, ShareAsrService.class);
        startService(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "内存不足！！！");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public AsrResultListener asrResultListener;

    public void addAsrResultListener(AsrResultListener asrResultListener) {
        this.asrResultListener = asrResultListener;
    }

    public interface AsrResultListener {
        void onGetAsrResult(String result);
    }


}
