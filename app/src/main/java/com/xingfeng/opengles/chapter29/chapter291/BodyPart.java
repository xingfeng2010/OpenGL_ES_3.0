package com.xingfeng.opengles.chapter29.chapter291;
import java.util.ArrayList;
import android.opengl.Matrix;
import android.view.View;

import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture9;
import com.xingfeng.opengles.util.MatrixState;

public class BodyPart{						//���岿��
	LoadedObjectVertexNormalTexture9 lovnt;	//������
	int index; 								//�������� 
	View msv;						//��Ӧ��GLSurfaceView
	float[] mFather=new float[16];  		//�˹����ڸ���������ϵ�е�ʵʱ�任����
	float[] mWorld=new float[16];			//�˹�������������ϵ�е�ʵʱ�任����
    float[] mFatherInit=new float[16];		//�˹����ڸ���������ϵ�еĳ�ʼ�任����
	float[] mWorldInitInver=new float[16];	//�˹�����ʼ���������������ϵ�еı任����������
	float[] finalMatrix=new float[16];		//���ձ任����
	float fx,fy,fz;//�˹�������������������ϵ�е�ԭʼ����
    //�˹����Լ����ӹ����б�
    ArrayList<BodyPart> childs=new ArrayList<BodyPart>();
    BodyPart father;//ָ�򸸹���������
    Robot robot;
    //fx,fy,fzΪ�ӹ�������������������ϵ������    
    public BodyPart(float fx,float fy,float fz,LoadedObjectVertexNormalTexture9 lovnt,int index,View msv,Robot robot){
    	this.index=index;
    	this.msv=msv;    	
    	this.lovnt=lovnt;
    	this.fx=fx;
    	this.fy=fy;
    	this.fz=fz;    	    
    	this.robot=robot;
    }
     //�����վ������ݿ����������õ����վ���
    public void copyMatrixForDraw(){
    	for(int i=0;i<16;i++){
    		
    		robot.fianlMatrixForDrawArray[index][i]=finalMatrix[i];
    	}}
    public void drawSelf(float[][] tempMatrixArray){
    	if(lovnt!=null){
    		MatrixState.pushMatrix();
    		MatrixState.setMatrix(tempMatrixArray[index]);   //�����¾���     	
        	lovnt.drawSelf();
        	MatrixState.popMatrix();   
    	}
    	for(BodyPart bp:childs){   	//�������еĺ���
    		bp.drawSelf(tempMatrixArray);
    	}}
     //��������ÿ���ӹ����ڸ���������ϵ�е�ԭʼ����
    public void initFatherMatrix(){
    	float tx=fx;
    	float ty=fy;
    	float tz=fz;  
    	//����������Ϊ�գ����ӹ����ڸ���������ϵ�е�ԭʼ����
   	     	if(father!=null){
    		tx=fx-father.fx;
    		ty=fy-father.fy;
    		tz=fz-father.fz;
    	}    	
    	//���ɳ�ʼ�Ĵ˹����ڸ���������ϵ�еĳ�ʼ�任����
    	Matrix.setIdentityM(mFather, 0);
    	Matrix.translateM(mFather, 0, tx, ty, tz);   
    	for(int i=0;i<16;i++){
    		mFatherInit[i]=mFather[i];
    	}  	
    		for(BodyPart bc:childs){//Ȼ������Լ������к���
    		bc.initFatherMatrix();
    	}}
      //��μ��������ӹ�����ʼ���������������ϵ�еı任����������
    public void calMWorldInitInver(){
    	Matrix.invertM(mWorldInitInver, 0, mWorld, 0);
    	for(BodyPart bc:childs){//Ȼ������Լ������к���
    		bc.calMWorldInitInver();
    	}}
    public void updateBone(){//��μ������¹�������ķ���
    	//���ȸ��ݸ���������Լ�����������ϵ�еı任����
    	if(father!=null){
    	 //����������Ϊ����˹�������������ϵ�еı任����
          Matrix.multiplyMM(mWorld, 0, father.mWorld, 0, mFather, 0);
    	}
    	else{
    		//��������Ϊ�գ���˹�������������ϵ�еı任����Ϊ�Լ��ڸ���������ϵ�еı任����
    		for(int i=0;i<16;i++){
    			mWorld[i]=mFather[i];
    		}}
    	calFinalMatrix();    	
    	for(BodyPart bc:childs){//Ȼ������Լ������к���
    		bc.updateBone();
    	}}
     //��ȡ���յİ󶨵��˹����Ķ����ܴ˹���Ӱ��Ĵ�Mesh����ϵ������������λ�õ����ձ任����
    public void calFinalMatrix(){
    	Matrix.multiplyMM(finalMatrix, 0, mWorld, 0, mWorldInitInver, 0);
    }
    public void backToIInit(){//���״̬
    	for(int i=0;i<16;i++){
    		mFather[i]=mFatherInit[i];
    	}  	
    	for(BodyPart bc:childs){//Ȼ������Լ������к���
    		bc.backToIInit();
    	}} 
    public void transtate(float x,float y,float z){//�ڸ�����ϵ��ƽ���Լ�
    	Matrix.translateM(mFather, 0, x, y, z);
    }
    public void rotate(float angle,float x,float y,float z){//�ڸ�����ϵ����ת�Լ�
    	Matrix.rotateM(mFather,0,angle,x,y,z);
    }    
    public void addChild(BodyPart child){//����ӹ���
    	childs.add(child);
    	child.father=this;
    }}