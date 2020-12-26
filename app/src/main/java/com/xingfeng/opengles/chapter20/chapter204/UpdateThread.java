package com.xingfeng.opengles.chapter20.chapter204;


public class UpdateThread extends Thread{

    GL204SurfaceView mv;
    int count=0;
    boolean isBallCube=true;//物体的状态，初始状态为球
    public UpdateThread(GL204SurfaceView mv)
    {
        this.mv=mv;
    }

    public void run()
    {
        while(true)
        {
            //获取顶点坐标数据
            mv.mBallAndCube.calVertices(count,isBallCube);
            try{
                count++;
                if(count%mv.mBallAndCube.span==0)
                {
                    count=0;
                    isBallCube=!isBallCube;
                }
                Thread.sleep(40);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
