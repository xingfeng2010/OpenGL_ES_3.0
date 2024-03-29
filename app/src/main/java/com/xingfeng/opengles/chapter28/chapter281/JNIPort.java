package com.xingfeng.opengles.chapter28.chapter281;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;

public class JNIPort
{
   static
   {
      System.loadLibrary("anative");
   }
   public static native void step();
   public static native void onSurfaceChanged(int width,int height);
   public static native void onSurfaceCreated(GLSurfaceView gsv);
   public static native void addBody(int id);
   public static native void setCamera(float cx,float cy,float cz,float upx,float upy,float upz);
   public static native void loadObjData(int objId, float[] vertices, int numsVer, float[] normals, int numsNor);
   public static native void nativeSetAssetManager(AssetManager am);
}