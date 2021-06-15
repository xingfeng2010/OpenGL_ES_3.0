#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

const vec2 texSize = vec2(1920, 1080);

/*
浮雕效果是指图像的前景前向凸出背景。
实现思路：把图象的一个象素和左上方的象素进行求差运算，
并加上一个灰度。这个灰度就是表示背景颜色。
这里我们设置这个插值为128 (图象RGB的值是0-255)。
同时,我们还应该把这两个颜色的差值转换为亮度信息，避免浮雕图像出现彩色像素。
*/
void main() {
    vec2 tex = vTextureCoord;
    vec2 upLeftUV = vec2(tex.x - 1.0 / texSize.x, tex.y - 1.0/texSize.y);
    vec4 textureColor = texture(sTexture, vTextureCoord);
    vec4 upColor = texture(sTexture, upLeftUV);
    vec4 delColor = textureColor - upColor;
    float h = 0.3 * delColor.x + 0.59*delColor.y + 0.11*delColor.z;
    vec4 bkColor = vec4(0.5, 0.5, 0.5, 1.0);

    fFragColor = vec4(h, h, h,0.0) + bkColor;
}