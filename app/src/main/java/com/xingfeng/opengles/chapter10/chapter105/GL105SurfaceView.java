package com.xingfeng.opengles.chapter10.chapter105;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xingfeng.opengles.R;
import com.xingfeng.opengles.util.LoadUtil;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalAverage;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalAverage2;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalFace;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalFace2;
import com.xingfeng.opengles.util.MatrixState;

class GL105SurfaceView extends GLSurfaceView
{
    private final float TOUCH_SCALE_FACTOR = 180.0f/200;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器
    private float mPreviousX;//上次的触控位置X坐标
    //关于摄像机的变量
    float cx=0;//摄像机x位置
    float cy=150;//摄像机y位置
    float cz=400;//摄像机z位置

    float pmScale = 200f;//平面矩形的边长

	public GL105SurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;//计算触控笔X位移
                cx += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                //将cx限制在一定范围内
                cx = Math.max(cx, -200);
                cx = Math.min(cx, 200);
                break;
        }
        mPreviousX = x;//记录触控笔位置
        return true;
    }
	private class SceneRenderer implements Renderer
    {
		LoadedObjectVertexNormalFace2 pm;
		LoadedObjectVertexNormalFace2 cft;
		LoadedObjectVertexNormalAverage2 qt;
		LoadedObjectVertexNormalAverage2 yh;
		LoadedObjectVertexNormalAverage2 ch;
		MixTextureRect rect;
		//物体离中心的的距离
		final float disWithCenter = 12.0f;
        @SuppressLint("InlinedApi")
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            MatrixState.setCamera
                    (
                            cx,   //����λ�õ�X
                            cy, 	//����λ�õ�Y
                            cz,   //����λ�õ�Z
                            0, 	//�����򿴵ĵ�X
                            0,   //�����򿴵ĵ�Y
                            0,   //�����򿴵ĵ�Z
                            0, 	//upλ��
                            1,
                            0
                    );

            MatrixState.pushMatrix();
            //若加载的物体部位空则绘制物体
            MatrixState.pushMatrix();
            pm.drawSelf();//平面
            MatrixState.popMatrix();
            //缩放物体
            MatrixState.pushMatrix();
            MatrixState.scale(5.0f, 5.0f, 5.0f);
            //绘制物体
            //绘制长方体
            MatrixState.pushMatrix();
            MatrixState.translate(-disWithCenter, 0f, 0);
            cft.drawSelf();
            MatrixState.popMatrix();
            //绘制球体
            MatrixState.pushMatrix();
            MatrixState.translate(disWithCenter, 0f, 0);
            qt.drawSelf();
            MatrixState.popMatrix();
            //绘制圆环
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -disWithCenter);
            yh.drawSelf();
            MatrixState.popMatrix();
            //绘制茶壶
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, disWithCenter);
            ch.drawSelf();
            MatrixState.popMatrix();
            MatrixState.popMatrix();

            MatrixState.popMatrix();
        }  

        @SuppressLint("NewApi")
		public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES30.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            float a = 0.5f;
            MatrixState.setProjectFrustum(-ratio*a, ratio*a, -1*a, 1*a, 2, 1000);
            //初始化光源位置
            MatrixState.setLightLocation(100, 100, 100);
        }
        @SuppressLint("NewApi")
		@Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //����id
            //����Ҫ���Ƶ�����
            ch= LoadUtil.loadFromFileVertexOnlyAverage2("chapter10/chapter10.1/ch.obj", GL105SurfaceView.this.getResources(),GL105SurfaceView.this);
            pm=LoadUtil.loadFromFileVertexOnlyFace2("chapter10/chapter10.1/pm.obj", GL105SurfaceView.this.getResources(),GL105SurfaceView.this);;
    		cft=LoadUtil.loadFromFileVertexOnlyFace2("chapter10/chapter10.1/cft.obj", GL105SurfaceView.this.getResources(),GL105SurfaceView.this);;
    		qt=LoadUtil.loadFromFileVertexOnlyAverage2("chapter10/chapter10.1/qt.obj", GL105SurfaceView.this.getResources(),GL105SurfaceView.this);;
    		yh=LoadUtil.loadFromFileVertexOnlyAverage2("chapter10/chapter10.1/yh.obj", GL105SurfaceView.this.getResources(),GL105SurfaceView.this);;
    		rect = new MixTextureRect(GL105SurfaceView.this, pmScale, pmScale);
        }
    }


	@SuppressLint("NewApi")
	public int initTexture(int drawableId)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);
		int textureId=textures[0];
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);

        //ͨ������������ͼƬ===============begin===================
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
        //ͨ������������ͼƬ===============end=====================

        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //��������
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ

        return textureId;
	}
	@Override
    public void onResume() {
    	super.onResume();
    }
	@Override
	public void onPause() {
		super.onPause();
	}
}
