package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ssview.Vector2;

public class Test {
	private static ArrayList<Vector2>
		labelLineEndpoints = new ArrayList<Vector2>(),
		nucleotideLineEndpoints = new ArrayList<Vector2>(),
		helixLabelPoints = new ArrayList<Vector2>(),
		labelPoints = new ArrayList<Vector2>(),
		letterPoints = new ArrayList<Vector2>(),
		intersections = new ArrayList<Vector2>(),
		intersections2 = new ArrayList<Vector2>();
	private static HashMap<Character, Rectangle2D.Float> letterBoundsMap = new HashMap<Character, Rectangle2D.Float>();
	private static ArrayList<String>
		labels = new ArrayList<String>(),
		helixLabels = new ArrayList<String>(),
		letters = new ArrayList<String>(),
		labelClasses = new ArrayList<String>(),
		helixClasses = new ArrayList<String>(),
		letterClasses = new ArrayList<String>();
	private static ArrayList<Rectangle2D.Float>
		letterBounds = new ArrayList<Rectangle2D.Float>(),
		labelBounds = new ArrayList<Rectangle2D.Float>();
	private static HashMap<String, Object[]> styleInfo = new HashMap<String, Object[]>();
	private static Font defaultFont = new Font("MyriadPro-Semibold", Font.PLAIN, 4);
	private static float
		minScalar = 0.1f,
		maxScalar = 5f,
		scalar = 1f,
		boundsScalar = 1f;
	private static Vector2 dv = new Vector2();
	
	public static void main(String[] args) {
//		File file = new File("C:\\Users\\caede\\Desktop\\XRNA\\geobacillus_stearothermophilus.svg");
		File file = new File("C:\\Users\\caede\\Desktop\\XRNA\\GC14_75_LSU_lc_asp.svg");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				if (line.toUpperCase().contains("LABELS_LINES")) {
					readLines(reader, labelLineEndpoints);
				} else if (line.toUpperCase().contains("LABELS_TEXT")) {
					readText(reader, labelPoints, labels, labelClasses);
				} else if (line.toUpperCase().contains("HELIX_LABELS")) {
					readText(reader, helixLabelPoints, helixLabels, helixClasses);
				} else if (line.toUpperCase().contains("LETTERS")) {
					readText(reader, letterPoints, letters, letterClasses);
				} else if (line.toUpperCase().contains("NUCLEOTIDE_LINES")) {
					readLines(reader, nucleotideLineEndpoints);
				} else if (line.toUpperCase().contains("<STYLE")) {
					readStyleInfo(reader, styleInfo);
				}
				line = reader.readLine();
			}
			reader.close();
			
			for (Iterator<HashMap.Entry<String, Object[]>> itr = styleInfo.entrySet().iterator(); itr.hasNext();) {
				HashMap.Entry<String, Object[]> entryI = itr.next();
//				System.out.println("(" + entryI.getKey() + ", " + Arrays.toString(entryI.getValue()) + ")");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Font
			labelFont = new Font("MyriadPro-Semibold", Font.PLAIN, 4),
			letterFont = new Font("MyriadPro-Light", Font.PLAIN, 4);
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				Graphics2D graphics2D = (Graphics2D)graphics;
				graphics2D.translate(dv.x, dv.y);
				graphics2D.scale(scalar, scalar);
				graphics2D.setColor(Color.BLACK);
				for (int i = 0; i < labelLineEndpoints.size(); i += 2) {
					Vector2
						linePoint0 = labelLineEndpoints.get(i),
						linePoint1 = labelLineEndpoints.get(i + 1);
					graphics2D.draw(new Line2D.Float(linePoint0.x, linePoint0.y, linePoint1.x, linePoint1.y));
				}
				for (int i = 0; i < nucleotideLineEndpoints.size(); i += 2) {
					Vector2
						linePoint0 = nucleotideLineEndpoints.get(i),
						linePoint1 = nucleotideLineEndpoints.get(i + 1);
					graphics2D.draw(new Line2D.Float(linePoint0.x, linePoint0.y, linePoint1.x, linePoint1.y));
				}
				graphics2D.setColor(new Color(8 * 15, 0, 0));
				graphics2D.setFont(labelFont);
				for (int i = 0; i < labels.size(); i++) {
					Vector2 labelPoint = labelPoints.get(i);
					graphics2D.drawString(labels.get(i), labelPoint.x, labelPoint.y);
				}
				
//				graphics2D.setColor(Color.RED);
//				Vector2.fill(graphics2D, intersections2.get(0));
//				graphics2D.setColor(Color.GREEN);
//				Vector2.fill(graphics2D, intersections2.get(1));
//				graphics2D.setColor(Color.BLUE);
//				Vector2.fill(graphics2D, intersections2.get(2));
//				graphics2D.setColor(Color.MAGENTA);
//				Vector2.fill(graphics2D, intersections2.get(3));
				
				graphics.setColor(new Color(1f, 0f, 0f, 0.25f));
				for (int i = 0; i < labels.size(); i++) {
					graphics2D.draw(labelBounds.get(i));
				}
				graphics2D.setColor(Color.BLACK);
				graphics2D.setFont(letterFont);
				for (int i = 0; i < letters.size(); i++) {
					Vector2 labelPoint = letterPoints.get(i);
					graphics2D.drawString(letters.get(i), labelPoint.x, labelPoint.y);
				}
				graphics.setColor(new Color(1f, 0f, 0f, 0.25f));
				for (int i = 0; i < letters.size(); i++) {
					graphics2D.draw(letterBounds.get(i));
				}
				graphics.setColor(new Color(1f, 0f, 0f, 0.25f));
				for (int i = 0; i < intersections.size(); i++) {
					Vector2.fill(graphics2D, intersections.get(i));
				}
				Vector2 negatedDv = Vector2.negate(dv);
				graphics2D.translate(negatedDv.x, negatedDv.y);
			}
		};
		panel.setBackground(Color.WHITE);
		frame.add(panel);
		frame.setSize(new Dimension(612, 792));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new KeyAdapter() {
			private float multiplicativeDeltaScalar = 1.01f;
			
			@Override
			public void keyPressed(KeyEvent event) {
				switch (event.getKeyCode()) {
					case KeyEvent.VK_UP:
						scalar = Math.min(maxScalar, scalar * this.multiplicativeDeltaScalar);
						frame.repaint();
						break;
					case KeyEvent.VK_DOWN:
						scalar = Math.max(minScalar, scalar / this.multiplicativeDeltaScalar);
						frame.repaint();
						break;
				}
			}
		});
		MouseAdapter mouseAdapter = new MouseAdapter() {
			Vector2
				mousePressedLocation,
				dv0;
			
			@Override
			public void mousePressed(MouseEvent event) {
				mousePressedLocation = new Vector2(event.getX(), event.getY());
				dv0 = dv;
			}
			
			@Override
			public void mouseDragged(MouseEvent event) {
				dv = Vector2.add(dv0, Vector2.subtract(new Vector2(event.getX(), event.getY()), mousePressedLocation));
				frame.repaint();
			}
		};
		frame.addMouseListener(mouseAdapter);
		frame.addMouseMotionListener(mouseAdapter);
		
		try {
			String documentName = "Foo";
			file = new File("C:\\Users\\caede\\Desktop\\XRNA\\foo.xrna");
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println("<ComplexDocument Name='" + documentName + "'>");
			writer.println("<SceneNodeGeom CenterX='0.0' CenterY='0.0' Scale='2.0' />");
			writer.println("<Complex Name='" + documentName + "'>");
			writer.println("<RNAMolecule Name='" + documentName + "'>");
			writer.println("<NucListData StartNucID='1' DataType='NucChar.XPos.YPos'>");
			
			Vector2 letterVector = letterPoints.get(0);
			float
				minX = letterVector.x,
				maxX = minX,
				minY = letterVector.y,
				maxY = minY;
			for (int i = 1; i < letters.size(); i++) {
				letterVector = letterPoints.get(i);
				if (letterVector.x < minX) {
					minX = letterVector.x;
				} else if (letterVector.x > maxX) {
					maxX = letterVector.x;
				}
				
				if (letterVector.y < minY) {
					minY = letterVector.y;
				} else if (letterVector.y > maxY) {
					maxY = letterVector.y;
				}
			}
			float
				rangeX = maxX - minX,
				rangeY = maxY - minY,
				midX = (maxX + minX) / 2f,
				midY = (maxY + minY) / 2f,
				scalar = 1f;// / Math.min((float)(NucCollection2D.MAX_XCO - NucCollection2D.MAX_XCO) / rangeX, rangeY);
			int fontSize = 4;
			Vector2 midXY = new Vector2(midX, midY);
			minX -= midX;
			maxX -= midX;
			minY -= midY;
			maxY -= midY;
			for (int i = 0; i < letters.size(); i++) {
				String letterString = letters.get(i);
				letterVector = Vector2.subtract(letterPoints.get(i), midXY);
				letterVector.y = -letterVector.y;
				writer.println(letterString + " " + letterVector.x + " " + letterVector.y);
			}
			
			writer.println("</NucListData>");
			String lineStarter = "<Nuc RefIDs='1-" + letters.size() + "'";
			writer.println(lineStarter + " Color='ff0000' FontID='0' FontSize='" + fontSize + "'/>");
			writer.println(lineStarter + " IsSchematic='false' SchematicColor='0' SchematicLineWidth='1.5' SchematicBPLineWidth='1.0' SchematicBPGap='2.0' SchematicFPGap='2.0' SchematicTPGap='2.0' IsNucPath='false' NucPathColor='ff0000' NucPathLineWidth='0.0' />");
			
			FontMetrics metrics = panel.getFontMetrics(defaultFont);
			for (char c : Arrays.asList('A', 'C', 'G', 'T', 'U')) {
				letterBoundsMap.put(c, new Rectangle2D.Float(0f, 0f, metrics.charWidth(c) * boundsScalar, defaultFont.getSize() * boundsScalar));
			}
			makeLetterBounds();
			for (Label label : makeLabels(metrics)) {
				writer.println("<Nuc RefID='" + label.nucID + "'>");
				writer.println("<LabelList>");
				writer.println("l " + label.v0.x + " " + label.v0.y + " " + label.dv.x + " " + label.dv.y + " " + label.lineFontSize + " " + label.hexadecimalColor + " 0.0 0 0 0 0");
				if (label.label != null) {
					writer.println("s " + label.labelLocation.x + " " + label.labelLocation.y + " 0.0 " + label.labelFontSize + " 0 " + label.hexadecimalColor + " \"" + label.label + "\"");
				}
				writer.println("</LabelList>");
				writer.println("</Nuc>");
			}
			for (Pair pair : makePairs()) {
				writer.println("<BasePairs nucID='" + pair.nucID + "' length='" + pair.length + "' bpNucID='" + pair.bpNucID + "' />");
			}
			
			writer.println("</RNAMolecule>");
			writer.println("</Complex>");
			writer.println("</ComplexDocument>");
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		frame.setVisible(true);
	}
	
	private static class Pair {
		int
			nucID,
			length,
			bpNucID;
		
		private Pair(int nucID, int length, int bpNucID) {
			this.nucID = nucID;
			this.length = length;
			this.bpNucID = bpNucID;
		}
	}
	
	private static float
		distanceScalar = 3f,
		distanceScalarOver2 = distanceScalar / 2f,
		scaledT0 = 0.5f - distanceScalarOver2,
		scaledT1 = 0.5f + distanceScalarOver2;
	
	private static void makeLetterBounds() {
		for (int i = 0; i < letters.size(); i++) {
			Vector2 letterPoint = letterPoints.get(i);
			try {
				Rectangle2D.Float untranslatedRectangle = letterBoundsMap.get(letters.get(i).charAt(0));
				letterBounds.add(new Rectangle2D.Float(letterPoint.x, letterPoint.y - untranslatedRectangle.height, untranslatedRectangle.width, untranslatedRectangle.height));
			} catch (StringIndexOutOfBoundsException ex) {
				System.out.println(letters.get(i));
				throw ex;
			}
		}
	}
	
	private static LinkedList<Pair> makePairs() {
		LinkedList<Pair> pairs = new LinkedList<Pair>();
		for (int i = 0; i < nucleotideLineEndpoints.size(); i += 2) {
			Vector2
				v0 = nucleotideLineEndpoints.get(i),
				v1 = nucleotideLineEndpoints.get(i + 1),
				interpScaledT0 = Vector2.interp(v0, v1, scaledT0),
				interpScaledT1 = Vector2.interp(v0, v1, scaledT1);
			float
				closestDistance0 = Float.POSITIVE_INFINITY,
				closestDistance1 = Float.POSITIVE_INFINITY;
			int
				closestIndex0 = -1,
				closestIndex1 = -1;
			for (int j = 0; j < letters.size(); j++) {
				Rectangle2D.Float letterBoundingBox = letterBounds.get(j);
				Vector2
					intersection0 = Vector2.intersect(letterBoundingBox, v0, interpScaledT1),
					intersection1 = Vector2.intersect(letterBoundingBox, interpScaledT0, v1);
				if (intersection0 != null) {
					float distance0 = Vector2.distance(v1, intersection0);
					if (distance0 < closestDistance0) {
						closestDistance0 = distance0;
						closestIndex0 = j + 1;
					}
				}
				if (intersection1 != null) {
					float distance1 = Vector2.distance(v0, intersection1);
					if (distance1 < closestDistance1) {
						closestDistance1 = distance1;
						closestIndex1 = j + 1;
					}
				}
			}
			pairs.add(new Pair(closestIndex0, 1, closestIndex1));
		}
		return pairs;
	}
	
	private static class Label {
		private int
			nucID,
			hexadecimalColor,
			labelFontSize;
		private Vector2
			v0,
			dv,
			labelLocation;
		private float lineFontSize;
		private String label;
		
		private Label(int nucID, int hexadecimalColor, int labelFontSize, Vector2 dv0, Vector2 dv1, Vector2 labelLocation, float lineFontSize, String label) {
			this.nucID = nucID;
			this.hexadecimalColor = hexadecimalColor;
			this.labelFontSize = labelFontSize;
			this.v0 = dv0;
			this.dv = dv1;
			this.labelLocation = labelLocation;
			this.lineFontSize = lineFontSize;
			this.label = label;
		}
	}
	
	private static LinkedList<Label> makeLabels(FontMetrics metrics) {
		LinkedList<Label> labels = new LinkedList<Label>();
		for (int i = 0; i < Test.labels.size(); i++) {
			String label = Test.labels.get(i);
			Vector2 labelPoint = labelPoints.get(i);
			float height = defaultFont.getSize();
			labelBounds.add(new Rectangle2D.Float(labelPoint.x, labelPoint.y - height * boundsScalar, metrics.stringWidth(label) * boundsScalar, height * boundsScalar));
		}
		for (int i = 0; i < labelLineEndpoints.size(); i += 2) {
			Vector2
				v0 = labelLineEndpoints.get(i),
				v1 = labelLineEndpoints.get(i + 1),
				interpScaledT0 = Vector2.interp(v0, v1, scaledT0),
				interpScaledT1 = Vector2.interp(v0, v1, scaledT1);
			float
				closestDistance0 = Float.POSITIVE_INFINITY,
				closestDistance1 = Float.POSITIVE_INFINITY;
			int
				closestIndex0 = -1,
				closestIndex1 = -1;
			Rectangle2D.Float letterBoundingBox = null;
			for (int j = 0; j < letters.size(); j++) {
				letterBoundingBox = letterBounds.get(j);
				Vector2
					intersection0 = Vector2.intersect(letterBoundingBox, v0, interpScaledT1),
					intersection1 = Vector2.intersect(letterBoundingBox, interpScaledT0, v1);
				if (intersection0 != null) {
					intersections.add(intersection0);
					float distance0 = Vector2.distance(v1, intersection0);
					if (distance0 < closestDistance0) {
						closestDistance0 = distance0;
						closestIndex0 = j + 1;
					}
				}
				if (intersection1 != null) {
					float distance1 = Vector2.distance(v0, intersection1);
					intersections.add(intersection1);
					if (distance1 < closestDistance1) {
						closestDistance1 = distance1;
						closestIndex1 = j + 1;
					}
				}
			}
			String label = null;
			Vector2 labelLocation = null;
			for (int j = 0; j < Test.labels.size(); j++) {
				Rectangle2D.Float labelBoundingBox = labelBounds.get(j);
				Vector2
					intersection0 = Vector2.intersect(labelBoundingBox, v0, interpScaledT1),
					intersection1 = Vector2.intersect(labelBoundingBox, interpScaledT0, v1),
					labelLocationDv = labelLocationDv(labelBoundingBox);
				if (j == 43 & (intersection0 != null | intersection1 != null)) {
//					System.out.println("intersection0 != null: " + (intersection0 != null) + "\tintersection1 != null: " + (intersection1 != null));
					intersections2.addAll(Arrays.asList(interpScaledT0, v0, v1, interpScaledT1));
				}
				if (intersection0 != null) {
					intersections.add(intersection0);
//					float distance0 = Vector2.distance(v1, intersection0);
//					if (distance0 < closestDistance0) {
//						closestDistance0 = distance0;
					closestIndex0 = -1;
					label = Test.labels.get(j);
					labelLocation = Vector2.add(labelPoints.get(j), labelLocationDv);
//					}
				} else if (intersection1 != null) {
					intersections.add(intersection1);
//					float distance1 = Vector2.distance(v0, intersection1);
//					if (distance1 < closestDistance1) {
//						closestDistance1 = distance1;
					closestIndex1 = -1;
					label = Test.labels.get(j);
					labelLocation = Vector2.add(labelPoints.get(j), labelLocationDv);
//					}
				}
			}
			Vector2
				dv0,
				dv1;
			boolean
				closestIndex0Flag = (closestIndex0 == -1),
				closestIndex1Flag = (closestIndex1 == -1);
			int
				hexadecimalColor = 0;
			if (closestIndex0Flag ^ closestIndex1Flag) {
				DvData dvData = dvData(v0, v1, closestIndex0Flag, closestIndex0, closestIndex1, letterBoundingBox);
				if (labelLocation == null) {
					labelLocation = new Vector2();
				} else {
					labelLocation = Vector2.subtract(labelLocation, dvData.letterVector);
					labelLocation.y = -labelLocation.y;
				}
				v0 = new Vector2();
				labels.add(new Label(dvData.nucID, hexadecimalColor, defaultFont.getSize(), dvData.dv0, dvData.dv1, labelLocation, 0.2f, label));
			} else {
				float
					distanceThreshold = Vector2.distance(v0, interpScaledT0),
					angleThreshold = (float)Math.toRadians(15f);
				if (closestIndex0Flag) {
					
				} else {
					labelLoop: for (int j = 0; j < Test.labels.size(); j++) {
						Rectangle2D.Float rectJ = Test.labelBounds.get(j);
						Vector2
							v0MinusV1 = Vector2.subtract(v0, v1),
							v1MinusV0 = Vector2.negate(v0MinusV1),
							verticesJ[] = Vector2.vertices(rectJ);
						for (Vector2 vertex : verticesJ) {
							if (Vector2.distance(vertex, v0) <= distanceThreshold && Math.abs(Vector2.angle(v0MinusV1, Vector2.subtract(vertex, v0))) <= angleThreshold) {
								DvData dvData = dvData(v0, v1, closestIndex0Flag, closestIndex0, closestIndex1, letterBoundingBox);
								labels.add(new Label(closestIndex1, hexadecimalColor, defaultFont.getSize(), dvData.dv0, dvData.dv1, Vector2.add(Test.labelPoints.get(j), labelLocationDv(labelBounds.get(j))), 0.2f, Test.labels.get(j)));
								break labelLoop;
							} else if (Vector2.distance(vertex, v0) <= distanceThreshold) {
//								System.out.println(Math.toDegrees(Vector2.angle(v0MinusV1, Vector2.subtract(vertex, v0))) + "\t" + );
							}
						}
						for (Vector2 vertex : verticesJ) {
							if (Vector2.distance(vertex, v1) <= distanceThreshold && Math.abs(Vector2.angle(v1MinusV0, Vector2.subtract(vertex, v1))) <= angleThreshold) {
								DvData dvData = dvData(v0, v1, closestIndex0Flag, closestIndex0, closestIndex1, letterBoundingBox);
								labels.add(new Label(closestIndex0, hexadecimalColor, defaultFont.getSize(), dvData.dv0, dvData.dv1, Vector2.add(Test.labelPoints.get(j), labelLocationDv(labelBounds.get(j))), 0.2f, Test.labels.get(j)));
								break labelLoop;
							} else if (Vector2.distance(vertex, v1) <= distanceThreshold) {
								System.out.println(Math.toDegrees(Vector2.angle(v1MinusV0, Vector2.subtract(vertex, v1))));
							}
						}
					}
				}
			}
		}
		return labels;
	}
	
	private static class DvData {
		private int nucID;
		private Vector2
			letterVector,
			dv0,
			dv1;
		
		private DvData(int nucID, Vector2 letterVector, Vector2 dv0, Vector2 dv1) {
			this.nucID = nucID;
			this.letterVector = letterVector;
			this.dv0 = dv0;
			this.dv1 = dv1;
		}
	}
	
	private static DvData dvData(Vector2 v0, Vector2 v1, boolean closestIndex0Flag, int closestIndex0, int closestIndex1, Rectangle2D.Float letterBoundingBox) {
		int nucID = closestIndex0Flag ? closestIndex1 : closestIndex0;
		Vector2
			dvDv = new Vector2(-letterBoundingBox.width / 2f, -letterBoundingBox.height / 2f * 0.8f),
			letterVector = letterPoints.get(nucID - 1),
			dv0 = Vector2.subtract(v0, letterVector),
			dv1 = Vector2.subtract(v1, letterVector);
		dv0.y = -dv0.y;
		dv1.y = -dv1.y;
		dv0 = Vector2.add(dv0, dvDv);
		dv1 = Vector2.add(dv1, dvDv);
		return new DvData(nucID, letterVector, dv0, dv1);
	}
	
	private static Vector2 labelLocationDv(Rectangle2D.Float boundingBox) {
		return new Vector2(boundingBox.width / 2f * 0.55f, 0f);//labelBoundingBox.height / 2f));
	}
	
	private static void readLines(BufferedReader reader, ArrayList<Vector2> vList) throws IOException {
		for (String line = reader.readLine(); line != null && !line.contains("</g>"); line = reader.readLine()) {
			String query = "x1=\"";
			int queryIndex = line.indexOf(query);
			line = line.substring(queryIndex + query.length());
			float x1 = Float.parseFloat(line.substring(0, line.indexOf("\"")));
			query = "y1=\"";
			queryIndex = line.indexOf(query);
			line = line.substring(queryIndex + query.length());
			float y1 = Float.parseFloat(line.substring(0, line.indexOf("\"")));
			query = "x2=\"";
			queryIndex = line.indexOf(query);
			line = line.substring(queryIndex + query.length());
			float x2 = Float.parseFloat(line.substring(0, line.indexOf("\"")));
			query = "y2=\"";
			queryIndex = line.indexOf(query);
			line = line.substring(queryIndex + query.length());
			float y2 = Float.parseFloat(line.substring(0, line.indexOf("\"")));
			vList.add(new Vector2(x1, y1));
			vList.add(new Vector2(x2, y2));
		}
	}
	
	private static void readText(BufferedReader reader, ArrayList<Vector2> vList, ArrayList<String> strList, ArrayList<String> classesList) throws IOException {
		int countGroups = 1;
		for (String line = reader.readLine(); line != null && countGroups > 0; line = reader.readLine()) {
			if (line.contains("<g")) {
				countGroups++;
			}
			if (line.contains("</g>")) {
				countGroups--;
			}
//			System.out.println(line + " " + countGroups);
			if (line.contains("matrix") & line.contains("<")) {
				String query = "class=\"";
				int
					queryIndex = line.indexOf(query),
					startQueryIndexPlusQueryLength = queryIndex + query.length();
				query = "\"";
				queryIndex = line.indexOf(query, startQueryIndexPlusQueryLength);
				classesList.add(line.substring(startQueryIndexPlusQueryLength, queryIndex));
				query = "matrix(";
				queryIndex = line.indexOf(query);
				line = line.substring(queryIndex + query.length());
				for (int i = 0; i < 4; i++) {
					line = line.substring(line.indexOf(" ") + 1);
				}
				queryIndex = line.indexOf(" ");
				float x = Float.parseFloat(line.substring(0, queryIndex));
				line = line.substring(queryIndex + 1);
				float y = Float.parseFloat(line.substring(0, line.indexOf(")")));
				line = line.substring(line.indexOf(">") + 1);
				String str = line.substring(0, line.indexOf("<"));
				if (str.length() > 0) {
					vList.add(new Vector2(x, y));
					strList.add(str);
				}
			}
		}
	}

	private static void readStyleInfo(BufferedReader reader, HashMap<String, Object[]> styleInfo) throws IOException {
		for (String line = reader.readLine(); line != null && !line.contains("</style>"); line = reader.readLine()) {
			String query = ".";
			int queryIndex = line.indexOf(query);
			line = line.substring(queryIndex + query.length());
			query = "{";
			queryIndex = line.indexOf(query);
			int startIndex = queryIndex + query.length();
			String name = line.substring(0, queryIndex);
			// Style entries contain font size, font family, fill color in that order. 
			Object[] styleEntry = new Object[3];
			query = "}";
			queryIndex = line.indexOf(query);
			String subLine = line.substring(startIndex, queryIndex);
			String[] styleEntryComponents = subLine.split(";");
			for (int i = 0; i < styleEntryComponents.length; i++) {
				String
					styleEntryComponentI = styleEntryComponents[i],
					styleEntryComponentIComponents[] = styleEntryComponentI.split(":");
				String styleEntryComponentsIComponents1 = styleEntryComponentIComponents[1];
				switch(styleEntryComponentIComponents[0].trim().toLowerCase()) {
					case "font-size":
						styleEntry[0] = Float.parseFloat(styleEntryComponentsIComponents1.replace("px", ""));
						break;
					case "font-family":
						styleEntry[1] = styleEntryComponentsIComponents1.replace("'", "");
						break;
					case "fill":
						styleEntry[2] = styleEntryComponentsIComponents1.replace("#", "");
						break;
				}
			}
			styleInfo.put(name, styleEntry);
		}
	}
}
