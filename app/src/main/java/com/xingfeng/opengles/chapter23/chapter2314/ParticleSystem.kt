package com.xingfeng.opengles.chapter23.chapter2314

import android.opengl.GLES30
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.BLEND_FUNC
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.CURR_INDEX
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.DST_BLEND
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.END_COLOR
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.GROUP_COUNT
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.LIFE_SPAN_STEP
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.MAX_LIFE_SPAN
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.RADIS
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.SRC_BLEND
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.START_COLOR
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.THREAD_SLEEP
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.VY
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.X_RANGE
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.Y_RANGE
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.lock

import com.xingfeng.opengles.util.MatrixState




class ParticleSystem constructor(positionx: Float, positionz: Float, fpfd: ParticleForDraw?, count: Int) : Comparable<ParticleSystem>{
    lateinit var startColor //粒子起始颜色数组
            : FloatArray
    lateinit var endColor //粒子终止颜色数组
            : FloatArray
    var srcBlend //源混合因子
            = 0
    var dstBlend //目标混合因子
            = 0
    var blendFunc //混合方式
            = 0
    var maxLifeSpan //粒子最大生命期
            = 0f
    var lifeSpanStep //粒子生命期步进
            = 0f
    var sleepSpan //粒子更新线程休眠时间间隔
            = 0
    var groupCount //每批喷发的粒子数量
            = 0
    var sx //基础发射点x坐标
            = 0f
    var sy //基础发射点y坐标
            = 0f
    var positionX //绘制位置x坐标
            = 0f
    var positionZ //绘制位置z坐标
            = 0f
    var xRange //发射点x方向的变化范围
            = 0f
    var yRange //发射点y方向的变化范围
            = 0f
    var vx //粒子发射的x方向速度
            = 0f
    var vy //粒子发射的y方向速度
            = 0f
    var yAngle = 0f //此粒子系统的旋转角度

    var fpfd //粒子群的绘制者
            : ParticleForDraw? = null
    var flag = true //线程工作的标志位

    var halfSize //粒子半径
            = 0f

    lateinit var points //粒子对应的所有顶点数据数组
            : FloatArray

    fun ParticleSystem(positionx: Float, positionz: Float, fpfd: ParticleForDraw, count: Int) { //构造器
        positionX = positionx //初始化此粒子系统的绘制位置x坐标
        positionZ = positionz //初始化此粒子系统的绘制位置y坐标
        startColor = START_COLOR.get(CURR_INDEX) //初始化粒子起始颜色
        endColor = END_COLOR.get(CURR_INDEX) //初始化粒子终止颜色
        srcBlend = SRC_BLEND.get(CURR_INDEX) //初始化源混合因子
        dstBlend = DST_BLEND.get(CURR_INDEX) //初始化目标混合因子
        blendFunc = BLEND_FUNC.get(CURR_INDEX) //初始化混合方式
        maxLifeSpan = MAX_LIFE_SPAN.get(CURR_INDEX) //初始化每个粒子的最大生命期
        lifeSpanStep = LIFE_SPAN_STEP.get(CURR_INDEX) //初始化每个粒子的生命步进
        groupCount = GROUP_COUNT.get(CURR_INDEX) //初始化每批喷发的粒子数
        sleepSpan = THREAD_SLEEP.get(CURR_INDEX) //初始化线程的休眠时间
        sx = 0f //初始化此粒子系统的中心点x坐标
        sy = 0f //初始化此粒子系统的中心点y坐标
        xRange = X_RANGE.get(CURR_INDEX) //初始粒子距离中心点x方向的最大距离
        yRange = Y_RANGE.get(CURR_INDEX) //初始粒子距离中心点y方向的最大距离
        vx = 0f //初始化粒子的x方向运动速度
        vy = VY.get(CURR_INDEX) //初始化粒子的y方向运动速度
        halfSize = RADIS.get(CURR_INDEX) //初始化此粒子系统的粒子半径
        this.fpfd = fpfd //初始化粒子群的绘制者
        points = initPoints(count) //初始化粒子所对应的所有顶点数据数组
        fpfd.initVertexData(points) //调用初始化顶点坐标与纹理坐标数据的方法
        object : Thread() {
            //创建粒子的更新线程
            override fun run() //重写run方法
            {
                while (flag) {
                    update() //调用update方法更新粒子状态
                    try {
                        sleep(sleepSpan.toLong()) //休眠一定的时间
                    } catch (e: InterruptedException) {
                        e.printStackTrace() //打印异常信息
                    }
                }
            }
        }.start() //启动线程
    }

    fun initPoints(zcount: Int): FloatArray //初始化粒子所对应的所有顶点数据的方法
    {
        val points = FloatArray(zcount * 4 * 6) //临时存放顶点数据的数组-每个粒子对应6个顶点，每个顶点包含4个值
        for (i in 0 until zcount) { //循环遍历所有粒子
            //在中心附近产生产生粒子的位置------**/
            val px = (sx + xRange * (Math.random() * 2 - 1.0f)).toFloat() //计算粒子位置的x坐标
            val py = (sy + yRange * (Math.random() * 2 - 1.0f)).toFloat() //计算粒子位置的y坐标
            val vx = (sx - px) / 150 //计算粒子的x方向运动速度
            points[i * 4 * 6] = px - halfSize / 2 //粒子对应的第一个点的x坐标
            points[i * 4 * 6 + 1] = py + halfSize / 2 //粒子对应的第一个点的y坐标
            points[i * 4 * 6 + 2] = vx //粒子对应的第一个点的x方向运动速度
            points[i * 4 * 6 + 3] = 10.0f //粒子对应的第一个点的当前生命期--10代表粒子处于未激活状态
            points[i * 4 * 6 + 4] = px - halfSize / 2
            points[i * 4 * 6 + 5] = py - halfSize / 2
            points[i * 4 * 6 + 6] = vx
            points[i * 4 * 6 + 7] = 10.0f
            points[i * 4 * 6 + 8] = px + halfSize / 2
            points[i * 4 * 6 + 9] = py + halfSize / 2
            points[i * 4 * 6 + 10] = vx
            points[i * 4 * 6 + 11] = 10.0f
            points[i * 4 * 6 + 12] = px + halfSize / 2
            points[i * 4 * 6 + 13] = py + halfSize / 2
            points[i * 4 * 6 + 14] = vx
            points[i * 4 * 6 + 15] = 10.0f
            points[i * 4 * 6 + 16] = px - halfSize / 2
            points[i * 4 * 6 + 17] = py - halfSize / 2
            points[i * 4 * 6 + 18] = vx
            points[i * 4 * 6 + 19] = 10.0f
            points[i * 4 * 6 + 20] = px + halfSize / 2
            points[i * 4 * 6 + 21] = py - halfSize / 2
            points[i * 4 * 6 + 22] = vx
            points[i * 4 * 6 + 23] = 10.0f
        }
        for (j in 0 until groupCount) { //循环遍历第一批的粒子
            points[4 * j * 6 + 3] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
            points[4 * j * 6 + 7] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
            points[4 * j * 6 + 11] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
            points[4 * j * 6 + 15] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
            points[4 * j * 6 + 19] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
            points[4 * j * 6 + 23] = lifeSpanStep //设置粒子生命期，不为10时，表示粒子处于活跃状态
        }
        return points //返回所有粒子顶点属性数据数组
    }

//  int countt=0;//计算帧速率的时间间隔次数--计算器
//	long timeStart=System.nanoTime();//开始时间

    //  int countt=0;//计算帧速率的时间间隔次数--计算器
    //	long timeStart=System.nanoTime();//开始时间
    fun drawSelf(texId: Int) { //绘制此粒子系统中所有粒子的方法
//    	if(countt==19)//每十次一计算帧速率
//    	{
//    		long timeEnd=System.nanoTime();//结束时间
//
//    		//计算帧速率
//    		float ps=(float)(1000000000.0/((timeEnd-timeStart)/20));
//    		System.out.println("ps="+ps);
//    		countt=0;//计算器置0
//    		timeStart=timeEnd;//起始时间置为结束时间
//    	}
//    	countt=(countt+1)%20;//更新计数器的值
        GLES30.glDisable(GLES30.GL_DEPTH_TEST) //关闭深度检测
        GLES30.glEnable(GLES30.GL_BLEND) //开启混合
        GLES30.glBlendEquation(blendFunc) //设置混合方式
        GLES30.glBlendFunc(srcBlend, dstBlend) //设置混合因子
        MatrixState.translate(positionX, 1f, positionZ) //执行平移变换
        MatrixState.rotate(yAngle, 0f, 1f, 0f) //执行旋转变换
        MatrixState.pushMatrix() //保护现场
        fpfd!!.drawSelf(texId, startColor, endColor, maxLifeSpan) //绘制粒子群
        MatrixState.popMatrix() //恢复现场
        GLES30.glEnable(GLES30.GL_DEPTH_TEST) //开启深度检测
        GLES30.glDisable(GLES30.GL_BLEND) //关闭混合
    }

    var count = 1 //激活粒子的位置计算器

    fun update() //更新粒子状态的方法
    {
        if (count >= points.size / groupCount / 4 / 6) //计算器超过激活粒子位置时
        {
            count = 0 //重新计数
        }

        //查看生命期以及计算下一位置的相应数据
        for (i in 0 until points.size / 4 / 6) { //循环遍历所有粒子
            if (points[i * 4 * 6 + 3] != 10.0f) //当前为活跃粒子时
            {
                points[i * 4 * 6 + 3] += lifeSpanStep //计算当前生命期
                points[i * 4 * 6 + 7] += lifeSpanStep //计算当前生命期
                points[i * 4 * 6 + 11] += lifeSpanStep //计算当前生命期
                points[i * 4 * 6 + 15] += lifeSpanStep //计算当前生命期
                points[i * 4 * 6 + 19] += lifeSpanStep //计算当前生命期
                points[i * 4 * 6 + 23] += lifeSpanStep //计算当前生命期
                if (points[i * 4 * 6 + 3] > maxLifeSpan) //当前生命期大于最大生命期时---重新设置该粒子参数
                {
                    val px = (sx + xRange * (Math.random() * 2 - 1.0f)).toFloat() //计算粒子位置x坐标
                    val py = (sy + yRange * (Math.random() * 2 - 1.0f)).toFloat() //计算粒子位置y坐标
                    val vx = (sx - px) / 150 //计算粒子x方向的速度
                    points[i * 4 * 6] = px - halfSize / 2 //粒子对应的第一个顶点的x坐标
                    points[i * 4 * 6 + 1] = py + halfSize / 2 //粒子对应的第一个顶点的y坐标
                    points[i * 4 * 6 + 2] = vx //粒子对应的第一个顶点的x方向运动速度
                    points[i * 4 * 6 + 3] = 10.0f //粒子对应的第一个顶点的当前生命期--10代表粒子处于未激活状态
                    points[i * 4 * 6 + 4] = px - halfSize / 2
                    points[i * 4 * 6 + 5] = py - halfSize / 2
                    points[i * 4 * 6 + 6] = vx
                    points[i * 4 * 6 + 7] = 10.0f
                    points[i * 4 * 6 + 8] = px + halfSize / 2
                    points[i * 4 * 6 + 9] = py + halfSize / 2
                    points[i * 4 * 6 + 10] = vx
                    points[i * 4 * 6 + 11] = 10.0f
                    points[i * 4 * 6 + 12] = px + halfSize / 2
                    points[i * 4 * 6 + 13] = py + halfSize / 2
                    points[i * 4 * 6 + 14] = vx
                    points[i * 4 * 6 + 15] = 10.0f
                    points[i * 4 * 6 + 16] = px - halfSize / 2
                    points[i * 4 * 6 + 17] = py - halfSize / 2
                    points[i * 4 * 6 + 18] = vx
                    points[i * 4 * 6 + 19] = 10.0f
                    points[i * 4 * 6 + 20] = px + halfSize / 2
                    points[i * 4 * 6 + 21] = py - halfSize / 2
                    points[i * 4 * 6 + 22] = vx
                    points[i * 4 * 6 + 23] = 10.0f
                } else  //生命期小于最大生命期时----计算粒子的下一位置坐标
                {
                    points[i * 4 * 6] += points[i * 4 * 6 + 2] //计算粒子对应的第一个顶点的x坐标
                    points[i * 4 * 6 + 1] += vy //计算粒子对应的第一个顶点的y坐标
                    points[i * 4 * 6 + 4] += points[i * 4 * 6 + 6]
                    points[i * 4 * 6 + 5] += vy
                    points[i * 4 * 6 + 8] += points[i * 4 * 6 + 10]
                    points[i * 4 * 6 + 9] += vy
                    points[i * 4 * 6 + 12] += points[i * 4 * 6 + 14]
                    points[i * 4 * 6 + 13] += vy
                    points[i * 4 * 6 + 16] += points[i * 4 * 6 + 18]
                    points[i * 4 * 6 + 17] += vy
                    points[i * 4 * 6 + 20] += points[i * 4 * 6 + 22]
                    points[i * 4 * 6 + 21] += vy
                }
            }
        }
        for (i in 0 until groupCount) { //循环发射一批激活计数器所指定位置的粒子
            if (points[groupCount * count * 4 * 6 + 4 * i * 6 + 3] == 10.0f) //如果粒子处于未激活状态时
            {
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 3] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 7] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 11] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 15] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 19] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
                points[groupCount * count * 4 * 6 + 4 * i * 6 + 23] = lifeSpanStep //激活粒子--设置粒子当前的生命周期
            }
        }
        synchronized(lock) { //加锁--防止在更新顶点坐标数据时，将顶点坐标数据送入渲染管线
            fpfd!!.updatVertexData(points) //更新顶点坐标数据缓冲的方法
        }
        count++ //下次激活粒子的位置
    }

    fun calculateBillboardDirection() {
        //根据摄像机位置计算火焰朝向
        val xspan: Float = positionX - GL2314SurfaceView.SceneRenderer.cx
        val zspan: Float = positionZ - GL2314SurfaceView.SceneRenderer.cz
        yAngle = if (zspan <= 0) {
            Math.toDegrees(Math.atan(xspan / zspan.toDouble())).toFloat()
        } else {
            180 + Math.toDegrees(Math.atan(xspan / zspan.toDouble())).toFloat()
        }
    }

    override fun compareTo(another: ParticleSystem): Int {
        //重写的比较两个火焰离摄像机距离的方法
        val xs: Float = positionX - GL2314SurfaceView.SceneRenderer.cx
        val zs: Float = positionZ - GL2314SurfaceView.SceneRenderer.cz
        val xo: Float = another.positionX -GL2314SurfaceView.SceneRenderer.cx
        val zo: Float = another.positionZ - GL2314SurfaceView.SceneRenderer.cz
        val disA = (xs * xs + zs * zs)
        val disB = (xo * xo + zo * zo)
        return if (disA - disB == 0f) 0 else if (disA - disB > 0) -1 else 1
    }
}