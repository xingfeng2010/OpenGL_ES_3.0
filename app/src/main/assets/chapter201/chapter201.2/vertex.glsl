#version 300 es
//总变换举证
uniform mat4 uMVPMatrix;
//变换矩阵
uniform mat4 uMMatrix;
//光源位置
uniform vec3 uLightLocation;
//摄像机位置
uniform vec3 uCamera;

//顶点位置
in vec3 aPosition;
//顶点法向量
in vec3 aNormal;
//顶点纹理坐标
in vec2 aTexCoor;

//用于传递给片元着色器的变量
out vec4 ambient;
out vec4 diffuse;
out vec4 specular;
out vec2 vTextureCoord;

//定位光光照计算方法
void pointLight(
in vec3 normal, //法向量
inout vec4 ambient, //环境光最终强度
inout vec4 diffuse, //散射光最终强度
inout vec4 specular, //镜面光最终强度
in vec3 lightLocation, //光源位置
in vec4 lightAmbient, //环境光强度
in vec4 lightDiffuse, //散射光强度
in vec4 lightSpecular//镜面光强度
) {
    ambient = lightAmbient;
    //变换后的法向量
    vec3 normalTarget = normal + aPosition;
    vec3 newNormal = (uMMatrix * vec4(normalTarget,1)).xyz - (uMMatrix * vec4(aPosition, 1)).xyz;
    //法向量规格化
    newNormal = normalize(newNormal);
    //计算从表面点到摄相机的向量
    vec3 eye = normalize(uCamera - (uMMatrix * vec4(aPosition, 1)).xyz);
    //计算从表面点到光源位置的向量
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(aPosition, 1)).xyz);
    //求视线与光线的半向量
    vec3 halfVector=normalize(vp+eye);
    //粗糙度，越小越光滑
    float shiness = 50.0;
    //求法向量与vp的点积与0的最大值;
    float nDotViewPosition=max(0.0,dot(newNormal,vp));
    //计算散射光的最终强度
    diffuse = lightDiffuse * nDotViewPosition;
    //法向量与半向量的点积
    float nDotHafPosition = dot(newNormal, halfVector);
    //镜面反射光强度因子
    float powerFactor = max(0.0, pow(nDotHafPosition, shiness));
    specular = lightSpecular * powerFactor;
}

void main() {
    //根据总变换矩阵计算此此绘制此顶点的位置
    gl_Position = uMVPMatrix * vec4(aPosition, 1);

    vec4 ambientTemp, diffuseTemp, specularTemp;
    pointLight(normalize(aNormal), ambientTemp, diffuseTemp, specularTemp,
    uLightLocation,
    vec4(0.15, 0.15, 0.15, 1.0),
    vec4(0.9, 0.9, 0.9, 1.0),
    vec4(0.4, 0.4, 0.4, 1.0));

    ambient = ambientTemp;
    diffuse = diffuseTemp;
    specular = specularTemp;

    vTextureCoord = aTexCoor;
}
