#ifndef _TexCuboid_H_
#define _TexCuboid_H_

#include "TexBody.h"
#include "../src/BulletDynamics/Dynamics/btDynamicsWorld.h"
#include "geometry/TextureRect.h"

//������������
class TexCuboid : public TexBody
{
	btRigidBody* body;//�������ָ��

	float offset_x;//x�����ϵ�λ��
	float offset_y;//y�����ϵ�λ��
	float offset_z;//z�����ϵ�λ��

	TextureRect* frontBackRect;//ǰ��ĳ�����
	TextureRect* topBottomRect;//���µĳ�����
	TextureRect* leftRightRect;//���ҵĳ�����

	int frontTexId;//ǰ������ͼid
	int backTexId;//�������ͼid
	int topTexId;//�ϲ�����ͼid
	int bottomTexId;//�²�����ͼid
	int leftTexId;//�������ͼid
	int rightTexId;//�Ҳ�����ͼid

public:
	TexCuboid(
			btDynamicsWorld* m_dynamicsWorldIn,//�����������
			btVector3 boxHalfExtentsIn,//������İ�����
			btScalar mass,//�����������
			btVector3 pos,//�����λ��
			btScalar restitutionIn,//�ָ�ϵ��
			btScalar frictionIn,//Ħ��ϵ��
			float UNIT_SIZE,//��λ����
			int frontTexIdIn,//ǰ������ͼid
			int backTexIdIn,//�������ͼid
			int topTexIdIn,//�ϲ�����ͼid
			int bottomTexIdIn,//�²�����ͼid
			int leftTexIdIn,//�������ͼid
			int rightTexIdIn//�Ҳ�����ͼid
	);
//	~TexCuboid();
	void drawSelf();
	btRigidBody* getBody();
};

#endif
