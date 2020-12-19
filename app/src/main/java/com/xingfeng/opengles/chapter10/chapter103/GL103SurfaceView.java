package com.xingfeng.opengles.chapter10.chapter103;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xingfeng.opengles.R;
import com.xingfeng.opengles.chapter10.chapter101.KeyThread;
import com.xingfeng.opengles.util.BnETC2Util;
import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.LoadUtil;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalAverage;
import com.xingfeng.opengles.util.LoadedObjectVertexNormalFace;
import com.xingfeng.opengles.util.MatrixState;

class GL103SurfaceView extends GLSurfaceView
{
    private SceneRenderer mRenderer;//������Ⱦ��  
	
	//���ε�λ��
	static float rectX;
	static float rectY;
	static int rectState = KeyThread.Stop;
	static final float moveSpan = 0.1f;
	private KeyThread3 keyThread;
	public GL103SurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	if(x< Constant.SCREEN_WIDTH/3.0f) {//������Ļ����1/3������
        		rectState = KeyThread.left;
        	}
        	else if(x>Constant.SCREEN_WIDTH*2/3.0f){//������Ļ����2/3������
        		rectState = KeyThread.right;
        	}
        	else {
            	if(y<Constant.SCREEN_HEIGHT/2.0f) {   //������Ļ�Ϸ�������     		
            		rectState = KeyThread.up;
            	}
            	else {//������Ļ�·������� 
            		rectState = KeyThread.down;
            	}
        	}
        	break;
        case MotionEvent.ACTION_UP://̧��ʱֹͣ�ƶ�
        	rectState = KeyThread.Stop;
        	break;
        }
        return true;
    }
	private class SceneRenderer implements Renderer
    {
		int rectTexId;//����id
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalFace pm;
		LoadedObjectVertexNormalFace cft;
		LoadedObjectVertexNormalAverage qt;
		LoadedObjectVertexNormalAverage yh;
		LoadedObjectVertexNormalAverage ch;
		MixTextureRect3 rect;
        @SuppressLint("InlinedApi")
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
           
            MatrixState.pushMatrix();
            MatrixState.pushMatrix();
            MatrixState.rotate(25, 1, 0, 0);       
            //�����ص����岿λ�����������            
            pm.drawSelf();//ƽ��
            
            //��������
            MatrixState.pushMatrix();
            MatrixState.scale(1.5f, 1.5f, 1.5f);          
            //�������� 
            //���Ƴ�����
            MatrixState.pushMatrix();
            MatrixState.translate(-10f, 0f, 0);
            cft.drawSelf();
            MatrixState.popMatrix();   
            //��������
            MatrixState.pushMatrix();
            MatrixState.translate(10f, 0f, 0);
            qt.drawSelf();
            MatrixState.popMatrix();  
            //����Բ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -10f);
            yh.drawSelf();
            MatrixState.popMatrix();  
            //���Ʋ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, 10f);
            ch.drawSelf();
            MatrixState.popMatrix();
            MatrixState.popMatrix(); 
            MatrixState.popMatrix(); 
              
            //�������
            GLES30.glEnable(GLES30.GL_BLEND);  
            //���û������,���е�һ��ΪԴ���ӣ��ڶ���ΪĿ������
            GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
            //�����������
            MatrixState.pushMatrix();
            MatrixState.translate(rectX, rectY, 25f);
            rect.drawSelf(rectTexId);//�����˹⾵�������
            MatrixState.popMatrix();
            //�رջ��
            GLES30.glDisable(GLES30.GL_BLEND);
            
            MatrixState.popMatrix();                  
        }  

        @SuppressLint("NewApi")
		public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //����cameraλ��
            MatrixState.setCamera
            (
            		0,   //����λ�õ�X
            		0, 	//����λ�õ�Y
            		50,   //����λ�õ�Z
            		0, 	//�����򿴵ĵ�X
            		0,   //�����򿴵ĵ�Y
            		0,   //�����򿴵ĵ�Z
            		0, 	//upλ��
            		1, 
            		0
            );
            //��ʼ����Դλ��
            MatrixState.setLightLocation(100, 100, 100);
            keyThread = new KeyThread3(GL103SurfaceView.this);
            keyThread.start();
        }
        @SuppressLint("NewApi")
		@Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.3f,0.3f,0.3f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();         
            //����id
            rectTexId= BnETC2Util.initTextureETC2("chapter10/chapter10.3/pkm/lgq.pkm",GL103SurfaceView.this.getResources());
            //����Ҫ���Ƶ�����
            ch= LoadUtil.loadFromFileVertexOnlyAverage("chapter10/chapter10.3/obj/ch.obj", GL103SurfaceView.this.getResources(),GL103SurfaceView.this);
            pm=LoadUtil.loadFromFileVertexOnlyFace("chapter10/chapter10.3/obj/pm.obj", GL103SurfaceView.this.getResources(),GL103SurfaceView.this);;
    		cft=LoadUtil.loadFromFileVertexOnlyFace("chapter10/chapter10.3/obj/cft.obj", GL103SurfaceView.this.getResources(),GL103SurfaceView.this);;
    		qt=LoadUtil.loadFromFileVertexOnlyAverage("chapter10/chapter10.3/obj/qt.obj", GL103SurfaceView.this.getResources(),GL103SurfaceView.this);;
    		yh=LoadUtil.loadFromFileVertexOnlyAverage("chapter10/chapter10.3/obj/yh.obj", GL103SurfaceView.this.getResources(),GL103SurfaceView.this);;
    		rect = new MixTextureRect3(GL103SurfaceView.this, 10, 10);
        }  
    }

	
	@SuppressLint("NewApi")
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
        		GLES30.GL_TEXTURE_2D,   //��������
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
	@Override
    public void onResume() {
    	super.onResume();
    	KeyThread.flag = true;
    	keyThread = new KeyThread3(GL103SurfaceView.this);
        keyThread.start();
    }
	@Override
	public void onPause() {
		super.onPause();
		KeyThread.flag = false;
	}
}
