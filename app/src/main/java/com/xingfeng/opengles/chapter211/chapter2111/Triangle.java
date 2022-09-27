package com.xingfeng.opengles.chapter211.chapter2111;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
import android.view.View;

import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

//����������
public class Triangle 
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
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    
    public Triangle(View mv)
    {    	
    	//��ʼ���������ݵķ���
    	initVertexData();
    	//��ʼ����ɫ��        
    	initShader(mv);
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData()
    {
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=3;
        final float UNIT_SIZE=0.15f;
        float vertices[]=new float[]
        {
        	0*UNIT_SIZE,11*UNIT_SIZE,0,
        	-11*UNIT_SIZE,-11*UNIT_SIZE,0,
        	11*UNIT_SIZE,-11*UNIT_SIZE,0,
        };
		
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
        float texCoor[]=new float[]//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
        {
        		0.5f,0, 
        		0,1, 
        		1,1        		
        };        
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
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
    }
    
    public void drawSelf(int texId)
    {        
    	 //ָ��ʹ��ĳ����ɫ������
    	 GLES30.glUseProgram(mProgram);        
    	 //��ʼ���任����
    	 MatrixState.setInitStack();
         
         //������y����ת
         MatrixState.rotate(yAngle, 0, 1, 0);
         //������z����ת
         MatrixState.rotate(zAngle, 0, 0, 1);  
         //������x����ת
         MatrixState.rotate(xAngle, 1, 0, 0);
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
         
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         //��ʵ����Ⱦ��������
         GLES30.glDrawArraysInstanced(GLES30.GL_TRIANGLES, 0, vCount,9);
         
    }
}
