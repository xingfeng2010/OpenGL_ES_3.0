package com.xingfeng.opengles.chapter7;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
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

public class Chapter7Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter71Activity.class
    };


    private String[] classDescription = new String[] {
            "纹理映射--简单案例"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
