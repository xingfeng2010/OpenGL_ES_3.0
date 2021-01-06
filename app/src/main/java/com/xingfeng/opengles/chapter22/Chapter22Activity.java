package com.xingfeng.opengles.chapter22;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter20.chapter201.Chapter201Activity;
import com.xingfeng.opengles.chapter20.chapter202.Chapter202Activity;
import com.xingfeng.opengles.chapter20.chapter203.Chapter203Activity;
import com.xingfeng.opengles.chapter20.chapter204.Chapter204Activity;
import com.xingfeng.opengles.chapter20.chapter205.Chapter205Activity;
import com.xingfeng.opengles.chapter20.chapter206.Chapter206Activity;
import com.xingfeng.opengles.chapter22.chapter221.Chapter221Activity;
import com.xingfeng.opengles.chapter22.chapter222.Chapter222Activity;
import com.xingfeng.opengles.chapter22.chapter223.Chapter223Activity;

public class Chapter22Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter221Activity.class,
            Chapter222Activity.class,
            Chapter223Activity.class
    };


    private String[] classDescription = new String[] {
            "飘扬的旗帜",
            "扭动的软糖",
            "风吹椰林场景开发"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
