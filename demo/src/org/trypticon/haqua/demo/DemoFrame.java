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

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main frame for the demo app.
 *
 * @author trejkaz
 */
public class DemoFrame extends JFrame {
    public DemoFrame() {
        super("Demo");

        JToggleButton activateButton = new JToggleButton("Activate Haqua");
        JToggleButton ltrButton = new JToggleButton("LTR");
        JToggleButton rtlButton = new JToggleButton("RTL");
        JToolBar toolBar = new JToolBar();
        final Container opaqueWrapper = new ForcedOpaqueWrapper();

        ButtonGroup orientationGroup = new ButtonGroup();
        orientationGroup.add(ltrButton);
        orientationGroup.add(rtlButton);

        activateButton.putClientProperty("JButton.buttonType", "textured");
        activateButton.putClientProperty("JButton.segmentPosition", "only");
        activateButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                try {
                    if (event.getStateChange() == ItemEvent.SELECTED) {
                        UIManager.setLookAndFeel("org.trypticon.haqua.HaquaLookAndFeel");
                    } else {
                        UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
                    }
                    SwingUtilities.updateComponentTreeUI(DemoFrame.this);
                } catch (Exception e) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error setting look and feel", e);
                }
            }
        });

        ltrButton.putClientProperty("JButton.buttonType", "textured");
        ltrButton.putClientProperty("JButton.segmentPosition", "first");
        ltrButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    repaint();
                }
            }
        });

        rtlButton.putClientProperty("JButton.buttonType", "textured");
        rtlButton.putClientProperty("JButton.segmentPosition", "last");
        rtlButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    repaint();
                }
            }
        });

        Demo[] demos = {
//                new TreeDemo(), new TableDemo(), new TreeTableDemo(),
//                new ComboBoxDemo(), new PopupMenuDemo(),new ProgressBarDemo(),
//                new SegmentedButtonDemo(), new ButtonDemo(), new OtherButtonDemo()
        };
        JTabbedPane tabbedPane = new JTabbedPane();
        for (Demo demo : demos) {
            tabbedPane.addTab(demo.getName(), demo.createPanel());
        }

        toolBar.setFloatable(false);

        GroupLayout toolBarLayout = new GroupLayout(toolBar);
        toolBar.setLayout(toolBarLayout);

        toolBarLayout.setHorizontalGroup(toolBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(activateButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ltrButton)
                .addComponent(rtlButton));

        toolBarLayout.setVerticalGroup(toolBarLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(activateButton)
                .addComponent(ltrButton)
                .addComponent(rtlButton));

        opaqueWrapper.setLayout(new BorderLayout());
        opaqueWrapper.add(tabbedPane, BorderLayout.CENTER);

        // Even though this says "brushed metal", it will use the modern unified style these days.
        getRootPane().putClientProperty("apple.awt.brushMetalLook", true);

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.PAGE_START);
        add(opaqueWrapper, BorderLayout.CENTER);
        pack();

        activateButton.setSelected(true);
        ltrButton.setSelected(true);
    }

    private static class ForcedOpaqueWrapper extends JPanel {
        private ForcedOpaqueWrapper() {
            setOpaque(true);
        }

        @Override
        public void updateUI() {
            setBackground(null);

            super.updateUI();

            // Deliberately sets the alpha to opaque by not using the (int, boolean) version of the method.
            setBackground(new Color(getBackground().getRGB()));
        }
    }
}
