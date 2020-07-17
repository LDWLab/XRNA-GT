package util;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;

import util.math.BMatrix4d;
import util.math.BVector3d;
import util.math.BVector4d;
import util.math.MathDefines;
import util.math.MathUtil;

public class
Graphics3DUtil
{

public static boolean
raySphereIntersect(Point3d tailPt, Point3d headPt, Point3d origin,
	double radius, float[] tvals)
{
	double[] originVals = new double[3];
	origin.get(originVals);
	double[] tailPtVals = new double[3];
	tailPt.get(tailPtVals);
	double[] headPtVals = new double[3];
	headPt.get(headPtVals);

	double h = originVals[0];
	double k = originVals[1];
	double l = originVals[2];
	double ax = tailPtVals[0];
	double ay = tailPtVals[1];
	double az = tailPtVals[2];
	double cx = headPtVals[0];
	double cy = headPtVals[1];
	double cz = headPtVals[2];

	double a = cz*cz - 2.0*az*cz + cy*cy - 2.0*ay*cy + cx*cx
		- 2.0*ax*cx + az*az + ay*ay + ax *ax;
	double b = -2.0*cz*l + 2.0*az*l - 2.0*cy*k + 2.0*ay*k
		- 2.0*cx*h + 2.0*ax*h + 2.0*az*cz + 2.0*ay*cy
		+ 2.0*ax*cx - 2.0*az*az - 2.0*ay*ay - 2.0*ax*ax;
	double c = - radius*radius + l*l - 2.0*az*l + k*k
		- 2.0*ay*k + h*h - 2.0*ax*h + az*az + ay*ay + ax*ax;

	double radical = b*b - 4.0*a*c;
	if (radical < 0.0)
	{
		tvals[0] = tvals[1] = (float) -1.0;
		return (false);
	}

	double rsqrt = Math.sqrt(radical);
	tvals[0] = (float) ((-b - rsqrt)/(2.0*a));
	tvals[1] = (float) ((-b + rsqrt)/(2.0*a));
	return (true);
}

/**
** Sets a new point, on this vector, a multiple of t times away
** from tail.
*/

public static void
getPointAtT(Point3d tailPt, Point3d headPt, Point3d newPt, double t)
{
	double[] tailPtVals = new double[3];
	tailPt.get(tailPtVals);
	double[] headPtVals = new double[3];
	headPt.get(headPtVals);

	newPt.set(
		tailPtVals[0] +
			(t * (headPtVals[0] - tailPtVals[0])),
		tailPtVals[1] +
			(t * (headPtVals[1] - tailPtVals[1])),
		tailPtVals[2] +
			(t * (headPtVals[2] - tailPtVals[2])));
}

/*
double
RaySphereDistance(tailPt, headPt, origin, t)
point tailPt, headPt, origin;
double *t;
{
	double DistPtToLine();

	return(DistPtToLine(tailPt, headPt, origin, t));

}

char
RayIntersectsSphere(tailPt, headPt, sphereradius, sphereorigin)
point tailPt, headPt;
double sphereradius;
point sphereorigin;
{
	float t, firstT, lastT;
	float RaySphereDistance();

	if(RaySphereDistance(tailPt, headPt, sphereorigin, &t) > sphereradius)
		return(FALSE);
	RaySphereIntersect(tailPt, headPt, sphereorigin, sphereradius, &firstT, &lastT);
	if(((firstT >= 0.0) && (firstT <= 1.0)) || ((lastT >= 0.0) && (lastT <= 1.0)))
		return(TRUE);
	return(FALSE);

}
*/

//BuildXRotationMatrix(&xrotmat, -Angle(spt, YZPlane));

public static AxisAngle4d
xAxisAngleToXYPlane(BVector4d vec)
{
	return (new AxisAngle4d(1.0, 0.0, 0.0, -(MathDefines.DegToRad*MathUtil.angleInYZPlane(vec))));
}

public static AxisAngle4d
yAxisAngleToXZPlane(BVector4d vec)
{
	return (new AxisAngle4d(0.0, 1.0, 0.0,
		(Math.PI/2.0) - (MathDefines.DegToRad*MathUtil.angleInXZPlane(vec))));
}

public static AxisAngle4d
zAxisAngleToXAxis(BVector4d vec)
{
	return (new AxisAngle4d(0.0, 0.0, 1.0, -(MathDefines.DegToRad*MathUtil.angleInXYPlane(vec))));
}


// takes 3 pts translates to origin and rotates fpt and spt until they're
// in the XY Plane with fpt on xaxis.
// Returns either the transformation matrix that will do this
// or the inverse of the transformation matrix that will do this.
public static BMatrix4d
normalizePoints(BVector4d ptA, BVector4d ptB, BVector4d ptC, boolean invert)
throws Exception
{
	BVector4d fpt = new BVector4d(ptA);
	BVector4d midpt = new BVector4d(ptB);
	BVector4d spt = new BVector4d(ptC);

	double[] midPtVals = new double[4];
	midpt.get(midPtVals);
	
	BMatrix4d transMat =  new BMatrix4d();
	transMat.setIdentity();
	BVector3d negatePt = new BVector3d(midPtVals[0], midPtVals[1], midPtVals[2]);
	negatePt.negate();
	transMat.setTranslation(negatePt);

	// translate all points to origin through midpt
	transMat.transform(fpt);
	transMat.transform(midpt);
	transMat.transform(spt);

	// Step 1) rotate configuration around the y-axis so
	// that the first point lies in the positive XY-plane.
	BMatrix4d yRotMat = new BMatrix4d();
	yRotMat.rotY(MathDefines.DegToRad * MathUtil.angleInXZPlane(fpt));
	yRotMat.transform(fpt);
	yRotMat.transform(spt);

	// Step 2) rotate configuration around the z-axis so that the
	// first point lies on the positive x-axis.
	BMatrix4d zRotMat = new BMatrix4d();

	// MIGHT NEED to change to MathUtil.angleIn like above:
	zRotMat.set(Graphics3DUtil.zAxisAngleToXAxis(fpt));

	zRotMat.transform(fpt); // don't have to do this step, debug only
	zRotMat.transform(spt);

	// Step 3) rotate configuration around the x-axis so that the
	// second point lies in the positive XY-plane.
	BMatrix4d xRotMat = new BMatrix4d();

	// MIGHT NEED to change to MathUtil.angleIn like above:
	xRotMat.set(Graphics3DUtil.xAxisAngleToXYPlane(spt));

	xRotMat.transform(fpt); // don't have to do this step, debug only
	xRotMat.transform(spt); // don't have to do this step, debug only

	if (invert)
	{
		transMat.invert();
		yRotMat.invert();
		xRotMat.invert();
		zRotMat.invert();
		BMatrix4d inverseMat = new BMatrix4d();
		
		inverseMat.setIdentity();
		inverseMat.mul(transMat, yRotMat);
		inverseMat.mul(zRotMat);
		inverseMat.mul(xRotMat);
		
		return (inverseMat);
	}
	else
	{
		BMatrix4d concatMat = new BMatrix4d();
		concatMat.setIdentity();

		/*
		concatMat.mul(xRotMat, zRotMat);
		*/
		concatMat.mul(xRotMat);
		concatMat.mul(zRotMat);

		concatMat.mul(yRotMat);
		concatMat.mul(transMat);
		return (concatMat);
	}
}

public static BMatrix4d
getNormalizePtsTransform(BVector4d fromPt1, BVector4d fromPt2, BVector4d fromPt3,
	BVector4d A, BVector4d B, BVector4d C)
throws Exception
{
	BVector4d NA = new BVector4d(fromPt1);
	BVector4d NB = new BVector4d(fromPt2);
	BVector4d NC = new BVector4d(fromPt3);
	BVector4d MA = new BVector4d(A);
	BVector4d MB = new BVector4d(B);
	BVector4d MC = new BVector4d(C);
	BMatrix4d conCatMat = new BMatrix4d();
	conCatMat.setIdentity();

	/*
	** Create a transformation matrix describing how from pts
	** are transformed to a normal position.
	** Create another transformation matrix describing how
	** to pts are transformed to same normal position.
	** Compose inverse of second transformation matrix with first.
	*/

	/* orig:
	BMatrix4d tmpmat0 = normalizePoints(NA, NC, NB, false);
	BMatrix4d tmpmat1 = normalizePoints(MA, MC, MB, true);
	conCatMat.mul(tmpmat0, tmpmat1);
	*/
	
	/*
	BMatrix4d tmpmat0 = normalizePoints(NA, NC, NB, false);
	BMatrix4d tmpmat1 = normalizePoints(MA, MC, MB, true);
	*/
	BMatrix4d tmpmat0 = normalizePoints(NA, NB, NC, false);
	BMatrix4d tmpmat1 = normalizePoints(MA, MB, MC, true);
	conCatMat.mul(tmpmat1, tmpmat0);
	
	return(conCatMat);
}

public static boolean
testPrecision(double testVal, double cmpVal, double precision)
{
	return (Math.abs(testVal - cmpVal) <= precision);
}


public static void
gaussEliminate(BMatrix4d amat)
throws Exception
{
	int icol = 0;
	int irow = 0;
	double tmpdval = 0.0;
	double big = 0.0;
	double dum = 0.0;
	double pivinv = 0.0;
	double tmpfval = 0.0;
	int[] indxc = new int[4];
	int[] indxr = new int[4];
	int[] ipiv = new int[4];

	for(int j = 0;j < 4;j++)
		ipiv[j] = 0;
	for(int i = 0;i < 4;i++)
	{
		big = 0.0;
		for(int j = 0;j < 4;j++)
		{
			if(ipiv[j] != 1)
			{
				for(int k = 0;k < 4;k++)
				{
					if(ipiv[k] == 0)
					{
						// tmpdval = (double)amat[j][k];
						tmpdval = amat.getElement(j, k);
						tmpfval = Math.abs(tmpdval);
						if(tmpfval >= big)
						{
							big = tmpfval;
							irow = j;
							icol = k;
						}
					}
					else if(ipiv[k] > 1)
					{
						throw new Exception("singular matrix at 1");
					}
				}
			}
		}
		ipiv[icol] = ipiv[icol] + 1;
		if(irow != icol)
		{
			for(int l = 0;l < 4;l++)
			{
				// dum = amat[irow][l];
				dum = amat.getElement(irow, l);
				amat.setElement(irow, l, amat.getElement(icol, l));
				amat.setElement(icol, l, dum);
			}
		}
		indxr[i] = irow;
		indxc[i] = icol;
		if(amat.getElement(icol, icol) == 0.0)
		{
			throw new Exception("singular matrix at 2");
		}
		pivinv = 1.0/amat.getElement(icol, icol);
		amat.setElement(icol, icol, 1.0);
		for(int l = 0;l < 4;l++)
		{
			amat.setElement(icol, l, amat.getElement(icol, l) * pivinv);
		}
		for(int ll = 0;ll < 4;ll++)
		{
			if(ll != icol)
			{
				dum = amat.getElement(ll, icol);
				amat.setElement(ll, icol, 0.0);
				for(int l = 0;l < 4;l++)
				{
					amat.setElement(ll, l, amat.getElement(ll, l) - (amat.getElement(icol, l) * dum));
				}
			}
		}
	}
	for(int l = 3;l >= 0;l--)
	{
		if(indxr[l] != indxc[l])
		{
			for(int k = 0;k < 4;k++)
			{
				dum = amat.getElement(k, indxr[l]);
				amat.setElement(k, indxr[l], amat.getElement(k, indxc[l]));
				amat.setElement(k, indxc[l], dum);
			}
		}
	}
}

private static void
debug(String s)
{
	System.out.println("Graphics3DUtil-> " + s);
}

}
