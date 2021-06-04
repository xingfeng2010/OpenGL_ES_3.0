#version 300 es
//总变换矩阵
uniform mat4 uMVPMatrix;
//顶点位置(来自1号关键帧)
in vec3 aPosition;

//顶点纹理坐标
in vec2 aTexCoor;

out vec2 vTextureCoord;

//当前整体扭动角度因子
uniform float ratio;

void main() {
    //圆周率
    float pi = 3.1415926;
    //中心点X坐标
    float centerX = 0.0;
    //中心点Y坐标
    float centerY = 5.0;
    //当前点的x坐标
    float currX = aPosition.x;
    //当前点的y坐标
    float currY = aPosition.y;
    //当前X偏移量
    float spanX = currX - centerX;
    //当前Y偏移量
    float spanY = currY - centerY;
    //计算距离
    float currRadius = sqrt(spanX  * spanX + spanY * spanY);
    //当前点与x轴正方向的夹角
    float currRadians;
    if (spanX != 0.0) {
        currRadians = atan(spanY, spanX);
    } else {
        currRadians = atan(spanY, spanX);
    }

    float resultRadians = currRadians + ratio*currRadius;//计算出扭曲后的角度
    float resultX = centerX + currRadius * cos(resultRadians);//计算结果点的x坐标
    float resultY = centerY + currRadius * sin(resultRadians);//计算结果点的y坐标
    //构造结果点，并根据总变换矩阵计算此次绘制此顶点的位置
    gl_Position = uMVPMatrix * vec4(resultX,resultY,0.0,1);
    vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
}
