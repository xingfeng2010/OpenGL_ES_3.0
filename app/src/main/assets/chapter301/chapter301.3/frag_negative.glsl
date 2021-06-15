#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

void main() {
    vec4 texMapColour = texture(sTexture, vTextureCoord);

    fFragColor = vec4(1.0 - texMapColour.rgb, 1.0);
}