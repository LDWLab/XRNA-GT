package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ssview.Vector2;

import java.util.HashMap;
import java.util.Arrays;
import java.awt.Color;

import util.Tuple2;
import util.Tree;
import java.util.function.BiConsumer;

import java.awt.geom.Rectangle2D;

public class SVGParser extends XRNAData {
    public static void main(String[] args) {
        String
            inputSVGFilePath = "C:\\Users\\caede\\OneDrive\\Desktop\\Input.svg",
            outputXRNAFilePath = "C:\\Users\\caede\\OneDrive\\Desktop\\Output.xrna";
        SVGParser
            parser = new SVGParser(inputSVGFilePath);
        // System.out.println(parser.groups.root.datum.);
        parser.printToXRNAFile(outputXRNAFilePath);

        // parser.display();
        // BiConsumer<Tree<Group>.Node, String>
        //     foo = (Tree<Group>.Node node, String printlnPrefix) -> {
        //         int
        //             numLines = node.datum.lines.size(),
        //             numTexts = node.datum.texts.size();
        //         if (numLines != 0 || numTexts != 1) {
        //             System.out.println(printlnPrefix + node.datum.id + "\n" + printlnPrefix + "\t#lines: " + numLines + "\n" + printlnPrefix + "\t#texts: " + numTexts);
        //         }
        //     };
        // parser.groups.iterationHelperSkipRoot(foo, "", (String printlnPrefix) -> printlnPrefix + "\t");
    }

    private Tree<Group>
        groups;

    private HashMap<String, Style>
        labeledStyles;

    public SVGParser(String inputSVGFilePath) {
        try {
            String
                fileContents = Files.readString(Paths.get(inputSVGFilePath));
            fileContents = parseExternalContent(fileContents, "<!--", "-->");
            Tree<RecursiveMatchIndices>
                recursiveIndices = parseRecursiveContent(fileContents, "<svg[^>]*>", "<\\/svg\\s*>\n?");
            if (recursiveIndices.root.children.size() == 1 && recursiveIndices.root.children.get(0).children.size() == 0) {
                Matcher
                    svgHeaderMatcher = Pattern.compile("<svg[^>]*>").matcher(fileContents);
                svgHeaderMatcher.find();
                Matcher
                    svgElementMatcher = Pattern.compile(HEADER_ELEMENT_REGEX).matcher(svgHeaderMatcher.group());
                while (svgElementMatcher.find()) {
                    String
                        svgElement = svgElementMatcher.group();
                    int
                        equalsSignIndex = svgElement.indexOf("=");
                    String
                        label = svgElement.substring(0, equalsSignIndex).stripTrailing(),
                        datum = svgElement.substring(equalsSignIndex + 1).stripLeading().replace("\"", "");
                    if (label.equalsIgnoreCase("style")) {
                        datum = datum.stripTrailing();
                        String[]
                            styleData = datum.split(";");
                        // System.out.println("Datum: " + datum + " styleData: " + Arrays.toString(styleData));
                        for (String styleDatum : styleData) {
                            int
                                colonIndex = styleDatum.indexOf(':');
                            label = styleDatum.substring(0, colonIndex).stripTrailing();
                            datum = styleDatum.substring(colonIndex + 1).stripLeading();
                            if (label.equalsIgnoreCase("font-size")) {
                                DEFAULT_FONT_SIZE = Integer.parseInt(datum.replace("px", ""));
                                // System.out.println("Default font size: " + DEFAULT_FONT_SIZE);
                            }
                        }
                    }
                }
                // For now, only deal with a single SVG element.
                RecursiveMatchIndices
                    svgIndices = recursiveIndices.root.children.get(0).datum;
                String
                    svgBody = fileContents.substring(svgIndices.contentStartIndex, svgIndices.contentEndIndex);
                this.labeledStyles = new HashMap<String, Style>();
                Matcher
                    styleOpenMatcher = Pattern.compile("<style[^>]*>").matcher(svgBody),
                    styleCloseMatcher = Pattern.compile("<\\/style\\s*>\n?").matcher(svgBody);
                while (styleOpenMatcher.find()) {
                    styleCloseMatcher.find();
                    String
                        styleBody = svgBody.substring(styleOpenMatcher.end(), styleCloseMatcher.start());
                    Matcher
                        stylesMatcher = Pattern.compile("\\.\\w[\\w\\d]*\\{[^\\}]*}").matcher(styleBody);
                    int
                        priority = 0;
                    while (stylesMatcher.find()) {
                        String
                            styleString = stylesMatcher.group();
                        Style
                            style = new Style();
                        int
                            openBracketIndex = styleString.indexOf('{');
                        String[]
                            styleDatums = styleString.substring(openBracketIndex + 1, styleString.indexOf('}')).split(";");
                        for (String styleDatum : styleDatums) {
                            styleDatum = styleDatum.replaceAll("\\s+", "");
                            int
                                colonIndex = styleDatum.indexOf(':');
                            if (colonIndex != -1) {
                                String
                                    label = styleDatum.substring(0, colonIndex),
                                    datum = styleDatum.substring(colonIndex + 1);
                                if (label.equalsIgnoreCase("fill")) {
                                    style.fill = parseColor(datum);
                                } else if (label.equalsIgnoreCase("font-size")) {
                                    style.fontSize = Double.parseDouble(datum.replace("px", ""));
                                } else if (label.equalsIgnoreCase("font-family")) {
                                    style.fontFamily = datum.replace("'", "");
                                } else if (label.equalsIgnoreCase("stroke")) {
                                    style.stroke = parseColor(datum);
                                } else if (label.equalsIgnoreCase("stroke-width")) {
                                    style.strokeWidth = Double.parseDouble(datum);
                                }
                            }
                        }
                        style.priority = priority;
                        String
                            label = styleString.substring(1, openBracketIndex);
                        style.label = label;
                        this.labeledStyles.put(label, style);
                        priority++;
                    }
                }
                parseGroups(svgBody);
            } else {
                throw new UnsupportedOperationException("Multiple SVG elements per file is currently unsupported.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Tuple2<Integer, Integer>> indicesBetweenChildren(Tree<RecursiveMatchIndices>.Node node) {
        int
            previousChildCloseIndex = node.datum.contentStartIndex;
        ArrayList<Tuple2<Integer, Integer>>
            indices = new ArrayList<>();
        for (Tree<RecursiveMatchIndices>.Node child : node.children) {
            indices.add(new Tuple2<>(previousChildCloseIndex, child.datum.headerStartIndex));
            previousChildCloseIndex = child.datum.contentEndIndex;
        }
        indices.add(new Tuple2<>(previousChildCloseIndex, node.datum.contentEndIndex));
        return indices;
    }

    public void parseGroups(String svgContents) {
        Tree<RecursiveMatchIndices>
            recursiveIndices = parseRecursiveContent(svgContents, "<g[^>]*>", "<\\/g\\s*>\n?");
        this.groups = recursiveIndices.transform((Tree<RecursiveMatchIndices>.Node node, Group parentGroup) -> {
            Group
                childGroup = new Group();
            RecursiveMatchIndices
                indices = node.datum;
            if (indices == null) {
                childGroup.id = "Dummy root node";
            } else {
                String
                    groupHeader = svgContents.substring(indices.headerStartIndex, indices.contentStartIndex);
                Matcher
                    groupHeaderElementMatcher = Pattern.compile(HEADER_ELEMENT_REGEX).matcher(groupHeader);
                while (groupHeaderElementMatcher.find()) {
                    String
                        groupHeaderElement = groupHeaderElementMatcher.group();
                    int
                        equalsSignIndex = groupHeaderElement.indexOf("=");
                    String
                        label = groupHeaderElement.substring(0, equalsSignIndex).stripTrailing(),
                        datum = groupHeaderElement.substring(equalsSignIndex + 1).stripLeading();
                    if (label.equalsIgnoreCase("id")) {
                        childGroup.id = datum.toLowerCase();
                    } else if (label.equalsIgnoreCase("transform")) {
                        childGroup.transform = AffineMatrix.multiply(parentGroup.transform, parseAffineMatrix(datum.strip()));
                    }
                }
                for (Tuple2<Integer, Integer> indicesBetweenChildren : indicesBetweenChildren(node)) {
                    String
                        betweenChildrenContents = svgContents.substring(indicesBetweenChildren.t0, indicesBetweenChildren.t1);
                    childGroup.parseLines(betweenChildrenContents);
                    childGroup.parseTexts(betweenChildrenContents);
                }
            }
            return childGroup;
        });
    }

    private static int
        fontChangedSerializer = 0;

    public static String parseExternalContent(String fileContents, String startTag, String endTag) {
        String
            externalContent = "";
        Matcher
            commentOpenMatcher = Pattern.compile(startTag).matcher(fileContents),
            commentCloseMatcher = Pattern.compile(endTag).matcher(fileContents);
        int
            previousCommentCloseIndex = 0;
        while (commentOpenMatcher.find()) {
            commentCloseMatcher.find();
            externalContent += fileContents.substring(previousCommentCloseIndex, commentOpenMatcher.start());
            previousCommentCloseIndex = commentCloseMatcher.end();
        }
        externalContent += fileContents.substring(previousCommentCloseIndex);
        return externalContent;
    }

    public static Tree<RecursiveMatchIndices> parseRecursiveContent(String contents, String startTag, String endTag) {
        Matcher
            openMatcher = Pattern.compile(startTag).matcher(contents),
            closeMatcher = Pattern.compile(endTag).matcher(contents);
        ArrayList<Tuple2<Integer, Integer>>
            openIndices = new ArrayList<>(),
            closeIndices = new ArrayList<>();
        while (openMatcher.find()) {
            openIndices.add(new Tuple2<>(openMatcher.start(), openMatcher.end()));
        }
        while (closeMatcher.find()) {
            closeIndices.add(new Tuple2<>(closeMatcher.start(), closeMatcher.end()));
        }
        if (openIndices.size() != closeIndices.size()) {
            throw new RuntimeException("Bad SVG file: unbalanced open/close tags.");
        }
        ArrayList<Tuple2<Tuple2<Integer, Integer>, Boolean>>
            svgMatches = new ArrayList<>();
        openIndices.forEach(indexPair -> svgMatches.add(new Tuple2<>(indexPair, true)));
        closeIndices.forEach(indexPair -> svgMatches.add(new Tuple2<>(indexPair, false)));
        svgMatches.sort((a, b) -> Integer.compare(a.t0.t0, b.t0.t0));
        Tree<RecursiveMatchIndices>
            indicesTree = new Tree<>(null);
        Tree<RecursiveMatchIndices>.Node
            parent = indicesTree.root;
        for (Tuple2<Tuple2<Integer, Integer>, Boolean> indexPair : svgMatches) {
            Tuple2<Integer, Integer>
                indices = indexPair.t0;
            if (indexPair.t1) {
                RecursiveMatchIndices
                    match = new RecursiveMatchIndices(indices.t0, indices.t1);
                parent = indicesTree.new Node(parent, match);
                // parent.children.add(new Tree<Tuple2<Integer, Integer>>.Node(parent, indexPair.t0));
            } else if (parent != null) {
                parent.datum.contentEndIndex = indices.t0;
                parent = parent.parent;
            } else {
                throw new RuntimeException("Bad SVG file: improperly ordered open/close tags.");
            }
        }
        return indicesTree;
    }

    private static class RecursiveMatchIndices {
        private int
            headerStartIndex,
            contentStartIndex,
            contentEndIndex;

        private RecursiveMatchIndices(int headerStartIndex, int contentStartIndex) {
            this.headerStartIndex = headerStartIndex;
            this.contentStartIndex = contentStartIndex;
        }
    }

    @Override
    public ArrayList<ArrayList<Text>> inputNucleotideTexts() {
        ArrayList<ArrayList<Text>>
            inputNucleotideTexts = new ArrayList<>();
        this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
            Group
                g = node.datum;
            if (g.id != null && (g.id.contains("sequence") || g.id.contains("letters"))) {
                inputNucleotideTexts.add(g.texts);
            }
        });
        // Nonstandard format. Treat all texts which match "A"|"C"|"G"|"U" as nucleotides.
        if (inputNucleotideTexts.size() == 0) {
            ArrayList<Text>
                allNucleotideTexts = new ArrayList<Text>();
            inputNucleotideTexts.add(allNucleotideTexts);
            this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
                Group
                    g = node.datum;
                for (Text text : g.texts) {
                    String
                        content = text.content.strip();
                    if (Pattern.matches("(A|C|G|U)", content)) {
                        allNucleotideTexts.add(text);
                    }
                }
            });
        }
        return inputNucleotideTexts;
    }

    @Override
    public ArrayList<Line> inputNucleotideLines() {
        ArrayList<Line>
            inputNucleotideLines = new ArrayList<Line>();
        this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
            Group
                g = node.datum;
            if (g.id != null && g.id.contains("nucleotide_lines")) {
                inputNucleotideLines.addAll(g.lines);
            }
        });
        if (inputNucleotideLines.size() == 0) {
            this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
                Group
                    g = node.datum;
                for (Line line : g.lines) {
                    String
                        editedClassLabel = line.classLabel.strip().toLowerCase();
                    if (!editedClassLabel.contains("numbering-line") && !editedClassLabel.contains("gray")) {
                        inputNucleotideLines.add(line);
                    }
                }
            });
        }
        return inputNucleotideLines;
    }

    @Override
    public ArrayList<Text> inputHelixTexts() {
        ArrayList<Text>
            inputHelixTexts = new ArrayList<Text>();
        this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
            Group
                g = node.datum;
            if (g.id != null && (g.id.contains("helix_labels"))) {
                inputHelixTexts.addAll(g.texts);
            }
        });
        return inputHelixTexts;
    }

    @Override
    public ArrayList<Text> inputLabelTexts() {
        ArrayList<Text>
            inputLabelTexts = new ArrayList<Text>();
        this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
            Group
                g = node.datum;
            if (g.id != null && g.id.contains("labels_text")) {
                inputLabelTexts.addAll(g.texts);
            }
        });
        if (inputLabelTexts.size() == 0) {
            this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
                Group
                    g = node.datum;
                for (Text text : g.texts) {
                    if (text.classLabel.strip().toLowerCase().contains("numbering-label") && Pattern.matches("\\d+", text.content.strip())) {
                        inputLabelTexts.add(text);
                    }
                }
            });
        }
        return inputLabelTexts;
    }

    @Override
    public ArrayList<Line> inputLabelLines() {
        ArrayList<Line>
            inputLabelLines = new ArrayList<Line>();
        this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
            Group
                g = node.datum;
            if (g.id != null && g.id.contains("labels_lines")) {
                inputLabelLines.addAll(g.lines);
            }
        });
        if (inputLabelLines.size() == 0) {
            this.groups.iterationHelperSkipRoot((Tree<Group>.Node node) -> {
                Group
                    g = node.datum;
                for (Line line : g.lines) {
                    if (line.classLabel.strip().toLowerCase().contains("numbering-line")) {
                        inputLabelLines.add(line);
                    }
                }
            });
        }
        return inputLabelLines;
    }    

    @Override
    public Vector2 getLocusForNucleotideBonding(Text text) {
        Rectangle2D.Double
            bounds = getBounds(text);
        return new Vector2(bounds.getCenterX(), bounds.getCenterY());
    }

    public static Color parseColor(String colorString) {
        Matcher
            colorMatcher = Pattern.compile("#[A-F\\d]{1,8}").matcher(colorString);
        if (colorMatcher.matches()) {
            return Color.decode(colorString);
        } else {
            colorMatcher = Pattern.compile("rgb\\(\\d{1,3},\\d{1,3},\\d{1,3}\\)").matcher(colorString);
            if (colorMatcher.matches()) {
                String
                    rgbSplit[] = colorString.substring(colorString.indexOf("(") + 1).split(",");
                return new Color(Integer.parseInt(rgbSplit[0]), Integer.parseInt(rgbSplit[1]), Integer.parseInt(rgbSplit[2]));
            } else {
                colorMatcher = Pattern.compile("rgba\\(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}\\)").matcher(colorString);
                if (colorMatcher.matches()) {
                    String
                        rgbaSplit[] = colorString.substring(colorString.indexOf("(") + 1).split(",");
                    return new Color(Integer.parseInt(rgbaSplit[0]), Integer.parseInt(rgbaSplit[1]), Integer.parseInt(rgbaSplit[2]), Integer.parseInt(rgbaSplit[3]));
                } else {
                    if (colorString.equals("none")) {
                        return new Color(0, 0, 0, 0);
                    } else {
                        // TODO: Handle HSL, HSLA, and predefined color constants.
                        // colorMatcher = Pattern.compile("hsl\\(\\d{1,3},\\d{1,3},\\d{1,3})").matcher(colorString);
                        // if (colorMatcher.matches()) {
                        //     String
                        //         hslSplit[] = colorString.substring(colorString.indexOf("(") + 1).split(",");
                        //     return new Color();
                        // } else {
                            
                        // }
                        throw new RuntimeException("Unrecognized CSS Color format: \"" + colorString + "\"");
                    }
                }
            }
        }
    }

    public static String generateRegex(String prefix, String delimiter, String suffix, boolean innerWhitespace, boolean outerWhitespace) {
        String
            regex;
        if (innerWhitespace) {
            regex = prefix + "\\s*" + delimiter + "\\s*" + suffix;
        } else {
            regex = prefix + delimiter + suffix;
        }
        if (outerWhitespace) {
            regex = "\\s*" + regex + "\\s*";
        }
        return regex;
    }

    public static AffineMatrix parseAffineMatrix(String affineMatrixString) {
        Matcher
            affineMatrixMatcher = Pattern.compile(generateRegex("matrix\\s*\\(\\s*", (FLOATING_POINT_REGEX + "\\s+").repeat(5), FLOATING_POINT_REGEX + "\\s*\\)", false, true)).matcher(affineMatrixString);
        if (affineMatrixMatcher.matches()) {
            String
                matrixString = affineMatrixMatcher.group(),
                entries[] = matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip().split("\\s+");
            return new AffineMatrix(Double.parseDouble(entries[0].strip()), Double.parseDouble(entries[1].strip()), Double.parseDouble(entries[2].strip()), Double.parseDouble(entries[3].strip()), Double.parseDouble(entries[4].strip()), Double.parseDouble(entries[5].strip()));
        } else {
            affineMatrixMatcher = Pattern.compile("none").matcher(affineMatrixString);
            if (affineMatrixMatcher.matches()) {
                return AffineMatrix.IDENTITY;
            } else {
                affineMatrixMatcher = Pattern.compile(generateRegex("translate\\(", FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                if (affineMatrixMatcher.matches()) {
                    String
                        matrixString = affineMatrixMatcher.group(),
                        entries[] = matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).split("\\s+");
                    return new AffineMatrix(1d, 0d, 0d, 1d, Double.parseDouble(entries[0].strip()), Double.parseDouble(entries[1].strip()));
                } else {
                    affineMatrixMatcher = Pattern.compile(generateRegex("translateX\\(", FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                    if (affineMatrixMatcher.matches()) {
                        String
                            matrixString = affineMatrixMatcher.group();
                        return new AffineMatrix(1d, 0d, 0d, 1d, Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip()), 0d);
                    } else {
                        affineMatrixMatcher = Pattern.compile(generateRegex("translateY\\(", FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                        if (affineMatrixMatcher.matches()) {
                            String
                                matrixString = affineMatrixMatcher.group();
                            return new AffineMatrix(1d, 0d, 0d, 1d, 0d, Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip()));
                        } else {
                            affineMatrixMatcher = Pattern.compile(generateRegex("scale\\(", FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                            if (affineMatrixMatcher.matches()) {
                                String
                                    matrixString = affineMatrixMatcher.group(),
                                    entries[] = matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).split("\\s+");
                                return new AffineMatrix(Double.parseDouble(entries[0].strip()), 0d, 0d, Double.parseDouble(entries[1].strip()), 0d, 0d);
                            } else {
                                affineMatrixMatcher = Pattern.compile(generateRegex("scaleX\\(", FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                                if (affineMatrixMatcher.matches()) {
                                    String
                                        matrixString = affineMatrixMatcher.group();
                                    return new AffineMatrix(Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip()), 0d, 0d, 1d, 0d, 0d);
                                } else {
                                    affineMatrixMatcher = Pattern.compile(generateRegex("scaleY\\(", FLOATING_POINT_REGEX, "\\)", true, true)).matcher(affineMatrixString);
                                    if (affineMatrixMatcher.matches()) {
                                        String
                                            matrixString = affineMatrixMatcher.group();
                                        return new AffineMatrix(1d, 0d, 0d, Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip()), 0d, 0d);
                                    } else {
                                        affineMatrixMatcher = Pattern.compile(generateRegex("rotateZ?\\(", FLOATING_POINT_REGEX + "(deg)?", "\\)", true, true)).matcher(affineMatrixString);
                                        if (affineMatrixMatcher.matches()) {
                                            String
                                                matrixString = affineMatrixMatcher.group().replace("deg", "");
                                            double
                                                angle = Math.toRadians(Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip())),
                                                cos = Math.cos(angle),
                                                sin = Math.sin(angle);
                                            return new AffineMatrix(cos, sin, -sin, cos, 0d, 0d);
                                        } else {
                                            affineMatrixMatcher = Pattern.compile(generateRegex("skew\\(", FLOATING_POINT_REGEX + "(deg)?\\s+" + FLOATING_POINT_REGEX + "(deg)?", "\\)", true, true)).matcher(affineMatrixString);
                                            if (affineMatrixMatcher.matches()) {
                                                String
                                                    matrixString = affineMatrixMatcher.group().replace("deg", ""),
                                                    entries[] = matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).split("\\s+");
                                                double
                                                    tanAngleX = Math.tan(Math.toRadians(Double.parseDouble(entries[0].strip()))),
                                                    tanAngleY = Math.tan(Math.toRadians(Double.parseDouble(entries[1].strip())));
                                                return new AffineMatrix(1d, tanAngleY, tanAngleX, 1d, 0d, 0d);
                                            } else {
                                                affineMatrixMatcher = Pattern.compile(generateRegex("skewX\\(", FLOATING_POINT_REGEX + "(deg)?", "\\)", true, true)).matcher(affineMatrixString);
                                                if (affineMatrixMatcher.matches()) {
                                                    String
                                                        matrixString = affineMatrixMatcher.group().replace("deg", "");
                                                    double
                                                        tanAngleX = Math.tan(Math.toRadians(Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip())));
                                                    return new AffineMatrix(1d, 0d, tanAngleX, 1d, 0d, 0d);
                                                } else {
                                                    affineMatrixMatcher = Pattern.compile(generateRegex("skewX\\(", FLOATING_POINT_REGEX + "(deg)?", "\\)", true, true)).matcher(affineMatrixString);
                                                    if (affineMatrixMatcher.matches()) {
                                                        String
                                                            matrixString = affineMatrixMatcher.group().replace("deg", "");
                                                        double
                                                            tanAngleY = Math.tan(Math.toRadians(Double.parseDouble(matrixString.substring(matrixString.indexOf('(') + 1, matrixString.indexOf(')')).strip())));
                                                        return new AffineMatrix(1d, tanAngleY, 0d, 1d, 0d, 0d);
                                                    } else {
                                                        throw new RuntimeException("Unrecognized CSS transform: \"" + affineMatrixString + "\"");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Style parseStyle(String datum) {
        ArrayList<Style>
            styles = new ArrayList<>();
        for (String classLabel : datum.strip().split("\\s+")) {
            if (this.labeledStyles.containsKey(classLabel)) {
                styles.add(this.labeledStyles.get(classLabel));
            }
        }
        return Style.combine(styles);
    }

    private class Group {
        private AffineMatrix
            transform = AffineMatrix.IDENTITY;
        private ArrayList<XRNAData.Text>
            texts = new ArrayList<XRNAData.Text>();
        private ArrayList<Line>
            lines = new ArrayList<Line>();
        private String
            id;

        private Group() {
            // Do nothing.
        }

        private Group(AffineMatrix transform, String id) {
            this.transform = transform;
            this.id = id;
        }

        public void parseLines(String input) {
            Matcher
                lineMatcher = Pattern.compile("<line[^>]*/>").matcher(input);
            while (lineMatcher.find()) {
                Matcher
                    lineElementMatcher = Pattern.compile(HEADER_ELEMENT_REGEX).matcher(lineMatcher.group());
                Double
                    x1 = null,
                    y1 = null,
                    x2 = null,
                    y2 = null;
                AffineMatrix
                    lineTransform = AffineMatrix.IDENTITY;
                Style
                    style = new Style();
                String
                    strokeDatum = null,
                    strokeWidthDatum = null,
                    classLabel = "";
                while (lineElementMatcher.find()) {
                    String
                        lineHeader = lineElementMatcher.group();
                    int
                        equalsSignIndex = lineHeader.indexOf("=");
                    String
                        label = lineHeader.substring(0, equalsSignIndex).stripTrailing(),
                        datum = lineHeader.substring(equalsSignIndex + 1).stripLeading().replace("\"", "");
                    if (label.equalsIgnoreCase("x1")) {
                        x1 = Double.parseDouble(datum);
                    } else if (label.equalsIgnoreCase("y1")) {
                        y1 = Double.parseDouble(datum);
                    } else if (label.equalsIgnoreCase("x2")) {
                        x2 = Double.parseDouble(datum);
                    } else if (label.equalsIgnoreCase("y2")) {
                        y2 = Double.parseDouble(datum);
                    } else if (label.equalsIgnoreCase("transform")) {
                        lineTransform = parseAffineMatrix(datum);
                    } else if (label.equalsIgnoreCase("class")) {
                        style = parseStyle(datum);
                        classLabel = datum;
                    } else if (label.equalsIgnoreCase("stroke")) {
                        strokeDatum = datum;
                    } else if (label.equalsIgnoreCase("stroke-width")) {
                        strokeWidthDatum = datum;
                    }
                }
                if (style.stroke == null) {
                    if (strokeDatum != null) {
                        style.stroke = parseColor(strokeDatum);
                    } else {
                        style.stroke = DEFAULT_STROKE;
                    }
                }
                if (style.strokeWidth == null) {
                    if (strokeWidthDatum != null) {
                        style.strokeWidth = Double.parseDouble(strokeWidthDatum);
                    } else {
                        style.strokeWidth = DEFAULT_STROKE_WIDTH;
                    }
                }
                if (x1 == null || y1 == null || x2 == null || y2 == null) {
                    throw new RuntimeException("Incomplete line element: " + lineMatcher.group());
                } else {
                    lineTransform = AffineMatrix.multiply(lineTransform, this.transform);
                    Tuple2<Double, Double>
                        transformedX1Y1 = AffineMatrix.multiply(lineTransform, x1, y1),
                        transformedX2Y2 = AffineMatrix.multiply(lineTransform, x2, y2);
                    Line
                        line = new Line(transformedX1Y1.t0, transformedX1Y1.t1, transformedX2Y2.t0, transformedX2Y2.t1, style, classLabel);
                    this.lines.add(line);
                }
            }
        }

        public void parseTexts(String input) {
            Matcher
                textOpenMatcher = Pattern.compile("<text[^>]*>").matcher(input),
                textCloseMatcher = Pattern.compile("<\\/text\\s*>\\n?").matcher(input);
            while (textOpenMatcher.find()) {
                textCloseMatcher.find();
                String
                    textOpen = textOpenMatcher.group();
                Matcher
                    textHeadElementMatcher = Pattern.compile(HEADER_ELEMENT_REGEX).matcher(textOpen);
                AffineMatrix
                    textTransform = AffineMatrix.IDENTITY;
                Style
                    style = new Style();
                String
                    fillDatum = null,
                    fontFamilyDatum = null,
                    fontSizeDatum = null,
                    xString = null,
                    yString = null,
                    classLabel = "";
                while (textHeadElementMatcher.find()) {
                    String
                        textHeadElement = textHeadElementMatcher.group();
                    int
                        equalsSignIndex = textHeadElement.indexOf('=');
                    String
                        label = textHeadElement.substring(0, equalsSignIndex).stripTrailing(),
                        datum = textHeadElement.substring(equalsSignIndex + 1).stripLeading().replace("\"", "");
                    if (label.equalsIgnoreCase("transform")) {
                        textTransform = parseAffineMatrix(datum);
                    } else if (label.equalsIgnoreCase("class")) {
                        style = parseStyle(datum);
                        classLabel = datum;
                    } else if (label.equalsIgnoreCase("fill")) {
                        fillDatum = datum;
                    } else if (label.equalsIgnoreCase("font-family")) {
                        fontFamilyDatum = datum;
                    } else if (label.equalsIgnoreCase("font-size")) {
                        fontSizeDatum = datum;
                    } else if (label.equalsIgnoreCase("x")) {
                        xString = datum;
                    } else if (label.equalsIgnoreCase("y")) {
                        yString = datum;
                    }
                }
                boolean
                    xStringNullFlag = xString == null,
                    yStringNullFlag = yString == null;
                if (!xStringNullFlag || !yStringNullFlag) {
                    textTransform = AffineMatrix.multiply(textTransform, AffineMatrix.translate(
                        xStringNullFlag ? 0d : Double.parseDouble(xString.strip()),
                        yStringNullFlag ? 0d : Double.parseDouble(yString.strip())
                    ));
                }
                boolean
                    styleChanged = false;
                if (style.fill == null) {
                    if (fillDatum != null) {
                        style.fill = parseColor(fillDatum);
                        styleChanged = true;
                    } else {
                        style.fill = DEFAULT_FONT_FILL;
                    }
                }
                if (style.fontFamily == null) {
                    if (fontFamilyDatum != null) {
                        style.fontFamily = fontFamilyDatum.replace("'", "");
                        styleChanged = true;
                    } else {
                        style.fontFamily = DEFAULT_FONT_FAMILY;
                    }
                }
                if (style.fontSize == null) {
                    if (fontSizeDatum != null) {
                        style.fontSize = Double.parseDouble(fontSizeDatum.replace("px", ""));
                        styleChanged = true;
                    } else {
                        style.fontSize = (double)DEFAULT_FONT_SIZE;
                    }
                }
                if (styleChanged) {
                    style.label = "" + fontChangedSerializer;
                    fontChangedSerializer++;
                }
                textTransform = AffineMatrix.multiply(textTransform, this.transform);
                Tuple2<Double, Double>
                    xy = AffineMatrix.multiply(textTransform, 0d, 0d);
                Text
                    text = new Text(xy.t0, xy.t1, input.substring(textOpenMatcher.end(), textCloseMatcher.start()), style, classLabel);
                this.texts.add(text);
            }
        }
    }

    private static class AffineMatrix {
        private double
            element0,
            element1,
            element2,
            element3,
            element4,
            element5;

        private static final AffineMatrix
            IDENTITY = new AffineMatrix(1d, 0d, 0d, 1d, 0d, 0d),
            ZERO = new AffineMatrix(0d, 0d, 0d, 0d, 0d, 0d);

        private AffineMatrix(double element0, double element1, double element2, double element3, double element4, double element5) {
            this.element0 = element0;
            this.element1 = element1;
            this.element2 = element2;
            this.element3 = element3;
            this.element4 = element4;
            this.element5 = element5;
        }

        @Override
        public String toString() {
            return 
                "[" + this.element0 + ", " + this.element2 + ", " + this.element4 + "]\n" +
                "[" + this.element1 + ", " + this.element3 + ", " + this.element5 + "]\n" +
                "[0 0 1]\n";
        }

        public static AffineMatrix rotate(double theta) {
            double
                cos = Math.cos(theta),
                sin = Math.sin(theta);
            return new AffineMatrix(
                cos,
                sin,
                -sin,
                cos,
                0d,
                0d
            );
        }

        public static AffineMatrix scale(double sX, double sY) {
            return new AffineMatrix(
                sX,
                0d,
                0d,
                sY,
                0d,
                0d
            );
        }

        public static AffineMatrix skew(double sX, double sY) {
            return new AffineMatrix(
                1d,
                Math.tan(sY),
                Math.tan(sX),
                1d,
                0d,
                0d
            );
        }

        public static AffineMatrix translate(double dx, double dy) {
            return new AffineMatrix(1d, 0d, 0d, 1d, dx, dy);
        }

        public static AffineMatrix multiply(AffineMatrix a, AffineMatrix b) {
            // [a0 a2 a4]   [b0 b2 b4]   [a0 * b0 + a2 * b1 a0 * b2 + a2 * b3 a0 * b4 + a2 * b5 + a4]
            // [a1 a3 a5] * [b1 b3 b5] = [a1 * b0 + a3 * b1 a1 * b2 + a3 * b3 a1 * b4 + a3 * b5 + a5]
            // [0  0  1 ]   [0  0  1 ]   [0                 0                 1                     ]
            return new AffineMatrix(
                a.element0 * b.element0 + a.element2 * b.element1,
                a.element1 * b.element0 + a.element3 * b.element1,
                a.element0 * b.element2 + a.element2 * b.element3,
                a.element1 * b.element2 + a.element3 * b.element3,
                a.element0 * b.element4 + a.element2 * b.element5 + a.element4,
                a.element1 * b.element4 + a.element3 * b.element5 + a.element5
            );
        }

        public static Tuple2<Double, Double> multiply(AffineMatrix a, double x, double y) {
            // [a0 a2 a4]   [x]   [a0 * x + a2 * y + a4]
            // [a1 a3 a5] * [y] = [a1 * x + a3 * y + a5]
            // [0  0  1 ]   [1]   [1                   ]
            return new Tuple2<Double, Double> (
                a.element0 * x + a.element2 * y + a.element4,
                a.element1 * x + a.element3 * y + a.element5
            );
        }
    }
}