package com.xingfeng.opengles.util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
import android.view.View;

//���غ�����塪��Я��������Ϣ���Զ������淨����
public class LoadedObjectVertexNormalFace
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int muMMatrixHandle;//λ�á���ת�任����
    int maPositionHandle; //����λ����������  
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դλ����������  
    int maCameraHandle; //�����λ���������� 
    
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�    
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���  
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;  
    
    public LoadedObjectVertexNormalFace(View mv, float[] vertices, float[] normals)
    {    	
    	//��ʼ����������
    	initVertexData(vertices,normals);
    	//��ʼ����ɫ��        
    	initShader(mv);
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData(float[] vertices,float[] normals)
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
        
        //���㷨�������ݵĳ�ʼ��================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ��
    public void initShader(View mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("chapter10/chapter10.1/vertex_light.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("chapter10/chapter10.1/frag_light.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
    }
    
    public void drawSelf()
    {        
    	 //ָ��ʹ��ĳ����ɫ������
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //��λ�á���ת�任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
         //����Դλ�ô�����Ⱦ����  
         GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //�������λ�ô�����Ⱦ���� 
         GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         
         // ������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�����㷨�������ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		3,   
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );   
         //���ö���λ�á���������������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maNormalHandle);  
         //���Ʊ����ص�����
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
