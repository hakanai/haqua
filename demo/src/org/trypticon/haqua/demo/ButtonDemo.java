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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author trejkaz
 */
public class ButtonDemo implements Demo {
    @Override
    public String getName() {
        return "Buttons";
    }

    @Override
    public JPanel createPanel() {
        String[] buttonTypes = { null, "square", "gradient", "bevel", "textured", "roundRect", "recessed" };
        String[] sizeVariants = { null, "small", "mini" };

        JComponent[][] grid = new JComponent[buttonTypes.length + 2][sizeVariants.length * 2 + 1];
        grid[0] = new JComponent[] {
                null,
                new JLabel("JButton"),
                new JLabel("JButton"),
                new JLabel("JButton"),
                new JLabel("JToggleButton"),
                new JLabel("JToggleButton"),
                new JLabel("JToggleButton")
        };
        grid[1] = new JComponent[] {
                null,
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature"),
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature")
        };

        for (int i = 0; i < buttonTypes.length; i++) {
            String buttonType = buttonTypes[i];
            grid[i + 2][0] = new JLabel(buttonType == null ? "(default)" : buttonType);

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < sizeVariants.length; k++) {
                    String sizeVariant = sizeVariants[k];

                    AbstractButton button = j == 1 ? new JToggleButton("Button") : new JButton("Button");
                    if (buttonType != null) {
                        button.putClientProperty("JButton.buttonType", buttonType);
                    }
                    if (sizeVariant != null) {
                        button.putClientProperty("JComponent.sizeVariant", sizeVariant);
                    }
                    grid[i + 2][sizeVariants.length * j + k + 1] = button;
                }
            }
        }

        return ContainerUtils.createGridPanel(grid);
    }

}
