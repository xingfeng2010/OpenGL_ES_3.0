#version 300 es
//总变换矩阵
uniform mat4 uMVPMatrix;
//顶点位置(来自1号关键帧)
in vec3 aPosition;
//顶点位置(来自2号关键帧)
in vec3 bPosition;
//顶点位置(来自3号关键帧)
in vec3 cPosition;

//顶点纹理坐标
in vec2 aTexCoor;
//融合比例
uniform float uBfb;
out vec2 vTextureCoord;

void main() {
    //融合后的结果顶点
    vec3 tv;
    //若融合比例小于等于1，则需要执行的是1、2号关键帧的融合
    if (uBfb <= 1.0)
    {
        tv = mix(aPosition, bPosition, uBfb);
    } else //若融合比例大于1，则需要执行的是2、3号关键帧的融合
    {
        tv = mix(bPosition, cPosition, uBfb-1.0);
    }

    gl_Position = uMVPMatrix * vec4(tv, 1);
    vTextureCoord = aTexCoor;
}
