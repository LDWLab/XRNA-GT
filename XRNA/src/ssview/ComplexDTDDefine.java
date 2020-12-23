package ssview;

import java.io.*;

public abstract class
ComplexDTDDefine
{

public static String complexDTD =
/* Original string from ComplexXMLParser:
"<!ELEMENT ComplexDocument (SceneNodeGeom?, ComplexDocument*, Label*, LabelList*,				" + "\n" +
"		Complex*, WithComplex*)>																" + "\n" +
"<!ATTLIST ComplexDocument																		" + "\n" +
"	Name CDATA #REQUIRED																		" + "\n" +
"	Author CDATA #IMPLIED																		" + "\n" +
"	ExpandUponWrite CDATA #IMPLIED																" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT Complex (SceneNodeGeom?, Label*, LabelList*, RNAMolecule*, ProteinMolecule*)>		" + "\n" +
"<!ATTLIST Complex																				" + "\n" +
"	Name CDATA #REQUIRED																		" + "\n" +
"	Author CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT WithComplex (SceneNodeGeom?, Label*, LabelList*, RNAMolecule*, ProteinMolecule*)>	" + "\n" +
"<!ATTLIST WithComplex																			" + "\n" +
"	Name CDATA #REQUIRED																		" + "\n" +
"	Author CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT RNAMolecule (SceneNodeGeom?, Label*, LabelList*, Nuc*,								" + "\n" +
"	NucSegment*, (NucListData | Nuc)*, RNAFile*, BasePairs*,									" + "\n" +
"	BasePair*, Parent?, AlignmentFile?)>														" + "\n" +
"<!ATTLIST RNAMolecule																			" + "\n" +
"	Name CDATA #REQUIRED																		" + "\n" +
"	Author CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!-- parent is like for e.coli being the numbering -->											" + "\n" +
"<!ELEMENT Parent (RNAMolecule)>																" + "\n" +
"<!ELEMENT AlignmentFile (#PCDATA)>																" + "\n" +
"																								" + "\n" +
"<!ELEMENT BasePairs EMPTY>																		" + "\n" +
"<!ATTLIST BasePairs																			" + "\n" +
"	nucID CDATA #REQUIRED																		" + "\n" +
"	bpNucID CDATA #REQUIRED																		" + "\n" +
"	length CDATA #REQUIRED																		" + "\n" +
"	bpName CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!--																							" + "\n" +
"BasePair can be used after above BasePairs is implemented and refers							" + "\n" +
"to base pairs already set, even across strands.												" + "\n" +
"	Type CDATA #IMPLIED																			" + "\n" +
"-->																							" + "\n" +
"<!ELEMENT BasePair EMPTY>																		" + "\n" +
"<!ATTLIST BasePair																				" + "\n" +
"	RefID CDATA #IMPLIED																		" + "\n" +
"	RefIDs CDATA #IMPLIED																		" + "\n" +
"	Type (																						" + "\n" +
"		Canonical |																				" + "\n" +
"		Wobble |																				" + "\n" +
"		MisMatch |																				" + "\n" +
"		Weak |																					" + "\n" +
"		Phosphate |																				" + "\n" +
"		Unknown																					" + "\n" +
"		) 'Unknown'																				" + "\n" +
"	Line5PDeltaX CDATA #IMPLIED																	" + "\n" +
"	Line5PDeltaY CDATA #IMPLIED																	" + "\n" +
"	Line3PDeltaX CDATA #IMPLIED																	" + "\n" +
"	Line3PDeltaY CDATA #IMPLIED																	" + "\n" +
"	LabelDeltaX CDATA #IMPLIED																	" + "\n" +
"	LabelDeltaY CDATA #IMPLIED																	" + "\n" +
"	Label5PSide CDATA #IMPLIED																	" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT NucListData (#PCDATA)>																" + "\n" +
"<!ATTLIST NucListData																			" + "\n" +
"	StartNucID CDATA #IMPLIED																	" + "\n" +
"	DataType (																					" + "\n" +
"		NucChar |																				" + "\n" +
"		NucID.NucChar |																			" + "\n" +
"		NucID.NucChar.XPos.YPos |																" + "\n" +
"		NucChar.XPos.YPos |																		" + "\n" +
"		NucID.NucChar.XPos.YPos.FormatType.BPID													" + "\n" +
"		) 'NucID.NucChar.XPos.YPos.FormatType.BPID'												" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT Label (StringLabel | CircleLabel | Trianglelabel |									" + "\n" +
"	LineLabel | ParallelogramLabel)>															" + "\n" +
"<!ATTLIST Label																				" + "\n" +
"	XPos CDATA #REQUIRED																		" + "\n" +
"	YPos CDATA #REQUIRED																		" + "\n" +
"	Color CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT StringLabel EMPTY>																	" + "\n" +
"<!ATTLIST StringLabel																			" + "\n" +
"	FontName CDATA #IMPLIED																		" + "\n" +
"	FontType CDATA #IMPLIED																		" + "\n" +
"	FontSize CDATA #REQUIRED																	" + "\n" +
"	Angle CDATA #IMPLIED																		" + "\n" +
"	Text CDATA #REQUIRED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT CircleLabel EMPTY>																	" + "\n" +
"<!ATTLIST CircleLabel																			" + "\n" +
"	Arc0 CDATA #IMPLIED																			" + "\n" +
"	Arc1 CDATA #IMPLIED																			" + "\n" +
"	Radius CDATA #IMPLIED																		" + "\n" +
"	LineWidth CDATA #IMPLIED																	" + "\n" +
"	IsOpen CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT TriangleLabel EMPTY>																	" + "\n" +
"<!ATTLIST TriangleLabel																		" + "\n" +
"	TopPtX CDATA #REQUIRED																		" + "\n" +
"	TopPtY CDATA #REQUIRED																		" + "\n" +
"	LeftPtX CDATA #REQUIRED																		" + "\n" +
"	LeftPtY CDATA #REQUIRED																		" + "\n" +
"	RightPtX CDATA #REQUIRED																	" + "\n" +
"	RightPtY CDATA #REQUIRED																	" + "\n" +
"	LineWidth CDATA #IMPLIED																	" + "\n" +
"	IsOpen CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT LineLabel EMPTY>																		" + "\n" +
"<!ATTLIST LineLabel																			" + "\n" +
"	X1 CDATA #REQUIRED																			" + "\n" +
"	Y1 CDATA #REQUIRED																			" + "\n" +
"	LineWidth CDATA #IMPLIED																	" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT ParallelogramLabel EMPTY>															" + "\n" +
"<!ATTLIST ParallelogramLabel																	" + "\n" +
"	Angle1 CDATA #REQUIRED																		" + "\n" +
"	Side1 CDATA #REQUIRED																		" + "\n" +
"	Angle2 CDATA #REQUIRED																		" + "\n" +
"	Side2 CDATA #REQUIRED																		" + "\n" +
"	LineWidth CDATA #IMPLIED																	" + "\n" +
"	IsOpen CDATA #IMPLIED																		" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT LabelList (#PCDATA)>																	" + "\n" +
"																								" + "\n" +
"<!--																							" + "\n" +
"<!ELEMENT Label EMPTY>																			" + "\n" +
"<!ATTLIST Label																				" + "\n" +
"	Type (String | Circle | Triangle | Line) 'String'											" + "\n" +
"	XPos CDATA #REQUIRED																		" + "\n" +
"	YPos CDATA #REQUIRED																		" + "\n" +
"	Color CDATA #IMPLIED																		" + "\n" +
"	FontName CDATA #IMPLIED																		" + "\n" +
"	FontType CDATA #IMPLIED																		" + "\n" +
"	FontSize CDATA #IMPLIED																		" + "\n" +
"	Angle CDATA #IMPLIED																		" + "\n" +
"	Text CDATA #IMPLIED																			" + "\n" +
"	Arc0 CDATA #IMPLIED																			" + "\n" +
"	Arc1 CDATA #IMPLIED																			" + "\n" +
"	Radius CDATA #IMPLIED																		" + "\n" +
"	LineWidth CDATA #IMPLIED																	" + "\n" +
"	IsOpen CDATA #IMPLIED																		" + "\n" +
"	TopPtX CDATA #IMPLIED																		" + "\n" +
"	TopPtY CDATA #IMPLIED																		" + "\n" +
"	LeftPtX CDATA #IMPLIED																		" + "\n" +
"	LeftPtY CDATA #IMPLIED																		" + "\n" +
"	RightPtX CDATA #IMPLIED																		" + "\n" +
"	RightPtY CDATA #IMPLIED																		" + "\n" +
"	X1 CDATA #IMPLIED																			" + "\n" +
"	Y1 CDATA #IMPLIED																			" + "\n" +
">																								" + "\n" +
"-->																							" + "\n" +
"																								" + "\n" +
"<!ELEMENT NucSegment (NucChars)>																" + "\n" +
"<!ATTLIST NucSegment																			" + "\n" +
"	StartNucID CDATA #IMPLIED																	" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT NucChars (#PCDATA)>																	" + "\n" +
"																								" + "\n" +
"<!ELEMENT NucSymbol (#PCDATA)>																	" + "\n" +
"																								" + "\n" +
"<!ELEMENT Nuc (Label*, LabelList?, NucSymbol?)>												" + "\n" +
"<!ATTLIST Nuc																					" + "\n" +
"	NucID CDATA #IMPLIED																		" + "\n" +
"	NucChar CDATA #IMPLIED																		" + "\n" +
"	XPos CDATA #IMPLIED																			" + "\n" +
"	YPos CDATA #IMPLIED																			" + "\n" +
"	Color CDATA #IMPLIED																		" + "\n" +
"	FontID CDATA #IMPLIED																		" + "\n" +
"	FontSize CDATA #IMPLIED																		" + "\n" +
"	FormatType CDATA #IMPLIED																	" + "\n" +
"	BPParent CDATA #IMPLIED																		" + "\n" +
"	BPNucID CDATA #IMPLIED																		" + "\n" +
"	RefID CDATA #IMPLIED																		" + "\n" +
"	RefIDs CDATA #IMPLIED																		" + "\n" +
"	IsHidden CDATA #IMPLIED																		" + "\n" +
"	GroupName CDATA #IMPLIED																	" + "\n" +
"	IsSchematic CDATA #IMPLIED																	" + "\n" +
"	SchematicColor CDATA #IMPLIED																" + "\n" +
"	SchematicLineWidth CDATA #IMPLIED															" + "\n" +
"	SchematicBPLineWidth CDATA #IMPLIED															" + "\n" +
"	SchematicBPGap CDATA #IMPLIED																" + "\n" +
"	SchematicFPGap CDATA #IMPLIED																" + "\n" +
"	SchematicTPGap CDATA #IMPLIED																" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT RNAFile EMPTY>																		" + "\n" +
"<!ATTLIST RNAFile																				" + "\n" +
"	FileName CDATA #REQUIRED																	" + "\n" +
"	FileType (																					" + "\n" +
"		NucChar |																				" + "\n" +
"		NucID.NucChar |																			" + "\n" +
"		NucID.NucChar.XPos.YPos |																" + "\n" +
"		NucID.NucChar.XPos.YPos.FormatType.BPID													" + "\n	" +
"		) 'NucID.NucChar.XPos.YPos.FormatType.BPID'												" + "\n" +
">																								" + "\n" +
"																								" + "\n" +
"<!ELEMENT SceneNodeGeom EMPTY>																	" + "\n" +
"<!ATTLIST SceneNodeGeom																		" + "\n" +
"	CenterX CDATA #IMPLIED																		" + "\n" +
"	CenterY CDATA #IMPLIED																		" + "\n" +
"	Scale CDATA #IMPLIED																		" + "\n" +
">																								" + "\n"
;
*/

"<!ELEMENT ComplexDocument (SceneNodeGeom?," + "\n" +
"   ComplexDocument*, Label*, LabelList*, Complex*," + "\n" +
"   WithComplex*)>" + "\n" +
"<!ATTLIST ComplexDocument" + "\n" +
"   Name CDATA #REQUIRED" + "\n" +
"   Author CDATA #IMPLIED" + "\n" +
"   ExpandUponWrite CDATA #IMPLIED" + "\n" +
"   PSScale CDATA #IMPLIED" + "\n" +
"   LandscapeMode CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT Complex (SceneNodeGeom?, Label*, LabelList*," + "\n" +
"   RNAMolecule*, ProteinMolecule*)>" + "\n" +
"<!ATTLIST Complex" + "\n" +
"   Name CDATA #REQUIRED" + "\n" +
"   Author CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT WithComplex (SceneNodeGeom?, Label*," + "\n" +
"   LabelList*, RNAMolecule*, ProteinMolecule*)>" + "\n" +
"<!ATTLIST WithComplex" + "\n" +
"   Name CDATA #REQUIRED" + "\n" +
"   Author CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT RNAMolecule (SceneNodeGeom?, Label*," + "\n" +
"   LabelList*, Nuc*, NucSegment*, (NucListData | Nuc)*," + "\n" +
"   RNAFile*, BasePairs*, BasePair*, Parent?," + "\n" +
"   AlignmentFile?)>" + "\n" +
"<!ATTLIST RNAMolecule" + "\n" +
"   Name CDATA #REQUIRED" + "\n" +
"   Author CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!-- parent is like for e.coli being the numbering -->" + "\n" +
"<!ELEMENT Parent (RNAMolecule)>" + "\n" +
"<!ELEMENT AlignmentFile (#PCDATA)>" + "\n" +
"" + "\n" +
"<!ELEMENT BasePairs EMPTY>" + "\n" +
"<!ATTLIST BasePairs" + "\n" +
"   nucID CDATA #REQUIRED" + "\n" +
"   bpNucID CDATA #REQUIRED" + "\n" +
"   length CDATA #REQUIRED" + "\n" +
"   bpName CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!--" + "\n" +
"BasePair can be used after above BasePairs is" + "\n" +
"implemented and refers to base pairs already set," + "\n" +
"even across strands." + "\n" +
"-->" + "\n" +
"<!ELEMENT BasePair EMPTY>" + "\n" +
"<!ATTLIST BasePair" + "\n" +
"   RefID CDATA #IMPLIED" + "\n" +
"   RefIDs CDATA #IMPLIED" + "\n" +
"   Type (" + "\n" +
"           Canonical |" + "\n" +
"           Wobble |" + "\n" +
"           MisMatch |" + "\n" +
"           Weak |" + "\n" +
"           Phosphate |" + "\n" +
"           Unknown" + "\n" +
"           ) 'Unknown'" + "\n" +
"   Line5PDeltaX CDATA #IMPLIED" + "\n" +
"   Line5PDeltaY CDATA #IMPLIED" + "\n" +
"   Line3PDeltaX CDATA #IMPLIED" + "\n" +
"   Line3PDeltaY CDATA #IMPLIED" + "\n" +
"   LabelDeltaX CDATA #IMPLIED" + "\n" +
"   LabelDeltaY CDATA #IMPLIED" + "\n" +
"   Label5PSide CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT NucListData (#PCDATA)>" + "\n" +
"<!ATTLIST NucListData" + "\n" +
"   StartNucID CDATA #IMPLIED" + "\n" +
"   DataType (" + "\n" +
"           NucChar |" + "\n" +
"           NucID.NucChar |" + "\n" +
"           NucID.NucChar.XPos.YPos |" + "\n" +
"           NucChar.XPos.YPos |" + "\n" +
"           NucID.NucChar.XPos.YPos.FormatType.BPID" + "\n" +
"           ) 'NucID.NucChar.XPos.YPos.FormatType.BPID'" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT Label (StringLabel | CircleLabel |" + "\n" +
"   Trianglelabel | LineLabel | ParallelogramLabel)>" + "\n" +
"<!ATTLIST Label" + "\n" +
"   XPos CDATA #REQUIRED" + "\n" +
"   YPos CDATA #REQUIRED" + "\n" +
"   Color CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT StringLabel EMPTY>" + "\n" +
"<!ATTLIST StringLabel" + "\n" +
"   FontName CDATA #IMPLIED" + "\n" +
"   FontType CDATA #IMPLIED" + "\n" +
"   FontSize CDATA #REQUIRED" + "\n" +
"   Angle CDATA #IMPLIED" + "\n" +
"   Text CDATA #REQUIRED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT CircleLabel EMPTY>" + "\n" +
"<!ATTLIST CircleLabel" + "\n" +
"   Arc0 CDATA #IMPLIED" + "\n" +
"   Arc1 CDATA #IMPLIED" + "\n" +
"   Radius CDATA #IMPLIED" + "\n" +
"   LineWidth CDATA #IMPLIED" + "\n" +
"   IsOpen CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT TriangleLabel EMPTY>" + "\n" +
"<!ATTLIST TriangleLabel" + "\n" +
"   TopPtX CDATA #REQUIRED" + "\n" +
"   TopPtY CDATA #REQUIRED" + "\n" +
"   LeftPtX CDATA #REQUIRED" + "\n" +
"   LeftPtY CDATA #REQUIRED" + "\n" +
"   RightPtX CDATA #REQUIRED" + "\n" +
"   RightPtY CDATA #REQUIRED" + "\n" +
"   LineWidth CDATA #IMPLIED" + "\n" +
"   IsOpen CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT LineLabel EMPTY>" + "\n" +
"<!ATTLIST LineLabel" + "\n" +
"   X1 CDATA #REQUIRED" + "\n" +
"   Y1 CDATA #REQUIRED" + "\n" +
"   LineWidth CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT ParallelogramLabel EMPTY>" + "\n" +
"<!ATTLIST ParallelogramLabel" + "\n" +
"   Angle1 CDATA #REQUIRED" + "\n" +
"   Side1 CDATA #REQUIRED" + "\n" +
"   Angle2 CDATA #REQUIRED" + "\n" +
"   Side2 CDATA #REQUIRED" + "\n" +
"   LineWidth CDATA #IMPLIED" + "\n" +
"   IsOpen CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT LabelList (#PCDATA)>" + "\n" +
"" + "\n" +
"<!ELEMENT NucSegment (NucChars)>" + "\n" +
"<!ATTLIST NucSegment" + "\n" +
"   StartNucID CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT NucChars (#PCDATA)>" + "\n" +
"" + "\n" +
"<!ELEMENT NucSymbol (#PCDATA)>" + "\n" +
"" + "\n" +
"<!ELEMENT Nuc (Label*, LabelList?, NucSymbol?)>" + "\n" +
"<!ATTLIST Nuc" + "\n" +
"   NucID CDATA #IMPLIED" + "\n" +
"   NucChar CDATA #IMPLIED" + "\n" +
"   XPos CDATA #IMPLIED" + "\n" +
"   YPos CDATA #IMPLIED" + "\n" +
"   Color CDATA #IMPLIED" + "\n" +
"   FontID CDATA #IMPLIED" + "\n" +
"   FontSize CDATA #IMPLIED" + "\n" +
"   FormatType CDATA #IMPLIED" + "\n" +
"   BPParent CDATA #IMPLIED" + "\n" +
"   BPNucID CDATA #IMPLIED" + "\n" +
"   RefID CDATA #IMPLIED" + "\n" +
"   RefIDs CDATA #IMPLIED" + "\n" +
"   IsHidden CDATA #IMPLIED" + "\n" +
"   GroupName CDATA #IMPLIED" + "\n" +
"   IsSchematic CDATA #IMPLIED" + "\n" +
"   SchematicColor CDATA #IMPLIED" + "\n" +
"   SchematicLineWidth CDATA #IMPLIED" + "\n" +
"   SchematicBPLineWidth CDATA #IMPLIED" + "\n" +
"   SchematicBPGap CDATA #IMPLIED" + "\n" +
"   SchematicFPGap CDATA #IMPLIED" + "\n" +
"   SchematicTPGap CDATA #IMPLIED" + "\n" +
"	IsNucPath CDATA #IMPLIED" + "\n" +
"	NucPathColor CDATA #IMPLIED" + "\n" +
"	NucPathLineWidth CDATA #IMPLIED" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT RNAFile EMPTY>" + "\n" +
"<!ATTLIST RNAFile" + "\n" +
"   FileName CDATA #REQUIRED" + "\n" +
"   FileType (" + "\n" +
"           NucChar |" + "\n" +
"           NucID.NucChar |" + "\n" +
"           NucID.NucChar.XPos.YPos |" + "\n" +
"           NucID.NucChar.XPos.YPos.FormatType.BPID" + "\n" +
"           ) 'NucID.NucChar.XPos.YPos.FormatType.BPID'" + "\n" +
">" + "\n" +
"" + "\n" +
"<!ELEMENT SceneNodeGeom EMPTY>" + "\n" +
"<!ATTLIST SceneNodeGeom" + "\n" +
"   CenterX CDATA #IMPLIED" + "\n" +
"   CenterY CDATA #IMPLIED" + "\n" +
"   Scale CDATA #IMPLIED" + "\n" +
">";

public static String
generateStringFromDTDFile()
throws Exception
{
	StringBuffer strBuf = new StringBuffer();
	StringWriter xmlWriter = new StringWriter();
	FileReader complexFile = new FileReader("ComplexXML.dtd");

	// NEED to use newlines so can do properly; or provide as a cArray
	// and have ComplexDTDDefine.java return a string of it.
	int maxChars = 10000;
	char cArray[] = new char[maxChars];
	int b = complexFile.read(cArray, 0, maxChars);
	if (b >= maxChars )
		throw new ComplexException("char array not allocated large enough: " + b);

	strBuf.append("\"");
	for (int i = 0;i < b;i++)
	{
		char c = cArray[i];
		if (c == '\r')
			continue;
		if (c == '\n')
		{
			strBuf.append("\" + \"\\n\" +" + "\n");
			strBuf.append("\"");
			continue;
		}
		strBuf.append("" + c);
		/*
		if (Character.isLetterOrDigit(c))
			continue;
		if (c == ' ')
			continue;
		if (c == '|')
				continue;
		if (c == '#')
			continue;
		if (c == '\n')
			continue;
			continue;
		*/
	}
	return (strBuf.toString());
}

}
