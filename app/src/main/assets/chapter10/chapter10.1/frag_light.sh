#version 300 es
precision mediump float;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{//����������������������
	vec4 finalColor=vec4(1.0,1.0,1.0,0.0);//������ɫ
	fragColor= finalColor*ambient+finalColor*specular+finalColor*diffuse;//����ƬԪ��ɫֵ   
}              