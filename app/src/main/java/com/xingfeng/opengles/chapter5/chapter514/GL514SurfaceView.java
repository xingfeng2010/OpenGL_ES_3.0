package com.xingfeng.opengles.chapter5.chapter514;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.xingfeng.opengles.chapter5.chapter51.SixPointedStar;
import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GL514SurfaceView extends GLSurfaceView
{
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private float mPreviousX;//上次的触控位置X坐标
    float yAngle=90;//总场景绕y轴旋转的角度
    private float mPreviousY;//上次的触控位置X坐标
    float xAngle=20;//总场景绕y轴旋转的角度

    public GL514SurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }


    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();//获取此次触控的y坐标
        float x = e.getX();//获取此次触控的x坐标
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE://若为移动动作
                float dy = y - mPreviousY;//计算触控位置的Y位移
                float dx = x - mPreviousX;//计算触控位置的X位移
                xAngle += dy * TOUCH_SCALE_FACTOR;//设置三角形对绕y轴旋转角度
                yAngle += dx * TOUCH_SCALE_FACTOR;
        }
        mPreviousY = y;//记录触控笔y坐标
        mPreviousX = x;//记录触控笔x坐标
        return true;
    }

    private class SceneRenderer implements Renderer
    {
        PerspectiveCube cube1;//立方体对象1引用
        PerspectiveCube cube2;//立方体对象2引用

        @SuppressLint({ "InlinedApi", "NewApi" })
        public void onDrawFrame(GL10 gl)
        {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //保护现场
            MatrixState.pushMatrix();
            //绕Y轴旋转
            MatrixState.rotate(yAngle, 0, 1, 0);//绕y轴旋转yAngle度
            MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴旋转xAngle度
            //绘制左侧立方体
            MatrixState.pushMatrix();
            MatrixState.translate(-250f, 0, 0);
            cube1.drawSelf();
            MatrixState.popMatrix();

            //绘制右侧立方体
            MatrixState.pushMatrix();
            MatrixState.translate(250f,0, 0);
            cube2.drawSelf();
            MatrixState.popMatrix();

            //恢复现场
            MatrixState.popMatrix();
        }

        @SuppressLint("NewApi")
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //设置视口的大小及位置
            GLES30.glViewport(0, 0, width, height);
            //计算视口的宽高比
            float ratio = (float) width / height;

            /**
             * 之所以产生了不正常的绘制效果是由于此次摄像机离要观察的物体较远（距离有4000多），
             * 而near参数设置得很小（只有1.0），造成了在进行深度计算时的区分度差，从而产生了不正常的绘制效果。
             * 想要产生正常的绘制效果很简单，只需要修改near透视参数值由1.0f改为300.0f即可。
             * 对于本案例这样的改动仅仅是增大了near值，但视角没有改变，摄像机位置、姿态也没有改变，因此观察到的场景区域是完全相同的，
             * 只是不会再出现不正确的波浪状交叠边缘了。
             */
            //final float NEAR=1.0f;//透视参数near

            final float NEAR=300.0f;//透视参数near
            final float FAR=10000.0f;//透视参数far
            final float LEFT=-NEAR*ratio*0.25f;//透视参数left
            final float RIGHT=NEAR*ratio*0.25f;//透视参数right
            final float BOTTOM=-NEAR*0.25f;//透视参数bottom
            final float TOP=NEAR*0.25f;//透视参数top


            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
            //调用此方法产生摄像机矩阵
            MatrixState.setCamera(5000f,0.5f,0f,0f,0f,0f,0f,1.0f,0.0f);

            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        @SuppressLint("NewApi")
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0f,0f,0f, 1.0f);
            //创建立方体对象
            cube1=new PerspectiveCube(GL514SurfaceView.this,500,new float[]{0,1,1,0});
            cube2=new PerspectiveCube(GL514SurfaceView.this,499.5f,new float[]{1,1,0,0});
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE);
        }
    }
}
