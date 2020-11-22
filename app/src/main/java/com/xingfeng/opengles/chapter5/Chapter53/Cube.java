package com.xingfeng.opengles.chapter5.Chapter53;

import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    String mVertexShader;
    String mFragmentShader;

    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;
    int vCount = 0;

    public Cube(View mv)
    {
        initVertexData();
        initShader(mv);
    }

    public void initVertexData() {
        vCount = 12 * 6;

        float vertices[] = new float[] {
                //前面
                0,0, Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                0,0,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                0,0,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                0,0,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                //后面
                0,0,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,0,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,0,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,0,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                //左面
                -Constant.UNIT_SIZE,0,0,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,0,0,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,0,0,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,0,0,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                //右面
                Constant.UNIT_SIZE,0,0,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,0,0,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,0,0,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,0,0,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                //上面
                0,Constant.UNIT_SIZE,0,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,Constant.UNIT_SIZE,0,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                0,Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                //下面
                0,-Constant.UNIT_SIZE,0,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                0,-Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,-Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                0,-Constant.UNIT_SIZE,0,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,-Constant.UNIT_SIZE,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,Constant.UNIT_SIZE,
        };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        mVertexBuffer = ByteBuffer.allocateDirect(vertices.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点颜色值数组，每个顶点4个色彩值RGBA
        float colors[]=new float[]{
                //前面
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                //后面
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                //左面
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                //右面
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                //上面
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                //下面
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
        };

        mColorBuffer = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void initShader(View mv) {
        //加载顶点着色器的脚本内容
        mVertexShader= ShaderUtil.loadFromAssetsFile("chapter5/chapter5.1/vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter5/chapter5.1/frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        //制定使用某套shader程序
        GLES30.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //为画笔指定顶点着色数据
        GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false, 4*4,mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
