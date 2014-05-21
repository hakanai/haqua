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

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author trejkaz
 */
public class ProgressBarDemo implements Demo {
    @Override
    public String getName() {
        return "Progress Bars";
    }

    @Override
    public JPanel createPanel() {
        String[] sizeVariants = { null, "small", "mini" };

        JComponent[][] grid = new JComponent[4][sizeVariants.length + 1];

        grid[0] = new JComponent[] {
                null,
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature"),
        };

        grid[1][0] = new JLabel("Normal");
        grid[2][0] = new JLabel("Indeterminate");
        grid[3][0] = new JLabel("Circular");

        for (int i = 0; i < sizeVariants.length; i++) {
            String sizeVariant = sizeVariants[i];

            JProgressBar normalProgressBar = new JProgressBar(new DefaultBoundedRangeModel(42, 0, 0, 100));
            normalProgressBar.setStringPainted(true);
            normalProgressBar.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[1][i + 1] = normalProgressBar;

            JProgressBar indeterminateProgressBar = new JProgressBar();
            indeterminateProgressBar.setIndeterminate(true);
            indeterminateProgressBar.setString("Reticulating splines");
            indeterminateProgressBar.setStringPainted(true);
            indeterminateProgressBar.putClientProperty("JComponent.sizeVariant", sizeVariant);
            grid[2][i + 1] = indeterminateProgressBar;

            if (i == 0) { // no variants for this one.
                JProgressBar circularProgressBar = new JProgressBar();
                circularProgressBar.setIndeterminate(true);
                circularProgressBar.putClientProperty("JProgressBar.style", "circular");
                circularProgressBar.putClientProperty("JComponent.sizeVariant", sizeVariant);
                grid[3][i + 1] = circularProgressBar;
            }
        }

        return ContainerUtils.createGridPanel(grid);
    }
}
