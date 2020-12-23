package ssview;

import jimage.DrawObject;
import jimage.DrawObjectLeafNode;
import jimage.DrawCharObject;
import jimage.DrawFontObject;
import jimage.DrawLineObject;
import jimage.DrawStringObject;
import jimage.DrawCircleObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;

public abstract class
ComplexDefines
{

/********** NUC MODE STUFF *********************************/

public static final int NULL_NUC_MODE =		-1;
public static final int InRNASingleNuc =		0;
public static final int InRNASingleStrand =	1;
public static final int InRNABasePair =		2;
public static final int InRNAHelix =			3;
public static final int InRNAHelicalRun =		4;
public static final int InRNASubDomain =		5;
public static final int InRNACycle =			6;
public static final int InRNAListNucs = 		7;
public static final int InRNASSData =			8;
public static final int InRNAColorUnit =		9;
public static final int InRNANamedGroup =		10;
public static final int InComplex =			11;
public static final int InLabelsOnly =			12;
/*
static final int InComplexArea =		13;
static final int InComplexScene =		14;
*/
public static final int InComplexScene =		13;

/*
InRNASingleNuc
InRNASingleStrand
InRNABasePair
InRNAHelix
InRNAHelicalRun
InRNASubDomain
InRNACycle
InRNAListNucs
InRNASSData
InRNAColorUnit
InRNANamedGroup
InComplex
InLabelsOnly
InComplexScene
*/

	/*
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNASingleStrand :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNABasePair :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNAHelix :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNAHelicalRun :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNASubDomain :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNANamedGroup :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNACycle :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNAColorUnit :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNAListNucs :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InRNASSData :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InComplex :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InLabelsOnly :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InComplexArea :
		this.printConsole("Not Yet");
		break;
	  case ComplexDefines.InComplexScene :
		this.printConsole("Not Yet");
		break;
	  default :
		this.printConsole("Not Yet");
		break;
	}
	*/

/********** BASEPAIR MODE STUFF *********************************/

public static final int BP_TYPE_UNKNOWN = -1;
public static final int BP_TYPE_CANONICAL = 0;
public static final int BP_TYPE_WOBBLE = 1;
public static final int BP_TYPE_MISMATCH = 2;
public static final int BP_TYPE_WEAK = 3;
public static final int BP_TYPE_PHOSPHATE = 4;

public static final int NULL_BASEPAIR = -1;
public static final int AA_BASEPAIR = 0;
public static final int AU_BASEPAIR = 1;
public static final int AG_BASEPAIR = 2;
public static final int AC_BASEPAIR = 3;
public static final int UA_BASEPAIR = 4;
public static final int UU_BASEPAIR = 5;
public static final int UG_BASEPAIR = 6;
public static final int UC_BASEPAIR = 7;
public static final int GA_BASEPAIR = 8;
public static final int GU_BASEPAIR = 9;
public static final int GG_BASEPAIR = 10;
public static final int GC_BASEPAIR = 11;
public static final int CA_BASEPAIR = 12;
public static final int CU_BASEPAIR = 13;
public static final int CG_BASEPAIR = 14;
public static final int CC_BASEPAIR = 15;

public static final double DEFAULT_BP_SYMBOL_CLOSED_RADIUS = 1.0;
public static final double DEFAULT_BP_SYMBOL_OPEN_RADIUS = 1.4;

/*************** Defines for ComplexException **************/

public static final String CREATE_LEVEL_ENTRY_HELIX_MSG = "more than one entry helix, possible pseudo-knot";
public static final String CREATE_SINGLESTRAND_AMBIGUOUS_MSG = "single strand delineator is ambiguous";
public static final String CREATE_HELIX_BASEPAIRS_ERROR_MSG = "can't possibly basepair nucs";
public static final String CREATE_BASEPAIR_ERROR_MSG = "basepair nucs have different basepair types";
public static final String FORMAT_NUC_ERROR_MSG = "nuc not formatted";
public static final String FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG = "nuc out of range";
public static final String FORMAT_NUC_COMPARISON_ERROR_MSG = "unknown comparison criteria";
public static final String FORMAT_SINGLESTRAND_ERROR_MSG = "base pair in single strand";
public static final String FORMAT_HELIX_HAIRPIN_ERROR_MSG = "less than 3 nucs in hairpin";
public static final String FORMAT_INVALID_PARAM_ERROR_MSG = "invalid parameter for format";
public static final String FORMAT_LEVEL_MSG = "bad format for this level";
public static final String FORMAT_LEVEL_NO_HELICES_ERROR_MSG = "no helices set to format";
public static final String FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG = "nuc not a valid compare nuc for format";
public static final String FORMAT_SSDATA_ENDPTS_ERROR_MSG = "endpts different after format";
public static final String FORMAT_SSDATA_BASEPAIR_ERROR_MSG = "invalid base pair distance after format";
public static final String FORMAT_SSDATA_HELIXNUC_ERROR_MSG = "invalid nuc to nuc+1 distance in helix after format";
public static final String FORMAT_SSDATA_NUC_TO_NUC_ERROR_MSG = "invalid nuc to nuc+1 distance after format";
public static final String READ_PS_TO_SSDATA_ERROR_MSG = "Not an old style XRNA postscript output file";
public static final String XRNA_IMAGE_FORMATION_ERROR_MSG = "Error forming XRNA image";
public static final String COMPLEX_SCENE_SSTR_SPLIT_ERROR_MSG = "Error splitting off rna Strand";
public static final String FORMAT_HELIX_INFO_ERROR_MSG = "Badly formed helix information";

// these will be '+'ed with below
public static int errorID = 1;
public static final int CREATE_ERROR = errorID++;
public static final int CREATE_LEVEL_PSEUDOKNOT_ERROR = errorID++;
public static final int CREATE_HELIX_BASEPAIRS_ERROR = errorID++;
public static final int CREATE_STACKED_HELIX_BASEPAIRS_ERROR = errorID++;
public static final int CREATE_SUBDOMAIN_HELIX_BASEPAIRS_ERROR = errorID++;
public static final int FORMAT_ERROR = errorID++;
public static final int FORMAT_BASEPAIR_ERROR = errorID++;
public static final int FORMAT_SINGLESTRAND_ERROR = errorID++;
public static final int FORMAT_HELIX_ERROR = errorID++;
public static final int FORMAT_HELIX_HAIRPIN_ERROR = errorID++;
public static final int READ_PS_ERROR = errorID++;
public static final int XRNA_IMAGE_FORMATION_ERROR = errorID++;
public static final int COMPLEX_SCENE_SSTR_SPLIT_ERROR = errorID++;

public static final int COMPLEX_TYPE_ERROR_BLOCK_SIZE = errorID;

public static final int COMPLEX_TYPE_ERROR = 0x500; // 1280
public static final int RNA_SINGLE_NUC_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNASingleNuc * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_SINGLE_STRAND_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNASingleStrand * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_BASE_PAIR_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNABasePair * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_HELIX_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNAHelix * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_HELICAL_RUN_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNAHelicalRun * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_SUBDOMAIN_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNASubDomain * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_NAMED_GROUP_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNANamedGroup * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_LEVEL_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNACycle * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_COLOR_UNIT_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNAColorUnit * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_LIST_NUCS_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNAListNucs * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_SSDATA_ERROR =
	(COMPLEX_TYPE_ERROR + (InRNASSData * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int COMPLEX_ERROR =
	(COMPLEX_TYPE_ERROR + (InComplex * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
public static final int RNA_LABELS_ONLY =
	(COMPLEX_TYPE_ERROR + (InLabelsOnly * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
/*
static final int COMPLEX_AREA_ERROR =
	(COMPLEX_TYPE_ERROR + (InComplexArea * COMPLEX_TYPE_ERROR_BLOCK_SIZE));
*/
public static final int COMPLEX_SCENE_ERROR =
	(COMPLEX_TYPE_ERROR + (InComplexScene * COMPLEX_TYPE_ERROR_BLOCK_SIZE));

public static String
lookupComplexErrorBlock(int error)
{
	if ((error >= RNA_SINGLE_NUC_ERROR) && (error < RNA_SINGLE_STRAND_ERROR))
			return ("RNA_SINGLE_NUC_ERROR");
	if ((error >= RNA_SINGLE_STRAND_ERROR) && (error < RNA_BASE_PAIR_ERROR))
			return ("RNA_SINGLE_STRAND_ERROR");
	if ((error >= RNA_BASE_PAIR_ERROR) && (error < RNA_HELIX_ERROR))
			return ("RNA_BASE_PAIR_ERROR");
	if ((error >= RNA_HELIX_ERROR) && (error < RNA_HELICAL_RUN_ERROR))
			return ("RNA_HELIX_ERROR");
	if ((error >= RNA_HELICAL_RUN_ERROR) && (error < RNA_NAMED_GROUP_ERROR))
			return ("RNA_HELICAL_RUN_ERROR");
	if ((error >= RNA_NAMED_GROUP_ERROR) && (error < RNA_LEVEL_ERROR))
			return ("RNA_NAMED_GROUP_ERROR");
	if ((error >= RNA_LEVEL_ERROR) && (error < RNA_LIST_NUCS_ERROR))
			return ("RNA_LEVEL_ERROR");
	if ((error >= RNA_LIST_NUCS_ERROR) && (error < RNA_SSDATA_ERROR))
			return ("RNA_LIST_NUCS_ERROR");
	if ((error >= RNA_SSDATA_ERROR) && (error < COMPLEX_ERROR))
			return ("RNA_SSDATA_ERROR");
	if ((error >= COMPLEX_ERROR) && (error < RNA_LABELS_ONLY))
			return ("COMPLEX_ERROR");

	/*
	if ((error >= RNA_LABELS_ONLY) && (error < COMPLEX_AREA_ERROR))
			return ("RNA_LABELS_ONLY");
	if ((error >= COMPLEX_AREA_ERROR) && (error < COMPLEX_SCENE_ERROR))
			return ("COMPLEX_AREA_ERROR");
	*/
	if ((error >= RNA_LABELS_ONLY) && (error < COMPLEX_SCENE_ERROR))
			return ("RNA_LABELS_ONLY");

	return ("COMPLEX_SCENE_ERROR");
}

public static String
nucModeDefineToString(int nucMode)
{
	switch (nucMode)
	{
		case InRNASingleNuc :
			return ("rna single nuc");
		case InRNASingleStrand :
			return ("rna single strand");
		case InRNABasePair :
			return ("rna basepair");
		case InRNAHelix :
			return ("rna helix");
		case InRNAHelicalRun :
			return ("rna stacked helix");
		case InRNASubDomain :
			return ("rna sub-domain");
		case InRNACycle :
			return ("rna cycle");
		case InRNAListNucs :
			return ("rna list nucs");
		case InRNASSData :
			return ("rna strand");
		case InRNAColorUnit :
			return ("rna color unit");
		case InRNANamedGroup :
			return ("rna named group");
		case InComplex :
			return ("rna strand group");
		case InLabelsOnly :
			return ("labels only");
		case InComplexScene :
			return ("entire scene");
		case NULL_NUC_MODE :
		default :
			return (null);
	}
}

public static int
drawObjToNucModeDefine(DrawObject drwObj)
{
	if ((drwObj instanceof NucNode) || (drwObj instanceof Nuc2D))
		return (InRNASingleNuc);

	if ((drwObj instanceof RNASingleStrand) || (drwObj instanceof RNASingleStrand2D))
		return (InRNASingleStrand);
	if ((drwObj instanceof RNABasePair) || (drwObj instanceof RNABasePair2D))
		return (InRNABasePair);
	if ((drwObj instanceof RNAHelix) || (drwObj instanceof RNAHelix2D))
		return (InRNAHelix);
	if ((drwObj instanceof RNAStackedHelix) || (drwObj instanceof RNAStackedHelix2D))
		return (InRNAHelicalRun);
	if ((drwObj instanceof RNASubDomain) || (drwObj instanceof RNASubDomain2D))
		return (InRNASubDomain);
	if ((drwObj instanceof RNANamedGroup) || (drwObj instanceof RNANamedGroup2D))
		return (InRNANamedGroup);
	if ((drwObj instanceof RNAColorUnit) || (drwObj instanceof RNAColorUnit2D))
		return (InRNAColorUnit);
	if ((drwObj instanceof RNAListNucs) || (drwObj instanceof RNAListNucs2D))
		return (InRNAListNucs);
	if ((drwObj instanceof SSData) || (drwObj instanceof SSData2D))
		return (InRNASSData);
	if ((drwObj instanceof ComplexSSDataCollection) || (drwObj instanceof ComplexSSDataCollection2D))
		return (InComplex);
	if ((drwObj instanceof DrawObjectLeafNode) ||
		(drwObj instanceof DrawCharObject) ||
		(drwObj instanceof DrawFontObject) ||
		(drwObj instanceof DrawLineObject) ||
		(drwObj instanceof DrawStringObject) ||
		(drwObj instanceof DrawCircleObject) ||
		(drwObj instanceof DrawTriangleObject) ||
		(drwObj instanceof DrawParallelogramObject))
		return (InLabelsOnly);
	/*
	if ((drwObj instanceof RNAComplexArea) || (drwObj instanceof RNAComplexArea2D))
		return (InComplexArea);
	*/
	if ((drwObj instanceof ComplexScene) || (drwObj instanceof ComplexScene2D))
		return (InComplexScene);
	
	return (NULL_NUC_MODE);
}


/********************* End Defines for ComplexException ***************************/

public static final int MODE_ATTRIBUTE_SIZE = (InComplexScene + 1); // all the pick modes
public static final int ATTRIBUTE_TYPE_COUNT = 2; // color, clear, ...
public static final int MODE_BLOCK_SIZE = MODE_ATTRIBUTE_SIZE * ATTRIBUTE_TYPE_COUNT;
public static final int TRANSFORM_MODE = (0 * MODE_BLOCK_SIZE);
public static final int ANNOTATE_MODE = (1 * MODE_BLOCK_SIZE);
public static final int EDIT_MODE = (2 * MODE_BLOCK_SIZE);
public static final int ANNOTATE_COLOR_MODE = ANNOTATE_MODE + (0 * MODE_ATTRIBUTE_SIZE);
public static final int ANNOTATE_COLOR_RNA_SINGLE_NUC = ANNOTATE_COLOR_MODE + InRNASingleNuc;
public static final int ANNOTATE_COLOR_RNA_SINGLE_STRAND = ANNOTATE_COLOR_MODE + InRNASingleStrand;
public static final int ANNOTATE_COLOR_RNA_BASE_PAIR = ANNOTATE_COLOR_MODE + InRNABasePair;
public static final int ANNOTATE_COLOR_RNA_HELIX = ANNOTATE_COLOR_MODE + InRNAHelix;
public static final int ANNOTATE_COLOR_RNA_HELICAL_RUN = ANNOTATE_COLOR_MODE + InRNAHelicalRun;
public static final int ANNOTATE_COLOR_RNA_SUBDOMAIN = ANNOTATE_COLOR_MODE + InRNASubDomain;
public static final int ANNOTATE_COLOR_RNA_NAMED_GROUP = ANNOTATE_COLOR_MODE + InRNANamedGroup;
public static final int ANNOTATE_COLOR_RNA_COLOR_UNIT = ANNOTATE_COLOR_MODE + InRNAColorUnit;
public static final int ANNOTATE_COLOR_RNA_LIST_NUCS = ANNOTATE_COLOR_MODE + InRNAListNucs;
public static final int ANNOTATE_COLOR_RNA_SSDATA = ANNOTATE_COLOR_MODE + InRNASSData;
public static final int ANNOTATE_COLOR_COMPLEX = ANNOTATE_COLOR_MODE + InComplex;
public static final int ANNOTATE_COLOR_LABELS_ONLY = ANNOTATE_COLOR_MODE + InLabelsOnly;
/*
static final int ANNOTATE_COLOR_COMPLEX_AREA = ANNOTATE_COLOR_MODE + InComplexArea;
*/
public static final int ANNOTATE_COLOR_COMPLEX_SCENE = ANNOTATE_COLOR_MODE + InComplexScene;

public static final int ANNOTATE_CLEAR_MODE = ANNOTATE_MODE + (1 * MODE_ATTRIBUTE_SIZE);
public static final int ANNOTATE_CLEAR_RNA_SINGLE_NUC = ANNOTATE_CLEAR_MODE + InRNASingleNuc;
public static final int ANNOTATE_CLEAR_RNA_SINGLE_STRAND = ANNOTATE_CLEAR_MODE + InRNASingleStrand;
public static final int ANNOTATE_CLEAR_RNA_BASE_PAIR = ANNOTATE_CLEAR_MODE + InRNABasePair;
public static final int ANNOTATE_CLEAR_RNA_HELIX = ANNOTATE_CLEAR_MODE + InRNAHelix;
public static final int ANNOTATE_CLEAR_RNA_HELICAL_RUN = ANNOTATE_CLEAR_MODE + InRNAHelicalRun;
public static final int ANNOTATE_CLEAR_RNA_SUBDOMAIN = ANNOTATE_CLEAR_MODE + InRNASubDomain;
public static final int ANNOTATE_CLEAR_RNA_NAMED_GROUP = ANNOTATE_CLEAR_MODE + InRNANamedGroup;
public static final int ANNOTATE_CLEAR_RNA_COLOR_UNIT = ANNOTATE_CLEAR_MODE + InRNAColorUnit;
public static final int ANNOTATE_CLEAR_RNA_LIST_NUCS = ANNOTATE_CLEAR_MODE + InRNAListNucs;
public static final int ANNOTATE_CLEAR_RNA_SSDATA = ANNOTATE_CLEAR_MODE + InRNASSData;
public static final int ANNOTATE_CLEAR_COMPLEX = ANNOTATE_CLEAR_MODE + InComplex;
public static final int ANNOTATE_CLEAR_LABELS_ONLY = ANNOTATE_CLEAR_MODE + InLabelsOnly;
/*
static final int ANNOTATE_CLEAR_COMPLEX_AREA = ANNOTATE_CLEAR_MODE + InComplexArea;
*/
public static final int ANNOTATE_CLEAR_COMPLEX_SCENE = ANNOTATE_CLEAR_MODE + InComplexScene;

/************* Action Commands **************************/

public static final String IO_EXTRACT_RNA_FEATURE = "io_extract_rna_feature";
public static final String IO_WRITE_STRUCTURE = "io_write_structure";
public static final String IO_PRINT_STRUCTURE = "io_print_structure";

public static final String ANNOTATE_COLOR = "annotate_color";
public static final String ANNOTATE_FONT = "annotate_font";
public static final String ANNOTATE_CIRCLE = "annotate_circle";
public static final String ANNOTATE_TRIANGLE = "annotate_triangle";
public static final String ANNOTATE_PARALLELOGRAM = "annotate_parallelogram";
public static final String ANNOTATE_LINE = "annotate_line";
public static final String ANNOTATE_LOWER_CASE = "annotate_lower_case";
public static final String ANNOTATE_SCHEMATICIZE = "annotate_schematicize";
public static final String ANNOTATE_CLEAR_SCHEMATICIZE = "annotate_clear_schematicize";
public static final String ANNOTATE_NUC_PATH = "annotate_nuc_path";
public static final String ANNOTATE_CLEAR_NUC_PATH = "annotate_clear_nuc_path";
public static final String ANNOTATE_BP_GAP_SCHEMATICIZE = "annotate_bp_gap_schematicize";
public static final String ANNOTATE_FP_GAP_SCHEMATICIZE = "annotate_fp_gap_schematicize";
public static final String ANNOTATE_TP_GAP_SCHEMATICIZE = "annotate_tp_gap_schematicize";
public static final String ANNOTATE_CLEAR = "annotate_clear";

public static final String EDIT_DELETE_NUC_LABELS = "edit_delete_nuc_labels";
public static final String EDIT_ADD_NUC_LABELS = "edit_add_nuc_labels";
public static final String EDIT_HIDE_DRAWOBJECT = "edit_hide_drawobject";
public static final String EDIT_ADD_TO_NAMED_GROUP = "edit_add_named_group";

public static final String FORMAT_NUCS = "format_nucs";
public static final String FORMAT_NUCS_IN_PLACE = "format_nucs_in_place";
public static final String FORMAT_UNSET_BASE_PAIRS = "format_unset_base_pairs";
public static final String FORMAT_RESET_HELICES = "format_reset_helices";

/************* End Action Commands **************************/

/************* Symbol Type Defines **************************/

public static final int CIRCLE_SYMBOL_TYPE = 0;
public static final int TRIANGLE_SYMBOL_TYPE = 1;
public static final int PARALLELOGRAM_SYMBOL_TYPE = 2;
public static final int LINE_SYMBOL_TYPE = 3;

/************* End Symbol Type Defines **************************/

/************* Nucleotide format Defines **************************/

public static final int DEFAULT_NUC_FONT_SIZE = 8;
public static final double NUC_TO_NEXTNUC_DISTANCE = 8.0;
public static final double RNA_HELIX_BASE_DISTANCE = 8.0;
public static final double RNA_BASEPAIR_DISTANCE = 20.0;
public static final double RNA_MISMATCH_BASEPAIR_DISTANCE = 10.0;//26.0;
public static final double RNA_DISTANCE_TOLERANCE = 0.005;

public static final double NUCLABEL_LINE_WIDTH = 0.2;
public static final double NUCLABEL_LINE_LENGTH = 6.0;
public static final double NUCLABEL_NUC_TO_LINE_DISTANCE = 2.0;
public static final double NUCLABEL_LINE_TO_NUMBER_DISTANCE = 2.0;

/************* End Nucleotide format Defines **************************/

/************* Undo Defines **************************/

public static final int UNDO_TYPE_NULL = 0;
public static final int UNDO_TYPE_POSITION = 1;

/************* End Undo Defines **************************/

}
