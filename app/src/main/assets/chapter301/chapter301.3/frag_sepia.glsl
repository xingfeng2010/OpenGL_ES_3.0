#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

void main() {
    float grey = dot(texture(sTexture, vTextureCoord).rgb, vec3(0.299, 0.587, 0.114));

    fFragColor = vec4(grey * vec3(1.2, 1.0, 0.8), 1.0);
}