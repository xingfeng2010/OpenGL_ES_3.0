#version 300 es
precision mediump float;

//接收从顶点着色器过来的参数
in vec2 vTextureCoord;
//接收从顶点着色器过来环境光最终强度
in vec4 vAmbient;
//接收从顶点着色器过来散射光最终强度
in vec4 vDiffuse;
//接收从顶点着色器过来镜面反射光最终强度
in vec4 vSpecular;

//白天纹理采样中采样出颜色值
uniform sampler2d sTextureDay;
//黑夜纹理采样中采样出颜色值
uniform sampler2d sTextureNight;

out vec4 fragColor;

void main()
{
   vec4 finalColorDay;
   vec4 finalColorNight;

   finalColorDay = texture(sTextureDay, vTextureCoord);
   finalColorDay = finalColorDay * vAmbient + finalColorDay * vSpecular + finalColorDay * vDiffuse;
   finalColorNight = texture(sTextureNight, vTextureCoord);
   finalColorNight = finalColorNight * vec4(0.5, 0.5, 0.5, 1.0);

   if (vDiffuse.x > 0.21) {
      fragColor = finalColorDay;
   } else if (vDiffuse.x < 0.5) {
      fragColor = finalColorNight;
   } else {
      float t = (vDiffuse.x - 0.05) / 0.16;
      fragColor = t * finalColorDay + (1.0-t) * finalColorNight;
   }
}