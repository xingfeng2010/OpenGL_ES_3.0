package com.xingfeng.opengles.chapter3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;


public class Chapter3Activity extends AppCompatActivity {

    MyTDView mview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mview=new MyTDView(this);//¥¥Ω®MyTDView¿‡µƒ∂‘œÛ
        mview.requestFocus();//ªÒ»°Ωπµ„
        mview.setFocusableInTouchMode(true);//…Ë÷√Œ™ø…¥•øÿ
        setContentView(mview);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mview.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mview.onPause();
    }
}
