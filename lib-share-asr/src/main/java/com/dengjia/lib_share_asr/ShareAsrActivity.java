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

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dengjia.lib_share_asr.grammer.Action;
import com.dengjia.lib_share_asr.grammer.Device;
import com.dengjia.lib_share_asr.grammer.Place;
import com.dengjia.lib_share_asr.grammer.Wakeup;

import org.json.JSONException;
import org.json.JSONObject;
import org.kaldi.Assets;
import org.kaldi.KaldiRecognizer;
import org.kaldi.Model;
import org.kaldi.RecognitionListener;
import org.kaldi.SpeechRecognizer;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ShareAsrActivity extends Activity implements RecognitionListener {
    @Override
    protected void onResume() {
        super.onResume();

    }

    static {
        System.loadLibrary("kaldi_jni");
    }

    private static HashMap<Integer, Integer> rawHash = new HashMap<>();

    static private final int STATE_START = 0;
    static private final int STATE_READY = 1;
    static private final int STATE_FILE = 2;
    static private final int STATE_MIC  = 3;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private Model model;
    private SpeechRecognizer recognizer;
    TextView resultView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.main);

        // Setup layout
        resultView = findViewById(R.id.result_text);
        setUiState(STATE_START);

        findViewById(R.id.recognize_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizeFile();
            }
        });

        findViewById(R.id.recognize_mic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizeMicrophone();
            }
        });

        rawHash.put(1, R.raw.tone_wake_1);
        rawHash.put(2, R.raw.tone_wake_2);
        rawHash.put(3, R.raw.tone_wake_3);
        rawHash.put(4, R.raw.tone_wake_4);
        rawHash.put(5, R.raw.tone_wake_5);
        rawHash.put(6, R.raw.tone_wake_6);
        rawHash.put(7, R.raw.tone_wake_7);
        rawHash.put(8, R.raw.tone_wake_8);
        rawHash.put(9, R.raw.tone_wake_9);

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }

        // 开启异步任务执行语音识别
        new SetupTask(this).execute();
    }

    // 加载语音识别模型Models的Task
    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<ShareAsrActivity> activityReference;

        SetupTask(ShareAsrActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                //Assets assets = new Assets(activityReference.get(), "");
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                Log.d("!!!!", assetDir.toString());
                activityReference.get().model = new Model(assetDir.toString() + "/model-android");
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                activityReference.get().setErrorState(String.format(activityReference.get().getString(R.string.failed), result));
                Log.e("SetupTask：", "SetupTask:我被执行了！！！！！");
            } else {
                Log.e("SetupTask：", "SetupTask:我被执行了！！！！！");
                activityReference.get().setUiState(STATE_READY);
            }
        }
    }

    // 从文件识别并直接在Task中执行完识别过程
    private static class RecognizeTask extends AsyncTask<Void, Void, String> {
        WeakReference<ShareAsrActivity> activityReference;
        WeakReference<TextView> resultView;

        RecognizeTask(ShareAsrActivity activity, TextView resultView) {
            this.activityReference = new WeakReference<>(activity);
            this.resultView = new WeakReference<>(resultView);
        }

        @Override
        protected String doInBackground(Void... params) {
            KaldiRecognizer rec;
            long startTime = System.currentTimeMillis();
            StringBuilder result = new StringBuilder();
            try {
                rec = new KaldiRecognizer(activityReference.get().model, 16000.f);
                // TODO 实现读取音频文件
                InputStream ais = activityReference.get().getAssets().open("bootaudio.wav");
//                InputStream ais = activityReference.get().getAssets().open("10001-90210-01803.wav");
                if (ais.skip(44) != 44) {
                    return "";
                }
                byte[] b = new byte[4096];
                int nbytes;
                while ((nbytes = ais.read(b)) >= 0) {
                    if (rec.AcceptWaveform(b, nbytes)) {
                        result.append(rec.Result());
                    } else {
                        result.append(rec.PartialResult());
                    }
                }
                result.append(rec.FinalResult());
            } catch (IOException e) {
                return "";
            }

            // TODO 此处可以获得音频文件的识别结果
            Log.e("识别结果", result.toString());
            return String.format(activityReference.get().getString(R.string.elapsed), result.toString(), (System.currentTimeMillis() - startTime));
        }

        @Override
        protected void onPostExecute(String result) {
            activityReference.get().setUiState(STATE_READY);
            resultView.get().append(result + "\n");
        }
    }

    // 权限检查回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    // Activity销毁函数
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * 识别结束后调用
     * 1. json；记录分词结果
     * 2. text：记录最终的识别结果字符串
     * @param hypothesis
     */
    @Override
    public void onResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
        // 这里是获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        Log.e("识别结果", "\n" + simpleDateFormat.format(date) + "\n onResult：\n" + hypothesis);
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
                    if (result.contains(Wakeup.WAKEUP.getWakeUpWord())) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(this, rawHash.get((int)(Math.random()*9+1)));
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
                        Boolean flag_1 = false;
                        Boolean flag_2 = false;
                        Boolean flag_3 = false;

                        // 依次检测三个语槽
                        for (Action action : Action.values()) {
                            if (result.contains(action.getAction())) {
                                flag_1 = true;
                            }
                        }
                        for (Place place : Place.values()) {
                            if (result.contains(place.getPlace())) {
                                flag_2 = true;
                            }
                        }
                        for (Device device : Device.values()) {
                            if (result.contains(device.getDevice())) {
                                flag_3 = true;
                            }
                        }

                        if (flag_1 && flag_2 && flag_3) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.action_done);
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
                }
            }

        }
    }

    /**
     * 当部分识别结果可用时调用
     * @param hypothesis
     */
    @Override
    public void onPartialResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
        // Log.e("识别结果", "\nonPartialResult：\n" + hypothesis);
    }

    // 发生错误时调用
    @Override
    public void onError(Exception e) {
        setErrorState(e.getMessage());
    }

    // 超时后调用
    @Override
    public void onTimeout() {
        recognizer.cancel();
        recognizer = null;
        setUiState(STATE_READY);
    }

    // 设置UI函数
    private void setUiState(int state) {
        switch (state) {
            case STATE_START:
                resultView.setText(R.string.preparing);
                findViewById(R.id.recognize_file).setEnabled(false);
                findViewById(R.id.recognize_mic).setEnabled(false);
                break;
            case STATE_READY:
                resultView.setText(R.string.ready);
                ((Button) findViewById(R.id.recognize_mic)).setText(R.string.recognize_microphone);
                findViewById(R.id.recognize_file).setEnabled(true);
                findViewById(R.id.recognize_mic).setEnabled(true);
                break;
            case STATE_FILE:
                resultView.append(getString(R.string.starting));
                findViewById(R.id.recognize_mic).setEnabled(false);
                findViewById(R.id.recognize_file).setEnabled(false);
                break;
            case STATE_MIC:
                ((Button) findViewById(R.id.recognize_mic)).setText(R.string.stop_microphone);
                findViewById(R.id.recognize_file).setEnabled(false);
                findViewById(R.id.recognize_mic).setEnabled(true);
                break;
        }
    }

    // 处理识别错误输出函数
    private void setErrorState(String message) {
        resultView.setText(message);
        ((Button) findViewById(R.id.recognize_mic)).setText(R.string.recognize_microphone);
        findViewById(R.id.recognize_file).setEnabled(false);
        findViewById(R.id.recognize_mic).setEnabled(false);
    }

    // 文件识别模式函数
    public void recognizeFile() {
        setUiState(STATE_FILE);
        new RecognizeTask(this, resultView).execute();
    }

    // 普通识别模式函数
    public void recognizeMicrophone() {
        if (recognizer != null) {
            setUiState(STATE_READY);
            recognizer.cancel();
            recognizer = null;
        } else {
            setUiState(STATE_MIC);
            try {
                recognizer = new SpeechRecognizer(model);
                recognizer.addListener(this);
                recognizer.startListening();
            } catch (IOException e) {
                setErrorState(e.getMessage());
            }
        }
    }

}
