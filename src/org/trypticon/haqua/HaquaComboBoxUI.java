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

import com.apple.laf.AquaComboBoxUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * @author trejkaz
 */
public class HaquaComboBoxUI extends AquaComboBoxUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaComboBoxUI();
    }

    @Override
    protected ComboPopup createPopup() {
        ComboPopup popup = super.createPopup();

        // For some reason, the JList in the combo popup comes up with a different background colour
        // to the rest of the menu.
        popup.getList().setBackground( ((Component) popup).getBackground() );

        return popup;
    }

    @NotNull
    @Override
    protected LayoutManager createLayoutManager() {
        return new HaquaComboBoxLayoutManager(super.createLayoutManager());
    }

    // Returns the same thing as the similarly-named method in the superclass. We're just making it visible.
    protected static boolean isPopDown(final JComboBox c) {
        return AquaComboBoxUI.isPopdown(c);
    }

    @Override
    protected Rectangle rectangleForCurrentValue() {
        // Copy of what's in BasicComboBoxUI, minus the left-to-right check.
        // Reasoning is that Aqua combo boxes always have the arrow on the right,
        // so it is incorrect to move the text field to the right to make way for an arrow on the left.
        int width = comboBox.getWidth();
        int height = comboBox.getHeight();
        Insets insets = getInsets();
        int buttonSize = height - (insets.top + insets.bottom);
        if (arrowButton != null) {
            buttonSize = arrowButton.getWidth();
        }
        return new Rectangle(insets.left, insets.top,
                             width - (insets.left + insets.right + buttonSize),
                             height - (insets.top + insets.bottom));
    }

    @Override
    protected ListCellRenderer createRenderer() {
        return new HaquaComboBoxRenderer(comboBox);
    }

    private class HaquaComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
        private final LayoutManager delegate;

        private HaquaComboBoxLayoutManager(LayoutManager delegate) {
            this.delegate = delegate;
        }

        @Override
        public void layoutContainer(final Container parent) {
            delegate.layoutContainer(parent);

            Rectangle bounds = new Rectangle();

            if (editor != null && comboBox.isEditable()) {
                bounds = editor.getBounds(bounds);
                bounds.y ++;
                editor.setBounds(bounds);
            }

            if (arrowButton != null && comboBox.isEditable()) {
                // Arrow button is one pixel too far to the left for editable combo boxes.
                bounds = arrowButton.getBounds(bounds);
                bounds.x ++;
                bounds.y ++;
                arrowButton.setBounds(bounds);
            }
        }
    }
}
