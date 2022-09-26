//
// Created by JinTao Liu on 2022/2/4.
//
#include "RenderJNI.h"
#include <android/asset_manager_jni.h>
#include <cmath>

#include <jni.h>
#include <GLES3/gl3.h>
#include <android/log.h>

#include "btBulletDynamicsCommon.h"
#include <glm/glm.hpp>
#include <glm/ext.hpp>
#include <glm/gtx/string_cast.hpp>

#define LOG_TAG "ndk-build"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#include <android/asset_manager_jni.h>

GLint g_programObject;
jint  g_width;
jint g_height;

jint circle_x = 600;
jint circle_y = 600;

AAssetManager* g_pAssetManager = NULL;
void drawCircle(GLfloat vVertices[],float radius,int n,float x,float y);
void drawBox(GLfloat vVertices[],int pos,float x1,float y1,float x2,float y2);
void drawBoxAndSphere(float radius,float circleX,float circleY,float boxX1,float boxY1,float boxX2,float boxY2);
GLuint  LoadShader(GLenum type,const char  * shaderSrc)
{
    GLuint shader;
    GLint compiled;

    //Create the shader object
    shader = glCreateShader(type);

    if ( shader == 0)
    {
        return 0;
    }

    glShaderSource(shader,1,&shaderSrc,NULL);

    glCompileShader(shader);

    glGetShaderiv(shader,GL_COMPILE_STATUS,&compiled);

    if(!compiled)
    {
        GLint infoLen = 0;
        glGetShaderiv(shader,GL_INFO_LOG_LENGTH,&infoLen);

        if(infoLen > 1)
        {
            char *infoLog = (char*)malloc(sizeof(char) * infoLen);
            glGetShaderInfoLog(shader,infoLen,NULL,infoLog);
            LOGE("Error compiling shader:[%s]",infoLog);

            free(infoLog);
        }
        glDeleteShader(shader);
        return 0;
    }

    return shader;
}
btDiscreteDynamicsWorld* dynamicsWorld;
void createBullet(){

    ///-----includes_end-----

    int i;
    ///-----initialization_start-----

    ///collision configuration contains default setup for memory, collision setup. Advanced users can create their own configuration.
    //冲突配置包含内存的默认设置，冲突设置。高级用户可以创建自己的配置
    btDefaultCollisionConfiguration* collisionConfiguration = new btDefaultCollisionConfiguration();

    ///use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
    btCollisionDispatcher* dispatcher = new btCollisionDispatcher(collisionConfiguration);

    ///btDbvtBroadphase is a good general purpose broadphase. You can also try out btAxis3Sweep.
    btBroadphaseInterface* overlappingPairCache = new btDbvtBroadphase();

    ///the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
    btSequentialImpulseConstraintSolver* solver = new btSequentialImpulseConstraintSolver;

    dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

    dynamicsWorld->setGravity(btVector3(0, -10, 0));

    ///-----initialization_end-----

    //keep track of the shapes, we release memory at exit.
    //make sure to re-use collision shapes among rigid bodies whenever possible!
    btAlignedObjectArray<btCollisionShape*> collisionShapes;

    ///create a few basic rigid bodies

    //the ground is a cube of side 100 at position y = -56.
    //the sphere will hit it at y = -6, with center at -5
    {
        btCollisionShape* groundShape = new btBoxShape(btVector3(btScalar(200.), btScalar(200.), btScalar(50.)));

        collisionShapes.push_back(groundShape);

        btTransform groundTransform;
        groundTransform.setIdentity();
        groundTransform.setOrigin(btVector3(0, -300, 0));

        btScalar mass(0.);

        //rigidbody is dynamic if and only if mass is non zero, otherwise static
        bool isDynamic = (mass != 0.f);

        btVector3 localInertia(0, 0, 0);
        if (isDynamic)
            groundShape->calculateLocalInertia(mass, localInertia);

        //using motionstate is optional, it provides interpolation capabilities, and only synchronizes 'active' objects
        btDefaultMotionState* myMotionState = new btDefaultMotionState(groundTransform);
        btRigidBody::btRigidBodyConstructionInfo rbInfo(mass, myMotionState, groundShape, localInertia);
        btRigidBody* body = new btRigidBody(rbInfo);
        body->setRestitution(btScalar(1.2));
        //add the body to the dynamics world
        dynamicsWorld->addRigidBody(body);
    }

    {
        //create a dynamic rigidbody

        //btCollisionShape* colShape = new btBoxShape(btVector3(1,1,1));
        btCollisionShape* colShape = new btSphereShape(btScalar(100));
        collisionShapes.push_back(colShape);

        /// Create Dynamic Objects
        btTransform startTransform;
        startTransform.setIdentity();

        btScalar mass(6.f);

        //rigidbody is dynamic if and only if mass is non zero, otherwise static
        bool isDynamic = (mass != 0.f);

        btVector3 localInertia(0, 0, 0);
        if (isDynamic)
            colShape->calculateLocalInertia(mass, localInertia);

        startTransform.setOrigin(btVector3(0, 600, 0));

        //using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
        btDefaultMotionState* myMotionState = new btDefaultMotionState(startTransform);
        btRigidBody::btRigidBodyConstructionInfo rbInfo(mass, myMotionState, colShape, localInertia);
        btRigidBody* body = new btRigidBody(rbInfo);
        body->setRestitution(btScalar(1.2));
        dynamicsWorld->addRigidBody(body);
    }

}

void drawStep(){
    dynamicsWorld->stepSimulation(1.f / 30.f, 10);
    float radius,circleX,circleY;
    float boxX1,boxY1,boxX2,boxY2;
    //print positions of all objects
    for (int j = dynamicsWorld->getNumCollisionObjects() - 1; j >= 0; j--)
    {
        btCollisionObject* obj = dynamicsWorld->getCollisionObjectArray()[j];
        btRigidBody* body = btRigidBody::upcast(obj);
        btTransform trans;
        if (body && body->getMotionState())
        {
            body->getMotionState()->getWorldTransform(trans);
        }
        else
        {
            trans = obj->getWorldTransform();
        }
        btCollisionShape* shape = body->getCollisionShape();
        glm::mat4 model = glm::mat4(1.0f);
        trans.getOpenGLMatrix(glm::value_ptr(model));
//        __android_log_print(ANDROID_LOG_ERROR,shape->getName() ," shape");
//        shape->getUserPointer();
        int shaeType = shape->getShapeType();
//        __android_log_print(ANDROID_LOG_ERROR,shape->getName() ," shapeName");
        //box
////        btVector3 tempVector = shape->get();
////        __android_log_print(ANDROID_LOG_ERROR,"------------" + tempVector.getX(),"--------------\n");
//        __android_log_print(ANDROID_LOG_ERROR,"TAG","world scale object %d = %f,%f,%f\n", j, float(tempVector.getX()), float(tempVector.getY()), float(tempVector.getZ()));
        if (shaeType == 0){
            btBoxShape* tempShape = static_cast<btBoxShape* >(shape);
            btVector3 tempVector = tempShape->getImplicitShapeDimensions();
            boxX1 = trans.getOrigin().getX() - tempVector.getX();
            boxY1 = trans.getOrigin().getY() - tempVector.getY();
            boxX2 = trans.getOrigin().getX() + tempVector.getX();
            boxY2 = trans.getOrigin().getY() + tempVector.getY();

        }else if (shaeType == 8){
            btSphereShape* tempShape = static_cast<btSphereShape* >(shape);
            radius = tempShape->getRadius();
            circleX = trans.getOrigin().getX();
            circleY = trans.getOrigin().getY();
        }

//        for (int l = 0;l < glm::mat4::length(); ++l) {
//
//
//            __android_log_print(ANDROID_LOG_ERROR,glm::to_string(model[l]).c_str() + l,"Need to print");
//        }
//            __android_log_print(ANDROID_LOG_ERROR,glm::to_string(model).c_str(),"Need to print");
//            __android_log_print(ANDROID_LOG_ERROR,"world pos object ","Need to print :");
//        __android_log_print(ANDROID_LOG_ERROR,"TAG","world pos object %d = %f,%f,%f\n", j, float(trans.getOrigin().getX()), float(trans.getOrigin().getY()), float(trans.getOrigin().getZ()));
//        __android_log_print(ANDROID_LOG_ERROR,"------------","-\n");
    }
    drawBoxAndSphere(radius,circleX,circleY,boxX1,boxY1,boxX2,boxY2);
//    __android_log_print(ANDROID_LOG_ERROR,"------------","--------------\n");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingfeng_opengles_chapter28_chapter282_TriangleRenderJNI_glesInit(JNIEnv *env, jobject thiz) {
    // TODO: implement glesInit()
    // TODO: implement glesInit()
    char vShaderStr[] =
            "#version 300 es                          \n"
            "layout(location = 0) in vec4 vPosition;  \n"
            "void main()                              \n"
            "{                                        \n"
            "   gl_Position = vPosition;              \n"
            "}                                        \n";

    char fShaderStr[] =
            "#version 300 es                              \n"
            "precision mediump float;                     \n"
            "out vec4 fragColor;                          \n"
            "void main()                                  \n"
            "{                                            \n"
            "   fragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );  \n"
            "}                                            \n";

//    char *pVertextShader = readShaderSrcFile("shader/vs.glsl",g_pAssetManager);
//    char *pFragmentShader = readShaderSrcFile("shader/fs.glsl",g_pAssetManager);

    char *pVertextShader = vShaderStr;
    char *pFragmentShader = fShaderStr;

    GLuint  vertexShader;
    GLuint  fragmentShader;
    GLuint  programObject;
    GLint  linked;

    //load the vertext/fragment shaders
    //vertexShader = LoadShader (GL_VERTEX_SHADER,vShaderStr);
    //fragmentShader = LoadShader();

    vertexShader = LoadShader(GL_VERTEX_SHADER,pVertextShader);
    fragmentShader = LoadShader(GL_FRAGMENT_SHADER,pFragmentShader);

    //Create the program object
    programObject = glCreateProgram();

    if (programObject == 0)
    {
        return;
    }

    glAttachShader(programObject,vertexShader);
    glAttachShader(programObject,fragmentShader);

    //Link the program
    glLinkProgram(programObject);

    //Check the link status
    glGetProgramiv(programObject,GL_LINK_STATUS,&linked);

    if (!linked)
    {
        GLint  infoLen = 0;

        glGetProgramiv(programObject,GL_INFO_LOG_LENGTH,&infoLen);

        if (infoLen > 1)
        {
            char *infoLog = (char *)malloc(sizeof (char )* infoLen);

            glGetProgramInfoLog(programObject,infoLen,NULL,infoLog);
            LOGE("Error linking program:[%s]",infoLog);

            free(infoLog);
        }

        glDeleteProgram(programObject);
        return;
    }


    // Store the progrom object
    g_programObject = programObject;

    glClearColor(1.0f,1.0f,1.0f,0.0f);
    createBullet();
}


void drawCircle(GLfloat vVertices[],float radius,int n,float x,float y){

    int pos = 0;


    float angDegSpan=360.0f/n;
    for(float i=0;i<360;i+=angDegSpan){
        vVertices[pos++] = x / g_width;
        vVertices[pos++] = y / g_height;
        vVertices[pos++] = 0;

        vVertices[pos++] = (float) ((x + radius*sin(i*M_PI/180.0f)) / g_width);
        vVertices[pos++] = (float)((y + radius*cos(i*M_PI/180.0f)) / g_height);
        vVertices[pos++] = 0;

        vVertices[pos++] = (float) ((x + radius*sin((i+angDegSpan)*M_PI/180.0f)) / g_width);
        vVertices[pos++] = (float)((y + radius*cos((i+angDegSpan)*M_PI/180.0f)) / g_height);
        vVertices[pos++] = 0;
    }

}

void drawBox(GLfloat vVertices[],int pos,float x1,float y1,float x2,float y2){
    //左上角
    vVertices[pos++] = x1 / g_width;
    vVertices[pos++] = y1 / g_height;
    vVertices[pos++] = 0;
    //左下角
    vVertices[pos++] = x1 / g_width;
    vVertices[pos++] = y2 / g_height;
    vVertices[pos++] = 0;
    //右下角
    vVertices[pos++] = x2 / g_width;
    vVertices[pos++] = y2 / g_height;
    vVertices[pos++] = 0;



    //左上角
    vVertices[pos++] = x1 / g_width;
    vVertices[pos++] = y1 / g_height;
    vVertices[pos++] = 0;
    //右上角
    vVertices[pos++] = x2 / g_width;
    vVertices[pos++] = y1 / g_height;
    vVertices[pos++] = 0;
    //右下角
    vVertices[pos++] = x2 / g_width;
    vVertices[pos++] = y2 / g_height;
    vVertices[pos++] = 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingfeng_opengles_chapter28_chapter282_TriangleRenderJNI_glesRender(JNIEnv *env, jobject thiz) {
    // TODO: implement glesRender()
//    glDrawArrays(GL_TRIANGLES,0,6);
//    GLfloat vVertices[(180)*9 + 6*3];
//    drawCircle(vVertices,180,180,circle_x++,circle_y++);
//    drawBox(vVertices,180*9,-300,400,300,-400);
//
//    // Set the viewport
//    glViewport(0,0,g_width,g_height);
//
//
//    //Clear the color buffer
//    glClear(GL_COLOR_BUFFER_BIT);
//
//    //Use the program object
//    glUseProgram(g_programObject);
//
//    //Load the vertex data
//    glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,vVertices);
//    glEnableVertexAttribArray(0);
//
//    glDrawArrays(GL_TRIANGLES,0,(180)*3 + 6);
    drawStep();
}

void drawBoxAndSphere(float radius,float circleX,float circleY,float boxX1,float boxY1,float boxX2,float boxY2){
    glDrawArrays(GL_TRIANGLES,0,6);
    GLfloat vVertices[(180)*9 + 6*3];
    drawCircle(vVertices,radius,180,circleX,circleY);
    drawBox(vVertices,180*9,boxX1,boxY1,boxX2,boxY2);

    // Set the viewport
    glViewport(0,0,g_width,g_height);


    //Clear the color buffer
    glClear(GL_COLOR_BUFFER_BIT);

    //Use the program object
    glUseProgram(g_programObject);

    //Load the vertex data
    glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,vVertices);
    glEnableVertexAttribArray(0);

    glDrawArrays(GL_TRIANGLES,0,(180)*3 + 6);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xingfeng_opengles_chapter28_chapter282_TriangleRenderJNI_glesResize(JNIEnv *env, jobject thiz, jint width,
                                                             jint height) {
    // TODO: implement glesResize()
    g_width = width;
    g_height = height;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingfeng_opengles_chapter28_chapter282_TriangleRenderJNI_readShaderFile(JNIEnv *env, jobject thiz,
                                                                 jobject asset_manager) {
    // TODO: implement readShaderFile()
    if (asset_manager && env){

        g_pAssetManager = AAssetManager_fromJava(env,asset_manager);
        if (NULL == g_pAssetManager)
        {
            LOGE("AAssetManager_fromJava() return null !");
        }

    } else{
        LOGE("assetManager is null !");
    }
}