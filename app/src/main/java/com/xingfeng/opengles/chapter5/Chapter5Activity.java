package com.xingfeng.opengles.chapter5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter5.Chapter52.Chapter52Activity;
import com.xingfeng.opengles.chapter5.Chapter53.Chapter53Activity;
import com.xingfeng.opengles.chapter5.chapter51.Chapter51Activity;

public class Chapter5Activity extends BaseListActivity {

    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[] {
            Chapter51Activity.class,
            Chapter52Activity.class,
            Chapter53Activity.class
    };


    private String[] classDescription = new String[] {
            "正交投影显示雪花",
            "透视投影显示雪花",
            "平移变换"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
