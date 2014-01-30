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

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * @author trejkaz
 */
public class HaquaComboBoxUI extends AquaComboBoxUI {
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

    @Override
    protected LayoutManager createLayoutManager() {
        return new HaquaComboBoxLayoutManager(super.createLayoutManager());
    }

    // Returns the same thing as the similarly-named method in the superclass. We're just making it visible.
    protected static boolean isPopDown(final JComboBox c) {
        return AquaComboBoxUI.isPopdown(c);
    }

    private class HaquaComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
        private final LayoutManager delegate;

        private HaquaComboBoxLayoutManager(LayoutManager delegate) {
            this.delegate = delegate;
        }

        @Override
        public void layoutContainer(final Container parent) {
            delegate.layoutContainer(parent);

            if (arrowButton != null && comboBox.isEditable()) {
                // Arrow button is one pixel too far to the left for editable combo boxes.
                Rectangle bounds = arrowButton.getBounds();
                bounds.x += 1;
                arrowButton.setBounds(bounds);
            }
        }
    }
}
