package com.xingfeng.opengles.chapter211.chapter2112;//������
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

public class GrassLoadUtil
{
	
	//��obj�ļ��м���Я��������Ϣ�����壬���Զ�����ÿ�������ƽ��������
    public static GrassObject loadFromFile
    (String fname, Resources r, View mv)
    {
    	//���غ����������
    	GrassObject lo=null;
    	//ԭʼ���������б�--ֱ�Ӵ�obj�ļ��м���
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//������װ�������б�--���������Ϣ���ļ��м���
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	//������������б�--������֯��
    	ArrayList<Float> alvResult=new ArrayList<Float>();  	
    	//ԭʼ���������б�
    	ArrayList<Float> alt=new ArrayList<Float>();  
    	//������������б�
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//ɨ���ļ������������͵Ĳ�ִͬ�в�ͬ�Ĵ����߼�
		    while((temps=br.readLine())!=null) 
		    {//��ȡһ���ı�
		    	
		    	String[] tempsa=temps.split("[ ]+");//���ı����ÿո���з�
		      	if(tempsa[0].trim().equals("v"))
		      	{//����������
		      	    //��Ϊ��������������ȡ���˶����XYZ������ӵ�ԭʼ���������б���
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("vt"))
		      	{//����������
		      		//��Ϊ��������������ȡST���겢��ӽ�ԭʼ���������б���
		      		alt.add(Float.parseFloat(tempsa[1]));//��ȡ��S��������
		      		alt.add(1-Float.parseFloat(tempsa[2])); 	//��ȡ��T��������
		      	}
		      	else if(tempsa[0].trim().equals("f")) 
		      	{//��������
		      		/*
		      		 *��Ϊ��������������� �����Ķ����������ԭʼ���������б���
		      		 *��ȡ��Ӧ�Ķ�������ֵ��ӵ�������������б��У�ͬʱ��������
		      		 *�����������������ķ���������ӵ�ƽ��ǰ����������Ӧ�ĵ�
		      		 *�ķ�����������ɵ�Map��
		      		*/
		      		
		      		int[] index=new int[3];//������������ֵ������
		      		
		      		//�����0�����������������ȡ�˶����XYZ��������	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index[0]);
		      		float y0=alv.get(3*index[0]+1);
		      		float z0=alv.get(3*index[0]+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //�����1�����������������ȡ�˶����XYZ��������	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index[1]);
		      		float y1=alv.get(3*index[1]+1);
		      		float z1=alv.get(3*index[1]+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //�����2�����������������ȡ�˶����XYZ��������	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index[2]);
		      		float y2=alv.get(3*index[2]+1);
		      		float z2=alv.get(3*index[2]+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);	
		      		
		      		//��¼����Ķ�������
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		//��������3���������������������֯��������������б���
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;//��ȡ����������
		      		//��0���������������
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;//��ȡ����������
		      		//��1���������������
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;//��ȡ����������
		      		//��2���������������
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	}		      		
		    } 
		    
		    //���ɶ�������
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    //������������
		    size=altResult.size();
		    float[] tST=new float[size];//���ڴ�Ž�������������ݵ�����
		    for(int i=0;i<size;i++)
		    {//�������������ݴ�������
		    	tST[i]=altResult.get(i);
		    }
		    
		    //���������������
		    lo=new GrassObject(mv,vXYZ,tST);
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    	
    	return lo;//���ش�����������������
    }
}
