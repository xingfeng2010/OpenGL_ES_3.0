#version 300 es
//总变换矩阵
uniform mat4 uMVPMatrix;
//本帧起始角度(即最左侧顶点的对应角度)
//横向长度总跨度
uniform float uHeightSpan;

//顶点位置
in vec3 aPosition;
//顶点纹理坐标
in vec2 aTexCoor;
//用于传递给片元着色器的纹理坐标
out vec2 vTextureCoord;

void main() {
    //计算X向波浪
    float angleSpanH = 2.0;
    //起始X坐标(即最左侧顶点的X坐标)
    float startY = -uHeightSpan / 2.0;
    //根据横向角度总跨度、横向长度总跨度及当前点X坐标折算出当前顶点X坐标对应的角度
    float currAngle = ((aPosition.y - startY) / uHeightSpan) * angleSpanH;

    vec3 tPosition = aPosition;
    if (aPosition.y > startY) {
        tPosition.x = aPosition.x * cos(currAngle) - aPosition.z * sin(currAngle);
        tPosition.z = aPosition.z * cos(currAngle) + aPosition.x * sin(currAngle);
    }

    //根据总变换矩阵计算此次绘制顶点的位置
    gl_Position = uMVPMatrix * vec4(tPosition, 1);
    vTextureCoord = aTexCoor;
}
