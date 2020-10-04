package jimage;

import java.io.*;

public class
DrawObjectException
extends Exception
{

public
DrawObjectException()
{
	super();
}

public
DrawObjectException(String comment)
{
	super (comment);
	this.setComment(comment);
}

public
DrawObjectException(String exceptionMsg, int errorCode)
{
	super(exceptionMsg + ", Error Code = " + errorCode);

	this.setExceptionMsg(exceptionMsg);
	this.setErrorCode(errorCode);
}

public
DrawObjectException(String exceptionMsg, int errorCode, String errorMsg)
{
	super(exceptionMsg + ", Error Code = " + errorCode +
			"Error Msg = " + errorMsg);

	this.setExceptionMsg(exceptionMsg);
	this.setErrorCode(errorCode);
	this.setErrorMsg(errorMsg);
}

public
DrawObjectException(String exceptionMsg, int errorCode, String errorMsg, String comment)
{
	this(exceptionMsg, errorCode, errorMsg);
	this.setComment(comment);
}

private String exceptionMsg = null;

public void
setExceptionMsg(String exceptionMsg)
{
    this.exceptionMsg = exceptionMsg;
}

public String
getExceptionMsg()
{
    return (this.exceptionMsg);
}

private int errorCode = 0;

public void
setErrorCode(int errorCode)
{
    this.errorCode = errorCode;
}

public int
getErrorCode()
{
    return (this.errorCode);
}

private String errorMsg = "";

public void
setErrorMsg(String errorMsg)
{
    this.errorMsg = errorMsg;
}

public String
getErrorMsg()
{
    return (this.errorMsg);
}

private String comment = null;

public void
setComment(String comment)
{
    this.comment = comment;
}

public String
getComment()
{
    return (this.comment);
}

public String
toString()
{
	StringBuffer str = new StringBuffer();
	
	str.append("\n" +
		this.getExceptionMsg() + "\n" +
		"errCode = " + this.getErrorCode() + "\n" +
		"errMsg = " + this.getErrorMsg() + "\n");
	if (this.getComment() != null)
		str.append(this.getComment() + "\n");
						
	/*
	if (exception != null)
		str.append("\n" + exception.toString());
	*/
	
   return (str.toString());
}

public String
getStackTraceAsString()
{
	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
	this.printStackTrace(new PrintStream(new DataOutputStream(excptArray)));
	return(new String(excptArray.toByteArray()));

		/* this might be better:
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
		*/
}

}
