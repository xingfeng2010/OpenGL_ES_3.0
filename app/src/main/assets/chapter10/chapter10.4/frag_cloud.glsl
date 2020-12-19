#version 300 es
precision mediump float;
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{  
  //����ƬԪ�������в�������ɫֵ            
  vec4 finalColor = texture(sTexture, vTextureCoord); 
  //������ɫֵ����͸����
  finalColor.a=(finalColor.r+finalColor.g+finalColor.b)/3.0;
  //�����������
  finalColor=finalColor*ambient+finalColor*specular+finalColor*diffuse;
  //����ƬԪ��ɫֵ 
  fragColor = finalColor;
}              