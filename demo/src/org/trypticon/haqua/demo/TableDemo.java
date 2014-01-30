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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;

/**
 * @author trejkaz
 */
public class TableDemo implements Demo {
    @Override
    public String getName() {
        return "Tables";
    }

    @Override
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        TableModel tableModel = new DummyTableModel();
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane tableScroll = new JScrollPane(table);

        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    private static class DummyTableModel extends DefaultTableModel {
        private static final Object[][] rowData = {
                { "Orionis C",          33000,  30000.0,    18.0,   5.90 },
                { "Becrux",             30000,  16000.0,    16.0,   5.70 },
                { "Spica",              22000,  8300.0,     10.5,   5.10 },
                { "Achernar",           15000,  750.0,      5.40,   3.70 },
                { "Rigel",              12500,  130.0,      3.50,   2.70 },
                { "Sirius A",           9500,   63.0,       2.60,   2.30 },
                { "Fomalhaut",          9000,   40.0,       2.20,   2.00 },
                { "Altair",             8700,   24.0,       1.90,   1.80 },
                { "Polaris A",          7400,   9.0,        1.60,   1.50 },
                { "Eta Scorpii",        7100,   6.3,        1.50,   1.30 },
                { "Procyon A",          6400,   4.0,        1.35,   1.20 },
                { "Alpha Centauri A",   5900,   1.45,       1.08,   1.05 },
                { "The Sun",            5800,   100.0,      1.00,   1.00 },
                { "Mu Cassiopeiae",     5600,   0.70,       0.95,   0.91 },
                { "Tau Ceti",           5300,   0.44,       0.85,   0.87 },
                { "Pollux",             5100,   0.36,       0.83,   0.83 },
                { "Epsilon Eridani",    4830,   0.28,       0.78,   0.79 },
                { "Alpha Centauri B",   4370,   0.18,       0.68,   0.74 },
                { ".",                  3670,   0.075,      0.47,   0.63 },
                { "Lalande 21185",      3400,   0.03,       0.33,   0.36 },
                { "Ross 128",           3200,   0.0005,     0.20,   0.21 },
                { "Wolf 359",           3000,   0.0002,     0.10,   0.12 },
        };

        private static final Object[] columnNames = {
                "Star", "Temperature (K)", "Luminosity", "Mass", "Radius"
        };

        private DummyTableModel() {
            super(rowData, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            } else {
                return Double.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
