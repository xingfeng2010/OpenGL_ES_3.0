package com.xingfeng.opengles.chapter23.chapter233

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.View
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant
import com.xingfeng.opengles.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL233SurfaceView(context: Context) : GLSurfaceView(context) {
    private var mRenderer: SceneRenderer

    //
    private val TOUCH_SCAL_FACTOR = 180.0f / 320

    private var mPreviousY: Float = 0.0f
    private var mPreviousX: Float = 0.0f

    init {
        this.setEGLContextClientVersion(3)
        mRenderer = SceneRenderer(this@GL233SurfaceView)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - mPreviousX
                var dy = y - mPreviousY

                mRenderer.textureRect.yAngle += dx * TOUCH_SCAL_FACTOR
                mRenderer.textureRect.xAngle += dy * TOUCH_SCAL_FACTOR

                mRenderer.textureRectLB.yAngle += dx * TOUCH_SCAL_FACTOR
                mRenderer.textureRectLB.xAngle += dy * TOUCH_SCAL_FACTOR
            }
        }

        mPreviousX = x
        mPreviousY = y
        return true
    }

    class SceneRenderer(view: View) : Renderer {
        private var mView: View = view
        lateinit var textureRect: TextureRect233
        lateinit var textureRectLB: TextureRect233
        var textureId = 0

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //设置屏幕背景色RGBA

            //设置屏幕背景色RGBA
            GLES30.glClearColor(1f, 1.0f, 1.0f, 1.0f)
            //打开深度检测
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //打开背面剪裁
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE)
            //初始化变换矩阵
            //初始化变换矩阵
            MatrixState.setInitStack()
            textureRect = TextureRect233(mView, 17.0f, 17.0f, 1.0f, 1.0f, false)
            textureRectLB = TextureRect233(mView, 17.0f, 17.0f, 1.0f, 1.0f, true)
            textureId = Constant.initTexture(mView.resources, R.drawable.jinyu)
        }

        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲

            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()
            MatrixState.translate(-9f+ textureRect.xAngle, 0f, 0f)
            textureRect.drawSelf(textureId) //平面

            MatrixState.popMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(9f + textureRect.xAngle, 0f, 0f)
            textureRectLB.drawSelf(textureId) //平面

            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            //设置视窗大小及位置

            //设置视窗大小及位置
            GLES30.glViewport(0, 0, width, height)
            //计算GLSurfaceView的宽高比
            //计算GLSurfaceView的宽高比
            val ratio = width.toFloat() / height
            //设置camera位置
            //设置camera位置
            MatrixState.setCamera(0f, 0f, 20f, 0f, 0f, 0f, 0f, 1f, 0f)
            //调用此方法计算产生透视投影矩阵
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        }
    }
}