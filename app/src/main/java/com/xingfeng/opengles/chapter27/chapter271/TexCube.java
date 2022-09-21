package com.xingfeng.opengles.chapter27.chapter271;
import android.view.View;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.xingfeng.opengles.util.MatrixState;
import com.xingfeng.opengles.util.SYSUtil;

public class TexCube
{
    TextureRect tr;
    float halfSize;
    RigidBody body;
    int mProgram;
    View mv;
    public TexCube(View mv,float halfSize,CollisionShape colShape,
                   DiscreteDynamicsWorld dynamicsWorld,float mass,float cx,float cy,float cz,int mProgram)
    {
        boolean isDynamic = (mass != 0f);
        Vector3f localInertia = new Vector3f(0, 0, 0);
        if(isDynamic)
        {
            colShape.calculateLocalInertia(mass, localInertia);
        }
        Transform startTransform = new Transform();
        startTransform.setIdentity();
        startTransform.origin.set(new Vector3f(cx, cy, cz));

        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);

        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
                (mass, myMotionState, colShape, localInertia);
        body = new RigidBody(rbInfo);
        body.setRestitution(0.6f);
        body.setFriction(0.8f);
        dynamicsWorld.addRigidBody(body);
        this.mv=mv;
        tr=new TextureRect(halfSize);
        this.mProgram=mProgram;
        this.halfSize=halfSize;
    }

    public void drawSelf(int[] texIda)
    {
        tr.initShader(mv, mProgram);
        int texId=texIda[0];
        if(!body.isActive()){texId=texIda[1];}
        MatrixState.pushMatrix();

        Transform trans = body.getMotionState().getWorldTransform(new Transform());

        MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
        Quat4f ro=trans.getRotation(new Quat4f());
        if(ro.x!=0||ro.y!=0||ro.z!=0)
        {
            float[] fa= SYSUtil.fromSYStoAXYZ(ro);
            MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);
        }
        MatrixState.pushMatrix();
        MatrixState.translate(0, halfSize, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        tr.drawSelf( texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0, -halfSize, 0);
        MatrixState.rotate(90, 1, 0, 0);
        tr.drawSelf( texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(-halfSize, 0, 0);
        MatrixState.rotate(-90, 0, 1, 0);
        tr.drawSelf( texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(halfSize, 0, 0);
        MatrixState.rotate(90, 0, 1, 0);
        tr.drawSelf( texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, halfSize);
        tr.drawSelf(texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -halfSize);
        MatrixState.rotate(180, 0, 1, 0);
        tr.drawSelf( texId);
        MatrixState.popMatrix();
        MatrixState.popMatrix();
    }
}