package jimage;

import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.awt.image.renderable.*;

/**
 * A subclass of JInternalFrame that contains
 * a RenderedImage.
 */

class ImageInternalFrame extends JInternalFrame {

        protected BufferedImage image;
        protected DisplayJAI imagePanel;
        private int index;
        private String scaleFactor;

	public ImageInternalFrame(String title, boolean resizable,
                                    boolean closable, boolean maximizable,
                                     boolean iconifiable) {

        	super(title, resizable, closable, maximizable, iconifiable);
		scaleFactor = "1x";

        }

	public BufferedImage getImage() {

		return image;

	}

        public void setImage(BufferedImage im) {

        	image = im;

        }

        public int getIndex() {

        	return index;

        }

        public String getScaleFactor() {

		return scaleFactor;

        }

        public void setScaleFactor(String s) {

        	scaleFactor = s;

        }

        public void setImagePanel(DisplayJAI ip) {

        	imagePanel = ip;

        }

        public DisplayJAI getImagePanel() {

        	return imagePanel;

        }

}


