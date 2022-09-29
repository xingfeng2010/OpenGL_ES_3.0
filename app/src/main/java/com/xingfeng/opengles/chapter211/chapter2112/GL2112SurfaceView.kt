package com.xingfeng.opengles.chapter211.chapter2112

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import android.view.MotionEvent
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.MatrixState
import java.io.IOException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

//声明包
internal class GL2112SurfaceView(context: Context?) : GLSurfaceView(context) {
    private val TOUCH_SCALE_FACTOR = 180.0f / 320 //角度缩放比例
    private val mRenderer //场景渲染器
            : SceneRenderer
    private var mPreviousX //上次的触控位置X坐标
            = 0f
    var textureId //系统分配的纹理id
            = 0
    var textureRDId //扰动纹理id
            = 0
    var textureJBId //小草颜色渐变纹理id
            = 0
    var cAngle = 0f //摄像机转动的角度
    var num = 400000f //小草的总棵数
    var r = Math.sqrt(num.toDouble()).toFloat() / 4 - 1 //摄像机到目标点的距离，即摄像机旋转的半径

    //摄像机目标点的坐标
    var targetX = r
    var targetY = 0f
    var targetZ = r

    //摄像机的up向量
    var upX = 0f
    var upY = 1f
    var upZ = 0f
    var ANGLE_MIN = -360f //摄像机旋转的角度范围的最小值
    var ANGLE_MAX = 360f //摄像机旋转的角度范围的最大值

    //摄像机的观察者坐标
    var CameraX = r
    var CameraY = 6f
    var CameraZ = r
    var uTexHandle //小草纹理属性引用id
            = 0
    var uJBTexHandle //颜色渐变纹理引用id
            = 0

    //触摸事件回调方法
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mPreviousX //计算触控点X位移
                cAngle += dx * TOUCH_SCALE_FACTOR
            }
        }
        mPreviousX = x //记录触控点位置
        calculateCamera(cAngle) //计算摄像机的观察点
        return true
    }

    fun calculateCamera(angle: Float) {
        //计算摄像机观察者的坐标
        CameraX = (r * Math.sin(Math.toRadians(angle.toDouble()))).toFloat() + targetX
        CameraZ = (r * Math.cos(Math.toRadians(angle.toDouble()))).toFloat() + targetZ
        //设置摄像机的9参数
        MatrixState.setCamera(CameraX, CameraY, CameraZ, targetX, targetY, targetZ, upX, upY, upZ)
    }

    private inner class SceneRenderer : Renderer {
        //从指定的obj文件中加载的对象
        var lovo: GrassObject? = null

        //测试刷帧频率的代码
        var olds: Long = 0
        var currs: Long = 0
        override fun onDrawFrame(gl: GL10) {
            //注释掉的为测试刷帧频率的代码
            currs = System.nanoTime()
            Log.d("FPS", (1000000000.0 / (currs - olds)).toString() + "FPS")
            olds = currs
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

            //若加载的物体不为空则绘制物体
            if (lovo != null) {
                lovo!!.drawSelf(textureId, textureRDId, textureJBId, num.toInt())
            }
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            //设置视窗大小及位置
            GLES30.glViewport(0, 0, width, height)
            //计算GLSurfaceView的宽高比
            val ratio = width.toFloat() / height
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100000f)
            calculateCamera(cAngle) //计算摄像机的观察点并设置摄像机的位置
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //加载要绘制的物体
            lovo =
                GrassLoadUtil.loadFromFile("chapter1101/chapter1101.2/grass.obj", this@GL2112SurfaceView.resources, this@GL2112SurfaceView)
            //小草纹理图
            textureId = initTexture(R.drawable.grass)
            //扰动纹理图
            textureRDId = initTexture(R.drawable.noise2)
            //小草颜色渐变纹理图
            textureJBId = initTexture(R.drawable.jb)
        }
    }

    fun initTexture(drawableId: Int): Int //textureId
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
            GLES30.GL_REPEAT.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_REPEAT.toFloat()
        )

        //通过输入流加载图片===============begin===================
        val `is` = this.resources.openRawResource(drawableId)
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
        GLUtils.texImage2D(
            GLES30.GL_TEXTURE_2D,  //纹理类型
            0,
            GLUtils.getInternalFormat(bitmapTmp),
            bitmapTmp,  //纹理图像
            GLUtils.getType(bitmapTmp),
            0 //纹理边框尺寸
        )
        bitmapTmp.recycle() //纹理加载成功后释放图片
        return textureId
    }

    init {
        setEGLContextClientVersion(3) //设置使用OPENGL ES3.0
        mRenderer = SceneRenderer() //创建场景渲染器
        setRenderer(mRenderer) //设置渲染器
        renderMode = RENDERMODE_CONTINUOUSLY //设置渲染模式为主动渲染
    }
}