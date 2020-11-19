package com.xingfeng.opengles.chapter3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.annotation.SuppressLint;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.xingfeng.opengles.util.ShaderUtil;

public class Triangle
{
    public static float[] mProjMatrix = new float[16];//4x4 Õ∂”∞æÿ’Û
    public static float[] mVMatrix = new float[16];//…„œÒª˙Œª÷√≥ØœÚµƒ≤Œ ˝æÿ’Û
    public static float[] mMVPMatrix;//◊Ó∫Û∆◊˜”√µƒ◊‹±‰ªªæÿ’Û

    int mProgram;//◊‘∂®“Â‰÷»æπ‹œﬂ≥Ã–Úid
    int muMVPMatrixHandle;//◊‹±‰ªªæÿ’Û“˝”√
    int maPositionHandle; //∂•µ„Œª÷√ Ù–‘“˝”√
    int maColorHandle; //∂•µ„—’…´ Ù–‘“˝”√
    String mVertexShader;//∂•µ„◊≈…´∆˜¥˙¬ÎΩ≈±æ
    String mFragmentShader;//∆¨‘™◊≈…´∆˜¥˙¬ÎΩ≈±æ
    static float[] mMMatrix = new float[16];//æﬂÃÂŒÔÃÂµƒ“∆∂Ø–˝◊™æÿ’Û£¨∞¸¿®–˝◊™°¢∆Ω“∆°¢Àı∑≈

    FloatBuffer   mVertexBuffer;//∂•µ„◊¯±Í ˝æ›ª∫≥Â
    FloatBuffer   mColorBuffer;//∂•µ„◊≈…´ ˝æ›ª∫≥Â
    public int vCount=0;
    public float xAngle=0;//»∆x÷·–˝◊™µƒΩ«∂»
    public Triangle(MyTDView mv)
    {
        //µ˜”√≥ı ºªØ∂•µ„ ˝æ›µƒinitVertexData∑Ω∑®
        initVertexData();
        //µ˜”√≥ı ºªØ◊≈…´∆˜µƒintShader∑Ω∑®
        initShader(mv);
    }

    public void initVertexData()//≥ı ºªØ∂•µ„ ˝æ›µƒ∑Ω∑®
    {
        //∂•µ„◊¯±Í ˝æ›µƒ≥ı ºªØ
        vCount=3;
        final float UNIT_SIZE=0.2f;
        float vertices[]=new float[]//∂•µ„◊¯±Í ˝◊È
                {
                        -4*UNIT_SIZE,0,0,
                        0,-4*UNIT_SIZE,0,
                        4*UNIT_SIZE,0,0,
                };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//…Ë÷√◊÷Ω⁄À≥–ÚŒ™±æµÿ≤Ÿ◊˜œµÕ≥À≥–Ú
        mVertexBuffer = vbb.asFloatBuffer();//◊™ªªŒ™∏°µ„(Float)–Õª∫≥Â
        mVertexBuffer.put(vertices);//‘⁄ª∫≥Â«¯ƒ⁄–¥»Î ˝æ›
        mVertexBuffer.position(0);//…Ë÷√ª∫≥Â«¯∆ ºŒª÷√

        float colors[]=new float[]//∂•µ„—’…´ ˝◊È
                {
                        1,1,1,0,//∞◊…´
                        0,0,1,0,//¿∂
                        0,1,0,0//¬Ã
                };

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//…Ë÷√◊÷Ω⁄À≥–ÚŒ™±æµÿ≤Ÿ◊˜œµÕ≥À≥–Ú
        mColorBuffer = cbb.asFloatBuffer();//◊™ªªŒ™∏°µ„(Float)–Õª∫≥Â
        mColorBuffer.put(colors);//‘⁄ª∫≥Â«¯ƒ⁄–¥»Î ˝æ›
        mColorBuffer.position(0);//…Ë÷√ª∫≥Â«¯∆ ºŒª÷√
    }

    //≥ı ºªØ◊≈…´∆˜µƒ∑Ω∑®
    @SuppressLint("NewApi")
    public void initShader(MyTDView mv)
    {
        //º”‘ÿ∂•µ„◊≈…´∆˜µƒΩ≈±æƒ⁄»›
        mVertexShader= ShaderUtil.loadFromAssetsFile("chapter3/vertex.glsl", mv.getResources());
        //º”‘ÿ∆¨‘™◊≈…´∆˜µƒΩ≈±æƒ⁄»›
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter3/frag.glsl", mv.getResources());
        //ª˘”⁄∂•µ„◊≈…´∆˜”Î∆¨‘™◊≈…´∆˜¥¥Ω®≥Ã–Ú
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //ªÒ»°≥Ã–Ú÷–∂•µ„Œª÷√ Ù–‘“˝”√
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //ªÒ»°≥Ã–Ú÷–∂•µ„—’…´ Ù–‘“˝”√
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //ªÒ»°≥Ã–Ú÷–◊‹±‰ªªæÿ’Û“˝”√
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    @SuppressLint("NewApi")
    public void drawSelf()
    {
        //÷∏∂® π”√ƒ≥Ã◊shader≥Ã–Ú
        GLES30.glUseProgram(mProgram);
        //≥ı ºªØ±‰ªªæÿ’Û
        Matrix.setRotateM(mMMatrix,0,0,0,1,0);
        //…Ë÷√—ÿZ÷·’˝œÚŒª“∆1
        Matrix.translateM(mMMatrix,0,0,0,1);
        //…Ë÷√»∆x÷·–˝◊™
        Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
        //Ω´±‰ªªæÿ’Û¥´»Î‰÷»æπ‹œﬂ
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFianlMatrix(mMMatrix), 0);
        //Ω´∂•µ„Œª÷√ ˝æ›¥´ÀÕΩ¯‰÷»æπ‹œﬂ
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3*4,
                mVertexBuffer
        );
        //Ω´∂•µ„—’…´ ˝æ›¥´ÀÕΩ¯‰÷»æπ‹œﬂ
        GLES30.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES30.GL_FLOAT,
                        false,
                        4*4,
                        mColorBuffer
                );
        GLES30.glEnableVertexAttribArray(maPositionHandle);//∆Ù”√∂•µ„Œª÷√ ˝æ›
        GLES30.glEnableVertexAttribArray(maColorHandle);//∆Ù”√∂•µ„◊≈…´ ˝æ›
        //ªÊ÷∆»˝Ω«–Œ
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
    public static float[] getFianlMatrix(float[] spec)
    {
        mMVPMatrix=new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
