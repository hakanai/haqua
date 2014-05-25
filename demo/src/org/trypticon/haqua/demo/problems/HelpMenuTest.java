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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class HelpMenuTest implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new HelpMenuTest());
    }

    @Override
    public void run() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JFrame frame = new JFrame();

        // Works:
//        JMenuBar menuBar = new JMenuBar();
//        JMenu helpMenu = new JMenu("Help");
//        menuBar.add(helpMenu);
//        frame.setJMenuBar(menuBar);

        // Doesn't work:
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Hilfe");
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // Doesn't work either (odd, because I specifically told it which one was the help menu.)
//        MenuBar menuBar = new MenuBar();
//        Menu helpMenu = new Menu("Hilfe");
//        helpMenu.add(new MenuItem("Docs"));
//        menuBar.add(helpMenu);
//        menuBar.setHelpMenu(helpMenu);
//        frame.setMenuBar(menuBar);

        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
