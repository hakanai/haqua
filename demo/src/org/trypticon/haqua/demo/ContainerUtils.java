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

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author trejkaz
 */
class ContainerUtils {
    @NotNull
    static JPanel createGridPanel(@NotNull JComponent[][] rows) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);

        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();

        horizontalGroup.addContainerGap();
        GroupLayout.Group[] horizontalGroups = new GroupLayout.Group[rows[0].length];
        for (int i = 0; i < rows[0].length; i++) {
            horizontalGroups[i] = layout.createParallelGroup(i > 0 ? GroupLayout.Alignment.CENTER : GroupLayout.Alignment.LEADING);
            horizontalGroup.addGroup(horizontalGroups[i]);
        }
        horizontalGroup.addContainerGap();
        verticalGroup.addContainerGap();

        for (JComponent[] row : rows) {
            GroupLayout.Group rowGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            for (int i = 0; i < row.length; i++) {
                JComponent component = row[i];
                if (component != null) {
                    horizontalGroups[i].addComponent(component, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
                    rowGroup.addComponent(component);
                }
            }
            verticalGroup.addGroup(rowGroup);
        }

        verticalGroup.addContainerGap();
        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);

        return panel;
    }
}
