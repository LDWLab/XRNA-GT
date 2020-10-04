/**
 *  Example display class Swing Component that is able
 *  to contain an image.  The size of the image
 *  and size of the container can be different.
 *  The image can be positioned within the
 *  container.  This class extends JPanel in order
 *  to support layout management.  Tiling is supported
 *  as of JDK1.3 via drawRenderedImage().
 *
 *  @author Dennis Sigel
 *
 *  @see javax.swing.JPanel
 *  @see javax.swing.JComponent
 *  @see java.awt.image.RenderedImage
 */

package jimage;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.swing.*;

public class
DisplayJAI
extends JPanel
{
protected AffineTransform affineTrans;
protected JScrollPane sp;

/** image to display */
// protected RenderedImage source = null;

/** image origin relative to panel origin */
protected int originX = 0;
protected int originY = 0;

protected double sx, sy;

/** default constructor */
public DisplayJAI() {
	super();
	setLayout(null);
}

public
DisplayJAI(RenderedImage image)
{
	super();
	setLayout(null);
	// source = image;
	setPreferredSize(
		/*
		new Dimension(source.getWidth(), source.getHeight()));
		*/
		new Dimension(this.getImg().getWidth(), this.getImg().getHeight()));
}

private RenderedImage img = null;

public void
setImg(RenderedImage img)
{
    this.img = img;
}

public RenderedImage
getImg()
{
    return (this.img);
}

public void scale(double scaleFactor) {
	if ( (sx != 0) && (sy != 0) && (scaleFactor != 1) ) {
	   sx *= scaleFactor;
	   sy *= scaleFactor;
	}
	else {
	   sx = scaleFactor;
	   sy = scaleFactor;
	}
	repaint();
	/*
	setPreferredSize( new Dimension( (int)(sx * source.getWidth()),
									  (int)(sy * source.getHeight()) ) );
	*/
	setPreferredSize(new Dimension(
		(int)(sx * this.getImg().getWidth()),
		(int)(sy * this.getImg().getHeight()) ) );

	sp.revalidate();
}

/** move image within it's container */
public void setOrigin(int x, int y) {
	originX = x;
	originY = y;
	repaint();
}

/** get the image origin */
public Point getOrigin() {
	return new Point(originX, originY);
}

/** use to display a new image */
/*
public void setImage(RenderedImage im) {
	source = im;
	repaint();
}
*/

public void setScrollPane(JScrollPane scrollP) {
	sp = scrollP;
}

/** @returns the Image */
/*
public RenderedImage getImage() {
	return source;
}
*/

/** paint routine */
public synchronized void paintComponent(Graphics g) {

	Graphics2D g2d = (Graphics2D)g;

	// empty component (no image)
	/*
	if ( source == null ) {
	*/
	if (this.getImg() == null )
	{
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		return;
	}

	// account for borders
	Insets insets = getInsets();
	int tx = insets.left + originX;
	int ty = insets.top  + originY;

	// clear damaged component area
	Rectangle clipBounds = g2d.getClipBounds();
	g2d.setColor(getBackground());
	g2d.fillRect(clipBounds.x,
				 clipBounds.y,
				 clipBounds.width,
				 clipBounds.height);
System.out.println("MADE IT CLIP: " + " " +
	clipBounds.x + " " + clipBounds.y + " " +
	clipBounds.width + " " + clipBounds.height);

	/**
		Translation moves the entire image within the container
	*/
	affineTrans = new AffineTransform();
	affineTrans.setTransform( AffineTransform.getTranslateInstance(tx, ty) );
System.out.println("MADE IT TRANSFORM: " + tx + " " + ty);
System.out.println("MADE IT Img: " + this.getImg());

	if ( (sx != 0) && (sy != 0) )
	{
System.out.println("MADE IT SCALE: " + sx + " " + sy);
	   affineTrans.scale(sx, sy);
	}
	g2d.drawRenderedImage(this.getImg(), affineTrans);
}

}
