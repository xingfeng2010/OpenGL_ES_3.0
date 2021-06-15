#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

/**
图像颠倒实现比较简单，在文理采样的时候我们只需要反转UV坐标，
便可以实现图像颠倒的效果。
*/

void main() {
    vec4 textureColor = texture(sTexture, vec2(vTextureCoord.x, 1.0 - vTextureCoord.y));

    fFragColor = textureColor;
}