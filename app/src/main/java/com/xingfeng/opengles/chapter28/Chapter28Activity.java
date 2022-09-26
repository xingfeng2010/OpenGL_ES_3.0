package com.xingfeng.opengles.chapter28;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter28.chapter281.Chapter281Activity;
import com.xingfeng.opengles.chapter28.chapter282.Chapter282Activity;

public class Chapter28Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter281Activity.class,
            Chapter282Activity.class
    };


    private String[] classDescription = new String[] {
            "Bullet-简单的物理场景",
            "Bullet-HelloWorld",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
