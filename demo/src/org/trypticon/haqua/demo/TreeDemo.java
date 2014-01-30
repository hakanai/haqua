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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import java.awt.BorderLayout;

/**
 * @author trejkaz
 */
public class TreeDemo implements Demo {
    @Override
    public String getName() {
        return "Trees";
    }

    @Override
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        TreeModel treeModel = new DummyTreeModel();
        JTree tree = new JTree(treeModel);
        JScrollPane tableScroll = new JScrollPane(tree);

        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    private static class DummyTreeModel extends DefaultTreeModel {
        public DummyTreeModel() {
            super(new DefaultMutableTreeNode("Root"));

            MutableTreeNode a = new DefaultMutableTreeNode("A");
            a.insert(new DefaultMutableTreeNode("1"), 0);
            a.insert(new DefaultMutableTreeNode("2"), 1);
            a.insert(new DefaultMutableTreeNode("3"), 2);
            ((MutableTreeNode) getRoot()).insert(a, 0);
            MutableTreeNode b = new DefaultMutableTreeNode("B");
            b.insert(new DefaultMutableTreeNode("1"), 0);
            b.insert(new DefaultMutableTreeNode("2"), 1);
            b.insert(new DefaultMutableTreeNode("3"), 2);
            ((MutableTreeNode) getRoot()).insert(b, 1);
            MutableTreeNode c = new DefaultMutableTreeNode("C");
            c.insert(new DefaultMutableTreeNode("1"), 0);
            c.insert(new DefaultMutableTreeNode("2"), 1);
            c.insert(new DefaultMutableTreeNode("3"), 2);
            ((MutableTreeNode) getRoot()).insert(c, 2);
        }
    }
}
