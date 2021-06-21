package com.xingfeng.opengles.chapter23.chapter2314

import android.view.View
import com.xingfeng.opengles.util.MatrixState




class WallsForwDraw(view: View) {
    private val wall = Wall(view, ParticleDataConstant.wallsLength)

    fun drawSelf() {
        //绘制第一面墙-底
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, 0f)
        wall.drawSelf(ParticleDataConstant.walls[0])
        MatrixState.popMatrix()

        //绘制第二面墙-上
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 2 * ParticleDataConstant.wallsLength, 0f)
        wall.drawSelf(ParticleDataConstant.walls[1])
        MatrixState.popMatrix()

        //绘制第三面墙-左
        MatrixState.pushMatrix()
        MatrixState.translate(-ParticleDataConstant.wallsLength, ParticleDataConstant.wallsLength, 0f)
        MatrixState.rotate(90f, 0f, 0f, 1f)
        MatrixState.rotate(-90f, 0f, 1f, 0f)
        wall.drawSelf(ParticleDataConstant.walls[2])
        MatrixState.popMatrix()

        //绘制第四面墙-右
        MatrixState.pushMatrix()
        MatrixState.translate(ParticleDataConstant.wallsLength, ParticleDataConstant.wallsLength, 0f)
        MatrixState.rotate(-90f, 0f, 0f, 1f)
        MatrixState.rotate(90f, 0f, 1f, 0f)
        wall.drawSelf(ParticleDataConstant.walls[3])
        MatrixState.popMatrix()

        //绘制第五面墙-前
        MatrixState.pushMatrix()
        MatrixState.translate(0f, ParticleDataConstant.wallsLength, ParticleDataConstant.wallsLength)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        wall.drawSelf(ParticleDataConstant.walls[4])
        MatrixState.popMatrix()

        //绘制第六面墙-后
        MatrixState.pushMatrix()
        MatrixState.translate(0f, ParticleDataConstant.wallsLength, -ParticleDataConstant.wallsLength)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        MatrixState.rotate(180f, 0f, 0f, 1f)
        wall.drawSelf(ParticleDataConstant.walls[5])
        MatrixState.popMatrix()
    }
}