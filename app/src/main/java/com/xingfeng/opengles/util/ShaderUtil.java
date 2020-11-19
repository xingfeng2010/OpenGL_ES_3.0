package com.xingfeng.opengles.util;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

//º”‘ÿ∂•µ„Shader”Î∆¨‘™Shaderµƒπ§æﬂ¿‡
public class ShaderUtil
{
    //º”‘ÿ÷∆∂®shaderµƒ∑Ω∑®
    public static int loadShader
    (
            int shaderType, //shaderµƒ¿‡–Õ  GLES30.GL_VERTEX_SHADER(∂•µ„)   GLES30.GL_FRAGMENT_SHADER(∆¨‘™)
            String source   //shaderµƒΩ≈±æ◊÷∑˚¥Æ
    )
    {
        //¥¥Ω®“ª∏ˆ–¬shader
        int shader = GLES30.glCreateShader(shaderType);
        //»Ù¥¥Ω®≥…π¶‘Úº”‘ÿshader
        if (shader != 0)
        {
            //º”‘ÿshaderµƒ‘¥¥˙¬Î
            GLES30.glShaderSource(shader, source);
            //±‡“Îshader
            GLES30.glCompileShader(shader);
            //¥Ê∑≈±‡“Î≥…π¶shader ˝¡øµƒ ˝◊È
            int[] compiled = new int[1];
            //ªÒ»°Shaderµƒ±‡“Î«Èøˆ
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0)
            {//»Ù±‡“Î ß∞‹‘Úœ‘ æ¥ÌŒÛ»’÷æ≤¢…æ≥˝¥Àshader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    //¥¥Ω®shader≥Ã–Úµƒ∑Ω∑®
    public static int createProgram(String vertexSource, String fragmentSource)
    {
        //º”‘ÿ∂•µ„◊≈…´∆˜
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0)
        {
            return 0;
        }

        //º”‘ÿ∆¨‘™◊≈…´∆˜
        int pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0)
        {
            return 0;
        }

        //¥¥Ω®≥Ã–Ú
        int program = GLES30.glCreateProgram();
        //»Ù≥Ã–Ú¥¥Ω®≥…π¶‘ÚœÚ≥Ã–Ú÷–º”»Î∂•µ„◊≈…´∆˜”Î∆¨‘™◊≈…´∆˜
        if (program != 0)
        {
            //œÚ≥Ã–Ú÷–º”»Î∂•µ„◊≈…´∆˜
            GLES30.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //œÚ≥Ã–Ú÷–º”»Î∆¨‘™◊≈…´∆˜
            GLES30.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //¡¥Ω”≥Ã–Ú
            GLES30.glLinkProgram(program);
            //¥Ê∑≈¡¥Ω”≥…π¶program ˝¡øµƒ ˝◊È
            int[] linkStatus = new int[1];
            //ªÒ»°programµƒ¡¥Ω”«Èøˆ
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            //»Ù¡¥Ω” ß∞‹‘Ú±®¥Ì≤¢…æ≥˝≥Ã–Ú
            if (linkStatus[0] != GLES30.GL_TRUE)
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    //ºÏ≤È√ø“ª≤Ω≤Ÿ◊˜ «∑Ò”–¥ÌŒÛµƒ∑Ω∑®
    @SuppressLint("NewApi")
    public static void checkGlError(String op)
    {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR)
        {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    //¥”shΩ≈±æ÷–º”‘ÿshaderƒ⁄»›µƒ∑Ω∑®
    public static String loadFromAssetsFile(String fname,Resources r)
    {
        String result=null;
        try
        {
            InputStream in=r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((ch=in.read())!=-1)
            {
                baos.write(ch);
            }
            byte[] buff=baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
