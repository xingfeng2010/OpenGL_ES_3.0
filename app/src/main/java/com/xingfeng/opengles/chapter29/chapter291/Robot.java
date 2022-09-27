package com.xingfeng.opengles.chapter29.chapter291;

import android.view.View;

import com.xingfeng.opengles.util.LoadedObjectVertexNormalTexture9;
import com.xingfeng.opengles.util.MatrixState;

public class Robot{
	//�����˵ĸ�������
	BodyPart bRoot,bBody,bHead,bLeftTop,bLeftBottom,bRightTop,bRightBottom,
		bRightLegTop,bRightLegBottom,bLeftLegTop,bLeftLegBottom,bLeftFoot,bRightFoot;
	BodyPart[] bpArray;
	//���ڻ��Ƶ����վ�������
	float[][] fianlMatrixForDrawArray;
	//���ڻ��Ƶ���ʱ��������
	float[][] fianlMatrixForDrawArrayTemp;  
	Object lock=new Object();//����������
	public Robot(LoadedObjectVertexNormalTexture9[] lovntArray, View msv){
		bRoot=new BodyPart(0,0,0,null,0,msv,this);//
        bBody=new BodyPart(0.0f,0.938f,0.0f,lovntArray[0],1,msv,this);//���������꣨x,z,y��
        bHead=new BodyPart(0.0f,1.00f,0.0f,lovntArray[1],2,msv,this);
        bLeftTop=new BodyPart(0.107f,0.938f,0.0f,lovntArray[2],3,msv,this);
        bLeftBottom=new BodyPart(0.105f,0.707f,-0.033f,lovntArray[3],4,msv,this);
        bRightTop=new BodyPart(-0.107f,0.938f,0.0f,lovntArray[4],5,msv,this);
        bRightBottom=new BodyPart(-0.105f,0.707f,-0.033f,lovntArray[5],6,msv,this);
        bRightLegTop=new BodyPart(-0.068f,0.6f,0.02f,lovntArray[6],7,msv,this);//y��z����
        bRightLegBottom=new BodyPart(-0.056f,0.312f,0f,lovntArray[7],8,msv,this);
        bLeftLegTop=new BodyPart(0.068f,0.6f,0.02f,lovntArray[8],9,msv,this);
        bLeftLegBottom=new BodyPart(0.056f,0.312f,0f,lovntArray[9],10,msv,this);
        bLeftFoot=new BodyPart(0.068f,0.038f,0.033f,lovntArray[10],11,msv,this);
        bRightFoot=new BodyPart(-0.068f,0.038f,0.033f,lovntArray[11],12,msv,this);
        bpArray=new BodyPart[]{//���еĹ����б�
        		bRoot,bBody,bHead,bLeftTop,bLeftBottom,bRightTop,bRightBottom,
        		bRightLegTop,bRightLegBottom,bLeftLegTop,bLeftLegBottom,bLeftFoot,bRightFoot
		};
        //ÿ������һ������
        fianlMatrixForDrawArray=new float[bpArray.length][16];    
        fianlMatrixForDrawArrayTemp=new float[bpArray.length][16];  
        bRoot.addChild(bBody);
        bBody.addChild(bHead);
        bBody.addChild(bLeftTop);
        bBody.addChild(bRightTop);
        bLeftTop.addChild(bLeftBottom);
        bRightTop.addChild(bRightBottom);
        bBody.addChild(bRightLegTop);
        bRightLegTop.addChild(bRightLegBottom);
        bBody.addChild(bLeftLegTop);
        bLeftLegTop.addChild(bLeftLegBottom);
        bLeftLegBottom.addChild(bLeftFoot);
        bRightLegBottom.addChild(bRightFoot);
        //��������ÿ���ӹ����ڸ���������ϵ�е�ԭʼ���꣬���ҽ�ƽ����Ϣ��¼������
        bRoot.initFatherMatrix();
        //��μ������¹�������ķ�����ʵ��ƽ����Ϣ���������������ϵ
        bRoot.updateBone();
        //��μ��������ӹ�����ʼ���������������ϵ�еı任����������
        bRoot.calMWorldInitInver();
	}  
	public void updateState(){//���߳��е��ô˷���  
		bRoot.updateBone();
	}	
	public void backToInit(){	//���߳��е��ô˷���
		bRoot.backToIInit();
	}	
	public void flushDrawData()	{//���߳��е��ô˷���
		synchronized(lock){//�����������ݿ�������������
			for(BodyPart bp:bpArray){
				bp.copyMatrixForDraw();
			}}}
	public void drawSelf(){	
		synchronized(lock){//����ǰ���������ݿ�������ʱ����
		     for(int i=0;i<bpArray.length;i++){
		    	 for(int j=0;j<16;j++){
		    		 fianlMatrixForDrawArrayTemp[i][j]=fianlMatrixForDrawArray[i][j];
		    	 }}}
		MatrixState.pushMatrix();
		//�Ӹ�������ʼ����
		bRoot.drawSelf(fianlMatrixForDrawArrayTemp);		
		MatrixState.popMatrix();
	}}
