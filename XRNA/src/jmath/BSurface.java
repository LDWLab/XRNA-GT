
package jmath;

/**
** defines all intrinsic properties and methods common to
** all surfaces.
*/

public class
BSurface
{
	/*
	BVector srfOrigin = null;
	double srfRadius = 0.0; // not sure srfRadius belongs here
	double glossiness = 0.0;
	double amtSpecular = 0.0;	// 0.0 -> 1.0
	double reflectR = 0.0;		// 0.0 -> 1.0
	double reflectG = 0.0;		// 0.0 -> 1.0
	double reflectB = 0.0;		// 0.0 -> 1.0
	*/

	public
	BSurface()
	throws Exception
	{
		this.setSrfOrigin(new BVector());
		this.setUnitNormalVector(new BVector());
	}

	private BVector srfOrigin = null;

	public void
	setSrfOrigin(BVector srfOrigin)
	{
		this.srfOrigin = srfOrigin;
	}

	public BVector
	getSrfOrigin()
	{
		return (this.srfOrigin);
	}

	// perpendicular to uv and unit length: uxv/|uxv|
	private BVector unitNormalVector = null;

	public void
	setUnitNormalVector(BVector unitNormalVector)
	{
		this.unitNormalVector = unitNormalVector;
	}

	public BVector
	getUnitNormalVector()
	{
		return (this.unitNormalVector);
	}

	// creates normal pt at origin for plane srf.
	// MAYBE make this class abstract
	public void
	resetUnitNormalVector()
	throws Exception
	{
	}

	// BELONGS in sphere srf
	private double srfRadius = 0.0;

	public void
	setSrfRadius(double srfRadius)
	throws Exception
	{
		if (srfRadius <= 0.0)
		{
			throw new Exception("Error in BSurface.setSrfRadius: " +
				"Trying to set a 0.0 or negative surface radius");
		}
		this.srfRadius = srfRadius;
	}

	public double
	getSrfRadius()
	{
		return (this.srfRadius);
	}

	private double glossiness = 0.0;

	public void
	setGlossiness(double glossiness)
	throws Exception
	{
		if (glossiness < 0.0)
		{
			throw new Exception("Error in BSurface.setSrfRadius: " +
				"Trying to set a negative glossiness factor");
		}
		this.glossiness = glossiness;
	}

	public double
	getGlossiness()
	{
		return (this.glossiness);
	}

	private double amtSpecular = 0.0;

	public void
	setAmtSpecular(double amtSpecular)
	throws Exception
	{
		if (!((amtSpecular >= 0.0) && (amtSpecular <= 1.0)))
		{
			throw new Exception("Error in BSurface.setAmtSpecular: " +
				"Trying to set a amtSpecular factor out of range 0.0->1.0");
		}
		this.amtSpecular = amtSpecular;
	}

	public double
	getAmtSpecular()
	{
		return (this.amtSpecular);
	}

	private double reflectR = 0.0;

	public void
	setReflectR(double reflectR)
	throws Exception
	{
		if (!((reflectR >= 0.0) && (reflectR <= 1.0)))
		{
			throw new Exception("Error in BSurface.setAmtSpecular: " +
				"Trying to set a reflectR factor out of range 0.0->1.0");
		}
		this.reflectR = reflectR;
	}

	public double
	getReflectR()
	{
		return (this.reflectR);
	}

	private double reflectG = 0.0;

	public void
	setReflectG(double reflectG)
	throws Exception
	{
		if (!((reflectG >= 0.0) && (reflectG <= 1.0)))
		{
			throw new Exception("Error in BSurface.setAmtSpecular: " +
				"Trying to set a reflectR factor out of range 0.0->1.0");
		}
		this.reflectG = reflectG;
	}

	public double
	getReflectG()
	{
		return (this.reflectG);
	}

	private double reflectB = 0.0;

	public void
	setReflectB(double reflectB)
	throws Exception
	{
		if (!((reflectB >= 0.0) && (reflectB <= 1.0)))
		{
			throw new Exception("Error in BSurface.setAmtSpecular: " +
				"Trying to set a reflectR factor out of range 0.0->1.0");
		}
		this.reflectB = reflectB;
	}

	public double
	getReflectB()
	{
		return (this.reflectB);
	}

	public String
	toString()
	{
		return(
			"srfOrigin: " +
			getSrfOrigin().xCoor() + " " +
			getSrfOrigin().yCoor() + " " +
			getSrfOrigin().zCoor() + "\n" +
		"srfRadius: " + getSrfRadius() + "\n" +
		"glossiness: " + getGlossiness() + "\n" +
		"amtSpecular: " + getAmtSpecular() + "\n" +
		"reflectR: " + getReflectR() + "\n" +
		"reflectG: " + getReflectG() + "\n" +
		"reflectB: " + getReflectB());
	}

	public String
	toRecord()
	{
		return(
		getSrfOrigin().xCoor() + " " +
		getSrfOrigin().yCoor() + " " +
		getSrfOrigin().zCoor() + " " +
		getSrfRadius() + " " +
		getGlossiness() + " " +
		getAmtSpecular() + " " +
		getReflectR() + " " +
		getReflectG() + " " +
		getReflectB());
	}

}
