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

import com.apple.laf.AquaToolBarUI;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author trejkaz
 */
public class HaquaToolBarUI extends AquaToolBarUI {
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaToolBarUI();
    }

    private Handler handler;

    @Override
    protected void installListeners() {
        super.installListeners();

        if (handler ==  null) {
            handler = new Handler();
        }

        toolBar.addMouseListener(handler);
        toolBar.addMouseMotionListener(handler);
    }

    @Override
    protected void uninstallListeners() {
        toolBar.removeMouseListener(handler);
        toolBar.removeMouseMotionListener(handler);
    }

    private class Handler extends MouseAdapter {
        private boolean dragging;
        private int dX;
        private int dY;

        @Override
        public void mousePressed(MouseEvent event) {
            if (toolBar.isFloatable()) {
                return;
            }

            Window window = SwingUtilities.getWindowAncestor(toolBar);
            if (window instanceof RootPaneContainer &&
                    Boolean.TRUE.equals(((RootPaneContainer) window).getRootPane().getClientProperty("apple.awt.brushMetalLook"))) {

                // There is a "apple.awt.draggableWindowBackground" client property which does something like this,
                // but despite documentation saying that clicking on heavyweight components will not drag the window,
                // even clicking on heavyweight components like Panel will drag the window.

                Point clickPoint = new Point(event.getPoint());
                SwingUtilities.convertPointToScreen(clickPoint, toolBar);
                dX = clickPoint.x - window.getX();
                dY = clickPoint.y - window.getY();
                dragging = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            dragging = false;
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            if (dragging) {
                Window window = SwingUtilities.getWindowAncestor(toolBar);
                if (window != null) { // which it should never be
                    Point dragPoint = new Point(event.getPoint());
                    SwingUtilities.convertPointToScreen(dragPoint, toolBar);
                    window.setLocation(dragPoint.x - dX, dragPoint.y - dY);
                }
            }
        }
    }
}
