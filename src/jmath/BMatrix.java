package jmath;

public class
BMatrix
implements MathDefines
{
	private int colCount = 0;
	protected double[][] transMat = null;

	// build a point with all 0.0's
    public
    BMatrix()
	throws Exception
    {
		this(4);
    }

    public
    BMatrix(int cCount)
	throws Exception
    {
		this (1, cCount);
    }

    public
    BMatrix(int rCount, int cCount)
	throws Exception
    {
		this.setRowCount(rCount);
		this.setColCount(cCount);
		this.transMat = new double[rowCount][colCount];
		buildNullMatrix();
    }

	// build a point with given x, y, z
    public
    BMatrix(double x, double y, double z)
	throws Exception
    {
		this(4);
		buildNullTransformMatrix();
		setPoint(x, y, z);
    }

	private int rowCount = 0;

	public int
	getRowCount()
	{
		return (this.rowCount);
	}

	public void
	setRowCount(int count)
	{
		this.rowCount = count;
	}

	public int
	getColCount()
	{
		return (this.colCount);
	}

	public void
	setColCount(int count)
	{
		this.colCount = count;
	}

	/**
	** build a 1x4 matrix for a point that can be transformed;
	** i.e., contains a 1.0 in the 4th cell.
	*/

	public static BMatrix
	point()
	throws Exception
	{
		return (new BMatrix(0.0, 0.0, 0.0));
	}

	/**
	** build a 1x4 matrix for a point that can be transformed;
	** i.e., contains a 1.0 in the 4th cell. initialized to
	** xPos, yPos, zPos.
	*/

	public static BMatrix
	point(double xPos, double yPos, double zPos)
	throws Exception
	{
		return (new BMatrix(xPos, yPos, zPos));
	}

	/**
	** build a 1x3 matrix for a point that won't be transformed.
	*/

	public static BMatrix
	boxPoint()
	throws Exception
	{
		return (new BMatrix(3));
	}

	public boolean
	isSquareMatrix()
	{
		return ((rowCount > 0) && (colCount > 0) &&
			(rowCount == colCount));
	}

	public boolean
	isPointMatrix()
	{
		return ((rowCount == 1) && (colCount == 4));
	}

	public void
	buildNullMatrix()
	throws Exception
	{
		if(transMat == null)
			throw new Exception("transMat is null");

		for(int row = 0;row < rowCount;row++)
			for(int col = 0;col < colCount;col++)
				transMat[row][col] = 0.0;
	}

	public void
	buildNullTransformMatrix()
	throws Exception
	{
		int row, col;

		if(transMat == null)
			throw new Exception("transMat == null");
		if (colCount != 4)
			throw new Exception("transform matrix must have 4 columns");
		for(row = 0;row < rowCount;row++)
			for(col = 0;col < colCount;col++)
				transMat[row][col] = 0.0;
		transMat[rowCount - 1][colCount-1] = 1.0;
	}

	public void
	buildIdentityMatrix()
	throws Exception
	{
		int i;
	 
		if(transMat == null)
			throw new Exception("transMat == null");
		if (!isSquareMatrix())
			throw new Exception("not a square matrix");
		this.buildNullMatrix();
		for (i = 0;i < rowCount;i++)
			transMat[i][i] = 1.0;
	}

	public void
	setPoint(double x, double y, double z)
	throws Exception
	{
		if (!((rowCount == 1) && (colCount == 4)))
			throw new Exception(rowCount + "x" + colCount + " not a point array size for matrix");
		transMat[0][XCoor] = x;
		transMat[0][YCoor] = y;
		transMat[0][ZCoor] = z;
	}

	public void
	setBoxPoint(double x, double y, double z)
	throws Exception
	{
		if (!((rowCount == 1) && (colCount == 3)))
			throw new Exception(rowCount + "x" + colCount + " not a box point array size for matrix");
		transMat[0][XCoor] = x;
		transMat[0][YCoor] = y;
		transMat[0][ZCoor] = z;
	}

	public double[]
	getRow(int row)
	throws Exception
	{
		if ((row < 0) || (row >= rowCount))
			throw new Exception("Error in BMatrix.getRow(), " +
				"Trying to access row out of bounds: " + row +
				" with bounds: 0->" + (rowCount-1));
		return (transMat[row]);
	}

	public double[]
	getPointRow()
	throws Exception
	{
		return (getRow(0));
	}

	/* Not sure what this is for
	public void
	buildNullTransformPoint()
	throws Exception
	{
		buildNullTransformMatrix();
	}
	*/

	public double
	getPointX()
	{
		return (transMat[0][XCoor]);
	}

	public void
	setPointX(double x)
	{
		transMat[0][XCoor] = x;
	}

	public double
	getPointY()
	{
		return (transMat[0][YCoor]);
	}

	public void
	setPointY(double y)
	{
		transMat[0][YCoor] = y;
	}

	public double
	getPointZ()
	{
		return (transMat[0][ZCoor]);
	}

	public void
	setPointZ(double z)
	{
		transMat[0][ZCoor] = z;
	}

	public double
	pointCoord(int coord)
	// throws Exception
	{
		return (transMat[0][coord]);
		// return (rowVal(0, coord));
	}

	public double
	rowVal(int row, int coord)
	throws Exception
	{
		double[] ptRow = null;

		try
		{
			ptRow = getRow(row);
			return (ptRow[coord]);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void
	buildXRotationMatrix(double theta)
	throws Exception
	{
		if(transMat == null)
			throw new Exception("transMat == null");
		if ((colCount != 4) || (rowCount !=4))
			throw new Exception(
				"transform matrix must be 4x4, got: " +
				rowCount + "x" + colCount);
		double radian;
	 
		radian = (double)MathOps.angleToRadian(-theta);
		this.buildIdentityMatrix();
		transMat[1][1] = transMat[2][2] = Math.cos(radian);
		transMat[2][1] = Math.sin(radian);
		transMat[1][2] = -transMat[2][1];
	}

	public void
	buildYRotationMatrix(double theta)
	throws Exception
	{
		double radian;
	 
		radian = (double)MathOps.angleToRadian(-theta);
		this.buildIdentityMatrix();
		transMat[0][0] = transMat[2][2] = Math.cos(radian);
		transMat[0][2] = Math.sin(radian);
		transMat[2][0] = -transMat[0][2];
	}

	public void
	buildZRotationMatrix(double theta)
	throws Exception
	{
		double radian;
	 
		radian = (double)MathOps.angleToRadian(-theta);
		this.buildIdentityMatrix();
		transMat[0][0] = transMat[1][1] = Math.cos(radian);
		transMat[1][0] = Math.sin(radian);
		transMat[0][1] = -transMat[1][0];
	}

	public void
	buildTranslationMatrix(double xPos, double yPos, double zPos)
	throws Exception
	{
		this.buildIdentityMatrix();
		transMat[3][0] = xPos;
		transMat[3][1] = yPos;
		transMat[3][2] = zPos;
	}
 
	public void
	buildScaleMatrix(double xAmount, double yAmount, double zAmount)
	throws Exception
	{
		this.buildIdentityMatrix();
		transMat[0][0] = xAmount;
		transMat[1][1] = yAmount;
		transMat[2][2] = zAmount;
	}

	public static void
	multiplyMatrices(BMatrix firstMat, BMatrix secondMat, BMatrix resultMat)
	throws Exception
	{
		BMatrix tmpMat;
	 
		if(firstMat.getColCount() != secondMat.getRowCount())
			return;
		tmpMat = new BMatrix(firstMat.getRowCount(), secondMat.getColCount());

		resultMat.setRowCount(firstMat.getRowCount());
		resultMat.setColCount(secondMat.getColCount());
		for(int row = 0;row < resultMat.getRowCount();row++)
		{
			for(int col = 0;col < resultMat.getColCount();col++)
				tmpMat.transMat[row][col] =
					dotProduct(firstMat, secondMat, row, col);
		}
		copyMatrix(resultMat, tmpMat);
	}

	public static double
	dotProduct(BMatrix mat1, BMatrix mat2, int row, int col)
	{
		int index, columnsize;
		double accum = 0.0;
		double[][] m1ptr, m2ptr;
	 
		m1ptr = mat1.transMat;
		m2ptr = mat2.transMat;
		columnsize = mat1.getColCount();
		for(index = 0;index < columnsize;index++)
		{
			accum += m1ptr[row][index] * m2ptr[index][col];
		}
		return(accum);
	}

	public static void
	pointMatrixMult(BMatrix testpt, BMatrix transMatrix)
	throws Exception
	{
		int col, row;
		double accum;
		BMatrix tmppt = new BMatrix(1, 4);
	 
		copyMatrix(tmppt, testpt);
		for(col = 0;col < 4;col++)
		{
			accum = 0.0;
			for(row = 0;row < 4;row++)
			{
				accum += tmppt.transMat[0][row] * transMatrix.transMat[row][col];
			}
			testpt.transMat[0][col] = accum;
		}
	}

	public static void
	copyMatrix(BMatrix copyToMat, BMatrix copyFromMat)
	{
		copyToMat.setRowCount(copyFromMat.getRowCount());
		copyToMat.setColCount(copyFromMat.getColCount());
		for(int row = 0;row < copyToMat.getRowCount();row++)
			for(int col = 0;col < copyToMat.getColCount();col++)
				copyToMat.transMat[row][col] = copyFromMat.transMat[row][col];
	}

	public boolean
	equals(BMatrix toMatrix)
	{
		if (this.getRowCount() != toMatrix.getRowCount())
			return (false);
		if (this.getColCount() != toMatrix.getColCount())
			return (false);
		for (int row = 0;row < toMatrix.getRowCount();row++)
			for (int col = 0;col < toMatrix.getColCount();col++)
				if (transMat[row][col] != toMatrix.transMat[row][col])
					return (false);
		return (true);
	}

	public String
	toString()
	{
		if(transMat == null)
			return ("empty matrix");

		StringBuffer strBuf = new StringBuffer();

		for(int row = 0;row < rowCount;row++)
		{
			for(int col = 0;col < colCount;col++)
			{
				strBuf.append(transMat[row][col]);
				strBuf.append(" ");
			}
			strBuf.append("\n");
		}
		return(strBuf.toString());
	}
}
