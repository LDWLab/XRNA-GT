package ssview;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ComplexEntityResolver
implements EntityResolver
{
	public InputSource
	resolveEntity (String publicId, String systemId)
	{
		System.out.println("IN ComplexEntityResolver");
		System.out.println("publicId: " + publicId);
		System.out.println("systemId: " + systemId);
		/*
		if (systemId.equals("http://www.myhost.com/today"))
		{
			// return a special input source
			MyReader reader = new MyReader();
			return new InputSource(reader);
		}
		else
		{
			// use the default behaviour
			return null;
		}
		*/
		return null;
	}
}
 

