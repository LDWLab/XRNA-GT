package util;

import java.io.*;
import java.util.*;

public class FileUtil
{

/**
* Return true if the given file exists, false otherwise.
*/

public static boolean
fileExists(String name)
{
	File file = new File(name);
	return file.exists();
}

/**
* Recursively delete a directory and its contents
*
* @param directory
*		The directory to delete.  If this is a file, not a directory,
*		then this method throws an IOException
*
* @param deleteDir
*		Set this to true if you want to delete the directory itself
*		as well as its contents.  Set this to false if you only want
*		to delete the contents
*/

public static void
deleteDirContents(File directory, boolean deleteDir) 
throws IOException
{
	if ( ! directory.isDirectory() )
	{
		throw new IOException(directory.getAbsolutePath() + " is not a directory");
	}
	
	String [] contents = directory.list();
	
	if ( contents != null )
	{			
		for ( int i = 0 ; i < contents.length ; i++ )
		{
			File file = new File(directory, contents[i]);
			
			if ( file.isFile() )
			{
				file.delete();
			}
			else
			{
				deleteDirContents(file, true);
			}
		}
	}
	
	if ( deleteDir )
	{
		directory.delete();
	}		
}	

/**
* Compare the contents of a text file with a String.
*
* @param filename
*		The name of the file to compare
*
* @param str
*		The string to compare the contents of the file with
*
* @return
*		true if there is an exact match, false otherwise.
*/

public static boolean
compareFile(String filename, String str)
throws IOException
{
	if ( fileIsEmpty(filename)  )
	{
		if ( str == null || str.length() == 0 )
		{
			return true;
		}
		
		return false;
	}
	
	StringReader strReader = new StringReader(str);
	FileReader fileReader = new FileReader(filename);
	
	for ( int b = fileReader.read() ; b != -1 ; b = fileReader.read() )
	{
		int sb = strReader.read();
		if ( sb == -1 )
		{
			return false;
		}
		
		if ( sb != b )
		{
			return false;
		}
	}
	
	return true;
}

/**
* Return true if the specified file exists but is empty (e.g. has
* zero characters in it)
*/

public static boolean
fileIsEmpty(String filename)
throws IOException
{
	FileReader fileReader = new FileReader(filename);
	
	int b = fileReader.read();
	if ( b == -1 )
	{
		return true;
	}
	else
	{
		return false;
	}
}

/**
* Print out the contents of a file to the given output stream
*/

public static void
writeFile(String filename, OutputStream out)
throws IOException
{
	FileReader reader = new FileReader(filename);
	
	for ( int b = reader.read() ; b != -1 ; b = reader.read() )
	{
		out.write(b);
	}
}

public static void
writeStringToFile(String printString, String fileName)
throws Exception
{
	File outFile = new File(fileName);

	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name

	StringWriter strWriter = null;
	PrintWriter printWriter = null;

	// first get xml as string to bypass any exceptions
	try
	{
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
		printWriter.println(printString);
	}
	catch (Exception e)
	{
		throw e;
	}

	// made it past any errors in creating xml

	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(strWriter.toString());
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

/**
* Get the contents of a file as a string
*/

public static String
getFileAsString(String filename)
throws IOException
{
	FileReader reader = new FileReader(filename);
	StringWriter writer = new StringWriter();

	for ( int b = reader.read() ; b != -1 ; b = reader.read() )
	{
		writer.write(b);
	}
	
	return writer.toString();
}

public static String
getFileAsString(File file)
throws IOException
{
	FileReader reader = new FileReader(file);
	StringWriter writer = new StringWriter();

	for ( int b = reader.read() ; b != -1 ; b = reader.read() )
	{
		writer.write(b);
	}
	
	return writer.toString();
}

/**
* Copy a file from one location to another
*
* @param source
*		The path to the source file
*
* @param dest
*		The path to the dest file
*
* @exception IOException
*		if for any reason the operation could not be completed
*/

public static void
copyFile(String source, String dest)
throws IOException
{
	copyFile(new File(source), new File(dest));
}

public static void
copyFile(File source, File dest)
throws IOException
{
	FileOutputStream out = new FileOutputStream(dest);
	FileInputStream in = new FileInputStream(source);
	
	int ch;
	while ( (ch = in.read()) != -1 )
	{
		out.write(ch);
	}
	in.close();
	out.close();
}

/**
* Move a file from one location to another. If source file does not
* exist, method does nothing.
*
* @param source
*		The path to the source file
*
* @param dest
*		The path to the dest file
*
* @exception IOException
*		if for any reason the operation could not be completed
*/

public static void
move(String source, String dest)
throws IOException
{
	File infile = new File(source);
	if (!infile.exists())
		return;

	File outfile = new File(dest);
	infile.renameTo(outfile);
	
	if (!outfile.exists())
		throw new IOException("Unable to move file " + source);
}

/**
* Delete a file.  If the file does not exist this method does nothing.
*
* @param name
*		The name of the file to be deleted.
*
* @exception IOException
*		If the file could not be deleted
*/

public static void
deleteFile(String name)
throws IOException
{
	File file = new File(name);
	
	if ( ! file.exists() )
	{
		return;
	}
	
	// Try for 9 seconds to delete the file...
	for (int i = 0 ; i < 3 ; i++)
	{
		if (! file.canWrite())
		{
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
		else
		{
			file.delete();
			i = 2;
		}
	}
	
	if ( file.exists() )
	{
		throw new IOException("Unable to delete file " + name);
	}
}

/**
* Create a directory with the given name. If the directory already exists,
* the method does nothing.
*
* @param name
*		The name of the directory to be created.
*
* @exception IOException
*		If the directory could not be created.
*/

public static void
makeDirectory(String name)
throws IOException
{
	File file = new File(name);
	if (file.exists())
		return;
	
	file.mkdir();
	if (!file.exists())
		throw new IOException("Unable to make directory " + name);
}

/**
* Create a directory, including all needed and non-existent parent
* directories, with the given name. If the directory already exists,
* the method does nothing.
*
* @param name
*		The name of the directory to be created.
*
* @exception IOException
*		If the directory could not be created.
*/

public static void
makeDirectories(String name)
throws IOException
{
	File file = new File(name);
	if (file.exists())
		return;
	
	file.mkdirs();
	if (!file.exists())
		throw new IOException("Unable to make directory " + name);
}

/**
 * Returns the list of files that end with the given string that are present
 * in the given directory.
 *
 * @param dir Directory to look in for the files.
 * @param ext
 *   Get list of files that end with the given string.
 *
 * @return
 *   List of file names as strings. This is an empty list if there are no
 *   files with the given extension in the given directory or if the
 *   given directory is not a directory.
 */

public static ArrayList
getListOfFiles(File dir, String endWith)
{
	ArrayList theFiles = new ArrayList();
	String[] allTheFiles = dir.list();
	
	if (allTheFiles != null)
	{ 
		for (int j = 0; j < allTheFiles.length; j++) 
			if (allTheFiles[j].endsWith(endWith))
				theFiles.add(allTheFiles[j]);
	}

	return theFiles;
}

}
