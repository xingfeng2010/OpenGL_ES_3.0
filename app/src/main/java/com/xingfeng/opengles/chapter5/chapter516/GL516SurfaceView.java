package com.xingfeng.opengles.chapter5.chapter516;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.xingfeng.opengles.chapter5.chapter515.MultiColorRect;
import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GL516SurfaceView extends GLSurfaceView
{
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器
    private float mPreviousX;//上次的触控位置X坐标
    float yAngle=90;//总场景绕y轴旋转的角度
    private float mPreviousY;//上次的触控位置X坐标
    float xAngle=20;//总场景绕y轴旋转的角度
    float polygonOffsetFactor =-1.0f;
    float polygonOffsetUnits  =-2.0f;


    public GL516SurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }
    //触摸事件回调方法
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;//计算触控笔X位移
                yAngle += dx * TOUCH_SCALE_FACTOR;//设置三角形对绕y轴旋转角度
                float dy = y - mPreviousY;//计算触控笔X位移
                xAngle += dy * TOUCH_SCALE_FACTOR;//设置三角形对绕y轴旋转角度
        }
        mPreviousX=x;
        mPreviousY=y;
        return true;
    }

    boolean cullFaceFlag=false;//是否开启背面剪裁的标志位
    //设置是否开启背面剪裁的标志位
    public void setCullFace(boolean flag)
    {
        cullFaceFlag=flag;
    }

    boolean cwCcwFlag=false;//是否打开自定义卷绕的标志位
    //设置是否打开自定义卷绕的标志位
    public void setCwOrCcw(boolean flag)
    {
        cwCcwFlag=flag;
    }
    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        TrianglePair tp;//三角形对对象引用
        public void onDrawFrame(GL10 gl)
        {
            if(cullFaceFlag)
            {									//判断是否要打开背面剪裁
                GLES30.glEnable(GLES30.GL_CULL_FACE);			//打开背面剪裁
            }
            else
            {
                GLES30.glDisable(GLES30.GL_CULL_FACE);			//关闭背面剪裁
            }
            if(cwCcwFlag)
            {									//判断是否需要打开顺时针卷绕
                GLES30.glFrontFace(GLES30.GL_CCW);			//使用逆时针卷绕
            }
            else
            {
                GLES30.glFrontFace(GLES30.GL_CW);				//使用顺时针卷绕
            }
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT |GLES30.GL_COLOR_BUFFER_BIT);//清除深度缓冲与颜色缓冲
            MatrixState.pushMatrix();            					//保护现场
            MatrixState.translate(0, -1.4f, 0);						//沿y轴负方向平移
            tp.drawSelf();										//绘制三角形对
            MatrixState.popMatrix();        						//恢复现场
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视口的大小及位置
            GLES30.glViewport(0, 0, width, height);
            //计算视口的宽高比
            Constant.ratio = (float) width / height;
            // 调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 10, 100);
            // 调用此方法产生摄像机矩阵
            MatrixState.setCamera(0, 0f, 20, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.5f,0.5f,0.5f, 1.0f);
            //创建三角形对对象
            tp=new TrianglePair(GL516SurfaceView.this);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }
    }
}
