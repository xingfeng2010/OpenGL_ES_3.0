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

public class StartMainActivity extends BaseListActivity{
    protected Class[] classes = new Class[] {
            Chapter3Activity.class,
            Chapter5Activity.class
    };

    protected String[] classDescription = new String[] {
            "Chapter3",
            "Chapter5"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }

}
