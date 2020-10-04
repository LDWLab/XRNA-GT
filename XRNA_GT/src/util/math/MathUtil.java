package util.math;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.*;
import java.util.*;

import javax.vecmath.*;

public class
MathUtil
{

// -1 == (pt left of ray), 0 == (pt on ray), 1 == (pt right of ray)
public static int
ptRelationToRayInXYPlane(double ptX, double ptY, double tailPtX,
	double tailPtY, double headPtX, double headPtY)
{
	BVector2d tmpPt1 = new BVector2d(ptX - tailPtX,
		ptY - tailPtY);
	BVector2d tmpPt2 = new BVector2d(headPtX - tailPtX,
		headPtY - tailPtY);

	BVector2d tmpPt3 = polarCoordToPoint(tmpPt1.length(),
		angleInXYPlane(tmpPt1) - angleInXYPlane(tmpPt2));
	if (precisionEquality(tmpPt3.getY(), 0.0))
		return (0);
	if (tmpPt3.getY() > 0.0)
		return (-1);
	return (1);
}

// -1 == (pt left of ray), 0 == (pt on ray), 1 == (pt right of ray)
public static int
ptRelationToRayInXYPlane(BVector2d pt, BVector2d tailPt, BVector2d headPt)
{
	return (ptRelationToRayInXYPlane(pt.getX(), pt.getY(), tailPt.getX(),
		tailPt.getY(), headPt.getX(), headPt.getY()));
}

public static int
ptRelationToRayInXYPlane(Point2D pt, Point2D tailPt, Point2D headPt)
{
	return (ptRelationToRayInXYPlane(pt.getX(), pt.getY(), tailPt.getX(),
		tailPt.getY(), headPt.getX(), headPt.getY()));
}

public static int
ptRelationToRayInXYPlane(Point2D pt, BLine2D line)
{
	return (ptRelationToRayInXYPlane(pt.getX(), pt.getY(),
		line.getP1().getX(), line.getP1().getY(),
		line.getP2().getX(), line.getP2().getY()));
}

public static int
ptRelationToRayInXYPlane(double ptX, double ptY, BLine2D line)
{
	return (ptRelationToRayInXYPlane(ptX, ptY,
		line.getP1().getX(), line.getP1().getY(),
		line.getP2().getX(), line.getP2().getY()));
}

/*
** PolarCoordToPoint() sets the ordinate and abscissa of a point,
** in a given plane, according to the given radius and angle that the
** new point is to be from the positive horizontal-axis.
** Assume XY Plane for now.
*/

/*
public static Point2D
polarCoordToPoint(double radius, double angle//, int plane//)
{
	double horizontalValue =
		radius * Math.cos(MathDefines.DegToRad * angle);
	double verticalValue =
		radius * Math.sin(MathDefines.DegToRad * angle);
	
	return (new Point2D.Double(horizontalValue, verticalValue));

	// need to work out for all planes
	switch(plane)
	{
	  case MathDefines.XYPlane :
		newPt[XCoor] = horizontalValue;
		newPt[YCoor] = verticalValue;
		break;
	  case MathDefines.XZPlane :
		newPt[XCoor] = horizontalValue;
		newPt[ZCoor] = verticalValue;
		break;
	  case MathDefines.YZPlane :
		newPt[YCoor] = horizontalValue;
		newPt[ZCoor] = verticalValue;
		break;
	  case MathDefines.YXPlane :
		newPt[YCoor] = horizontalValue;
		newPt[XCoor] = verticalValue;
		break;
	  case MathDefines.ZXPlane :
		newPt[ZCoor] = horizontalValue;
		newPt[XCoor] = verticalValue;
		break;
	  case MathDefines.ZYPlane :
		newPt[ZCoor] = horizontalValue;
		newPt[YCoor] = verticalValue;
		break;
	  default :
		break;
	}
	//
}
*/

// this for xy plane only; angle is in degrees, not radians
public static BVector2d
polarCoordToPoint(double radius, double angle)
{
	/*
	double horizontalValue =
		roundVal(radius * Math.cos(MathDefines.DegToRad * angle), 10);
	double verticalValue =
		roundVal(radius * Math.sin(MathDefines.DegToRad * angle), 10);
	*/
	double horizontalValue =
		radius * Math.cos(MathDefines.DegToRad * angle);
	double verticalValue =
		radius * Math.sin(MathDefines.DegToRad * angle);
	// debug("horival: " + horizontalValue);
	// debug("vertval: " + verticalValue);
	
	return (new BVector2d(horizontalValue, verticalValue));
}

// this for xy plane only; angle is in degrees, not radians
public static Point2D
polarCoordToPoint2D(double radius, double angle)
{
	return (polarCoordToPoint(radius, angle).getPoint2D());
}

// apparantly works up to places == 14
public static boolean
precisionEquality(double val0, double val1, int places)
{
	return (Math.round((Math.pow(10.0, (double)places)) * val0) == Math.round((Math.pow(10.0, (double)places)) * val1));
}

public static boolean
precisionEquality(double val0, double val1, double precision)
{
	return (Math.round(precision * val0) == Math.round(precision * val1));
}

public static boolean
precisionEquality(double val0, double val1)
{
	return (precisionEquality(val0, val1, MathDefines.PRECISION));
}

public static boolean
precisionEquality(float val0, float val1, float precision)
{
	return (Math.round(precision * val0) == Math.round(precision * val1));
}

public static boolean
precisionEquality(float val0, float val1)
{
	return (precisionEquality(val0, val1, MathDefines.FLOAT_PRECISION));
}


/*
public static double
angleBetween2Vectors(vec0Tail, vec0Head, vec1Tail, vec1Head)
point vec0Tail, vec0Head, vec1Tail, vec1Head;
{
	point head0Pt, head1Pt;
	float VectorsAngle();

	GetNewPoint(head0Pt);
	GetNewPoint(head1Pt);
	CopyPoint(head0Pt, vec0Head);
	CopyPoint(head1Pt, vec1Head);
	TransVecToWorldOrigin(vec0Tail, head0Pt);
	printf("%s %.3f %.3f %.3f\n", "head0Pt: ", head0Pt[XCoor], head0Pt[YCoor], head0Pt[ZCoor]); 
	TransVecToWorldOrigin(vec1Tail, head1Pt);
	printf("%s %.3f %.3f %.3f\n", "head1Pt: ", head1Pt[XCoor], head1Pt[YCoor], head1Pt[ZCoor]); 
	return (radiantoangle(VectorsAngle(head0Pt, head1Pt)));
}
*/

static BVector2d angleVec = new BVector2d();

// the angle between the + x-axis and vec projected into the XYPlane
public static double
angleInXYPlane(BVector4d vec)
{
	return (angleInXYPlane(vec.getX(), vec.getY()));
}

public static double
angleInXYPlane(double x, double y)
{
	angleVec.setXY(x, y);
	return (angleVec.angleInXYPlane());
}


public static double
angleInXYPlane(Point2D pt)
{
	return (angleInXYPlane(pt.getX(), pt.getY()));
}

public static double
angleInXYPlane(BVector2d vec)
{
	return (angleInXYPlane(vec.getX(), vec.getY()));
}

public static double
angleInXZPlane(BVector4d vec)
{
	// try dropping Y component and calling angleInXYPlane
	return (angleInXYPlane(vec.getX(), vec.getZ()));
}

public static double
angleInYZPlane(BVector4d vec)
{
	// try dropping X component and calling angleInXYPlane
	return (angleInXYPlane(vec.getY(), vec.getZ()));
}

/**
** Sets a new point, on this vector, a multiple of t times away
** from tail.
*/

public static void
getVector2dAtT(BVector2d newPt, BVector2d tailPt,
	BVector2d headPt, double t)
{
	newPt.setX(tailPt.getX() + (t * (headPt.getX() - tailPt.getX())));
	newPt.setY(tailPt.getY() + (t * (headPt.getY() - tailPt.getY())));
}

public static BVector2d
getVector2dAtT(BVector2d tailPt, BVector2d headPt, double t)
{
	BVector2d newPt = new BVector2d();
	getVector2dAtT(newPt, tailPt, headPt, t);
	return(newPt);
}

public static void
getPerpendicularPointAtT(BVector2d tailPt, BVector2d headPt, double t,
	BVector2d tailPerpPt, BVector2d headPerpPt, double length,
	boolean positiveSide)
{
	getVector2dAtT(tailPerpPt, tailPt, headPt, t);

	double deltaX = headPt.getX() - tailPt.getX();
	double deltaY = headPt.getY() - tailPt.getY();
	double multLength = length/Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	deltaX *= multLength;
	deltaY *= multLength;
	if (positiveSide)
		headPerpPt.setXY(tailPerpPt.getX() + deltaY,
			tailPerpPt.getY() - deltaX);
	else
		headPerpPt.setXY(tailPerpPt.getX() - deltaY,
			tailPerpPt.getY() + deltaX);
}

/*
** GetPerpendicularPointsAtT() returns the 2 points (tailPerpPt &
** headPerpPt) which are perpendicular to a new point that is a t value
** from tailPt to headPt a distance length from the new point.
** positiveSide is true or false depending on whether headPerpPt is on
** positive or negative side of line starting from tailPt -> headPt.
** tailPerpPt will be opposite headPerpPt an equal distance from line.
*/

public static void
getPerpendicularPointsAtT(BVector2d tailPt, BVector2d headPt, double t,
	BVector2d tailPerpPt, BVector2d headPerpPt, double length,
	boolean positiveSide)
{
	if (tailPt.equals(headPt))
	{
		tailPerpPt.copy(tailPt);
		headPerpPt.copy(headPt);
		return;
	}
	double deltaX = headPt.getX() - tailPt.getX();
	double deltaY = headPt.getY() - tailPt.getY();
	double multLength = length/Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	deltaX *= multLength;
	deltaY *= multLength;

	getVector2dAtT(tailPerpPt, tailPt, headPt, t);

	if(positiveSide)
	{
		headPerpPt.setXY(tailPerpPt.getX() + deltaY, tailPerpPt.getY() - deltaX);
		tailPerpPt.setX(tailPerpPt.getX() - deltaY);
		tailPerpPt.setY(tailPerpPt.getY() + deltaX);
	}
	else
	{
		headPerpPt.setXY(tailPerpPt.getX() - deltaY, tailPerpPt.getY() + deltaX);
		tailPerpPt.setX(tailPerpPt.getX() + deltaY);
		tailPerpPt.setY(tailPerpPt.getY() - deltaX);
	}
}


/*
//
** GetPerpendicularsPointAtT() returns the 2 points (fppt & sppt) which
** are perpendicular to a new point that is a t value from fpt to spt
** a distance length from the new point. mode is TRUE or FALSE depending
** on whether sppt is on positive or negative side of line starting
** from fpt -> spt. fppt will be opposite sppt an equal distance from line.
//

GetPerpendicularPointsAtT(fpt, spt, t, fppt, sppt, length, mode)
point fpt, spt;
float t;
point fppt, sppt;
float length;
int mode;
{
	float deltax, deltay, multlength;
	double sqrt();

	if((fpt[XCoor] == spt[XCoor]) && (fpt[YCoor] == spt[YCoor]))
	{
		CopyPoint(fppt, fpt);
		CopyPoint(sppt, spt);
		return;
	}
	GetNewPoint(fppt);
	GetNewPoint(sppt);
	GetVectorLineAtT(fpt, spt, fppt, t);
	deltax = spt[XCoor] - fpt[XCoor];
	deltay = spt[YCoor] - fpt[YCoor];
	multlength = length/(float)sqrt(deltax*deltax + deltay*deltay);
	deltax *= multlength;
	deltay *= multlength;
	if(mode)
	{
		SetPoint(sppt, fppt[XCoor] + deltay, fppt[YCoor] - deltax, 0.0);
		fppt[XCoor] -= deltay;
		fppt[YCoor] += deltax;
	}
	else
	{
		SetPoint(sppt, fppt[XCoor] - deltay, fppt[YCoor] + deltax, 0.0);
		fppt[XCoor] += deltay;
		fppt[YCoor] -= deltax;
	}

}
*/

/*
** Shell sort taken directly from Kernighan's C-manual.
**
*/

public static void
shellSort(int[] v)
{
	int n = v.length;
	int gap, i, j, temp;

	for (gap = n/2; gap > 0; gap /= 2)
	for (i = gap; i < n; i++)
	for (j=i-gap;((j>=0) && (v[j]>v[j+gap]));j-=gap)
	{
		temp = v[j];
		v[j] = v[j+gap];
		v[j+gap] = temp;
	}
}

public static double
roundVal(double val, int decimalPlace)
{
	double pow = Math.pow(10.0, (double)decimalPlace);
	return(((double)Math.round(val * pow)/pow));
}

public static float
roundVal(float val, int decimalPlace)
{
	double pow = Math.pow(10.0, (double)decimalPlace);
	return((float)(Math.round((double)val * pow)/pow));
}

/*
** Consider the linear system (in matrix form) AX = B
** A is Matrix Coefficient
** B is the nonhomogeneous term
** X the unknown column-matrix
**
** if A invertible (determinant is non 0) then can use cramers rule to
** solve for X
*/

public static BVector3f
cramer3x3Solution(Matrix3f A, BVector3f B)
{
	BVector3f X = new BVector3f();

	float detA = A.determinant();
	debug("detA: " + detA);
	if (detA == 0.0f) // then A is singular or non-invertible
		return (null);

	Matrix3f xMat = new Matrix3f(
		B.getX(), A.m01, A.m02,
		B.getY(), A.m11, A.m12,
		B.getZ(), A.m21, A.m22);

	Matrix3f yMat = new Matrix3f(
		A.m00, B.getX(), A.m02,
		A.m10, B.getY(), A.m12,
		A.m20, B.getZ(), A.m22);

	Matrix3f zMat = new Matrix3f(
		A.m00, A.m01, B.getX(),
		A.m10, A.m11, B.getY(),
		A.m20, A.m21, B.getZ());

	X.setX(xMat.determinant()/detA);
	X.setY(yMat.determinant()/detA);
	X.setZ(zMat.determinant()/detA);
	return (X);

	/*
	register int row, col;
	matrixdef equalityvector, mainarray;
	matrixptr mptr;
	float val, Determinant();

	CreateMatrix(solution, m->rowsize, 1);
	CreateMatrix(&equalityvector, m->rowsize, 1);
	CreateMatrix(&mainarray, m->rowsize, m->columnsize - 1);

	mptr = m->rectarray;
	for(row = 0;row < m->rowsize;row++)
		for(col = 0;col < m->columnsize - 1;col++)
			mainarray.rectarray[row][col] = mptr[row][col];
	for(row = 0;row < m->rowsize;row++)
		equalityvector.rectarray[row][0] = mptr[row][m->columnsize - 1];
	val = 1.0/Determinant(&mainarray);

	Adjoint(&mainarray);
	MultiplyMatrices(&mainarray, &equalityvector, solution);

	ScalarMultiplyMatrix(solution, val);

	FreeMatrix(&equalityvector);
	FreeMatrix(&mainarray);
	*/
}

public static BVector3d
cramer3x3Solution(Matrix3d A, BVector3d B)
{
	BVector3d X = new BVector3d();

	double detA = A.determinant();
	debug("detA: " + detA);
	if (detA == 0.0f) // then A is singular or non-invertible
		return (null);

	Matrix3d xMat = new Matrix3d(
		B.getX(), A.m01, A.m02,
		B.getY(), A.m11, A.m12,
		B.getZ(), A.m21, A.m22);

	Matrix3d yMat = new Matrix3d(
		A.m00, B.getX(), A.m02,
		A.m10, B.getY(), A.m12,
		A.m20, B.getZ(), A.m22);

	Matrix3d zMat = new Matrix3d(
		A.m00, A.m01, B.getX(),
		A.m10, A.m11, B.getY(),
		A.m20, A.m21, B.getZ());

	X.setX(xMat.determinant()/detA);
	X.setY(yMat.determinant()/detA);
	X.setZ(zMat.determinant()/detA);
	return (X);
}

public static Point2D
getCircleCenterFrom3Pts(Point2D pt1, Point2D pt2, Point2D pt3)
throws Exception
{
	double x1 = pt1.getX();
	double y1 = pt1.getY();
	double x2 = pt2.getX();
	double y2 = pt2.getY();
	double x3 = pt3.getX();
	double y3 = pt3.getY();

	double N1 = ((x2*x2 + y2*y2 - x1*x1 - y1*y1) * (y3 - y1)) -
		((x3*x3 + y3*y3 - x1*x1 - y1*y1) * (y2 - y1));
	double N2 = ((x2 - x1) * (x3*x3 + y3*y3 - x1*x1 - y1*y1)) -
		((x3 - x1) * (x2*x2 + y2*y2 -x1*x1 - y1*y1));
	double D = ((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1));
	if (D == 0.0)
		throw new Exception("Error in MathUtil.getCircleCenterFrom3Pts: NAN");
	return (new Point2D.Double(N1/(2.0*D), N2/(2.0*D)));
}

// returns value in first real line mapped to second real line.
public static double
rOneToROneMap(double value, double begin1, double end1,
	double begin2, double end2)
{
	return((value - begin1) * ((end2 - begin2)/(end1 - begin1)) + begin2);
}

// returns new point in second plane as mapped from first plane.
public static Point2D
rTwoToRTwoMap(double valuex, double valuey,
	double minx, double miny, double maxx, double maxy,
	double MinX, double MinY, double MaxX, double MaxY)
{
	return (new Point2D.Double(
		rOneToROneMap(valuex, minx, maxx, MinX, MaxX),
		rOneToROneMap(valuey, miny, maxy, MinY, MaxY)));
}

// returns value in first real line mapped to second real line.
public static int
rOneToROneMap(int value, int begin1, int end1, int begin2, int end2)
{
	return((value - begin1) * ((end2 - begin2)/(end1 - begin1)) + begin2);
}

// returns new point in second plane as mapped from first plane.
public static Point
rTwoToRTwoMap(int valuex, int valuey,
	int minx, int miny, int maxx, int maxy,
	int MinX, int MinY, int MaxX, int MaxY)
{
	return (new Point(
		rOneToROneMap(valuex, minx, maxx, MinX, MaxX),
		rOneToROneMap(valuey, miny, maxy, MinY, MaxY)));
}

public static double
colorToRedNormal(Color color)
{
	return(rOneToROneMap((double)color.getRed(), 0.0, 255.0, 0.0, 1.0));
}

public static double
colorToGreenNormal(Color color)
{
	return(rOneToROneMap((double)(color.getGreen()), 0.0, 255.0, 0.0, 1.0));
}

public static double
colorToBlueNormal(Color color)
{
	return(rOneToROneMap((double)(color.getBlue()), 0.0, 255.0, 0.0, 1.0));
}

/******************* CIRCLE STUFF *************************/

public static double
ptSetDiameter(Vector ptSet)
{
	double largestDiameter = 0.0;

	for (int i = 0;i < ptSet.size();i++)
	{
		Point2D pt = (Point2D)ptSet.elementAt(i);
		for (int j = 0;j < ptSet.size();j++)
		{
			if (i == j)
				continue;
			Point2D cmpPt = (Point2D)ptSet.elementAt(j);
			double diameter = pt.distance(cmpPt);
			if (diameter > largestDiameter)
				largestDiameter = diameter;
		}
	}
	return (largestDiameter);
}

/******************* END CIRCLE STUFF *************************/

public static void
getMoment(Vector ptList, BPoint3d meanPt, BPoint3d variancePt)
{
	if ((meanPt == null) || (variancePt == null))
		return;
	
	double smallestMean = Double.MAX_VALUE;
	double smallestVariance = Double.MAX_VALUE;
	int meanPtID = -1;
	int variancePtID = -1;

	for (int i = 0;i < ptList.size();i++)
	{
		double mean = MathUtil.getMean(i, ptList);
		double variance = MathUtil.getVariance(i, ptList, mean);
		if (mean < smallestMean)
		{
			smallestMean = mean;
			meanPtID = i;
		}
		if(variance < smallestVariance)
		{
			smallestVariance = variance;
			variancePtID = i;
		}
	}

	meanPt.set(new BPoint3d((BVector3d)ptList.elementAt(meanPtID)));
	variancePt.set(new BPoint3d((BVector3d)ptList.elementAt(variancePtID)));
}

public static double
getMean(int ptID, Vector ptList)
{
	BVector3d ptAtID = (BVector3d)ptList.elementAt(ptID);
	BVector3d searchPt = new BVector3d();
	double accum = 0.0;

	for (int i = 0;i < ptList.size();i++)
	{
		if (i == ptID) // don't deal with pt in question
			continue;
		searchPt.set((BVector3d)ptList.elementAt(i));
		accum += ptAtID.distance(searchPt);
	}
	return(accum/(double)(ptList.size() - 1)); // don't count point in question
}

public static double
getVariance(int ptID, Vector ptList, double mean)
{
	BVector3d ptAtID = (BVector3d)ptList.elementAt(ptID);
	BVector3d searchPt = new BVector3d();
	double accum = 0.0;

	for (int i = 0;i < ptList.size();i++)
	{
		if (i == ptID) // don't deal with pt in question
			continue;
		searchPt.set((BVector3d)ptList.elementAt(i));
		double dist = (double)ptAtID.distance(searchPt);
		accum += ((dist - mean) * (dist - mean));
	}
	return(accum/(double)(ptList.size() - 1)); // don't count point in question
}

public static byte[]
convertHexToByte(String hexString)
{
	int		index			= 0;	// index for string iteration.
	int		hexLength		= 0;	// length of hex string.
	int		byteArrayLength = 0;	// length of corresponding byte array.
	byte[]	byteArray		= null;	// returned byte array.
	char[]	charArray		= null;	// char array equivalent of hex string.
	char	theHexChar		= '0';	// a particular hex char from string.

	// save the hex string length.
	hexLength = hexString.length();

	/*
	** First need to check if hexLength is odd length since must left
	** pad with a '0' if hexString is so.
	*/

	if (hexLength % 2 == 0)
	{
		charArray = hexString.toCharArray();
	}
	else
	{
		hexLength++;
		charArray = ("0" + hexString).toCharArray();
	}

	/*
	** Need to assert that hexString contains only chars from
	** 'a'->'f', 'A'->'F', '0'->'9'; What to do if not?
	*/

	for (index = 0; index < hexLength; index++)
	{
		theHexChar = charArray[index];
		if ((theHexChar >= 'a' && theHexChar <= 'f') ||
			(theHexChar >= 'A' && theHexChar <= 'F') ||
			(theHexChar >= '0' && theHexChar <= '9'))
			// then valid hex char.
			continue;

		// return null byte array for now until come up with exception.
		return (null);
	}

	// valid hexstring so now form byte array equivalent.

	/*
	** Get the bytearray length == hexLength/2 . First need to check
	** if odd length since must left pad with a '0' if hexString is
	** odd length.
	*/

	byteArrayLength = hexLength/2;
	byteArray = new byte[byteArrayLength];
	for (index = 0; index < byteArrayLength; index++)
	{
		// first convert the high order nibbles hex digit.
		byteArray[index] = (byte)
			(((toByte(charArray[index * 2]) << 4 ) & 255));

		// now convert the low order nibble hex digit.
		byteArray[index] += (byte)
			(toByte(charArray[index * 2 + 1]));
	}

	return (byteArray);
}

public static byte
toByte(char inByte)
{
	byte theByte = 0; // the byte to be returned.

	if (inByte >= '0' && inByte <= '9')
		theByte = (byte) (inByte - '0' );
	else if (inByte >= 'A' && inByte <= 'F')
		theByte = (byte) (inByte - 'A' + 10);
	else if (inByte >= 'a' && inByte <= 'f')
		theByte = (byte) (inByte - 'a' + 10);
	return (theByte);
}

private static void
debug(String s)
{
	System.out.println("MathUtil-> " + s);
}

}
