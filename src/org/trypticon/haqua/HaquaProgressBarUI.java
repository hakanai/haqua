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

import com.apple.laf.AquaProgressBarUI;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;

/**
 * @author trejkaz
 */
public class HaquaProgressBarUI extends AquaProgressBarUI {
    // Offset required to move the string to the correct position, relative to where the Aqua
    // look and feel would have put it.
    private static final int STRING_OFFSET = 1;

    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaProgressBarUI();
    }

    @Override
    public int getBaseline(JComponent c, int width, int height) {
        // Same as BasicProgressBarUI. AquaProgressBarUI doesn't override at all, making components not line up.
        super.getBaseline(c, width, height);
        if (progressBar.isStringPainted() && progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            FontMetrics metrics = progressBar.getFontMetrics(progressBar.getFont());
            Insets insets = progressBar.getInsets();
            int y = insets.top;
            height = height - insets.top - insets.bottom;
            int result = y + (height + metrics.getAscent() -
                              metrics.getLeading() -
                              metrics.getDescent()) / 2;

            // Adjust for the amount we moved the string down in getStringPlacement() and then one pixel
            // down again to account for the baseline being wrong in the first place.
            result += STRING_OFFSET - 1;
            return result;
        }
        return -1;
    }

    @Override
    protected Point getStringPlacement(Graphics g, String progressString, int x, int y, int width, int height) {
        Point location = super.getStringPlacement(g, progressString, x, y, width, height);
        location.y += STRING_OFFSET;
        return location;
    }
}
