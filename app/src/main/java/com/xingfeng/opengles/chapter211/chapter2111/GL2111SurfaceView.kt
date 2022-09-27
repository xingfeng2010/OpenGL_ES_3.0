package com.xingfeng.opengles.chapter211.chapter2111

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.view.MotionEvent
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.MatrixState
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL2111SurfaceView(context: Context) : GLSurfaceView(context) {
    private val TOUCH_SCALE_FACTOR = 180.0f / 320 //角度缩放比例
    private var mRenderer: SceneRenderer

    private var mPreviousX: Float = 0f
    private var mPreviousY: Float = 0f

    private var textureId = 0

    init {
        setEGLContextClientVersion(3)
        mRenderer = SceneRenderer()
        setRenderer(mRenderer)
        setRenderMode(RENDERMODE_CONTINUOUSLY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - mPreviousX
                var dy = y - mPreviousY
                mRenderer.texRect.yAngle += dx * TOUCH_SCALE_FACTOR
                mRenderer.texRect.zAngle += dy * TOUCH_SCALE_FACTOR
            }
        }

        mPreviousY = y
        mPreviousX = x
        return true
    }

    inner class SceneRenderer : Renderer {
        //纹理矩形
        lateinit var texRect: Triangle

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.5f,0.5f,0.5f, 1.0f);
            //创建三角形对对象
            texRect= Triangle(this@GL2111SurfaceView)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //初始化纹理
            initTexture();
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//设置视窗大小及位置
            //设置视窗大小及位置
            GLES30.glViewport(0, 0, width, height)
            //计算GLSurfaceView的宽高比
            //计算GLSurfaceView的宽高比
            val ratio = width.toFloat() / height
            //调用此方法计算产生透视投影矩阵
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 100f)
            //调用此方法产生摄像机9参数位置矩阵
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0f, 0f, 10f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        }

        override fun onDrawFrame(gl: GL10?) {
            //清除深度缓冲与颜色缓冲
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            //绘制纹理矩形
            //绘制纹理矩形
            texRect.drawSelf(textureId)
        }
    }

    fun initTexture(resId: Int): Int //textureId
    {
        //生成纹理ID
        val textures = IntArray(1)
        GLES30.glGenTextures(
            1,  //产生的纹理id的数量
            textures,  //纹理id的数组
            0 //偏移量
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )


        //通过输入流加载图片===============begin===================
        val `is`: InputStream = this@GL2111SurfaceView.resources.openRawResource(resId)
        val bitmapTmp: Bitmap
        bitmapTmp = try {
            BitmapFactory.decodeStream(`is`)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //通过输入流加载图片===============end=====================

        //实际加载纹理
        GLUtils.texImage2D(
            GLES30.GL_TEXTURE_2D,  //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
            0,  //纹理的层次，0表示基本图像层，可以理解为直接贴图
            bitmapTmp,  //纹理图像
            0 //纹理边框尺寸
        )
        bitmapTmp.recycle() //纹理加载成功后释放图片
        return textureId
    }

    fun initTexture() //textureId
    {
        //生成纹理ID
        val textures = IntArray(1)
        GLES30.glGenTextures(
            1,  //产生的纹理id的数量
            textures,  //纹理id的数组
            0 //偏移量
        )
        textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        //通过输入流加载图片===============begin===================
        val `is` = this.resources.openRawResource(R.drawable.wall)
        val bitmapTmp: Bitmap
        bitmapTmp = try {
            BitmapFactory.decodeStream(`is`)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //通过输入流加载图片===============end=====================

        //实际加载纹理
        GLUtils.texImage2D(
            GLES30.GL_TEXTURE_2D,  //纹理类型
            0,  //纹理的层次，0表示基本图像层，可以理解为直接贴图
            bitmapTmp,  //纹理图像
            0 //纹理边框尺寸
        )
        bitmapTmp.recycle() //纹理加载成功后释放图片
    }
}
