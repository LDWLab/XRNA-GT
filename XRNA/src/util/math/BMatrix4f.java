package util.math;

import java.awt.geom.*;
import java.util.*;

import javax.vecmath.*;

public class
BMatrix4f
extends Matrix4f
{

public
BMatrix4f()
{
	super();
}

public
BMatrix4f(BMatrix4f mat)
{
	super(mat);
}

// translate to origin, rotate x,y,z, translate back to transPt
// xRot, yRot, zRot in radians
public static BMatrix4f
setTransform(BVector3f transPt, float xRot, float yRot, float zRot)
{
	BVector3f negTransPt = new BVector3f(transPt);
	negTransPt.negate();

	BMatrix4f transMat = new BMatrix4f();
	transMat.setIdentity();
	transMat.setTranslation(negTransPt);
	BMatrix4f resultMat = new BMatrix4f(transMat);

	BMatrix4f xRotMat = new BMatrix4f();
	xRotMat.rotX(xRot);
	BMatrix4f yRotMat = new BMatrix4f();
	yRotMat.rotY(yRot);
	BMatrix4f zRotMat = new BMatrix4f();
	zRotMat.rotZ(zRot);

	BMatrix4f rotConCatMat = new BMatrix4f(zRotMat);
	rotConCatMat.mul(yRotMat, rotConCatMat);
	rotConCatMat.mul(xRotMat, rotConCatMat);

	resultMat.mul(rotConCatMat, resultMat);

	transMat.setIdentity();
	transMat.setTranslation(transPt);
	resultMat.mul(transMat, resultMat);

	return (resultMat);
}

public String
myToString()
{
	StringBuffer strBuf = new StringBuffer();
	strBuf.append(this.m00 + " " + this.m01 + " " + this.m02 + " " + this.m03 + "\n");
	strBuf.append(this.m10 + " " + this.m11 + " " + this.m12 + " " + this.m13 + "\n");
	strBuf.append(this.m20 + " " + this.m21 + " " + this.m22 + " " + this.m23 + "\n");
	strBuf.append(this.m30 + " " + this.m31 + " " + this.m32 + " " + this.m33);
	return (strBuf.toString());
}

public String
toString()
{
/*
debug("mat:\n");
debug("" + mat.m00 + " " + mat.m01 + " " + mat.m02 + " " + mat.m03);
debug("" + mat.m10 + " " + mat.m11 + " " + mat.m02 + " " + mat.m13);
debug("" + mat.m20 + " " + mat.m21 + " " + mat.m22 + " " + mat.m23);
debug("" + mat.m30 + " " + mat.m31 + " " + mat.m32 + " " + mat.m33);
*/
	return (super.toString());
}

private static void
debug(String s)
{
	System.out.println("BMatrix4f-> " + s);
}


}
