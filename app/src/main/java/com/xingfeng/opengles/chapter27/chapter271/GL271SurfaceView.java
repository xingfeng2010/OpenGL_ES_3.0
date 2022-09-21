package com.xingfeng.opengles.chapter27.chapter271;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.xingfeng.opengles.R;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.ShaderUtil;

import static com.xingfeng.opengles.chapter27.chapter271.Constant.*;

class GL271SurfaceView extends GLSurfaceView
{
	private SceneRenderer mRenderer;//������Ⱦ��	
	DiscreteDynamicsWorld dynamicsWorld;//�������
	ArrayList<TexCube> tca=new ArrayList<TexCube>();
	ArrayList<TexCube> tcaForAdd=new ArrayList<TexCube>();
	CollisionShape boxShape;//���õ�������
	CollisionShape planeShape;//���õ�ƽ����״
	Chapter271Activity activity;
	private int mProgram;

	public GL271SurfaceView(Context context)
	{
        super(context);
        this.activity=(Chapter271Activity) context;
        this.setEGLContextClientVersion(3);
        //��ʼ����������
        initWorld();        
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//��ʼ����������ķ���
	public void initWorld()
	{
		//������ײ���������Ϣ����
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		//������ײ����㷨�����߶�������ɨ�����е���ײ���ԣ�ȷ��ʹ�ú��ּ�����
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);//����������������ı߽���Сֵ
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);//����������������ı߽����ֵ
		int maxProxies = 1024;//����������
		//������ײ���ֲ�׶εļ����㷨����
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//�����ƶ�Լ������߶���
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//���������������
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//�����������ٶ�
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		//�������õ���������ײ��״
		boxShape=new BoxShape(new Vector3f(Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE));
		//�������õ�ƽ����ײ��״
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int[] cubeTextureId=new int[2];//����������
		int floorTextureId;//��������
		TexFloor floor;//�������1		
		
        public void onDrawFrame(GL10 gl) { 
        	//�����ɫ��������Ȼ���
        	GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);            
            //��������
            synchronized(tca)
			{
	            for(TexCube tc:tca)
	            {
	            	MatrixState.pushMatrix();
	                tc.drawSelf(cubeTextureId); 
	                MatrixState.popMatrix();         
	            }            
			}
            
            //���Ƶذ�
            MatrixState.pushMatrix();
            floor.drawSelf( floorTextureId);
            MatrixState.popMatrix();         
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height);
            //����͸��ͶӰ�ı���
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫ��ɫRGBA
            GLES30.glClearColor(0,0,0,0);            
            //������Ȳ���
            GLES30.glEnable(GL10.GL_DEPTH_TEST);  
            //����Ϊ�򿪱������
            GLES30.glEnable(GL10.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            MatrixState.setCamera( 
            		EYE_X,   //����λ�õ�X
            		EYE_Y, 	//����λ�õ�Y
            		EYE_Z,   //����λ�õ�Z
            		TARGET_X, 	//�����򿴵ĵ�X
            		TARGET_Y,   //�����򿴵ĵ�Y
            		TARGET_Z,   //�����򿴵ĵ�Z
            		0, 
            		1, 
            		0);

			//加载顶点着色器的脚本内容
			String mVertexShader = ShaderUtil.loadFromAssetsFile(com.xingfeng.opengles.util.Constant.OBJ_VER_PATH,
					activity.getResources());
			//加载片元着色器的脚本内容
			String mFragmentShader = ShaderUtil.loadFromAssetsFile(com.xingfeng.opengles.util.Constant.OBJ_FRAG_PATH,
					activity.getResources());
			//基于顶点着色器与片元着色器创建程序
			mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
			//获取程序中顶点位置属性引用

//            //��ʼ�����õ���shader����
//            ShaderManager.loadCodeFromFile(activity.getResources());
//            ShaderManager.compileShader();
            //��ʼ������
            cubeTextureId[0]=initTexture(R.drawable.wood_bin2);
            cubeTextureId[1]=initTexture(R.drawable.wood_bin1);
            floorTextureId=initTextureRepeat(R.drawable.f6);            
            
            //�����������
            floor=new TexFloor(mProgram,80*Constant.UNIT_SIZE,-Constant.UNIT_SIZE,planeShape,dynamicsWorld);
           
            //����������       
            int size=2;   //���������ӳߴ�
            float xStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.UNIT_SIZE;//x������ʼֵ
            float yStart=0.02f;//y������ʼֵ
            float zStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.UNIT_SIZE-4f;//z������ʼֵ
            for(int i=0;i<size;i++)//�Բ㡢�С��н���ѭ������������8������������
            {
            	for(int j=0;j<size;j++)
            	{
            		for(int k=0;k<size;k++)
            		{
            			TexCube tcTemp=new TexCube       //��������������
            			(
            					GL271SurfaceView.this,		//GL271SurfaceView������
                				Constant.UNIT_SIZE,		//���ӵĳߴ�
                				boxShape,				//��ײ��״
                				dynamicsWorld,			//��������
                				1,						//��������		
                				xStart+i*(2+0.4f)*Constant.UNIT_SIZE,//���ӳ�ʼx����
                				yStart+j*(2.02f)*Constant.UNIT_SIZE, //���ӳ�ʼy����        
                				zStart+k*(2+0.4f)*Constant.UNIT_SIZE,//���ӳ�ʼz����
								mProgram//��ɫ����������
                		);            			
            			tca.add(tcTemp);//����������ӵ�����������Ӽ�����
            			//����������������Ϊһ��ʼ�ǲ������
            			tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);
            		}
            	}
            }
            
            new Thread()//ͨ�������ڲ��ഴ���̶߳���
            {
            	public void run()//��д�߳���ִ�������run����
            	{
            		while(true)//ģ��ѭ��
            		{            			
            			try 
            			{
            				synchronized(tcaForAdd)//������������Ӽ���
            	            {
            					synchronized(tca)//���������Ӽ���
            					{
            						for(TexCube tc:tcaForAdd)
                	                {
                	            		tca.add(tc);  //�������Ӽ������������
                	                }
            					}            	            	
            	            	tcaForAdd.clear();		//����������Ӽ������
            	            }           
            				//ִ��ģ��
                			dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
							Thread.sleep(20);	//��ǰ�߳�˯��20����
						} catch (Exception e) //�����쳣���ӡ�쳣ջ
						{
							e.printStackTrace();
						}
            		}
            	}
            }.start();					//�����߳�
        }
    }
	
	//�����¼��ص�����
    @Override public boolean onTouchEvent(MotionEvent e) 
    {
        switch (e.getAction()) 
        {
           case MotionEvent.ACTION_DOWN:			//�����ص㰴�µ��¼�
        	TexCube tcTemp=new TexCube				//����һ������
   			(
   					this,							//GL271SurfaceView������
       				Constant.UNIT_SIZE,				//���ӵĳߴ�
       				boxShape,						//��ײ��״
       				dynamicsWorld,					//��������
       				1,								//��������
       				0,								//���ӳ�ʼx����
       				2,         						//���ӳ�ʼy���� 
       				4,								//���ӳ�ʼz����
					mProgram//��ɫ����������
       		);        
        	//�������ӵĳ�ʼ���ٶ�
        	tcTemp.body.setLinearVelocity(new Vector3f(0,2,-12));
        	//�������ӵĳ�ʼ���ٶ�
        	tcTemp.body.setAngularVelocity(new Vector3f(0,0,0));
        	
        	synchronized(tcaForAdd)//������������Ӽ���
            {
        	   tcaForAdd.add(tcTemp);//����������ӵ���������Ӽ�����
            }
           break;
        }
        return true;//����true֪ͨϵͳ�����ѱ�����
    }   
	public int initTexture(int drawableId)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end=====================  
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
	public int initTextureRepeat(int drawableId)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end=====================  
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
}
