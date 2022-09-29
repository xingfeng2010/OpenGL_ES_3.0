package com.xingfeng.opengles;

import android.os.Bundle;
import android.util.Log;

import com.xingfeng.opengles.chapter10.Chapter10Activity;
import com.xingfeng.opengles.chapter20.Chapter20Activity;
import com.xingfeng.opengles.chapter211.Chapter211Activity;
import com.xingfeng.opengles.chapter212.Chapter212Activity;
import com.xingfeng.opengles.chapter22.Chapter22Activity;
import com.xingfeng.opengles.chapter23.Chapter23Activity;
import com.xingfeng.opengles.chapter27.Chapter27Activity;
import com.xingfeng.opengles.chapter28.Chapter28Activity;
import com.xingfeng.opengles.chapter28.chapter281.Chapter281Activity;
import com.xingfeng.opengles.chapter29.Chapter29Activity;
import com.xingfeng.opengles.chapter3.Chapter3Activity;
import com.xingfeng.opengles.chapter5.Chapter5Activity;
import com.xingfeng.opengles.chapter6.Chapter6Activity;
import com.xingfeng.opengles.chapter7.Chapter7Activity;

public class StartMainActivity extends BaseListActivity{
    protected Class[] classes = new Class[] {
            Chapter3Activity.class,
            Chapter5Activity.class,
            Chapter6Activity.class,
            Chapter7Activity.class,
            Chapter10Activity.class,
            Chapter20Activity.class,
            Chapter22Activity.class,
            Chapter23Activity.class,
            Chapter27Activity.class,
            Chapter28Activity.class,
            Chapter29Activity.class,
            Chapter211Activity.class,
            Chapter212Activity.class
    };

    protected String[] classDescription = new String[] {
            "OPENGL ES 3.0基础知识",
            "必知必会3D开发",
            "光照",
            "纹理映射",
            "纹理混合",
            "缓冲区对象",
            "顶点着色器",
            "片元着色器",
            "JBullet-3D物理引擎",
            "Bullet-3D物理引擎",
            "人体骨骼动画",
            "几何体实例渲染",
            "12章-杂项"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("DEBUG_TEST","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("DEBUG_TEST","onPause");
    }

}
