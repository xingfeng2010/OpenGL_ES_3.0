#version 300 es
precision mediump float;
in vec2 mcLongLat;//接收从顶点着色器过来的参数
out vec4 fFragColor;//输出的片元颜色
void main()
{
    vec3 redColor=vec3(1.0,1.0,1.0);//红色
    vec3 greenColor=vec3(0.75,0.75,0.75);//绿色
    vec3 color;//片元的最终颜色

    float ny=(mcLongLat.y+90.0)/10.0;
    int column = int(mod((mcLongLat.x)/30.0,2.0));

    if(ny<=2.0 || ny >= 16.0)
    {
        color=redColor;
    }
    else
    {
        if (column == 1) {
            color=redColor;
        } else {
            color = greenColor;
        }
    }
    //将片元的最终颜色传递进渲染管线
    fFragColor=vec4(color,1.0);
}
