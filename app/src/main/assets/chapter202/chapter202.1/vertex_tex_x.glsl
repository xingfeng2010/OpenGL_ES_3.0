#version 300 es
//总变换矩阵
uniform mat4 uMVPMatrix;
//本帧起始角度(即最左侧顶点的对应角度)
uniform float uStartAngle;
//横向长度总跨度
uniform float uWidthSpan;

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
    float startX = uWidthSpan / 2.0;
    //根据横向角度总跨度、横向长度总跨度及当前点X坐标折算出当前顶点X坐标对应的角度
    float currAngle = uStartAngle + ((aPosition.x - startX) / uWidthSpan) * angleSpanH;
    //通过正弦函数求出当前点的Z坐标
    float tz = sin(currAngle) * 0.1;

    //根据总变换矩阵计算此次绘制顶点的位置
    gl_Position = uMVPMatrix * vec4(aPosition.x, aPosition.y,tz, 1);
    vTextureCoord = aTexCoor;
}
