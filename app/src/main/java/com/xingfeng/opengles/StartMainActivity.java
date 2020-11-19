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

public class StartMainActivity extends AppCompatActivity{

    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[] {
            Chapter3Activity.class
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main);


        mListView = findViewById(R.id.list_view);

        mListView.setAdapter(new MyBaseAdapter());

        mLayoutInflator = LayoutInflater.from(this);
    }

    private class MyBaseAdapter extends BaseAdapter {
        private String[] classDescription = new String[] {
                "Chapter3"
        };

        @Override
        public int getCount() {
            return classDescription.length;
        }

        @Override
        public String getItem(int i) {
            return classDescription[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mLayoutInflator.inflate(R.layout.class_item,parent, false);
                viewHolder.button = view.findViewById(R.id.item_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.button.setText(classDescription[position]);
            viewHolder.button.setOnClickListener(v -> {
                Intent intent = new Intent(StartMainActivity.this, classes[position]);
                StartMainActivity.this.startActivity(intent);
            });

            return view;
        }
    }

    private static class ViewHolder {
        private Button button;
    }
}
