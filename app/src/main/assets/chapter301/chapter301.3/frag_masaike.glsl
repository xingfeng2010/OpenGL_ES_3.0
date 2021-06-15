#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

const vec2 TexSize = vec2(400.0, 400.0);
const vec2 mosaicSize = vec2(8.0, 8.0);

/*
马赛克效果就是把图片的一个相当大小的区域用同一个点的颜色来表示.
可以认为是大规模的降低图像的分辨率,而让图像的一些细节隐藏起来。
*/
void main() {
    vec2 intXY = vec2(vTextureCoord.x*TexSize.x, vTextureCoord.y*TexSize.y);
    vec2 XYMosaic = vec2(floor(intXY.x/mosaicSize.x)*mosaicSize.x, floor(intXY.y/mosaicSize.y)*mosaicSize.y);
    vec2 UVMosaic = vec2(XYMosaic.x/TexSize.x, XYMosaic.y/TexSize.y);
    vec4 color = texture(sTexture, UVMosaic);

    fFragColor = color;
}