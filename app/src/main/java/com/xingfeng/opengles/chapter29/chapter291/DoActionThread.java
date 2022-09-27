package com.xingfeng.opengles.chapter29.chapter291;

import android.view.View;

public class DoActionThread extends Thread{
    int currActionIndex=0;//��ǰ�������
    int currStep=0;//��ǰ�Ķ������ϸ�ڱ��
    Action currAction;//��ǰ��������
    Robot robot;
    View msv;
    public DoActionThread(Robot robot,View msv){
    	this.robot=robot;
    	this.msv=msv;
    }    
    public void run(){
    	try{
			Thread.sleep(500);
		} 
    	catch (InterruptedException e){
			e.printStackTrace();
		}
    	currAction=ActionGenerator.acArray[currActionIndex];	//�õ���ǰ�Ķ������
    	while(true){
    		 robot.backToInit();//�����ԭʼ�ĳ�ʼ�仯����
    		//����˴ζ����������ˣ��������һ�鶯���Ĳ���
    		if(currStep>=currAction.totalStep){
    			//ȡ����ƶ����ı����һ����Χ��
    			currActionIndex=(currActionIndex+1)%ActionGenerator.acArray.length;
    			currAction=ActionGenerator.acArray[currActionIndex];//���»�ȡ��ǰ�Ķ������
    			currStep=0;//��ǰ�Ķ������ϸ�ڱ�ţ���Ϊ0;
    		}    
    		//��ActionGenerator�и����Ķ������ݽ��зֽ�
    		for(float[] ad:currAction.data){//�޸�����
    			int partIndex=(int) ad[0];//��������
    			int aType=(int)ad[1]; //��������
    			if(aType==0){//��aType==0�˲�������Ϊƽ��
    				float xStart=ad[2];//��ʼλ�õ�x����
    				float yStart=ad[3];//��ʼλ�õ�y����
    				float zStart=ad[4];//��ʼλ�õ�z����
    				float xEnd=ad[5];//��������Ҫ����λ�õ�x����
    				float yEnd=ad[6];//��������Ҫ����λ�õ�y����
    				float zEnd=ad[7];//��������Ҫ����λ�õ�z����
    				//���ݵ�ǰ�Ķ������ϸ�ڱ�ţ��������ǰ��ƽ�ƾ���
    				float currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
    				float currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
    				float currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
    				//����ǰ������ƽ����Ϣ��¼���任����
    				robot.bpArray[partIndex].transtate(currX, currY, currZ);
    			}
    			else if(aType==1){//��aType==0�˲�������Ϊ��ת
    				float startAngle=ad[2];//��ת����ʼ�Ƕ�
    				float endAngle=ad[3];//��ת�Ľ��ܽǶ�
    				//���ݵ�ǰ�Ķ������ϸ�ڱ�ų���ǰ����ת�Ƕ�
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				//�˲�������ת��
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				//����ǰ��������ת��Ϣ��¼���任����
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);
    			}}    
    		robot.updateState();//��μ������¹�������ķ���
    		//�����վ������ݿ����������õ����վ���
    		robot.flushDrawData();
    		currStep++;  //��ǰ�Ķ������ϸ�ڱ��+1  		
    		try{
				Thread.sleep(30);
			}
    		catch (InterruptedException e){
				e.printStackTrace();
    		}}}}