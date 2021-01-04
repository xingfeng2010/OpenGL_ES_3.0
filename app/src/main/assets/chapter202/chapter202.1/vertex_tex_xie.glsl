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
    float currAngleH = uStartAngle + ((aPosition.x - startX) / uWidthSpan) * angleSpanH;

    //计算出随Y向发展起始角度的扰动值
    //纵向角度总跨度，用于进行Y距离与角度的换算
    float angleSpanZ = 4.0 * 3.1415926;
    //纵向长度总跨度
    float uHeightSpan = 0.75 * uWidthSpan;
    //起始Y坐标(即最左侧顶点的Y坐标)
    float startY = uHeightSpan / 2.0;
    //根据纵向角度总跨度、纵向长度总跨度及当前点Y坐标折算出当前顶点Y坐标对应的角度
    float currAnglez = ((aPosition.y - startY) / uHeightSpan) * angleSpanZ;

    //通过正弦函数求出斜向波浪
    float tzH = sin(currAngleH - currAnglez) * 0.1;

    //根据总变换矩阵计算此次绘制顶点的位置
    gl_Position = uMVPMatrix * vec4(aPosition.x, aPosition.y, tzH, 1);
    vTextureCoord = aTexCoor;
}
