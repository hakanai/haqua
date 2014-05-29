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

import com.apple.laf.AquaPanelUI;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import java.awt.Graphics;

/**
 * @author trejkaz
 */
public class HaquaPanelUI extends AquaPanelUI {
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaPanelUI();
    }

    protected void installDefaults(JPanel panel) {
        super.installDefaults(panel);

        // Despite having set the default for Panel.opaque to false, Aqua seems to be ignoring the value.
        LookAndFeel.installProperty(panel, "opaque", UIManager.get("Panel.opaque"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }
}
