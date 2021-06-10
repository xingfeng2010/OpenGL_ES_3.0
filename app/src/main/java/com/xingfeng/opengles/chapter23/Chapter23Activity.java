package com.xingfeng.opengles.chapter23;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter23.chapter231.Chapter231Activity;
import com.xingfeng.opengles.chapter23.chapter232.Chapter232Activity;

public class Chapter23Activity extends BaseListActivity {
    Class[] classes = new Class[]{
            Chapter231Activity.class,
            Chapter232Activity.class
    };

    String[] classDescription = new String[]{
            "砖块着色器",
            "沙滩排球"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes, classDescription);
    }
}