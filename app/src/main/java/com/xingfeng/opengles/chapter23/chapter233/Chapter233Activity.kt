package com.xingfeng.opengles.chapter23.chapter233

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant

open class Chapter233Activity : GLRenderActivity() {
    lateinit var mGL233SurfaceView: GL233SurfaceView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化GLSurfaceView

        mGL233SurfaceView = GL233SurfaceView(this)

        setGLSurfaceView(mGL233SurfaceView)
        Constant.OBJ_VER_PATH = "chapter301/chapter301.3/vertex.glsl"


        Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/fraglb.glsl"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gl_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.smoothFilter -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/fraglb.glsl"
            }

            R.id.contrastEnhancement -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_enhansement.glsl"
            }

            R.id.fudiaoEffect -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_fudiao.glsl"
            }

            R.id.huiduEffect -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_huidu.glsl"
            }

            R.id.updownEffect -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_updown.glsl"
            }

            R.id.whirlpoolEffect -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_whirlpool.glsl"
            }

            R.id.masaikeEffect -> {
                Constant.SELECT_FRAG_PATH = "chapter301/chapter301.3/frag_masaike.glsl"
            }
        }

        mGL233SurfaceView.switchProgram()

        return true
    }
}