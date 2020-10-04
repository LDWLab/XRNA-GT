package ssview;

import java.io.*;

import jimage.DrawObjectException;

public class
ComplexException
extends DrawObjectException
{

public
ComplexException()
{
	super();
}

public
ComplexException(String comment)
{
	super (comment);
	this.setComment(comment);
}

public
ComplexException(String exceptionMsg, int errorCode)
{
	super(exceptionMsg + ", Error Code = " + errorCode);

	this.setExceptionMsg(exceptionMsg);
	this.setErrorCode(errorCode);
}

public
ComplexException(String exceptionMsg, int errorCode, String errorMsg)
{
	super(exceptionMsg + ", Error Code = " + errorCode +
			"Error Msg = " + errorMsg);

	this.setExceptionMsg(exceptionMsg);
	this.setErrorCode(errorCode);
	this.setErrorMsg(errorMsg);
}

public
ComplexException(String exceptionMsg, int errorCode, String errorMsg, String comment)
{
	this(exceptionMsg, errorCode, errorMsg);
	this.setComment(comment);
}

}
