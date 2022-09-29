package com.xingfeng.opengles.chapter212;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter212.chapter2121.Chapter2121Activity;

public class Chapter212Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter2121Activity.class,
    };


    private String[] classDescription = new String[] {
            "杂项-增强现实基础",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
