package com.xingfeng.opengles.chapter22;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter22.chapter221.Chapter221Activity;
import com.xingfeng.opengles.chapter22.chapter222.Chapter222Activity;
import com.xingfeng.opengles.chapter22.chapter223.Chapter223Activity;
import com.xingfeng.opengles.chapter22.chapter224.Chapter224Activity;
import com.xingfeng.opengles.chapter22.chapter225.Chapter225Activity;
import com.xingfeng.opengles.chapter22.chapter226.Chapter226Activity;

public class Chapter22Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter221Activity.class,
            Chapter222Activity.class,
            Chapter223Activity.class,
            Chapter224Activity.class,
            Chapter225Activity.class,
            Chapter226Activity.class
    };


    private String[] classDescription = new String[] {
            "飘扬的旗帜",
            "扭动的软糖",
            "风吹椰林场景开发",
            "展翅的雄鹰",
            "二维扭曲",
            "吹气膨胀效果"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
