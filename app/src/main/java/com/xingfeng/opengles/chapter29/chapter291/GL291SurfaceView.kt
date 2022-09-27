package com.xingfeng.opengles.chapter29.chapter291

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.view.MotionEvent
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.LoadUtil
import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture9
import com.xingfeng.opengles.util.MatrixState
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL291SurfaceView(context: Context) : GLSurfaceView(context) {
    private var mRenderer: SceneRenderer
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 80.0f / 800 //角度缩放比例

    var armTexId //系统分配的纹理id--胳膊、身体
            = 0
    var headTexId //系统分配的纹理id--头
            = 0
    var floorTexId //系统分配的纹理 地面
            = 0

    //各个身体部件的绘制者数组
    var lovntArray = arrayOfNulls<LoadedObjectVertexNormalTexture9>(12)

    //机器人
    var robot: Robot? = null

    //执行动作的线程
    var dat: DoActionThread? = null

    init {
        setEGLContextClientVersion(2)
        mRenderer = SceneRenderer()
        setRenderer(mRenderer)
        setRenderMode(RENDERMODE_CONTINUOUSLY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x: Float = event.x
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - mPreviousX
                mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR
            }
        }

        return true
    }

    inner class SceneRenderer : GLSurfaceView.Renderer {
        var yAngle: Double = 0.0
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //设置屏幕背景色RGBA
            //设置屏幕背景色RGBA
            GLES30.glClearColor(1f, 1f, 1f, 1.0f)
            //关闭背面剪裁
            //关闭背面剪裁
            GLES30.glDisable(GLES30.GL_CULL_FACE)
            //打开深度检测
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //初始化纹理
            //初始化纹理
            armTexId = initTexture(R.drawable.arm)
            headTexId = initTexture(R.drawable.head) //系统分配的纹理id

            floorTexId = initTexture(R.drawable.wenli)
            //加载身体
            //加载身体
            lovntArray[0] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/body.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            //加载头
            //加载头
            lovntArray[1] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/head.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                headTexId
            )
            //加载左胳膊
            //加载左胳膊
            lovntArray[2] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/left_top.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            lovntArray[3] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/left_bottom.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )


            //加载右胳膊


            //加载右胳膊
            lovntArray[4] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/right_top.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            lovntArray[5] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/right_bottom.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )

            //加载右腿

            //加载右腿
            lovntArray[6] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/right_leg_top.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            lovntArray[7] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/right_leg_bottom.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )

            //加载左腿

            //加载左腿
            lovntArray[8] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/left_leg_top.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            lovntArray[9] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/left_leg_bottom.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )

            //加载左脚

            //加载左脚
            lovntArray[10] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/left_foot.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )
            lovntArray[11] = LoadUtil.loadFromFile9(
                "chapter901/chapter901.1/right_foot.obj",
                this@GL291SurfaceView.getResources(),
                this@GL291SurfaceView,
                armTexId
            )

            robot = Robot(lovntArray, this@GL291SurfaceView)
            dat = DoActionThread(robot, this@GL291SurfaceView)
            dat?.start()
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
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
            //调用此方法产生摄像机9参数位置矩阵
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(
                2f,  //人眼位置的X
                0.05f,  //人眼位置的Y
                2f, 0f,  //人眼球看的点X
                0f,  //人眼球看的点Y
                0f, 0f, 1f, 0f
            )
            MatrixState.setInitStack()
            MatrixState.setLightLocation(0f, 10f, 10f)
        }

        override fun onDrawFrame(gl: GL10?) {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            MatrixState.setCamera(
                (2.5 * Math.sin(yAngle)).toFloat(),   //人眼位置的X
                0.05f,    //人眼位置的Y
                (2.5 * Math.cos(yAngle)).toFloat(),   //人眼位置的Z
                0f,    //人眼球看的点X
                0.03f,   //人眼球看的点Y
                0f,   //人眼球看的点Z
                0f,
                1f,
                0f
            );

            //绘制机器人
            robot?.drawSelf();
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
        val `is`: InputStream = this@GL291SurfaceView.resources.openRawResource(resId)
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
}
