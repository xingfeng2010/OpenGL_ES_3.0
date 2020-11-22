#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
layout (location = 0) in vec3 aPosition;  //����λ��
layout (location = 1) in vec4 aColor;    //������ɫ
out vec4 vColor;  //���ڴ��ݸ�ƬԪ��ɫ���ı���

void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vColor = aColor;//�����յ���ɫ���ݸ�ƬԪ��ɫ�� 
}