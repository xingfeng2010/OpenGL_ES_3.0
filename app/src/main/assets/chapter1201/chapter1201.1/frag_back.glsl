#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;
in vec2 vTextureCoord; //接收从顶点着色器过来的参数
uniform samplerExternalOES sTexture;//纹理内容数据
out vec4 fragColor;
void main()
{
   //给此片元从纹理中采样出颜色值
   fragColor = texture(sTexture, vTextureCoord);
}