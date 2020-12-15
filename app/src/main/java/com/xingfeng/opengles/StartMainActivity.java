package com.xingfeng.opengles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xingfeng.opengles.chapter3.Chapter3Activity;
import com.xingfeng.opengles.chapter5.Chapter5Activity;
import com.xingfeng.opengles.chapter6.Chapter6Activity;
import com.xingfeng.opengles.chapter7.Chapter7Activity;

public class StartMainActivity extends BaseListActivity{
    protected Class[] classes = new Class[] {
            Chapter3Activity.class,
            Chapter5Activity.class,
            Chapter6Activity.class,
            Chapter7Activity.class
    };

    protected String[] classDescription = new String[] {
            "OPENGL ES 3.0基础知识",
            "必知必会3D开发",
            "光照",
            "纹理映射"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }

}
