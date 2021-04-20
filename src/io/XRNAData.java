package io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ssview.ComplexParentFrame;
import ssview.Vector2;
import util.Tuple2;
import util.Tuple3;
import util.Tuple4;

public abstract class XRNAData {
    public ArrayList<ArrayList<Text>>
        nucleotideTexts;
    public ArrayList<Text>
        labelTexts,
        helixTexts;
    public ArrayList<Line>
        nucleotideLines,
        labelLines;
    private ArrayList<Tuple2<Integer, Integer>>
        basePairs = new ArrayList<>();
    private ArrayList<Tuple3<Text, Line, Integer>>
        labels = new ArrayList<>();

    public abstract ArrayList<ArrayList<Text>> inputNucleotideTexts();

    public abstract ArrayList<Line> inputNucleotideLines();

    public abstract ArrayList<Text> inputLabelTexts();

    public abstract ArrayList<Line> inputLabelLines();

    public abstract ArrayList<Text> inputHelixTexts();

    public void invertYCoordinates() {
        double
            maximumYIndex = -Double.MAX_VALUE;
        ArrayList<ArrayList<Text>>
            textLists = new ArrayList<>(Arrays.asList(this.labelTexts, this.helixTexts));
        textLists.addAll(this.nucleotideTexts);
        for (ArrayList<Text> texts : textLists) {
            for (Text text : texts) {
                if (text.y > maximumYIndex) {
                    maximumYIndex = text.y;
                }
            }
        }
        List<ArrayList<Line>>
            lineLists = Arrays.asList(this.nucleotideLines, this.labelLines);
        for (ArrayList<Line> lines : lineLists) {
            for (Line line : lines) {
                if (line.y0 > maximumYIndex) {
                    maximumYIndex = line.y0;
                }
                if (line.y1 > maximumYIndex) {
                    maximumYIndex = line.y1;
                }
            }
        }
        for (ArrayList<Text> texts : textLists) {
            for (Text text : texts) {
                text.y = maximumYIndex - text.y;
            }
        }
        for (ArrayList<Line> lines : lineLists) {
            for (Line line : lines) {
                line.y0 = maximumYIndex - line.y0;
                line.y1 = maximumYIndex - line.y1;
            }
        }
    }

    private void processInputs() {
        this.nucleotideTexts = this.inputNucleotideTexts();
        this.labelTexts = this.inputLabelTexts();
        this.helixTexts = this.inputHelixTexts();
        this.nucleotideLines = this.inputNucleotideLines();
        this.labelLines = this.inputLabelLines();

        HashMap<Integer, Integer>
            listIndexToNucleotideBaseIndexMap = new HashMap<>();
        int
            listIndex = 0,
            baseIndex = 0;
        for (ArrayList<Text> nucleotideTextsSublist : this.nucleotideTexts) {
            listIndexToNucleotideBaseIndexMap.put(listIndex, baseIndex);
            listIndex++;
            baseIndex += nucleotideTextsSublist.size();
        }
        BiFunction<Vector2, Text, Double>
            distanceMetric = (Vector2 v, Text nucText) -> {
                Rectangle2D.Double
                    bounds = getBounds(nucText);
                return Vector2.distance(new Vector2(bounds.getCenterX(), bounds.getCenterY()), v);
            };
        Function<Tuple4<Text, Integer, Integer, Double>, Integer>
            totalIndexHelper = t3 -> listIndexToNucleotideBaseIndexMap.get(t3.t1) + t3.t2;
        // Function<Tuple4<Tuple3<Text, Integer, Integer>, Tuple3<Text, Integer, Integer>, Vector2, Vector2>, Boolean>
        //     indicesCheck = t4 -> Math.abs(totalIndexHelper.apply(t4.t0) - totalIndexHelper.apply(t4.t1)) > 1,
        //     nucPairValidator = this.lineSpecicifityFlag ? 
        //     indicesCheck :
        //     t4 -> {
        //         Tuple3<Text, Integer, Integer>
        //             nearestNucText0 = t4.t0,
        //             nearestNucText1 = t4.t1;
        //         if (!indicesCheck.apply(t4)) {
        //             // System.out.println();
        //             // System.out.println("Index0: " + t4.t0.t1 + " " + t4.t0.t2 + "\tIndex1: " + t4.t1.t1 + " " + t4.t1.t2 + "\tv0: " + t4.t2 + "\tv1: " + t4.t3 + "\tText: " + t4.t0.t0.content + " " + t4.t1.t0.content);
        //             return false;
        //         }
        //         double
        //             dist0 = Vector2.distance(t4.t2, new Vector2(nearestNucText0.t0.x, nearestNucText0.t0.y)),
        //             dist1 = Vector2.distance(t4.t2, new Vector2(nearestNucText1.t0.x, nearestNucText1.t0.y)),
        //             ratio = dist0 >= dist1 ? dist0 / dist1 : dist1 / dist0,
        //             threshold = 3d;
        //         return ratio <= threshold;
        //     };
        for (Line nucBondLine : this.nucleotideLines) {
            if (nucBondLine.magnitude() > EPSILON) {
                // (Effectively) zero-magnitude line
                for (double scalar : Arrays.asList(2d, 2.5d, 3d, 3.5d, 5d)) {
                    nucBondLine = nucBondLine.scale(scalar);
                    Vector2
                        v0 = new Vector2(nucBondLine.x0, nucBondLine.y0),
                        v1 = new Vector2(nucBondLine.x1, nucBondLine.y1);
                    Tuple4<Text, Integer, Integer, Double>
                        nearestNucText0 = XRNAData.closestElementWithIndicesAndDistance(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                        nearestNucText1 = XRNAData.closestElementWithIndicesAndDistance(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
                    // if (nucPairValidator.apply(new Tuple4<>(nearestNucText0, nearestNucText1, v0, v1))) {
                    // if (Math.abs(totalIndex0 - totalIndex1) > 1) {
                    //     this.basePairs.add(new Tuple2<>(
                    //         listIndexToNucleotideBaseIndexMap.get(nearestNucText0.t1) + nearestNucText0.t2,
                    //         listIndexToNucleotideBaseIndexMap.get(nearestNucText1.t1) + nearestNucText1.t2
                    //     ));
                    //     break;
                    // }
                    int
                        totalIndex0 = totalIndexHelper.apply(nearestNucText0),
                        totalIndex1 = totalIndexHelper.apply(nearestNucText1);
                    if (Math.abs(totalIndex0 - totalIndex1) > 1) {
                        this.basePairs.add(new Tuple2<>(totalIndex0, totalIndex1));
                        break;
                    }
                }
            } else {
                Vector2
                    nucBondLineEndpoint = new Vector2(nucBondLine.x0, nucBondLine.y0);
                ArrayList<Tuple4<Text, Integer, Integer, Double>>
                    twoClosestNucleotideTexts = XRNAData.closestElementsWithIndicesAndDistance(this.nucleotideTexts, (Text nucText) -> Vector2.distance(nucBondLineEndpoint, getLocusForNucleotideBonding(nucText)), 2);
                int
                    totalIndex0 = totalIndexHelper.apply(twoClosestNucleotideTexts.get(0)),
                    totalIndex1 = totalIndexHelper.apply(twoClosestNucleotideTexts.get(1));
                
                this.basePairs.add(new Tuple2<>(totalIndex0, totalIndex1));
            }
        }
        ArrayList<LabelDatum>
            labelData = new ArrayList<>();
        for (Line labelLine : this.labelLines) {
            Line
                nonscaledLabelLine = labelLine;
            labelLine = labelLine.scale(1.5d);
            Vector2
                v0 = new Vector2(labelLine.x0, labelLine.y0),
                v1 = new Vector2(labelLine.x1, labelLine.y1);
            Tuple3<Text, Integer, Double>
                nearestLabelText0 = XRNAData.closestElementWithIndexAndDistance(this.labelTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                nearestLabelText1 = XRNAData.closestElementWithIndexAndDistance(this.labelTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
            Vector2
                nucleotideEndpoint;
            Tuple3<Text, Integer, Double>
                nearestLabelTextWithDistance;
            if (nearestLabelText0.t2 < nearestLabelText1.t2) {
                nucleotideEndpoint = v1;
                nearestLabelTextWithDistance = nearestLabelText0;
            } else {
                nucleotideEndpoint = v0;
                nearestLabelTextWithDistance = nearestLabelText1;
            }
            Tuple3<Text, Integer, Integer>
                nearestNucleotideText = XRNAData.closestElementWithIndices(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(nucleotideEndpoint, nucText));
            labelData.add(new LabelDatum(
                nearestLabelTextWithDistance.t0,
                nearestNucleotideText.t0,
                nonscaledLabelLine,
                listIndexToNucleotideBaseIndexMap.get(nearestNucleotideText.t1),
                nearestNucleotideText.t2
            ));
        }
        HashMap<Integer, Integer>
            countIndexOffsetsMap = new HashMap<>();
        for (LabelDatum labelDatum : labelData) {
            int
                indexOffset = Integer.parseInt(labelDatum.label.content) - labelDatum.nucleotideIndex;
            labelDatum.indexOffset = indexOffset;
            if (countIndexOffsetsMap.containsKey(indexOffset)) {
                countIndexOffsetsMap.put(indexOffset, countIndexOffsetsMap.get(indexOffset) + 1);
            } else {
                countIndexOffsetsMap.put(indexOffset, 1);
            }
        }
        Integer
            maximumOffsetCount = 0,
            mostCommonIndexOffset = null;
        // System.out.println();
        for (HashMap.Entry<Integer, Integer> indexOffsetEntry : countIndexOffsetsMap.entrySet()) {
            int
                indexOffset = indexOffsetEntry.getKey(),
                indexOffsetCount = indexOffsetEntry.getValue();
            // System.out.println("indexOffset: " + indexOffset + " indexOffsetCount: " + indexOffsetCount);
            if (indexOffsetCount > maximumOffsetCount) {
                maximumOffsetCount = indexOffsetCount;
                mostCommonIndexOffset = indexOffset;
            }
        }
        for (LabelDatum labelDatum : labelData) {
            int
                correctIndex = labelDatum.nucleotideSublistBaseIndex + labelDatum.nucleotideIndex - (mostCommonIndexOffset - labelDatum.indexOffset);
            // System.out.println(correctIndex + " vs. " + (labelDatum.nucleotideSublistBaseIndex + labelDatum.nucleotideIndex) + " " + labelDatum.indexOffset + " " + mostCommonIndexOffset);
            Text
                labelText = labelDatum.label.copy(),
                nucleotideText = null;
            if (labelDatum.indexOffset != mostCommonIndexOffset) {
                nucleotideText = getNucleotideByIndex(correctIndex);
                // System.out.println("\t" + labelDatum.label.content + " " + labelDatum.indexOffset);
            } else {
                nucleotideText = labelDatum.nucleotide;
            }
            // System.out.println("Correct: " + correctIndex + "\tTotal Index: " + labelDatum.nucleotideSublistBaseIndex + " + " + labelDatum.nucleotideIndex + " = " + (labelDatum.nucleotideSublistBaseIndex + labelDatum.nucleotideIndex) + "\tIndex Offset: " + labelDatum.indexOffset + "\tLabel: " + labelDatum.label.content);
            nucleotideText = nucleotideText.copy();
            Line
                labelLine = labelDatum.line.copy();
            Rectangle2D.Double
                nucBounds = getBounds(nucleotideText),
                labelBounds = getBounds(labelText);
            double
                nucCenterX = nucBounds.getCenterX(),
                nucCenterY = nucBounds.getCenterY(),
                labelCenterX = labelBounds.getCenterX(),
                labelCenterY = labelBounds.getCenterY();
            Style
                style = labelDatum.label.style;
            Font
                font = new Font(style.fontFamily, Font.PLAIN, (int)Math.round(style.fontSize));
            FontMetrics
                fontMetrics = ComplexParentFrame.frame.getFontMetrics(font);
            int
                labelWidth = fontMetrics.stringWidth(labelDatum.label.content),
                nucWidth = fontMetrics.stringWidth(labelDatum.nucleotide.content);
            double
                nucHeight = labelDatum.nucleotide.style.fontSize;
            // labelCenterX -= labelWidth / 2d;

            // labelText.x = labelCenterX - nucCenterX;
            // labelText.y = -(labelCenterY - nucCenterY);
            // labelLine.x0 = labelLine.x0 - nucCenterX;
            // labelLine.y0 = -(labelLine.y0 - nucCenterY);
            // labelLine.x1 = labelLine.x1 - nucCenterX;
            // labelLine.y1 = -(labelLine.y1 - nucCenterY);
            labelText.x = labelCenterX - nucCenterX - labelWidth / 4d;
            labelText.y = -(labelCenterY - nucCenterY);
            labelLine.x0 = labelLine.x0 - nucCenterX + nucWidth / 2d;
            labelLine.y0 = -(labelLine.y0 - nucCenterY) + nucHeight / 4d;
            labelLine.x1 = labelLine.x1 - nucCenterX + nucWidth / 2d;
            labelLine.y1 = -(labelLine.y1 - nucCenterY) + nucHeight / 4d;
            
            this.labels.add(new Tuple3<>(labelText, labelLine, correctIndex));
        }
        // Nullify label-line offsets.
        // This solves a seemingly intractable issue stemming from different origins of SVG files causing offsets between label lines and nucleotides.
        Vector2
            averageOffset = new Vector2(0d, 0d);
        int
            usefulOffsetsCounts = 0;
        for (Tuple3<Text, Line, Integer> label : labels) {
            Line
                labelLine = label.t1;
            if (labelLine.magnitude() >= EPSILON) {
                Text
                    nucleotide = getNucleotideByIndex(label.t2);
                Rectangle2D.Double
                    nucleotideCenterBounds = getBounds(nucleotide);
                Vector2
                    v0 = new Vector2(labelLine.x0, labelLine.y0),
                    offset = Vector2.reject(
                        Vector2.subtract(
                            new Vector2(0d, 0d), 
                            v0
                        ), 
                        Vector2.subtract(
                            new Vector2(labelLine.x1, labelLine.y1),
                            v0
                        )
                    );
                averageOffset = Vector2.add(averageOffset, offset);
                usefulOffsetsCounts++;
            }
        }
        // Tuple3<Text, Line, Integer>
        //     label0 = this.labels.get(0);
        // Text
        //     text0 = label0.t0;
        // Line
        //     line0 = label0.t1;
        // Integer
        //     index0 = label0.t2;
        // Text
        //     nucleotide0 = getNucleotideByIndex(index0);
        // Rectangle2D.Double
        //     bounds0 = getBounds(nucleotide0);
        // Vector2
        //     v00 = new Vector2(line0.x0, line0.y0),
        //     v10 = new Vector2(line0.x1, line0.y1),
        //     center0 = new Vector2(bounds0.getCenterX(), bounds0.getCenterY());
        // System.out.println("v0: " + v00 + " v1: " + v10 + " c: " + center0 + " label: " + text0.content + " index: " + index0);
        if (usefulOffsetsCounts > 0) {
            averageOffset = Vector2.scaleDown(averageOffset, usefulOffsetsCounts);
            System.out.println("AverageOffset: " + averageOffset);
            System.out.println("UsefulOffsetsCounts: " + usefulOffsetsCounts);
            for (Tuple3<Text, Line, Integer> label : labels) {
                Line
                    line = label.t1;
                Vector2
                    displacedV0 = Vector2.add(new Vector2(line.x0, line.y0), averageOffset),
                    displacedV1 = Vector2.add(new Vector2(line.x1, line.y1), averageOffset);
                line.x0 = displacedV0.x;
                line.y0 = displacedV0.y;
                line.x1 = displacedV1.x;
                line.y1 = displacedV1.y;
            }
        }
        this.invertYCoordinates();
    }

    private Text getNucleotideByIndex(int index) {
        int
            nucleotideSublistBaseIndex = 0;
        for (ArrayList<Text> nucleotideTextSublist : this.nucleotideTexts) {
            int
                endIndex = nucleotideSublistBaseIndex + nucleotideTextSublist.size();
            if (endIndex >= index) {
                return nucleotideTextSublist.get(endIndex - index);
            }
            nucleotideSublistBaseIndex = endIndex;
        }
        throw new RuntimeException("The input index is out of bounds.");
    }

    private static <T> T closestElement(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        return closestElementWithIndex(iterable, distanceMetric).t0;
    }

    private static <T> Tuple2<T, Integer> closestElementWithIndex(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        Tuple3<T, Integer, Double>
            closestElementWithIndexAndDistance = closestElementWithIndexAndDistance(iterable, distanceMetric);
        return new Tuple2<>(closestElementWithIndexAndDistance.t0, closestElementWithIndexAndDistance.t1);
    }

    private static <T> Tuple3<T, Integer, Double> closestElementWithIndexAndDistance(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        Iterator<T>
            iterator = iterable.iterator();
        T
            closestElement = null;
        Double
            minimumDistance = Double.MAX_VALUE;
        Integer
            closestElementIndex = -1;
        int
            index = 0;
        while (iterator.hasNext()) {
            T
                next = iterator.next();
            Double
                distance = distanceMetric.apply(next);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                closestElement = next;
                closestElementIndex = index;
            }
            index++;
        }
        return new Tuple3<>(closestElement, closestElementIndex, minimumDistance);
    }

    private static <T> Tuple3<T, Integer, Integer> closestElementWithIndices(Iterable<? extends Iterable<T>> iterable, Function<T, Double> distanceMetric) {
        Tuple4<T, Integer, Integer, Double>
            closestElementWithIndicesAndDistance = closestElementWithIndicesAndDistance(iterable, distanceMetric);
        return new Tuple3<>(closestElementWithIndicesAndDistance.t0, closestElementWithIndicesAndDistance.t1, closestElementWithIndicesAndDistance.t2);
    }

    private static <T> Tuple4<T, Integer, Integer, Double> closestElementWithIndicesAndDistance(Iterable<? extends Iterable<T>> iterable, Function<T, Double> distanceMetric) {
        Iterator<? extends Iterable<T>>
            iteratorOfIterables = iterable.iterator();
        double
            minimumDistance = Double.MAX_VALUE;
        int
            minimumDistanceIterableIndex = -1,
            minimumDistanceIndex = -1,
            iterableIndex = 0;
        T
            closestElement = null;
        while (iteratorOfIterables.hasNext()) {
            Iterator<T>
                iterator = iteratorOfIterables.next().iterator();
            int
                index = 0;
            while (iterator.hasNext()) {
                T
                    next = iterator.next();
                double
                    distance = distanceMetric.apply(next);
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    minimumDistanceIndex = index;
                    minimumDistanceIterableIndex = iterableIndex;
                    closestElement = next;
                }
                index++;
            }
            iterableIndex++;
        }
        return new Tuple4<>(closestElement, minimumDistanceIterableIndex, minimumDistanceIndex, minimumDistance);
    }

    private static <T> ArrayList<Tuple4<T, Integer, Integer, Double>> closestElementsWithIndicesAndDistance(Iterable<? extends Iterable<T>> iterable, Function<T, Double> distanceMetric, int numberOfClosestElements) {
        LinkedList<Tuple4<T, Integer, Integer, Double>>
            elements = new LinkedList<>();
        int
            outerIndex = 0;
        for (Iterable<T> subIterable : iterable) {
            int
                innerIndex = 0;
            for (T element : subIterable) {
                elements.add(new Tuple4<>(element, outerIndex, innerIndex, distanceMetric.apply(element)));
                innerIndex++;
            }
            outerIndex++;
        }
        if (elements.size() < numberOfClosestElements) {
            throw new RuntimeException("The requested number of closest elements exceeds the number of elements yielded by the Iterable object.");
        }
        elements.sort((Tuple4<T, Integer, Integer, Double> t0, Tuple4<T, Integer, Integer, Double> t1) -> Double.compare(t0.t3, t1.t3));
        return new ArrayList<>(elements.subList(0, numberOfClosestElements));
    }

    public void printToXRNAFile(String path) {
        this.printToXRNAFile(new File(path));
    }

    public void printToXRNAFile(File file) {
        this.processInputs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            double
                meanX = 0d,
                meanY = 0d;
            int
                numNucleotides = 0;
            for (ArrayList<Text> nucleotideTextsSublist : this.nucleotideTexts) {
                numNucleotides += nucleotideTextsSublist.size();
                for (Text nucleotide : nucleotideTextsSublist) {
                    meanX += nucleotide.x;
                    meanY += nucleotide.y;
                }
            }
            meanX /= numNucleotides;
            meanY /= numNucleotides;
            String
                complexName = "Unknown",
                fileString = "<ComplexDocument Name='" + complexName + "'>\n<SceneNodeGeom CenterX='" + String.format(DOUBLE_FORMAT_STRING, -meanX) + "' CenterY='" + String.format(DOUBLE_FORMAT_STRING, -meanY) + "' Scale='1.0' />\n<Complex Name='" + complexName + "'>\n<RNAMolecule Name='" + complexName + "'>\n<NucListData StartNucID='1' DataType='NucChar.XPos.YPos'>\n";
            for (ArrayList<Text> nucleotideTextsSublist : this.nucleotideTexts) {
                for (Text nucleotide : nucleotideTextsSublist) {
                    fileString += nucleotide.content + " " + String.format(DOUBLE_FORMAT_STRING, nucleotide.x) + " " + String.format(DOUBLE_FORMAT_STRING, nucleotide.y) + "\n";
                }
            }
            fileString += "</NucListData>\n";
            fileString += "<Nuc RefIDs='1-" + numNucleotides + "' Color='0' FontID='0' FontSize='" + DEFAULT_FONT_SIZE + "' />\n";
            HashMap<Style, ArrayList<Integer>>
                map = new HashMap<>();
            int
                nucleotideIndex = 1;
            for (ArrayList<Text> nucleotideTextsSublist : this.nucleotideTexts) {
                for (Text nucleotideText : nucleotideTextsSublist) {
                    Style
                        style = nucleotideText.style;
                    ArrayList<Integer>
                        indices;
                    if (map.containsKey(style)) {
                        indices = map.get(style);
                    } else {
                        indices = new ArrayList<Integer>();
                        map.put(style, indices);
                    }
                    indices.add(nucleotideIndex++);
                }
            }
            ArrayList<String>
                toBeAppended = new ArrayList<>();
            map.forEach((Style style, ArrayList<Integer> indices) -> {
                int
                    anchorIndex = indices.get(0),
                    previousIndex = anchorIndex;
                String
                    indicesString = "" + anchorIndex;
                boolean
                    streakFlag = false;
                for (int i = 1; i < indices.size(); i++) {
                    int
                        currentIndex = indices.get(i);
                    if (currentIndex == previousIndex + 1) {
                        streakFlag = true;
                    } else {
                        if (streakFlag) {
                            indicesString += "-" + previousIndex;
                        }
                        indicesString += "," + currentIndex;
                        streakFlag = false;
                    }
                    previousIndex = currentIndex;
                }
                if (streakFlag) {
                    indicesString += "-" + previousIndex;
                }
                toBeAppended.add("<Nuc RefIDs='" + indicesString + "' Color='" + Integer.toHexString(style.fill.getRGB() & 0x00FFFFFF) + "' FontID='0' FontSize='" + ((int)Math.round(style.fontSize)) + "' />\n");
            });
            fileString += String.join("", toBeAppended);
            fileString += "<Nuc RefIDs='1-" + numNucleotides + "' IsSchematic='false' SchematicColor='0' SchematicLineWidth='1.5' SchematicBPLineWidth='1.0' />\n";
            fileString += "<Nuc RefIDs='1-" + numNucleotides + "' SchematicBPGap='2.0' SchematicFPGap='2.0' SchematicTPGap='2.0' />\n";
            fileString += "<Nuc RefIDs='1-" + numNucleotides + "' IsNucPath='false' NucPathColor='ff0000' NucPathLineWidth='0.0' />\n";
            for (Tuple3<Text, Line, Integer> labelDatum : this.labels) {
                fileString += "<Nuc RefID='" + (labelDatum.t2 + 1) + "'>\n";
                fileString += "<LabelList>\n";
                Line
                    line = labelDatum.t1;
                // line = new Line(0d, 0d, labelDatum.t0.x, labelDatum.t0.y);
                // line.style = labelDatum.t1.style;
                fileString += "l " + String.format(DOUBLE_FORMAT_STRING, line.x0) + " " + String.format(DOUBLE_FORMAT_STRING, line.y0) + " " + String.format(DOUBLE_FORMAT_STRING, line.x1) + " " + String.format(DOUBLE_FORMAT_STRING, line.y1) + " " + String.format(DOUBLE_FORMAT_STRING, line.style.strokeWidth) + " " + 255 + " 0.0 0 0 0 0\n";
                Text
                    text = labelDatum.t0;
                fileString += "s " + String.format(DOUBLE_FORMAT_STRING, text.x) + " " + String.format(DOUBLE_FORMAT_STRING, text.y) + " 0.0 " + (int)Math.round(text.style.fontSize) + " 0 " + Integer.toHexString(text.style.fill.getRGB() & 0x00FFFFFF) + " \"" + text.content + "\"\n";
                fileString += "</LabelList>\n";
                fileString += "</Nuc>\n";
            }
            for (Tuple2<Integer, Integer> basePairsDatum : this.basePairs) {
                // TODO: Collapse adjacent base-pair indices (length > 1).
                fileString += "<BasePairs nucID='" + (basePairsDatum.t0 + 1) + "' length='1' bpNucID='" + (basePairsDatum.t1 + 1) + "' />\n";
            }
            fileString += "</RNAMolecule>\n</Complex>\n</ComplexDocument>\n";
            writer.write(fileString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void display() {
        JFrame
            frame = new JFrame();
        JPanel
            panel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D
                        g2D = (Graphics2D)g;
                    g.setColor(Color.WHITE);
                    g2D.scale(2d, 2d);
                    ArrayList<ArrayList<Text>>
                        nucTexts = XRNAData.this.inputNucleotideTexts();
                    ArrayList<ArrayList<Text>>
                        textsListOfLists = new ArrayList<>();//Arrays.asList(XRNAData.this.inputLabelTexts(), XRNAData.this.inputHelixTexts());
                    textsListOfLists.add(XRNAData.this.inputLabelTexts());
                    textsListOfLists.add(XRNAData.this.inputHelixTexts());
                    textsListOfLists.addAll(nucTexts);
                    ArrayList<Text>
                        subList0 = nucTexts.get(0);
                    for (int i = 0; i < subList0.size(); i += 2) {
                        subList0.get(i).style.fontSize = 5d;
                    }
                    for (ArrayList<Text> texts : textsListOfLists) {
                        for (Text text : texts) {
                            Style
                                style = text.style;
                            Tuple2<Rectangle2D.Double, Font>
                                boundsAndFont = getBoundsAndFont(text);
                            Rectangle2D.Double
                                bounds = boundsAndFont.t0;
                            g2D.setColor(new Color(255, 0, 0, 255));
                            g2D.setStroke(new BasicStroke(0.1f));
                            g2D.draw(boundsAndFont.t0);
                            g2D.setColor(Color.RED);
                            
                            double
                                radius = 1d,
                                diameter = 2d * radius,
                                centerX = text.x,//bounds.getCenterX() - radius,
                                centerY = text.y;//bounds.getCenterY() - radius;
                            g2D.fill(new Ellipse2D.Double(centerX, centerY, diameter, diameter));
                            g2D.setColor(new Color(255 - style.fill.getRed(), 255 - style.fill.getGreen(), 255 - style.fill.getBlue()));
                            g2D.setFont(boundsAndFont.t1);
                            g2D.drawString(text.content, (float)text.x, (float)text.y);
                        }
                    }
                    ArrayList<Line>
                        nucBondLines = XRNAData.this.inputNucleotideLines();
                    for (ArrayList<Line> lines : Arrays.asList(nucBondLines, XRNAData.this.inputLabelLines())) {
                        for (Line line : lines) {
                            line = line.scale(1.5d);
                            Style
                                style = line.style;
                            g2D.setColor(new Color(255 - style.stroke.getRed(), 255 - style.stroke.getGreen(), 255 - style.stroke.getBlue()));
                            g2D.setStroke(new BasicStroke((float)style.strokeWidth.doubleValue()));
                            g2D.draw(new Line2D.Double(line.x0, line.y0, line.x1, line.y1));
                        }
                    }

                    // for (Line nucBondLine : nucBondLines) {
                    //     nucBondLine = nucBondLine.scale(2d);
                    //     g2D.setColor(Color.ORANGE);
                    //     g2D.draw(new Line2D.Double(nucBondLine.x0, nucBondLine.y0, nucBondLine.x1, nucBondLine.y1));
                    //     Vector2
                    //         v0 = new Vector2(nucBondLine.x0, nucBondLine.y0),
                    //         v1 = new Vector2(nucBondLine.x1, nucBondLine.y1);
                    //     BiFunction<Vector2, Text, Double>
                    //         distanceMetric = (Vector2 v, Text nucText) -> {
                    //             return Vector2.distance(new Vector2(nucText.x, nucText.y), v);
                    //         };
                    //     Tuple2<Text, Integer>
                    //         nearestNucText0 = XRNAData.this.closestElementWithIndex(nucTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                    //         nearestNucText1 = XRNAData.this.closestElementWithIndex(nucTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
                    //     g2D.setColor(Color.RED);
                    //     Rectangle2D.Double
                    //         bounds0 = XRNAData.getBounds(nearestNucText0.t0),
                    //         bounds1 = XRNAData.getBounds(nearestNucText1.t0);
                    //     g2D.draw(new Line2D.Double(bounds0.getCenterX(), bounds0.getCenterY(), v0.x, v0.y));
                    //     g2D.setColor(Color.GREEN);
                    //     g2D.draw(new Line2D.Double(bounds1.getCenterX(), bounds1.getCenterY(), v1.x, v1.y));
                    // }
                }
            };
        panel.setBackground(Color.BLACK);
        Dimension
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize = new Dimension(screenSize.width * 105 / 144, screenSize.height);
        frame.add(panel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });
        frame.setSize(screenSize);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static Rectangle2D.Double getBounds(Text text) {
        return getBoundsAndFont(text).t0;
    }

    public static Tuple2<Rectangle2D.Double, Font> getBoundsAndFont(Text text) {
        Style
            style = text.style;
        Font
            font = new Font(style.fontFamily, Font.PLAIN, (int)Math.round(style.fontSize));
        FontMetrics
            fontMetrics = ComplexParentFrame.frame.getFontMetrics(font);
        int
            height = fontMetrics.getHeight();
        Rectangle2D.Double
            bounds = new Rectangle2D.Double(text.x, text.y - height * 0.75d, fontMetrics.stringWidth(text.content), height);
        return new Tuple2<>(bounds, font);
    }

    public abstract Vector2 getLocusForNucleotideBonding(Text text);

    public static final double
        EPSILON = 1E-4d,
        DEFAULT_STROKE_WIDTH = 0.2d;

    public static final String
        FLOATING_POINT_REGEX = "-?(?:(?:\\d+)|(?:(?:\\d+)?\\.\\d+))(?:e-?\\d+)?",
        HEADER_ELEMENT_REGEX = "\\w[\\w\\d-]*\\s*=\\s*\"[^\"]*\"",
        DOUBLE_FORMAT_STRING = "%.3f",
        DEFAULT_FONT_FAMILY = Font.DIALOG,
        DEFAULT_LABEL = "";

    public static int
        DEFAULT_FONT_SIZE = 4;

    public static final Color
        DEFAULT_FONT_FILL = Color.BLACK,
        DEFAULT_STROKE = Color.BLACK;

    private static class LabelDatum {
        private Text
            label,
            nucleotide;
        private Line
            line;
        private int
            nucleotideSublistBaseIndex,
            nucleotideIndex,
            indexOffset;

        private LabelDatum(Text label, Text nucleotide, Line line, int nucleotideSublistBaseIndex, int nucleotideIndex) {
            this.label = label;
            this.nucleotide = nucleotide;
            this.line = line;
            this.nucleotideSublistBaseIndex = nucleotideSublistBaseIndex;
            this.nucleotideIndex = nucleotideIndex;
        }
    }

    public static class Style {
        public Double
            fontSize,
            strokeWidth;
        public Color
            fill,
            stroke;
        public String
            fontFamily,
            label;
        public int
            priority;

        public Style() {
            // Do nothing.
        }

        public void setToDefaults() {
            this.fontSize = (double)DEFAULT_FONT_SIZE;
            this.strokeWidth = DEFAULT_STROKE_WIDTH;
            this.fill = DEFAULT_FONT_FILL;
            this.stroke = DEFAULT_STROKE;
            this.fontFamily = DEFAULT_FONT_FAMILY;
            this.label = DEFAULT_LABEL;
        }

        @Override
        public String toString() {
            return "(fontSize: " + this.fontSize + ", strokeWidth: " + this.strokeWidth + ", fill: " + this.fill + ", stroke: " + this.stroke + ", fontFamily: " + this.fontFamily + ")";
        }

        @Override
        public int hashCode() {
            return this.label.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            Style
                other = (Style)object;
            return this.label.equals(other.label);
        }

        public static Style combine(ArrayList<Style> styles) {
            styles.sort((Style s0, Style s1) -> -Integer.compare(s0.priority, s1.priority));
            Style
                combined = new Style();
            for (Style style : styles) {
                if (style.fontSize != null) {
                    combined.fontSize = style.fontSize;
                }
                if (style.strokeWidth != null) {
                    combined.strokeWidth = style.strokeWidth;
                }
                if (style.fill != null) {
                    combined.fill = style.fill;
                }
                if (style.stroke != null) {
                    combined.stroke = style.stroke;
                }
                if (style.fontFamily != null) {
                    combined.fontFamily = style.fontFamily;
                }
            }

            if (combined.fill == null) {
                combined.fill = Color.BLACK;
            }
            if (combined.stroke == null) {
                combined.stroke = Color.BLACK;
            }
            if (combined.fontSize == null) {
                combined.fontSize = (double)DEFAULT_FONT_SIZE;
            }
            styles.sort((Style s0, Style s1) -> s0.label.compareTo(s1.label));
            String
                label = "";
            for (Style style : styles) {
                label += style.label + " ";
            }
            combined.label = label;
            return combined;
        }
    }

    public static class Text {
        public double
            x,
            y;
        public String
            content,
            classLabel = "";
        public Style
            style;

        public Text(double x, double y, String content) {
            this.x = x;
            this.y = y;
            this.content = content;
        }

        public Text(double x, double y, String content, Style style) {
            this(x, y, content);
            this.style = style;
        }

        public Text(double x, double y, String content, Style style, String classLabel) {
            this(x, y, content);
            this.style = style;
            this.classLabel = classLabel;
        }
        
        public Text copy() {
            return new Text(this.x, this.y, this.content, this.style);
        }
    }

    public static class Line {
        public double
            x0,
            y0,
            x1,
            y1;
        public Style
            style;
        public String
            classLabel = "";

        public Line(double x0, double y0, double x1, double y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }

        public Line(double x0, double y0, double x1, double y1, Style style) {
            this(x0, y0, x1, y1);
            this.style = style;
        }

        public Line(double x0, double y0, double x1, double y1, Style style, String classLabel) {
            this(x0, y0, x1, y1, style);
            this.classLabel = classLabel;
        }

        public Vector2 center() {
            return new Vector2((this.x0 + this.x1) * 0.5d, (this.y0 + this.y1) * 0.5d);
        }

        public Line copy() {
            return new Line(this.x0, this.y0, this.x1, this.y1, this.style);
        }

        public double magnitudeSquared() {
            return magnitudeSquared(this.x0, this.y0, this.x1, this.y1);
        }

        public double magnitude() {
            return magnitude(this.x0, this.y0, this.x1, this.y1);
        }

        public Line scale(double scalar) {
            Vector2
                center = this.center(),
                dv = Vector2.scale(Vector2.subtract(new Vector2(this.x0, this.y0), center), scalar),
                scaledV0 = Vector2.add(center, dv),
                scaledV1 = Vector2.subtract(center, dv);
            return new Line(scaledV0.x, scaledV0.y, scaledV1.x, scaledV1.y, this.style);
        }

        public static double magnitudeSquared(double x0, double y0, double x1, double y1) {
            double
                dx = x1 - x0,
                dy = y1 - y0;
            return dx * dx + dy * dy;
        }

        public static double magnitude(double x0, double y0, double x1, double y1) {
            return Math.sqrt(magnitudeSquared(x0, y0, x1, y1));
        }
    }

    public static class Circle {
        public double
            centerX,
            centerY,
            radius;

        public Circle(double centerX, double centerY, double radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
    }
}
