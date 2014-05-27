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

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Arrays;

/**
 * @author trejkaz
 */
public class TreeTableDemo implements Demo {
    @NotNull
    @Override
    public String getName() {
        return "Tree Tables";
    }

    @NotNull
    @Override
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        TreeTableModel treeTableModel = new DummyTreeTableModel();
        JXTreeTable treeTable = new BetterTreeTable(treeTableModel);
        JScrollPane treeTableScroll = new JScrollPane(treeTable);

        panel.add(treeTableScroll, BorderLayout.CENTER);
        return panel;
    }

    private static class BetterTreeTable extends JXTreeTable {
        @Nullable
        private Highlighter oddRowHighlighter;

        public BetterTreeTable(TreeTableModel treeModel) {
            super(treeModel);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        @Override
        public void updateUI() {
            if (oddRowHighlighter != null) {
                removeHighlighter(oddRowHighlighter);
                oddRowHighlighter = null;
            }

            super.updateUI();

            // JTable does this striping automatically but JXTable's renderer seems to break it, so JXTreeTable
            // inherits this broken behaviour.
            if (UIManager.get("Table.alternateRowColor") != null) {
                oddRowHighlighter =
                        HighlighterFactory.createSimpleStriping(UIManager.getColor("Table.alternateRowColor"));
                addHighlighter(oddRowHighlighter);
            }
        }
    }

    private static class DummyTreeTableNode extends DefaultMutableTreeTableNode {
        private final Object[] values;

        private DummyTreeTableNode(String name) {
            super(name);
            values = new Object[5];
            values[0] = name;
        }

        private DummyTreeTableNode(Object... values) {
            super(values[0]);
            this.values = values;
        }

        @Override
        public Object getValueAt(int column) {
            return values[column];
        }
    }

    private static class DummyTreeTableModel extends DefaultTreeTableModel {
        @NotNull
        private static DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
        static {
            DefaultMutableTreeTableNode blue = new DefaultMutableTreeTableNode("Blue");
            blue.add(new DummyTreeTableNode("Orionis C",          33000,  30000.0,    18.0,   5.90));
            rootNode.add(blue);

            DefaultMutableTreeTableNode bluish = new DefaultMutableTreeTableNode("Bluish");
            bluish.add(new DummyTreeTableNode("Becrux",             30000,  16000.0,    16.0,   5.70));
            bluish.add(new DummyTreeTableNode("Spica",              22000,  8300.0,     10.5,   5.10));
            bluish.add(new DummyTreeTableNode("Achernar",           15000,  750.0,      5.40,   3.70));
            bluish.add(new DummyTreeTableNode("Rigel",              12500,  130.0,      3.50,   2.70));
            rootNode.add(bluish);

            DefaultMutableTreeTableNode blueWhite = new DefaultMutableTreeTableNode("Blue-White");
            blueWhite.add(new DummyTreeTableNode("Sirius A",           9500,   63.0,       2.60,   2.30));
            blueWhite.add(new DummyTreeTableNode("Fomalhaut",          9000,   40.0,       2.20,   2.00));
            blueWhite.add(new DummyTreeTableNode("Altair",             8700,   24.0,       1.90,   1.80));
            rootNode.add(blueWhite);

            DefaultMutableTreeTableNode white = new DefaultMutableTreeTableNode("White");
            white.add(new DummyTreeTableNode("Polaris A",          7400,   9.0,        1.60,   1.50));
            white.add(new DummyTreeTableNode("Eta Scorpii",        7100,   6.3,        1.50,   1.30));
            white.add(new DummyTreeTableNode("Procyon A",          6400,   4.0,        1.35,   1.20));
            rootNode.add(white);

            DefaultMutableTreeTableNode yellowWhite = new DefaultMutableTreeTableNode("Yellow-White");
            yellowWhite.add(new DummyTreeTableNode("Alpha Centauri A",   5900,   1.45,       1.08,   1.05));
            yellowWhite.add(new DummyTreeTableNode("The Sun",            5800,   100.0,      1.00,   1.00));
            yellowWhite.add(new DummyTreeTableNode("Mu Cassiopeiae",     5600,   0.70,       0.95,   0.91));
            yellowWhite.add(new DummyTreeTableNode("Tau Ceti",           5300,   0.44,       0.85,   0.87));
            rootNode.add(yellowWhite);

            DefaultMutableTreeTableNode orange = new DefaultMutableTreeTableNode("Orange");
            orange.add(new DummyTreeTableNode("Pollux",             5100,   0.36,       0.83,   0.83));
            orange.add(new DummyTreeTableNode("Epsilon Eridani",    4830,   0.28,       0.78,   0.79));
            orange.add(new DummyTreeTableNode("Alpha Centauri B",   4370,   0.18,       0.68,   0.74));
            rootNode.add(orange);

            DefaultMutableTreeTableNode red = new DefaultMutableTreeTableNode("Red");
            red.add(new DummyTreeTableNode("Lalande 21185",      3400,   0.03,       0.33,   0.36));
            red.add(new DummyTreeTableNode("Ross 128",           3200,   0.0005,     0.20,   0.21));
            red.add(new DummyTreeTableNode("Wolf 359",           3000,   0.0002,     0.10,   0.12));
            rootNode.add(red);
        }

        private static final Object[] columnNames = {
                "Star", "Temperature (K)", "Luminosity", "Mass", "Radius"
        };

        public DummyTreeTableModel() {
            super(rootNode, Arrays.asList(columnNames));
        }

        @NotNull
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            } else {
                return Double.class;
            }
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return false;
        }
    }
}
