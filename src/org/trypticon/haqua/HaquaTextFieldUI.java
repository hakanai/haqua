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

import com.apple.laf.AquaTextFieldUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * @author trejkaz
 */
public class HaquaTextFieldUI extends AquaTextFieldUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaTextFieldUI();
    }

    @Override
    public Dimension getPreferredSize(@NotNull JComponent c) {
        Dimension size = super.getPreferredSize(c);
        if (c.getParent() instanceof JComboBox) {
            // Height of a normal text field is one pixel too tall for a combo box.
            size.height--;
        }
        return size;
    }
}
