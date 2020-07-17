
package jmath;

import java.util.Vector;

/**
** ray is defined by 3 position vectors, positionVector to show
** direction and length of ray, tailPt to define start
** of ray, headPt that defines the end of ray which
** is headPt - tailPt translated. Therefore:
** headPt = positionVector + tailPt
**
** resetting tailPt will not affect positionVector, but headPt needs
** to be refigured.
**
** currently just altering tailPt or postionVector and resetting headPt
** upon either alteration.
*/

public class
BRay
{
	public BRay()
	throws Exception
	{
		this.setHeadPt(new BVector());
		this.setTailPt(new BVector());
		this.setPositionVector(new BVector());
	}

	// make a new ray with headPt x,y,z values
	public BRay(double xPos, double yPos, double zPos)
	throws Exception
	{
		this();
		this.setHeadPt(xPos, yPos, zPos);
		this.resetPositionVector();
	}

	// make a new ray with headPt
	public BRay(BVector headPt)
	throws Exception
	{
		this();
		this.getHeadPt().copyPoint(headPt);
		this.resetPositionVector();
	}

	// make a new ray with just tailPt and headPt. refigures positionVector.
	public BRay(BVector tailPt, BVector headPt)
	throws Exception
	{
		this();
		this.getTailPt().copyPoint(tailPt);
		this.getHeadPt().copyPoint(headPt);
		this.resetPositionVector();
	}

	// make a new ray with just positionVector and tailPt x,y,z values
	public BRay(double tX, double tY, double tZ, double hX, double hY, double hZ)
		throws Exception
	{
		this(new BVector(tX, tY, tZ), new BVector(hX, hY, hZ));
	}

	private BVector tailPt = null;

	public void
	setTailPt(BVector tailPt)
	{
		this.tailPt = tailPt;
	}

	public void
	setTailPt(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.getTailPt().setPoint(xPos, yPos, zPos);
	}

	public BVector
	getTailPt()
	{
		return (this.tailPt);
	}

	private BVector headPt = null;

	public void
	setHeadPt(BVector headPt)
	throws Exception
	{
		this.headPt = headPt;
	}

	public void
	setHeadPt(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.getHeadPt().setPoint(xPos, yPos, zPos);
	}

	public BVector
	getHeadPt()
	{
		return (this.headPt);
	}

	// position vector is headPt translated to origin: headPt - tailPt
	private BVector positionVector = null;

	public void
	setPositionVector(BVector positionVector)
	throws Exception
	{
		this.positionVector = positionVector;
	}

	public void
	setPositionVector(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.getPositionVector().setPoint(xPos, yPos, zPos);
	}

	public BVector
	getPositionVector()
	{
		return (this.positionVector);
	}

	/**
	** sets tailpt headpt from params, resets position vec directly.
	*/

	public void
	setRay(double tX, double tY, double tZ, double hX, double hY, double hZ)
	throws Exception
	{
		this.setTailPt(tX, tY, tZ);
		this.setHeadPt(hX, hY, hZ);
		this.resetPositionVector();
	}

	private Vector tVals = null;

	public void
	setTVals(Vector tVals)
	{
		this.tVals = tVals;
	}

	public Vector
	getTVals()
	{
		return (this.tVals);
	}

	public void
	setNewTValue(double tVal)
	{
		if (this.tVals == null)
			this.tVals = new Vector();
		else
			this.tVals.clear();
		setNextTValue(tVal);
	}

	public void
	setNextTValue(double tVal)
	{
		if (this.tVals == null)
			this.tVals = new Vector();
		this.tVals.addElement(new Double(tVal));
	}

	/* Need to see if this makes sense for 3D
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
	*/

	/******* Vector methods **************/

	public double
	tailPtXCoor()
	{
		return (tailPt.xCoor());
	}

	public double
	tailPtYCoor()
	{
		return (tailPt.yCoor());
	}

	public double
	tailPtZCoor()
	{
		return (tailPt.zCoor());
	}

	public double
	headPtXCoor()
	{
		return (headPt.xCoor());
	}

	public double
	headPtYCoor()
	{
		return (headPt.yCoor());
	}

	public double
	headPtZCoor()
	{
		return (headPt.zCoor());
	}

	public double
	positionVectorXCoor()
	{
		return (positionVector.xCoor());
	}

	public double
	positionVectorYCoor()
	{
		return (positionVector.yCoor());
	}

	public double
	positionVectorZCoor()
	{
		return (positionVector.zCoor());
	}

	public double
	rayLength()
	throws Exception
	{
		return (this.getPositionVector().norm());
	}

	/**
	** just translate the tailPt. reset headPt. position vector remains
	** invariante under translation.
	*/

	public void
	translate(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.getTailPt().translate(xPos, yPos, zPos);
		resetHeadPt();
	}

	public void
	translate(BVector transPt)
	throws Exception
	{
		this.translate(transPt.xCoor(), transPt.yCoor(), transPt.zCoor());
	}

	public void
	changeLength(double length)
	throws Exception
	{
		resetPositionVector();
		length /= this.rayLength();
		setPositionVector(
			positionVectorXCoor() * length,
			positionVectorYCoor() * length,
			positionVectorZCoor() * length);
		// NOW need to map this back to ray
	}

	public void
	lengthenRay(double length)
	throws Exception
	{
		/* BUG: doesn't work yet
		double halfLength = length/2.0;
		debug("IN lengthenRay, halfLength: " + halfLength);
		debug("In lengthenRay, RL: " + rayLength());
		getPointAtT(getHeadPt(), getTValue(rayLength() + halfLength));
		getPointAtT(getTailPt(), getTValue(-halfLength));
		this.resetPositionVector();
		*/
	}

	public void
	lengthenHeadPt(double length)
	throws Exception
	{
		/* BUG: doesn't work yet
		getPointAtT(getHeadPt(), getTValue(rayLength() + length));
		*/
	}

	public void
	lengthenTailPt(double length)
	throws Exception
	{
		/* BUG: doesn't work yet
		getPointAtT(getTailPt(), getTValue(-length));
		*/
	}

	/**
	** Returns a t value for a vector given distance from
	** tailpt to headpt if t==0.0 is t value at tailpt, and t==1.0 is t
	** value at headpt.
	*/

	public double
	getTValue(double distance)
	throws Exception
	{
		return(distance / this.rayLength());
	}

	/**
	** return the positional vector that represents the midpt
	** between tailPt and headPt or ray.
	*/

	public void
	midPoint(BVector midPt)
	throws Exception
	{
        midPt.setPoint(
            (tailPtXCoor() + headPtXCoor())/2.0,
            (tailPtYCoor() + headPtYCoor())/2.0,
            (tailPtZCoor() + headPtZCoor())/2.0);
	}

	public BVector
	midPoint()
	throws Exception
	{
		BVector midPt = new BVector();
		this.midPoint(midPt);
		return (midPt);
	}

	/**
	** DistPtToLine() returns the distance from a point, pt, to the line
	** segment defined by tailPt, headPt. It also assigns to this.tVals
	** the t value needed to find the point on line that cooresponds to
	** the minimum distance found at this.tVals first cell.
	*/

	public double
	distPtToLine(BVector pt)
	{
		double x = 0.0, y = 0.0, z = 0.0, x1 = 0.0, y1 = 0.0, z1 = 0.0;
		double x0 = 0.0, y0 = 0.0, z0 = 0.0, a = 0.0, b = 0.0, c = 0.0;
		double x2 = 0.0, y2 = 0.0, z2 = 0.0;
		double t = 0.0;

		x0 = pt.xCoor();
		y0 = pt.yCoor();
		z0 = pt.zCoor();
		x1 = this.tailPtXCoor();
		y1 = tailPtYCoor();
		z1 = tailPtZCoor();
		x2 = headPtXCoor();
		y2 = headPtYCoor();
		z2 = headPtZCoor();
		
		a = x2 - x1;
		b = y2 - y1;
		c = z2 - z1;

		t = - (a*(x1 - x0) + b*(y1 - y0) + c*(z1 - z0))/ (a*a + b*b + c*c);
		setNewTValue(t);
		x = x1 + a*t;
		y = y1 + b*t;
		z = z1 + c*t;

		return (Math.sqrt(
			(x - x0)*(x - x0) + (y - y0)*(y - y0) + (z - z0)*(z - z0)));
	}

	/**
	** Sets a new point, on this vector, a multiple of t times away
	** from tail.
	*/

	public void
	getPointAtT(BVector newPt, double t)
	throws Exception
	{
		if ((this.tailPt == null) || (this.headPt == null) || (newPt == null))
			throw new Exception("BVector is null in BRay.getPointAtT");
		newPt.setPoint(
			tailPtXCoor() +
				(t * (headPtXCoor() - tailPtXCoor())),
			tailPtYCoor() +
				(t * (headPtYCoor() - tailPtYCoor())),
			tailPtZCoor() +
				(t * (headPtZCoor() - tailPtZCoor())));
	}

	public BVector
	getPointAtT(double t)
	throws Exception
	{
		BVector newPt = new BVector();
		getPointAtT(newPt, t);
		return(newPt);
	}

	/**
	** Sets positionVector to headPt - tailPt so shifted to origin.
	**
	** NEED a routine that maps any change to a position vector (or
	** a unit vector) back to ray.
	*/

	public void
	resetPositionVector()
	throws Exception
	{
		setPositionVector(
			headPtXCoor() - tailPtXCoor(),
			headPtYCoor() - tailPtYCoor(),
			headPtZCoor() - tailPtZCoor());
	}

	/**
	** Sets headPt to positionVector + tailPt so shifts headPt
	** to tailPt the same direction and magnitued as positionVector.
	*/

	public void
	resetHeadPt()
	throws Exception
	{
		setHeadPt(
			positionVectorXCoor() + tailPtXCoor(),
			positionVectorYCoor() + tailPtYCoor(),
			positionVectorZCoor() + tailPtZCoor());
	}

	private void
	debug(String s)
	{
		System.out.println(s);
	}

}
