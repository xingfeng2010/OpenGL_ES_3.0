#version 300 es
precision mediump float;
out vec4 fragColor;
uniform sampler2D sTexture;//纹理内容数据
void main()
{
   //进行纹理采样
   fragColor = texture(sTexture, gl_PointCoord);
}