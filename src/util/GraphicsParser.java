package util;

import java.io.*;
// import com.unikala.util.ByteUtil;
// import com.livemedia.servlet.scdispatcher.SCMLDocument;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.xml.sax.ext.DeclHandler;

import org.apache.xerces.parsers.SAXParser;
// need to include the following so can build so msgs can be printed out properly
import org.apache.xerces.msg.DatatypeMessages;
import org.apache.xerces.msg.ExceptionMessages;
import org.apache.xerces.msg.SchemaMessages;
import org.apache.xerces.msg.XMLMessages;
import org.apache.xerces.msg.ImplementationMessages;


public abstract class
GraphicsParser
implements ContentHandler, ErrorHandler
{

private static boolean debugOn = true;
public static boolean checkDTD = true;
public Locator locator;
public String charactersString = null;

public static void
setCheckDTD(boolean checkDTD)
{
	checkDTD = checkDTD;
}

public static boolean
getCheckDTD()
{
	return (checkDTD);
}

private String dtdPath = null;

public void
setDTDPath(String dtdPath)
{
    this.dtdPath = dtdPath;
}

public String
getDTDPath()
{
    return (this.dtdPath);
}

public abstract String generateComplexXML_DTD();

public abstract void initParse() throws Exception;

public void
parse(InputSource inputSource)
throws Exception
{
	try
	{
		// don't use with applet, but servlet and helper parser
		XMLReader parser = new SAXParser();

		// kind of worked 02.07.01. works for applet if need be
		// XMLReader parser = XMLReaderFactory.createXMLReader();
		//
		
		parser.setContentHandler((ContentHandler)this);
		parser.setErrorHandler((ErrorHandler)this);
		if (getCheckDTD())
		{
			parser.setFeature(
				"http://xml.org/sax/features/validation", true);
			parser.setFeature(
				"http://xml.org/sax/features/namespaces", false);
		}
		parser.parse(inputSource);
	}
	catch (IOException ioe)
	{
		debug("IOException in parse: " +
			ioe.toString());
		throw (ioe);
	}
	catch (SAXException saxe)
	{
		debug("SAXException in parse: " +
			saxe.toString());
		throw (saxe);
	}
	catch (Exception e)
	{
		debug("Exception in parse: " +
			e.toString());
		throw (e);
	}
}

public void
parse(String source)
throws Exception {
	this.initParse();
	String xmlString = source.trim();
	
	if (xmlString.startsWith("<!DOCTYPE")) {
		parse(new InputSource(new CharArrayReader(xmlString.toCharArray())));
	} else {
		parse(new InputSource(new CharArrayReader(
			("<!DOCTYPE ComplexDocument\n[\n" +
			this.generateComplexXML_DTD() + "\n]\n>\n" +
				xmlString).toCharArray())));
	}
}

public void
parse(FileReader file)
throws Exception
{
	this.parse(file, this.getDTDPath());
}

public void
parse(FileReader complexFile, String dtdPathAndFileName)
throws Exception
{
	this.initParse(); // redundant in some cases
	StringWriter xmlWriter = new StringWriter();

	int maxChars = 10000;
	char cArray[] = new char[maxChars];
	for (int b = complexFile.read(cArray, 0, maxChars);b != -1;b = complexFile.read(cArray, 0, maxChars))
	{
		xmlWriter.write(cArray, 0, b);
	}
	this.parse(xmlWriter.toString());
}

public abstract void
startElement(String namespaceURI, String localName,
	String rawName, Attributes atts)
throws SAXException;

public abstract void
endElement(String namespaceURI, String localName, String rawName)
throws SAXException;

public void
characters(char[] ch, int start, int length)
throws SAXException
{
	charactersString = new String(ch, start, length);
	//System.out.println("char string: " + charactersString);
}

public void
setDocumentLocator(Locator locator)
{
	this.locator = locator;
}

public void
startDocument()
throws SAXException
{
}

public void
endDocument()
throws SAXException
{
}

public void
processingInstruction(String target, String data)
throws SAXException
{
}

public void
startPrefixMapping(String prefix, String uri)
throws SAXException
{
}

public void
endPrefixMapping(String prefix)
throws SAXException
{
}

public void
ignorableWhitespace(char[] ch, int start, int end)
throws SAXException
{
	// debug("ignorableWhitespace: " + (new String(ch, start, end)));
	//System.out.println("ignorableWhitespace: " + (new String(ch, start, end)));
}

public void
skippedEntity(String name)
throws SAXException
{
	// debug("skippedEntity: " + name);
	//System.out.println("skippedEntity: " + name);
}

public void
warning(SAXParseException exception)
throws SAXException
{
	debug("Parsing warning from GraphicsParser, Line: " +
		exception.getLineNumber() + "\n" +
		"URI: " + exception.getSystemId() + "\n" +
		"Message: " + exception.getMessage() + "\n");
	throw exception;
}

public void
error(SAXParseException exception)
throws SAXException
{
	debug("Parsing error from GraphicsParser, Line: " +
		exception.getLineNumber() + "\n" +
		"URI: " + exception.getSystemId() + "\n" +
		"Message: " + exception.getMessage() + "\n");
	throw exception;
}

public void
fatalError(SAXParseException exception)
throws SAXException
{
	debug("Parsing fatal Error from GraphicsParser, Line: " +
		exception.getLineNumber() + "\n" +
		"URI: " + exception.getSystemId() + "\n" +
		"Message: " + exception.getMessage() + "\n");
	throw exception;
}

private static void
debug(String msg)
{
	if (!debugOn)
		return;
	System.out.println("GraphicsParser-> " + msg);
}

public static void
handleException(Throwable t)
{
	debug("Exception in GraphicsParser: " + t.toString());
	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
	t.printStackTrace(new PrintStream(
		new DataOutputStream(excptArray)));
	debug(new String(excptArray.toByteArray()));
}

}
