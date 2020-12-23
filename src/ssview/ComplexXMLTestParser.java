package ssview;

import java.io.*;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.xerces.parsers.SAXParser;

public class
ComplexXMLTestParser
{
	private static boolean debugOn = true;

	public static void
	main(String[] args)
	{
		try
		{
			parseComplexXML(new FileReader(args[0]));
		}
		catch (Exception e)
		{
			handleException(e);
		}
	}

	private static void
	handleException(Throwable t)
	{
		debugOut("Exception in ComplexXMLDocument: " + t.toString());

		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		t.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debugOut(new String(excptArray.toByteArray()));
	}

	private static void
	debugOut(String msg)
	{
		if (!debugOn)
			return;
		System.out.println("ComplexXMLDocument-> " + msg);
	}

	public static void
	parseComplexXML(String scmlDocument)
	throws Exception
	{
		debugOut("Parsing ComplexXML Document in parseComplexXML\n\n");

//		ContentHandler contentHandler = new ContentHandler();
//		ErrorHandler errorHandler = new ErrorHandler();
		
		try
		{
			// don't use with applet
			XMLReader parser = new SAXParser();

			// kind of worked 02.07.01
			// XMLReader parser =
			// 	XMLReaderFactory.createXMLReader();
			//

//			parser.setContentHandler(contentHandler);
//			parser.setErrorHandler(errorHandler);
			parser.setFeature(
				"http://xml.org/sax/features/validation", true);
			parser.setFeature(
				"http://xml.org/sax/features/namespaces", false);

			parser.parse(new InputSource(
				new CharArrayReader(scmlDocument.toCharArray())));

			// setContentHandler(contentHandler);
			// setErrorHandler(errorHandler);
			// if (getCheckDTD())
			// {
				// setFeature(
					// "http://xml.org/sax/features/validation", true);
				// // setFeature(
					// "http://xml.org/sax/features/namespaces", false);
			// }
// 
			// parse(new InputSource(
				// new CharArrayReader(scmlDocument.toCharArray())));

			// XMLReader parser =
				// (XMLReader)SAXParserFactory.newInstance().newSAXParser();
			// XMLReader parser =
				// XMLReaderFactory.createXMLReader("javax.xml.parsers.SAXParser");
			// XMLReader parser =
				// XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			// SAXParser parser = 
				// SAXParserFactory.newInstance().newSAXParser();
		}
		catch (IOException ioe)
		{
			debugOut("IOException in parseComplexXML: " +
				ioe.toString());
			throw (ioe);
		}
		catch (SAXException saxe)
		{
			// debugOut("SAXException in parseComplexXML: " +
				// saxe.toString());
			throw (saxe);
		}
		catch (Exception e)
		{
			debugOut("Exception in parseComplexXML: " +
				e.toString());
			throw (e);
		}

		debugOut("\nComplexXML Document Parsed\n\n");
	}

	public static void
	parseComplexXML(FileReader scmlFile)
	throws Exception
	{
		debugOut("Parsing XML File: " + "\n\n");

//		ContentHandler contentHandler = new ComplexXMLContentHandler();
//		ErrorHandler errorHandler = new ComplexXMLErrorHandler();
		
		try
		{
			// don't use with applet
			XMLReader parser = new SAXParser();

			// kind of worked 02.07.01
			// XMLReader parser =
				// XMLReaderFactory.createXMLReader();
			//

//			parser.setContentHandler(contentHandler);
//			parser.setErrorHandler(errorHandler);
			parser.setFeature(
				"http://xml.org/sax/features/validation", true);
			parser.setFeature(
				"http://xml.org/sax/features/namespaces", false);

			parser.parse(new InputSource(scmlFile));

			// setContentHandler(contentHandler);
			// setErrorHandler(errorHandler);
			// if (getCheckDTD())
			// {
				// setFeature(
					// "http://xml.org/sax/features/validation", true);
				// setFeature(
					// // "http://xml.org/sax/features/namespaces", false);
			// }
// 
			// parse(new InputSource(scmlFile));
		}
		catch (IOException ioe)
		{
			debugOut("IOException in parseComplexXML: " +
				ioe.toString());
			throw (ioe);
		}
		catch (SAXException saxe)
		{
			debugOut("SAXException in parseComplexXML: " +
				saxe.toString());
			throw (saxe);
		}
		catch (Exception e)
		{
			debugOut("Exception in parseComplexXML: " +
				e.toString());
			throw (e);
		}

		debugOut("\nXML File: " + " , Parsed\n\n");
	}
}

class TestComplexXMLContentHandler
implements ContentHandler
{
	private boolean debugOn = true;
	private Locator locator;

	public void
	setDocumentLocator(Locator locator)
	{
		// debugOut("setDocumentLoader called");
		this.locator = locator;
	}

	public void
	startDocument()
	throws SAXException
	{
		// debugOut("Parsing begins");
	}

	public void
	endDocument()
	throws SAXException
	{
		// debugOut("Parsing ends");
	}

	public void
	processingInstruction(String target, String data)
	throws SAXException
	{
		// debugOut("PI: Target: " + target + " and Data: " + data);
	}

	public void
	startPrefixMapping(String prefix, String uri)
	throws SAXException
	{
		// debugOut("Mapping starts for prefix " + prefix + " mapped to URI " + uri);
	}

	public void
	endPrefixMapping(String prefix)
	throws SAXException
	{
		// debugOut("endPrefixMapping: " + prefix);
	}

	public void
	startElement(String namespaceURI, String localName,
		String rawName, Attributes atts)
	throws SAXException
	{
		debugOut("");
		debugOut("startElement " + rawName);
		if (!namespaceURI.equals(""))
		{
			// debugOut(" in namespace " + namespaceURI +
				// " (" + rawName + ")");
		}
		else
		{
			// debugOut(" has no associated namespace");
		}

		for (int i=0;i < atts.getLength();i++)
		{
			debugOut("Attribute: " + atts.getQName(i) +
				"=" + atts.getValue(atts.getQName(i)));
			// debugOut("Attribute: " +
				// atts.getLength() + ":" +
				// atts.getURI(i) + ":" +
				// atts.getLocalName(i) + ":" +
				// atts.getQName(i) + "->" +
				// atts.getType(atts.getQName(i)) + ":" +
				// atts.getValue(atts.getQName(i)));
		}
	}

	public void
	endElement(String namespaceURI, String localName,
		String rawName)
	throws SAXException
	{
		debugOut("endElement: " + rawName + "\n");
	}

	public void
	characters(char[] ch, int start, int end)
	throws SAXException
	{
		debugOut("characters: " + (new String(ch, start, end)));
	}

	public void
	ignorableWhitespace(char[] ch, int start, int end)
	throws SAXException
	{
		// debugOut("ignorableWhitespace: " + (new String(ch, start, end)));
	}

	public void
	skippedEntity(String name)
	throws SAXException
	{
		// debugOut("skippedEntity: " + name);
	}

	private void
	debugOut(String msg)
	{
		if (!debugOn)
			return;
		System.out.println("ComplexXMLContentHandler-> " + msg);
	}
}

