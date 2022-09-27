package com.xingfeng.opengles.chapter211;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter211.chapter2111.Chapter2111Activity;

public class Chapter211Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter2111Activity.class,
    };


    private String[] classDescription = new String[] {
            "几何体实例渲染-三角形简单示例",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
