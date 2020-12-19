package com.xingfeng.opengles.chapter10.chapter102;

import android.view.View;

public class KeyThread2 extends Thread {

    View mv;
    public static boolean flag = true;
    // 表示按钮状态的常量
    public static final int Stop = 0;
    public static final int up = 1;
    public static final int down = 2;
    public static final int left = 3;
    public static final int right = 4;

    public KeyThread2(View mv) {
        this.mv = mv;
    }

    public void run() {
        while (flag) {
            if (GL102SurfaceView.rectState == up) {// 上
                GL102SurfaceView.rectY += GL102SurfaceView.moveSpan;
            }
            else if (GL102SurfaceView.rectState == down) {// 下
                GL102SurfaceView.rectY -= GL102SurfaceView.moveSpan;
            }
            else if (GL102SurfaceView.rectState == left) {// 左
                GL102SurfaceView.rectX -= GL102SurfaceView.moveSpan;
            }
            else if (GL102SurfaceView.rectState == right) {// 右
                GL102SurfaceView.rectX += GL102SurfaceView.moveSpan;
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

