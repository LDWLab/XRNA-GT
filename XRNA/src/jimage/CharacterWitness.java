package jimage;


import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
public class
CharacterWitness
extends Frame
{

public static void
main(String[] args)
{
	// The following line compensates for a JDK 1.2beta4 bug.
	// GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

	// String fontName = "Serif";
	String fontName = "Helvetica";

	if (args.length > 0)
		fontName = args[0];
	Frame f = new CharacterWitness(fontName);
	f.setVisible(true);
}

public
CharacterWitness(String fontName)
{
	super("CharacterWitness v1.0");
	createUI(fontName);
}

protected void
createUI(String fontName)
{
	// setFont(new Font("Serif", Font.PLAIN, 12));
	setFont(new Font("Helvetica", Font.PLAIN, 12));
	addWindowListener(new WindowAdapter()
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);
		};
	});

	setSize(300, 600);
	center();
	setLayout(new BorderLayout());
	// Add the CharacterWitness component.
	boolean detail = true;
	boolean lines = true;
	Font font = new Font(fontName, Font.PLAIN, 256);
	final CharacterWitnessComponent cwc =
		new CharacterWitnessComponent(font, detail, lines);
	add(cwc, BorderLayout.CENTER);

	// Add the controls for entering a character code.
	Panel topControls = new Panel(new FlowLayout());
	topControls.add(new Label("Unicode character code:"));
	// final TextField tf = new TextField(4);
	final TextField tf = new TextField("0041");

	topControls.add(tf);
	add(topControls, BorderLayout.NORTH);
	tf.addTextListener(new TextListener()
	{
		public void textValueChanged(TextEvent te)
		{
			try
			{
				String codeString = tf.getText();
				char code = (char)(Integer.decode("0x" + codeString).intValue());
				if (code != 0)
					cwc.setCharacterCode(code);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
	});
	// Add controls for advance detail and vertical lines.
	Panel bottomControls = new Panel(new FlowLayout());
	final Checkbox detailCheckbox = new Checkbox("Advance detail", detail);
	bottomControls.add(detailCheckbox);
	final Checkbox lineCheckbox = new Checkbox("Line ends", lines);
	bottomControls.add(lineCheckbox);
	add(bottomControls, BorderLayout.SOUTH);
	detailCheckbox.addItemListener(new ItemListener()
	{
		public void itemStateChanged(ItemEvent ie)
		{
			cwc.setAdvanceDetail(detailCheckbox.getState());
		}
	});
	lineCheckbox.addItemListener(new ItemListener()
	{
		public void itemStateChanged(ItemEvent ie)
		{
			cwc.setVerticalLines(lineCheckbox.getState());
		}
	});
}

protected void
center()
{
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension us = getSize();
	int x = (screen.width - us.width) / 2;
	int y = (screen.height - us.height) / 2;
	setLocation(x, y);
}

}

class CharacterWitnessComponent
extends Component
{

private char mCharacter;
private Font mFont;
private boolean mAdvanceDetail;
private boolean mVerticalLines;

public CharacterWitnessComponent(Font font, boolean detail, boolean lines)
{
	mFont = font;
	mAdvanceDetail = detail;
	mVerticalLines = lines;
}

public void
setCharacterCode(char code)
{
	mCharacter = code;
	repaint();
}

public void
setFont(Font font)
{
	mFont = font;
	repaint();
}

public void
setAdvanceDetail(boolean b)
{
	mAdvanceDetail = b;
	repaint();
}

public void
setVerticalLines(boolean b)
{
	mVerticalLines = b;
	repaint();
}

public void
paint(Graphics g)
{
	Graphics2D g2 = (Graphics2D)g;
	if (mFont != null)
		g2.setFont(mFont);
	Font font = g2.getFont();
	if (font.canDisplay(mCharacter) == false)
		return;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	String s = new String(new char[] { mCharacter });

	// Gather information about the glyph.
	FontRenderContext frc = g2.getFontRenderContext();
	GlyphVector gv = font.createGlyphVector(frc, s);
	if (gv.getNumGlyphs() > 1)
		System.out.println("gv.getNumGlyphs() == " + gv.getNumGlyphs());
	GlyphMetrics gm = gv.getGlyphMetrics(0);
	float advance = gm.getAdvance();
	float width = (float)gm.getBounds2D().getWidth();
	float lsb = gm.getLSB();
	float rsb = gm.getRSB();
	LineMetrics lm = font.getLineMetrics(s, frc);
	Dimension d = getSize();
	float x = (d.width - advance) / 2;
	float y = (d.height + lm.getAscent()) / 2;
	g2.setPaint(Color.lightGray);
	if (mAdvanceDetail == false)
		drawHorizontalArrow(g2, x, y, x + advance);
	// advance
	drawHorizontalArrow(g2, x, y - lm.getAscent(), x + advance);
	// ascent
	g2.setPaint(Color.red);
	drawHorizontalArrow(g2, x, y + lm.getDescent(), x + advance);
	// descent
	g2.setPaint(Color.green);
	drawHorizontalArrow(g2, x, y + lm.getDescent() + lm.getLeading(), x + advance);
	// leading
	// Draw the character itself.
	g2.setPaint(Color.black);
	g2.drawString(s, x, y);
	if (mAdvanceDetail == true)
	{
		g2.setPaint(Color.blue);
		drawHorizontalArrow(g2, x + lsb, y, x + lsb + width);
		// width
		g2.setPaint(Color.red);
		drawHorizontalArrow(g2, x, y, x + lsb);
		// left side bearing
		drawHorizontalArrow(g2, x + width + lsb, y, x + width + lsb + rsb);
		// right side bearing
	}
}

protected void
drawHorizontalArrow(Graphics2D g2, float x0, float y0, float x1)
{
	// Draw the line itself
	Line2D line = new Line2D.Float(x0, y0, x1, y0);
	g2.draw(line);
	// Create a GeneralPath representing the arrowhead.
	float side = 3.0f;
	GeneralPath path = new GeneralPath();
	path.moveTo(x1, y0);
	float arrowBase = (x0 < x1) ? x1 - side : x1 + side;
	path.lineTo(arrowBase, y0 - side);
	path.lineTo(arrowBase, y0 + side);
	path.lineTo(x1, y0);
	g2.fill(path);
	if (mVerticalLines == true)
	{
		// Draw vertical lines for reference.
		float verticalSize = 12.0f;
		line = new Line2D.Float(x0, y0 - verticalSize, x0, y0 + verticalSize);
		g2.draw(line);
		line = new Line2D.Float(x1, y0 - verticalSize, x1, y0 + verticalSize);
		g2.draw(line);
	}
}

}
