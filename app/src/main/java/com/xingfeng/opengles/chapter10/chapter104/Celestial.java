package com.xingfeng.opengles.chapter10.chapter104;

import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Celestial {
    final float UNIT_SIZE = 10.0f;
    private FloatBuffer mVertexBuffer;
    //星星数量
    int vCount = 0;
    float yAngle;
    //星星尺寸
    float scale;
    String mVertexShader;
    String mFragmentShader;

    int mProgram;
    int muMVPMatrixHandle;
    //顶点位置属性引用
    int maPositionHandle;
    //顶点尺寸参数引用
    int uPointSizeHandle;

    public Celestial(float scale, float yAngle, int vCount, View view) {
        this.yAngle = yAngle;
        this.scale = scale;
        this.vCount = vCount;
        initVertexData();
        initShader(view);
    }

    public void initVertexData() {
        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < vCount; i ++) {
            double angleTempJD = Math.PI * 2 * Math.random();
            double angleTempWD = Math.PI * 2 * (Math.random() - 0.5f);

            vertices[i * 3] = (float)(UNIT_SIZE * Math.cos(angleTempWD) * Math.sin(angleTempJD));
            vertices[i * 3 + 1] = (float)(UNIT_SIZE * Math.sin(angleTempWD));
            vertices[i * 3 + 2] = (float)(UNIT_SIZE * Math.cos(angleTempWD) * Math.cos(angleTempJD));
        }

        mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void initShader(View view) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("chapter10/chapter10.4/vertex_xk.sh", view.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("chapter10/chapter10.4/frag_xk.sh", view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        uPointSizeHandle = GLES30.glGetUniformLocation(mProgram, "uPointSize");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glUniform1f(uPointSizeHandle, scale);

        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glDrawArrays(GLES30.GL_POINTS,0, vCount);
    }
}
