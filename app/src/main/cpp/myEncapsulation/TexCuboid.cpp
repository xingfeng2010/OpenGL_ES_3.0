#include "TexCuboid.h"

#include "../src/LinearMath/btVector3.h"
#include "../src/BulletCollision/CollisionShapes/btCollisionShape.h"
#include "../src/BulletCollision/CollisionShapes/btBoxShape.h"
#include "../src/LinearMath/btDefaultMotionState.h"
#include "../src/BulletDynamics/Dynamics/btRigidBody.h"
#include "MatrixState.h"

TexCuboid::TexCuboid(
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
)
{
	bool isDynamic = (mass != 0);//�ж������Ƿ�����˶�
	btVector3 localInertia = btVector3(0, 0, 0);//��������
	btCollisionShape* colShape = new btBoxShape(boxHalfExtentsIn);
	if(isDynamic)
	{
		colShape->calculateLocalInertia(mass, localInertia);//�������
	}
	btTransform startTansform = btTransform();//���������ʼ�任����
	startTansform.setIdentity();//��ʼ������任����
	startTansform.getOrigin().setValue(pos.x(), pos.y(), pos.z());//���ñ任����ĳ�ʼλ��

	//�����˶�״̬����
	btDefaultMotionState* myMotionState  = new btDefaultMotionState(startTansform,btTransform::getIdentity());

	//������Ϣ����
	btRigidBody::btRigidBodyConstructionInfo rbInfo(mass, myMotionState, colShape, localInertia);
	rbInfo.m_restitution = restitutionIn;//���÷���ϵ��
	rbInfo.m_friction = frictionIn;//����Ħ��ϵ��
	
	body = new btRigidBody(rbInfo);//��������

	m_dynamicsWorldIn->addRigidBody(body);//��������ӽ���������

	this->offset_x = boxHalfExtentsIn.x();
	this->offset_y = boxHalfExtentsIn.y();
	this->offset_z = boxHalfExtentsIn.z();

	//ǰ��ĳ�����    ǰλ���� offset_z  ��λ���� -offset_z
	frontBackRect = new TextureRect(
    		offset_x, //���
    		offset_y, //���
    		0, //λ����
    		UNIT_SIZE//��λ����
			);
	//���µĳ�����   ǰλ���� offset_y  ��λ���� -offset_y
	topBottomRect = new TextureRect(
    		offset_x, //���
    		offset_z, //���
    		0, //λ����
    		UNIT_SIZE//��λ����
			);
	//���ҵĳ�����    ǰλ���� offset_x  ��λ���� -offset_x
	leftRightRect = new TextureRect(
    		offset_z, //���
    		offset_y, //���
    		0, //λ����
    		UNIT_SIZE//��λ����
			);

	this->frontTexId =  frontTexIdIn;//ǰ������ͼid
	this->backTexId =  backTexIdIn;//�������ͼid
	this->topTexId = topTexIdIn;//�ϲ�����ͼid
	this->bottomTexId = bottomTexIdIn;//�²�����ͼid
	this->leftTexId = leftTexIdIn;//�������ͼid
	this->rightTexId = rightTexIdIn;//�Ҳ�����ͼid

}

void TexCuboid::drawSelf()
{
	MatrixState::pushMatrix();//�����ֳ�

	btTransform trans;
	trans = body->getWorldTransform();
	trans.getOpenGLMatrix(MatrixState::currMatrix);

	if(body->isActive())
	{
		topBottomRect->mFlagK = 0;
		frontBackRect->mFlagK = 0;
		leftRightRect->mFlagK = 0;
	}
	else
	{
		topBottomRect->mFlagK = 1;
		frontBackRect->mFlagK = 1;
		leftRightRect->mFlagK = 1;
	}

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(0, offset_y, 0);//ִ��ƽ��
	MatrixState::rotate(90, 1, 0, 0);//ִ����ת
	topBottomRect->drawSelf(topTexId);//��������
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(0, -offset_y, 0);//ִ��ƽ��
	MatrixState::rotate(-90, 1, 0, 0);//ִ����ת
	topBottomRect->drawSelf(bottomTexId);//��������
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(-offset_x, 0, 0);//ִ��ƽ��
	MatrixState::rotate(90, 0, 1, 0);//ִ����ת
	leftRightRect->drawSelf(leftTexId);//��������
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(offset_x, 0, 0);//ִ��ƽ��
	MatrixState::rotate(-90, 0, 1, 0);//ִ����ת
	leftRightRect->drawSelf(rightTexId);//��������
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(0, 0, offset_z);//ִ��ƽ��
	frontBackRect->drawSelf(frontTexId);//����ǰ��
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::pushMatrix();//�����ֳ�
	MatrixState::translate(0, 0, -offset_z);//ִ��ƽ��
	MatrixState::rotate(180, 0, 1, 0);//ִ����ת
	frontBackRect->drawSelf(backTexId);//���ƺ���
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::popMatrix();//�ָ��ֳ�
}
btRigidBody* TexCuboid::getBody()
{
	return body;
}
