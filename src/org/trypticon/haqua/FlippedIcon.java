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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * An icon which renders another icon flipped left-to-right.
 *
 * @author trejkaz
 */
class FlippedIcon implements Icon {
    private final Icon delegate;

    FlippedIcon(Icon delegate) {
        this.delegate = delegate;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D gCopy = (Graphics2D) g.create();
        try {
            int width = getIconWidth();
            gCopy.translate(x, y);
            gCopy.transform(AffineTransform.getScaleInstance(-1, 1));
            delegate.paintIcon(c, gCopy, -width, 0);
        } finally {
            gCopy.dispose();
        }
    }

    @Override
    public int getIconWidth() {
        return delegate.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return delegate.getIconHeight();
    }
}
