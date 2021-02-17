package io;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.FontMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import util.Tuple2;
import util.Tuple3;
import util.Tuple4;

import ssview.ComplexParentFrame;
import ssview.Vector2;

public abstract class XRNAData {
    public ArrayList<Text>
        nucleotideTexts,
        labelTexts,
        helixTexts;
    public ArrayList<Line>
        nucleotideLines,
        labelLines;
    private ArrayList<Tuple2<Integer, Integer>>
        basePairs = new ArrayList<>();
    private ArrayList<Tuple3<Text, Line, Integer>>
        labels = new ArrayList<>();

    public abstract ArrayList<Text> inputNucleotideTexts();

    public abstract ArrayList<Line> inputNucleotideLines();

    public abstract ArrayList<Text> inputLabelTexts();

    public abstract ArrayList<Line> inputLabelLines();

    public abstract ArrayList<Text> inputHelixTexts();

    public void invertYCoordinates() {
        double
            maximumYIndex = -Double.MAX_VALUE;
        List<ArrayList<Text>>
            textLists = Arrays.asList(this.nucleotideTexts, this.labelTexts, this.helixTexts);
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

        BiFunction<Vector2, Text, Double>
            distanceMetric =  (Vector2 v, Text nucText) -> {
                return Vector2.distance(new Vector2(nucText.x, nucText.y), v);
            };
        for (Line nucBondLine : this.nucleotideLines) {
            nucBondLine = nucBondLine.scale(2d);
            Vector2
                v0 = new Vector2(nucBondLine.x0, nucBondLine.y0),
                v1 = new Vector2(nucBondLine.x1, nucBondLine.y1);
            Tuple2<Text, Integer>
                nearestNucText0 = XRNAData.this.closestElementWithIndex(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                nearestNucText1 = XRNAData.this.closestElementWithIndex(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
            if (Math.abs(nearestNucText0.t1 - nearestNucText1.t1) > 1) {
                this.basePairs.add(new Tuple2<>(nearestNucText0.t1, nearestNucText1.t1));
            }
        }
        ArrayList<Tuple4<Text, Line, Text, Integer>>
            labelData = new ArrayList<>();
        for (Line labelLine : this.labelLines) {
            Line
                nonscaledLabelLine = labelLine;
            labelLine = labelLine.scale(1.5d);
            Vector2
                v0 = new Vector2(labelLine.x0, labelLine.y0),
                v1 = new Vector2(labelLine.x1, labelLine.y1);
            Tuple3<Text, Integer, Double>
                nearestLabelText0 = XRNAData.this.closestElementWithIndexAndDistance(this.labelTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                nearestLabelText1 = XRNAData.this.closestElementWithIndexAndDistance(this.labelTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
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
            Tuple2<Text, Integer>
                nearestNucleotideText = XRNAData.this.closestElementWithIndex(this.nucleotideTexts, (Text nucText) -> distanceMetric.apply(nucleotideEndpoint, nucText));
            // Rectangle2D.Double
            //     nucBounds = XRNAData.getBounds(nearestNucleotideText.t0);
            if (Math.abs(Integer.parseInt(nearestLabelTextWithDistance.t0.content) - nearestNucleotideText.t1) < 10) {
            // if ((nearestLabelTextWithDistance.t2 + 2d) <= 5d * Vector2.distance(new Vector2(nucBounds.getCenterX(), nucBounds.getCenterY()), nucleotideEndpoint)) {
                labelData.add(new Tuple4<>(nearestLabelTextWithDistance.t0, nonscaledLabelLine, nearestNucleotideText.t0, nearestNucleotideText.t1));
            } else {
                System.out.println("Error line: (" + nonscaledLabelLine.x0 + ", " + nonscaledLabelLine.y0 + ", " + nonscaledLabelLine.x1 + ", " + nonscaledLabelLine.y1 + ")");
            }
        }
        HashMap<Integer, Integer>
            countIndexOffsetsMap = new HashMap<>();
        for (Tuple4<Text, Line, Text, Integer> labelDatum : labelData) {
            int
                indexOffset = Integer.parseInt(labelDatum.t0.content) - labelDatum.t3;
            if (countIndexOffsetsMap.containsKey(indexOffset)) {
                countIndexOffsetsMap.put(indexOffset, countIndexOffsetsMap.get(indexOffset) + 1);
            } else {
                countIndexOffsetsMap.put(indexOffset, 1);
            }
        }
        Integer
            maximumOffsetCount = 0,
            mostCommonIndexOffset = null;
        for (HashMap.Entry<Integer, Integer> indexOffsetEntry : countIndexOffsetsMap.entrySet()) {
            int
                indexOffset = indexOffsetEntry.getKey(),
                indexOffsetCount = indexOffsetEntry.getValue();
            if (indexOffsetCount > maximumOffsetCount) {
                maximumOffsetCount = indexOffsetCount;
                mostCommonIndexOffset = indexOffset;
            }
        }
        for (Tuple4<Text, Line, Text, Integer> labelDatum : labelData) {
            int
                indexOffset = Integer.parseInt(labelDatum.t0.content) - labelDatum.t3;
            labelDatum.t3 -= mostCommonIndexOffset - indexOffset;
        }
        for (Tuple4<Text, Line, Text, Integer> labelDatum : labelData) {
            Text
                labelText = labelDatum.t0.copy(),
                nucleotideText = labelDatum.t2.copy();
            Rectangle2D.Double
                nucBounds = getBounds(nucleotideText),
                labelBounds = getBounds(labelText);
            double
                nucCenterX = nucBounds.getCenterX(),
                nucCenterY = nucBounds.getCenterY(),
                labelCenterX = labelBounds.getCenterX(),
                labelCenterY = labelBounds.getCenterY();
            Line
                labelLine = labelDatum.t1.copy();
            labelText.x = labelCenterX - nucCenterX;
            labelText.y = -(labelCenterY - nucCenterY);
            labelLine.x0 = labelLine.x0 - nucCenterX;
            labelLine.y0 = -(labelLine.y0 - nucCenterY);
            labelLine.x1 = labelLine.x1 - nucCenterX;
            labelLine.y1 = -(labelLine.y1 - nucCenterY);
            
            this.labels.add(new Tuple3<>(labelText, labelLine, labelDatum.t3));
        }
        this.invertYCoordinates();

        // for (Line line : this.labelLines) {
        //     double
        //         magnitude = line.magnitude();
        //     if (magnitude > EPSILON) {
        //         Vector2
        //             center = line.center(),
        //             direction = Vector2.normalize(Vector2.subtract(new Vector2(line.x0, line.y0), new Vector2(line.x1, line.y1)));
        //         double
        //             maximumTextMagnitude = magnitude * 2d;
        //         ArrayList<Tuple2<Text, Integer>>
        //             nearbyNucleotideTextsWithIndices = new ArrayList<>();
        //         ArrayList<Text>
        //             nearbyLabelTexts = new ArrayList<>();
        //         for (int i = 0; i < this.nucleotideTexts.size(); i++) {
        //             Text
        //                 nucleotideText = this.nucleotideTexts.get(i);
        //             double
        //                 textMagnitude = Line.magnitude(center.x, center.y, nucleotideText.x, nucleotideText.y);
        //             if (textMagnitude <= maximumTextMagnitude) {
        //                 nearbyNucleotideTextsWithIndices.add(new Tuple2<>(nucleotideText, i + 1));
        //             }
        //         }
        //         for (Text labelText : this.labelTexts) {
        //             double
        //                 textMagnitude = Line.magnitude(center.x, center.y, labelText.x, labelText.y);
        //             if (textMagnitude <= maximumTextMagnitude) {
        //                 nearbyLabelTexts.add(labelText);
        //             }
        //         }
        //         if (nearbyNucleotideTextsWithIndices.size() > 0 && nearbyLabelTexts.size() > 0) {
        //             Tuple2<Text, Integer>
        //                 nucleotideTextWithIndex = null;
        //             Text
        //                 labelText = null;
        //             if (nearbyNucleotideTextsWithIndices.size() == 1) {
        //                 nucleotideTextWithIndex = nearbyNucleotideTextsWithIndices.get(0);
        //                 labelText = findMostDirectLabelText(center, direction, nearbyLabelTexts).t0;
        //             } else if (nearbyLabelTexts.size() == 1) {
        //                 labelText = nearbyLabelTexts.get(0);
        //                 boolean
        //                     dotProductIsNegative = Vector2.dot(direction, Vector2.normalize(Vector2.subtract(new Vector2(labelText.x, labelText.y), center))) < 0;
        //                 nucleotideTextWithIndex = findMostDirectNucleotideTextWithIndex();
        //             } else {
        //                 Tuple2<Text, Boolean>
        //                     nearbyLabelTextData = findMostDirectLabelText(center, direction, nearbyLabelTexts);
        //                 if (nearbyLabelTextData.t1) {
        //                     double
        //                         maximumDotProduct = -1d;
        //                     for (Tuple2<Text, Integer> nearbyNucleotideTextWithIndex : nearbyNucleotideTextsWithIndices) {
        //                         Text
        //                             nearbyNucleotideText = nearbyNucleotideTextWithIndex.t0;
        //                         double
        //                             dotProduct = Vector2.dot(direction, Vector2.normalize(Vector2.subtract(new Vector2(nearbyNucleotideText.x, nearbyNucleotideText.y), center)));
        //                         if (dotProduct > maximumDotProduct) {
        //                             maximumDotProduct = dotProduct;
        //                             nucleotideTextWithIndex = nearbyNucleotideTextWithIndex;
        //                         }
        //                     }
        //                 } else {
        //                     double
        //                         minimumDotProduct = -1d;
        //                     for (Tuple2<Text, Integer> nearbyNucleotideTextWithIndex : nearbyNucleotideTextsWithIndices) {
        //                         Text
        //                             nearbyNucleotideText = nearbyNucleotideTextWithIndex.t0;
        //                         double
        //                             dotProduct = Vector2.dot(direction, Vector2.normalize(Vector2.subtract(new Vector2(nearbyNucleotideText.x, nearbyNucleotideText.y), center)));
        //                         if (dotProduct < minimumDotProduct) {
        //                             minimumDotProduct = dotProduct;
        //                             nucleotideTextWithIndex = nearbyNucleotideTextWithIndex;
        //                         }
        //                     }
        //                 }
        //             }
        //             labelData.add(new Tuple4<>(labelText, line, nucleotideTextWithIndex.t0, nucleotideTextWithIndex.t1));
        //         }
        //     } else {
        //         throw new UnsupportedOperationException("Label lines of zero length are currently unsupported.");
        //     }
        // }
    }

    private <T> T closestElement(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        return closestElementWithIndex(iterable, distanceMetric).t0;
    }

    private <T> Tuple2<T, Integer> closestElementWithIndex(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        Tuple3<T, Integer, Double>
            closestElementWithIndexAndDistance = this.closestElementWithIndexAndDistance(iterable, distanceMetric);
        return new Tuple2<>(closestElementWithIndexAndDistance.t0, closestElementWithIndexAndDistance.t1);
    }

    private <T> Tuple3<T, Integer, Double> closestElementWithIndexAndDistance(Iterable<T> iterable, Function<T, Double> distanceMetric) {
        Iterator<T>
            iterator = iterable.iterator();
        T
            closestElement = null;
        Double
            minimumDistance = Double.MAX_VALUE;
        Integer
            closestElementIndex = -1;
        int
            index = 1;
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

    public void printToXRNAFile(String path) {
        this.printToXRNAFile(new File(path));
    }

    public void printToXRNAFile(File file) {
        this.processInputs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            double
                meanX = 0d,
                meanY = 0d;
            for (Text text : this.nucleotideTexts) {
                meanX += text.x;
                meanY += text.y;
            }
            meanX /= this.nucleotideTexts.size();
            meanY /= this.nucleotideTexts.size();
            String
                complexName = "Unknown",
                fileString = "<ComplexDocument Name='" + complexName + "'>\n<SceneNodeGeom CenterX='" + String.format(DOUBLE_FORMAT_STRING, -meanX) + "' CenterY='" + String.format(DOUBLE_FORMAT_STRING, -meanY) + "' Scale='1.0' />\n<Complex Name='" + complexName + "'>\n<RNAMolecule Name='" + complexName + "'>\n<NucListData StartNucID='1' DataType='NucChar.XPos.YPos'>\n";
            for (Text nucleotide : this.nucleotideTexts) {
                fileString += nucleotide.content + " " + String.format(DOUBLE_FORMAT_STRING, nucleotide.x) + " " + String.format(DOUBLE_FORMAT_STRING, nucleotide.y) + "\n";
            }
            fileString += "</NucListData>\n";
            int
                numNucleotides = this.nucleotideTexts.size();
            fileString += "<Nuc RefIDs='1-" + numNucleotides + "' Color='0' FontID='0' FontSize='" + DEFAULT_FONT_SIZE + "' />\n";
            HashMap<Style, ArrayList<Integer>>
                map = new HashMap<>();
            int
                nucleotideIndex = 1;
            for (Text nucleotideText : this.nucleotideTexts) {
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
                fileString += "<Nuc RefID='" + labelDatum.t2 + "'>\n";
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
                fileString += "<BasePairs nucID='" + basePairsDatum.t0 + "' length='1' bpNucID='" + basePairsDatum.t1 + "' />\n";
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
                    ArrayList<Text>
                        nucTexts = XRNAData.this.inputNucleotideTexts();
                    for (ArrayList<Text> texts : Arrays.asList(nucTexts, XRNAData.this.inputLabelTexts(), XRNAData.this.inputHelixTexts())) {
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
                            g2D.setColor(Color.WHITE);
                            double
                                radius = 1d,
                                diameter = 2d * radius;
                            // g2D.draw(new Ellipse2D.Double(bounds.getCenterX() - radius, bounds.getCenterY() - radius, diameter, diameter));

                            g2D.setFont(boundsAndFont.t1);
                            g2D.setColor(new Color(255 - style.fill.getRed(), 255 - style.fill.getGreen(), 255 - style.fill.getBlue()));
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
                            // g2D.draw(new Line2D.Double(line.x0, line.y0, line.x1, line.y1));
                        }
                    }

                    for (Line nucBondLine : nucBondLines) {
                        nucBondLine = nucBondLine.scale(2d);
                        g2D.setColor(Color.ORANGE);
                        g2D.draw(new Line2D.Double(nucBondLine.x0, nucBondLine.y0, nucBondLine.x1, nucBondLine.y1));
                        Vector2
                            v0 = new Vector2(nucBondLine.x0, nucBondLine.y0),
                            v1 = new Vector2(nucBondLine.x1, nucBondLine.y1);
                        BiFunction<Vector2, Text, Double>
                            distanceMetric =  (Vector2 v, Text nucText) -> {
                                return Vector2.distance(new Vector2(nucText.x, nucText.y), v);
                            };
                        Tuple2<Text, Integer>
                            nearestNucText0 = XRNAData.this.closestElementWithIndex(nucTexts, (Text nucText) -> distanceMetric.apply(v0, nucText)),
                            nearestNucText1 = XRNAData.this.closestElementWithIndex(nucTexts, (Text nucText) -> distanceMetric.apply(v1, nucText));
                        g2D.setColor(Color.RED);
                        Rectangle2D.Double
                            bounds0 = XRNAData.getBounds(nearestNucText0.t0),
                            bounds1 = XRNAData.getBounds(nearestNucText1.t0);
                        g2D.draw(new Line2D.Double(bounds0.getCenterX(), bounds0.getCenterY(), v0.x, v0.y));
                        g2D.setColor(Color.GREEN);
                        g2D.draw(new Line2D.Double(bounds1.getCenterX(), bounds1.getCenterY(), v1.x, v1.y));
                    }
                }
            };
        panel.setBackground(Color.BLACK);
        Dimension
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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

    public static Tuple2<Text, Boolean> findMostDirectLabelText(Vector2 center, Vector2 direction, ArrayList<Text> nearbyLabelTexts) {
        double
            maximumDotProductMagnitude = 0d;
        boolean
            maximumMagnitudeDotProductIsNegative = false;
        Text
            labelText = null;
        for (Text nearbyLabelText : nearbyLabelTexts) {
            double
                dotProduct = Vector2.dot(direction, Vector2.normalize(Vector2.subtract(new Vector2(nearbyLabelText.x, nearbyLabelText.y), center))),
            dotProductMagnitude = Math.abs(dotProduct);
            if (dotProductMagnitude > maximumDotProductMagnitude) {
                maximumDotProductMagnitude = dotProductMagnitude;
                maximumMagnitudeDotProductIsNegative = dotProduct < 0;
                labelText = nearbyLabelText;
            }
        }
        return new Tuple2<>(labelText, maximumMagnitudeDotProductIsNegative);
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

    public static final double
        EPSILON = 1E-4d,
        DEFAULT_STROKE_WIDTH = 0.2d;

    public static final String
        FLOATING_POINT_REGEX = "-?((\\d+)|((\\d+)?\\.\\d+))(e-?\\d+)?",
        HEADER_ELEMENT_REGEX = "\\w[\\w\\d-]*\\s*=\\s*\"[^\"]*\"",
        DOUBLE_FORMAT_STRING = "%.3f",
        DEFAULT_FONT_FAMILY = Font.DIALOG,
        DEFAULT_LABEL = "";

    public static final int
        DEFAULT_FONT_SIZE = 8;

    public static final Color
        DEFAULT_FONT_FILL = Color.BLACK,
        DEFAULT_STROKE = Color.BLACK;

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
            content;
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
