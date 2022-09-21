package com.xingfeng.opengles.chapter27.chapter278

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.util.Constant

open class Chapter278Activity : GLRenderActivity() {
    //SensorManager对象引用
    private lateinit var mySensorManager: SensorManager
    //传感器类型
    private lateinit var myAccelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //获得SensorManager对象
        mySensorManager = getSystemService (SENSOR_SERVICE) as (SensorManager);
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Constant.OBJ_VER_PATH = "chapter701/chapter701.8/vertex.glsl"
        Constant.OBJ_FRAG_PATH = "chapter701/chapter701.8/frag.glsl"

        setGLSurfaceView(GL278SurfaceView(this@Chapter278Activity))
    }

    private val mySensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            //计算出重力在屏幕上的投影方向
            //计算出重力在屏幕上的投影方向
            val values: FloatArray = event.values //获取三个轴方向上的加速度值

            //System.out.println("v[0]="+values[0]+",,v[1]="+values[1]+",,v[2]="+values[2]);
            //System.out.println("v[0]="+values[0]+",,v[1]="+values[1]+",,v[2]="+values[2]);
            Constant.Angle = values[1]
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    override fun onResume() {
        super.onResume()
//        mySensorManager.registerListener(
//            mySensorListener,
//            myAccelerometer,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
    }

    override fun onPause() {
        super.onPause()

        mySensorManager.unregisterListener(mySensorListener);
    }
}
