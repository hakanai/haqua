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

import com.apple.laf.AquaMenuPainter;
import sun.swing.SwingUtilities2;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

/**
 * Delegating renderer for combo box popup list entries.
 *
 * @author trejkaz
 */
public class HaquaComboBoxRenderer extends JLabel implements ListCellRenderer, UIResource {

    //TODO: Awkward situation here where I had to clone the whole class. Is there a better way?
    // The fixes are marked with //HAQUA comments below.

    final JComboBox fComboBox;
    boolean fSelected;
    boolean fChecked;
    boolean fInList;
    boolean fEditable;
    boolean fDrawCheckedItem = true;

    // Works around not having access to AquaMenuPainter.instance()
    private AquaMenuPainter myMenuPainter = new AquaMenuPainter();

    // Provides space for a checkbox, and is translucent
    public HaquaComboBoxRenderer(final JComboBox comboBox) {
        super();
        fComboBox = comboBox;
    }

    // Don't include checkIcon space, because this is also used for button size calculations
    // - the popup-size calc will get checkIcon space from getInsets
    public Dimension getPreferredSize() {
        // From BasicComboBoxRenderer - trick to avoid zero-height items
        final Dimension size;

        final String text = getText();
        if ((text == null) || ("".equals(text))) {
            setText(" ");
            size = super.getPreferredSize();
            setText("");
        } else {
            size = super.getPreferredSize();
        }
        return size;
    }

    // Don't paint the border here, it gets painted by the UI
    protected void paintBorder(final Graphics g) {

    }

    public int getBaseline(int width, int height) {
        return super.getBaseline(width, height) - 1;
    }

    // Really means is the one with the mouse over it
    public Component getListCellRendererComponent(final JList list, final Object value, int index, final boolean isSelected, final boolean cellHasFocus) {
        fInList = (index >= 0); // When the button wants the item painted, it passes in -1
        fSelected = isSelected;
        if (index < 0) {
            index = fComboBox.getSelectedIndex();
        }

        // changed this to not ask for selected index but directly compare the current item and selected item
        // different from basic because basic has no concept of checked, just has the last one selected,
        // and the user changes selection. We have selection and a check mark.
        // we used to call fComboBox.getSelectedIndex which ends up being a very bad call for large checkboxes
        // it does a linear compare of every object in the checkbox until it finds the selected one, so if
        // we have a 5000 element list we will 5000 * (selected index) .equals() of objects.
        // See Radar #3141307

        // Fix for Radar # 3204287 where we ask for an item at a negative index!
        if (index >= 0) {
            final Object item = fComboBox.getItemAt(index);
            fChecked = fInList && item != null && item.equals(fComboBox.getSelectedItem());
        } else {
            fChecked = false;
        }

        fEditable = fComboBox.isEditable();
        if (isSelected) {
            if (fEditable) {
                setBackground(UIManager.getColor("List.selectionBackground"));
                setForeground(UIManager.getColor("List.selectionForeground"));
            } else {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
        } else {
            if (fEditable) {
                setBackground(UIManager.getColor("List.background"));
                setForeground(UIManager.getColor("List.foreground"));
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
        }

        setFont(list.getFont());

        if (value instanceof Icon) {
            setIcon((Icon)value);
        } else {
            setText((value == null) ? " " : value.toString());
        }

        //HAQUA: AquaComboBoxRenderer outright forgets to set this at all.
        applyComponentOrientation(list.getComponentOrientation());

        return this;
    }

    public Insets getInsets(Insets insets) {
        if (insets == null) insets = new Insets(0, 0, 0, 0);
        insets.top = 1;
        insets.bottom = 1;

        //HAQUA: If right-to-left, make space for the check mark on the right instead of the left.
        // BTW, the 7s here are probably the width of the check mark (it is actually that wide.)
        if (getComponentOrientation().isLeftToRight()) {
            insets.right = 5;
            insets.left = (fInList && !fEditable ? 16 + 7 : 5);
        } else {
            insets.left = 5;
            insets.right = (fInList && !fEditable ? 16 + 7 : 5);
        }

        return insets;
    }

    protected void setDrawCheckedItem(final boolean drawCheckedItem) {
        this.fDrawCheckedItem = drawCheckedItem;
    }

    // Paint this component, and a checkbox if it's the selected item and not in the button
    protected void paintComponent(final Graphics g) {
        if (fInList) {
            if (fSelected && !fEditable) {
                myMenuPainter.paintSelectedMenuItemBackground(g, getWidth(), getHeight());
            } else {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            if (fChecked && !fEditable && fDrawCheckedItem) {
                final int y = getHeight() - 4;
                g.setColor(getForeground());

                //HAQUA: Draw the check mark in the right location.
                if (getComponentOrientation().isLeftToRight()) {
                    SwingUtilities2.drawString(fComboBox, g, "\u2713", 6, y);
                } else {
                    int stringWidth = g.getFontMetrics().stringWidth("\u2713");
                    SwingUtilities2.drawString(fComboBox, g, "\u2713", getWidth() - 6 - stringWidth, y);
                }
            }
        }
        super.paintComponent(g);
    }
}
