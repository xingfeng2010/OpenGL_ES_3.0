package com.xingfeng.opengles.chapter10.chapter101;

import android.view.View;

public class KeyThread extends Thread {

    View mv;
    public static boolean flag = true;
    // 表示按钮状态的常量
    public static final int Stop = 0;
    public static final int up = 1;
    public static final int down = 2;
    public static final int left = 3;
    public static final int right = 4;

    public KeyThread(View mv) {
        this.mv = mv;
    }

    public void run() {
        while (flag) {
            if (GL101SurfaceView.rectState == up) {// 上
                GL101SurfaceView.rectY += GL101SurfaceView.moveSpan;
            }
            else if (GL101SurfaceView.rectState == down) {// 下
                GL101SurfaceView.rectY -= GL101SurfaceView.moveSpan;
            }
            else if (GL101SurfaceView.rectState == left) {// 左
                GL101SurfaceView.rectX -= GL101SurfaceView.moveSpan;
            }
            else if (GL101SurfaceView.rectState == right) {// 右
                GL101SurfaceView.rectX += GL101SurfaceView.moveSpan;
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

