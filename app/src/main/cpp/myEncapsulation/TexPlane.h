#ifndef TexPlane__h
#define TexPlane__h

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include "TexBody.h"
#include "../src/btBulletDynamicsCommon.h"
#include "../src/LinearMath/btVector3.h"
#include "geometry/TexturePlane.h"

class TexPlane:public TexBody{
    int vCount;
    float UNIT_SIZE;
	btRigidBody *body;	//��Ӧ�ĸ������
	int texId;
	TexturePlane* tp;
public:
	TexPlane(float aSize,//�����������εģ����������ʾ�߳���һ��
			int texId,
			btCollisionShape *planeShape,
			btDynamicsWorld *dynamicsWorld,
			btScalar cx,btScalar cy,btScalar cz,
			btScalar restitutionIn,
			btScalar frictionIn);
    void drawSelf();
	btRigidBody* getBody();
};


#endif
