package com.xingfeng.opengles.chapter212.chapter2121

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.os.Build
import android.util.Log
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.LoadUtil
import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture10
import com.xingfeng.opengles.util.MatrixState
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL2121SurfaceView(context: Context) : GLSurfaceView(context) {
    private val mRenderer: SceneRenderer by lazy { SceneRenderer() }
    var textureIdCamera //系统分配的纹理id
            = 0
    var textureId = 0
    var st //摄像头纹理辅助工具，作用是从一个图像流中捕获图像帧作为OpenGL ES纹理
            : SurfaceTexture? = null
    var lock = Any() //同步锁

    var canUpdate = false //摄像头纹理是否需要更新

    var gCamera: Camera? = null
    var angle = 0f

    init {
        setEGLContextClientVersion(3)
        setRenderer(mRenderer)
        setRenderMode(RENDERMODE_CONTINUOUSLY)
    }

    inner class SceneRenderer : Renderer {
        var backDrawer //绘制摄像头实时画面的类
                : CameraBackDrawer? = null
        var lovo //茶壶绘制对象
                : LoadedObjectVertexNormalTexture10? = null

        override fun onDrawFrame(gl: GL10?) {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            Log.i("GL2121SurfaceView", "onDrawFrame canUpdate:$canUpdate")
            synchronized(lock) {
                if (canUpdate) {
                    /*updateTexImage方法可以得到帧信息的回调，可以在任何线程被调用，
             * 因此在没有做必要的保护的情况下，
             * updateTexImage()不应该直接从回调函数中调用。
             */
                    st!!.updateTexImage() //更新纹理图像为从图像流中提取的最近一帧。
                    canUpdate = false
                }
            }
            //绘制摄像头实时背景
            MatrixState.pushMatrix()
            MatrixState.translate(0f, 0f, -20f)
            backDrawer?.drawSelf(textureIdCamera)
            MatrixState.popMatrix()

            //绘制茶壶
            MatrixState.pushMatrix()
            MatrixState.translate(0f, -0.2f, 0f)
            MatrixState.scale(0.02f, 0.02f, 0.02f)
            MatrixState.rotate(18f, 1f, 0f, 0f)
            MatrixState.rotate(angle, 0f, 1f, 0f)
            lovo!!.drawSelf(textureId)
            MatrixState.popMatrix()
            angle = angle + 0.5f
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            //设置视窗大小及位置
            GLES30.glViewport(0, 0, width, height)
            //计算GLSurfaceView的宽高比
            val ratio = width.toFloat() / height
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 20f, 1000f)
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            //初始化变换矩阵
            MatrixState.setInitStack()
            //初始化光源位置
            MatrixState.setLightLocation(-40f, 10f, 20f)
            //初始化摄像头并开始预览
            initCamera(width, height)
            //创建摄像机内容绘制者对对象
            backDrawer = CameraBackDrawer(this@GL2121SurfaceView, ratio, 1.0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE)
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //初始化摄像头专用纹理
            textureIdCamera = initSurfaceTexture()
            //初始化摄像头纹理工具
            st = genSurfaceTexture(textureIdCamera)
            //加载纹理
            textureId = initTexture(R.drawable.ghxp)
            //加载要绘制的物体
            lovo = LoadUtil.loadFromFile10(
                "chapter1201/chapter1201.1/ch_t.obj",
                this@GL2121SurfaceView.getResources(),
                this@GL2121SurfaceView
            )
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
        val `is`: InputStream = this@GL2121SurfaceView.resources.openRawResource(resId)
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

    fun initSurfaceTexture(): Int {
        //生成纹理id
        val textures = IntArray(1)
        GLES30.glGenTextures(
            1,  //产生的纹理id的数量
            textures,  //纹理id的数组
            0 //偏移量
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_REPEAT.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_REPEAT.toFloat()
        )
        return textureId
    }

    fun genSurfaceTexture(texId: Int): SurfaceTexture? {
        val st = SurfaceTexture(texId)
        st.setDefaultBufferSize(512, 512) //设置默认的图像缓冲区大小
        st.setOnFrameAvailableListener { _ ->
            synchronized(lock) {
                canUpdate=true
            }
        }
        return st
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun initCamera(screenWidth: Int, screenHeight: Int) {
        Log.i("GL2121SurfaceView","begin Camera.open")
        gCamera?.let {
            it.stopPreview()
            it.release()
        }

        gCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK) //打开照相机
        gCamera!!.setPreviewTexture(st)
        gCamera!!.setDisplayOrientation(90)
        Log.i("GL2121SurfaceView",if(gCamera == null) "gCamera is NULL" else "gCamera is NOT NULL")
        gCamera?.let {
            //调用Camera的getParameters方法获取拍照参数
            val parameters: Camera.Parameters = it.getParameters()
            //获取可支持的预览图片的大小
            val preSize: List<Camera.Size> = parameters.getSupportedPreviewSizes()
            var previewWidth: Int = preSize[0].width //获取宽度
            var previewHeight: Int = preSize[0].height //获取高度
            for (i in 1 until preSize.size) {
                val similarity = Math.abs(
                    preSize[i].height.toDouble() / screenHeight -
                            preSize[i].width.toDouble() / screenWidth
                )
                if (similarity < Math.abs(previewHeight.toDouble() / screenHeight - previewWidth.toDouble() / screenWidth)) {
                    previewWidth = preSize[i].width
                    previewHeight = preSize[i].height
                }
            }
            parameters.setPreviewSize(previewWidth, previewHeight) //设置预览大小
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) //设置焦点模式为连续拍摄
            it.setParameters(parameters) //将参数设置入Camera
            it.startPreview() //开始预览
        }
    }
}
