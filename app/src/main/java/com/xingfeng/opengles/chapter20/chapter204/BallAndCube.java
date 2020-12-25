package com.xingfeng.opengles.chapter20.chapter204;

import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class BallAndCube {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    String mVertexShader;
    String mFragmentShader;

    int vCount = 0;
    int iCount = 0;
    float yAngle = 0;
    float xAngle = 0;
    float zAngle = 0;

    FloatBuffer mVertexMappedBuffer;
    FloatBuffer mTexCoorMappedBuffer;
    IntBuffer mIndicesBuffer;
    FloatBuffer mTexCoorBuffer;

    //顶点坐标数据缓冲 id
    int mVertexBufferId;
    //顶点纹理数据缓冲id
    int mTexCoorBufferId;
    int mIndicesBufferId;

    //顶点坐标数据的映射缓冲
    ByteBuffer vbb1;
    //原始球的顶点坐标数组
    float[] vertices;
    //原始正方体的顶点坐标数组
    float[] verticesCube;
    int[] indices;
    //纹理数组
    float[] texCoors;
    public float[] curBallForDraw;
    public float[] curBallForCal;
    //切分为30份
    float span = 30;

    //存放顶点坐标的ArrayList
    ArrayList<Float> alVertix = new ArrayList<Float>();
    //存放对应正方体的顶点坐标的ArrayList
    ArrayList<Float> alVertix1 = new ArrayList<Float>();
    //存放纹理坐标的ArrayList
    ArrayList<Float> alVertixTexCoor = new ArrayList<Float>();
    //存放顶点坐标的ArrayList
    ArrayList<Integer> alVertixIndice = new ArrayList<Integer>();

    //临时存放顶点坐标的ArrayList
    ArrayList<Float> alVertix2 = new ArrayList<Float>();
    //临时存放对应正方体的顶点坐标的ArrayList
    ArrayList<Float> alVertixCube2 = new ArrayList<Float>();
    //临时存放纹理坐标的ArrayList
    ArrayList<Float> alVertixTexCoor2 = new ArrayList<Float>();

    //锁对象
    Object lock = new Object();

    public BallAndCube(View view, float r) {
        //计算顶点数据
        initYSData(r);
        //调用初始化顶点数据的方法
        initVertexData();
        //调用初始化着色器的方法
        initShader(view);
    }

    public void initYSData(float r)
    {
        //顶点坐标数据的初始化================begin============================
        final float UNIT_SIZE=0.5f;
        final float angleSpan=5;//将球进行单位切分的角度
        float length=(float) (r*UNIT_SIZE*Math.sin(Math.toRadians(45)));//正方体边长的一半
        float length2=length*2;//正方体边长
        for(float vAngle=90;vAngle>=-90;vAngle=vAngle-angleSpan)//垂直方向angleSpan度一份
        {
            for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//水平方向angleSpan度一份
            {//纵向横向各到一个角度后计算对应的此点在球面上的坐标
                //当前点 ====================start====================
                double xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                //球面上第一个顶点
                float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
                float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
                float y1=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));

                //对应正方体上的第一个顶点
                float x10=x1;
                float z10=z1;
                float y10=y1;

                //第一个顶点的纹理坐标
                float s1=0;
                float t1=0;

                //计算上下面的临界边缘
                if(vAngle==50||vAngle==-45)
                {
                    float x1Temp=0;float z1Temp=0;float y1Temp=0;//球上的顶点
                    float x10Temp=0;float z10Temp=0;	float y10Temp=0;     //对应正方体上的顶点
                    float s2=0;float t2=0;//纹理坐标
                    float xozLengthTemp=0;

                    if(vAngle==50)
                    {
                        //当vAngle等于50时，上面少了45时的一圈顶点，所以计算45度时球的顶点、对应正方体上的顶点、纹理坐标
                        xozLengthTemp=(float) (r*UNIT_SIZE*Math.cos(Math.toRadians(45)));
                        x1Temp=(float)(xozLengthTemp*Math.cos(Math.toRadians(hAngle)));
                        z1Temp=(float)(xozLengthTemp*Math.sin(Math.toRadians(hAngle)));
                        y1Temp=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(45)));
                        y10Temp=length;
                    }
                    else if(vAngle==-45)
                    {
                        //当vAngle等于-45时，下面少了-45时的一圈顶点，所以计算-45度时球的顶点、对应正方体上的顶点、纹理坐标
                        xozLengthTemp=(float) (r*UNIT_SIZE*Math.cos(Math.toRadians(-45)));
                        x1Temp=(float)(xozLengthTemp*Math.cos(Math.toRadians(hAngle)));
                        z1Temp=(float)(xozLengthTemp*Math.sin(Math.toRadians(hAngle)));
                        y1Temp=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(-45)));
                        y10Temp=-length;
                    }

                    if(Math.abs(x1Temp)>Math.abs(z1Temp)){
                        if(x1Temp>0){
                            x10Temp=(float) xozLengthTemp;
                        }else{
                            x10Temp=(float) -xozLengthTemp;
                        }
                        z10Temp=(float) (x10Temp*Math.tan(Math.toRadians(hAngle)));
                    }else{
                        if(z1Temp>0){
                            z10Temp=(float) xozLengthTemp;
                        }else{
                            z10Temp=(float) -xozLengthTemp;
                        }
                        x10Temp=(float) (z10Temp/Math.tan(Math.toRadians(hAngle)));
                    }

                    if(vAngle==50){//计算纹理坐标
                        s2=0.5f+x10Temp/length2;
                        t2=0.5f+z10Temp/length2;
                    }
                    else  if(vAngle==-45){//计算纹理坐标
                        s2=0.5f+x10Temp/length2;
                        t2=(-0.5f+z10Temp/length2)*-1;
                    }
                    //球
                    alVertix2.add(x1Temp);alVertix2.add(y1Temp);alVertix2.add(z1Temp);
                    //正方体
                    alVertixCube2.add(x10Temp);alVertixCube2.add(y10Temp);alVertixCube2.add(z10Temp);
                    //纹理
                    alVertixTexCoor2.add(s2); alVertixTexCoor2.add(t2);
                }

                if(vAngle>45)
                {//如果vAngle大于45时，对应正方体的上面
                    if(Math.abs(x1)>Math.abs(z1)){
                        if(x1>0){
                            x10=(float) xozLength;
                        }else{
                            x10=(float) -xozLength;
                        }
                        z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
                    }else{
                        if(z1>0){
                            z10=(float) xozLength;
                        }else{
                            z10=(float) -xozLength;
                        }
                        x10=(float) (z10/Math.tan(Math.toRadians(hAngle)));
                    }
                    y10=length;
                    s1=0.5f+x10/length2;
                    t1=0.5f+z10/length2;
                }
                else  if(vAngle<(-45))
                {//如果vAngle小于-45时，对应正方体的下面
                    if(Math.abs(x1)>Math.abs(z1)){
                        if(x1>0){
                            x10=(float) xozLength;
                        }else{
                            x10=(float) -xozLength;
                        }
                        z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
                    }else{
                        if(z1>0){
                            z10=(float) xozLength;
                        }else{
                            z10=(float) -xozLength;
                        }
                        x10=(float) (z10/Math.tan(Math.toRadians(hAngle)));
                    }
                    y10=-length;
                    s1=0.5f+x10/length2;
                    t1=1-(0.5f+z10/length2);

                }
                else{
                    if(Math.abs(x1)>Math.abs(z1))
                    {//x>z
                        if(x1>0){
                            x10=length;
                            z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
                            s1=0.5f+z10/length2;
                        }else{
                            x10=-length;
                            z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
                            s1=(-0.5f+z10/length2)*-1;
                        }
                        t1=1-(0.5f+(y10)/length2);
                    }else{
                        if(z1>0)
                        {
                            z10=length;
                            x10=(float)(z10/Math.tan(Math.toRadians(hAngle)));
                            s1=0.5f+x10/length2;
                        }else{
                            z10=-length;
                            x10=(float)(z10/Math.tan(Math.toRadians(hAngle)));
                            s1=-1*(-0.5f+x10/length2);
                        }
                        t1=1-(0.5f+(y10)/length2);
                    }
                }
                //当前点====================end====================

                //将球当前的顶点放入列表中=============
                alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
                //将立方体对应的顶点放入列表中=============
                alVertix1.add(x10);alVertix1.add(y10);alVertix1.add(z10);
                //将纹理坐标放入列表中
                alVertixTexCoor.add(s1); alVertixTexCoor.add(t1);
            }

            if(vAngle==50||vAngle==-45)
            {
                for(int i=0;i<alVertix2.size()/3;i++)
                {
                    alVertix.add(alVertix2.get(i*3));
                    alVertix.add(alVertix2.get(i*3+1));
                    alVertix.add(alVertix2.get(i*3+2));
                }
                for(int i=0;i<alVertixCube2.size()/3;i++)
                {
                    alVertix1.add(alVertixCube2.get(i*3));
                    alVertix1.add(alVertixCube2.get(i*3+1));
                    alVertix1.add(alVertixCube2.get(i*3+2));
                }
                for(int i=0;i<alVertixTexCoor2.size()/3;i++)
                {
                    alVertixTexCoor.add(alVertixTexCoor2.get(i*3));
                    alVertixTexCoor.add(alVertixTexCoor2.get(i*3+1));
                    alVertixTexCoor.add(alVertixTexCoor2.get(i*3+2));
                }

                alVertix2.clear();
                alVertixCube2.clear();
                alVertixTexCoor2.clear();
            }
        }
        //外面卷绕，防止出现断裂情况==========start=============
        int w = (int) (360 / angleSpan);
        for(int i = 0; i <(w+2); i++){
            for(int j = 0; j < w; j++){
                int x = i * w + j;
                alVertixIndice.add(x);
                alVertixIndice.add(x + w);
                alVertixIndice.add(x + 1);
                alVertixIndice.add(x + 1);
                alVertixIndice.add(x + w);
                alVertixIndice.add(x + w + 1);
            }
        }
        //外面卷绕，防止出现断裂情况==========end=============

        vCount=alVertix.size()/3;//顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

        iCount=alVertixIndice.size();
        vertices=new float[vCount*3];
        verticesCube=new float[vCount*3];
        texCoors=new float[alVertixTexCoor.size()];
        curBallForDraw=new float[vertices.length/2];
        curBallForCal=new float[vertices.length/2];
        indices=new int[iCount];
        for(int i=0;i<alVertix.size();i++)
        {//顶点坐标
            vertices[i]=alVertix.get(i);
        }

        for(int i=0;i<iCount;i++)
        {
            indices[i]=alVertixIndice.get(i);
        }

        for(int i=0;i<alVertix1.size();i++)
        {//对应正方体的顶点坐标
            verticesCube[i]=alVertix1.get(i);
        }
        for(int i=0;i<alVertixTexCoor.size();i++)
        {//顶点纹理坐标
            texCoors[i]=alVertixTexCoor.get(i);
        }

    }

    public void initVertexData() {
        //缓冲id数组
        int[] bufferIds = new int[3];
        //生成两个缓冲id
        GLES30.glGenBuffers(3, bufferIds, 0);
        mVertexBufferId=bufferIds[0];
        mTexCoorBufferId=bufferIds[1];
        mIndicesBufferId = bufferIds[2];

        mTexCoorBuffer = ByteBuffer.allocateDirect(texCoors.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTexCoorBuffer.put(texCoors);
        mTexCoorBuffer.position(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTexCoorBufferId);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoors.length * 4, mTexCoorBuffer, GLES30.GL_STATIC_DRAW);

        mIndicesBuffer = ByteBuffer.allocateDirect(indices.length*4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        mIndicesBuffer.put(indices);
        mIndicesBuffer.position(0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndicesBufferId);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, mIndicesBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, null, GLES30.GL_STATIC_DRAW);

        ByteBuffer mapBuffer = (ByteBuffer) GLES30.glMapBufferRange(
                GLES30.GL_ARRAY_BUFFER,//表示顶点数据
                0,//偏移量
                vertices.length * 4,
                GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_BUFFER_BIT
        );
        if (mapBuffer == null) {
            return;
        }
        mapBuffer.order(ByteOrder.nativeOrder());
        mVertexMappedBuffer = mapBuffer.asFloatBuffer();

        mVertexMappedBuffer.put(vertices);
        mVertexMappedBuffer.position(0);
        if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
            return;
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    public void initShader(View view) {
        mVertexShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_VER_PATH, view.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile(Constant.OBJ_FRAG_PATH, view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void updateMapping(float[] currVertex)
    {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        ByteBuffer byteBuffer = (ByteBuffer)GLES30.glMapBufferRange(
                GLES30.GL_ARRAY_BUFFER,
                0,
                currVertex.length * 4,
                GLES30.GL_MAP_WRITE_BIT|GLES30.GL_MAP_INVALIDATE_BUFFER_BIT
        );

        if (byteBuffer == null) {
            return;
        }

        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexMappedBuffer = byteBuffer.asFloatBuffer();
        mVertexMappedBuffer.put(currVertex);
        mVertexMappedBuffer.position(0);
        if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
            return;
        }
    }

    public void drawSelf(int texId) {
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        GLES30.glEnableVertexAttribArray(maTexCoorHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTexCoorBufferId);
        GLES30.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                2 * 4,
                0
        );

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                0
        );

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        synchronized (lock) {
            updateMapping(curBallForDraw);
        }

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndicesBufferId);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount,GLES30.GL_UNSIGNED_INT, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
    }

    public void calVertices(int count, boolean flag) {
        for (int i = 0; i < vertices.length / 2; i++) {
            curBallForCal[i] = insertValue(vertices[i], verticesCube[i], span, count, flag);
        }

        synchronized (lock) {
            curBallForDraw = Arrays.copyOf(curBallForCal, curBallForCal.length);
        }
    }


    //计算插值方法
    public float insertValue(float start,float end,float span,int count,boolean isBallToCubeY)
    {
        float result=0;
        if(isBallToCubeY)
        {
            result=start+count*(end-start)/span;
        }else{
            result=end-count*(end-start)/span;
        }
        return result;
    }
}
