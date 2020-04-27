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

package com.dengjia.share_screen_snapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengjia.lib_share_asr.ShareAsrService;
import com.dengjia.lib_share_rtc.CallActivity;
import com.dengjia.lib_share_usb.ShareUsb;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements ServiceConnection {

    private ViewPager qmuiViewPager;
    private View view_1;
    private View view_2;
    private View view_3;

    private List<View> viewList;

    private TextView tv_result_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置为沉浸式
        QMUIStatusBarHelper.translucent(this);

        //QMUIStatusBarHelper.setStatusBarLightMode(this);

        // 动态申请权限
        String[] perms = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要麦克风及相机使用权限", 0, perms);
        }

        qmuiViewPager = findViewById(R.id.qvp_test1);

        qmuiViewPager.arrowScroll(17);
        qmuiViewPager.setEnabled(true);

        view_1 = getLayoutInflater().inflate(R.layout.viewpager_chil_1, null);
        view_1.setBackgroundResource(R.drawable.wallpaper_a);
        view_2 = getLayoutInflater().inflate(R.layout.viewpager_chil_2, null);
        view_2.setBackgroundResource(R.drawable.wallpaper_b);
        view_3 = getLayoutInflater().inflate(R.layout.viewpager_chil_3, null);
        view_3.setBackgroundResource(R.drawable.wallpaper_c);

        viewList = new ArrayList<>();
        viewList.add(view_1);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View child = viewList.get(position);
                container.addView(child);

                return child;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }
        };

        qmuiViewPager.setAdapter(pagerAdapter);

        tv_result_show = findViewById(R.id.tv_result_show);

        Intent intent = new Intent(this, ShareAsrService.class);
        startService(intent);
        bindService(intent, this, BIND_AUTO_CREATE);

        ShareUsb shareUsb = new ShareUsb();
        shareUsb.run(this);

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ShareAsrService.AsrResultBinder asrResultBinder = (ShareAsrService.AsrResultBinder) iBinder;
        ShareAsrService shareAsrService = asrResultBinder.getService();
        shareAsrService.addAsrResultListener(new ShareAsrService.AsrResultListener() {
            @Override
            public void onGetAsrResult(String result) {
                Log.e("MainActivity", "\n语音识别结果：" + result);

                if (tv_result_show != null) {
                    tv_result_show.setText(result);
                } else {
                    tv_result_show = findViewById(R.id.tv_result_show);
                    tv_result_show.setText(result);
                }

                // 启动音视频通信
                if (result.contains("视频") && result.contains("通话")) {
                    Intent intent = new Intent(MainActivity.this, CallActivity.class);
                    startActivity(intent);
                }
                if (result.contains("结束") && result.contains("通话")) {
//                    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
//                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
//                    ComponentName componentName1 = runningTaskInfo.topActivity;
//                    Log.e("当前顶部Avtivity为：", componentName1.getClassName());
//
//                    try {
//                        Class<?> callActivity = Class.forName(componentName1.getClassName());
//                        callActivity.
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
