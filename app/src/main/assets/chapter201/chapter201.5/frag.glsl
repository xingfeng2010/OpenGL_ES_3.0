#version 300 es
precision mediump float;
//纹理内容数据
uniform sampler2D sTexture;

in vec4 ambient;
in vec4 diffuse;
in vec4 specular;

//接收从顶点着色器过来的参数
in vec2 vTextureCoord;

out vec4 fragColor;

void main() {
    //将计算出的颜色给此片元
    vec4 finalColor = texture(sTexture, vTextureCoord);
    //给此片元颜色值
    fragColor = finalColor * ambient + finalColor * diffuse + finalColor * specular;
}
