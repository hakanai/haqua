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

package org.trypticon.haqua.demo;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author trejkaz
 */
public class OtherButtonDemo implements Demo {
    @Override
    public String getName() {
        return "Other Buttons";
    }

    @Override
    public JPanel createPanel() {
        String[] sizeVariants = { null, "small", "mini" };

        JComponent[][] grid = new JComponent[3][sizeVariants.length + 1];
        grid[0] = new JComponent[] {
                null,
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature"),
        };

        grid[1][0] = new JLabel("JRadioButton");
        grid[2][0] = new JLabel("JCheckBox");

        for (int i = 0; i < sizeVariants.length; i++)
        {
            String sizeVariant = sizeVariants[i];

            JRadioButton radioButton = new JRadioButton("Button");
            radioButton.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[1][i + 1] = radioButton;

            JCheckBox checkBox = new JCheckBox("Check");
            checkBox.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[2][i + 1] = checkBox;
        }

        return ContainerUtils.createGridPanel(grid);
    }
}
