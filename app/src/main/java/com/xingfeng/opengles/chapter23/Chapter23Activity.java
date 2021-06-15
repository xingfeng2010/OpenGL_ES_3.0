package com.xingfeng.opengles.chapter23;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter23.chapter231.Chapter231Activity;
import com.xingfeng.opengles.chapter23.chapter232.Chapter232Activity;
import com.xingfeng.opengles.chapter23.chapter233.Chapter233Activity;

public class Chapter23Activity extends BaseListActivity {
    Class[] classes = new Class[]{
            Chapter231Activity.class,
            Chapter232Activity.class,
            Chapter233Activity.class,
    };

    String[] classDescription = new String[]{
            "砖块着色器",
            "沙滩排球",
            "数字图像处理"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes, classDescription);
    }


}