package com.xingfeng.opengles.chapter27;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter27.chapter271.Chapter271Activity;
import com.xingfeng.opengles.chapter27.chapter278.Chapter278Activity;

public class Chapter27Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter271Activity.class,
            Chapter278Activity.class
    };


    private String[] classDescription = new String[] {
            "JBullet-箱子互相碰撞",
            "JBullet-汽车交通工具",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
