package com.xingfeng.opengles.chapter23.chapter232

import android.opengl.GLES30
import android.view.View
import com.xingfeng.opengles.util.Constant.*
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*


//球
class Ball232(mv: View) {
    var mProgram //自定义渲染管线着色器程序id
            = 0
    var muMVPMatrixHandle //总变换矩阵引用
            = 0
    var maPositionHandle //顶点位置属性引用
            = 0
    var maLongLatHandle //顶点经纬度属性引用
            = 0
    var mVertexShader //顶点着色器代码脚本
            : String? = null
    var mFragmentShader //片元着色器代码脚本
            : String? = null
    var mVertexBuffer //顶点坐标数据缓冲
            : FloatBuffer? = null
    var mLongLatBuffer //顶点经纬度数据缓冲
            : FloatBuffer? = null
    var vCount = 0
    var yAngle = 0f //绕y轴旋转的角度
    var xAngle = 0f //绕x轴旋转的角度
    var zAngle = 0f //绕z轴旋转的角度
    var r = 0.8f

    //初始化顶点数据的方法
    fun initVertexData() {
        //顶点坐标数据的初始化================begin============================
        val alVertix = ArrayList<Float>() //存放顶点坐标的ArrayList
        val alLongLat = ArrayList<Float>() //存放顶点经纬度的ArrayList
        val angleSpan = 10 //将球进行单位切分的角度
        var vAngle = -90
        while (vAngle < 90) {
            var hAngle = 0
            while (hAngle <= 360) {
                //纵向横向各到一个角度后计算对应的此点在球面上的坐标
                val x0 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble())).toFloat() * Math.cos(Math.toRadians(hAngle.toDouble())).toFloat())
                val y0 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble())).toFloat() * Math.sin(Math.toRadians(hAngle.toDouble())).toFloat())
                val z0 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                val long0 = hAngle.toFloat()
                val lat0 = vAngle.toFloat()
                val x1 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble())).toFloat() * Math.cos(Math.toRadians(hAngle + angleSpan.toDouble())).toFloat())
                val y1 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble())).toFloat() * Math.sin(Math.toRadians(hAngle + angleSpan.toDouble())).toFloat())
                val z1 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble())).toFloat())
                val long1 = hAngle + angleSpan.toFloat()
                val lat1 = vAngle.toFloat()
                val x2 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat() * Math.cos(Math.toRadians(hAngle + angleSpan.toDouble())).toFloat())
                val y2 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat() * Math.sin(Math.toRadians(hAngle + angleSpan.toDouble())).toFloat())
                val z2 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat())
                val long2 = hAngle + angleSpan.toFloat()
                val lat2 = vAngle + angleSpan.toFloat()
                val x3 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat() * Math.cos(Math.toRadians(hAngle.toDouble())).toFloat())
                val y3 = (r * UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat() * Math.sin(Math.toRadians(hAngle.toDouble())).toFloat())
                val z3 = (r * UNIT_SIZE * Math.sin(Math.toRadians(vAngle + angleSpan.toDouble())).toFloat())
                val long3 = hAngle.toFloat()
                val lat3 = vAngle + angleSpan.toFloat()

                //将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                alVertix.add(x0)
                alVertix.add(y0)
                alVertix.add(z0)
                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)

                //将计算出来的顶点经纬度加入存放顶点经纬度的ArrayList
                alLongLat.add(long1)
                alLongLat.add(lat1)
                alLongLat.add(long3)
                alLongLat.add(lat3)
                alLongLat.add(long0)
                alLongLat.add(lat0)
                alLongLat.add(long1)
                alLongLat.add(lat1)
                alLongLat.add(long2)
                alLongLat.add(lat2)
                alLongLat.add(long3)
                alLongLat.add(lat3)
                hAngle = hAngle + angleSpan
            }
            vAngle = vAngle + angleSpan
        }
        vCount = alVertix.size / 3 //顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

        //将alVertix中的坐标值转存到一个float数组中
        val vertices = FloatArray(vCount * 3)
        for (i in alVertix.indices) {
            vertices[i] = alVertix[i]
        }

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) //设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer() //转换为int型缓冲
        mVertexBuffer?.put(vertices) //向缓冲区中放入顶点坐标数据
        mVertexBuffer?.position(0) //设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题

        //将alLongLat中的经纬度值转存到一个float数组中
        val longlat = FloatArray(alLongLat.size)
        for (i in alLongLat.indices) {
            longlat[i] = alLongLat[i]
        }
        val llbb = ByteBuffer.allocateDirect(longlat.size * 4)
        llbb.order(ByteOrder.nativeOrder()) //设置字节顺序
        mLongLatBuffer = llbb.asFloatBuffer()
        mLongLatBuffer?.put(longlat)
        mLongLatBuffer?.position(0)
    }

    //初始化着色器
    fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile(OBJ_VER_PATH,
                mv.getResources())
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile(OBJ_FRAG_PATH,
                mv.getResources())
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader)
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        //获取程序中顶点经纬度属性引用
        maLongLatHandle = GLES30.glGetAttribLocation(mProgram, "aLongLat")
    }

    fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f) //绕X轴转动
        MatrixState.rotate(yAngle, 0f, 1f, 0f) //绕Y轴转动
        MatrixState.rotate(zAngle, 0f, 0f, 1f) //绕Z轴转动
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram)
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
                MatrixState.getFinalMatrix(), 0)

        //将顶点经纬度数据传入渲染管线
        GLES30.glVertexAttribPointer(
                maLongLatHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                mLongLatBuffer
        )
        //将顶点位置数据传入渲染管线
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer)
        //启用顶点位置、顶点经纬度数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        GLES30.glEnableVertexAttribArray(maLongLatHandle)
        //绘制球
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }

    init {
        //初始化顶点数据
        initVertexData()
        //初始化着色器
        initShader(mv)
    }
}