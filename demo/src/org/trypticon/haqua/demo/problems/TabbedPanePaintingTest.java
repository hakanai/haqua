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

package org.trypticon.haqua.demo.problems;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TabbedPanePaintingTest implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TabbedPanePaintingTest());
    }

    @Override
    public void run() {
        // The painting on this tab will leave a 1px gap to the left of each tab.
        // You'll also notice that the contained panels will have the wrong background colour.
        JTabbedPane pane = new JTabbedPane();
        pane.add("Tab 1", new JPanel());
        pane.add("Tab 2", new JPanel());
        pane.add("Tab 3", new JPanel());
        pane.add("Tab 4", new JPanel());

        JFrame frame = new JFrame();
        frame.setContentPane(pane);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
