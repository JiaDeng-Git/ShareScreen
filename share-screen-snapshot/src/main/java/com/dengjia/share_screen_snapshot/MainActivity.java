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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengjia.lib_share_asr.ShareAsrService;

import com.dengjia.lib_share_rtc.CallActivity;
import com.dengjia.lib_share_rtc.ContactListActivity;
import com.dengjia.lib_share_usb.ShareUsb;
import com.dengjia.lib_share_weather.MiuiWeatherView;
import com.dengjia.lib_share_weather.Weather;
import com.dengjia.lib_share_weather.WeatherManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements ServiceConnection {

    private PagerAdapter pagerAdapter;
    private ViewPager vp_banner;
    private View view_1;
    private View view_2;
    private View view_3;

    private List<View> viewList;

    private TextView tv_result_show;

    private ShareUsb shareUsb;

    // 测试天气布局
    private MiuiWeatherView weatherView;
    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_release);

        // 动态申请权限
        String[] perms = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要麦克风及相机使用权限", 0, perms);
        }

        // 页面主体ViewPager
        vp_banner = findViewById(R.id.vp_banner);
        vp_banner.arrowScroll(17);
        vp_banner.setEnabled(true);

        view_1 = getLayoutInflater().inflate(R.layout.viewpager_chil_1, null);
        view_2 = getLayoutInflater().inflate(R.layout.viewpager_chil_2, null);
        view_3 = getLayoutInflater().inflate(R.layout.viewpager_chil_3, null);

        viewList = new ArrayList<>();
        viewList.add(view_1);
        viewList.add(view_2);
        viewList.add(view_3);

        pagerAdapter = new PagerAdapter() {
            private View mCurrentChildView;

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

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                Log.e("setPrimaryItem()", "当前View的position为: "+ position);
                mCurrentChildView = (View) object;
            }
        };

        vp_banner.setAdapter(pagerAdapter);

        shareUsb = new ShareUsb();
        shareUsb.run(this);
        shareUsb.sendData("Phone's Data");

        shareUsb.addReceiveDataListener(new ShareUsb.ReceiveDataListener() {
            @Override
            public void receiveData(String data, int dataLength) {
                System.out.println("收到数据：" + data);
            }
        });

        // 天气布局
        weatherView = view_2.findViewById(R.id.weather);
        weatherManager = new WeatherManager(this, false);
        WeatherManager.WeatherDataListener weatherDataListener = new WeatherManager.WeatherDataListener() {
            @Override
            public void onWeatherDataReceived(List<Weather> weathers) {
                weatherView.setData(weathers);
            }
        };
        weatherManager.addWeatherDataListener(weatherDataListener);

    }


    @Override
    protected void onResume() {
        super.onResume();

        List<Integer> imageList = new ArrayList<>();

        imageList.add(R.drawable.wallpaper_a);
        imageList.add(R.drawable.wallpaper_b);
        imageList.add(R.drawable.wallpaper_c);

        Intent intent = new Intent(this, ShareAsrService.class);
        startService(intent);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ShareAsrService.AsrResultBinder asrResultBinder = (ShareAsrService.AsrResultBinder) iBinder;
        ShareAsrService shareAsrService = asrResultBinder.getService();
        shareAsrService.addAsrResultListener(new ShareAsrService.AsrResultListener() {
            @Override
            public void onGetAsrResult(String result) {
                Log.e("MainActivity", "\n语音识别结果：" + result);

                if ( result.contains("打开") && result.contains("房间") && result.contains("台灯")){
                    shareUsb.sendData("open");
                }
                if ( result.contains("关闭") && result.contains("房间") && result.contains("台灯")){
                    shareUsb.sendData("close");
                }

                tv_result_show = findViewById(R.id.tv_asr_result);
                tv_result_show.setText(result);

                // 启动音视频通信
                if (result.contains("视频") && result.contains("通话")) {
                    Intent intent = new Intent(MainActivity.this, CallActivity.class);
                    startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareUsb.end();
    }
}
