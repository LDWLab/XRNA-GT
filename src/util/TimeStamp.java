/**
** TimeStamp class ( ).<p>
**
** Description:
** ------------
** TimeStamp creates different time stamp information for the caller.
** It can determine elapsed time along with returning GMT and Local 
** times.
**
*/

package util;

import java.util.*;
import java.text.*;


public class
TimeStamp extends Object
{

	protected long					startTime;

	public
	TimeStamp()
	{
		startTime = this.nowTime();
	}


	/**
	** Format of the returning string is the following:
	** 01/06/97 21:20:18 :
	*/

	public static String
	getTimeStamp()
	{
		// Local variables.
		SimpleDateFormat	theFormatter = null;
		Date				theNow = new Date();
		
		// Create theFormatter object.
		theFormatter = new SimpleDateFormat ("MM.dd.yy. HH:mm:ss.SSS");

		// Return the date with milliseconds
		return (theFormatter.format(theNow));
	}
	
	/**
	** Returns a string representing the date in the current GMT.
	** e.g.  "31 Dec 1998 22:57:23 GMT"
	*/

	public static String
	getGMTTimeStamp()
	{
		Date				td = new Date();
		TimeZone			tz = TimeZone.getTimeZone("GMT");
		SimpleDateFormat	df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");

		df.setTimeZone(tz);

		return(df.format(td));
	}

	public static long
	getTime()
	{
		return (TimeStamp.nowTime());
	}

	public static float
	getElapsedTime(long iStartTime)
	{
		return (TimeStamp.elapsedTime(iStartTime));
	}

	public float
	getElapsedTime()
	{
		return (this.elapsedTime(startTime));
	}

	/////////////////////////////////////////////////////////////////////////
	//					Set Methods
	/////////////////////////////////////////////////////////////////////////

	public void
	setTime()
	{
		startTime = this.nowTime();
	}

	/////////////////////////////////////////////////////////////////////////
	//					Private Static Methods
	/////////////////////////////////////////////////////////////////////////

	private static long
	nowTime()
	{
		return (new Date().getTime());
	}

	private static float
	elapsedTime(long iStartTime)
	{
		// Local variables.
		Float	theTime = null;
		long	theEndTime = 0;
		long	theElapsedTime = 0;

		// Get the time now and treat it as the end time.
		theEndTime = new Date().getTime();

		// Determine the elapsed time.
		theElapsedTime = theEndTime - iStartTime;

		// Convert to Float.
		theTime = new Float( (float) theElapsedTime);

		// Return the elapsed time.
		return (theTime.floatValue()/1000);
	}
}
