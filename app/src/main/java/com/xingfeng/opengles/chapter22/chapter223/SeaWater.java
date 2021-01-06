package com.xingfeng.opengles.chapter22.chapter223;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 绘制海水
 * 能够进行波动
 */
public class SeaWater {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    //本帧起始角度属性引用
    int maStartAngleHandle;
    //横向长度总跨度引用
    int muWidthSpanHandle;

    //当前着色器索引
    int currIndex = 0;
    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;

    int vCount = 0;
    //当前帧的起始角度0~2PI
    float currStartAngle = 0;
    float texSize = 16;

    public SeaWater(int program) {
        this.mProgram = program;
        initVertexData();

        initShader();

        new Thread() {
            @Override
            public void run() {
                while (Constant.flag_go) {
                    currStartAngle+= (Math.PI / 16);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
    }

    private void initVertexData() {
        vCount=Constant.COLS*Constant.ROWS*2*3;//每个格子两个三角形，每个三角形3个顶点
        float vertices[]=new float[vCount*3];//每个顶点xyz三个坐标
        int count=0;//顶点计数器
        for(int i=0;i<Constant.ROWS;i++)//逐行扫描
        {
            for(int j=0;j<Constant.COLS;j++)//逐列扫描
            {
                //计算当前格子左上侧点坐标
                float zsx=j*Constant.WATER_SPAN;
                float zsy=0;
                float zsz=i*Constant.WATER_SPAN;
                //左上点
                vertices[count++]=zsx;
                vertices[count++]=zsy;
                vertices[count++]=zsz;
                //左下点
                vertices[count++]=zsx;
                vertices[count++]=zsy;
                vertices[count++]=zsz+Constant.WATER_SPAN;
                //右上点
                vertices[count++]=zsx+Constant.WATER_SPAN;
                vertices[count++]=zsy;
                vertices[count++]=zsz;
                //右上点
                vertices[count++]=zsx+Constant.WATER_SPAN;
                vertices[count++]=zsy;
                vertices[count++]=zsz;
                //左下点
                vertices[count++]=zsx;
                vertices[count++]=zsy;
                vertices[count++]=zsz+Constant.WATER_SPAN;
                //右下点
                vertices[count++]=zsx+Constant.WATER_SPAN;
                vertices[count++]=zsy;
                vertices[count++]=zsz+Constant.WATER_SPAN;
            }
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        float texCoor[]=generateTexCoor(Constant.COLS,Constant.ROWS);
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
    }

    private float[] generateTexCoor(int bw, int bh) {
        float[] result=new float[bw*bh*6*2];
        float sizew=texSize/bw;//列数
        float sizeh=texSize/bh;//行数
        int c=0;
        for(int i=0;i<bh;i++)
        {
            for(int j=0;j<bw;j++)
            {
                //每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
                float s=j*sizew;
                float t=i*sizeh;

                result[c++]=s;
                result[c++]=t;

                result[c++]=s;
                result[c++]=t+sizeh;

                result[c++]=s+sizew;
                result[c++]=t;


                result[c++]=s+sizew;
                result[c++]=t;

                result[c++]=s;
                result[c++]=t+sizeh;

                result[c++]=s+sizew;
                result[c++]=t+sizeh;
            }
        }
        return result;
    }

    private void initShader() {
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取本帧起始角度属性引用
        maStartAngleHandle =GLES30.glGetUniformLocation(mProgram, "uStartAngle");
        //获取横向长度总跨度引用
        muWidthSpanHandle =GLES30.glGetUniformLocation(mProgram, "uWidthSpan");
    }

    public void drawSelf(int texId) {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle,1,false, MatrixState.getFinalMatrix(), 0);
        GLES30.glUniform1f(maStartAngleHandle, currStartAngle);
        GLES30.glUniform1f(muWidthSpanHandle, Constant.UNIT_SIZE*31*3);
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3*4,
                mVertexBuffer
        );

        GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2*4, mTexCoorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        GLES30.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,vCount);
    }
}
