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

import com.apple.laf.AquaTableUI;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author trejkaz
 */
public class HaquaTableUI extends AquaTableUI {
    private Handler handler;

    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaTableUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        // Default margin of 1 artificially separates cells when usually they are painted in contiguous blocks.
        table.setRowMargin(UIManager.getInt("Table.rowMargin"));
        table.getColumnModel().setColumnMargin(UIManager.getInt("Table.columnMargin"));

        // Tell Swing to fill the whole viewport so that we can fill in the stripes all the way down.
        table.setFillsViewportHeight(UIManager.getBoolean("Table.fillsViewportHeight"));

        // Use magically switching colours to solve the issue of painting the wrong selection colour when
        // the window is not active.
        Color selectionForeground = table.getSelectionForeground();
        if (selectionForeground == null || selectionForeground instanceof UIResource) {
            table.setSelectionForeground(new InactivatableColor(
                    table,
                    UIManager.getColor("Table.selectionForeground"),
                    UIManager.getColor("Table.selectionInactiveForeground")));
        }
        Color selectionBackground = table.getSelectionBackground();
        if (selectionBackground == null || selectionBackground instanceof UIResource) {
            table.setSelectionBackground(new InactivatableColor(
                    table,
                    UIManager.getColor("Table.selectionBackground"),
                    UIManager.getColor("Table.selectionInactiveBackground")));
        }
    }

    @Override
    protected void installListeners() {
        super.installListeners();

        if (handler == null)
        {
            handler = new Handler();
        }

        table.addPropertyChangeListener("columnModel", handler);
    }

    @Override
    protected void uninstallListeners() {
        table.removePropertyChangeListener("columnModel", handler);

        super.uninstallListeners();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        // Redoing logic super.paint will do to figure out the range of rows we're painting.
        Rectangle clip = g.getClipBounds();
        Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
        int rMax = table.rowAtPoint(lowerRight);
        if (rMax == -1) {
            // Paint all the rows after the last row.
            int row = table.getRowCount() - 1;
            Rectangle rowBounds = table.getCellRect(row, -1, false);
            // Change rowBounds to be the bounds of the first unpainted row:
            row++;
            rowBounds.y += rowBounds.height;
            rowBounds.height = table.getRowHeight();

            Color background = table.getBackground();
            Color alternateBackground = background;
            if (background == null || background instanceof javax.swing.plaf.UIResource) {
                alternateBackground = UIManager.getColor("Table.alternateRowColor");
            }

            while (rowBounds.y < clip.y + clip.height) {
                g.setColor(row % 2 != 0 ? alternateBackground : background);
                g.fillRect(clip.x, rowBounds.y, clip.width, rowBounds.height);

                row++;
                rowBounds.y += rowBounds.height;
            }
        }

        super.paint(g, c);
    }

    private static class Handler implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // Typically the client code will install its own column model or it will install its own model,
            // which will create a new column model automatically. So we have to chase this and update the
            // margin every time it changes.
            // I'm not entirely satisfied with this approach. One alternative was to override
            // #paint(Graphics, JComponent) and paint the solid rows there. But I found it would leave ghost
            // painting of the selection colour, because Swing doesn't appear to repaint enough of the table
            // when the selection changes (another bug?)
            ((TableColumnModel) event.getNewValue()).setColumnMargin(UIManager.getInt("Table.columnMargin"));
        }
    }

}
