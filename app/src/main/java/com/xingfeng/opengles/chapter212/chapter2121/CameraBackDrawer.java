package com.xingfeng.opengles.chapter212.chapter2121;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

public class CameraBackDrawer
{
    int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount=0;

    public CameraBackDrawer(View mv, float widthHalf, float heightHalf)
    {
        //初始化顶点数据的方法
        initVertexData(widthHalf,heightHalf);
        //初始化着色器的方法
        initShader(mv);
    }

    //初始化顶点数据的方法
    public void initVertexData(float w,float h)
    {
        //顶点坐标数据的初始化================begin============================
        vCount=6;
        float vertices[]=new float[]
                {
                        -w,h,0, -w,-h,0, w,-h,0,
                        -w,h,0, w,-h,0, w,h,0
                };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点纹理坐标数据的初始化================begin============================
        float texCoor[]=new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
                {
                        0,0, 0,1, 1,1,
                        0,0, 1,1, 1,0
                };
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点纹理数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================

    }

    //初始化着色器的方法
    public void initShader(View mv)
    {
        //加载顶点着色器的脚本内容
        mVertexShader= ShaderUtil.loadFromAssetsFile("chapter1201/chapter1201.1/vertex_back.glsl", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter1201/chapter1201.1/frag_back.glsl", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(int texId)
    {
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);

        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据传入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        //将顶点纹理数据传入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2*4,
                        mTexCoorBuffer
                );

        GLES30.glEnableVertexAttribArray(maPositionHandle);//启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);//启用顶点纹理数据数组

        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texId);

        //绘制纹理矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}