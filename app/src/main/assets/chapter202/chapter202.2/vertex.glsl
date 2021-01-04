#version 300 es
//总变换矩阵
uniform mat4 uMVPMatrix;
//本帧起始角度(即最左侧顶点的对应角度)
uniform float uStartAngle;
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
    //横向角度总跨度，用于进行X距离与角度的换算
    float angleSpanH = 4.0 * 3.1415926;
    //起始X坐标(即最左侧顶点的X坐标)
    float startY = uHeightSpan / 2.0;
    //根据横向角度总跨度、横向长度总跨度及当前点X坐标折算出当前顶点X坐标对应的角度
    float currAngle = uStartAngle + ((aPosition.y - startY) / uHeightSpan) * angleSpanH;

    float tx = aPosition.x * cos(currAngle) - aPosition.z * sin(currAngle);
    float tz = aPosition.z * cos(currAngle) + aPosition.x * sin(currAngle);

    //根据总变换矩阵计算此次绘制顶点的位置
    gl_Position = uMVPMatrix * vec4(tx, aPosition.y,tz, 1);
    vTextureCoord = aTexCoor;
}
