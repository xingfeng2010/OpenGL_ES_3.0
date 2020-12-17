package com.xingfeng.opengles.chapter7.chapter75;

import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Moon {
    int mProgram;
    //总变换矩阵
    int muMVPMatrixHandle;
    //位置、旋转变换矩阵
    int muMMatrixHandle;
    //摄像机位置属性引用
    int muCameraHandle;
    //顶点位置属性引用
    int maPositionHandle;
    //顶点法向量属性引用
    int maNormalHandle;
    //顶点纹理坐标属性引用
    int maTexCoorHandle;
    //光源位置属性引用
    int maSunLightLocationHandle;

    String mVertexShader;//顶点着色器 代码脚本
    String mFragmentShader;//片元着色器 代码脚本

    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;
    int vCount = 0;

    public Moon(View view, float r) {
        initVertexData(r);
        initShader(view);
    }

    //初始化顶点数据的方法
    public void initVertexData(float r)
    {
        //顶点坐标数据的初始化================begin============================
        final float UNIT_SIZE=0.5f;
        ArrayList<Float> alVertix=new ArrayList<Float>();//存放顶点坐标的ArrayList
        final float angleSpan=10f;//将球进行单位切分的角度
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan)//垂直方向angleSpan度一份
        {
            for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//水平方向angleSpan度一份
            {//纵向横向各到一个角度后计算对应的此点在球面上的坐标
                double xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
                float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
                float y1=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));

                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
                float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
                float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
                float y2=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));

                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
                float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
                float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
                float y3=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));

                xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
                float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
                float y4=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));

                //构建第一三角形
                alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                //构建第二三角形
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
            }
        }
        vCount=alVertix.size()/3;//顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

        //将alVertix中的坐标值转存到一个float数组中
        float vertices[]=new float[vCount*3];
        for(int i=0;i<alVertix.size();i++)
        {
            vertices[i]=alVertix.get(i);
        }

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题

        //将alTexCoor中的纹理坐标值转存到一个float数组中
        float[] texCoor=generateTexCoor//获取切分整图的纹理数组
                (
                        (int)(360/angleSpan), //纹理图切分的列数
                        (int)(180/angleSpan)  //纹理图切分的行数
                );
        ByteBuffer llbb = ByteBuffer.allocateDirect(texCoor.length*4);
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer=llbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);
        //顶点坐标数据的初始化================end============================
    }

    public void initShader(View view) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("chapter7/chapter7.5/vertex_moon.glsl",view.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("chapter7/chapter7.5/frag_moon.glsl",view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader,mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        maNormalHandle = GLES30.glGetAttribLocation(mProgram, "aNormal");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        muCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera");
        maSunLightLocationHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
    }

    public void drawSelf(int texId) {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES30.glUniform3fv(muCameraHandle, 1, MatrixState.cameraFB);
        GLES30.glUniform3fv(maSunLightLocationHandle, 1, MatrixState.lightPositionFBSun);

        GLES30.glVertexAttribPointer(maPositionHandle, 3 ,GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        GLES30.glVertexAttribPointer(maTexCoorHandle, 2 ,GLES30.GL_FLOAT,
                false, 2 * 4, mTexCoorBuffer);
        GLES30.glVertexAttribPointer(maNormalHandle, 4 ,GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);
        GLES30.glEnableVertexAttribArray(maNormalHandle);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0, vCount);
    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw,int bh)
    {
        float[] result=new float[bw*bh*6*2];
        float sizew=1.0f/bw;//列数
        float sizeh=1.0f/bh;//行数
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
}
