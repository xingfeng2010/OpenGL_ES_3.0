#version 300 es
precision mediump float;
//纹理内容数据
uniform sampler2D sTexture;

//接收从顶点着色器过来的参数
in vec2 vTextureCoord;

out vec4 fragColor;

void main() {
    //将计算出的颜色给此片元
    vec4 finalColor = texture(sTexture, vTextureCoord);
    //给此片元颜色值
    fragColor = finalColor;
}
