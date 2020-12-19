package com.xingfeng.opengles.chapter10.chapter103;

import android.view.View;

public class KeyThread3 extends Thread {

    View mv;
    public static boolean flag = true;
    // 表示按钮状态的常量
    public static final int Stop = 0;
    public static final int up = 1;
    public static final int down = 2;
    public static final int left = 3;
    public static final int right = 4;

    public KeyThread3(View mv) {
        this.mv = mv;
    }

    public void run() {
        while (flag) {
            if (GL103SurfaceView.rectState == up) {// 上
                GL103SurfaceView.rectY += GL103SurfaceView.moveSpan;
            }
            else if (GL103SurfaceView.rectState == down) {// 下
                GL103SurfaceView.rectY -= GL103SurfaceView.moveSpan;
            }
            else if (GL103SurfaceView.rectState == left) {// 左
                GL103SurfaceView.rectX -= GL103SurfaceView.moveSpan;
            }
            else if (GL103SurfaceView.rectState == right) {// 右
                GL103SurfaceView.rectX += GL103SurfaceView.moveSpan;
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

