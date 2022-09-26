#include <stdio.h>
#include <math.h>

#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <vector>
#include "LinearMath/btAlignedObjectArray.h"
#include "btBulletDynamicsCommon.h"
#include "BulletSoftBody/btSoftBody.h"
#include "BulletSoftBody/btSoftBodyHelpers.h"
#include "BulletSoftBody/btSoftRigidDynamicsWorld.h"
#include "BulletSoftBody/btSoftBodySolvers.h"
#include "BulletSoftBody/btDefaultSoftBodySolver.h"
#include "BulletSoftBody/btSoftBodyRigidBodyCollisionConfiguration.h"
#include "../myEncapsulation/MatrixState.h"
#include "../myEncapsulation/TexCuboid.h"
#include "../myEncapsulation/TexBody.h"
#include "../myEncapsulation/TexPlane.h"
#include "../myEncapsulation/FileUtil.h"


#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native-activity", __VA_ARGS__))

using namespace std;

//����  ����Ͷ��
extern "C" {

	int boxTexId;	//����������id
	int planeTexId;	//��������id
	float UNIT_SIZE = 1.0;	//��λ����

	vector<TexBody*> tca;	//�洢�����װ������vector������ �����壬���壬���棩
	TexBody* tbTemp;		//��ʱ�����װ�����

	int addBodyId = 0;		//��Ӹ����id

	//�������λ������
	float cx = 10;
	float cy = 30;
	float cz = 30;

	//up����
	float upx = 0;
	float upy = 1;
	float upz = 0;

	btDefaultCollisionConfiguration* m_collisionConfiguration;//ϵͳĬ����ײ���������Ϣ
	btCollisionDispatcher*	m_dispatcher;	//��ײ����㷨������
	btBroadphaseInterface*	m_broadphase;	//��Ƚ׶���ײ���
	btConstraintSolver*		m_solver;		//��ײԼ�������
	btDynamicsWorld*		m_dynamicsWorld;//��������

	//���������λ��
	void setCamera(float x,float y,float z,float upxt,float upyt,float upzt)
	{
		cx = x;
		cy = y;
		cz = z;
		upx = upxt;
		upy = upyt;
		upz = upzt;
	}

	//��ʼ����������ķ���
	void initWorld()
	{
		m_collisionConfiguration = new btDefaultCollisionConfiguration();//����Ĭ�ϵ���ײ���������Ϣ����
		//������ײ����㷨�����������书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
		m_dispatcher = new	btCollisionDispatcher(m_collisionConfiguration);
		m_broadphase = new btSimpleBroadphase();
		btSequentialImpulseConstraintSolver* sol = new btSequentialImpulseConstraintSolver();//�����ƶ�Լ�����������
		m_solver = sol;

		//������ɢ����������
		m_dynamicsWorld = new btDiscreteDynamicsWorld(m_dispatcher,m_broadphase,m_solver,m_collisionConfiguration);
		m_dynamicsWorld->setGravity(btVector3(0,-10,0));//��������

	}


	void initCreateBodys()
	{
		{
			btCollisionShape* planeShape=new btStaticPlaneShape(btVector3(0, 1, 0), 0);//����������״
			//��������
			tbTemp = new TexPlane(UNIT_SIZE*100,planeTexId,planeShape,m_dynamicsWorld,
					0,-5,0,
					0.8,0.8);
			//������������뵽�б���
			tca.push_back(tbTemp);
		}
		{
			#define ARRAY_SIZE_X 3
			#define ARRAY_SIZE_Y 3
			#define ARRAY_SIZE_Z 3

			#define START_POS_X 0
			#define START_POS_Y 10
			#define START_POS_Z 0
			float start_x = START_POS_X - ARRAY_SIZE_X/2;
			float start_y = START_POS_Y;
			float start_z = START_POS_Z - ARRAY_SIZE_Z/2;

			for (int k=0;k<ARRAY_SIZE_Y;k++)
			{
				for (int i=0;i<ARRAY_SIZE_X;i++)
				{
					for(int j = 0;j<ARRAY_SIZE_Z;j++)
					{
						btVector3 pos(btScalar((UNIT_SIZE*4+2)*i + start_x),
								btScalar(0+(UNIT_SIZE*4+2)*k + start_y),
								btScalar((UNIT_SIZE*4+2)*j + start_z));

						tbTemp = new TexCuboid(
								m_dynamicsWorld,//�����������
								btVector3(UNIT_SIZE*2, UNIT_SIZE*2, UNIT_SIZE*2),//������İ�����
								10.0f,//�����������
								pos,//�����λ��
								0.7f,//�ָ�ϵ��
								0.8f,//Ħ��ϵ��
								UNIT_SIZE,//��λ����
								boxTexId,boxTexId,boxTexId,
								boxTexId,boxTexId,boxTexId
						);
						//������������뵽�б���
						tca.push_back(tbTemp);
					}
				}
			}
		}

	}

	void setAddBodyId(int id)
	{
		addBodyId = id;
	}
	void addBody(int id)
	{
		tbTemp = new TexCuboid(
				m_dynamicsWorld,//�����������
				btVector3(UNIT_SIZE*2, UNIT_SIZE*2, UNIT_SIZE*2),//������İ�����
				10.0f,//�����������
				btVector3(cx,cy-10,cz),//�����λ��
				0.2f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				UNIT_SIZE,//��λ����
				boxTexId,boxTexId,boxTexId,
				boxTexId,boxTexId,boxTexId
		);
    	//�������ӵĳ�ʼ�ٶ�
		btVector3 vvec = btVector3(-cx,-cy+10,-cz);
		btVector3 avec = btVector3(0,0,0);
		tbTemp->getBody()->setLinearVelocity(vvec);//����ֱ���˶����ٶ�--Vx,Vy,Vz��������
		tbTemp->getBody()->setAngularVelocity(avec); //����������ת���ٶ�--�����������x,y,x������ת���ٶ�
    	//������������뵽�б���
    	tca.push_back(tbTemp);
	}
	void cleanVector()
	{
		tca.clear();
	}

	bool onSurfaceChanged(int w, int h) {
	    glViewport(0, 0, w, h);
	    float ratio = (float) w/h;
	    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
		return true;
	}


	bool onSurfaceCreated(JNIEnv * env,jobject obj) {

	    MatrixState::setCamera(0, 20, 20, 0, 0, -5, 0, 1, 0);
	    MatrixState::setLightLocation(0, 20, 20);//���ù�Դλ��
	    MatrixState::setInitStack();
	    glClearColor(0, 0, 0, 0);
	    glEnable(GL_DEPTH_TEST);

		jclass cl = env->FindClass("com/xingfeng/opengles/chapter28/chapter281/GL2JNIView");
		jmethodID id = env->GetStaticMethodID(cl,"initTextureRepeat","(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
		jstring name = env->NewStringUTF("chapter801/chapter801.1/box.jpg");
		boxTexId = env->CallStaticIntMethod(cl,id,obj,name);
		name = env->NewStringUTF("chapter801/chapter801.1/grass.png");
		planeTexId = env->CallStaticIntMethod(cl,id,obj,name);

		initWorld();
		cleanVector();
 		initCreateBodys();

		return true;
	}

	void renderFrame() {
	    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        //����cameraλ��
        MatrixState::setCamera
        (
        		cx,	//����λ�õ�X
        		cy, //����λ�õ�Y
        		cz, //����λ�õ�Z
        		0, 	//�����򿴵ĵ�X
        		0,  //�����򿴵ĵ�Y
        		0,  //�����򿴵ĵ�Z
        		upx, 	//up����
        		upy,
        		upz
        );
        MatrixState::setLightLocation(cx, cy, cz);//���ù�Դλ��

	    for ( int i = 0; i < tca.size(); ++i )
	    {
	    	 tca[i]->drawSelf();//�ص�����Ļ��Ʒ���
	    }

	    if(addBodyId!=0)
	    {
	    	addBody(addBodyId);
	    	addBodyId = 0;
	    }

	    m_dynamicsWorld->stepSimulation(1.0/60,5);
	}

	void loadObjData(int objId, float* vertices, int numsVer, float* normals, int numsNor){}

	JNIEXPORT void JNICALL Java_com_xingfeng_opengles_chapter28_chapter281_JNIPort_setCamera
	  (JNIEnv *env, jclass jc,jfloat cx,jfloat cy,jfloat cz,jfloat upx,jfloat upy,jfloat upz)
	{
		setCamera(cx,cy,cz,upx,upy,upz);
	}

	float* copyFloats(float* src,int count)
	{
		float* dst=new float[count];
		for(int i=0;i<count;i++)
		{
			dst[i]=src[i];
		}
		return dst;
	}

	JNIEXPORT void JNICALL Java_com_xingfeng_opengles_chapter28_chapter281_JNIPort_loadObjData
	  (JNIEnv *env, jclass jc, jint objId, jfloatArray vertices, jint numsVer, jfloatArray normals, jint numsNor)
	{
		jfloat*  jfVertexData= (jfloat*)(env->GetFloatArrayElements(vertices,0));
		jfloat*  jfNormalData= (jfloat*)(env->GetFloatArrayElements(normals,0));

		jsize vlen = env->GetArrayLength(vertices);
		jsize nlen = env->GetArrayLength(normals);


		loadObjData(
				(int)objId,
				copyFloats((float*)jfVertexData,(int)vlen),
				(int)numsVer,
				copyFloats((float*)jfNormalData,(int)nlen),
				(int)numsNor
				);


		env->ReleaseFloatArrayElements(vertices,jfVertexData,0);
		env->ReleaseFloatArrayElements(normals,jfNormalData,0);
	}

	JNIEXPORT void JNICALL Java_com_xingfeng_opengles_chapter28_chapter281_JNIPort_nativeSetAssetManager
	  (JNIEnv *env, jclass cls, jobject assetManager)
	{
		AAssetManager* aamIn = AAssetManager_fromJava( env, assetManager );
	    FileUtil::setAAssetManager(aamIn);
	}
}

