package com.xingfeng.opengles.chapter211.chapter2112;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

//����������
public class GrassObject 
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muVPMatrixHandle;//�������ͶӰ��Ͼ�������id
    int muMMatrixHandle;//�����任��������id
    int maPositionHandle; //����λ����������id  
    int maTexCoorHandle; //��������������������id  
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
    int vCount=0;
    int uTexHandle;//С��������������id  
    int uRDTexHandle;//�Ŷ�������������id
    int uJBTexHandle;//��ɫ������������id
    int uTotalHandle;//�ܵ�С�ݿ�������id
    
   
    public GrassObject(View mv, float[] vertices, float texCoors[])
    {
    	//��ʼ���������ݵķ���
    	initVertexData(vertices,texCoors);
    	//��ʼ����ɫ��      
    	initShader(mv);
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData(float[] vertices,float texCoors[])
    {
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=vertices.length/3;   
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //���������������ݵĳ�ʼ��================begin============================  
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ��
    public void initShader(View mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader= ShaderUtil.loadFromAssetsFile(Constant.OBJ_VER_PATH, mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile(Constant.OBJ_FRAG_PATH, mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������������ͶӰ��Ͼ�������id
        muVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uVPMatrix");  
        //��ȡ�����л����任��������id
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡС�ݡ��Ŷ�������������id
        uTexHandle=GLES30.glGetUniformLocation(mProgram, "ssTexture");  
        uRDTexHandle=GLES30.glGetUniformLocation(mProgram, "sTexture");
        //��ȡ�������ܵ�С�ݿ�����id
        uTotalHandle=GLES30.glGetUniformLocation(mProgram, "totalNum");
        //��ɫ������������id
        uJBTexHandle=GLES30.glGetUniformLocation(mProgram, "jbTexture");
    }
    
    public void drawSelf(int texId,int texRDId,int jbTexId,int totalNum)
    {        
    	 //ָ��ʹ��ĳ����ɫ����
    	 GLES30.glUseProgram(mProgram);        
    	 //��ʼ���任����
    	 MatrixState.setInitStack();
         //���������ͶӰ��Ͼ�������Ⱦ����
         GLES30.glUniformMatrix4fv(muVPMatrixHandle, 1, false, MatrixState.getVPMatrix(), 0); 
         //�������任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getmMatrix(), 0); 
         //������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );
         //�����������������ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );
         //���ö���λ�á�����������������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //��С������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         //���Ŷ�����
         GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texRDId);
         //����ɫ��������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, jbTexId);
         
         GLES30.glUniform1i(uTexHandle, 0);//ͨ������ָ��С������
         GLES30.glUniform1i(uRDTexHandle, 1);   //ͨ������ָ���Ŷ�����
         GLES30.glUniform1i(uJBTexHandle, 2);   //ͨ������ָ����ɫ����
         //С�ݵ��ܿ���
         GLES30.glUniform1f(uTotalHandle, totalNum);
         //����ʵ����Ⱦ
         GLES30.glDrawArraysInstanced(GLES30.GL_TRIANGLES, 0, vCount,totalNum);
    }
}
