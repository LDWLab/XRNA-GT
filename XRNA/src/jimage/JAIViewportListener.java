package jimage;

/*
 * Copyright (c) 1997-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * An interface used by the <code>JAIScrollingImagePanel</code>
 * class to inform listeners of the current viewable area of the image.
 *
 * @see JAIScrollingImagePanel
 *
 * <p>
 * This class has been deprecated.  The source
 * code has been moved to the samples/widget
 * directory.  These widgets are no longer
 * supported.
 *
 * @deprectated as of 1.1
 */
public interface JAIViewportListener {
    
    /**
     * Called to inform the listener of the currently viewable area od
     * the source image.
     *
     * @param x The X coordinate of the upper-left corner of the current 
     *          viewable area.
     * @param y The Y coordinate of the upper-left corner of the current 
     *          viewable area.
     * @param width The width of the current viewable area in pixels.
     * @param height The height of the current viewable area in pixels.
     */
    void setViewport(int x, int y, int width, int height);
}
