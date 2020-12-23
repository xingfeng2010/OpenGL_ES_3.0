package com.xingfeng.opengles.chapter20;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter20.chapter201.Chapter201Activity;
import com.xingfeng.opengles.chapter20.chapter202.Chapter202Activity;
import com.xingfeng.opengles.chapter20.chapter203.Chapter203Activity;

public class Chapter20Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter201Activity.class,
            Chapter202Activity.class,
            Chapter203Activity.class
    };


    private String[] classDescription = new String[] {
            "缓冲区对象-顶点缓冲区-VBO",
            "缓冲区对象-顶点数组缓冲区-VAO",
            "缓冲区对象-一致缓冲区对象-UBO"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
