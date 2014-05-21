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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * @author trejkaz
 */
public class PopupMenuDemo implements Demo {
    @Override
    public String getName() {
        return "Popup Menus";
    }

    @Override
    public JPanel createPanel() {
        JComponent[][] grid = new JComponent[3][2];

        grid[0][0] = new JLabel("Combo box");
        grid[1][0] = new JLabel("Button with popup");
        grid[2][0] = new JLabel("Button with tool tip");

        JComboBox<String> normalComboBox = new JComboBox<>(new String[] { "One", "Two", "Three" });
        grid[0][1] = normalComboBox;

        JButton buttonWithPopup = new JButton("Act");
        JPopupMenu popupMenu = new JPopupMenu("Popup");
        popupMenu.add(new JMenuItem("One"));
        popupMenu.add(new JMenuItem("Two"));
        popupMenu.add(new JMenuItem("Three"));
        JMenu subMenu = new JMenu("Sub-menu");
        subMenu.add(new JMenuItem("One"));
        subMenu.add(new JMenuItem("Two"));
        subMenu.add(new JMenuItem("Three"));
        popupMenu.add(subMenu);
        buttonWithPopup.setComponentPopupMenu(popupMenu);
        grid[1][1] = buttonWithPopup;

        JButton buttonWithToolTip = new JButton("Act");
        buttonWithToolTip.setToolTipText("Tool Tip");
        grid[2][1] = buttonWithToolTip;

        return ContainerUtils.createGridPanel(grid);
    }
}
