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

import com.apple.laf.AquaTabbedPaneContrastUI;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author trejkaz
 */
public class HaquaTabbedPaneUI extends AquaTabbedPaneContrastUI {
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaTabbedPaneUI();
    }

    @Override
    protected MouseListener createMouseListener() {
        return new CustomMouseHandler();
    }

    @Override
    protected void paintCUITab(Graphics g, int tabPlacement, Rectangle tabRect, boolean isSelected, boolean frameActive, boolean isLeftToRight, int nonRectIndex) {
        // Don't extend the leftmost tab. It should remain the same size.
        //TODO: Pressed tab still lacks the solid line to the left.
        if ((isSelected || isPressedAt(nonRectIndex)) && nonRectIndex > 0) {

            Rectangle tabRectFixed = new Rectangle(tabRect);
            tabRectFixed.x--;
            tabRectFixed.width++;

            super.paintCUITab(g, tabPlacement, tabRectFixed, isSelected, frameActive, isLeftToRight, nonRectIndex);

        } else {
            super.paintCUITab(g, tabPlacement, tabRect, isSelected, frameActive, isLeftToRight, nonRectIndex);
        }
    }

    private class CustomMouseHandler extends MouseHandler {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            repaintTabArea();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            repaintTabArea();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            repaintTabArea();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            repaintTabArea();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            repaintTabArea();
        }

        private void repaintTabArea() {
            // Repaint the whole width of the tab area. The superclass implementation is not repainting enough,
            // resulting in some of the UI not repainting when it should.
            Rectangle bounds = getTabBounds(tabPane, tabPane.getSelectedIndex());
            bounds.x = 0;
            bounds.width = tabPane.getWidth();
            tabPane.repaint(bounds);
        }
    }
}
