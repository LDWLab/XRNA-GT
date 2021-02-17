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
        strParser.display();
    }

    @Override
    public ArrayList<Text> inputNucleotideTexts() {
        return this.nucleotideTexts;
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

    public STRParser(String inputSTRFilePath) {
        try {
            String
                fileContents = Files.readString(Paths.get(inputSTRFilePath));
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
                int
                    textOrLineMatchStart = textOrLineMatcher.start();
                String
                    label = fileContents.substring(textOrLineMatchStart, textOrLineMatchStart + 4);
                if (label.equalsIgnoreCase("text")) {
                    Matcher
                        xyCoordinatesMatcher = Pattern.compile("\\{\\s*" + FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX + "\\s*\\}").matcher(textOrLineContents),
                        nucleotideLetterMatcher = Pattern.compile("\\s*(A|C|G|U)\\s+\\d+\\s*$").matcher(textOrLineContents);
                    if (xyCoordinatesMatcher.find() && nucleotideLetterMatcher.find()) {
                        // Remove the brackets and whitespace.
                        // Split on whitespace to separate coordinate strings.
                        String[]
                            xyCoordinateStrings = textOrLineContents.substring(xyCoordinatesMatcher.start() + 1, xyCoordinatesMatcher.end() - 1).strip().split("\\s+");
                        double
                            x = Double.parseDouble(xyCoordinateStrings[0]),
                            y = Double.parseDouble(xyCoordinateStrings[1]);
                        String
                            nucleotideLetter = nucleotideLetterMatcher.group().strip().split("\\s+")[0];
                        Text
                            text = new Text(x, y, nucleotideLetter);
                        text.style = new Style();
                        text.style.setToDefaults();
                        this.nucleotideTexts.add(text);
                    }
                } else {
                    Matcher
                        coordinatesMatcher = Pattern.compile("\\{\\s*" + FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX + "\\s+" + FLOATING_POINT_REGEX + "\\s*\\}").matcher(textOrLineContents);
                    if (coordinatesMatcher.find()) {
                        String[]
                            coordinateStrings = textOrLineContents.substring(coordinatesMatcher.start() + 1, coordinatesMatcher.end() - 1).strip().split("\\s+");
                        double
                            x0 = Double.parseDouble(coordinateStrings[0]),
                            y0 = Double.parseDouble(coordinateStrings[1]),
                            x1 = Double.parseDouble(coordinateStrings[2]),
                            y1 = Double.parseDouble(coordinateStrings[3]);
                        Line
                            line = new Line(x0, y0, x1, y1);
                        line.style = new Style();
                        line.style.setToDefaults();
                        this.lines.add(line);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
