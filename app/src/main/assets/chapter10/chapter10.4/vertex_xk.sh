#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float uPointSize;//��ߴ�
in vec3 aPosition;  //����λ��

void main()     
{     
   //�����ܱ任�������˴λ��ƴ˶���λ��                         		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   //
   gl_PointSize=uPointSize;
}                 