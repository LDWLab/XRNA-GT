
package jmath;

/**
** BVector is a positional vector with origin at cartesian
** coordinate system origin.
*/

public class
BVector
{
	public BVector()
	throws Exception
	{
		this.setPositionVector(BMatrix.point());
	}

	public BVector(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.setPositionVector(BMatrix.point(xPos, yPos, zPos));
	}

	public BVector(BVector positionVector)
	throws Exception
	{
		this(positionVector.xCoor(), positionVector.yCoor(), positionVector.zCoor());
	}

	private BMatrix positionVector = null;

	public void
	setPositionVector(BMatrix positionVector)
	{
		this.positionVector = positionVector;
	}

	public BMatrix
	getPositionVector()
	{
		return (this.positionVector);
	}

	/*********** BVector methods ******************/

	public double
	xCoor()
	{
		return (this.getPositionVector().getPointX());
	}

	public double
	yCoor()
	{
		return (this.getPositionVector().getPointY());
	}

	public double
	zCoor()
	{
		return (this.getPositionVector().getPointZ());
	}

	public void
	transform(BMatrix transformMatrix)
	throws Exception
	{
		BMatrix.pointMatrixMult(this.getPositionVector(),
			transformMatrix);
	}

	public void
	copyPoint(BMatrix fromPt)
	throws Exception
	{
		this.getPositionVector().setPoint(
			fromPt.getPointX(),
			fromPt.getPointY(),
			fromPt.getPointZ());
	}

	public void
	copyPoint(BVector fromPt)
	throws Exception
	{
		this.getPositionVector().setPoint(
			fromPt.xCoor(),
			fromPt.yCoor(),
			fromPt.zCoor());
	}

	public void
	setPoint(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.getPositionVector().setPoint(xPos, yPos, zPos);
	}

	/**
	** norm is the length of position vector
	*/

	public double
	norm()
	throws Exception
	{
		double a = this.xCoor();
		double b = this.yCoor();
		double c = this.zCoor();

		return(Math.sqrt(a*a + b*b + c*c));
	}

	public void
	translate(double xPos, double yPos, double zPos)
	throws Exception
	{
		setPoint(xCoor() + xPos, yCoor() + yPos, zCoor() + zPos);
	}

	public void
	translate(BVector transPt)
	throws Exception
	{
		this.translate(transPt.xCoor(), transPt.yCoor(), transPt.zCoor());
	}

	/*
	** 2 position vectors, u,v, are equal if u.x==v.x  && u.y==v.y && u.z==v.z
	*/

	public boolean
	equals(double xPos, double yPos, double zPos)
	{
		return(
			(Math.round(MathDefines.PRECISION * this.xCoor()) ==
				Math.round(MathDefines.PRECISION * xPos)) &&
			(Math.round(MathDefines.PRECISION * this.yCoor()) ==
				Math.round(MathDefines.PRECISION * yPos)) &&
			(Math.round(MathDefines.PRECISION * this.zCoor()) ==
				Math.round(MathDefines.PRECISION * zPos)));
	}

	public boolean
	equals(BVector testPt)
	{
		return (this.equals(testPt.xCoor(), testPt.yCoor(), testPt.zCoor()));
	}

	public double
	unitVectorXCoor()
	throws Exception
	{
		return (this.xCoor()/this.norm());
	}

	public double
	unitVectorYCoor()
	throws Exception
	{
		return (this.yCoor()/this.norm());
	}

	public double
	unitVectorZCoor()
	throws Exception
	{
		return (this.zCoor()/this.norm());
	}

	public BVector
	unitVector()
	throws Exception
	{
		return (new BVector(
			this.unitVectorXCoor(),
			this.unitVectorYCoor(),
			this.unitVectorZCoor()));
	}

	public double
	scalarMultipleXCoor(double scalar)
	throws Exception
	{
		return (scalar * this.xCoor());
	}

	public double
	scalarMultipleYCoor(double scalar)
	throws Exception
	{
		return (scalar * this.yCoor());
	}

	public double
	scalarMultipleZCoor(double scalar)
	throws Exception
	{
		return (scalar * this.zCoor());
	}

	public BVector
	scalarMultiple(double scalar)
	throws Exception
	{
		return (new BVector(
			this.scalarMultipleXCoor(scalar),
			this.scalarMultipleYCoor(scalar),
			this.scalarMultipleZCoor(scalar)));
	}

	public double
	addResultXCoor(BVector toVector)
	throws Exception
	{
		return (toVector.xCoor() + this.xCoor());
	}

	public double
	addResultYCoor(BVector toVector)
	throws Exception
	{
		return (toVector.yCoor() + this.yCoor());
	}

	public double
	addResultZCoor(BVector toVector)
	throws Exception
	{
		return (toVector.zCoor() + this.zCoor());
	}

	public BVector
	addResult(BVector toVector)
	throws Exception
	{
		return (new BVector(
			this.addResultXCoor(toVector),
			this.addResultYCoor(toVector),
			this.addResultZCoor(toVector)));
	}


}
