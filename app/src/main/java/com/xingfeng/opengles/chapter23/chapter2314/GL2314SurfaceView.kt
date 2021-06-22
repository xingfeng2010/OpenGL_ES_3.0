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
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.count
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.cx
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.cy
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.cz
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.tx
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.ty
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.tz
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.ux
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.uy
import com.xingfeng.opengles.chapter23.chapter2314.GL2314SurfaceView.SceneRenderer.Companion.uz
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.CURR_INDEX
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.RADIS
import com.xingfeng.opengles.util.*
import com.xingfeng.opengles.util.Constant.initTexture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GL2314SurfaceView(context: Context) : GLSurfaceView(context) {
    private var mRenderer: SceneRenderer

    private var Offset = 20.0f

    // 视线方向
    private var direction: Float = 0.0f

    private val DEGREE_SPAN: Float = 3.0.toFloat() / (180.0f * Math.PI).toFloat()

    private val TOUCH_SCAL_FACTOR = 180.0f / 320

    private var mPreviousY: Float = 0.0f
    private var mPreviousX: Float = 0.0f

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
                flag=false
            }
        }

        //设置新的观察目标点XZ坐标

        //设置新的观察目标点XZ坐标
        cx = (Math.sin(direction.toDouble()) * Offset).toFloat() //观察目标点x坐标

        cz = (Math.cos(direction.toDouble()) * Offset).toFloat() //观察目标点z坐标

        //重新计算Up向量
        //重新计算Up向量
        ux = -cx //观察目标点x坐标

        uy = Math.abs((cx * tx + cz * tz - cx * cx - cz * cz) / (ty - cy)) //观察目标点y坐标

        uz = -cz //观察目标点z坐标

        //计算粒子的朝向
        //计算粒子的朝向
        for (i in 0 until count) {
            SceneRenderer.fps.get(i).calculateBillboardDirection()
        }
        SceneRenderer.fps.sort()
        //重新设置摄像机的位置
        //重新设置摄像机的位置
        MatrixState.setCamera(cx, cy, cz, tx, ty, tz, ux, uy, uz)
        //根据粒子与摄像机的距离进行排序
        //根据粒子与摄像机的距离进行排序
        return true
    }


    class SceneRenderer(view: View) : Renderer {
        // 摄像机x坐标
        companion object {
             var count = 0
            var cx: Float = 0.0f


            //摄像机y坐标
            var cy: Float = 18.0f

            //摄像机z坐标
            var cz: Float = 20.0f


            //观察目标点x坐标
            var tx: Float = 0.0f

            //观察目标点y坐标
            var ty: Float = 5.0f

            //观察目标点z坐标
            var tz: Float = 0.0f
            var ux: Float = -cx

            //观察目标点y坐标
            var uy = Math.abs((cx * tx + cz * tz - cz * cz) / (ty - cy));

            //观察目标点z坐标
            var uz = -cz

            var fps = mutableListOf<ParticleSystem>()
        }


        private var mView: View = view
        private var timeStart = System.nanoTime()

        lateinit var wallsForDraw: WallsForwDraw
        lateinit var brazier: LoadedObjectVertexNormalTexture7

        var fpfd = emptyArray<ParticleForDraw?>()
        var textureId = 0


        // 系统火焰分配的纹理id
        private var textureIdFire = 0

        //系统火盆分配的纹理id
        private var textureIdbrazier = 0

        private var countt = 0

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //初始化纹理坐标

            //初始化纹理坐标
            for (i in 0 until ParticleDataConstant.walls.size) {
                ParticleDataConstant.walls[i] = initTexture(mView.resources, R.drawable.wall0 + i)
            }
            textureIdbrazier = initTexture(mView.resources, R.drawable.brazier)
            count = ParticleDataConstant.START_COLOR.size
            fpfd = arrayOfNulls<ParticleForDraw>(count) //4组绘制着，4种颜色

            //创建粒子系统
            //创建粒子系统
            for (i in 0 until count) {
                CURR_INDEX = i
                fpfd[i] = ParticleForDraw(mView, RADIS.get(CURR_INDEX))
                //创建对象,将火焰的初始位置传给构造器
                fps.add(ParticleSystem(ParticleDataConstant.positionFireXZ[i][0], ParticleDataConstant.positionFireXZ[i][1], fpfd[i], ParticleDataConstant.COUNT.get(i)))
            }
            wallsForDraw = WallsForwDraw(mView)
            //加载要绘制的物体
            //加载要绘制的物体
            brazier = LoadUtil.loadFromFile7("chapter301/chapter301.14/brazier.obj", mView.getResources(), mView)
            //设置屏幕背景色RGBA
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.6f, 0.3f, 0.0f, 1.0f)
            //打开深度检测
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //初始化纹理
            //初始化纹理
            textureIdFire = initTexture(mView.resources, R.drawable.fire)
            //关闭背面剪裁
            //关闭背面剪裁
            GLES30.glDisable(GLES30.GL_CULL_FACE)
        }

        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲

            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()
            //绘制墙体
            //绘制墙体
            //wallsForDraw.drawSelf()
            MatrixState.translate(0f, 2.5f, 0f)
            for (i in 0 until count) {
                MatrixState.pushMatrix()
                MatrixState.translate(ParticleDataConstant.positionBrazierXZ.get(i).get(0), -2f, ParticleDataConstant.positionBrazierXZ.get(i).get(1))
                //若加载的物体部位空则绘制物体
                if (brazier != null) {
                    brazier.drawSelf(textureIdbrazier)
                }
                MatrixState.popMatrix()
            }
            MatrixState.translate(0f, 0.65f, 0f)
            for (i in 0 until count) {
                MatrixState.pushMatrix()
                fps.get(i).drawSelf(textureIdFire)
                MatrixState.popMatrix()
            }
            MatrixState.popMatrix()
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
            MatrixState.setProjectFrustum(-0.3f * ratio, 0.3f * ratio, -1 * 0.3f, 1 * 0.3f, 1f, 100f)
            //调用此方法产生摄像机9参数位置矩阵
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(cx, cy, cz, tx, ty, tz, ux, uy, uz)
            //初始化变换矩阵
            //初始化变换矩阵
            MatrixState.setInitStack()
            //初始化光源位置
            //初始化光源位置
            MatrixState.setLightLocation(0f, 15f, 0f)
        }
    }
}