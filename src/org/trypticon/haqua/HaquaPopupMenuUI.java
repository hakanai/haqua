/*
 * Haqua - a collection of hacks to work around issues in the Aqua look and feel
 * Copyright (C) 2014  Trejkaz, Haqua Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.trypticon.haqua;

import com.apple.laf.AquaPopupMenuUI;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author trejkaz
 */
public class HaquaPopupMenuUI extends AquaPopupMenuUI {
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaPopupMenuUI();
    }

    @Override
    public Popup getPopup(JPopupMenu popupMenu, int x, int y) {
        Popup popup = super.getPopup(popupMenu, x, y);

        Component invoker = popupMenu.getInvoker();
        if (invoker instanceof JComboBox && HaquaComboBoxUI.isPopDown((JComboBox) invoker)) {
            // Pop-down from a combo box is supposed to be square, which already works.
            return popup;
        }

        // I tried making my own popup window class, but when I clicked on the popup menu,
        // it didn't seem to receive the mouse event.
        // So we're using this one which the superclass will give us, presumably already configured correctly.
        // We know it will always be heavyweight under Aqua, so this should be OK.
        final JWindow popupWindow = (JWindow) SwingUtilities.getWindowAncestor(popupMenu);

        if (!(invoker instanceof JComboBox)) {
            // When the invoker is a combo box, the menu uses a transparent panel. When it's any
            // other kind of popup menu, it is set to opaque, which messes up the rounded corners.
            popupMenu.setBackground(new Color(0, 0, 0, 0));
            popupMenu.setOpaque(false);
        }

        // setOpaque(true) and setShape() gives you an opaque square with rounded black corners.
        // setOpaque(false) and setShape() gives you rounded corners but no drop shadow.
        // setBackground() by itself removes the borders which should be painted.
        // Until that is fixed, we paint the window border and drop shadow ourselves.

        final Color originalBackground = popupWindow.getBackground();
        final Container originalContentPane = popupWindow.getContentPane();

        popupWindow.setBackground(new Color(0, 0, 0, 0));
        popupWindow.setContentPane(new DropShadowPanel(popupMenu));

        // Restore the window to its original state once it's hidden, because the PopupFactory
        // will cache the instance and things like tooltips will be messed up.
        // Using PopupMenuListener for this seems to be the most reliable. Using WindowListener
        // somehow results in getting the event after the next popup menu is being displayed,
        // which messes up the next component to reuse the menu.
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
                popupWindow.setBackground(originalBackground);
                popupWindow.setContentPane(originalContentPane);
                ((JPopupMenu) event.getSource()).removePopupMenuListener(this);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent event) {
            }
        });

        Point location = popupWindow.getLocation();
        location.x -= DropShadowPanel.LEFT_MARGIN;
        location.y -= DropShadowPanel.TOP_MARGIN;
        popupWindow.setLocation(location);

        return popup;
    }

    private static class DropShadowPanel extends JPanel {
        // These values were obtained via trial and error, comparing to the real thing. They are probably wrong.
        private static final int SHADOW_RADIUS = 12;
        private static final float SHADOW_OPACITY = 0.3f;
        private static final int ROUND_RECT_ARC = 8;
        private static final int ROUND_RECT_RADIUS = ROUND_RECT_ARC / 2;

        private static final int LEFT_MARGIN = SHADOW_RADIUS + 1;
        private static final int TOP_MARGIN = 1;

        private final JPopupMenu popupMenu;
        private final Dimension wrapSize;

        private DropShadowPanel(JPopupMenu popupMenu) {
            this.popupMenu = popupMenu;

            setBackground(new Color(0, 0, 0, 0));

            // Positioning the wrapped component completely inside the boundaries, so that it can't interfere
            // with the border we will be drawing.
            // This is important because menus paint directly via paintImmediately(), so we can't clip their painting.
            wrapSize = popupMenu.getPreferredSize();
            setLayout(null);
            add(popupMenu);
            popupMenu.setBounds(LEFT_MARGIN, TOP_MARGIN, wrapSize.width, wrapSize.height);
            popupMenu.doLayout();

            setPreferredSize(new Dimension(wrapSize.width + SHADOW_RADIUS * 2 + 2,
                    wrapSize.height + SHADOW_RADIUS * 2 + 2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            BufferedImage borderImage = createBorderImage();
            BufferedImage shadow = createShadowImage(borderImage);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(shadow, 0, 0, this);
            g2.drawImage(borderImage, SHADOW_RADIUS, 0, this);
            g2.dispose();
        }

        private BufferedImage createBorderImage() {
            BufferedImage wrapImage = Images.createCompatibleTranslucentImage(wrapSize.width + 2, wrapSize.height + ROUND_RECT_RADIUS * 2 + 2);
            Graphics2D wrapImageGraphics = wrapImage.createGraphics();
            try
            {
                wrapImageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = createPopupShape();

                // Background of the round rect
                wrapImageGraphics.setPaint(UIManager.getColor("PopupMenu.background"));
                wrapImageGraphics.fill(shape);

                // Outline of the round rect
                wrapImageGraphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                wrapImageGraphics.setPaint(new Color(0, 0, 0, 64));
                wrapImageGraphics.setComposite(AlphaComposite.Clear);
                wrapImageGraphics.draw(shape);
                wrapImageGraphics.setComposite(AlphaComposite.SrcOver);
                wrapImageGraphics.draw(shape);
            } finally {
                wrapImageGraphics.dispose();
            }

            return wrapImage;
        }

        private BufferedImage createShadowImage(BufferedImage borderImage) {
            ShadowRenderer shadowRenderer = new ShadowRenderer(SHADOW_RADIUS, SHADOW_OPACITY, Color.BLACK);
            return shadowRenderer.createShadow(borderImage);
        }

        private Shape createPopupShape() {
            int width = wrapSize.width + 1;
            int height = wrapSize.height + 1;
            Area shape = new Area(new RoundRectangle2D.Float(0, 0, width, height, ROUND_RECT_ARC, ROUND_RECT_ARC));

            Component invoker = popupMenu.getInvoker();
            if (!(invoker instanceof JMenu)) {
                return shape;
            }

            // If the menu came as a fly-out from some other menu, its shape depends on where the invoker is.
            // Figuring out which direction it came from is a bit fiddly.

            Rectangle invokerBounds = invoker.getBounds();
            invokerBounds.setLocation(invoker.getLocationOnScreen());
            Rectangle popupMenuBounds = popupMenu.getBounds();
            popupMenuBounds.setLocation(popupMenu.getLocationOnScreen());

            Point invokerCentre = new Point((int) invokerBounds.getCenterX(), (int) invokerBounds.getCenterY());
            if (popupMenuBounds.contains(invokerCentre)) {
                return shape;
            }

            Point popupMenuCentre = new Point((int) popupMenuBounds.getCenterX(), (int) popupMenuBounds.getCenterY());
            boolean poppedDown = invokerCentre.y <= popupMenuCentre.y;

            // If the invoker is to the left of the menu...
            if (invokerBounds.x + invokerBounds.width < popupMenuBounds.x + 10) {
                if (poppedDown) {
                    // Top left
                    shape.add(new Area(new Rectangle(0, 0, ROUND_RECT_ARC, ROUND_RECT_ARC)));
                }
                else {
                    // Bottom left
                    shape.add(new Area(new Rectangle(0, height - ROUND_RECT_ARC, ROUND_RECT_ARC, ROUND_RECT_ARC)));
                }
            }

            // If the invoker is to the right of the menu...
            else if (popupMenuBounds.x + popupMenuBounds.width < invokerBounds.x + 10) {
                if (poppedDown) {
                    // Top right
                    shape.add(new Area(new Rectangle(width - ROUND_RECT_ARC, 0, ROUND_RECT_ARC, ROUND_RECT_ARC)));
                }
                else {
                    // Bottom right
                    shape.add(new Area(new Rectangle(width - ROUND_RECT_ARC, height - ROUND_RECT_ARC, ROUND_RECT_ARC, ROUND_RECT_ARC)));
                }
            }

            // If the invoker is above the menu...
            else if (invokerBounds.y + invokerBounds.height < popupMenuBounds.y + 10) {
                shape.subtract(new Area(new Rectangle(0, 0, width, ROUND_RECT_ARC)));
            }

            return shape;
        }
    }


    // Originally taken from SwingX code. I didn't want to make a dependency on SwingX.
    // There may be other ways to do this more directly. e.g., Aqua LAF has some kind of shadow
    // rendering as part of the internal frame support, and theirs uses ConvolveOp somehow.
    private static class ShadowRenderer {
        private final int size;
        private final float opacity;
        private final Color color;

        private ShadowRenderer(final int size, final float opacity, final Color color) {
            this.size = size;
            this.opacity = opacity;
            this.color = color;
        }

        private BufferedImage createShadow(final BufferedImage image) {

            int shadowSize = size * 2;

            int srcWidth = image.getWidth();
            int srcHeight = image.getHeight();

            int dstWidth = srcWidth + shadowSize;
            int dstHeight = srcHeight + shadowSize;

            int left = size;
            int right = shadowSize - left;

            int yStop = dstHeight - right;

            int shadowRgb = color.getRGB() & 0x00FFFFFF;
            int[] aHistory = new int[shadowSize];
            int historyIdx;

            int aSum;

            BufferedImage dst = Images.createCompatibleTranslucentImage(dstWidth, dstHeight);

            int[] dstBuffer = new int[dstWidth * dstHeight];
            int[] srcBuffer = new int[srcWidth * srcHeight];

            Images.getPixels(image, 0, 0, srcWidth, srcHeight, srcBuffer);

            int lastPixelOffset = right * dstWidth;
            float hSumDivider = 1.0f / shadowSize;
            float vSumDivider = opacity / shadowSize;

            int[] hSumLookup = new int[256 * shadowSize];
            for (int i = 0; i < hSumLookup.length; i++) {
                hSumLookup[i] = (int) (i * hSumDivider);
            }

            int[] vSumLookup = new int[256 * shadowSize];
            for (int i = 0; i < vSumLookup.length; i++) {
                vSumLookup[i] = (int) (i * vSumDivider);
            }

            int srcOffset;

            // horizontal pass : extract the alpha mask from the source picture and
            // blur it into the destination picture
            for (int srcY = 0, dstOffset = left * dstWidth; srcY < srcHeight; srcY++) {

                // first pixels are empty
                for (historyIdx = 0; historyIdx < shadowSize; ) {
                    aHistory[historyIdx++] = 0;
                }

                aSum = 0;
                historyIdx = 0;
                srcOffset = srcY * srcWidth;

                // compute the blur average with pixels from the source image
                for (int srcX = 0; srcX < srcWidth; srcX++) {

                    int a = hSumLookup[aSum];
                    dstBuffer[dstOffset++] = a << 24;   // store the alpha value only
                    // the shadow color will be added in the next pass

                    aSum -= aHistory[historyIdx]; // subtract the oldest pixel from the sum

                    // extract the new pixel ...
                    a = srcBuffer[srcOffset + srcX] >>> 24;
                    aHistory[historyIdx] = a;   // ... and store its value into history
                    aSum += a;                  // ... and add its value to the sum

                    if (++historyIdx >= shadowSize) {
                        historyIdx -= shadowSize;
                    }
                }

                // blur the end of the row - no new pixels to grab
                for (int i = 0; i < shadowSize; i++) {

                    int a = hSumLookup[aSum];
                    dstBuffer[dstOffset++] = a << 24;

                    // subtract the oldest pixel from the sum ... and nothing new to add !
                    aSum -= aHistory[historyIdx];

                    if (++historyIdx >= shadowSize) {
                        historyIdx -= shadowSize;
                    }
                }
            }

            // vertical pass
            for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {

                aSum = 0;

                // first pixels are empty
                for (historyIdx = 0; historyIdx < left;) {
                    aHistory[historyIdx++] = 0;
                }

                // and then they come from the dstBuffer
                for (int y = 0; y < right; y++, bufferOffset += dstWidth) {
                    int a = dstBuffer[bufferOffset] >>> 24;         // extract alpha
                    aHistory[historyIdx++] = a;                     // store into history
                    aSum += a;                                      // and add to sum
                }

                bufferOffset = x;
                historyIdx = 0;

                // compute the blur average with pixels from the previous pass
                for (int y = 0; y < yStop; y++, bufferOffset += dstWidth) {

                    int a = vSumLookup[aSum];
                    dstBuffer[bufferOffset] = a << 24 | shadowRgb;  // store alpha value + shadow color

                    aSum -= aHistory[historyIdx];   // subtract the oldest pixel from the sum

                    a = dstBuffer[bufferOffset + lastPixelOffset] >>> 24;   // extract the new pixel ...
                    aHistory[historyIdx] = a;                               // ... and store its value into history
                    aSum += a;                                              // ... and add its value to the sum

                    if (++historyIdx >= shadowSize) {
                        historyIdx -= shadowSize;
                    }
                }

                // blur the end of the column - no pixels to grab anymore
                for (int y = yStop; y < dstHeight; y++, bufferOffset += dstWidth) {

                    int a = vSumLookup[aSum];
                    dstBuffer[bufferOffset] = a << 24 | shadowRgb;

                    aSum -= aHistory[historyIdx];   // subtract the oldest pixel from the sum

                    if (++historyIdx >= shadowSize) {
                        historyIdx -= shadowSize;
                    }
                }
            }

            Images.setPixels(dst, 0, 0, dstWidth, dstHeight, dstBuffer);
            return dst;
        }
    }
}
