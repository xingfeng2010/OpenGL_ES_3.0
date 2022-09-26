#include "TextureRect.h"
#include "../ShaderUtil.h"
#include "../MatrixState.h"

#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native-activity", __VA_ARGS__))

#include <stdio.h>
#include <stdlib.h>
#include "../FileUtil.h"

#include <string>
using namespace std;

TextureRect::TextureRect(
		float halfWidth, //���
		float halfHeight, //���
		float offset, //λ����
		float UNIT_SIZE//��λ����
		)
{
    initVertexDataRectangle(halfWidth, halfHeight, offset, UNIT_SIZE);
    initShader();
}

void TextureRect::initVertexDataRectangle(float halfWidth, float halfHeight, float offset, float UNIT_SIZE)
{
	//  3----2
	//  |    |
	//	|	 |
	//  0----1    ˳��Ϊ  0,1,2  0,2,3
    vCount = 6;
    float *vertices=new float[vCount*3];
	int count=0;//���������

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	mVertexBuffer = &vertices[0];

    float *normal=new float[vCount*3];
	count=0;//���������
	for(int i = 0; i<vCount; i++)
	{
		normal[count++] = 0;
		normal[count++] = 0;
		normal[count++] = 1;
	}
	mNormalBuffer = &normal[0];

    float *textures=new float[vCount*2];
	count=0;//���������

	textures[count++]=0;
	textures[count++]=0;

	textures[count++]=1;
	textures[count++]=0;

	textures[count++]=1;
	textures[count++]=1;

	textures[count++]=0;
	textures[count++]=0;

	textures[count++]=1;
	textures[count++]=1;

	textures[count++]=0;
	textures[count++]=1;

	mTextureBuffer = &textures[0];
}


void TextureRect::initShader()
{
	string strVertex=FileUtil::loadShaderStr("chapter801/chapter801.1/CommonTex.vert");
	string strFrag=FileUtil::loadShaderStr("chapter801/chapter801.1/CommonTex.frag");

	const char* SimpleVertexShader = strVertex.c_str();
	const char* SimpleFragmentShader = strFrag.c_str();

	//���ڶ�����ɫ����ƬԪ��ɫ����������
    mProgram = ShaderUtil::createProgram(SimpleVertexShader, SimpleFragmentShader);
    //��ȡ�����ж���λ����������
    maPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    //��ȡ�����ж���������������
    maTexCoorHandle=glGetAttribLocation(mProgram, "aTexCoor");
    //��ȡ�������ܱ任��������
    muMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    //��ȡλ�á���ת�任��������
    muMMatrixHandle = glGetUniformLocation(mProgram, "uMMatrix");
    //��ȡ�����������λ������
    maCameraHandle=glGetUniformLocation(mProgram, "uCamera");
    //��ȡ���������
    mTexHandle=glGetUniformLocation(mProgram, "sTexture");

    //��ȡ���㷨�������Ե�����
    maNormalHandle = glGetAttribLocation(mProgram, "aNormal");
    //��ȡ�����й�Դ��λ�õ�����
    maLightLocationHandle = glGetUniformLocation(mProgram, "uLightLocation");

    mFragK = glGetUniformLocation(mProgram, "k");
}

void TextureRect::drawSelf(const GLint texId)
{
	//ָ��ʹ��ĳ����ɫ������
	glUseProgram(mProgram);
	//�����ձ任��������Ⱦ����
	glUniformMatrix4fv(muMVPMatrixHandle, 1, 0, MatrixState::getFinalMatrix());
	//��λ�á���ת�任��������Ⱦ����
	glUniformMatrix4fv(muMMatrixHandle, 1, 0, MatrixState::getMMatrix());
	//�������λ�ô�����Ⱦ����
	glUniform3fv(maCameraHandle, 1, MatrixState::cameraFB);
	//����Դλ�ô�����Ⱦ����
	glUniform3fv(maLightLocationHandle, 1, MatrixState::lightPositionFBSun);

	//������λ�����ݴ�����Ⱦ����
	glVertexAttribPointer
	(
		maPositionHandle,
		3,
		GL_FLOAT,
		GL_FALSE,
		3*4,
		mVertexBuffer
	);
	//�����������������ݴ�����Ⱦ����
	glVertexAttribPointer
	(
		maTexCoorHandle,
		2,
		GL_FLOAT,
		GL_FALSE,
		2*4,
		mTextureBuffer
	);
	//Ϊ�����㷨�������ݴ�����Ⱦ����
	glVertexAttribPointer
	(
		maNormalHandle,
		3,
		GL_FLOAT,
		GL_FALSE,
		3*4,
		mNormalBuffer
	);

	glEnableVertexAttribArray(maPositionHandle);//���ö���������������
	glEnableVertexAttribArray(maTexCoorHandle);//���ö�������������������
	glEnableVertexAttribArray(maNormalHandle);//���ö��㷨������������


	glActiveTexture(GL_TEXTURE0);//ѡ������Ԫ
	glBindTexture(GL_TEXTURE_2D, texId);	//������
	glUniform1i(mTexHandle, 0);
	glUniform1f(mFragK, mFlagK);//mFlagK Ϊ0ʱ��ʾ��������   Ϊ1ʱ��ʾ�к�ɫֵӰ��

	//����������
	glDrawArrays(GL_TRIANGLES, 0, vCount);

    glDisableVertexAttribArray(maPositionHandle);
    glDisableVertexAttribArray(maTexCoorHandle);
    glDisableVertexAttribArray(maNormalHandle);
}
