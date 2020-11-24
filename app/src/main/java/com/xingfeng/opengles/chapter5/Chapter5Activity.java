package com.xingfeng.opengles.chapter5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.xingfeng.opengles.BaseListActivity;
import com.xingfeng.opengles.chapter5.chapter510.Chapter510Activity;
import com.xingfeng.opengles.chapter5.chapter52.Chapter52Activity;
import com.xingfeng.opengles.chapter5.chapter53.Chapter53Activity;
import com.xingfeng.opengles.chapter5.chapter51.Chapter51Activity;
import com.xingfeng.opengles.chapter5.chapter56.Chapter56Activity;
import com.xingfeng.opengles.chapter5.chapter57.Chapter57Activity;
import com.xingfeng.opengles.chapter5.chapter58.Chapter58Activity;
import com.xingfeng.opengles.chapter5.chapter59.Chapter59Activity;

public class Chapter5Activity extends BaseListActivity {

    private Class[] classes = new Class[] {
            Chapter51Activity.class,
            Chapter52Activity.class,
            Chapter53Activity.class,
            Chapter56Activity.class,
            Chapter57Activity.class,
            Chapter58Activity.class,
            Chapter59Activity.class,
            Chapter510Activity.class
    };


    private String[] classDescription = new String[] {
            "正交投影显示雪花",
            "透视投影显示雪花",
            "平移变换",
            "基本图形绘制",
            "三角形绘制",
            "GL_TRIANGLE_STRIP绘制不连接版片断",
            "index结合DrawElements绘制圆",
            "layout指定属性变量索引"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData(classes,classDescription);
    }
}
