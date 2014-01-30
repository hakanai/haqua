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

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author trejkaz
 */
public class ComboBoxDemo implements Demo {
    @Override
    public String getName() {
        return "Combo Boxes";
    }

    @Override
    public JPanel createPanel() {
        String[] sizeVariants = { null, "small", "mini" };

        JComponent[][] grid = new JComponent[5][sizeVariants.length + 1];

        grid[0] = new JComponent[] {
                null,
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature"),
        };

        grid[1][0] = new JLabel("Normal");
        grid[2][0] = new JLabel("Square");
        grid[3][0] = new JLabel("Pop-down");
        grid[4][0] = new JLabel("Editable");

        for (int i = 0; i < sizeVariants.length; i++)
        {
            String sizeVariant = sizeVariants[i];

            JComboBox<String> normalComboBox = new JComboBox<>(new String[] { "One", "Two", "Three" });
            normalComboBox.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[1][i + 1] = normalComboBox;

            JComboBox<String> squareComboBox = new JComboBox<>(new String[] { "One", "Two", "Three" });
            squareComboBox.putClientProperty("JComponent.sizeVariant", sizeVariant);
            squareComboBox.putClientProperty("JComboBox.isSquare", true);
            grid[2][i + 1] = squareComboBox;

            JComboBox<String> popDownComboBox = new JComboBox<>(new String[] { "One", "Two", "Three" });
            popDownComboBox.putClientProperty("JComponent.sizeVariant", sizeVariant);
            popDownComboBox.putClientProperty("JComboBox.isPopDown", true);
            grid[3][i + 1] = popDownComboBox;

            JComboBox<String> editableComboBox = new JComboBox<>(new String[] { "One", "Two", "Three" });
            editableComboBox.setEditable(true);
            popDownComboBox.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[4][i + 1] = editableComboBox;
        }

        return ContainerUtils.createGridPanel(grid);
    }
}
