package com.xingfeng.opengles.chapter6;

import android.os.Bundle;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter5.chapter51.Chapter51Activity;
import com.xingfeng.opengles.chapter5.chapter510.Chapter510Activity;
import com.xingfeng.opengles.chapter5.chapter512.Chapter512Activity;
import com.xingfeng.opengles.chapter5.chapter513.Chapter513Activity;
import com.xingfeng.opengles.chapter5.chapter514.Chapter514Activity;
import com.xingfeng.opengles.chapter5.chapter515.Chapter515Activity;
import com.xingfeng.opengles.chapter5.chapter516.Chapter516Activity;
import com.xingfeng.opengles.chapter5.chapter52.Chapter52Activity;
import com.xingfeng.opengles.chapter5.chapter53.Chapter53Activity;
import com.xingfeng.opengles.chapter5.chapter56.Chapter56Activity;
import com.xingfeng.opengles.chapter5.chapter57.Chapter57Activity;
import com.xingfeng.opengles.chapter5.chapter58.Chapter58Activity;
import com.xingfeng.opengles.chapter5.chapter59.Chapter59Activity;
import com.xingfeng.opengles.chapter6.chapter61.Chapter61Activity;
import com.xingfeng.opengles.chapter6.chapter62.Chapter62Activity;
import com.xingfeng.opengles.chapter6.chapter63.Chapter63Activity;
import com.xingfeng.opengles.chapter6.chapter64.Chapter64Activity;
import com.xingfeng.opengles.chapter6.chapter65.Chapter65Activity;
import com.xingfeng.opengles.chapter6.chapter66.Chapter66Activity;
import com.xingfeng.opengles.chapter6.chapter67.Chapter67Activity;
import com.xingfeng.opengles.chapter6.chapter68.Chapter68Activity;

public class Chapter6Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter61Activity.class,
            Chapter62Activity.class,
            Chapter63Activity.class,
            Chapter64Activity.class,
            Chapter65Activity.class,
            Chapter66Activity.class,
            Chapter67Activity.class,
            Chapter68Activity.class
    };


    private String[] classDescription = new String[] {
            "曲面物体的构建",
            "基本光照-环境光",
            "基本光照-散射光",
            "基本光照-镜面光",
            "基本光照-三种光叠加",
            "光照-定向光",
            "光照-点法向量",
            "光照-面法向量"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
