package com.xingfeng.opengles.chapter10;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter10.chapter101.Chapter101Activity;
import com.xingfeng.opengles.chapter10.chapter102.Chapter102Activity;
import com.xingfeng.opengles.chapter10.chapter103.Chapter103Activity;
import com.xingfeng.opengles.chapter6.chapter61.Chapter61Activity;
import com.xingfeng.opengles.chapter6.chapter62.Chapter62Activity;
import com.xingfeng.opengles.chapter6.chapter63.Chapter63Activity;
import com.xingfeng.opengles.chapter6.chapter64.Chapter64Activity;
import com.xingfeng.opengles.chapter6.chapter65.Chapter65Activity;
import com.xingfeng.opengles.chapter6.chapter66.Chapter66Activity;
import com.xingfeng.opengles.chapter6.chapter67.Chapter67Activity;
import com.xingfeng.opengles.chapter6.chapter68.Chapter68Activity;
import com.xingfeng.opengles.chapter6.chapter69.Chapter69Activity;
import com.xingfeng.opengles.chapter7.chapter71.Chapter71Activity;
import com.xingfeng.opengles.chapter7.chapter72.Chapter72Activity;
import com.xingfeng.opengles.chapter7.chapter73.Chapter73Activity;
import com.xingfeng.opengles.chapter7.chapter74.Chapter74Activity;
import com.xingfeng.opengles.chapter7.chapter75.Chapter75Activity;
import com.xingfeng.opengles.chapter7.chapter76.Chapter76Activity;
import com.xingfeng.opengles.chapter7.chapter77.Chapter77Activity;
import com.xingfeng.opengles.chapter7.chapter78.Chapter78Activity;
import com.xingfeng.opengles.chapter7.chapter79.Chapter79Activity;

public class Chapter10Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter101Activity.class,
            Chapter102Activity.class,
            Chapter103Activity.class
    };


    private String[] classDescription = new String[] {
            "混和技术--简单混合",
            "混和技术--简单混合2",
            "混和技术--ETC2"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
