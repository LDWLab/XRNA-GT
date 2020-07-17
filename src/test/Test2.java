package test;

import java.awt.BasicStroke;
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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test2 {
	private static String
		pathName = "C:\\Users\\caede\\Desktop\\XRNA\\GC14_75_LSU_lc_asp.svg";
	private static LinkedList<Tuple3<Character, Vector2, Ellipse2D>>
		letters = new LinkedList<Tuple3<Character, Vector2, Ellipse2D>>();
	private static LinkedList<Tuple3<Vector2, Vector2, Rectangle2D>>
		nucleotideLines = new LinkedList<Tuple3<Vector2, Vector2, Rectangle2D>>(),
		labelLines = new LinkedList<Tuple3<Vector2, Vector2, Rectangle2D>>();
	private static LinkedList<Tuple3<String, Vector2, Rectangle2D>>
		labels = new LinkedList<Tuple3<String, Vector2, Rectangle2D>>();
	private static double
		scalar = 1d,
		scalarMin = 1d / 5d,
		scalarMax = 5d,
		multiplicativeDScalar = 1.05d;
	private static Vector2
		dv = new Vector2();
	private static int
		fontSize = 3;
	private static Font
		font = new Font(Font.DIALOG, Font.PLAIN, fontSize);
	
	public static void main(String[] args) {
		try {
			Dimension frameSize = null;
			BufferedReader reader = new BufferedReader(new FileReader(new File(pathName)));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				int
					indexViewBox = line.indexOf("viewBox");
				if (indexViewBox != -1) {
					indexViewBox = line.indexOf("\"", indexViewBox + 7) + 1;
					String[]
						viewBoxEntries = line.substring(indexViewBox, line.indexOf("\"", indexViewBox)).split(" ");
					frameSize = new Dimension(Integer.parseUnsignedInt(viewBoxEntries[2]), Integer.parseUnsignedInt(viewBoxEntries[3]));
				}
				if (line.contains("<g id=\"Letters\">")) {
					parseLetters(reader);
				} else if (line.contains("<g id=\"Nucleotide_Lines\">")) {
					parseLines(reader, nucleotideLines);
				} else if (line.contains("<g id=\"Labels_Lines\">")) {
					parseLines(reader, labelLines);
				} else if (line.contains("<g id=\"Labels_Text\">")) {
					parseLabels(reader);
				}
			}
			JFrame frame = new JFrame();
			JPanel panel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2D = (Graphics2D)g;
					g2D.translate(dv.x, dv.y);
					g2D.scale(scalar, scalar);
					g2D.setStroke(new BasicStroke(0.2f));
					g.setFont(font);
					g.setColor(Color.RED);
					for (Tuple3<Character, Vector2, Ellipse2D> tuple3 : letters) {
						g2D.draw(tuple3.t2);
					}
					for (Tuple3<String, Vector2, Rectangle2D> tuple3 : labels) {
						g2D.draw(tuple3.t2);
					}
					
					g.setColor(Color.WHITE);
					for (Tuple3<Character, Vector2, Ellipse2D> tuple3 : letters) {
						Vector2 location = tuple3.t1;
						g.drawString(tuple3.t0.toString(), (int)Math.round(location.x), (int)Math.round(location.y));
					}
					for (Tuple3<String, Vector2, Rectangle2D> tuple3 : labels) {
						Vector2 location = tuple3.t1;
						g.drawString(tuple3.t0, (int)Math.round(location.x), (int)Math.round(location.y));
					}
					for (Tuple3<Vector2, Vector2, Rectangle2D> line : labelLines) {
						Vector2
							v0 = line.t0,
							v1 = line.t1;
						g2D.draw(new Line2D.Double(v0.x, v0.y, v1.x, v1.y));
					}
					for (Tuple3<Vector2, Vector2, Rectangle2D> line : nucleotideLines) {
						Vector2
							v0 = line.t0,
							v1 = line.t1;
						g2D.draw(new Line2D.Double(v0.x, v0.y, v1.x, v1.y));
					}
				}
			};
			panel.setBackground(Color.DARK_GRAY);
			frame.add(panel);
			frame.setSize(frameSize);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent event) {
					int delta = event.getWheelRotation();
					scalar *= Math.pow(multiplicativeDScalar, -delta);
					if (scalar < scalarMin) {
						scalar = scalarMin;
					} else if (scalar > scalarMax) {
						scalar = scalarMax;
					}
					frame.repaint();
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
					dv = dv0.add(new Vector2(event.getX(), event.getY()).subtract(mousePressedLocation));
					frame.repaint();
				}
			};
			frame.addMouseListener(mouseAdapter);
			frame.addMouseMotionListener(mouseAdapter);
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
			frame.setVisible(true);
			
			Graphics2D g = (Graphics2D)frame.getGraphics();
			FontMetrics metrics = g.getFontMetrics(font);
			FontRenderContext renderContext = g.getFontRenderContext();
			for (Tuple3<String, Vector2, Rectangle2D> tuple3 : labels) {
				Rectangle2D t2 = metrics.getStringBounds(tuple3.t0, g);
				tuple3.t2 = t2;
				Vector2 t1 = tuple3.t1;
				t2.setRect(t1.x, t1.y - metrics.getAscent(), t2.getWidth(), t2.getHeight());
			}
			for (Tuple3<Character, Vector2, Ellipse2D> tuple3 : letters) {
				Rectangle2D t2 = metrics.getStringBounds(tuple3.t0.toString(), g);
				Vector2 t1 = tuple3.t1;
				double minDimension = Math.min(t2.getWidth(), t2.getHeight());
				tuple3.t2.setFrame(t1.x, t1.y - metrics.getAscent(), minDimension, minDimension);
			}
		} catch (IOException ex) {
			System.err.print(ex);
		}
	}
	
	public static void parseLetters(BufferedReader reader) throws IOException {
		for (String line = reader.readLine(); line != null && !line.contains("</g>"); line = reader.readLine()) {
			String matrixString = "matrix(";
			int
				indexMatrixPlusStringLength = line.indexOf(matrixString) + matrixString.length(),
				indexParen = line.indexOf(")", indexMatrixPlusStringLength);
			String[]
				matrixEntries = line.substring(indexMatrixPlusStringLength, indexParen).split(" ");
			letters.add(new Tuple3<Character, Vector2, Ellipse2D>(line.charAt(line.indexOf(">", indexParen + 1) + 1), new Vector2(Double.parseDouble(matrixEntries[4]), Double.parseDouble(matrixEntries[5])), new Ellipse2D.Double()));
		}
	}
	
	public static void parseLines(BufferedReader reader, LinkedList<Tuple3<Vector2, Vector2, Rectangle2D>> lines) throws IOException {
		for (String line = reader.readLine(); line != null && !line.contains("</g>"); line = reader.readLine()) {
			int
				indexX1 = line.indexOf("\"", line.indexOf("x1") + 2) + 1,
				indexX2 = line.indexOf("\"", line.indexOf("x2") + 2) + 1,
				indexY1 = line.indexOf("\"", line.indexOf("y1") + 2) + 1,
				indexY2 = line.indexOf("\"", line.indexOf("y2") + 2) + 1;
			lines.add(new Tuple3<Vector2, Vector2, Rectangle2D>(new Vector2(Double.parseDouble(line.substring(indexX1, line.indexOf("\"", indexX1))), Double.parseDouble(line.substring(indexY1, line.indexOf("\"", indexY1)))), new Vector2(Double.parseDouble(line.substring(indexX2, line.indexOf("\"", indexX2))), Double.parseDouble(line.substring(indexY2, line.indexOf("\"", indexY2)))), new Rectangle2D.Double()));
		}
	}
	
	public static void parseLabels(BufferedReader reader) throws IOException {
		for (String line = reader.readLine(); line != null && !line.contains("</g>"); line = reader.readLine()) {
			String matrixString = "matrix(";
			int
				indexMatrixPlusStringLength = line.indexOf(matrixString) + matrixString.length(),
				indexParen = line.indexOf(")", indexMatrixPlusStringLength);
			String[]
				matrixEntries = line.substring(indexMatrixPlusStringLength, indexParen).split(" ");
			int
				indexRightBracketPlusOne = line.indexOf(">", indexParen + 1) + 1,
				indexLeftBracket = line.indexOf("<", indexRightBracketPlusOne);
			labels.add(new Tuple3<String, Vector2, Rectangle2D>(line.substring(indexRightBracketPlusOne, indexLeftBracket), new Vector2(Double.parseDouble(matrixEntries[4]), Double.parseDouble(matrixEntries[5])), new Rectangle2D.Double()));
		}
	}
}