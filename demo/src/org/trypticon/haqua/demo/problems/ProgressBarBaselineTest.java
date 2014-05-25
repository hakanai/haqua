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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;

public class ProgressBarBaselineTest implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ProgressBarBaselineTest());
    }

    @Override
    public void run() {
        JLabel label = new JLabel("Label");

        JProgressBar progressBar = new JProgressBar();
        progressBar.setString("Progress Bar");
        progressBar.setStringPainted(true);

        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        layout.setAlignOnBaseline(true);
        BaselinePaintingPanel panel = new BaselinePaintingPanel(layout);
        panel.add(label);
        panel.add(progressBar);

        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setLayout(layout);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class BaselinePaintingPanel extends JPanel {
        public BaselinePaintingPanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            g.setColor(Color.RED);
            for (Component component : getComponents()) {
                int x = component.getX();
                int y = component.getY();
                int width = component.getWidth();
                int baseline = component.getBaseline(width, component.getHeight());
                g.drawLine(x, y + baseline, x + width, y + baseline);
            }
        }
    }
}
