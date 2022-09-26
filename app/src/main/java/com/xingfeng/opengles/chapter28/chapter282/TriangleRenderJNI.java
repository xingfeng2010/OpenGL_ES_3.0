package com.xingfeng.opengles.chapter28.chapter282;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderJNI implements GLSurfaceView.Renderer {

    static {
        System.loadLibrary("anative");
    }

    public TriangleRenderJNI(Context context){
        mAssetMgr = context.getAssets();
        if (null == mAssetMgr){
            Log.e("TAG", "TriangleRenderJNI: " + "getAssets return null !");
        }
    }

    private AssetManager mAssetMgr = null;

    public native void glesInit();
    public native void glesRender();
    public native void glesResize(int width,int height);
    public native void readShaderFile(AssetManager assetManager);


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        readShaderFile(mAssetMgr);
        glesInit();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glesResize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glesRender();
    }
}
