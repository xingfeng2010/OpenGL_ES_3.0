package com.xingfeng.opengles.chapter3;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyTDView extends GLSurfaceView
{
    final float ANGLE_SPAN = 0.375f;

    RotateThread rthread;
    SceneRenderer mRenderer;//◊‘∂®“Â‰÷»æ∆˜µƒ“˝”√
    public MyTDView(Context context)
    {
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer=new SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        Triangle tle;
        public void onDrawFrame(GL10 gl)
        {
            //«Â≥˝…Ó∂»ª∫≥Â”Î—’…´ª∫≥Â
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //ªÊ÷∆»˝Ω«–Œ∂‘œÛ
            tle.drawSelf();
        }
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //…Ë÷√ ”¥∞¥Û–°º∞Œª÷√
            GLES30.glViewport(0, 0, width, height);
            //º∆À„GLSurfaceViewµƒøÌ∏ﬂ±»
            float ratio = (float) width / height;
            //µ˜”√¥À∑Ω∑®º∆À„≤˙…˙Õ∏ ”Õ∂”∞æÿ’Û
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            //µ˜”√¥À∑Ω∑®≤˙…˙…„œÒª˙9≤Œ ˝Œª÷√æÿ’Û
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0,0,3,0f,0f,0f,0f,1.0f,0.0f);
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            //…Ë÷√∆¡ƒª±≥æ∞…´RGBA
            GLES30.glClearColor(0,0,0,1.0f);
            //¥¥Ω®»˝Ω«–Œ∂‘∂‘œÛ
            tle=new Triangle(MyTDView.this);
            //¥Úø™…Ó∂»ºÏ≤‚
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            rthread=new RotateThread();
            rthread.start();
        }
    }
    public class RotateThread extends Thread//◊‘∂®“Âµƒƒ⁄≤ø¿‡œﬂ≥Ã
    {
        public boolean flag=true;
        @Override
        public void run()//÷ÿ–¥µƒrun∑Ω∑®
        {
            while(flag)
            {
                mRenderer.tle.xAngle=mRenderer.tle.xAngle+ANGLE_SPAN;
                try
                {
                    Thread.sleep(20);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
