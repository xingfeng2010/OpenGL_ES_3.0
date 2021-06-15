#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

void main() {
    vec2 pos = vTextureCoord.st;
    vec3 irgb = texture(sTexture, pos).rgb;

    vec3 target = vec3(0.0, 0.0, 0.0);

    fFragColor = vec4(mix(target, irgb, 0.5), 1.0);
}