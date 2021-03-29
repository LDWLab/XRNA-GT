## CURRENT PROJECT XRNA-GT
This is a GT fork of the XRNA program.


The XRNA-GT project provides the adjustments to the original XRNA program from the Noller Lab at UC Santa Cruiz: B. Weiser, R. R. Gutell, H.F. Noller http://rna.ucsc.edu/rnacenter/xrna/xrna_download.html (unpubished).


All essential 2D layout editing features have been preserved.


The modified version adds support to several additional formats: 
* SVG (RiboVision,  Faraday Discuss. 2014;169:195-207. doi: 10.1039/c3fd00126a; RNACentral, https://doi.org/10.1093/nar/gky1034); 
* CSV (RiboVision, Faraday Discuss. 2014;169:195-207. doi: 10.1039/c3fd00126a);
* TR (Traveler, BMC Bioinformatics. 2017 Nov 15;18(1):487. doi: 10.1186/s12859-017-1885-4);

and provides export/import functionality between some of them.

## FEATURES ADDED
* Button-facilitated export of XRNA data to SVG format (The labels sticks, dashes representing Watson Crick base pairs are saved  as svg's line objects;
  filled circles representing Wobble pairs and open circles denoting the mismatches saved as svg's circle objects);
	* The process of SVG export occurs as follows:
		* ssview.ComplexSceneIOTab.java::buildGui(Color, int, int)
			* Adds SVG export button to XRNA's GUI
			* Upon button clicked, call ComplexSceneIOTab.java::printSVG(File, boolean);
		* ssview.ComplexSceneIOTab.java::pintSVG(File, boolean)
			* Call this.complexSceneView.runWriteSVGBt();
		* ssview.ComplexSceneView.java::runWriteSVGBt()
			* Call this.runWriteSVGBt(this.complexSceneOutFile);
		* ssview.ComplexSceneView.java::runWriteSVGBt(File outFile)
			* Call this.getComplexScene().printComplexSVG(outFile);
		* ssview.ComplexCollection::printComplexSVG(File)
			* Cache StringWriter strWriter = new StringWriter();
			* Call this.printComplexSVG(new PrintWriter(strWriter));
		* ssview.ComplexCollection::printComplexSVG(PrintWriter outFile) is overwritten by three subclasses:
			* ssview.ComplexSSDataCollection2D::printComplexSVG(PrintWriter)
				* Calculate and Cache the overall bounds of all molecules (minX, minY, maxX, maxY)
					* for each NucCollection2D in this.getCollection()
						* Calculate and Update minX, minY, maxX, maxY
				* for each NucCollection2D nucCollection in this.getCollection()
					* call nucCollection.printComplexSVG(outFile)
				* for each NucCollection2D in this.getCollectin()
					* print the nucleotide bond symbols (lines segments, filled circles, and hollow circles) to an SVG output file using the PrintWriter
			* ssview.ComplexScene2D::printComplexSVG(PrintWriter outFile)
				* for each ComplexCollection collection in this.getCollection()
					* call collection.printComplexSVG(outFile);
			* ssview.NucCollection2D::printComplexSVG(PrintWriter)
				* for each nucleotide
					* print the nucleotide position and character (A, U, C, or G) to an SVG output file using the PrintWriter
					* print the associated label(s)' text and line to an output SVG file using the PrintWriter
* Button-facilitated export of XRNA data to Traveler's TR format ();
* Button-facilitated export of XRNA data to BPSeq format;
* import of SVG-format files to XRNA data (backed by .XRNA files).
	* The algorithms used to correct imperfect SVG data to programmatically ascertain the intention of the creator of the imported SVG files are intricate and tailor-made targeting a plethora of SVG formats.
	* These algorithms are mainly geometric; they perform the following calculations:
		* Calculate the closest point to a given label's line segment
		* Calculate the closest point to a given label's line
		* Analyze errors in algorithmically associated nucleotide-label pairs.
			* A correction is attempted to eliminate the average erroneous index displacement
	* All the code relevant to parsing an SVG to a backing XRNA file is located in ssview.ComplexSceneView.SVGToXRNAParser.
	* The indices of nucleotides to which labels are programmatically tied is an important aspect of this code to get correct.
		* NOTE: it is possible to get the exact coordinates of nucleotide label lines correct, but the associated nucleotide index incorrect.
			* Certain late steps in the algorithmic association of nucleotides with nucleotide labels attempts to eliminate this error by calculating the statistical mode of index offset error and subtracting it from every calculated nucleotide-label index.

## FEATURES EXPANDED
* Allowance for import of XRNA files containing multiple molecules and subsequent conversion to SVG, TR, or CSV formats
	* These import, export processes are facilitated by button UI elements.
	* The import of TR and CSV files begins at ssview.ComplexSceneIOTab.java::buildGui(Color, int, int) and is implemented very similarly to the process detailed above.

## BUG FIXES
* Some scaling / centering issues were improved when exporting to CSV, SVG, TR formats
* When a file is loaded into XRNA, then exported multiple times the data is flipped along the Y axis.
	* This bug fix is in progress.
* Placement of label line segments is incorrect
	* This bug fix is in progress.

The executable (jar) is located at XRNA\xrna-GT.jar. It was compiled using AdoptOpenJDK 14, an open-source JDK alternative.	



