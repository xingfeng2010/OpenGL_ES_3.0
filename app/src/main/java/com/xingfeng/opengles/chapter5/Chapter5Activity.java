package com.xingfeng.opengles.chapter5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.R;
import com.xingfeng.opengles.StartMainActivity;
import com.xingfeng.opengles.chapter3.Chapter3Activity;

public class Chapter5Activity extends BaseListActivity {

    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[] {
            Chapter51Activity.class
    };


    private String[] classDescription = new String[] {
            "正交投影显示雪花"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
