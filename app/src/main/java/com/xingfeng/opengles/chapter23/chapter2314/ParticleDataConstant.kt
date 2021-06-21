package com.xingfeng.opengles.chapter23.chapter2314

import android.opengl.GLES30




object ParticleDataConstant {
    //资源访问锁
    var lock = Any()

    //墙体的长度缩放比
    var wallsLength = 30f

    //当前索引
    var CURR_INDEX = 3

    //火焰的初始总位置
    var distancesFireXZ = 6f

    //火盆的初始总位置
    var distancesBrazierXZ = 6f
    var positionFireXZ = arrayOf(
            floatArrayOf(distancesFireXZ, distancesFireXZ),
            floatArrayOf(distancesFireXZ, -distancesFireXZ),
            floatArrayOf(-distancesFireXZ, distancesFireXZ),
            floatArrayOf(-distancesFireXZ, -distancesFireXZ)
    )
    var positionBrazierXZ = arrayOf(
            floatArrayOf(distancesBrazierXZ, distancesBrazierXZ),
            floatArrayOf(distancesBrazierXZ, -distancesBrazierXZ),
            floatArrayOf(-distancesBrazierXZ, distancesBrazierXZ),
            floatArrayOf(-distancesBrazierXZ, -distancesBrazierXZ)
    )
    var walls = IntArray(6)

    val START_COLOR = arrayOf(
            floatArrayOf(0.7569f, 0.2471f, 0.1176f, 1.0f),
            floatArrayOf(0.7569f, 0.2471f, 0.1176f, 1.0f),
            floatArrayOf(0.6f, 0.6f, 0.6f, 1.0f),
            floatArrayOf(0.6f, 0.6f, 0.6f, 1.0f)
    )

    val END_COLOR = arrayOf(
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    )

    val SRC_BLEND = intArrayOf( //源混合因子
            GLES30.GL_SRC_ALPHA,  //0-普通火焰
            GLES30.GL_ONE,  //1-白亮火焰
            GLES30.GL_SRC_ALPHA,  //2-普通烟
            GLES30.GL_ONE)

    val DST_BLEND = intArrayOf( //目标混合因子
            GLES30.GL_ONE,  //0-普通火焰
            GLES30.GL_ONE,  //1-白亮火焰
            GLES30.GL_ONE_MINUS_SRC_ALPHA,  //2-普通烟
            GLES30.GL_ONE)

    val BLEND_FUNC = intArrayOf( //混合方式
            GLES30.GL_FUNC_ADD,  //0-普通火焰
            GLES30.GL_FUNC_ADD,  //1-白亮火焰
            GLES30.GL_FUNC_ADD,  //2-普通烟
            GLES30.GL_FUNC_REVERSE_SUBTRACT)

    val COUNT = intArrayOf( //总粒子数
            340,  //0-普通火焰
            340,  //1-白亮火焰
            99,  //2-普通烟
            99)

    val RADIS = floatArrayOf( //单个粒子半径
            0.5f,  //0-普通火焰
            0.5f,  //1-白亮火焰
            0.8f,  //2-普通烟
            0.8f)

    val MAX_LIFE_SPAN = floatArrayOf( //粒子最大生命期
            5.0f,  //0-普通火焰
            5.0f,  //1-白亮火焰
            6.0f,  //2-普通烟
            6.0f)

    val LIFE_SPAN_STEP = floatArrayOf( //粒子生命期步进
            0.07f,  //0-普通火焰
            0.07f,  //1-白亮火焰
            0.07f,  //2-普通烟
            0.07f)

    val X_RANGE = floatArrayOf( //粒子发射的x左右范围
            0.5f,  //0-普通火焰
            0.5f,  //1-白亮火焰
            0.5f,  //2-普通烟
            0.5f)


    val Y_RANGE = floatArrayOf( //粒子发射的y上下范围
            0.3f,  //0-普通火焰
            0.3f,  //1-白亮火焰
            0.15f,  //2-普通烟
            0.15f)

    val GROUP_COUNT = intArrayOf( //每批激活的粒子数量
            4,  //0-普通火焰
            4,  //1-白亮火焰
            1,  //2-普通烟
            1)


    val VY = floatArrayOf( //粒子y方向升腾的速度
            0.05f,  //0-普通火焰
            0.05f,  //1-白亮火焰
            0.04f,  //2-普通烟
            0.04f)

    val THREAD_SLEEP = intArrayOf( //粒子更新物理线程休眠时间（ms）
            60,  //0-普通火焰
            60,  //1-白亮火焰
            30,  //2-普通烟
            30)
}