package com.xingfeng.opengles.chapter20.chapter205;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.xingfeng.opengles.R;
import com.xingfeng.opengles.util.LoadUtil;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture5;
import com.xingfeng.opengles.util.MatrixState;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GL205SurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    float ratio;

    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=1024;

    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;

    public GL205SurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    //触摸事件回调方法
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控点Y位移
                float dx = x - mPreviousX;//计算触控点X位移
                mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//设置沿z轴旋转角度
                requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控点位置
        mPreviousX = x;//记录触控点位置
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        float yAngle;//绕Y轴旋转的角度
        float xAngle; //绕X轴旋转的角度
        //从指定的obj文件中加载对象
        LoadedObjectVertexNormalTexture5 lovo;

        //帧缓冲id
        int frameBufferId;
        //渲染深度缓冲id
        int renderDepthBufferId;
        //最后生成的纹理id
        int textureId;
        //国画小品的纹理id
        int textureIdGHXP;
        //矩形绘制对象
        TextureRect5 tr;

        //初始化帧缓冲和渲染缓冲的方法
        public void initFRBuffers() {
            //用于存放产生的帧缓冲id的数组
            int tia[] = new int[1];
            //产生一个帧缓冲id
            GLES30.glGenFramebuffers(1, tia, 0);

            frameBufferId = tia[0];
            //绑定帧缓冲id
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);

            //产生一个渲染缓冲id
            GLES30.glGenRenderbuffers(1, tia, 0);
            renderDepthBufferId=tia[0];
            GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
            //为渲染缓冲区初始化存储
            GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER,
                    GLES30.GL_DEPTH_COMPONENT16, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);

            int[] tempIds = new int[1];
            GLES30.glGenTextures(1, tempIds, 0);
            textureId = tempIds[0];
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureId);//绑定纹理id
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置MIN采样方式
                    GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置MAG采样方式
                    GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置S轴拉伸方式
                    GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//设置T轴拉伸方式
                    GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
            //设置颜色附件纹理图的格式
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0,GLES30.GL_RGBA,GEN_TEX_WIDTH,GEN_TEX_HEIGHT,
                    0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
            //设置自定义帧缓冲的颜色缓冲附件
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,
                    GLES30.GL_TEXTURE_2D, textureId, 0);
            //设置自定义帧缓冲的深度缓冲附件
            GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER,GLES30.GL_DEPTH_ATTACHMENT,
                    GLES30.GL_RENDERBUFFER,renderDepthBufferId);

        }

        //通过绘制产生纹理
        public void generateTextImage() {
            //设置视窗大小及位置
            GLES30.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
            //绑定帧缓冲id
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
            //清楚深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            //设置透视投影
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);

            MatrixState.pushMatrix();//保护现场
            MatrixState.translate(0, -16f, -80f);//坐标系推远
            //绕Y轴、X轴旋转
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            if(lovo!=null)//若加载的物体不为空则绘制物体
            {
                lovo.drawSelf(textureIdGHXP);
            }
            MatrixState.popMatrix();//恢复现场

        }

        //绘制生成的矩形纹理
        public void drawShadowTexture() {
            //设置视窗大小及位置
            GLES30.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//绑定帧缓冲id
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT |GLES30.GL_COLOR_BUFFER_BIT);
            //设置正交投影
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 2, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
            MatrixState.pushMatrix();
            tr.drawSelf(textureId);//绘制纹理矩形
            MatrixState.popMatrix();
        }

        public void onDrawFrame(GL10 gl)
        {
            generateTextImage();//通过绘制产生矩形纹理
            drawShadowTexture();//绘制矩形纹理
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            SCREEN_WIDTH = width;
            SCREEN_HEIGHT = height;

            //计算GLSurfaceView的宽高比
            ratio = (float) width / height;

            initFRBuffers();
            textureIdGHXP = initTexture(R.drawable.ghxp);
            tr = new TextureRect5(GL205SurfaceView.this, ratio);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //初始化光源位置
            MatrixState.setLightLocation(40, 10, 20);
            //加载要绘制的物体
            lovo=LoadUtil.loadFromFile5("chapter201/chapter201.5/ch_t.obj", GL205SurfaceView.this.getResources(),GL205SurfaceView.this);
        }
    }
    public int initTexture(int drawableId)//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);

        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================
        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D, //纹理类型
                        0,
                        GLUtils.getInternalFormat(bitmapTmp),
                        bitmapTmp, //纹理图像
                        GLUtils.getType(bitmapTmp),
                        0 //纹理边框尺寸
                );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
    }
}
