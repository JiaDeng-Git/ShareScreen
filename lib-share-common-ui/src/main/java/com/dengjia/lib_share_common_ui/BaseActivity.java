package com.dengjia.lib_share_common_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置为沉浸式
        QMUIStatusBarHelper.translucent(this);

    }
}
