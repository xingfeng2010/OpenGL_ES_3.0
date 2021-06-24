package com.xingfeng.opengles.chapter23.chapter2315

import android.opengl.GLES30
import android.view.View
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.lock
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ShaderUtil
import com.xingfeng.opengles.util.ShaderUtil.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class PointParticleForDraw(view: View, halfSize: Float) {
    var mProgram //自定义渲染管线程序id
            = 0
    var muMVPMatrixHandle //总变换矩阵引用id
            = 0
    var muLifeSpan //
            = 0
    var muBj //单个粒子的半径引用id
            = 0
    var muStartColor //起始颜色引用id
            = 0
    var muEndColor //终止颜色引用id
            = 0
    var muCameraPosition //摄像机位置
            = 0
    var muMMatrix //基本变换矩阵总矩阵
            = 0
    var maPositionHandle //顶点位置属性引用id
            = 0
    var mVertexShader //顶点着色器
            : String? = null
    var mFragmentShader //片元着色器
            : String? = null

    var mVertexBuffer //顶点坐标数据缓冲
            : FloatBuffer? = null
    var vCount = 0


    private var halfSize:Float = halfSize

    init {
        initShader(view)
    }

    //更新顶点坐标数据缓冲的方法
    fun updatVertexData(points: FloatArray?) {
        mVertexBuffer?.run {
            clear() //清空顶点坐标数据缓冲
            put(points) //向缓冲区中放入顶点坐标数据
            position(0) //设置缓冲区起始位置
        }
    }

    //初始化顶点坐标与纹理坐标数据的方法
    fun initVertexData(points: FloatArray) {
        //顶点坐标数据的初始化================begin============================
        vCount = points.size / 4 //个数

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        mVertexBuffer = ByteBuffer.allocateDirect(points.size * 4).run {
            order(ByteOrder.nativeOrder()) //设置字节顺序
            asFloatBuffer()
        }

        mVertexBuffer?.run {
            put(points) //向缓冲区中放入顶点坐标数据
            position(0) //设置缓冲区起始位置
        }
    }

    //初始化着色器
    fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("chapter301/chapter301.15/vertex.glsl", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter301/chapter301.15/frag.glsl", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取程序中衰减因子引用id
        muLifeSpan=GLES30.glGetUniformLocation(mProgram, "maxLifeSpan");
        //获取程序中半径引用id
        muBj=GLES30.glGetUniformLocation(mProgram, "bj");
        //获取起始颜色引用id
        muStartColor=GLES30.glGetUniformLocation(mProgram, "startColor");
        //获取终止颜色引用id
        muEndColor=GLES30.glGetUniformLocation(mProgram, "endColor");
        //获取摄像机位置引用id
        muCameraPosition=GLES30.glGetUniformLocation(mProgram, "cameraPosition");
        //获取基本变换矩阵总矩阵引用id
        muMMatrix=GLES30.glGetUniformLocation(mProgram, "uMMatrix");
    }

    fun drawSelf(texId: Int, startColor: FloatArray?, endColor: FloatArray?, maxLifeSpan: Float) {
        //制定使用某套shader程序

        //制定使用某套shader程序
        GLES30.glUseProgram(mProgram)
        //将最终变换矩阵传入shader程序
        //将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)
        //
        //
        GLES30.glUniform1f(muLifeSpan, maxLifeSpan)
        //将半径传入shader程序
        //将半径传入shader程序
        GLES30.glUniform1f(muBj, halfSize)
        //将起始颜色送入渲染管线
        //将起始颜色送入渲染管线
        GLES30.glUniform4fv(muStartColor, 1, startColor, 0)
        //将终止颜色送入渲染管线
        //将终止颜色送入渲染管线
        GLES30.glUniform4fv(muEndColor, 1, endColor, 0)
        //将摄像机位置传入渲染管线
        //将摄像机位置传入渲染管线
        GLES30.glUniform3f(muCameraPosition, MatrixState.cameraLocation[0], 20.0f, MatrixState.cameraLocation[2])
        //将基本变换矩阵总矩阵传入渲染管线
        //将基本变换矩阵总矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMMatrix, 1, false, MatrixState.getMMatrix(), 0)

        //允许顶点位置数据数组

        //允许顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)

        //绑定纹理

        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId)

        synchronized(lock) {
            //加锁--防止在将顶点坐标数据送入渲染管线时，更新顶点坐标数据
            //将顶点坐标数据送入渲染管线
            GLES30.glVertexAttribPointer(
                    maPositionHandle,
                    4,
                    GLES30.GL_FLOAT,
                    false,
                    4 * 4,
                    mVertexBuffer
            )
            //绘制点
            GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount)
        }
    }
}