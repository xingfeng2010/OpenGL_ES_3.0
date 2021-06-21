package com.xingfeng.opengles.chapter23.chapter2314

import android.opengl.GLES30
import android.view.View
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class Wall constructor(view: View, wallsLength: Float) {
    var mProgram //自定义渲染管线程序id
            = 0
    var muMVPMatrixHandle //总变换矩阵引用id
            = 0
    var muMMatrixHandle //位置、旋转变换矩阵
            = 0
    var maLightLocationHandle //光源位置属性引用
            = 0
    var maCameraHandle //摄像机位置属性引用
            = 0
    var maNormalHandle //顶点法向量属性引用
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
    var mNormalBuffer //顶点法向量数据缓冲
            : FloatBuffer? = null
    var mTexCoorBuffer //顶点纹理坐标数据缓冲
            : FloatBuffer? = null
    var vCount = 0

    //立方体屋子以1为单位长度，进行的缩放比例
    var wallsLength = wallsLength


    init {
        initVertextData(wallsLength)
        initShader(view)
    }

    private fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader= ShaderUtil.loadFromAssetsFile("chapter301/chapter301.14/vertex_brazier.glsl", mv.resources)
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter301/chapter301.14/frag_brazier.glsl", mv.resources)
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点颜色属性引用
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中光源位置引用
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");

        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取程序中摄像机位置引用
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera");
    }

    private fun initVertextData(wallsLength: Float) {
//顶点坐标数据的初始化================begin============================

        //顶点坐标数据的初始化================begin============================
        vCount = 6
        val vertices = floatArrayOf(
                -wallsLength, 0f, -wallsLength,
                wallsLength, 0f, wallsLength,
                -wallsLength, 0f, wallsLength,
                -wallsLength, 0f, -wallsLength,
                wallsLength, 0f, -wallsLength,
                wallsLength, 0f, wallsLength)

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        val vbb: ByteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) //设置字节顺序

        mVertexBuffer = vbb.asFloatBuffer() //转换为Float型缓冲

        mVertexBuffer!!.put(vertices) //向缓冲区中放入顶点坐标数据

        mVertexBuffer!!.position(0) //设置缓冲区起始位置

        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点法向量的初始化================end============================
        //顶点法向量的初始化================begin============================
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点法向量的初始化================end============================
        //顶点法向量的初始化================begin============================
        val normalVertex = floatArrayOf(0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f)
        //创建顶点纹理坐标数据缓冲
        //创建顶点纹理坐标数据缓冲
        val nbb: ByteBuffer = ByteBuffer.allocateDirect(normalVertex.size * 4)
        nbb.order(ByteOrder.nativeOrder()) //设置字节顺序

        mNormalBuffer = nbb.asFloatBuffer() //转换为Float型缓冲

        mNormalBuffer!!.put(normalVertex) //向缓冲区中放入顶点着色数据

        mNormalBuffer!!.position(0) //设置缓冲区起始位置

        //顶点纹理坐标数据的初始化================begin============================
        //顶点纹理坐标数据的初始化================begin============================
        val texCoor = floatArrayOf(0f, 0f, 1f, 1f, 0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f)
        //创建顶点纹理坐标数据缓冲
        //创建顶点纹理坐标数据缓冲
        val cbb: ByteBuffer = ByteBuffer.allocateDirect(texCoor.size * 4)
        cbb.order(ByteOrder.nativeOrder()) //设置字节顺序

        mTexCoorBuffer = cbb.asFloatBuffer() //转换为Float型缓冲

        mTexCoorBuffer!!.put(texCoor) //向缓冲区中放入顶点着色数据

        mTexCoorBuffer!!.position(0) //设置缓冲区起始位置

        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    fun drawSelf(texId: Int) {
        //制定使用某套shader程序

        //制定使用某套shader程序
        GLES30.glUseProgram(mProgram)
        //将最终变换矩阵传入shader程序
        //将最终变换矩阵传入shader程序
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)
        //将位置、旋转变换矩阵传入着色器程序
        //将位置、旋转变换矩阵传入着色器程序
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0)
        //将光源位置传入着色器程序
        //将光源位置传入着色器程序
        GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB)
        //将摄像机位置传入着色器程序
        //将摄像机位置传入着色器程序
        GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB)

        //为画笔指定顶点位置数据

        //为画笔指定顶点位置数据
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        )

        //将顶点法向量数据传入渲染管线

        //将顶点法向量数据传入渲染管线
        GLES30.glVertexAttribPointer(
                maNormalHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mNormalBuffer
        )
        //为画笔指定顶点纹理坐标数据
        //为画笔指定顶点纹理坐标数据
        GLES30.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        )
        //允许顶点位置数据数组
        //允许顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        GLES30.glEnableVertexAttribArray(maNormalHandle)
        GLES30.glEnableVertexAttribArray(maTexCoorHandle)

        //绑定纹理

        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId)

        //绘制纹理矩形

        //绘制纹理矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }
}