
package jmath;

/**
** A plane srf will be defined by 2 position vectors, u,v, and
** an origin of the srf, srfOrigin. The u, v, vectors are invariant
** under a translation of srfOrigin.
*/

public class
BPlaneSrf
extends BSurface
{
	public
	BPlaneSrf()
	throws Exception
	{
		super();
		setCrossProduct(new BVector());
		this.setUVec(new BVector());
		this.setVVec(new BVector());
	}

	/*
	** Create a plane surface at origin
	*/

	public
	BPlaneSrf(BVector uVec, BVector vVec)
	throws Exception
	{
		super();
		setCrossProduct(new BVector());
		this.setUVec(uVec);
		this.setVVec(vVec);
		if (!isValidPlaneSrf())
			throw new Exception("Invalid PlaneSrf in BPlaneSrf constructor");
	}

	/*
	** Create a plane surface positioned at vecTailPt
	*/

	public
	BPlaneSrf(BVector vecTailPt, BVector uVecHeadPt,
		BVector vVecHeadPt)
	throws Exception
	{
		this(uVecHeadPt, vVecHeadPt);
		this.getSrfOrigin().setPoint(vecTailPt.xCoor(), vecTailPt.yCoor(), vecTailPt.zCoor());

		if (!isValidPlaneSrf())
			throw new Exception("Invalid PlaneSrf in BPlaneSrf constructor");
	}

	/*
	public
	BPlaneSrf(double a, double b, double c, double d)
	{
		this.setACoeff(a);
		this.setBCoeff(b);
		this.setCCoeff(c);
		this.setDCoeff(d);
	}
	*/

	private BVector uVec = null;

	public void
	setUVec(BVector uVec)
	{
        this.uVec = uVec;
	}

	public BVector
	getUVec()
	{
        return (this.uVec);
	}

	private BVector vVec = null;

	public void
	setVVec(BVector vVec)
	{
        this.vVec = vVec;
	}

	public BVector
	getVVec()
	{
        return (this.vVec);
	}

	private BVector crossProduct = null;

	public void
	setCrossProduct(BVector crossProduct)
	{
		this.crossProduct = crossProduct;
	}

	public BVector
	getCrossProduct()
	{
		return (this.crossProduct);
	}

	private double aCoeff = 0.0;

	public void
	setACoeff(double aCoeff)
	{
		this.aCoeff = aCoeff;
	}

	public double
	getACoeff()
	{
		return (this.aCoeff);
	}

	private double bCoeff = 0.0;

	public void
	setBCoeff(double bCoeff)
	{
		this.bCoeff = bCoeff;
	}

	public double
	getBCoeff()
	{
		return (this.bCoeff);
	}

	private double cCoeff = 0.0;

	public void
	setCCoeff(double cCoeff)
	{
		this.cCoeff = cCoeff;
	}

	public double
	getCCoeff()
	{
		return (this.cCoeff);
	}

	private double dCoeff = 0.0;

	public void
	setDCoeff(double dCoeff)
	{
		this.dCoeff = dCoeff;
	}

	public double
	getDCoeff()
	{
		return (this.dCoeff);
	}

	public boolean
	isValidPlaneSrf()
	{
		return ((uVec != null) && (vVec != null));
	}
	
	// creates normal pt at origin
	public void
	resetUnitNormalVector()
	throws Exception
	{
		if (uVec.equals(vVec))
		{
			this.getUnitNormalVector().setPoint(0.0, 0.0, 0.0);
			return;
		}
		// MathOps.vectorCrossProduct(uVec, vVec, unitNormalVector);
		// MathOps.setToUnitVector(unitNormalVector);
		this.resetCrossProduct();
		BVector tmpPt = new BVector(this.getCrossProduct());
		this.getUnitNormalVector().copyPoint(tmpPt.unitVector());
	}

	public void
	resetCrossProduct()
	throws Exception
	{
		double uX = this.getUVec().xCoor();
		double uY = this.getUVec().yCoor();
		double uZ = this.getUVec().zCoor();
		double vX = this.getVVec().xCoor();
		double vY = this.getVVec().yCoor();
		double vZ = this.getVVec().zCoor();
		/*
		System.out.println("IN resetCorssProduct: " +
			uX + " " +
			uY + " " +
			uZ + " " +
			vX + " " +
			vY + " " +
			vZ);
		System.out.println("(uY * vZ) - (vY * uZ): " +
			((uY * vZ) - (vY * uZ)));
		System.out.println("-(uX * vZ) + (vX * uZ): " +
			(-(uX * vZ) + (vX * uZ)));
		System.out.println("(uX * vY) - (vX * uY): " +
			((uX * vY) - (vX * uY)));
		*/
		this.getCrossProduct().setPoint((uY * vZ) - (vY * uZ),
			-(uX * vZ) + (vX * uZ), (uX * vY) - (vX * uY));
	}

	public void
	setCoefficients(double a, double b, double c, double d)
	{
		this.setACoeff(a);
		this.setBCoeff(b);
		this.setCCoeff(c);
		this.setDCoeff(d);
	}

	public void
	setCoefficientsFromUnitNormalVector()
	throws Exception
	{
		this.resetUnitNormalVector();
		debug("N.x: " + this.getUnitNormalVector().xCoor());
		debug("N.y: " + this.getUnitNormalVector().yCoor());
		debug("N.z: " + this.getUnitNormalVector().zCoor());
		this.setACoeff(this.getUnitNormalVector().xCoor());
		this.setBCoeff(this.getUnitNormalVector().yCoor());
		this.setCCoeff(this.getUnitNormalVector().zCoor());
		this.setDCoeff(
			-this.getACoeff() * this.getSrfOrigin().xCoor()
			-this.getBCoeff() * this.getSrfOrigin().yCoor()
			-this.getCCoeff() * this.getSrfOrigin().zCoor());
	}

	public double
	rayPlaneIntersectTValue(BRay ray)
	throws Exception
	{
		this.setCoefficientsFromUnitNormalVector();
		debug("A: " + getACoeff());
		debug("B: " + getBCoeff());
		debug("C: " + getCCoeff());
		debug("D: " + getDCoeff());
		debug("tX: " + ray.getTailPt().xCoor()); 
		debug("tY: " + ray.getTailPt().yCoor()); 
		debug("tZ: " + ray.getTailPt().zCoor()); 
		debug("hX: " + ray.getHeadPt().xCoor()); 
		debug("hY: " + ray.getHeadPt().yCoor()); 
		debug("hZ: " + ray.getHeadPt().zCoor()); 
        double tmp =
			getACoeff()*(ray.getPositionVector().xCoor()) +
			getBCoeff()*(ray.getPositionVector().yCoor()) +
			getCCoeff()*(ray.getPositionVector().zCoor());
        if(tmp == 0.0)
		{
			// DON't know what causes this yet. Throw an exception
			// for now.
			// then perpendicular to unit normal. Can't divide
			// by 0.0 below. This current method can be made more
			// efficient; it 
			// return (ray.distPtToLine(new BVector()));
			//
			// MAYBE it is parallel to plane. therefore need to return ??
			// MAYBE should first check for parallelality
			throw new Exception("trying to divide by 0.0 in BPlaneSrf.rayPlaneIntersectTValue()");
		}
		return ((-getACoeff()*ray.getTailPt().xCoor() -
			getBCoeff()*ray.getTailPt().yCoor() -
			getCCoeff()*ray.getTailPt().zCoor() -
			getDCoeff())/tmp);
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

	public String
	toString()
	{
		return ("Plane\n" + super.toString());
		/*
		return(Plane:
		("srf.uVec: " +
				planeSrf.getUVec().positionVectorXCoor() + " " +
				planeSrf.getUVec().positionVectorYCoor() + " " +
				planeSrf.getUVec().positionVectorZCoor());
			System.out.println("srf.vVec: " +
				planeSrf.getVVec().positionVectorXCoor() + " " +
				planeSrf.getVVec().positionVectorYCoor() + " " +
				planeSrf.getVVec().positionVectorZCoor());
			System.out.println("srf.crossProduct.uVec: " +
				planeSrf.getCrossProduct().positionVectorXCoor() + " " +
				planeSrf.getCrossProduct().positionVectorYCoor() + " " +
				planeSrf.getCrossProduct().positionVectorZCoor());
			System.out.println("srf.length: " + planeSrf.getCrossProduct().positionVectorLength());
		*/

	}

	public String
	toRecord()
	{
		// NOT CORRECT:
		return ("s P " + super.toRecord());
	}

	private void
	debug(String s)
	{
		System.out.println(s);
	}

}
