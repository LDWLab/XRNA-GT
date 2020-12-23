
package jmath;

import java.awt.*;
import java.lang.Math;
import java.util.*;

public class
MathOps
implements MathDefines
{
	/* GENERAL MATH */

	public static boolean
	precisionEquality(double val0, double val1)
	{
		return (Math.round(MathDefines.PRECISION * val0) ==
			Math.round(MathDefines.PRECISION * val1));
	}

	public static boolean
	isDecimal(char c)
	{
		return ((Character.isDigit(c)) || (c == '-') || (c == '.'));
	}

	public static char
	getCtrlCharOf(char x)
	{
		return ((char)(x - 'A' + 1));
	}

	/*
	** Converts an angle measured in degrees to the equivalent angle
	** measured in radians.
	**
	** Note, this method is over 3 times faster than
	** java.lang.Math.toRadians() which does the same thing.  This is
	** because javas version always does an unnecessary extra division.
	*/

	public static double
	angleToRadian(double angle)
	{
		return (DegToRad * angle);
	}

	/*
	** Converts an angle measured in radians to the equivalent angle
	** measured in degrees.
	**
	** Note, this method is over 3 times faster than
	** java.lang.Math.toRadians() which does the same thing.  This is
	** because javas version always does an unnecessary extra division.
	*/

	public static double
	radianToAngle(double radian)
	{
		return (RadToDeg * radian);
	}

	// Need to get my  own routines for random as Java only supplies
	// random double 0.0 -> 1.0

	public static char
	randomChar()
	{
		/*
		return (rand() % 26 + 'a');
		*/
		return ('0');
	}

	public static double
	randomDouble()
	{
		/*
		return (rand() % 1000 / 10.0);
		*/
		return (0.0);
	}

	/* USE BVector.length()
	public static double
	rayLength(BMatrix fpt, BMatrix spt)
	throws Exception
	{
		if ((fpt == null) || (spt == null))
			throw new Exception("null BMatrix");
		if ((!fpt.isPointMatrix()) || (!spt.isPointMatrix()))
			throw new Exception("not a point matrix");
		double a = fpt.pointCoord(XCoor) - spt.pointCoord(XCoor);
		double b = fpt.pointCoord(YCoor) - spt.pointCoord(YCoor);
		double c = fpt.pointCoord(ZCoor) - spt.pointCoord(ZCoor);

		return(Math.sqrt(a*a + b*b + c*c));
	}
	*/

	/*
	** GetRayTValue() returns a t value for a vector given distance from
	** tailpt to headpt if t==0.0 is t value at tailpt, and t==1.0 is t
	** value at headpt.
	**
	** float
	** GetRayTValue(tailpt, headpt, distance)
	** point tailpt, headpt;
	** float distance;
	*/

	/* USE BVector.getTValue()
	public static double
	getRayTValue(BMatrix tailpt, BMatrix headpt, double distance)
	throws Exception
	{
		return(distance / rayLength(tailpt, headpt));
	}
	*/

	public static void
	copyPoint(BMatrix fpt, BMatrix spt)
	{
		// maybe speed up
		BMatrix.copyMatrix(fpt, spt);
	}

	/* USE BVector.getMidPoint()
	public static void
	getMidPoint(BMatrix midPt, BMatrix pt0, BMatrix pt1)
	{
        midPt.transMat[0][XCoor] =
            (pt0.transMat[0][XCoor] + pt1.transMat[0][XCoor]) / 2.0;
        midPt.transMat[0][YCoor] =
            (pt0.transMat[0][YCoor] + pt1.transMat[0][YCoor]) / 2.0;
        midPt.transMat[0][ZCoor] =
            (pt0.transMat[0][ZCoor] + pt1.transMat[0][ZCoor]) / 2.0;
	}

	public static BMatrix
	getMidPoint(BMatrix pt0, BMatrix pt1)
	throws Exception
	{
		BMatrix midPt = new BMatrix(4);
        midPt.transMat[0][XCoor] =
            (pt0.transMat[0][XCoor] + pt1.transMat[0][XCoor]) / 2.0;
        midPt.transMat[0][YCoor] =
            (pt0.transMat[0][YCoor] + pt1.transMat[0][YCoor]) / 2.0;
        midPt.transMat[0][ZCoor] =
            (pt0.transMat[0][ZCoor] + pt1.transMat[0][ZCoor]) / 2.0;
		return (midPt);
	}
	*/

	public static double
	rOneToROneMap(double value, double begin1, double end1,
		double begin2, double end2)
	{
		return((value - begin1) * ((end2 - begin2)/(end1 - begin1)) + begin2);
	}

	public static boolean
	pointInRasterRectangle(int x, int y, int topleftx, int toplefty,
		int length, int depth)
	{
		return((x >= topleftx) && (x <= topleftx + length) &&
			(y >= toplefty) && (y <= toplefty + depth));
	}

	public static Point
	polarCoordTo2DPt(double radius, double angle)
	throws Exception
	{
		return (new Point(
			(int)Math.round(polarCoordToX(radius, angle)),
			(int)Math.round(polarCoordToY(radius, angle))
			));
	}

	public static double
	polarCoordToX(double radius, double angle)
	throws Exception
	{
		if (radius <= 0.0)
			throw new Exception("Trying to get polarCoordToX with " +
				"invalid radius (<=0.0): " + radius);
		return (radius * Math.cos((double)angleToRadian(angle)));
	}

	public static double
	polarCoordToY(double radius, double angle)
	throws Exception
	{
		if (radius <= 0.0)
			throw new Exception("Trying to get polarCoordToY with " +
				"invalid radius (<=0.0): " + radius);
		return (radius * Math.sin((double)angleToRadian(angle)));
	}

	/*
	** DistPtToLine() returns the distance from a point, pt, to the line
	** segment defined by tailPt, headPt. It also assigns to the parameter
	** t the value needed to find the point on line that cooresponds to
	** the minimum distance found.
	**
	*/

	/* USE BVector.distPtToLine
	public static double
	distPtToLine(BMatrix tailPt, BMatrix headPt, BMatrix pt, double[] tVal)
	{
		double x = 0.0, y = 0.0, z = 0.0, x1 = 0.0, y1 = 0.0, z1 = 0.0;
		double x0 = 0.0, y0 = 0.0, z0 = 0.0, a = 0.0, b = 0.0, c = 0.0;
		double x2 = 0.0, y2 = 0.0, z2 = 0.0;
		double t = 0.0;
		double headPtMatPtr[] = headPt.transMat[0];
		double tailPtMatPtr[] = tailPt.transMat[0];
		double ptMatPtr[] = pt.transMat[0];

		//
		// x0 = pt[XCoor];
		// y0 = pt[YCoor];
		// z0 = pt[ZCoor];
		// x1 = tailPt[XCoor];
		// y1 = tailPt[YCoor];
		// z1 = tailPt[ZCoor];
		// x2 = headPt[XCoor];
		// y2 = headPt[YCoor];
		// z2 = headPt[ZCoor];
		//
		
		x0 = ptMatPtr[XCoor];
		y0 = ptMatPtr[YCoor];
		z0 = ptMatPtr[ZCoor];
		x1 = tailPtMatPtr[XCoor];
		y1 = tailPtMatPtr[YCoor];
		z1 = tailPtMatPtr[ZCoor];
		x2 = headPtMatPtr[XCoor];
		y2 = headPtMatPtr[YCoor];
		z2 = headPtMatPtr[ZCoor];
		
		a = x2 - x1;
		b = y2 - y1;
		c = z2 - z1;

		//
		// need to deal with returning val of *t
		// *t = - (a*(x1 - x0) + b*(y1 - y0) + c*(z1 - z0))/ (a*a + b*b + c*c);
		// x = x1 + a*(*t);
		// y = y1 + b*(*t);
		// z = z1 + c*(*t);
		//

		t = - (a*(x1 - x0) + b*(y1 - y0) + c*(z1 - z0))/ (a*a + b*b + c*c);
		tVal[0] = t;
		x = x1 + a*t;
		y = y1 + b*t;
		z = z1 + c*t;

		return
		(
			Math.sqrt
			(
				(x - x0)*(x - x0) + (y - y0)*(y - y0) + (z - z0)*(z - z0)
			)
		);
	}
	*/

	/*
	** Sets a new point, on the line given by the vector
	** defined by tailPt and headPt, a multiple of t times away from tail.
	*/

	/* USE BVector.getPointAtT
	public static void
	getVectorLineAtT(BMatrix tailPt, BMatrix headPt, BMatrix newPt, double t)
	throws Exception
	{
		if ((tailPt == null) || (headPt == null) || (newPt == null))
			throw new Exception("BMatrix is null");
		double tailPtMatPtr[] = tailPt.getPointRow();
		double headPtMatPtr[] = headPt.getPointRow();
		double newPtMatPtr[] = newPt.getPointRow();

		newPt.setPoint(
			tailPtMatPtr[XCoor] +
				(t * (headPtMatPtr[XCoor] - tailPtMatPtr[XCoor])),
			tailPtMatPtr[YCoor] +
				(t * (headPtMatPtr[YCoor] - tailPtMatPtr[YCoor])),
			tailPtMatPtr[ZCoor] +
				(t * (headPtMatPtr[ZCoor] - tailPtMatPtr[ZCoor])));
		newPtMatPtr[3] = 1.0;
	}

	public static BMatrix
	getVectorLineAtT(BMatrix tailPt, BMatrix headPt, double t)
	throws Exception
	{
		BMatrix newPt = BMatrix.point(); 
		getVectorLineAtT(tailPt, headPt, newPt, t);
		return(newPt);
	}
	*/

	public static int
	signOfInt(int number)
	{
		if(number < 0)
				return(-1);
		if(number == 0)
				return(0);
		return(1);
	}

	/**** GCD MATH *****/

	public static long
	getGCD(long m, long n)
	{
		if (m > n) // then swap them
		{
			long hold = m;
			m = n;
			n = hold;
		}
		while (m > 0)
		{
			long t = n % m;
			n = m;
			m = t;
		}
		return n;
	}

	public static boolean
	isRelativelyPrime(long m, long n)
	{
		return (getGCD(m, n) == 1);
	}

	/*
	** getRandomRelativePrime returns a random number less than n
	** and relatively prime to n.
	*/

	public static long
	getRandomRelativePrime(long lowerBound, long upperBound, long n)
	{
        Random msRand      = null; // the random number generator.
		long m = 0;

        // set random number generator with seed.
        msRand = new Random(getSeed());

		while (true)
		{
			m = Math.abs(msRand.nextLong()) % upperBound;
			if ((m > lowerBound) && (m < upperBound) &&
				(isRelativelyPrime(m, n)))
				return (m);
		}
		//return (0);
	}

	public static boolean
	isPrime(long m)
	{
		// assert int m is a natural number
		if (m < 1)
			return (false);

		if (m == 1)
			return (false);

		if (m == 2)
			return (true);

		for(long divisor = m/2;divisor > 2;divisor--)
			if (m % divisor == 0)
				return (false);

		return (true);
	}

	public static long
	genPrime(double lowerLimit)
	{
		double y;

        if ((lowerLimit < 0) || (Math.floor(lowerLimit) != lowerLimit))
        {
            return (0);
        }
      
        if (lowerLimit <= 2.0)
		{
			return (0);
		}
      
        while (true)
        {
            lowerLimit += 1.0;
            y = Math.floor(Math.sqrt(lowerLimit));
            while (true)
            {
                if (((lowerLimit/y) - (Math.floor(lowerLimit/y))) != 0.0)
                {
                    y -= 1.0;        
                    if (y < 2.0)
                        return (Math.round(lowerLimit));
                }
				else
				{
					break;
				}
            }
        }
	}

	public static long
	genRandomPrime(long lowerLimit, long upperLimit)
	{
        Random msRand      = null; // the random number generator.
		long nextVal = 0;

        // set random number generator with seed.
        msRand = new Random(getSeed());

		while (nextVal < Math.round(lowerLimit))
			nextVal = Math.abs(msRand.nextLong()) % upperLimit;

		return (genPrime((double)nextVal));
	}

	public static long
	getSeed()
	{
        return ((new Date()).getTime());
	}


	/*********** BRESENHAM ROUTINES ************/

	public static boolean
	inBresLine(int x, int y, Vector bresLine)
	{
		int count = bresLine.size();
		for(int i = 0;i < count;i++)
		{
			Point tmppt = (Point)(bresLine.elementAt(i));
			if((tmppt.x == x) && (tmppt.y == y))
				return(true);
		}
		return(false);
	}

	public static void
	getBresenhamLine(int x1, int y1, int x2, int y2, Vector result)
	{
		int x = 0, y = 0, deltax = 0, deltay = 0, s1 = 0, s2 = 0,
			tmp = 0, error = 0, i = 0;
		boolean interchanged = false;

		if(!result.isEmpty())
			result.removeAllElements();
		if((x1 == x2) && (y1 == y2))
		{
			result.addElement(new Point(x1, y1));
			return;
		}
		x = x1;
		y = y1;
		deltax = Math.abs(x2 - x1);
		deltay = Math.abs(y2 - y1);
		s1 = signOfInt(x2 - x1);
		s2 = signOfInt(y2 - y1);
		if(deltay > deltax)
		{
			tmp = deltax;
			deltax = deltay;
			deltay = tmp;
			interchanged = true;
		}
		else
		{
			interchanged = false;
		}
		error = (2 * deltay) - deltax;
		for(i = 0;i <= deltax;i++)
		{
			result.addElement(new Point(x, y));
			while(error >= 0)
			{
				if(interchanged)
					x += s1;
				else
					y += s2;
				error -= 2 * deltax;
			}
			if(interchanged)
				y += s2;
			else
				x += s1;
			error += 2 * deltay;
		}
		return;
	}

	public static int
	getBresenhamCircle(int centerX, int centerY, int radius,
		int quarterIndex[], Vector result)
	{
		int bresX = radius, bresY = 0, delta = 2*(1-radius), limit = 0;
		int resultIndex = 0;
		Point pt = null;
	 
		if (result == null)
			return (-1);
		if(!result.isEmpty())
			result.removeAllElements();
		while(true)
		{
			result.addElement(new Point(bresX, bresY));
			resultIndex++;
			if(bresX <= limit)
			{
				/* first fill in other quadrants before returning */
				int current = resultIndex;
				quarterIndex[0] = resultIndex - 1;
				for(int i = current - 2;i >= 0;i--)
				{
					pt = (Point)result.elementAt(i);
					result.addElement(new Point(-pt.x, pt.y));
					resultIndex++;
				}
				current = resultIndex;
				for(int i = current - 2;i > 0;i--)
				{
					pt = (Point)result.elementAt(i);
					result.addElement(new Point(pt.x, -pt.y));

					resultIndex++;
				}
				pt = (Point)result.elementAt(0);
				result.addElement(new Point(pt.x, pt.y));

				resultIndex++;
				for(int i = 0;i < resultIndex;i++)
				{
					pt = (Point)result.elementAt(i);
					result.setElementAt(new Point(pt.x + centerX,
						pt.y + centerY), i);
				}
				return(resultIndex);
			}
			if(delta < 0)
			{
				if((2*delta + 2*bresX - 1) <= 0)
				{
					bresY++;
					delta += 2*bresY + 1;
				}
				else
				{
					bresY++;
					bresX--;
					delta += 2*bresY - 2*bresX + 2;
				}
			}
			else if(delta > 0)
			{
				if((2*delta - 2*bresY - 1) <= 0)
				{
					bresY++;
					bresX--;
					delta += 2*bresY - 2*bresX + 2;
				}
				else
				{
					bresX--;
					delta += -2*bresX + 1;
				}
			}
			else
			{
				bresY++;
				bresX--;
				delta += 2*bresY - 2*bresX + 2;
			}
		}
	}

	/* a different bres algorithm */
	/*
	void drawCircle(int xc, int yc, int r) 
	{ 
	int x = 0; 
	int y = r; 
	int p = 3 - 2 * r; 
	   while (x <= y) 
	   { 
		  putPixel(xc + x, yc + y); 
		  putPixel(xc - x, yc + y); 
		  putPixel(xc + x, yc - y); 
		  putPixel(xc - x, yc - y); 
		  putPixel(xc + y, yc + x); 
		  putPixel(xc - y, yc + x); 
		  putPixel(xc + y, yc - x); 
		  putPixel(xc - y, yc - x); 
		  if (p < 0) 
			 p += 4 * x++ + 6; 
		  else 
			 p += 4 * (x++ - y--) + 10; 
	   } 
	} 
	*/



	public static int
	org0GetBresenhamCircle(int centerx, int centery, int radius,
		int quarterindex[], Vector result)
	{
		int X;
		int Y;
		int delta;
		int limit;
		int resultindex;
		int i;
		int current;
		Point pt;
	 
		if (result == null)
			return (-1);
		if(!result.isEmpty())
			result.removeAllElements();
		Y = 0;
		X = radius;
		delta = 2 * (1 - radius);
		limit = 0;
		resultindex = 0;
		while(true)
		{
			result.addElement(new Point(X, Y));
			resultindex++;
			if(X <= limit)
			{
				/* first fill in other quadrants before returning */
				current = resultindex;
				quarterindex[0] = resultindex - 1;
				for(i = current - 2;i >= 0;i--)
				{
					pt = (Point)result.elementAt(i);
					result.addElement(new Point(-pt.x, pt.y));
					resultindex++;
				}
				current = resultindex;
				for(i = current - 2;i > 0;i--)
				{
					pt = (Point)result.elementAt(i);
					result.addElement(new Point(pt.x, -pt.y));

					resultindex++;
				}
				pt = (Point)result.elementAt(0);
				result.addElement(new Point(pt.x, pt.y));

				resultindex++;
				for(i = 0;i < resultindex;i++)
				{
					pt = (Point)result.elementAt(i);
					result.setElementAt(new Point(pt.x + centerx,
						pt.y + centery), i);
				}
				return(resultindex);
			}
			if(delta < 0)
			{
				if((2*delta + 2*X - 1) <= 0)
				{
					Y++;
					delta += 2*Y + 1;
				}
				else
				{
					Y++;
					X--;
					delta += 2*Y - 2*X + 2;
				}
			}
			else if(delta > 0)
			{
				if((2*delta - 2*Y - 1) <= 0)
				{
					Y++;
					X--;
					delta += 2*Y - 2*X + 2;
				}
				else
				{
					X--;
					delta += -2*X + 1;
				}
			}
			else
			{
				Y++;
				X--;
				delta += 2*Y - 2*X + 2;
			}
		}
	}


	/*********** END BRESENHAM ROUTINES ************/

	public static void
	testStack()
	{
		JavaStack tmpStack = new JavaStack();

		int[] testObj0 = new int[3];
		testObj0[0] = 0;
		testObj0[1] = 1;
		testObj0[2] = 2;
		tmpStack.push((Object)(testObj0));

		int[] testObj1 = new int[3];
		testObj1[0] = 10;
		testObj1[1] = 11;
		testObj1[2] = 12;
		tmpStack.push((Object)(testObj1));

		int[] testObj2 = new int[3];
		testObj2[0] = 20;
		testObj2[1] = 21;
		testObj2[2] = 22;
		tmpStack.push((Object)(testObj2));


		int[] getintptr = (int[])tmpStack.top();
		//System.out.println(getintptr[0] + " " + getintptr[1] + " " + getintptr[2]);

		tmpStack.pop();
		getintptr = (int[])tmpStack.top();
		//System.out.println(getintptr[0] + " " + getintptr[1] + " " + getintptr[2]);

		tmpStack.pop();
		getintptr = (int[])tmpStack.top();
		//System.out.println(getintptr[0] + " " + getintptr[1] + " " + getintptr[2]);

	}

	static final int PIXELROW = 0;
	static final int PIXELCOL = 1;

    static final byte PIXNOTVISITEDTYPE = 0;
    static final byte PIXVISITEDTYPE = 1;
    static final byte PIXBOUNDARYTYPE = 2;
    static final byte PIXFILLTYPE = 3;

	private static boolean
	outOfImageRange(int x, int y, int imageWidth, int imageHeight)
	{
		return((x < 0) || (x > imageWidth) || (y < 0) || (y > imageHeight));
	}

	private static boolean
	inImageRange(int x, int y, int imageWidth, int imageHeight)
	{
		return((x >= 0) && (x < imageWidth) &&
			(y >= 0) && (y <= imageHeight));
	}

	private static boolean
	pixelInImageRange(int[] imagepixel, int imageWidth, int imageHeight)
	{
		return(inImageRange(imagepixel[PIXELCOL],
			imagepixel[PIXELROW], imageWidth, imageHeight));
	}

	private static void
	setSpanPixelType(int[] spanPixel, byte[][] visitType, byte type)
	{
		visitType[spanPixel[PIXELROW]][spanPixel[PIXELCOL]] = type; 
		/*
		System.out.println(spanPixel[PIXELROW] + " " +
			spanPixel[PIXELCOL] + " " + type);
		*/
	}

	private static byte
	getSpanPixelType(int[] spanPixel, byte[][] visitType)
	{
		return(visitType[spanPixel[PIXELROW]][spanPixel[PIXELCOL]]);
		/*
		return(sourceData[spanPixel[PIXELCOL] +
			(spanPixel[PIXELROW] * imageWidth)]);
		*/
	}

	static int[] spanPixel = null;

	public static void
	seedStackFill(int x, int y, int[] sourceData,
		byte[][] visitType, int imageWidth, int imageHeight)
	{
		int SaveX = 0, SaveY = 0, XRight = 0, XLeft = 0,
			XEnter = 0, trackx = 0, tracky = 0;
		boolean PFlag, ClearSpan;
	 	byte testval = 0;
		JavaStack fillPixelStack = new JavaStack();

		if(outOfImageRange(x, y, imageWidth, imageHeight))
			return; 
		if(spanPixel == null)
			spanPixel = new int[3];
		spanPixel[PIXELROW] = y;
		spanPixel[PIXELCOL] = x; //LAST!
		/*
		pushStack(spanPixel, fillPixelStack); // Push Seed
		*/
		fillPixelStack.push((Object)(spanPixel));

		/*
		while(!isEmptyStack(fillPixelStack))
		*/
		while(!fillPixelStack.isEmpty())
		{
			// get the seed pixel and set it to the new value
			/*
			popStack(spanPixel, fillPixelStack);
			*/
			spanPixel = (int[])fillPixelStack.pop();

			/*
			setSpanPixel(spanPixel, r_newPixel);
			*/
			setSpanPixelType(spanPixel, visitType, PIXFILLTYPE);

			// save the xcoord of the seed pixel
			x = SaveX = spanPixel[PIXELCOL];

			// fill the span to the right of the seed pixel
			x++;
			spanPixel[PIXELCOL] = x;
			while(pixelInImageRange(spanPixel, imageWidth, imageHeight) &&
				(getSpanPixelType(spanPixel, visitType) == PIXNOTVISITEDTYPE))
			{
				/*
				setSpanPixel(spanPixel, r_newPixel);
				*/
				setSpanPixelType(spanPixel, visitType, PIXFILLTYPE);
				x++;
				spanPixel[PIXELCOL] = x;
			}

			// save the extreme right pixel
			XRight = x - 1;
			// reset the x coord to the value for the seed pixel
			x = SaveX;
			// fill the span to the left of the seed pixel
			x--;
			spanPixel[PIXELCOL] = x;
			while(pixelInImageRange(spanPixel, imageWidth, imageHeight) &&
				(getSpanPixelType(spanPixel, visitType) == PIXNOTVISITEDTYPE))
			{
				/*
				setSpanPixel(spanPixel, r_newPixel);
				*/
				setSpanPixelType(spanPixel, visitType, PIXFILLTYPE);

				x--;
				spanPixel[PIXELCOL] = x;
			}
			// save the extreme left pixel
			XLeft = x + 1;
			// reset the x coordinate to the value for the seed pixel
			x = SaveX;
			y = SaveY = spanPixel[PIXELROW];

			// check that the scan line above is neither a polygon
			// boundary nor has been previously completely filled;
			// if not, seed the scan line. Start at the left edge of
			// the scan line subspan

			ClearSpan = true;
			if(y > 0)
			{
				spanPixel[PIXELROW]--;
				tracky = spanPixel[PIXELROW] /* * imageWidth */;
				for(trackx = XLeft;trackx <= XRight;trackx++)
				{
					if(!inImageRange(trackx, y, imageWidth, imageHeight))
						continue;
					//if(r_track[tracky + trackx])
					if(visitType[tracky][trackx] == PIXVISITEDTYPE)
						continue;
					ClearSpan = false;
					break;
				}
			}
			if(!ClearSpan)
			{
				x = XLeft;
				y--;
				spanPixel[PIXELCOL] = x;
				spanPixel[PIXELROW] = y;
				tracky = spanPixel[PIXELROW]/* * imageWidth*/;
				while(x <= XRight)
				{
					// seed the scan line above
					PFlag = false;
					testval = getSpanPixelType(spanPixel, visitType);
					if((x == XRight) &&
						(testval == PIXNOTVISITEDTYPE) &&
						//(!r_track[tracky + x]))
						(visitType[tracky][x] != PIXVISITEDTYPE))
					{
						PFlag = true;
					}
					else
					{
					while(((testval == PIXNOTVISITEDTYPE) ||
						//r_track[tracky + x]) &&
						(visitType[tracky][x] == PIXVISITEDTYPE)) &&
						(x < XRight))
					{
						if(!PFlag)
							PFlag = true;
						x++;
						spanPixel[PIXELCOL] = x;
						testval = getSpanPixelType(spanPixel, visitType);
					}
					}

					// push extreme right pixel onto stack
					if(PFlag)
					{
						if((x == XRight) &&
							(testval == PIXNOTVISITEDTYPE) &&
							//(!r_track[tracky + x]))
							(visitType[tracky][x] != PIXVISITEDTYPE))
						{
							/*
							pushStack(spanPixel,
								fillPixelStack);
							*/
							fillPixelStack.push((Object)(spanPixel));
						}
						else
						{
							spanPixel[PIXELCOL] = x - 1;
							/*
							pushStack(spanPixel,
								fillPixelStack);
							*/
							fillPixelStack.push((Object)(spanPixel));
						}
						PFlag = false;
					}

					// continue checking in case the span is
					// interrupted
					XEnter = x;
					spanPixel[PIXELCOL] = x;
					testval = getSpanPixelType(spanPixel, visitType);
					while((pixelInImageRange(spanPixel, imageWidth, imageHeight)) && ((testval != PIXNOTVISITEDTYPE) ||
						//(r_track[tracky + x])) &&
						(visitType[tracky][x] == PIXVISITEDTYPE)) &&
						(x < XRight))
					{
						x++;
						spanPixel[PIXELCOL] = x;
						testval = getSpanPixelType(spanPixel, visitType);
					}

					// make sure that the pixel coordinate
					// is incremented
					if(x == XEnter)
						x++;

				}
			}

			x = SaveX;
			y = SaveY;

			// check that the scan line below is neither a polygon
			// boundary nor has been previously completely filled;
			// if not, seed the scan line. Start at the left edge of
			// the scan line subspan

			ClearSpan = true;
			if(y < imageHeight - 1)
			{
				spanPixel[PIXELROW] = y + 1;
				tracky = spanPixel[PIXELROW] /* * imageWidth*/;
				for(trackx = XLeft;trackx <= XRight;trackx++)
				{
					if(!inImageRange(trackx, y, imageWidth, imageHeight))
						continue;
					//if(r_track[tracky + trackx])
					if(visitType[tracky][trackx] == PIXVISITEDTYPE)
						continue;
					ClearSpan = false;
					break;
				}
			}
			if(!ClearSpan)
			{
				x = XLeft;
				y++;
				spanPixel[PIXELCOL] = x;
				spanPixel[PIXELROW] = y;
				tracky = spanPixel[PIXELROW] /* * imageWidth */;
				while(x <= XRight)
				{
					// seed the scan line below
					PFlag = false;
					testval = getSpanPixelType(spanPixel, visitType);
					if((x == XRight) &&
						(testval == PIXNOTVISITEDTYPE) &&
						//(!r_track[tracky + x]))
						(visitType[tracky][x] != PIXVISITEDTYPE))
					{
						PFlag = true;
					}
					else
					{
					while(((testval == PIXNOTVISITEDTYPE) ||
						//r_track[tracky + x]) &&
						(visitType[tracky][x] == PIXVISITEDTYPE)) &&
						(x < XRight))
					{
						if(!PFlag)
							PFlag = true;
						x++;
						spanPixel[PIXELCOL] = x;
						testval = getSpanPixelType(spanPixel, visitType);
					}
					}
					// push extreme right pixel onto stack
					if(PFlag)
					{
						if((x == XRight) &&
							(testval == PIXNOTVISITEDTYPE)&&
							//(!r_track[tracky + x]))
							(visitType[tracky][x] != PIXVISITEDTYPE))
						{
							/*
							pushStack(spanPixel,
								fillPixelStack);
							*/
							fillPixelStack.push((Object)(spanPixel));
						}
						else
						{
							spanPixel[PIXELCOL] = x - 1;
							/*
							pushStack(spanPixel,
								fillPixelStack);
							*/
							fillPixelStack.push((Object)(spanPixel));
						}
						PFlag = false;
					}

					// continue checking in case the span is
					// interrupted
					XEnter = x;
					spanPixel[PIXELCOL] = x;
					testval = getSpanPixelType(spanPixel, visitType);
					while((pixelInImageRange(spanPixel, imageWidth, imageHeight)) &&
						((testval != PIXNOTVISITEDTYPE) ||
						//(r_track[tracky + x])) &&
						(visitType[tracky][x] == PIXVISITEDTYPE)) &&
						(x < XRight))
					{
						x++;
						spanPixel[PIXELCOL] = x;
						testval = getSpanPixelType(spanPixel, visitType);
					}

					// make sure that the pixel coordinate
					// is incremented
					if(x == XEnter)
						x++;

				}
			}
		}
	}
	/** END STACK STUFF **/

	/** HEX STUFF **/

	static public int
    hexStringToInt(String hexStr)
    {
		System.out.println("hexstr, length: " + hexStr + " " + hexStr.length());
        byte[] hexBytes = convertHexToByte(hexStr);
		System.out.println("hexBytes.length: " + hexBytes.length);

        return ((0xff000000 & ((hexBytes[0] & 0xff) << 32)) +
            ((hexBytes[1] & 0xff) << 16) + ((hexBytes[2] & 0xff) << 8) +
                ((hexBytes[3] & 0xff) << 0));
    }

    /*
    ** convertByteToHex() takes a byte array and returns
    ** a String of hex characters which is double in size of
    ** byte array length.
    */

    static public String
    convertByteToHex(byte[] byteArray)
    {
        int     index       = 0;    // the array iterator
        char    stringBuf[] = null; // the char array to convert
        int     length      = byteArray.length; // length of passed array
 
        stringBuf = new char[length * 2];
 
        // convert the whole array.
        for (index = 0; index < length; index++)
        {
            byte tmpByte = (byte)((byteArray[index] >> 0x04) & 0x0f);
            // convert the high nibble
            stringBuf[index * 2] = ((tmpByte >= 0) && (tmpByte <= 9)) ?
                ((char) (tmpByte + '0')) : ((char) (tmpByte - 10 + 'A'));
 
            tmpByte = (byte)(byteArray[index] & 0x0f);
            // now mask and convert the low nibble
            stringBuf[index * 2 + 1] = ((tmpByte >= 0) && (tmpByte <= 9)) ?
                ((char) (tmpByte + '0')) : ((char) (tmpByte - 10 + 'A'));
        }
 
        // return the string converted from the byte array.
        return (new String(stringBuf));
    }


    /*
    ** convertHexToByte() takes a string of hex characters and
    ** returns a byte array 1/2 the size of the String arg.
    */

    static public byte[]
    convertHexToByte(String hexString)
    {
        int     index           = 0;    // index for string iteration.
        int     hexLength       = 0;    // length of hex string.
        int     byteArrayLength = 0;    // length of corresponding byte array.
        byte[]  byteArray       = null; // returned byte array.
        char[]  charArray       = null; // char array equivalent of hex string.
        char    hexChar      = '0';  // a particular hex char from string.

        // save the hex string length.
System.out.println("before, hexString, length: " + hexString + " " +
	hexString.length());
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
			if (hexLength == 2)
			{
				hexLength += 6;
				charArray = ("0000000" + hexString).toCharArray();
			}
			else
			{
				charArray = ("0" + hexString).toCharArray();
			}
        }


System.out.println("charArray, hexLength: " + charArray + " " + hexLength);
        /*
        ** Need to assert that hexString contains only chars from
        ** 'a'->'f', 'A'->'F', '0'->'9'; What to do if not?
        */

        for (index = 0; index < hexLength; index++)
        {
            hexChar = charArray[index];
            if ((hexChar >= 'a' && hexChar <= 'f') ||
                (hexChar >= 'A' && hexChar <= 'F') ||
                (hexChar >= '0' && hexChar <= '9'))
                // then valid hex char.
                continue;

            // return null byte array for now until come up with exception.
			System.out.println("OFFENDING CHAR: " + hexChar);
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

    /*
    ** toByte() converts a hexadecimal digit to an integer value
    **
    ** inDecimal is the digit to convert where 0 <= inDecimal < 16
    */

    static public byte
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

	/** END HEX STUFF **/

	public static double
	angleInXYPlane(double xPos, double yPos)
	{
		double testAngle;
		int quadrant;

		quadrant = findQuadrantInXYPlane(xPos, yPos);
		switch(quadrant)
		{
		  case MathDefines.inQuadrant00 :
			return(0.0);
		  case MathDefines.inQuadrant15 :
			return(90.0);
		  case MathDefines.inQuadrant25 :
			return(180.0);
		  case MathDefines.inQuadrant35 :
			return(270.0);
		  default :
			break;
		}
		testAngle = radianToAngle(Math.atan(yPos/xPos));
		switch (quadrant)
		{
		  case MathDefines.inQuadrant10 :
			return(testAngle);
		  case MathDefines.inQuadrant20 :
		  case MathDefines.inQuadrant30 :
			return(testAngle + 180.0);
		  case MathDefines.inQuadrant40 :
			return(testAngle + 360.0);
		  default :
			return(0.0);
		}
	}

	/*
	** findQuadrantInXYPlane() returns the quadrant that given point is in, in
	** two-space. The quadrants are defined as follows:
	** given a point x,y , then
	**
	**	if x >= 0 && y == 0 then point is in quadrant 00
	**	if x > 0 && y > 0 then point is in quadrant 10
	**	if x == 0 && y > 0 then point is in quadrant 15
	**	if x < 0 && y > 0 then point is in quadrant 20
	**	if x < 0 && y == 0 then point is in quadrant 25
	**	if x < 0 && y < 0 then point is in quadrant 30
	**	if x == 0 && y < 0 then point is in quadrant 35
	**	if x > 0 && y < 0 then point is in quadrant 40
	**
	**
	**
	**			 15
	**
	**			  |
	**			  |
	**		 20	  |    10
	**			  |
	**			  |
	**			  |
	**	 25   ------------------------	00
	**			  |
	**			  |
	**			  |
	**		 30	  |    40
	**			  |
	**			  |
	**
	**			 35
	**
	**
	** int
	** findQuadrantInXYPlane(VectorPoint, Plane)
	** point VectorPoint;
	** enum plane Plane;
	*/

	public static int
	findQuadrantInXYPlane(double xPos, double yPos)
	{
		return(getQuadrant(xPos, yPos));
	}

	public static int
	getQuadrant(double firstCoor, double secondCoor)
	{
		int quadrant = -1;

		if ((firstCoor >= 0.0) && (secondCoor == 0.0))
			quadrant = MathDefines.inQuadrant00;
		else
		if ((firstCoor > 0.0) && (secondCoor > 0.0))
			quadrant = MathDefines.inQuadrant10;
		else
		if ((firstCoor == 0.0) && (secondCoor > 0.0))
			quadrant = MathDefines.inQuadrant15;
		else
		if ((firstCoor < 0.0) && (secondCoor > 0.0))
			quadrant = MathDefines.inQuadrant20;
		else
		if ((firstCoor < 0.0) && (secondCoor == 0.0))
			quadrant = MathDefines.inQuadrant25;
		else
		if ((firstCoor < 0.0) && (secondCoor < 0.0))
			quadrant = MathDefines.inQuadrant30;
		else
		if ((firstCoor == 0.0) && (secondCoor < 0.0))
			quadrant = MathDefines.inQuadrant35;
		else
		if ((firstCoor > 0.0) && (secondCoor < 0.0))
			quadrant = MathDefines.inQuadrant40;

		return(quadrant);
	}

	/*
	public static double
	getRayPlaneIntersectTValue(BMatrix tailPt, BMatrix headPt,
		double A, double B, double C, double D)
	throws Exception
	{
        double tmp = A*(headPt.getPointX() - tailPt.getPointX()) +
			B*(headPt.getPointY() - tailPt.getPointY()) +
			C*(headPt.getPointZ() - tailPt.getPointZ());
        if(tmp == 0.0)
			return (-1.0);
        else
			return ((-A*tailPt.getPointX() - B*tailPt.getPointY() -
				C*tailPt.getPointZ() - D)/tmp);
	}
	*/

	// PUT INTO BPlaneSrf
	public static void
	vectorCrossProduct(BMatrix u, BMatrix v, BMatrix crossProduct)
	throws Exception
	{
		if (u == null)
			throw new Exception("u is null in MathOps.vectorCrossProduct()");
		if (v == null)
			throw new Exception("v is null in MathOps.vectorCrossProduct()");
		if (crossProduct == null)
			throw new Exception("crossProduct is null " +
				"in MathOps.vectorCrossProduct()");
		double uX = u.getPointX();
		double uY = u.getPointY();
		double uZ = u.getPointZ();
		double vX = v.getPointX();
		double vY = v.getPointY();
		double vZ = v.getPointZ();
		crossProduct.setPoint((uY * vZ) - (vY * uZ), -(uX * vZ) + (vX * uZ),
			(uX * vY) - (vX * uY));
	}

	// PUT INTO BPlaneSrf
	public static BMatrix
	vectorCrossProduct(BMatrix u, BMatrix v)
	throws Exception
	{
		BMatrix crossProduct = BMatrix.point();
		double uX = u.getPointX();
		double uY = u.getPointY();
		double uZ = u.getPointZ();
		double vX = v.getPointX();
		double vY = v.getPointY();
		double vZ = v.getPointZ();
		crossProduct.setPoint(
			(uY * vZ) - (vY * uZ),
			-(uX * vZ) + (vX * uZ),
			(uX * vY) - (vX * uY));
		return (crossProduct);
	}

	/*
	** I think vectorNorm returns a length of a position vector
	** as opposed to rayLength which appears to first translate
	** a ray to the origin.
	*/

	/* USE BVector.normal
	static public double
	vectorNorm(BMatrix positionVector)
	throws Exception
	{
		if (positionVector == null)
			throw new Exception("positionVector == null in: " +
				"MathOps.vectorNorm()");
		if (!positionVector.isPointMatrix())
			throw new Exception("positionVector not a point in: " +
				"MathOps.vectorNorm()");
		double posX = positionVector.getPointX();
		double posY = positionVector.getPointY();
		double posZ = positionVector.getPointZ();
		return(Math.sqrt((posX*posX) + (posY*posY) + (posZ*posZ)));
	}
	*/

	/* USE BVector.getUnitVector
	static public void
	setToUnitVector(BMatrix positionVector)
	throws Exception
	{
		if (positionVector == null)
			throw new Exception("positionVector == null in: " +
				"MathOps.unitVector()");
		double norm = vectorNorm(positionVector);
		positionVector.setPoint(
			positionVector.getPointX()/norm,
			positionVector.getPointY()/norm,
			positionVector.getPointZ()/norm);
	}

	static public BMatrix
	getUnitVector(BMatrix positionVector)
	throws Exception
	{
		if (positionVector == null)
			throw new Exception("positionVector == null in: " +
				"MathOps.unitVector()");
		BMatrix unitVec = BMatrix.point();
		BMatrix.copyMatrix(unitVec, positionVector);
		setToUnitVector(unitVec);
		return (unitVec);
	}
	*/

	// PUT INTO BPlaneSrf
	static public double
	vectorDotProduct(BMatrix u, BMatrix v)
	{
		return((u.getPointX() * v.getPointX()) +
			(u.getPointY() * v.getPointY()) +
			(u.getPointZ() * v.getPointZ()));
	}

	/*
	** Shell sort taken directly from Kernighan's C-manual.
	**
	*/

	static public void
	shellSort(int[] v)
	{
		int n = v.length;
		int gap, i, j, temp;

		for (gap = n/2; gap > 0; gap /= 2)
		{
			for (i = gap; i < n; i++)
			{
				for (j=i-gap;((j>=0) && (v[j]>v[j+gap]));j-=gap)
				{
					temp = v[j];
					v[j] = v[j+gap];
					v[j+gap] = temp;
				}
			}
		}
	}

}


	/* TO ADD

//
// Precision() takes a floating point value and the number of places
// to round off to and returns the value rounded off.
// Example :
//	Precision(2.543267, 3) == 2.5430000 and
// 	Precision(132.583409821, 5) == 132.583410000
//
// float
// Precision(number, precision)
// float number;
// int precision;
//

float
Precision(number, precision)
float number;
int precision;
{
	double atof();
	char string[80], newnumber[80];

	sprintf(string, "%s%d%c", "%.", precision, 'f');
	sprintf(newnumber, string, number);
	return((float)atof(newnumber));

}

order2f(x, y)
float *x, *y;
{
        float t;

        if(*x <= *y)
                return;
        t = *x;
        *x = *y;
        *y = t;
}

order3f(x, y, z)
float *x, *y, *z;
{
        order2f(x, y);
        order2f(x, z);
        order2f(y, z);
}

float
decrnd(decimal, place)
float decimal;
int place;
{
        float mult;
        double pow();

        mult = (float)pow((double)10.0, (double)place);
        return((float)((float)(irint(decimal * mult))/mult));
}

//
// rnd() takes a floating point value and rounds it off to the
// larger integer value.
// Example :
//	rnd(2.5) == 3	 and	rnd(-2.5) == -2
//
// rnd(x)
// float x;
//

rnd(x)
float x;
{
	double floor(), ceil();

	return((int)((((float)floor(x) + 0.5) > x) ? (float)floor(x) : (float)ceil(x)));
}


//
// Length() takes a line and returns the length between its
// two defining points.
//
// float
// Length(lineseg)
// line *lineseg;
//

float
Length(lineseg)
line *lineseg;
{
	float Hypotenuse();

	return(Hypotenuse(lineseg->fpt, lineseg->spt));

}

//
// Length_3() takes a line and returns the length between its
// two defining points in 3-space.
//
// float
// Length_3(lineseg)
// line *lineseg;
//

float
Length_3(lineseg)
line *lineseg;
{
	double sqrt();

	return((float)(sqrt((double)
		(sqr((lineseg->fpt[XCoor] - lineseg->spt[XCoor])) +
		sqr((lineseg->fpt[YCoor] - lineseg->spt[YCoor])) +
		sqr((lineseg->fpt[ZCoor] - lineseg->spt[ZCoor]))))));

}

//
// AssignLineEndPoints() assigns firstpoint and secondpoint as the
// two defining end-points of a line segment.
//
// AssignLineEndPoints(lineseg, fpt, spt)
// line *lineseg;
// point fpt, spt;
//

AssignLineEndPoints(lineseg, fpt, spt)
line *lineseg;
point fpt, spt;
{
	CopyPoint(lineseg->fpt, fpt);
	CopyPoint(lineseg->spt, spt);

}

//
// Hypotenuse() returns the euclidean distance between two points.
//
// float
// Hypotenuse(fpt, spt)
// point fpt, spt;
//

float
Hypotenuse(fpt, spt)
point fpt, spt;
{
	double sqrt();

	return((float)(sqrt((double)
		(sqr((fpt[XCoor] - spt[XCoor] )) +
		sqr((fpt[YCoor] - spt[YCoor] )) +
		sqr((fpt[ZCoor] - spt[ZCoor]))))));

}

//
// MakeUnitVector() takes a directed line segment and returns a point
** that is the unit-vector representation of the line segment.
**
** MakeUnitVector(vector, unitpoint)
** line *vector;
** point unitpoint;
//

MakeUnitVector(vector, unitpoint)
line *vector;
point unitpoint;
{
	float Length_3(), veclen;

	veclen = Length_3(vector);
	SetPoint(unitpoint,
		(vector->spt[XCoor] - vector->fpt[XCoor])/ veclen,
		(vector->spt[YCoor] - vector->fpt[YCoor])/ veclen,
		(vector->spt[ZCoor] - vector->fpt[ZCoor])/ veclen);
		
}

//
** Sort() sorts an array of integers from smallest to largest value.
**
** Sort(Positions)
** int Positions[];
**
** BUG: for use with non-zero integers.
//

Sort(Positions)
int Positions[];
{
	int Head = 0; // head of array to sort //
	int Count, SmallestIndex, TempPosition;

	while (Positions[Head] != 0)
	{
		SmallestIndex = Head;
		Count = Head + 1;
		while (Positions[Count] != 0) // find smallest value //
		{
			if (Positions[SmallestIndex] > Positions[Count])
				SmallestIndex = Count;
			Count++;
		}
		TempPosition = Positions[Head];
		// now swap head of list with smallest value //
		Positions[Head] = Positions[SmallestIndex];
		Positions[SmallestIndex] = TempPosition;
		Head++;
	}

}

NSort(list, n)
int list[], n;
{
	register int head, count, smallest, tmp;

	for(head = 0;head < n;head++)
	{
		smallest = head;
		for(count = head+1;count < n;count++) // find smallest value //
		{
			if(list[smallest] > list[count])
				smallest = count;
		}
		tmp = list[head];
		list[head] = list[smallest];
		list[smallest] = tmp;
	}

}

//
** PointOnCircleOf() takes the radius of a circle centered on the origin
** and either a x or y value of a point on the circle and returns the
** necessary x or y value needed to complete point.
**
** PointOnCircleOf(Radius, Value)
** float Radius;
** int Value;
//

// PointOnCircleOf(Radius, Value)
// float Radius;
// int Value;
// {
	// float sqrt();
// 
	// return(rnd((float)(sqrt(((Radius*Radius) - sqr(Value))))));
// 
// }

//
** InsideLines() returns true if TestPoint is within the region
** bounded by FirstLine and SecondLine. This is done by seeing where
** the point in question is to both lines. For example, if it to
** left (or above) both lines then it is outside the region bounded
** by both lines.
**
** InsideLines(FirstLine, SecondLine, TestPoint)
** line *FirstLine, *SecondLine;
** point TestPoint;
//

InsideLines(FirstLine, SecondLine, TestPoint)
struct LineInformation *FirstLine, *SecondLine;
point TestPoint;
{
	switch(CheckPosition(TestPoint, FirstLine) +
	       CheckPosition(TestPoint, SecondLine))
	{
	  case 0 :
	  case 2 :
		return(FALSE);
	  case 1 :
		return(TRUE);
	}
	return(FALSE);

}

//
** CheckPosition() checks if a point is above or below a line. 
**
** CheckPosition(TestPoint, lineseg)
** point TestPoint;
** line *lineseg;
//

CheckPosition(TestPoint, lineseg)
point TestPoint;
struct LineInformation *lineseg;
{
	float CheckValue, ACoeff, BCoeff, CCoeff;

	ACoeff = lineseg->Coefficient.ACoeff;
	BCoeff = lineseg->Coefficient.BCoeff;
	CCoeff = lineseg->Coefficient.CCoeff;

	CheckValue = (ACoeff * TestPoint[XCoor]) + (BCoeff * TestPoint[YCoor]) + CCoeff;
	if (BCoeff != 0)
		return(SameSign(BCoeff, CheckValue));
	else
		return(SameSign(ACoeff, CheckValue));

}

//
** SameSign() returns true if the firstvalue and secondvalue are both
** negative values or both positive values.
**
** SameSign(FirstValue, SecondValue)
** float FirstValue, SecondValue;
//

SameSign(FirstValue, SecondValue)
float FirstValue, SecondValue;
{
	return(((FirstValue < 0.0) && (SecondValue < 0.0)) || ((FirstValue > 0.0) && (SecondValue > 0.0)));

}

#define InQuadrant00   0
#define InQuadrant10   1
#define InQuadrant15   15
#define InQuadrant20   20
#define InQuadrant25   25
#define InQuadrant30   30
#define InQuadrant35   35
#define InQuadrant40   40

//
** PolarCoordToPoint() sets the ordinate and abscissa of a point,
** in a given plane, according to the given radius and angle that the
** new point is to be from the positive horizontal-axis.
**
** PolarCoordToPoint(radius, angle, RectCoor, Plane)
** float radius, angle;
** point RectCoor;
** enum plane Plane;
//

PolarCoordToPoint(radius, angle, newpt, Plane)
float radius, angle;
point newpt;
enum plane Plane;
{
	double sin(), cos();
	float HorizontalValue, VerticalValue;

	HorizontalValue = radius * (float)cos((double)angleToRadian(angle));
	VerticalValue = radius * (float)sin((double)angleToRadian(angle));

	switch(Plane)
	{
	  case XYPlane :
		newpt[XCoor] = HorizontalValue;
		newpt[YCoor] = VerticalValue;
		break;
	  case XZPlane :
		newpt[XCoor] = HorizontalValue;
		newpt[ZCoor] = VerticalValue;
		break;
	  case YZPlane :
		newpt[YCoor] = HorizontalValue;
		newpt[ZCoor] = VerticalValue;
		break;
	  case YXPlane :
		newpt[YCoor] = HorizontalValue;
		newpt[XCoor] = VerticalValue;
		break;
	  case ZXPlane :
		newpt[ZCoor] = HorizontalValue;
		newpt[XCoor] = VerticalValue;
		break;
	  case ZYPlane :
		newpt[ZCoor] = HorizontalValue;
		newpt[YCoor] = VerticalValue;
		break;
	  default :
		break;
	}

}
*/

/*
PolarRadianCoordToXY(radius, radian, x, y)
float radius, radian;
float *x, *y;
{
	double sin(), cos();

	*x = radius * (float)cos((double)radian);
	*y = radius * (float)sin((double)radian);

}

//
** PointToPolarCoord() returns the cylindrical polar coordinate of
** a point in 3-dimensional space.
**
** PointToPolarCoord(VectorPoint, PolarCoord)
** point VectorPoint;
** struct CylindricalPolarCoordinate *PolarCoord;
//

PointToPolarCoord(VectorPoint, PolarCoord)
point VectorPoint;
struct CylindricalPolarCoordinate *PolarCoord;
{
	float Angle(), RadiusInPlane();

	PolarCoord->ZDisplacement = VectorPoint[ZCoor];
	PolarCoord->Angle = Angle(VectorPoint, XYPlane);
	PolarCoord->Radius = RadiusInPlane(VectorPoint, XYPlane);

}

//*************** SPHERICAL COORD STUFF ******************* //

SetPolarPoint(pt, radius, uval, vval)
point pt;
float radius, uval, vval;
{
	double cos(), sin();

	pt[XCoor] = radius * (float)sin((double)(DegToRad * uval)) *
		(float)sin((double)(DegToRad * vval));
	pt[YCoor] = radius * (float)cos((double)(DegToRad * uval));
	pt[ZCoor] = radius * (float)sin((double)(DegToRad * uval)) *
		(float)cos((double)(DegToRad * vval));
}

SPHEREPOLARPTR
GetNewSphericalPolarVector()
{
        SPHEREPOLARPTR tmpvec;
        void *malloc();

        tmpvec = ((SPHEREPOLARPTR) malloc(sizeof(SPHEREPOLARDEF)));
		tmpvec->uval = tmpvec->vval = tmpvec->radius = 0.0;
		GetNewPoint(tmpvec->spherecenterpt);
        return(tmpvec);
}

SetSphericalPolarVector(sphericalcoord, centerpt, endpt)
SPHEREPOLARPTR sphericalcoord;
point centerpt, endpt;
{
        float RayLength(), Angle();
        point tmppt;
        matrixdef rotmat;

		if(sphericalcoord == NULLSPHEREPOLARPTR)
			return;
        CreateMatrix(&rotmat, 4, 4);
        CopyPoint(sphericalcoord->spherecenterpt, centerpt);
        sphericalcoord->radius = RayLength(centerpt, endpt);
        GetNewPoint(tmppt);
        CopyPoint(tmppt, endpt);
        TranslatePoint(tmppt,
                centerpt[XCoor], centerpt[YCoor], centerpt[ZCoor]);
        sphericalcoord->vval = Angle(tmppt, ZXPlane);
        BuildYRotationMatrix(&rotmat, -sphericalcoord->vval);
        PointMatrixMult(tmppt, &rotmat);
        sphericalcoord->uval = Angle(tmppt, YZPlane);

}

SphericalCoordToPoint(sphericalcoord, spherept)
SPHEREPOLARPTR sphericalcoord;
point spherept;
{
	double sin(), cos();

	spherept[XCoor] = sphericalcoord->radius *
		(float)sin((double)(DegToRad * sphericalcoord->uval)) *
		(float)sin((double)(DegToRad * sphericalcoord->vval)) +
		sphericalcoord->spherecenterpt[XCoor];
	spherept[YCoor] = sphericalcoord->radius *
		(float)cos((double)(DegToRad * sphericalcoord->uval)) +
		sphericalcoord->spherecenterpt[YCoor];
	spherept[ZCoor] = sphericalcoord->radius *
		(float)sin((double)(DegToRad * sphericalcoord->uval)) *
		(float)cos((double)(DegToRad * sphericalcoord->vval)) +
		sphericalcoord->spherecenterpt[ZCoor];
	spherept[3] = 1.0;

}

SphericalCoordVectorAdd(vec1, vec2, resultvec)
SPHEREPOLARPTR vec1, vec2, resultvec;
{
        point cenpt1, endpt1, cenpt2, endpt2, endptresult;

        CopyPoint(cenpt1, vec1->spherecenterpt);
        SphericalCoordToPoint(vec1, endpt1);
        CopyPoint(cenpt2, vec2->spherecenterpt);
        SphericalCoordToPoint(vec2, endpt2);
        SetPoint(endptresult,
                endpt1[XCoor] - cenpt2[XCoor] + endpt2[XCoor],
                endpt1[YCoor] - cenpt2[YCoor] + endpt2[YCoor],
                endpt1[ZCoor] - cenpt2[ZCoor] + endpt2[ZCoor]);
        SetSphericalPolarVector(resultvec, cenpt1, endptresult);
}

//*************** END SPHERICAL COORD STUFF ******************* //

//
** AngleBetweenTwoPoints() takes any two points in a cartesian
** coordinate system and returns the angle between fpt and
** spt, in the counter-clockwise direction, in relation to
** the origin.
**
** float
** AngleBetweenTwoPoints(fpt, spt, Plane)
** point fpt, spt;
** int Plane;
//

// NEED to put in BPlaneSrf
float
AngleBetweenTwoPoints(fpt, spt, Plane)
point fpt, spt;
enum plane Plane;
{
	float Angle(), FirstAngle, SecondAngle;
	double fabs();

	FirstAngle = Angle(fpt, Plane);
	SecondAngle = Angle(spt, Plane);
	if(FirstAngle > SecondAngle)
		return((float)fabs((double)(360.0 - FirstAngle + SecondAngle)));
	else
		return((float)fabs((double)(FirstAngle - SecondAngle)));
	
}

//
** Angle() returns the angle between the positive x-axis and VectorPoint
** that is created by rotating the point in a counter clockwise
** direction.
**
** float
** Angle(VectorPoint, Plane)
** point VectorPoint;
** enum plane Plane;
//

float
Angle(VectorPoint, Plane)
point VectorPoint;
enum plane Plane;
{
	double atan();
	float testangle, tmp;
	int Quadrant;

	Quadrant = FindQuadrantInPlane(VectorPoint, Plane);

	//
	** First check for the trivial cases where the
	** point is on a x or y-axis.
	//

	switch(Quadrant)
	{
	  case InQuadrant00 :
		return(0.0);

	  case InQuadrant15 :
		return(90.0);

	  case InQuadrant25 :
		return(180.0);

	  case InQuadrant35 :
		return(270.0);

	  default :
		break;
	}

	// Now find an angle using the arctan function //

	switch(Plane)
	{
	  case XYPlane :
		tmp = (float)(VectorPoint[YCoor]/VectorPoint[XCoor]);
		break;

	  case XZPlane :
		tmp = (float)(VectorPoint[ZCoor]/VectorPoint[XCoor]);
		break;

	  case YZPlane :
		tmp = (float)(VectorPoint[ZCoor]/VectorPoint[YCoor]);
		break;

	  case YXPlane :
		tmp = (float)(VectorPoint[XCoor]/VectorPoint[YCoor]);
		break;

	  case ZXPlane :
		tmp = (float)(VectorPoint[XCoor]/VectorPoint[ZCoor]);
		break;

	  case ZYPlane :
		tmp = (float)(VectorPoint[YCoor]/VectorPoint[ZCoor]);
		break;

	  default :
		break;
	}

	testangle = radianToAngle((float)atan((double)tmp));

	//
	** The next statement maps the value returned by atan() to
	** the proper angle that is created by rotating a point from
	** the positive horizontal-axis in a counter-clockwise
	** direction.
	//

	switch (Quadrant)
	{
	  case InQuadrant10 :
		return(testangle);
	  case InQuadrant20 :
	  case InQuadrant30 :
		return(testangle + 180.0);
	  case InQuadrant40 :
		return(testangle + 360.0);
	  default :
		return(0.0);
	}

}

//
** GetQuadrant() returns the quadrant of point in question.
**
** GetQuadrant(FirstCoor, SecondCoor)
** float FirstCoor, SecondCoor;
//

//
** RadiusInPlane() returns the distance from a point, in a cartesian
** coordinate system denoted by Plane, to the origin.
**
** float
** RadiusInPlane(RectCoor, Plane)
** point RectCoor;
** enum plane Plane;
//

float
RadiusInPlane(RectCoor, Plane)
point RectCoor;
enum plane Plane;
{
	float FirstValue, SecondValue;
	double sqrt();

	switch(Plane)
	{
	  case XYPlane :
		FirstValue = RectCoor[XCoor];
		SecondValue = RectCoor[YCoor];
		break;
	  case XZPlane :
		FirstValue = RectCoor[XCoor];
		SecondValue = RectCoor[ZCoor];
		break;
	  case YZPlane :
		FirstValue = RectCoor[YCoor];
		SecondValue = RectCoor[ZCoor];
		break;
	  case YXPlane :
		FirstValue = RectCoor[YCoor];
		SecondValue = RectCoor[XCoor];
		break;
	  case ZXPlane :
		FirstValue = RectCoor[ZCoor];
		SecondValue = RectCoor[XCoor];
		break;
	  case ZYPlane :
		FirstValue = RectCoor[ZCoor];
		SecondValue = RectCoor[YCoor];
		break;
	  default :
		break;
	}

	return((float)(sqrt((double)(sqr(FirstValue) + sqr(SecondValue)))));

}

//
** Radius() returns the distance from a point,in 3-space,to the origin.
**
** float
** Radius(VectorPoint)
** point VectorPoint;
//

float
Radius(VectorPoint)
point VectorPoint;
{
	double sqrt();

	return((float)(sqrt((double)(sqr(VectorPoint[XCoor]) + sqr(VectorPoint[YCoor]) + sqr(VectorPoint[ZCoor])))));

}

//
** CreatePerpendicular() creates a perpendicular line to BaseLine at
** BasePoint which must be on BaseLine. The perpendicular line created
** will have its fpt at the BasePoint and its spt a
** LineLength distance away from BasePoint in a Perpendicular direction
** according to whether Direction is 0 or a positive value. The
** algorithm for determining which direction Perpendicular.spt
** lies in relation to BaseLine is as follows: BasePoint is transferred
** to the origin of cartesian coordinate system. BaseLine.fpt is
** transfered in relation to origin. If one were to look down at
** cartesian coordinate system at BaseLine.fpt and at BasePoint,
** then Direction == 0	means that Perpendicular.spt will be to
** the left of BaseLine.
**
** BUGS: BasePoint can't be BaseLine.fpt.
**
** CreatePerpendicular(BaseLine, BasePoint, Perpendicular, LineLength, Direction)
** line *BaseLine;
** point BasePoint;
** line *Perpendicular;
** float LineLength;
//

CreatePerpendicular(BaseLine, BasePoint, Perpendicular, LineLength, Direction)
line *BaseLine;
point BasePoint;
line *Perpendicular;
float LineLength;
int Direction;
{
	float Angle();
	line TestLine;

	AssignLineEndPoints(&TestLine, BaseLine->fpt, BaseLine->spt);

	//
	** First transform TestLine to origin with BasePoint on
	** the origin.
	//

	TranslatePoint(TestLine.fpt, BasePoint[XCoor], BasePoint[YCoor], 0.0);

	//
	** Next get Perpendicular.spt at a LineLengths
	** distance away from origin rotated clockwise 90 degrees from
	** the position of TestLine.fpt if Direction is negative
	** or rotated counter-clockwise if Direction is positive.
	//

	PolarCoordToPoint(LineLength,
		Angle(TestLine.fpt, XYPlane) + (Direction ? 90.0 : -90.0),
		Perpendicular->spt, XYPlane);

	// Now translate points back to starting place //	

	CopyPoint(Perpendicular->fpt, BasePoint);
	TranslatePoint(Perpendicular->spt, -BasePoint[XCoor], -BasePoint[YCoor], 0.0);

}

//
** GetNewPointOnLine() returns a new point on a linesegment a Distance
** distance away from the lineseg.fpt.
**
** GetNewPointOnLine(lineseg, NewPoint, Distance)
** line *lineseg;
** point NewPoint;
** float Distance;
//

GetNewPointOnLine(lineseg, NewPoint, Distance)
line *lineseg;
point NewPoint;
float Distance;
{
	float Length(), r;

	r = Distance / Length(lineseg);
	NewPoint[XCoor] = lineseg->fpt[XCoor] + (r * (lineseg->spt[XCoor] - lineseg->fpt[XCoor]));
	NewPoint[YCoor] = lineseg->fpt[YCoor] + (r * (lineseg->spt[YCoor] - lineseg->fpt[YCoor]));
	NewPoint[ZCoor] = lineseg->fpt[ZCoor] + (r * (lineseg->spt[ZCoor] - lineseg->fpt[ZCoor]));

}

//
** CopyPoint() copies the contents of spt to fpt.
**
** CopyPoint(fpt, spt)
** point fpt, spt;
//

CopyPoint(fpt, spt)
point fpt, spt;
{
	register int i;

	for(i = 0;i < 4;i++)
		fpt[i] = spt[i];

}

//
** CylindricalPolarCoordToPoint() takes a cylindrical polar coordinate
** and returns the x, y, z position.
**
** CylindricalPolarCoordToPoint(RadialDistance, RotationAngle, AxialRise, EqualPoint)
** float RadialDistance, RotationAngle, AxialRise;
** point EqualPoint;
//

CylindricalPolarCoordToPoint(RadialDistance, RotationAngle, AxialRise, EqualPoint)
float RadialDistance, RotationAngle, AxialRise;
point EqualPoint;
{
	PolarCoordToPoint(RadialDistance, RotationAngle, EqualPoint, XYPlane);
	
	EqualPoint[ZCoor] = AxialRise;

}

//
** TranslatePoint() translate a point in 3-D according to the x, y, z
** value given.
**
** TranslatePoint(TestPoint, X, Y, Z)
** point TestPoint;
** float X, Y, Z;
//

// USE BPoint.translate
TranslatePoint(testpt, xpos, ypos, zpos)
point testpt;
float xpos, ypos, zpos;
{
	testpt[XCoor] -= xpos;
	testpt[YCoor] -= ypos;
	testpt[ZCoor] -= zpos;

}

//
** ShiftPtsOrigin(pt1, pt2) translates pt1 to reference point pt2;
//

ShiftPtsOrigin(pt1, pt2)
point pt1, pt2;
{
	pt1[XCoor] += pt2[XCoor];
	pt1[YCoor] += pt2[YCoor];
	pt1[ZCoor] += pt2[ZCoor];

}

//
** TransVecToWorldOrigin() tranlates a vector with tail point at tail
** and head point at head to the origion in world coordinates such that
** head = head - tail; tail is left as is.
**
** TransVecToWorldOrigin(tail, head)
** point tail, head;
//

TransVecToWorldOrigin(tail, head)
point tail, head;
{
	TranslatePoint(head, tail[XCoor], tail[YCoor], tail[ZCoor]);

}

//
look into:
VectorsAngle(positionvector1, positionvector2)
in mathtools.c
and:
which translate head only to origin as if tail is at origin
//

// PUT into BPlaneSrf
// return the angle between 2 vectors in space (not position vectors) //
float
AngleBetween2Vectors(vec0Tail, vec0Head, vec1Tail, vec1Head)
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
	return (radianToAngle(VectorsAngle(head0Pt, head1Pt)));
}

//
** TranslatePoints() translates a list of points in 3-D according to
** the x, y, z value given.
**
** TranslatePoints(PointCount, ListOfPoints, X, Y, Z)
** int PointCount;
** point ListOfPoints[];
** float X, Y, Z;
//

TranslatePoints(ptlist, ptcount, xpos, ypos, zpos)
point ptlist[];
register int ptcount;
register float xpos, ypos, zpos;
{
	register int count;

	for(count = 0;count < ptcount;count++)
		TranslatePoint(ptlist[count], xpos, ypos, zpos);

}

//
** CenterPoints() centers a list of points in 3-D.
**
** CenterPoints(PointCount, ListOfPoints);
** int PointCount;
** point ListOfPoints[];
//

CenterPoints(PointCount, ListOfPoints)
int PointCount;
point ListOfPoints[];
{
	point centerpt, smallpt, largept;
	int listindex, ptindex;

	GetNewPoint(centerpt);
	GetNewPoint(smallpt);
	GetNewPoint(largept);
	SetPoint(smallpt, 999999.9, 999999.9, 999999.9);
	SetPoint(largept, -999999.9, -999999.9, -999999.9);
	for(listindex = 0;listindex < PointCount;listindex++)
	{
		for(ptindex = 0;ptindex < 3;ptindex++)
		{
			if(ListOfPoints[listindex][ptindex] < smallpt[ptindex])
				smallpt[ptindex] = ListOfPoints[listindex][ptindex];
			if(ListOfPoints[listindex][ptindex] > largept[ptindex])
				largept[ptindex] = ListOfPoints[listindex][ptindex];
		}
	}
	GetMidPoint(centerpt, smallpt, largept);
	TranslatePoints(ListOfPoints, PointCount, centerpt[XCoor], centerpt[YCoor], centerpt[ZCoor]);

}

//
** GetMidPoint() returns MidPoint equal to the point exactly 1/2 way
** between fpt and spt.
**
** GetMidPoint(MidPoint, fpt, spt)
** point MidPoint, fpt, spt;
//

GetMidPoint(MidPoint, fpt, spt)
point MidPoint, fpt, spt;
{
	MidPoint[XCoor] = (fpt[XCoor] + spt[XCoor]) / 2.0;
	MidPoint[YCoor] = (fpt[YCoor] + spt[YCoor]) / 2.0;
	MidPoint[ZCoor] = (fpt[ZCoor] + spt[ZCoor]) / 2.0;
}

//
** TransferToOrigin() transfers 3 points in 3-D such that midpt is
** on the origion and fpt, spt are in relation to
** midpt.
** 
** TransferToOrigin(fpt, midpt, spt)
** point fpt, midpt, spt;
//

TransferToOrigin(fpt, midpt, spt)
point fpt, midpt, spt;
{
	TranslatePoint(fpt, midpt[XCoor], midpt[YCoor], midpt[ZCoor]);
	TranslatePoint(spt, midpt[XCoor], midpt[YCoor], midpt[ZCoor]);
	midpt[XCoor] = midpt[YCoor] = midpt[ZCoor] = 0.0;

}

//
** GetNewLine() initializes a new line.
**
** GetNewLine(lineseg)
** line *lineseg;
//

GetNewLine(lineseg)
line *lineseg;
{
	GetNewPoint(lineseg->fpt);
	GetNewPoint(lineseg->spt);

}

//
** GetNewPoint() creates a 1x4 matrix representing a point.
**
** GetNewPoint(PointMatrix)
** point PointMatrix;
//

GetNewPoint(PointMatrix)
point PointMatrix;
{
	SetPoint(PointMatrix, 0.0, 0.0, 0.0);
	PointMatrix[3] = 1.0;

}

//
** SetPoint() Assigns x, y, z values to TestPoint;
**
** SetPoint(TestPoint, X, Y, Z)
** point TestPoint;
** float X, Y, Z;
//

SetPoint(TestPoint, X, Y, Z)
point TestPoint;
float X, Y, Z;
{
	TestPoint[XCoor] = X;
	TestPoint[YCoor] = Y;
	TestPoint[ZCoor] = Z;

}

//
** ExtendPoints() contracts or expands fpt and spt in
** the XY-Plane a Distance amount.
//

ExtendPoints(fpt, spt, Distance)
point fpt, spt;
float Distance;
{
	line TempLine;
	float Length();

	AssignLineEndPoints(&TempLine, spt, fpt);
	GetNewPointOnLine(&TempLine, fpt, Length_3(&TempLine) + Distance);
	AssignLineEndPoints(&TempLine, fpt, spt);
	GetNewPointOnLine(&TempLine, spt, Length_3(&TempLine) + Distance);

}

//
** ROneToROneMap() returns value in first real line mapped to
** second real line.
**
** float
** ROneToROneMap(value, begin1, end1, begin2, end2)
** float value, begin1, end1, begin2, end2;
//

float
ROneToROneMap(value, begin1, end1, begin2, end2)
float value, begin1, end1, begin2, end2;
{
	return((value - begin1) * ((end2 - begin2)/(end1 - begin1)) + begin2);

}

//
** RTwoToRTwoMap() returns newx, newy, which is new point in second
** plane as mapped from first plane.
**
** float
** RTwoToRTwoMap(valuex, valuey, newx, newy, minx, miny, maxx, maxy,
**	MinX, MinY, MaxX, MaxY)
** float valuex, valuey, *newx, *newy, minx, miny, maxx, maxy,
**	MinX, MinY, MaxX, MaxY;
**
//

RTwoToRTwoMap(valuex, valuey, newx, newy, minx, miny, maxx, maxy, MinX, MinY, MaxX, MaxY)
float valuex, valuey, *newx, *newy, minx, miny, maxx, maxy, MinX, MinY, MaxX, MaxY;
{
	float ROneToROneMap();
	
	*newx = ROneToROneMap(valuex, minx, maxx, MinX, MaxX);
	*newy = ROneToROneMap(valuey, miny, maxy, MinY, MaxY);

}

float
NormalToFloatRGB(normalval)
float normalval;
{
	return(ROneToROneMap(normalval, 0.0, 1.0, 0.0, 255.0));
}

NormalToIntRGB(normalval)
float normalval;
{
	return(irint(ROneToROneMap(normalval, 0.0, 1.0, 0.0, 255.0)));
}

float
FloatRGBToNormal(normalval)
float normalval;
{
	return(ROneToROneMap(normalval, 0.0, 255.0, 0.0, 1.0));
}

//
SCALARPTR
CrossProduct(u, v)
point u, v;
{
	point vecproduct;

	GetNewPoint(vecproduct);
	vecproduct[0] = (u[YCoor] * v[ZCoor]) - (v[YCoor] * u[ZCoor]);
	vecproduct[1] = -(u[XCoor] * v[ZCoor]) + (v[XCoor] * u[ZCoor]);
	vecproduct[2] = (u[XCoor] * v[YCoor]) - (v[XCoor] * u[YCoor]);

	return(&vecproduct[0]);
}
//

// BUG fix warning: function returns address of local variable
SCALARPTR
CrossProductComp(x1, y1, z1, x2, y2, z2)
float x1, y1, z1, x2, y2, z2;
{
	point vecproduct;

	GetNewPoint(vecproduct);
	vecproduct[0] = (y1 * z2) - (y2 * z1);
	vecproduct[1] = -(x1 * z2) + (x2 * z1);
	vecproduct[2] = (x1 * y2) - (x2 * y1);
	return(&vecproduct[0]);

}
//

VectorCrossProduct(u, v, crossproduct)
point u, v, crossproduct;
{
	crossproduct[0] = (u[YCoor] * v[ZCoor]) - (v[YCoor] * u[ZCoor]);
	crossproduct[1] = -(u[XCoor] * v[ZCoor]) + (v[XCoor] * u[ZCoor]);
	crossproduct[2] = (u[XCoor] * v[YCoor]) - (v[XCoor] * u[YCoor]);
	crossproduct[3] = 1.0;
}

TriangleCrossProduct(u, triorigin, v, crossproduct)
point u, triorigin, v, crossproduct;
{
	float ux, uy, uz, vx, vy, vz;

	ux = u[XCoor] - triorigin[XCoor];
	uy = u[YCoor] - triorigin[YCoor];
	uz = u[ZCoor] - triorigin[ZCoor];
	vx = v[XCoor] - triorigin[XCoor];
	vy = v[YCoor] - triorigin[YCoor];
	vz = v[ZCoor] - triorigin[ZCoor];
	crossproduct[XCoor] = (uy * vz) - (vy * uz) + triorigin[XCoor];
	crossproduct[YCoor] = -(ux * vz) + (vx * uz) + triorigin[YCoor];
	crossproduct[ZCoor] = (ux * vy) - (vx * uy) + triorigin[ZCoor];
	crossproduct[3] = 1.0;

}

float
VectorDotProduct(u, v)
point u, v;
{
	return((u[XCoor] * v[XCoor]) + (u[YCoor] * v[YCoor]) + (u[ZCoor] * v[ZCoor]));
}

float
VectorDotProductComp(ux, uy, uz, vx, vy, vz)
float ux, uy, uz, vx, vy, vz;
{
	return((ux * vx) + (uy * vy) + (uz * vz));

}

float
VectorNorm(positionvector)
point positionvector;
{
	double sqrt();

	return((float)sqrt((double)(sqr(positionvector[XCoor]) +
		sqr(positionvector[YCoor]) + sqr(positionvector[ZCoor]))));

}

float
VectorNormComp(xcoor, ycoor, zcoor)
float xcoor, ycoor, zcoor;
{
	double sqrt();

	return((float)sqrt((double)(sqr(xcoor) + sqr(ycoor) + sqr(zcoor))));

}

// BUG fix warning: function returns address of local variable
SCALARPTR
GetUnitVector(vec)
point vec;
{
	float VectorNorm(), norm;
	point normvec;

	norm = VectorNorm(vec);
	SetPoint(normvec, vec[XCoor]/norm, vec[YCoor]/norm, vec[ZCoor]/norm);
	return(&normvec[0]);

}
//

UnitVector(positionvector)
point positionvector;
{
	float VectorNorm(), norm;

	norm = VectorNorm(positionvector);
	SetPoint(positionvector,
		positionvector[XCoor]/norm,
		positionvector[YCoor]/norm,
		positionvector[ZCoor]/norm);
}

ChangeVectorLength(positionvector, length)
point positionvector;
float length;
{
	float VectorNorm();

	length /= VectorNorm(positionvector);
	positionvector[XCoor] *= length;
	positionvector[YCoor] *= length;
	positionvector[ZCoor] *= length;

}

LengthenVector(pt1, pt2, length)
point pt1, pt2;
float length;
{
	point newpt1, newpt2;
	float GetRayTValue(), RayLength();

	GetNewPoint(newpt1);
	GetNewPoint(newpt2);
	GetVectorLineAtT(pt1, pt2, newpt1,
		GetRayTValue(pt1, pt2, RayLength(pt1, pt2) + (length/2.0)));
	GetVectorLineAtT(pt2, pt1, newpt2,
		GetRayTValue(pt2, pt1, RayLength(pt2, pt1) + (length/2.0)));
	CopyPoint(pt1, newpt2);
	CopyPoint(pt2, newpt1);

}

ShortenVector(pt1, pt2, length)
point pt1, pt2;
float length;
{
	point newpt1, newpt2;
	float GetRayTValue(), RayLength();

	GetNewPoint(newpt1);
	GetNewPoint(newpt2);
	GetVectorLineAtT(pt1, pt2, newpt1,
		GetRayTValue(pt1, pt2, RayLength(pt1, pt2) - length));
	GetVectorLineAtT(pt2, pt1, newpt2,
		GetRayTValue(pt2, pt1, RayLength(pt2, pt1) - length));
	CopyPoint(pt1, newpt2);
	CopyPoint(pt2, newpt1);

}

ShortenFptVector(pt1, pt2, length)
point pt1, pt2;
float length;
{
	point newpt;
	float GetRayTValue();

	GetNewPoint(newpt);
	GetVectorLineAtT(pt1, pt2, newpt,
		GetRayTValue(pt1, pt2, length));
	CopyPoint(pt1, newpt);

}

LengthenVectorHead(pt1, pt2, length)
point pt1, pt2;
float length;
{
	point newpt;
	float GetRayTValue();

	GetNewPoint(newpt);
	GetVectorLineAtT(pt1, pt2, newpt,
		1.0 + GetRayTValue(pt1, pt2, length));
	CopyPoint(pt2, newpt);

}

//
** GetRayTValue() returns a t value for a vector given distance from
** tailpt to headpt if t==0.0 is t value at tailpt, and t==1.0 is t
** value at headpt.
**
** float
** GetRayTValue(tailpt, headpt, distance)
** point tailpt, headpt;
** float distance;
//

float
GetRayTValue(tailpt, headpt, distance)
point tailpt, headpt;
float distance;
{
	float RayLength();

	return(distance / RayLength(tailpt, headpt));

}

//
** RayLength() returns the distance between the two endpoints of a
** vector.
**
** float
** RayLength(fpt, spt)
** point fpt, spt;
//

float
RayLength(fpt, spt)
point fpt, spt;
{
	register float a, b, c;
	double sqrt();

	a = fpt[XCoor] - spt[XCoor];
	b = fpt[YCoor] - spt[YCoor];
	c = fpt[ZCoor] - spt[ZCoor];

	return((float)sqrt((double)(a*a + b*b + c*c)));

}

//
** MinDistLineToLine() returns the minimum distance between two lines.
**
** float
** MinDistLineToLine(tail1pt, head1pt, tail2pt, head2pt)
** point tail1pt, head1pt, tail2pt, head2pt;
//

float
MinDistLineToLine(tail1pt, head1pt, tail2pt, head2pt)
point tail1pt, head1pt, tail2pt, head2pt;
{
	float x1, y1, z1, x2, y2, z2, f1, g1, h1, f2, g2, h2;
	double sqrt();

	x1 = tail1pt[XCoor];
	y1 = tail1pt[YCoor];
	z1 = tail1pt[ZCoor];

	x2 = tail2pt[XCoor];
	y2 = tail2pt[YCoor];
	z2 = tail2pt[ZCoor];

	f1 = head1pt[XCoor] - x1;
	g1 = head1pt[YCoor] - y1;
	h1 = head1pt[ZCoor] - z1;
	f2 = head2pt[XCoor] - x2;
	g2 = head2pt[YCoor] - y2;
	h2 = head2pt[ZCoor] - z2;

	return(((x2 - x1)*(g1*h2 - g2*h1) -(y2 - y1)*(f1*h2 - f2*h1)
		 +(z2 - z1)*(f1*g2 - f2*g1) ) /
		 (float)sqrt((f1*g2-f2*g1)*(f1*g2-f2*g1) +
			(g1*h2-g2*h1)*(g1*h2-g2*h1) + (h1*f2-h2*f1)*(h1*f2-h2*f1)));

}

// BUG: need to stop using VectorLineAtT
// SCALARPTR
// GeomRaySphereIntersect(tailpt, headpt, sphereorigion, radius, rayspheredist, t)
// point tailpt, headpt, sphereorigion;
// float radius, *rayspheredist, *t;
// {
	// point sphereraycenterpt;
	// SCALARPTR VectorLineAtT();
	// float Hypotenuse(), DistPtToLine();
	// double sqrt();
// 
	// *rayspheredist = 
		// (float)sqrt((double)DistPtToLine(tailpt, headpt, sphereorigion, t));
	// CopyPoint(sphereraycenterpt, VectorLineAtT(tailpt, headpt, *t));
// 	
	// return(VectorLineAtT(sphereraycenterpt, tailpt,
		// (1.0/Hypotenuse(tailpt, sphereraycenterpt)) *
		// (float)sqrt((double)(radius*radius - (*rayspheredist)*(*rayspheredist)))));
// 
// }

RaySphereIntersect(tailpt, headpt, origion, radius, firstt, lastt)
point tailpt, headpt, origion;
float radius, *firstt, *lastt;
{
	float ax, ay, az, cx, cy, cz, a, b, c, h, k, l, radical, rsqrt;
	double sqrt();

	h = origion[XCoor];
	k = origion[YCoor];
	l = origion[ZCoor];
	ax = tailpt[XCoor];
	ay = tailpt[YCoor];
	az = tailpt[ZCoor];
	cx = headpt[XCoor];
	cy = headpt[YCoor];
	cz = headpt[ZCoor];

	a = cz*cz - 2.0*az*cz + cy*cy - 2.0*ay*cy + cx*cx
		- 2.0*ax*cx + az*az + ay*ay + ax *ax;
	b = -2.0*cz*l + 2.0*az*l - 2.0*cy*k + 2.0*ay*k
		- 2.0*cx*h + 2.0*ax*h + 2.0*az*cz + 2.0*ay*cy
		+ 2.0*ax*cx - 2.0*az*az - 2.0*ay*ay - 2.0*ax*ax;
	c = - radius*radius + l*l - 2.0*az*l + k*k
		- 2.0*ay*k + h*h - 2.0*ax*h + az*az + ay*ay + ax*ax;

	//
	radical = (float)sqrt((double)(b*b - 4.0*a*c));
	*firstt = (-b - radical)/(2.0*a);
	*lastt = (-b + radical)/(2.0*a);
	//
        radical = b*b - 4.0*a*c;
        if(radical < 0.0)
        {
                *firstt = *lastt = -1.0;
                return;
        }
        rsqrt = (float)sqrt((double)radical);
 
        *firstt = (-b - rsqrt)/(2.0*a);
        *lastt = (-b + rsqrt)/(2.0*a);
}

float
RaySphereDistance(tailpt, headpt, origion, t)
point tailpt, headpt, origion;
float *t;
{
	float DistPtToLine();

	return(DistPtToLine(tailpt, headpt, origion, t));

}

char
RayIntersectsSphere(tailpt, headpt, sphereradius, sphereorigion)
point tailpt, headpt;
float sphereradius;
point sphereorigion;
{
	float t, firstt, lastt;
	float RaySphereDistance();

	if(RaySphereDistance(tailpt, headpt, sphereorigion, &t) > sphereradius)
		return(FALSE);
	RaySphereIntersect(tailpt, headpt, sphereorigion, sphereradius, &firstt, &lastt);
	if(((firstt >= 0.0) && (firstt <= 1.0)) || ((lastt >= 0.0) && (lastt <= 1.0)))
		return(TRUE);
	return(FALSE);

}

//
** VectorsAngle() returns the angle, in radians, between two position vectors.
**
** float
** VectorsAngle(positionvector1, positionvector2)
** point positionvector1, positionvector2;
//

float
VectorsAngle(positionvector1, positionvector2)
point positionvector1, positionvector2;
{
	float VectorNorm(), VectorDotProduct();
	double acos();

	return((float)acos((double)VectorDotProduct(positionvector1, positionvector2)/
		(VectorNorm(positionvector1) * VectorNorm(positionvector2))));

}

//
** Max3() returns the maximum floating point value of 3 floats.
**
** float
** Max3(a, b, c)
** float a, b, c;
//

float
Max3(a, b, c)
float a, b, c;
{
	if((a > b) && (a > c))
		return(a);
	if((b > a) && (b > c))
		return(b);
	if((c > a) && (c > b))
		return(c);
	if(a == b)
		return(Max(a, c));
	if(a == c)
		return(Max(a, b));
	if(b == c)
		return(Max(a, b));
	if((a == b) && (b == c))
		return(a);
	return(a);

}

//
** SeparatePoints() take the points fpt and spt and separates them
** by a distance amount;
**
** SeparatePoints(fpt, spt, distance)
** point fpt, spt;
** float distance;
//

SeparatePoints(fpt, spt, distance)
point fpt, spt;
float distance;
{
	float GetRayTValue();
	point tailpt, headpt, midpoint;

	GetNewPoint(midpoint);
	GetMidPoint(midpoint, fpt, spt);
	CopyPoint(tailpt, fpt);
	CopyPoint(headpt, spt);
	GetVectorLineAtT(midpoint, headpt, spt, GetRayTValue(midpoint, headpt, 0.5 * distance));
	GetVectorLineAtT(midpoint, tailpt, fpt, GetRayTValue(midpoint, tailpt, 0.5 * distance));

}

//
** MakePointsCircular() takes a list of points, ptlist, ptcount long and
** returns the list in the best fit circle.
**
** MakePointsCircular(ptlist, ptcount)
** point ptlist[];
** int ptcount;
//

MakePointsCircular(ptlist, ptcount, rad, centerpt)
point ptlist[];
int ptcount;
float *rad;
point centerpt;
{
	register int i;
	point smallpt, largept, tmppt;
	float x, y;
	float GetRayTValue();

	FindMinMaxPt(ptlist, ptcount, smallpt, largept);
	GetMidPoint(centerpt, smallpt, largept);
	x = 0.5*(largept[XCoor] - smallpt[XCoor]);
	y = 0.5*(largept[YCoor] - smallpt[YCoor]);
	*rad = Max(x, y);
	for(i = 0;i < ptcount;i++)
	{
		GetVectorLineAtT(centerpt, ptlist[i], tmppt, GetRayTValue(centerpt, ptlist[i], *rad));
		CopyPoint(ptlist[i], tmppt);
	}

}

//
** GetPointsRadius() takes a list of points, ptlist, ptcount long and
** returns the radius in the best fit circle.
**
** GetPointsRadius(ptlist, ptcount)
** point ptlist[];
** int ptcount;
//

float
GetPointsRadius(ptlist, ptcount)
point ptlist[];
int ptcount;
{
	point smallpt, largept, centerpt;
	float x, y;

	FindMinMaxPt(ptlist, ptcount, smallpt, largept);
	GetMidPoint(centerpt, smallpt, largept);
	x = 0.5*(largept[XCoor] - smallpt[XCoor]);
	y = 0.5*(largept[YCoor] - smallpt[YCoor]);
	return(Max(x, y));

}

//
** FindCircularCenterOfPoints() finds the center point, centerpt, and
** the radius, rad, of a list of arbitrary points.
**
** FindCircularCenterOfPoints(ptlist, ptcount, centerpt, rad)
** point ptlist[];
** int ptcount;
** point centerpt;
** float *rad;
//

FindCircularCenterOfPoints(ptlist, ptcount, centerpt, rad)
point ptlist[];
int ptcount;
point centerpt;
float *rad;
{
	point smallpt, largept;
	float x, y;

	FindMinMaxPt(ptlist, ptcount, smallpt, largept);
	GetMidPoint(centerpt, smallpt, largept);
	x = 0.5*(largept[XCoor] - smallpt[XCoor]);
	y = 0.5*(largept[YCoor] - smallpt[YCoor]);
	*rad = Max(x, y);

}

//
** FindMinMaxPt() finds the smallest and largest point in a collection
** points.
**
** FindMinMaxPt(ptlist, ptcount, smallpt, largept)
** point ptlist[];
** int ptcount;
** point smallpt, largept;
//

FindMinMaxPt(ptlist, ptcount, smallpt, largept)
point ptlist[];
int ptcount;
point smallpt, largept;
{
	register int i;
 	float xpos, ypos, zpos;
 	float smallx = 9999999.9, smally = 9999999.9, smallz = 9999999.9,
 		largex = -9999999.9, largey = -9999999.9, largez = -9999999.9;

	for(i = 0;i < ptcount;i++)
	{
		xpos = ptlist[i][XCoor];
		ypos = ptlist[i][YCoor];
		zpos = ptlist[i][ZCoor];
		if(xpos < smallx)
			smallx = xpos;
		if(ypos < smally)
			smally = ypos;
		if(zpos < smallz)
			smallz = zpos;

		if(xpos > largex)
			largex = xpos;
		if(ypos > largey)
			largey = ypos;
		if(zpos > largez)
			largez = zpos;
	}
	smallpt[XCoor] = smallx;
	smallpt[YCoor] = smally;
	smallpt[ZCoor] = smallz;
	largept[XCoor] = largex;
	largept[YCoor] = largey;
	largept[ZCoor] = largez;

}

int
GetLineIntersect(tail1, head1, u, tail2, head2, v, intersect)
point tail1, head1;
float *u;
point tail2, head2;
float *v;
point intersect;
{
	float tmp, tmpu, tmpv;

	tmp = ((head2[YCoor] - tail2[YCoor])*(head1[XCoor] - tail1[XCoor]) - (head2[XCoor] - tail2[XCoor])*(head1[YCoor] - tail1[YCoor]));
	if(tmp == 0.0)
		return(FALSE);
	tmpv = ((tail2[XCoor] - tail1[XCoor])*(head1[YCoor] - tail1[YCoor]) - (tail2[YCoor] - tail1[YCoor])*(head1[XCoor] - tail1[XCoor])) / tmp;
	tmp = (head1[XCoor] - tail1[XCoor])*(head2[YCoor] - tail2[YCoor]) - (head2[XCoor] - tail2[XCoor])*(head1[YCoor] - tail1[YCoor]);
	if(tmp == 0.0)
		return(FALSE);
	tmpu = ((tail2[XCoor] - tail1[XCoor])*(head2[YCoor] - tail2[YCoor]) + (head2[XCoor] - tail2[XCoor])*(tail1[YCoor] - tail2[YCoor]))/tmp;

	intersect[XCoor] = tail2[XCoor] + tmpv*(head2[XCoor] - tail2[XCoor]);
	intersect[YCoor] = tail2[YCoor] + (tmpv*(head2[YCoor] - tail2[YCoor]));
	intersect[ZCoor] = tail2[ZCoor] + tmpv*(head2[ZCoor] - tail2[ZCoor]);
	*u = tmpu;
	*v = tmpv;
	return(TRUE);

}

int
Get2DRectangleRayIntersect(rectcenter, offsetx, offsety, raytail, rayhead, intersect, u)
point rectcenter;
float offsetx, offsety;
point raytail, rayhead, intersect;
float *u;
{
	point bottomtail, bottomhead, lefttail, lefthead, toptail, tophead,
		righttail, righthead, tmppt;
	float rectu, rayu, huge = 9999999.9;
	
	SetPoint(bottomtail, rectcenter[XCoor] - offsetx,
		rectcenter[YCoor] - offsety, 0.0);
	SetPoint(bottomhead, rectcenter[XCoor] + offsetx,
		rectcenter[YCoor] - offsety, 0.0);

	SetPoint(lefttail, rectcenter[XCoor] - offsetx,
		rectcenter[YCoor] - offsety, 0.0);
	SetPoint(lefthead, rectcenter[XCoor] - offsetx,
		rectcenter[YCoor] + offsety, 0.0);

	SetPoint(toptail, rectcenter[XCoor] - offsetx,
		rectcenter[YCoor] + offsety, 0.0);
	SetPoint(tophead, rectcenter[XCoor] + offsetx,
		rectcenter[YCoor] + offsety, 0.0);

	SetPoint(righttail, rectcenter[XCoor] + offsetx,
		rectcenter[YCoor] + offsety, 0.0);
	SetPoint(righthead, rectcenter[XCoor] + offsetx,
		rectcenter[YCoor] - offsety, 0.0);
	
	*u = huge;

	if(GetLineIntersect(raytail, rayhead, &rayu, bottomtail, bottomhead, &rectu, tmppt))
	{
		if((rayu >= 0.0) && (rayu <= 1.0) && (rectu >= 0.0) && (rectu <= 1.0))
		{
			CopyPoint(intersect, tmppt);
			*u = rayu;
		}
	}
	if(GetLineIntersect(raytail, rayhead, &rayu, lefttail, lefthead, &rectu, tmppt))
	{
		if((rayu >= 0.0) && (rayu <= 1.0) && (rectu >= 0.0) && (rectu <= 1.0) && (rayu < *u))
		{
			CopyPoint(intersect, tmppt);
			*u = rayu;
		}
	}
	if(GetLineIntersect(raytail, rayhead, &rayu, toptail, tophead, &rectu, tmppt))
	{
		if((rayu >= 0.0) && (rayu <= 1.0) && (rectu >= 0.0) && (rectu <= 1.0) && (rayu < *u))
		{
			CopyPoint(intersect, tmppt);
			*u = rayu;
		}
	}
	if(GetLineIntersect(raytail, rayhead, &rayu, righttail, righthead, &rectu, tmppt))
	{
		if((rayu >= 0.0) && (rayu <= 1.0) && (rectu >= 0.0) && (rectu <= 1.0) && (rayu < *u))
		{
			CopyPoint(intersect, tmppt);
			*u = rayu;
		}
	}

	return(*u < huge);

}

GetTriangleCenterPoint(a, b, c, centerpt)
point a, b, c, centerpt;
{
	point bcmidpoint, acmidpoint;
	float u, v;

	GetMidPoint(acmidpoint, a, c);
	GetMidPoint(bcmidpoint, b, c);
	GetLineIntersect(a, bcmidpoint, &u, b, acmidpoint, &v, centerpt);

}

PtLeftOfRayInPlane(pt, raytail, rayhead, inalign)
point pt, raytail, rayhead;
char *inalign;
{
	point tmppt1, tmppt2, tmppt3;
	float RadiusInPlane(), AngleInXYPlane();

	SetPoint(tmppt1, pt[XCoor] - raytail[XCoor],
		pt[YCoor] - raytail[YCoor], 0.0);
	SetPoint(tmppt2, rayhead[XCoor] - raytail[XCoor],
		rayhead[YCoor] - raytail[YCoor], 0.0);
	PolarCoordToPoint(RadiusInPlane(tmppt1, XYPlane),
		AngleInXYPlane(tmppt1) - AngleInXYPlane(tmppt2), tmppt3, XYPlane);
	*inalign = (tmppt3[YCoor] == 0.0);
	return(tmppt3[YCoor] > 0.0);

}

PointInPlaneTriangle(pt, tpt1, tpt2, tpt3)
point pt, tpt1, tpt2, tpt3;
{
	char inalign;

	return((PtLeftOfRayInPlane(pt, tpt1, tpt2, &inalign)) &&
		(PtLeftOfRayInPlane(pt, tpt2, tpt3, &inalign)) &&
		(PtLeftOfRayInPlane(pt, tpt3, tpt1, &inalign)));

}

PointInPlaneRectangle(pt, tpt1, tpt2, tpt3, tpt4)
point pt, tpt1, tpt2, tpt3, tpt4;
{
	char inalign;

	return((PtLeftOfRayInPlane(pt, tpt1, tpt2, &inalign)) &&
		(PtLeftOfRayInPlane(pt, tpt2, tpt3, &inalign)) &&
		(PtLeftOfRayInPlane(pt, tpt3, tpt4, &inalign)) &&
		(PtLeftOfRayInPlane(pt, tpt4, tpt1, &inalign)));

}

PointInRasterRectangle(x, y, topleftx, toplefty, length, depth)
int x, y, topleftx, toplefty, length, depth;
{
        return((x >= topleftx) && (x <= topleftx + length) &&
                (y >= toplefty) && (y <= toplefty + depth));

}

PointInPlanePolygon(pt, ptlist, ptcount)
point pt, ptlist[];
int ptcount;
{
	register int i;
	char inalign;

	for(i = 0;i < ptcount - 1;i++)
		if(!PtLeftOfRayInPlane(pt, ptlist[i], ptlist[i+1], &inalign))
			return(FALSE);
	if(!PtLeftOfRayInPlane(pt, ptlist[i], ptlist[0], &inalign))
		return(FALSE);
	return(TRUE);

}

void
QuickSortInt(left, right)
int *left, *right;
{
	int pivot, *p, *PartitionInt();
	yes_no FindPivotInt();

	if(FindPivotInt(left, right, &pivot) == yes)
	{
		p = PartitionInt(left, right, pivot);
		QuickSortInt(left, p - 1);
		QuickSortInt(p, right);
	}

}

yes_no
FindPivotInt(left, right, pivotptr)
int *left, *right, *pivotptr;
{
	int a, b, c, *p;

	a = *left;
	b = *(left + (right - left) / 2);
	c = *right;
	order3(a, b, c);
	if(a < b)
	{
		*pivotptr = b;
		return(yes);
	}
	if(b < c)
	{
		*pivotptr = c;
		return(yes);
	}
	for(p = left + 1;p <= right; ++p)
	{
		if(*p != *left)
		{
			*pivotptr = (*p < *left) ? *left : *p;
			return(yes);
		}
	}
	return(no);

}

int *
PartitionInt(left, right, pivot)
int *left, *right, pivot;
{
	while(left <= right)
	{
		while(*left < pivot)
			++left;
		while(*right >= pivot)
			--right;
		if(left < right)
		{
			swap(*left, *right);
			++left;
			--right;
		}
	}
	return(left);

}

void
QuickSortFloat(left, right)
float *left, *right;
{
	float pivot, *p, *PartitionFloat();
	yes_no FindPivotFloat();

	if(FindPivotFloat(left, right, &pivot) == yes)
	{
		p = PartitionFloat(left, right, pivot);
		QuickSortFloat(left, p - 1);
		QuickSortFloat(p, right);
	}

}

yes_no
FindPivotFloat(left, right, pivotptr)
float *left, *right, *pivotptr;
{
	float a, b, c, *p;

	a = *left;
	b = *(left + (right - left) / 2);
	c = *right;
	order3f(&a, &b, &c);
	if(a < b)
	{
		*pivotptr = b;
		return(yes);
	}
	if(b < c)
	{
		*pivotptr = c;
		return(yes);
	}
	for(p = left + 1;p <= right; ++p)
	{
		if(*p != *left)
		{
			*pivotptr = (*p < *left) ? *left : *p;
			return(yes);
		}
	}
	return(no);

}

float *
PartitionFloat(left, right, pivot)
float *left, *right, pivot;
{
	while(left <= right)
	{
		while(*left < pivot)
			++left;
		while(*right >= pivot)
			--right;
		if(left < right)
		{
			swap(*left, *right);
			++left;
			--right;
		}
	}
	return(left);

}

void
QuickSortMatrix(left, right, field, fieldcount)
matrixptr left, right;
int field, fieldcount;
{
	matrixptr p, PartitionMatrix();
	float pivot;
	yes_no FindPivotMatrix();

	if(FindPivotMatrix(left, right, &pivot, field) == yes)
	{
		p = PartitionMatrix(left, right, pivot, field, fieldcount);
		QuickSortMatrix(left, p - 1, field, fieldcount);
		QuickSortMatrix(p, right, field, fieldcount);
	}

}

yes_no
FindPivotMatrix(left, right, pivotptr, field)
matrixptr left, right;
float *pivotptr;
int field;
{
	matrixptr p;
	float a, b, c;

	a = *(*left + field);
	b = *(*(left + (right - left) / 2) + field);
	c = *(*right + field);
	order3f(&a, &b, &c);
	if(a < b)
	{
		*pivotptr = b;
		return(yes);
	}
	if(b < c)
	{
		*pivotptr = c;
		return(yes);
	}
	for(p = left + 1;p <= right; ++p)
	{
		if(*(*p + field) != *(*left + field))
		{
			*pivotptr = (*(*p + field) < *(*left + field)) ?
				*(*left + field) : *(*p + field);
			return(yes);
		}
	}
	return(no);

}

matrixptr
PartitionMatrix(left, right, pivot, field, fieldcount)
matrixptr left, right;
float pivot;
int field, fieldcount;
{
	register int i;
	float buffer[100];

	while(left <= right)
	{
		while(*(*left + field) < pivot)
			++left;
		while(*(*right + field) >= pivot)
			--right;
		if(left < right)
		{
			for(i = 0;i < fieldcount;i++)
			{
				buffer[i] = *(*left + i);
				*(*left + i) = *(*right + i);
			}
			for(i = 0;i < fieldcount;i++)
			{
				*(*right + i) = buffer[i];
			}
			++left;
			--right;
		}
	}
	return(left);

}

void
QuickSortIntMatrix(left, right, field, fieldcount)
intmatrixptr left, right;
int field, fieldcount;
{
	intmatrixptr p, PartitionIntMatrix();
	int pivot;
	yes_no FindPivotIntMatrix();
	void QuickSortIntMatrix();

	if(FindPivotIntMatrix(left, right, &pivot, field) == yes)
	{
		p = PartitionIntMatrix(left, right, pivot, field, fieldcount);
		QuickSortIntMatrix(left, p - 1, field, fieldcount);
		QuickSortIntMatrix(p, right, field, fieldcount);
	}

}

void
QuickSortShortMatrix(left, right, field, fieldcount)
short **left, **right;
int field, fieldcount;
{
	short **p, **PartitionShortMatrix();
	short pivot;
	yes_no FindPivotShortMatrix();
	void QuickSortShortMatrix();

	if(FindPivotShortMatrix(left, right, &pivot, field) == yes)
	{
		p = PartitionShortMatrix(left, right, pivot, field, fieldcount);
		QuickSortShortMatrix(left, p - 1, field, fieldcount);
		QuickSortShortMatrix(p, right, field, fieldcount);
	}

}

yes_no
FindPivotShortMatrix(left, right, pivotptr, field)
short **left, **right;
short *pivotptr;
int field;
{
	register short **p, a, b, c;

	a = *(*left + field);
	b = *(*(left + (right - left) / 2) + field);
	c = *(*right + field);
	order3(a, b, c);
	if(a < b)
	{
		*pivotptr = b;
		return(yes);
	}
	if(b < c)
	{
		*pivotptr = c;
		return(yes);
	}
	for(p = left + 1;p <= right; ++p)
	{
		if(*(*p + field) != *(*left + field))
		{
			*pivotptr = (*(*p + field) < *(*left + field)) ?
				*(*left + field) : *(*p + field);
			return(yes);
		}
	}
	return(no);

}

yes_no
FindPivotIntMatrix(left, right, pivotptr, field)
intmatrixptr left, right;
int *pivotptr;
int field;
{
	int a, b, c;
	register intmatrixptr p;

	a = *(*left + field);
	b = *(*(left + (right - left) / 2) + field);
	c = *(*right + field);
	order3(a, b, c);
	if(a < b)
	{
		*pivotptr = b;
		return(yes);
	}
	if(b < c)
	{
		*pivotptr = c;
		return(yes);
	}
	for(p = left + 1;p <= right; ++p)
	{
		if(*(*p + field) != *(*left + field))
		{
			*pivotptr = (*(*p + field) < *(*left + field)) ?
				*(*left + field) : *(*p + field);
			return(yes);
		}
	}
	return(no);

}

intmatrixptr
PartitionIntMatrix(left, right, pivot, field, fieldcount)
intmatrixptr left, right;
int pivot;
int field, fieldcount;
{
	register int i;
	int buffer[100];

	while(left <= right)
	{
		while(*(*left + field) < pivot)
			++left;
		while(*(*right + field) >= pivot)
			--right;
		if(left < right)
		{
			for(i = 0;i < fieldcount;i++)
			{
				buffer[i] = *(*left + i);
				*(*left + i) = *(*right + i);
			}
			for(i = 0;i < fieldcount;i++)
			{
				*(*right + i) = buffer[i];
			}
			++left;
			--right;
		}
	}
	return(left);

}

short **
PartitionShortMatrix(left, right, pivot, field, fieldcount)
short **left, **right;
short pivot;
int field, fieldcount;
{
	register int i;
	short buffer[100];

	while(left <= right)
	{
		while(*(*left + field) < pivot)
			++left;
		while(*(*right + field) >= pivot)
			--right;
		if(left < right)
		{
			for(i = 0;i < fieldcount;i++)
			{
				buffer[i] = *(*left + i);
				*(*left + i) = *(*right + i);
			}
			for(i = 0;i < fieldcount;i++)
			{
				*(*right + i) = buffer[i];
			}
			++left;
			--right;
		}
	}
	return(left);

}

// QuickSortIntMatrixAllFields not tested for fields > 2 //

QuickSortIntMatrixAllFields(left, right, field, fieldcount)
intmatrixptr left, right;
int field, fieldcount;
{
	register int val1, fieldindex;
	register intmatrixptr subleft, subright;
	void QuickSortIntMatrix();

	QuickSortIntMatrix(left, right, 0, fieldcount);
	for(fieldindex = 1;fieldindex < fieldcount;fieldindex++)
	{
		subleft = subright = left;
		val1 = subleft[fieldindex-1][0];
		while(TRUE)
		{
			if(val1 == subright[fieldindex-1][0])
			{
				subright++;
				continue;
			}
			if(subleft != subright-1)
				QuickSortIntMatrix(subleft, subright-1, fieldindex, fieldcount);
			subleft = subright;
			val1 = subleft[fieldindex-1][0];
			if(subright >= right)
				break;
			if(subleft >= right)
				break;
		}
	}

}

//
** GetPerpendicularPointAtT() returns the 2 points (fppt & sppt) which
** are perpendicular to a new point (fppt) that is a t value from fpt to spt
** a distance length from the new point (fppt). mode is TRUE or FALSE depending
** on whether sppt is on positive or negative side of line starting
** from fpt -> spt.
//

GetPerpendicularPointAtT(fpt, spt, t, fppt, sppt, length, mode)
point fpt, spt;
float t;
point fppt, sppt;
float length;
int mode;
{
	float deltax, deltay, multlength;
	double sqrt();

	GetNewPoint(fppt);
	GetVectorLineAtT(fpt, spt, fppt, t);
	deltax = spt[XCoor] - fpt[XCoor];
	deltay = spt[YCoor] - fpt[YCoor];
	multlength = length/(float)sqrt(deltax*deltax + deltay*deltay);
	deltax *= multlength;
	deltay *= multlength;
	if(mode)
		SetPoint(sppt, fppt[XCoor] + deltay, fppt[YCoor] - deltax, 0.0);
	else
		SetPoint(sppt, fppt[XCoor] - deltay, fppt[YCoor] + deltax, 0.0);

}

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

GetPerpendicularPointsAtPoint(fpt, spt, tpt, fppt, sppt, length, mode)
point fpt, spt, tpt;
point fppt, sppt;
float length;
int mode;
{
	float deltax, deltay, multlength;
	double sqrt();

	CopyPoint(fppt, tpt);
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

GetPerpLinePtFromOffSetPt(tailpt, headpt, linept, offsetpt)
point tailpt, headpt, linept, offsetpt;
{
	float DistPtToLine(), t, dist;

	dist = DistPtToLine(tailpt, headpt, offsetpt, &t);
	GetVectorLineAtT(tailpt, headpt, linept, t);
}

GetGCD(top, bottom)
int *top, *bottom;
{
	int divisor;
	int largestdiv = 0, abstop, absbottom, largest;
	double fabs();

	abstop = irint(fabs((double)*top));
	absbottom = irint(fabs((double)*bottom));
	largest = abstop > absbottom ? abstop : absbottom;

	for(divisor = 2; divisor <= largest;divisor++)
	{
		if((abstop % divisor == 0) && (absbottom % divisor == 0))
			largestdiv = divisor;
	}

	if(largestdiv > 0)
	{
		*top /= largestdiv;
		*bottom /= largestdiv;
	}

}

QuickSortIntXYListYDominant(left, right)
intmatrixptr left, right;
{
	register int val2;
	register intmatrixptr subleft, subright;
	void QuickSortIntMatrix();

	QuickSortIntMatrix(left, right, 1, 2);
	subleft = subright = left;
	val2 = subleft[0][1];
	while(TRUE)
	{
		if(val2 == subright[0][1])
		{
			subright++;
			continue;
		}
		if(subleft != subright-1)
			QuickSortIntMatrix(subleft, subright-1, 0, 2);
		subleft = subright;
		val2 = subleft[0][1];
		if(subright >= right)
			break;
		if(subleft >= right)
			break;
	}

}

GetBresenhamPolyGon(polylist, polyindices, nodecount, result)
int polylist[][2], polyindices[], nodecount;
intmatrixptr result;
{
	register int nodeindex = 0, resultindex = 0;
	int GetBresenhamMatrixLine();

	polyindices[0] = 0;
	for(nodeindex = 0;nodeindex < nodecount-1;nodeindex++)
	{
		resultindex += (GetBresenhamMatrixLine(
			polylist[nodeindex][0],
			polylist[nodeindex][1],
			polylist[nodeindex+1][0],
			polylist[nodeindex+1][1],
			result+resultindex) - 1);
		polyindices[nodeindex+1] = resultindex;
	}
	resultindex += GetBresenhamMatrixLine(
		polylist[nodeindex][0],
		polylist[nodeindex][1],
		polylist[0][0],
		polylist[0][1],
		result+resultindex);
	return(resultindex);

}

int
GetBresenhamLine(x1, y1, x2, y2, result)
int x1, y1, x2, y2, result[][2];
{
	register int x, y, deltax, deltay, s1, s2, tmp, interchanged, error, i;

	if((x1 == x2) && (y1 == y2))
	{
		result[0][0] = x1;
		result[0][1] = y1;
		return(1);
	}
	x = x1;
	y = y1;
	deltax = abs(x2 - x1);
	deltay = abs(y2 - y1);
	s1 = Sign(x2 - x1);
	s2 = Sign(y2 - y1);
	if(deltay > deltax)
	{
		tmp = deltax;
		deltax = deltay;
		deltay = tmp;
		interchanged = TRUE;
	}
	else
	{
		interchanged = FALSE;
	}
	error = (2 * deltay) - deltax;
	for(i = 0;i <= deltax;i++)
	{
		result[i][0] = x;
		result[i][1] = y;
		while(error >= 0)
		{
			if(interchanged)
				x += s1;
			else
				y += s2;
			error -= 2 * deltax;
		}
		if(interchanged)
			y += s2;
		else
			x += s1;
		error += 2 * deltay;
	}
	return(deltax+1);

}

int
GetBresenhamMatrixLine(x1, y1, x2, y2, result)
int x1, y1, x2, y2;
intmatrixptr result;
{
	register int x, y, deltax, deltay, s1, s2, tmp, interchanged, error, i;

	if((x1 == x2) && (y1 == y2))
	{
		result[0][0] = x1;
		result[0][1] = y1;
		return(1);
	}
	x = x1;
	y = y1;
	deltax = abs(x2 - x1);
	deltay = abs(y2 - y1);
	s1 = Sign(x2 - x1);
	s2 = Sign(y2 - y1);
	if(deltay > deltax)
	{
		tmp = deltax;
		deltax = deltay;
		deltay = tmp;
		interchanged = TRUE;
	}
	else
	{
		interchanged = FALSE;
	}
	error = (2 * deltay) - deltax;
	for(i = 0;i <= deltax;i++)
	{
		result[i][0] = x;
		result[i][1] = y;
		while(error >= 0)
		{
			if(interchanged)
				x += s1;
			else
				y += s2;
			error -= 2 * deltax;
		}
		if(interchanged)
			y += s2;
		else
			x += s1;
		error += 2 * deltay;
	}
	return(deltax+1);

}

int
GetBresenhamCircle(centerx, centery, radius, quarterindex, result)
int centerx, centery, radius, *quarterindex, result[][2];
{
	register int X, Y, delta, limit, resultindex, i, current;

	Y = 0;
	X = radius;
	delta = 2 * (1 - radius);
	limit = 0;
	resultindex = 0;
	while(TRUE)
	{
		result[resultindex][XCoor] = X;
		result[resultindex][YCoor] = Y;
		resultindex++;
		if(X <= limit)
		{
			// first fill in other quadrants before returning //
			current = resultindex;
			*quarterindex = resultindex - 1;
			for(i = current - 2;i >= 0;i--)
			{
				result[resultindex][XCoor] = -result[i][XCoor];
				result[resultindex][YCoor] = result[i][YCoor];
				resultindex++;
			}
			current = resultindex;
			for(i = current - 2;i > 0;i--)
			{
				result[resultindex][XCoor] = result[i][XCoor];
				result[resultindex][YCoor] = -result[i][YCoor];
				resultindex++;
			}
			result[resultindex][XCoor] = result[0][XCoor];
			result[resultindex][YCoor] = result[0][YCoor];
			resultindex++;
			for(i = 0;i < resultindex;i++)
			{
				result[i][XCoor] += centerx;
				result[i][YCoor] += centery;
			}
			return(resultindex);
		}
		if(delta < 0)
		{
			if((2*delta + 2*X - 1) <= 0)
			{
				Y++;
				delta += 2*Y + 1;
			}
			else
			{
				Y++;
				X--;
				delta += 2*Y - 2*X + 2;
			}
		}
		else if(delta > 0)
		{
			if((2*delta - 2*Y - 1) <= 0)
			{
				Y++;
				X--;
				delta += 2*Y - 2*X + 2;
			}
			else
			{
				X--;
				delta += -2*X + 1;
			}
		}
		else
		{
			Y++;
			X--;
			delta += 2*Y - 2*X + 2;
		}
	}

}

GetPointOnBresenhamCircle(bresx, bresy, angle, resultcount, result)
int *bresx, *bresy;
float angle;
int resultcount, result[][2];
{
	int resultindex;

	resultindex = irint(((float)(resultcount - 1) * angle) / 360.0);
	*bresx = result[resultindex][XCoor];
	*bresy = result[resultindex][YCoor];
}

Sign(number)
int number;
{
	if(number < 0)
		return(-1);
	if(number == 0)
		return(0);
	return(1);

}

GetCoefficients(lineseg)
struct LineInformation *lineseg;
{
	float X1,Y1,X2,Y2;

	X1 = lineseg->fpt[XCoor];
	Y1 = lineseg->fpt[YCoor];
	X2 = lineseg->spt[XCoor];
	Y2 = lineseg->spt[YCoor];
	lineseg->Coefficient.ACoeff = Y1 - Y2;
	lineseg->Coefficient.BCoeff = X2 - X1;
	lineseg->Coefficient.CCoeff = X1*Y2 - X2*Y1;

}

float
Largest3FAbs(x, y, z)
float x, y, z;
{
	float fx, fy, fz;
	double fabs();

	fx = (float)fabs((double)x);
	fy = (float)fabs((double)y);
	fz = (float)fabs((double)z);
	if((fx >= fy) && (fx >= fz))
		return(fx);
	if((fy >= fx) && (fy >= fz))
		return(fy);
	if((fz >= fx) && (fz >= fy))
		return(fz);
	return(fz);

}

float
Largest4FAbs(w, x, y, z)
float w, x, y, z;
{
	float fw, fx, fy, fz;
	double fabs();

	fw = (float)fabs((double)w);
	fx = (float)fabs((double)x);
	fy = (float)fabs((double)y);
	fz = (float)fabs((double)z);
	if((fw >= fx) && (fw >= fy) && (fw >= fz))
		return(fw);
	if((fx >= fw) && (fx >= fy) && (fx >= fz))
		return(fx);
	if((fy >= fw) && (fy >= fx) && (fy >= fz))
		return(fy);
	if((fz >= fw) && (fz >= fx) && (fz >= fz))
		return(fz);
	return(fw);

}

int
GetLineIntersectInPlane(tail1x, tail1y, head1x, head1y, u, tail2x, tail2y, head2x, head2y, v, intersectx, intersecty)
float tail1x, tail1y, head1x, head1y, *u, tail2x, tail2y, head2x, head2y, *v, *intersectx, *intersecty;
{
	register float tmp, tmpu, tmpv;

	tmp = ((head2y - tail2y)*(head1x - tail1x) - (head2x - tail2x)*(head1y - tail1y));
	if(tmp == 0.0)
		return(FALSE);
	tmpv = ((tail2x - tail1x)*(head1y - tail1y) - (tail2y - tail1y)*(head1x - tail1x)) / tmp;
	tmp = (head1x - tail1x)*(head2y - tail2y) - (head2x - tail2x)*(head1y - tail1y);
	if(tmp == 0.0)
		return(FALSE);
	tmpu = ((tail2x - tail1x)*(head2y - tail2y) + (head2x - tail2x)*(tail1y - tail2y))/tmp;
	*intersectx = tail2x + tmpv*(head2x - tail2x);
	*intersecty = tail2y + tmpv*(head2y - tail2y);
	*u = tmpu;
	*v = tmpv;
	return(TRUE);
}

//
**
** GetRayPolygonIntersectInPlane() returns the intersection of a ray with a
** polygon in the XYPlane. polygonlist must be connected, and last point will
** connect up with first point.
**
//

int
GetRayPolygonIntersectInPlane(rayfptx, rayfpty, raysptx, rayspty, polylist, intersectx, intersecty, count)
float rayfptx, rayfpty, raysptx, rayspty, polylist[][2], *intersectx, *intersecty;
int count;
{
	register int i, j;
	int testlist[20], nointersect;
	float ulist[20], vlist[20], xlist[20], ylist[20];

	for(i = 0;i < count - 1;i++)
	{
	testlist[i] =
	GetLineIntersectInPlane(rayfptx, rayfpty, raysptx, rayspty, &ulist[i],
	polylist[i][0], polylist[i][1], polylist[i+1][0], polylist[i+1][1],
	&vlist[i], &xlist[i], &ylist[i]);
	}
	testlist[i] =
	GetLineIntersectInPlane(rayfptx, rayfpty, raysptx, rayspty, &ulist[i],
	polylist[i][0], polylist[i][1], polylist[0][0], polylist[0][1],
	&vlist[i], &xlist[i], &ylist[i]);

	for(i = 0;i < count;i++)
	{
		if(ulist[i] < 0.0)
		{
			ulist[i] = HUGE_VAL;
			testlist[i] = FALSE;
		}
		if(ulist[i] > 1.0)
			testlist[i] = FALSE;
	}

	for(i = 0;i < count;i++)
	{
		if(vlist[i] < 0.0)
		{
			vlist[i] = HUGE_VAL;
			testlist[i] = FALSE;
		}
		if(vlist[i] > 1.0)
			testlist[i] = FALSE;
	}

	for(i = 0;i < count;i++)
	{
		if((testlist[i] == TRUE) && (ulist[i] >= 0.0) &&
			(ulist[i] <= 1.0) && (vlist[i] >= 0.0) &&
			(vlist[i] <= 1.0))
		{
			nointersect = FALSE;
			for(j = 0;j < count;j++)
			{
				if(!testlist[j])
					continue;
				if(ulist[i] > (ulist[j] + 0.001))
				{
					nointersect = TRUE;
					break;
				}
			}
			if(!nointersect)
			{
				*intersectx = xlist[i];
				*intersecty = ylist[i];
				return(TRUE);
			}
		}
	}
	return(FALSE);

}

float
SpiralXPt(t, afact, bfact, tfact)
float t, afact, bfact, tfact;
{
	double cos(), exp();

	return(afact * (float)exp((double)(bfact * (t/tfact))) * (float)cos((double)t));

}

float
SpiralYPt(t, afact, bfact, tfact)
float t, afact, bfact, tfact;
{
	double sin(), exp();

	return(afact * (float)exp((double)(bfact * (t/tfact))) * sin((double)t));

}

float
LogForBase(base, arg)
int base;
float arg;
{
	double log();

	if ((arg > 0.0) && (base > 0))
		return((float)(log((double)arg) / log((double)base)));
	return(0.0);

}

//*********************** BIT-FLIP && COLOR STUFF ************************** //

static colortype currentcolorval = MINCOLORVAL;

colortype
CurrentColorVal()
{
	return(currentcolorval);

}

SetCurrentColorVal(colorval)
colortype colorval;
{
	currentcolorval = colorval;

}

CurrentGrayColorIndex()
{
	float ColorValToRedNormal();
	colortype CurrentColorVal();

	return(NormalToColorIndex(ColorValToRedNormal(CurrentColorVal())));

}

colortype
ColorComplement(color)
colortype color;
{
	return(0x00ffffff - color);
}

// Add 2 colors in additive color model //
colortype
AddColorsInCMY(col0, col1)
colortype col0, col1;
{
    colortype comp0, comp1, red0, green0, blue0, red1, green1, blue1,
        addred, addgreen, addblue, RedPart(), GreenPart(), BluePart(),
        RGBValsToColorVal();
 
    comp0 = ColorComplement(col0);
    comp1 = ColorComplement(col1);
    red0 = RedPart(comp0);
    green0 = GreenPart(comp0);
    blue0 = BluePart(comp0);
    red1 = RedPart(comp1);
    green1 = GreenPart(comp1);
    blue1 = BluePart(comp1);
    addred = red0 + red1;
    if(addred >= 0xff)
        addred = 0xff;
    addgreen = green0 + green1;
    if(addgreen >= 0xff)
        addgreen = 0xff;
    addblue = blue0 + blue1;
    if(addblue >= 0xff)
        addblue = 0xff;
    return(ColorComplement(RGBValsToColorVal(addred, addgreen, addblue)));
}

colortype
RedPart(hex)
colortype hex;
{
	return((colortype)(((hex & REDPART) >> 16) & 0xff));

}

colortype
GreenPart(hex)
colortype hex;
{
	return((colortype)(((hex & GREENPART) >> 8) & 0xff));

}

colortype
BluePart(hex)
colortype hex;
{
	return((colortype)((hex & BLUEPART) & 0xff));

}

rgbtype
NormalToCharRGB(normalval)
float normalval;
{
	float ROneToROneMap();

	return(0xff & irint(ROneToROneMap(normalval, 0.0, 1.0, 0.0, 255.0)));

}

colortype
NormalToColorVal(normalval)
float normalval;
{
	float ROneToROneMap();
	colortype RGBToColorVal();

	return(RGBToColorVal((colortype)irint(ROneToROneMap(normalval, 0.0, 1.0, 0.0, 255.0))));

}

NormalToColorIndex(normalval)
float normalval;
{
	float ROneToROneMap();

	return(irint(ROneToROneMap(normalval, 0.0, 1.0, 0.0, 255.0)));

}

colortype
RGBToGrayColorVal(rgbval)
rgbtype rgbval;
{
	colortype hex;

	hex = rgbval & 0xff;
	return(hex + (hex << 8) + (hex << 16));

}

colortype
RGBToColorVal(hex)
colortype hex;
{
	return((hex & 0x0000ff) + (hex << 8) + (hex << 16));

}

rgbtype
GrayValToRGB(grayval)
float grayval;
{
	float ROneToROneMap();

	return((rgbtype)irint(ROneToROneMap(grayval, 0.0, 1.0, 0.0, 255.0)));

}

float
RGBToGrayVal(rgbval)
rgbtype rgbval;
{
	float ROneToROneMap();

	return(ROneToROneMap((float)(rgbval & 0xff), 0.0, 255.0, 0.0, 1.0));

}

float
ColorValToGrayVal(hexcolor)
colortype hexcolor;
{
	float ROneToROneMap();
	colortype BluePart();

	return(ROneToROneMap((float)(BluePart(hexcolor) & 0xff), 0.0, 255.0, 0.0, 1.0));

}

float
ColorValToRedNormal(hexcolor)
colortype hexcolor;
{
	float ROneToROneMap();
	colortype RedPart();

	return(ROneToROneMap((float)(RedPart(hexcolor)), 0.0, 255.0, 0.0, 1.0));

}

float
ColorValToGreenNormal(hexcolor)
colortype hexcolor;
{
	float ROneToROneMap();
	colortype GreenPart();

	return(ROneToROneMap((float)(GreenPart(hexcolor)), 0.0, 255.0, 0.0, 1.0));

}

float
ColorValToBlueNormal(hexcolor)
colortype hexcolor;
{
	float ROneToROneMap();
	colortype BluePart();

	return(ROneToROneMap((float)(BluePart(hexcolor)), 0.0, 255.0, 0.0, 1.0));

}

colortype
RGBValsToColorVal(redval, greenval, blueval)
colortype redval, greenval, blueval;
{
	colortype RGBToRedColorVal(), RGBToGreenColorVal(), RGBToBlueColorVal();

	return(RGBToRedColorVal(blueval) + RGBToGreenColorVal(greenval) + RGBToBlueColorVal(redval));

}

colortype
RGBToRedColorVal(hex)
colortype hex;
{
	return(hex & 0x0000ff);

}

colortype
RGBToGreenColorVal(hex)
colortype hex;
{
	return(hex << 8);

}

colortype
RGBToBlueColorVal(hex)
colortype hex;
{
	return(hex << 16);

}

RGBValsToColorString(rval, gval, bval, colorstring)
rgbtype rval, gval, bval;
char *colorstring;
{
	sprintf(colorstring, "%#.2x%.2x%.2x", rval, gval, bval);

}

RGBValsToXColorString(rval, gval, bval, colorstring)
colortype rval, gval, bval;
char *colorstring;
{
	sprintf(colorstring, "#%.2x%.2x%.2x", rval, gval, bval);

}

colortype
ScaleBits8To16(val)
colortype val;
{
	return(0xffff & ((0xff & val) + ((0xff & val) << 8)));

}

Bit(byte, bit)
char byte;
int bit;
{
	return((byte & (1 << bit)) > 0);
}

BitOn(byte, bit)
char *byte;
int bit;
{
	*byte |= (1 << bit);
}

BitOff(byte, bit)
char *byte;
int bit;
{
	*byte &= ~(1 << bit);
}

UnsignedBit(byte, bit)
unsigned int byte;
int bit;
{
	return((byte & (1 << bit)) > 0);
}

UnsignedBitOn(byte, bit)
unsigned *byte;
int bit;
{
	*byte |= (1 << bit);
}

UnsignedBitOff(byte, bit)
unsigned *byte;
int bit;
{
	*byte &= ~(1 << bit);
}

ishexchar(hexchar)
char hexchar;
{
	switch(hexchar)
	{
	  case '0' :
	  case '1' :
	  case '2' :
	  case '3' :
	  case '4' :
	  case '5' :
	  case '6' :
	  case '7' :
	  case '8' :
	  case '9' :
	  case 'a' :
	  case 'b' :
	  case 'c' :
	  case 'd' :
	  case 'e' :
	  case 'f' :
		return(TRUE);
	  default :
		return(FALSE);
	}

}

SetUnsignedIntVal(strval, strvaltype, mode)
unsigned int *strval;
int strvaltype;
char mode;
{
        if(mode)
                UnsignedBitOn(strval, strvaltype);
        else
                UnsignedBitOff(strval, strvaltype);
}

GetUnsignedIntVal(strval, strvaltype)
unsigned int strval;
int strvaltype;
{
        return(UnsignedBit(strval, strvaltype));
}


//*********************** END BIT-FLIP STUFF ******************************* //


printmat(m)
MATRIXTYPE m;
{
	int row, col;

	for(row = 0;row < m->rowsize;row++)
	{
		for(col = 0;col < m->columnsize;col++)
		{
			printf("%.2f ", m->rectarray[row][col]);
		}
		printf("\n");
	}
}

printpt(pt)
point pt;
{
	printf("%.2f %.2f %.2f %.2f\n", pt[0], pt[1], pt[2],pt[3]);
}

FlipOnOff(logicval)
int *logicval;
{
        if(*logicval)
                *logicval = FALSE;
        else
                *logicval = TRUE;
}

FlipCharOnOff(logicval)
char *logicval;
{
        if(*logicval)
                *logicval = FALSE;
        else
                *logicval = TRUE;
}

*/

class
JavaStack extends Vector
{
	public
	JavaStack()
	{
		this.clear();
	}

	public void
	clear()
	{
		removeAllElements();
	}
	 
	public void
	push(Object genericObj)
	{
		insertElementAt(genericObj, 0);
	}
	 
	public Object
	pop()
	{
		Object tmpObj = top();
		if(!isEmpty())
			removeElementAt(0);
		return(tmpObj);
	}
	 
	public Object
	top()
	{
		if(isEmpty())
			return(null);
		return((Object)elementAt(0));
	}
}
