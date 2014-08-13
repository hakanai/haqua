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

import com.apple.laf.AquaScrollPaneUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author trejkaz
 */
public class HaquaScrollPaneUI extends AquaScrollPaneUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaScrollPaneUI();
    }

    @Override
    protected void installDefaults(JScrollPane scrollPane) {
        super.installDefaults(scrollPane);

        // Opacity on JScrollPane has to match JViewport.
        LookAndFeel.installProperty(scrollPane, "opaque", UIManager.get("Viewport.opaque"));
    }

    @Override
    protected MouseWheelListener createMouseWheelListener() {
        return new FixedXYMouseWheelHandler();
    }

    protected class FixedXYMouseWheelHandler extends XYMouseWheelHandler {
        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {

            // "shift down" is the condition XYMouseWheelHandler is looking for as well.
            // Somewhere on the native side, Apple must have translated actual horizontal scroll events into
            // shift-down vertical scrolling for Swing, which doesn't technically support horizontal scrolling.
            if (event.isShiftDown() &&
                    !scrollpane.getComponentOrientation().isLeftToRight()) {

                // AquaScrollPaneUI doesn't reverse the scroll direction when operating in right-to-left.
                event = new MouseWheelEvent(
                        event.getComponent(), event.getID(), event.getWhen(), event.getModifiers(),
                        event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(),
                        event.getClickCount(), event.isPopupTrigger(),
                        event.getScrollType(), -event.getScrollAmount(),
                        -event.getWheelRotation(), -event.getPreciseWheelRotation());
            }

            super.mouseWheelMoved(event);
        }
    }

}
