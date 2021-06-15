#version 300 es
precision mediump float;//给出默认的浮点精度
in vec2 vTextureCoord;//从顶点着色器传递过来的纹理坐标
uniform sampler2D sTexture;//纹理内容数据
out vec4 fFragColor;//输出的片元颜色

void main() {
    vec4 sampleArray[25];
    float texCoordOffsets[25];

    float xInc = 1.0f / 1920.0;
    float yInc = 1.0f / 1080.0;

    for (int i = 0; i < 5; i++)
    {
        for (int j = 0; j < 5; j++)
        {
            texCoordOffsets[(((i*5)+j)*2)+0] = (-2.0f * xInc) + (1.0 * i * xInc);
            texCoordOffsets[(((i*5)+j)*2)+1] = (-2.0f * yInc) + (1.0* j * yInc);
        }
    }

    for (int i = 0; i < 25; i++)
    {
        // Sample a grid around and including our texel
        sampleArray[i] = texture(sTexture, vTextureCoord + texCoordOffsets[i]);
    }

    // Gaussian weighting:
    // 1  4  7  4 1
    // 4 16 26 16 4
    // 7 26 41 26 7 / 273 (i.e. divide by total of weightings)
    // 4 16 26 16 4
    // 1  4  7  4 1

    vec4 finalColor = (
    (1.0  * (sampleArray[0] + sampleArray[4]  + sampleArray[20] + sampleArray[24])) +
    (4.0  * (sampleArray[1] + sampleArray[3]  + sampleArray[5]  + sampleArray[9] + sampleArray[15] + sampleArray[19] + sampleArray[21] + sampleArray[23])) +
    (7.0  * (sampleArray[2] + sampleArray[10] + sampleArray[14] + sampleArray[22])) +
    (16.0 * (sampleArray[6] + sampleArray[8]  + sampleArray[16] + sampleArray[18])) +
    (26.0 * (sampleArray[7] + sampleArray[11] + sampleArray[13] + sampleArray[17])) +
    (41.0 * sampleArray[12])
    ) / 273.0;

    fFragColor = finalColor;
}