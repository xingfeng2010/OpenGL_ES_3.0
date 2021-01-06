#version 300 es
//叶子的顶点着色器
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;

void main() {

    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    vTextureCoord = aTexCoor;
}
