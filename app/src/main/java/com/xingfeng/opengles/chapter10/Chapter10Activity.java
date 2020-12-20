package com.xingfeng.opengles.chapter10;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter10.chapter101.Chapter101Activity;
import com.xingfeng.opengles.chapter10.chapter102.Chapter102Activity;
import com.xingfeng.opengles.chapter10.chapter103.Chapter103Activity;
import com.xingfeng.opengles.chapter10.chapter104.Chapter104Activity;
import com.xingfeng.opengles.chapter10.chapter105.Chapter105Activity;

public class Chapter10Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter101Activity.class,
            Chapter102Activity.class,
            Chapter103Activity.class,
            Chapter104Activity.class,
            Chapter105Activity.class
    };


    private String[] classDescription = new String[] {
            "混和技术--简单混合",
            "混和技术--简单混合2",
            "混和技术--ETC2",
            "混和技术--地月星系",
            "混和技术--雾"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
