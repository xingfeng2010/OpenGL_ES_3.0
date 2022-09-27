package com.xingfeng.opengles.chapter29;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter28.chapter281.Chapter281Activity;
import com.xingfeng.opengles.chapter28.chapter282.Chapter282Activity;
import com.xingfeng.opengles.chapter29.chapter291.Chapter291Activity;

public class Chapter29Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter291Activity.class,
    };


    private String[] classDescription = new String[] {
            "骨骼动画-简单奔跑",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
