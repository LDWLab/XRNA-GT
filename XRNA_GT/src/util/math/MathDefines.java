package util.math;

public interface
MathDefines
{

// use java.lang.Math.PI instead.
// public static final double Pi = 3.1415926535897932384;

// DegToRad should be around 0.0174532925199433
public static final double DegToRad = (Math.PI / 180.0);
public static final float FLOAT_DEGTORAD = ((float)Math.PI / 180.0f);

// RadToDeg should be around 57.2957795130823229
public static final double RadToDeg = (180.0 / Math.PI);
public static final float FLOAT_RADTODEG = (180.0f / (float)Math.PI);

public static final double PRECISION = 1000000000000.0;
public static final float FLOAT_PRECISION = 1000000000.0f;

/*
** The quadrants in a cartesian coordinate system are defined
** as follows:
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
**	 25   ------------   00
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
*/

public static final int inQuadrant00 = 0;
public static final int inQuadrant10 = 1;
public static final int inQuadrant15 = 15;
public static final int inQuadrant20 = 20;
public static final int inQuadrant25 = 25;
public static final int inQuadrant30 = 30;
public static final int inQuadrant35 = 35;
public static final int inQuadrant40 = 40;

public static final int XYPlane = 0;
public static final int XZPlane = 1;
public static final int YZPlane = 2;
public static final int YXPlane = 3;
public static final int ZXPlane = 4;
public static final int ZYPlane = 5;

public static final int	 REDPART	 = 0xff0000;
public static final int	 GREENPART	 = 0x00ff00;
public static final int	 BLUEPART	 = 0x0000ff;
public static final int	 MAXCOLORVAL = 0xffffff;
public static final int	 MINCOLORVAL = 0x0;
public static final int	 MINCOLORINC = 0x1;
public static final int	 MAX_SHORT   = 0xffff;

// Ray trace defines

public static final double PT_BOX_INTERSECT_ERR = 0.000001;

public static final char SPHERESRF   = 'S';
public static final char TORUSSRF    = 'T';
public static final char STEINERSRF  = 'I';
public static final char CUSPSRF     = 'C';
public static final char KUMMERSRF   = 'K';
public static final char CYLINDERSRF = 'Y';
public static final char PLANESRF    = 'P';
public static final char HYPERPLANE  = 'H';
public static final char TRIANGLESRF = 'A';
public static final char POLYGONSRF  = 'G';
public static final char PSPHERESRF  = 'E'; /* part of sphere for lens */
public static final char RPLANESRF   = 'R';
public static final char LSPHERESRF  = 'L';
public static final char RSPHERESRF  = 'F';

/************* Line format Defines **************************/

public static final int LINE_PARTITION_DEGENERATE = -3; // strand is one nuc, no delineation
public static final int LINE_PARTITION_NOT_SET = -2;
public static final int LINE_PARTITION_TAIL = -1;
public static final int LINE_PARTITION_MID = 0;
public static final int LINE_PARTITION_HEAD = 1;
public static final int LINE_PARTITION_ERROR = 2;

/************* End Line format Defines **************************/

}
