#version 300 es
precision mediump float;
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
in vec4 vAmbient;
in vec4 vDiffuse;
in vec4 vSpecular;
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{  
  //����ƬԪ�������в�������ɫֵ            
  vec4 finalColor = texture(sTexture, vTextureCoord); 
  //����ƬԪ��ɫֵ 
  fragColor = finalColor*vAmbient+finalColor*vSpecular+finalColor*vDiffuse;
}              