package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STRParser extends XRNAData {
    private ArrayList<Text>
        nucleotideTexts = new ArrayList<Text>();
    private ArrayList<Line>
        lines = new ArrayList<Line>();

    public static void main(String[] args) {
        String
            inputSTRFilePath = "C:\\Users\\caede\\OneDrive\\Desktop\\Input.str",
            outputXRNAFilePath = "C:\\Users\\caede\\OneDrive\\Desktop\\Output.xrna";
        STRParser
            strParser = new STRParser(inputSTRFilePath);
        // strParser.printToXRNAFile(outputXRNAFilePath);
        // strParser.display();
    }

    @Override
    public ArrayList<ArrayList<Text>> inputNucleotideTexts() {
        ArrayList<ArrayList<Text>>
            inputNucleotideTexts = new ArrayList<>();
        inputNucleotideTexts.add(this.nucleotideTexts);
        return inputNucleotideTexts;
    }

    @Override
    public ArrayList<Line> inputNucleotideLines() {
        return this.lines;
    }

    @Override
    public ArrayList<Text> inputLabelTexts() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Line> inputLabelLines() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Text> inputHelixTexts() {
        return new ArrayList<>();
    }

    @Override
    public Vector2 getLocusForNucleotideBonding() {
        
    }

    public STRParser(String inputSTRFilePath) {
        try {
            String
                fileContents = Files.readString(Paths.get(inputSTRFilePath));
            Matcher
                fileInfoMatcher = Pattern.compile("basefont[^\\}]*\\{[^\\}]+\\}[^\\}\\d]*(\\d+)[^\\}]*\\}").matcher(fileContents);
            if (fileInfoMatcher.find()) {
                // Default font size parsing.
                int
                    defaultFontSize = Integer.parseInt(fileInfoMatcher.group(1));
                System.out.println("Found default font size: " + defaultFontSize);
                XRNAData.DEFAULT_FONT_SIZE = defaultFontSize;
            }
            Matcher
                textOrLineMatcher = Pattern.compile("(text|line)\\s*\\{").matcher(fileContents);
            while (textOrLineMatcher.find()) {
                int
                    openCloseBracketBalanceIndex = 1,
                    textOrLineMatchEnd = textOrLineMatcher.end(),
                    previousIndex = textOrLineMatchEnd - 1;
                while (openCloseBracketBalanceIndex > 0) {
                    int
                        indexOfNextOpenBracket = fileContents.indexOf((int)'{', previousIndex + 1),
                        indexOfNextCloseBracket = fileContents.indexOf((int)'}', previousIndex + 1);
                    if (indexOfNextOpenBracket == -1) {
                        if (indexOfNextCloseBracket == -1) {
                            throw new RuntimeException("Improper STR file; unclosed bracket.");
                        } else {
                            previousIndex = indexOfNextCloseBracket;
                            openCloseBracketBalanceIndex--;
                        }
                    } else if (indexOfNextCloseBracket == -1) {
                        previousIndex = indexOfNextOpenBracket;
                        openCloseBracketBalanceIndex++;
                    } else if (indexOfNextCloseBracket < indexOfNextOpenBracket) {
                        previousIndex = indexOfNextCloseBracket;
                        openCloseBracketBalanceIndex--;
                    } else {
                        // Note: it is impossible for indexOfNextCloseBracket == indexOfNextOpenBracket.
                        // Therefore, indexOfNextCloseBracket > indexOfNextOpenBracket.
                        previousIndex = indexOfNextOpenBracket;
                        openCloseBracketBalanceIndex++;
                    }
                }
                String
                    textOrLineContents = fileContents.substring(textOrLineMatchEnd, previousIndex);
                // System.out.println("Contents: " + textOrLineContents);
                int
                    textOrLineMatchStart = textOrLineMatcher.start();
                String
                    label = fileContents.substring(textOrLineMatchStart, textOrLineMatchStart + 4);
                if (label.equalsIgnoreCase("text")) {
                    // String
                    //     pattern = "{\\s*(" + FLOATING_POINT_REGEX + ")\\s+(" + FLOATING_POINT_REGEX + ")\\s*}.*(A|C|G|U)\\s+0\\s*$";
                    String
                        pattern = "\\{\\s*(" + FLOATING_POINT_REGEX + ")\\s+(" + FLOATING_POINT_REGEX + ")\\s*\\}.*(A|C|G|U)\\s+0\\s*$";
                    // System.out.println("Pattern: " + pattern);
                    Matcher
                        textMatcher = Pattern.compile(pattern).matcher(textOrLineContents);
                    if (textMatcher.find()) {
                        Text
                            text = new Text(Double.parseDouble(textMatcher.group(1)), Double.parseDouble(textMatcher.group(2)), textMatcher.group(3));
                        text.style = new Style();
                        text.style.setToDefaults();
                        this.nucleotideTexts.add(text);
                        // System.out.println(this.nucleotideTexts.size());
                    } else {
                        // System.out.println("Pattern: " + pattern);
                        // System.out.println("Element: " + textOrLineContents);
                        // throw new RuntimeException("Broken STR file: broken text element.");
                    }
                } else {
                    String 
                        pattern = "\\{\\s*(" + FLOATING_POINT_REGEX + ")\\s+(" + FLOATING_POINT_REGEX + ")\\s+(" + FLOATING_POINT_REGEX + ")\\s+(" + FLOATING_POINT_REGEX + ")\\s*\\}";
                    Matcher
                        coordinatesMatcher = Pattern.compile(pattern).matcher(textOrLineContents);
                    if (coordinatesMatcher.find()) {
                        Line
                            line = new Line(Double.parseDouble(coordinatesMatcher.group(1)), Double.parseDouble(coordinatesMatcher.group(2)), Double.parseDouble(coordinatesMatcher.group(3)), Double.parseDouble(coordinatesMatcher.group(4)));
                        line.style = new Style();
                        line.style.setToDefaults();
                        this.lines.add(line);
                    } else {
                        System.out.println("Pattern: " + pattern + "\nElement: " + textOrLineContents);
                        throw new RuntimeException("Broken STR file: broken line element.");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
