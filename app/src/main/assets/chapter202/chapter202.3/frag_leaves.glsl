#version 300 es
precision mediump float;
in vec2 vTextureCoord;

//纹理内容数据
uniform sampler2D sTexture;

//输出的片元颜色
out vec4 fragColor;

void main() {
    fragColor = texture(sTexture, vTextureCoord);
}
