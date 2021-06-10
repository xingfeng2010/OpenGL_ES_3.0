package com.xingfeng.opengles.chapter23.chapter231

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.xingfeng.opengles.util.Constant
import com.xingfeng.opengles.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GL231SurfaceView(context: Context): GLSurfaceView(context) {
    private var mRenderer:SceneRenderer
    //
    private val TOUCH_SCAL_FACTOR = 180.0f/320

    private var mPreviousY: Float = 0.0f
    private var mPreviousX: Float = 0.0f

    init {
        this.setEGLContextClientVersion(3)
        mRenderer = SceneRenderer(this@GL231SurfaceView)
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

                mRenderer.ball.yAngle += dx * TOUCH_SCAL_FACTOR
                mRenderer.ball.xAngle += dy * TOUCH_SCAL_FACTOR
            }
        }

        mPreviousX = x
        mPreviousY= y
        return true
    }

    class SceneRenderer(view: View): Renderer {
        private var mView:View = view
         lateinit var ball: Ball231

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0f, 0f, 0f, 1.0f)
            ball = Ball231(mView)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE)
        }

        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            //保护现场
            MatrixState.pushMatrix()
            ball.drawSelf()
            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            Constant.ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1f, 1f, 20f, 100f)
            MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

            //初始化变换矩阵
            MatrixState.setInitStack()
        }
    }
}