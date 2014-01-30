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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the demo application.
 *
 * @author trejkaz
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JToggleButton activateButton = new JToggleButton("Activate Haqua");
                JPanel toolBar = new JPanel(new FlowLayout());
                final JFrame frame = new JFrame("Demo");

                activateButton.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        try {
                            if (event.getStateChange() == ItemEvent.SELECTED) {
                                UIManager.setLookAndFeel("org.trypticon.haqua.HaquaLookAndFeel");
                            } else {
                                UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
                            }
                            SwingUtilities.updateComponentTreeUI(frame);
                        } catch (Exception e) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error setting look and feel", e);
                        }
                    }
                });
                activateButton.setSelected(true);

                Demo[] demos = { new TreeDemo(), new TableDemo(), new ComboBoxDemo(), new SegmentedButtonDemo(), new ButtonDemo(), new OtherButtonDemo() };
                JTabbedPane tabbedPane = new JTabbedPane();
                for (Demo demo : demos) {
                    tabbedPane.addTab(demo.getName(), demo.createPanel());
                }

                //TODO: Get the unified look on this toolbar and the frame just to show we can do it.
                toolBar.add(activateButton);

                frame.setLayout(new BorderLayout());
                frame.add(toolBar, BorderLayout.PAGE_START);
                frame.add(tabbedPane, BorderLayout.CENTER);
                frame.pack();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
