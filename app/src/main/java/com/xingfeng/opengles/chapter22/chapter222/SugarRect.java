package com.xingfeng.opengles.chapter22.chapter222;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.SuppressLint;
import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

//有波浪效果的纹理矩形
public class SugarRect {
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int maStartAngleHandle; //本帧起始角度属性引用
    int muHeightSpanHandle;//横向长度总跨度引用
    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount = 0;
    final float WIDTH_SPAN = 0.575f;//2.8f;//横向长度总跨度
    final float Y_MAX=1.5f;
    final float Y_MIN=-1.5f;
    final float HEIGHT_SPAN = Y_MAX - Y_MIN;//纵向长度总跨度
    int HIGH_NUMS = 6;
    private int uHeightSpanHandle;
    private int uYStartHandle;
    private int uYSpanHandle;

    float angleSpan=0;
    float angleStep=2f;

    public SugarRect(View mv) {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader(mv);
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData() {
        //顶点坐标数据的初始化================begin============================
        vCount = HIGH_NUMS *4* 6;//每个格子两个三角形，每个三角形3个顶点
        float vertices[] = new float[vCount * 3];//每个顶点xyz三个坐标
        int count = 0;//顶点计数器
        float heighSpan = HEIGHT_SPAN / HIGH_NUMS;
        for (int i = 0; i < HIGH_NUMS; i++) {
            //前面矩形
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;

            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = WIDTH_SPAN / 2;

            //后面矩形
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = -WIDTH_SPAN / 2;

            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;

            //左面矩形
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;

            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = WIDTH_SPAN / 2;

            //右面矩形
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;

            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = -WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * (i+1);
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = WIDTH_SPAN / 2;
            vertices[count++] = -HEIGHT_SPAN / 2 + heighSpan * i;
            vertices[count++] = WIDTH_SPAN / 2;
        }
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //顶点纹理坐标数据的初始化================begin============================
        float texCoor[] = generateTexCoor();
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
    }

    //初始化shader
    @SuppressLint("NewApi")
    public void initShader(View mv) {
        //加载顶点着色器的脚本内容
        String mVertexShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_VER_PATH, mv.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_FRAG_PATH, mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取程序中总扭曲角度跨度
        uHeightSpanHandle = GLES30.glGetUniformLocation(mProgram, "uHeightSpan");
    }

    @SuppressLint("NewApi")
    public void drawSelf(int texId) {
        //制定使用某套shader程序
        GLES30.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据传入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //将顶点纹理坐标数据传入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        //启用顶点位置、纹理坐标数据
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glUniform1f(uHeightSpanHandle , HEIGHT_SPAN);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor() {
        int textureCount = HIGH_NUMS *4* 6;
        float[] result = new float[textureCount * 2];
        int c = 0;
        for (int i = 0; i < HIGH_NUMS; i++) {
            //前面矩形
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;

            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 1.0f;

            //后面矩形
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;

            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 1.0f;

            //左面矩形
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;

            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 1.0f;

            //右面矩形
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;

            result[c++] = 1.0f;
            result[c++] = 1.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 0.0f;
            result[c++] = 1.0f;
        }
        return result;
    }
}
