#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

const float PI = 3.14159265;
const float uD = 80.0; //旋转角度
const float uR = 0.5; //旋转半径

/**
图像漩涡主要是在某个半径范围里，把当前采样点旋转一定角度，
旋转以后当前点的颜色就被旋转后的点的颜色代替，因此整个半径范围里会有旋转的效果。
如果旋转的时候旋转角度随着当前点离半径的距离递减，整个图像就会出现漩涡效果。
这里使用的了抛物线递减因子：(1.0-(r/Radius)*(r/Radius) )。
*/
void main() {
    ivec2 ires = ivec2(512, 512);
    float Res = float(ires.s);

    vec2 st = vTextureCoord;
    float Radius = Res * uR;

    vec2 xy = Res * st;

    vec2 dxy = xy - vec2(Res/2., Res/2.);
    float r = length(dxy);

    float beta = atan(dxy.y, dxy.x) + radians(uD) * 2.0 * (-(r/Radius)*(r/Radius) + 1.0);//(1.0 - r/Radius);

    vec2 xy1 = xy;
    if(r<=Radius)
    {
        xy1 = Res/2. + r*vec2(cos(beta), sin(beta));
    }

    st = xy1/Res;

    vec3 irgb = texture(sTexture, st).rgb;

    fFragColor = vec4(irgb, 1.0);
}