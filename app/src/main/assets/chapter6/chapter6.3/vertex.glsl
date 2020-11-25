#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
in vec3 aPosition;  //顶点位置
in vec3 aNormal;    //顶点法向量
out vec3 vPosition;//用于传递给片元着色器的顶点位置
out vec4 vDiffuse; //用于传递给片元着色器的散射光分量

//散射光光照计算方法
void pointLight(
    in vec3 normal,  //法向量
    inout vec4 diffuse, //  散射光计算结果
    in vec3 lightLocation, //光源位置
    in vec4 lightDiffuse   //散射光强度
) {
    //计算变换后的法向量
    vec3 normalTarget = aPosition + normal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(aPosition,1)).xyz;
    //对法向量规格化
    newNormal = normalize(newNormal);
    //计算从表面点到光源位置的向量VP
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(aPosition,1)).xyz);
    vp = normalize(vp);
    //求法向量与VP向量的点积与0的最大值
    float nDotViewPosition = max(0.0, dot(newNormal,vp));
    //计算散射光的最终强度
    diffuse = lightDiffuse * nDotViewPosition;
}

void main()
{
   //根据总变换矩阵计算此次绘制此顶点位置
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vec4 diffuseTemp = vec4(0.0,0.0,0.0,0.0);
   pointLight(normalize(aNormal),diffuseTemp, uLightLocation,vec4(0.8,0.8,0.8,1.0));
   //将顶点的位置传给片元着色器
   vPosition = aPosition;//将原始顶点位置传递给片元着色器
   //将散射光强度传给片元着色器
   vDiffuse = diffuseTemp;
}