#version 300 es
precision mediump float;
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   fragColor= texture(sTexture, vTextureCoord); 
}              