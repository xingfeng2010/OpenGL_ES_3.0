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
import com.xingfeng.opengles.chapter7.chapter72.Chapter72Activity;
import com.xingfeng.opengles.chapter7.chapter73.Chapter73Activity;
import com.xingfeng.opengles.chapter7.chapter74.Chapter74Activity;
import com.xingfeng.opengles.chapter7.chapter75.Chapter75Activity;
import com.xingfeng.opengles.chapter7.chapter76.Chapter76Activity;

public class Chapter7Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter71Activity.class,
            Chapter72Activity.class,
            Chapter73Activity.class,
            Chapter74Activity.class,
            Chapter75Activity.class,
            Chapter76Activity.class
    };


    private String[] classDescription = new String[] {
            "纹理映射--简单案例",
            "纹理映射--色彩通道组合",
            "纹理映射--纹理拉伸",
            "纹理映射--纹理采样",
            "纹理映射--多重纹理与过程纹理",
            "纹理映射--纹理压缩"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
