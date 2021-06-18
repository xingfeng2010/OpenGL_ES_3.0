package com.xingfeng.opengles.chapter23.chapter2314

import android.R.attr
import android.app.Activity
import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant
import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ScreentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL2314SurfaceView(context: Context) : GLSurfaceView(context) {
    private var mRenderer: SceneRenderer
    var fps: List<ParticleSystem> = emptyList()
    var fdfd = emptyArray<ParticleForDraw>()
    lateinit var wallsForDraw: WallsForwDraw
    lateinit var brazier: LoadedObjectVertexNormalTexture

    private var Offset = 20.0f

    // 视线方向
    private var direction: Float = 0.0f

    // 摄像机x坐标
    private var cx: Float = 0.0f

    //摄像机y坐标
    private var cy: Float = 0.0f

    //摄像机z坐标
    private var cz: Float = 0.0f

    //观察目标点x坐标
    private var tx: Float = 0.0f

    //观察目标点y坐标
    private var ty: Float = 0.0f

    //观察目标点z坐标
    private var tz: Float = 0.0f
    private var ux: Float = -cx

    //观察目标点y坐标
    private var uy = Math.abs((cx * tx + cz * tz - cz * cz) / (ty - cy));

    //观察目标点z坐标
    private var uz = -cz
    private val DEGREE_SPAN: Float = 3.0.toFloat() / (180.0f * Math.PI).toFloat()

    private val TOUCH_SCAL_FACTOR = 180.0f / 320

    private var mPreviousY: Float = 0.0f
    private var mPreviousX: Float = 0.0f

    // 系统火焰分配的纹理id
    private var textureIdFire = 0

    //系统火盆分配的纹理id
    private var textureIdbrazier = 0

    private var count = 0

    //线程循环的标志位
    private var flag = true

    private var WIDTH = 0
    private var HEIGHT = 0

    init {
        this.setEGLContextClientVersion(3)
        mRenderer = SceneRenderer(this@GL2314SurfaceView)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_CONTINUOUSLY

        WIDTH = ScreentUtil.getScreenWidth(context as Activity)
        HEIGHT = ScreentUtil.getScreenHeight(context as Activity)

        Log.i("DEBUTE_TEST", "WIDTH:$WIDTH,  HEIGHT:$HEIGHT")
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                flag = true
                CoroutineScope(Dispatchers.IO).launch {
                    while (flag) {
                        if (x > WIDTH / 4 && x < 3 * WIDTH / 4 && attr.y > 0 && attr.y < HEIGHT / 2) {    //向前
                            if (Math.abs(Offset - 0.5f) > 25 || Math.abs(Offset - 0.5f) < 15) {

                            } else {
                                Offset = Offset - 0.5f
                            }
                        } else if (x > WIDTH / 4 && x < 3 * WIDTH / 4 && attr.y > HEIGHT / 2 && attr.y < HEIGHT) {    //向后
                            if (Math.abs(Offset + 0.5f) > 25 || Math.abs(Offset + 0.5f) < 15) {

                            } else {
                                Offset = Offset + 0.5f
                            }
                        } else if (x < WIDTH / 4) {
                            //顺时针旋转
                            direction = direction - DEGREE_SPAN
                        } else if (x > WIDTH / 4) {
                            //逆时针旋转
                            direction = direction + DEGREE_SPAN
                        }
                        try {
                            Thread.sleep(100)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            }

            MotionEvent.ACTION_UP -> {

            }
        }

        mPreviousX = x
        mPreviousY = y
        return true
    }


    class SceneRenderer(view: View) : Renderer {
        private var mView: View = view

        //        lateinit var textureRect: TextureRect233
//        lateinit var textureRectLB: TextureRect233
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
//            textureRect = TextureRect233(mView, 17.0f, 17.0f, 1.0f, 1.0f, false)
//            textureRectLB = TextureRect233(mView, 17.0f, 17.0f, 1.0f, 1.0f, true)
            textureId = Constant.initTexture(mView.resources, R.drawable.castle)
        }

        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲

            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
//            MatrixState.pushMatrix()
//            MatrixState.translate(-9f+ textureRect.xAngle, 0f, 0f)
//            textureRect.drawSelf(textureId) //平面
//
//            MatrixState.popMatrix()

            MatrixState.pushMatrix()
//            MatrixState.translate(9f + textureRect.xAngle, 0f, 0f)
            //textureRectLB.drawSelf(textureId) //平面

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