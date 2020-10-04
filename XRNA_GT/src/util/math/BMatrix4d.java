package util.math;

import java.awt.geom.*;
import java.util.*;

import javax.vecmath.*;

public class
BMatrix4d
extends Matrix4d
{

public
BMatrix4d()
{
	super();
}

public
BMatrix4d(BMatrix4d mat)
{
	super(mat);
}

// translate to origin, rotate x,y,z, translate back to transPt
// xRot, yRot, zRot in radians
// THIS SEEMS BOGUS. don't know what transPt is unless refers to a vector tail
// THIS IS NOT TESTED OR UNDERSTOOD as to its purpose
public static BMatrix4d
setTransformation(BVector3d transPt, double xRot, double yRot, double zRot)
{
	BVector3d negTransPt = new BVector3d(transPt);
	negTransPt.negate();

	BMatrix4d transMat = new BMatrix4d();
	transMat.setIdentity();
	transMat.setTranslation(negTransPt);
	BMatrix4d resultMat = new BMatrix4d(transMat);

	BMatrix4d xRotMat = new BMatrix4d();
	xRotMat.rotX(xRot);
	BMatrix4d yRotMat = new BMatrix4d();
	yRotMat.rotY(yRot);
	BMatrix4d zRotMat = new BMatrix4d();
	zRotMat.rotZ(zRot);

	BMatrix4d rotConCatMat = new BMatrix4d(zRotMat);
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
	System.out.println("BMatrix4d-> " + s);
}


}
