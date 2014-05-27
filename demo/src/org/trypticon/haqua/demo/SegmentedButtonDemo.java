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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * @author trejkaz
 */
public class SegmentedButtonDemo implements Demo {
    @NotNull
    @Override
    public String getName() {
        return "Segmented Buttons";
    }

    @NotNull
    @Override
    public JPanel createPanel() {
        String[] buttonTypes = { "segmented", "segmentedRoundRect", "segmentedCapsule", "segmentedTextured", "segmentedGradient" };
        String[] sizeVariants = { null, "small", "mini" };
        String[] segmentPositions = { "first", "middle", "last" }; // "only" exists too, but we won't use it.

        JComponent[][] grid = new JComponent[buttonTypes.length + 1][sizeVariants.length + 1];
        grid[1] = new JComponent[] {
                null,
                new JLabel("(default)"),
                new JLabel("small"),
                new JLabel("miniature"),
        };

        for (int i = 0; i < buttonTypes.length; i++) {
            String buttonType = buttonTypes[i];
            grid[i + 1][0] = new JLabel(buttonType == null ? "(default)" : buttonType);

            for (int j = 0; j < sizeVariants.length; j++) {
                String sizeVariant = sizeVariants[j];

                JPanel subPanel = new SegmentedButtonPanel();

                for (String segmentPosition : segmentPositions) {
                    AbstractButton button = new JButton(segmentPosition);
                    if (buttonType != null) {
                        button.putClientProperty("JButton.buttonType", buttonType);
                    }
                    if (sizeVariant != null) {
                        button.putClientProperty("JComponent.sizeVariant", sizeVariant);
                    }
                    button.putClientProperty("JButton.segmentPosition", segmentPosition);
                    subPanel.add(button);
                }

                grid[i + 1][j + 1] = subPanel;
            }
        }

        return ContainerUtils.createGridPanel(grid);
    }

    /**
     * Panel class for grouping segmented buttons.
     */
    private static class SegmentedButtonPanel extends JPanel {
        private SegmentedButtonPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        }

        @Override
        public int getBaseline(int width, int height) {
            if (getComponentCount() >= 1) {
                return getComponent(0).getBaseline(width, height);
            } else {
                return super.getBaseline(width, height);
            }
        }
    }
}
