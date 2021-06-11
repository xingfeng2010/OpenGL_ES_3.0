package com.xingfeng.opengles.chapter23.chapter233

import android.opengl.GLES30
import android.view.View
import com.xingfeng.opengles.util.Constant
import com.xingfeng.opengles.util.MatrixState
import com.xingfeng.opengles.util.ShaderUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class TextureRect233(
        view: View,
        width: Float,
        height: Float,
        sEnd: Float,
        tEnd: Float,
        userLB:Boolean
) {

    var mProgram = 0
    var muMVPMatrixHandle = 0
    var maPositionHandle = 0
    var maTexCoorHandle = 0

    lateinit var mVertexShader: String
    lateinit var mFragmentShader:String

    lateinit var mVertexBuffer: FloatBuffer
    lateinit var mTexCoorBuffer: FloatBuffer
    var vCount = 0

    var width = 0.0f
    var height = 0.0f

    //按钮右下角的s、t值
    var sEnd = 0.0f
    var tEnd = 0.0f

    var yAngle = 0f //绕y轴旋转的角度
    var xAngle = 0f //绕x轴旋转的角度
    var zAngle = 0f //绕z轴旋转的角度

    init {
        this.width = width
        this.height = height
        this.sEnd = sEnd
        this.tEnd = tEnd
        
        initVertexData()
        initShader(view, userLB)
    }

    private fun initShader(view: View, userLB: Boolean) {
        if (userLB) {
            Constant.OBJ_VER_PATH = "chapter301/chapter301.3/vertex.glsl"
            Constant.OBJ_FRAG_PATH = "chapter301/chapter301.3/frag.glsl"
        } else {
            Constant.OBJ_VER_PATH = "chapter301/chapter301.3/vertexlb.glsl"
            Constant.OBJ_FRAG_PATH = "chapter301/chapter301.3/fraglb.glsl"
        }
        mVertexShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_VER_PATH, view.resources)
        mFragmentShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_FRAG_PATH, view.resources)
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader)
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //获取程序中总变换矩阵id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor")
    }

    private fun initVertexData() {
        vCount =  6
        val vertices = floatArrayOf(
                -width / 2.0f, height / 2.0f, 0f,
                -width / 2.0f, -height / 2.0f, 0f,
                width / 2.0f, height / 2.0f, 0f,
                -width / 2.0f, -height / 2.0f, 0f,
                width / 2.0f, -height / 2.0f, 0f,
                width / 2.0f, height / 2.0f, 0f)


        mVertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        mVertexBuffer.put(vertices)
        mVertexBuffer.position(0)

        var texCoor = floatArrayOf(
                0.0f,0.0f,0.0f,
                tEnd, sEnd, 0.0f,
                0.0f, tEnd, sEnd,
                tEnd, sEnd,0.0f
        )
        mTexCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        mTexCoorBuffer.put(texCoor)
        mTexCoorBuffer.position(0)
    }

    fun drawSelf(texId: Int) {
        //指定使用某套着色器程序

//        MatrixState.rotate(xAngle, 1.0f, 0.0f, 0.0f)
//        MatrixState.rotate(yAngle, 0.0f, 1.0f, 0.0f)

        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram)
        //将最终变换矩阵传入渲染管线
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0)

        // 将顶点纹理坐标数据传入渲染管线

        // 将顶点纹理坐标数据传入渲染管线
        GLES30.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        )
        //启用顶点位置数据、顶点纹理坐标数组
        //启用顶点位置数据、顶点纹理坐标数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        GLES30.glEnableVertexAttribArray(maTexCoorHandle)
        // 绑定纹理
        // 绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)

        //传递被按下的图片id

        //传递被按下的图片id
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId)
        //将顶点法向量数据传入渲染管线
        //将顶点法向量数据传入渲染管线
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        )
        //绘制矩形
        //绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }
}