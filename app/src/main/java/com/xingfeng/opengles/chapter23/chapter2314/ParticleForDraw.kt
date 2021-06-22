package com.xingfeng.opengles.chapter23.chapter2314

import android.opengl.GLES30
import android.view.View
import com.xingfeng.opengles.chapter23.chapter2314.ParticleDataConstant.lock
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ShaderUtil
import com.xingfeng.opengles.util.ShaderUtil.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class ParticleForDraw(view: View, halfSize: Float) {
    var mProgram //自定义渲染管线程序id
            = 0
    var muMVPMatrixHandle //总变换矩阵引用id
            = 0
    var muLifeSpan //衰减因子引用id
            = 0
    var muBj //半径引用id
            = 0
    var muStartColor //起始颜色引用id
            = 0
    var muEndColor //终止颜色引用id
            = 0
    var maPositionHandle //顶点位置属性引用id
            = 0
    var maTexCoorHandle //顶点纹理坐标属性引用id
            = 0
    var mVertexShader //顶点着色器
            : String? = null
    var mFragmentShader //片元着色器
            : String? = null

    var mVertexBuffer //顶点坐标数据缓冲
            : FloatBuffer? = null
    var mTexCoorBuffer //顶点纹理坐标数据缓冲
            : FloatBuffer? = null

    var vCount = 0 //顶点个数


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

        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
        //顶点纹理坐标数据的初始化================begin============================
        val texCoor = FloatArray(vCount * 2) //顶点颜色值数组，每个顶点4个色彩值RGBA
        for (i in 0 until vCount / 6) {
            texCoor[12 * i] = 0.0f
            texCoor[12 * i + 1] = 0f
            texCoor[12 * i + 2] = 0f
            texCoor[12 * i + 3] = 1f
            texCoor[12 * i + 4] = 1f
            texCoor[12 * i + 5] = 0f
            texCoor[12 * i + 6] = 1f
            texCoor[12 * i + 7] = 0f
            texCoor[12 * i + 8] = 0f
            texCoor[12 * i + 9] = 1f
            texCoor[12 * i + 10] = 1f
            texCoor[12 * i + 11] = 1f
        }

        //创建顶点纹理坐标数据缓冲
        mTexCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * 4).run {
            order(ByteOrder.nativeOrder()) //设置字节顺序
            asFloatBuffer()
        }

        mTexCoorBuffer?.run {
            clear()
            put(texCoor) //向缓冲区中放入顶点着色数据
            position(0) //设置缓冲区起始位置
        }

        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    //初始化着色器
    fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("chapter301/chapter301.14/vertex.glsl", mv.getResources())
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("chapter301/chapter301.14/frag.glsl", mv.getResources())
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader)
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor")
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        //
        muLifeSpan = GLES30.glGetUniformLocation(mProgram, "maxLifeSpan")
        //获取程序中半径引用id
        muBj = GLES30.glGetUniformLocation(mProgram, "bj")
        //获取起始颜色引用id
        muStartColor = GLES30.glGetUniformLocation(mProgram, "startColor")
        //获取终止颜色引用id
        muEndColor = GLES30.glGetUniformLocation(mProgram, "endColor")
    }

    fun drawSelf(texId: Int, startColor: FloatArray?, endColor: FloatArray?, maxLifeSpan: Float) {
        //制定使用某套shader程序
        GLES30.glUseProgram(mProgram)
        //将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)
        //
        GLES30.glUniform1f(muLifeSpan, maxLifeSpan)
        //将半径传入shader程序
        GLES30.glUniform1f(muBj, halfSize * 60)
        //将起始颜色送入渲染管线
        GLES30.glUniform4fv(muStartColor, 1, startColor, 0)
        //将终止颜色送入渲染管线
        GLES30.glUniform4fv(muEndColor, 1, endColor, 0)

        //将顶点纹理坐标数据送入渲染管线
        GLES30.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        )
        //允许顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        GLES30.glEnableVertexAttribArray(maTexCoorHandle)

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
            //绘制纹理矩形
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
        }
    }
}