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

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SegmentedButtonBorderLossTest implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SegmentedButtonBorderLossTest());
    }

    @Override
    public void run() {
        JLabel normalButtonLabel = new JLabel("Normal buttons:");
        JLabel toggleButtonLabel = new JLabel("Toggle buttons:");

        JButton normalButtonFirst = new JButton("First");
        normalButtonFirst.putClientProperty("JButton.buttonType", "segmentedTextured");
        normalButtonFirst.putClientProperty("JButton.segmentPosition", "first");

        JButton normalButtonLast = new JButton("Last");
        normalButtonLast.putClientProperty("JButton.buttonType", "segmentedTextured");
        normalButtonLast.putClientProperty("JButton.segmentPosition", "last");

        JButton toggleButtonFirst = new JButton("First");
        toggleButtonFirst.putClientProperty("JButton.buttonType", "segmentedTextured");
        toggleButtonFirst.putClientProperty("JButton.segmentPosition", "first");

        JButton toggleButtonLast = new JButton("Last");
        toggleButtonLast.putClientProperty("JButton.buttonType", "segmentedTextured");
        toggleButtonLast.putClientProperty("JButton.segmentPosition", "last");

        JPanel content = new JPanel();
        GroupLayout layout = new GroupLayout(content);
        layout.setAutoCreateContainerGaps(true);
        content.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(normalButtonLabel)
                .addComponent(toggleButtonLabel))
            .addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addComponent(normalButtonFirst)
                    .addComponent(normalButtonLast))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(toggleButtonFirst)
                    .addComponent(toggleButtonLast))));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(normalButtonLabel)
                        .addComponent(normalButtonFirst)
                        .addComponent(normalButtonLast))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(toggleButtonLabel)
                        .addComponent(toggleButtonFirst)
                        .addComponent(toggleButtonLast)));

        final JFrame frame = new JFrame();
        frame.setContentPane(content);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Giving the user 5 seconds to see what it *should* look like, then switch to Metal:
        Timer timer1 = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // And now we switch back and forth and watch what happens to our button styles.
                try {
                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                    SwingUtilities.updateComponentTreeUI(frame);
                    frame.pack();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timer1.setRepeats(false);
        timer1.start();

        // Then a second later, switch back to Aqua:
        Timer timer2 = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // And now we switch back and forth and watch what happens to our button styles.
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(frame);
                    frame.pack();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timer2.setRepeats(false);
        timer2.start();
    }
}
