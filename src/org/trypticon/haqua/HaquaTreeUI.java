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

import com.apple.laf.AquaTreeUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author trejkaz
 */
public class HaquaTreeUI extends AquaTreeUI {
    private Icon selectedCollapsedIcon;
    private Icon selectedExpandedIcon;
    private Icon pressedSelectedCollapsedIcon;
    private Icon pressedSelectedExpandedIcon;
    private int selectionBackgroundForIcons;

    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaTreeUI();
    }

    private void lazyInitialiseIcons() {
        // We keep track of which colour the selection background was set for so that we can generate
        // new icons if the user changes their theme while we're running.
        Color selectionForeground = UIManager.getColor("Tree.selectionForeground");
        Color selectionBackground = UIManager.getColor("Tree.selectionBackground");
        if (selectedCollapsedIcon == null || selectionBackgroundForIcons != selectionBackground.getRGB()) {
            selectionBackgroundForIcons = selectionBackground.getRGB();

            Icon normalCollapsedIcon = UIManager.getIcon(
                    tree.getComponentOrientation().isLeftToRight() ?
                    "Tree.collapsedIcon" : "Tree.rightToLeftCollapsedIcon");
            Icon normalExpandedIcon = UIManager.getIcon("Tree.expandedIcon");

            selectedExpandedIcon = createAlternateColourVersion(normalExpandedIcon, selectionForeground);
            selectedCollapsedIcon = createAlternateColourVersion(normalCollapsedIcon, selectionForeground);

            // Colour of the disclosure triangle when you are pressing it is subtly different.
            Color pressedDisclosureTriangleForeground = new Color(
                    derivePressedColourComponent(selectionBackground.getRed()),
                    derivePressedColourComponent(selectionBackground.getGreen()),
                    derivePressedColourComponent(selectionBackground.getBlue()));
            pressedSelectedExpandedIcon = createAlternateColourVersion(normalExpandedIcon, pressedDisclosureTriangleForeground);
            pressedSelectedCollapsedIcon = createAlternateColourVersion(normalCollapsedIcon, pressedDisclosureTriangleForeground);
        }
    }

    private int derivePressedColourComponent(int component) {
        // Experiment shows that the slightly shaded colour you get when pressed is:
        //  X' = 153 + (255-153) * X / 255 = 164
        // There is probably a way to do this in a single shot for all three components...
        return (int) Math.floor(153 + (255 - 153) * (double) component / 255);
    }

    @NotNull
    private Icon createAlternateColourVersion(@NotNull Icon icon, @NotNull final Color colour) {
        class RecolourFilter extends PerPixelFilter {
            @Override
            protected void manipulatePixels(@NotNull int[] pixels) {
                int rgb = colour.getRGB();
                for (int i = 0; i < pixels.length; i++) {
                    // Leave the alpha alone, change all RGB values to the target.
                    pixels[i] = (pixels[i] & 0xFF000000) | (rgb & 0xFFFFFF);
                }
            }
        }

        // Same image type used by AquaIcon itself.
        final BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = image.createGraphics();
        try {
            icon.paintIcon(tree, graphics, 0, 0);
        } finally {
            graphics.dispose();
        }

        new RecolourFilter().filter(image, image);
        return new ImageIcon(image);
    }

    @Override
    protected void paintRow(@NotNull Graphics g, @NotNull Rectangle clipBounds, Insets insets, @NotNull Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        // Paint the full row in the appropriate background colour, not just the node.
        if (tree.isPathSelected(path)) {
            Color selectionBackground = UIManager.getColor(
                    FocusUtils.isInActiveWindow(tree) ? "Tree.selectionBackground" : "Tree.selectionInactiveBackground");
            g.setColor(selectionBackground);
            // A purist might say this should be a round-rect with arc diameter equal to the row height
            // and flat edges above/below if the row in that direction is also selected.
            g.fillRect(clipBounds.x, bounds.y, clipBounds.width, bounds.height);
        }

        // This was already called by DefaultTreeUI, but before calling paintRow(), so we just painted over it.
        // I guess ask the Swing developers what they were smoking when they decided to paint them that way around.
        paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);

        // This will then paint the node itself.
        super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
    }

    @NotNull
    @Override
    protected TreeCellRenderer createDefaultCellRenderer() {
        // Custom renderer fixes the colours of the tree node itself when the window is inactive.
        return new HaquaTreeCellRenderer();
    }

    @Override
    public Rectangle getPathBounds(@NotNull JTree tree, TreePath path) {
        Rectangle bounds = super.getPathBounds(tree, path);
        if (bounds != null) {
            Insets insets = tree.getInsets();
            if (tree.getComponentOrientation().isLeftToRight()) {
                // Expand the path bounds to cover everything to the right of the label.
                // The main effect of this is that clicking to the right will still select the row.
                bounds.width = tree.getWidth() - bounds.x - insets.right;
            } else {
                // Expand the path bounds to cover everything to the left of the label.
                bounds.width += bounds.x - insets.left;
                bounds.x = insets.left;
            }
        }
        return bounds;
    }

    @Override
    public TreePath getClosestPathForLocation(JTree treeLocal, int x, int y) {
        return super.getClosestPathForLocation(treeLocal, x, y);
    }

    @Override
    protected void handleExpandControlClick(TreePath path, final int mouseX, final int mouseY) {
        if (tree.isPathSelected(path)) {
            // Trick the handler the superclass will create into storing the selection background colour
            // into a field it uses when it clears the background before drawing the icon.
            Color oldBackground = tree.getBackground();
            boolean oldIgnoreRepaint = tree.getIgnoreRepaint();
            try {
                tree.setIgnoreRepaint(true);
                tree.setBackground(UIManager.getColor("Tree.selectionBackground"));
                super.handleExpandControlClick(path, mouseX, mouseY);
            } finally {
                tree.setBackground(oldBackground);
                tree.setIgnoreRepaint(oldIgnoreRepaint);
            }
        } else {
            super.handleExpandControlClick(path, mouseX, mouseY);
        }
    }

    @Override
    protected void paintExpandControl(final Graphics g, final Rectangle clipBounds,
                                      final Insets insets, final Rectangle bounds,
                                      final TreePath path, final int row,
                                      final boolean isExpanded, final boolean hasBeenExpanded, final boolean isLeaf) {
        if (tree.isPathSelected(path) && FocusUtils.isInActiveWindow(tree)) {

            // Trick the superclass into thinking we're using custom icons, so that it won't use its own
            // native painter which paints the wrong colour.
            // This causes us to lose the animation, but this is less bad than the arrow being the wrong colour.
            Icon oldExpandedIcon = getExpandedIcon();
            Icon oldCollapsedIcon = getCollapsedIcon();
            try {
                lazyInitialiseIcons();
                setExpandedIcon(fIsPressed ? pressedSelectedExpandedIcon : selectedExpandedIcon);
                setCollapsedIcon(fIsPressed ? pressedSelectedCollapsedIcon : selectedCollapsedIcon);
                super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
            } finally {
                setExpandedIcon(oldExpandedIcon);
                setCollapsedIcon(oldCollapsedIcon);
            }
        } else {
            super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
        }
    }
}
