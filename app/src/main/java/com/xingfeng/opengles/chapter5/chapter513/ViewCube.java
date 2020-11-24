package com.xingfeng.opengles.chapter5.chapter513;

import android.view.View;

import com.xingfeng.opengles.util.MatrixState;

public class ViewCube {
    private final float UNIT_SIZE = 0.3f;
    //用于绘制各个面的颜色矩形
    ColorRect cr;

    public ViewCube(View mv)
    {
        //创建用于绘制各个面的颜色矩形对象
        cr=new ColorRect(mv);
    }

    public void drawSelf()
    {
        //总绘制思想：通过把一个颜色矩形旋转移位到立方体每个面的位置
        //绘制立方体的每个面

        //保护现场
        MatrixState.pushMatrix();

        //绘制前小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, UNIT_SIZE);//移到相应位置
        cr.drawSelf();//绘制矩形面
        MatrixState.popMatrix();

        //绘制后小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -UNIT_SIZE);
        MatrixState.rotate(180, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制上大面
        MatrixState.pushMatrix();
        MatrixState.translate(0,UNIT_SIZE,0);
        MatrixState.rotate(-90, 1, 0, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制下大面
        MatrixState.pushMatrix();
        MatrixState.translate(0,-UNIT_SIZE,0);
        MatrixState.rotate(90, 1, 0, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制左大面
        MatrixState.pushMatrix();
        MatrixState.translate(UNIT_SIZE,0,0);
        //rotate不要连接使用，会改变坐标轴的方向，尽量只使用一次旋转
        MatrixState.rotate(90, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制右大面
        MatrixState.pushMatrix();
        MatrixState.translate(-UNIT_SIZE,0,0);
        MatrixState.rotate(-90, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //恢复现场
        MatrixState.popMatrix();
    }
}
