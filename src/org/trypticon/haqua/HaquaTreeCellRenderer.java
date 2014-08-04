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

package org.trypticon.haqua;

import org.jetbrains.annotations.NotNull;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * @author trejkaz
 */
public class HaquaTreeCellRenderer extends DefaultTreeCellRenderer {
    @NotNull
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        // We know DefaultTreeCellRenderer returns itself.
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (sel && getBackground() instanceof UIResource) {
            // DefaultTreeCellRenderer falls back to getForeground() / getBackground()
            // if a more specific method returns null, so there is a lot of double-setting we have to do here.

            // When the window is inactive, we have painted a different row colour in HaquaTreeUI,
            // so the text colour should be different. Funnily enough, Aqua has a property for this,
            // but doesn't appear to use it.
            boolean active = FocusUtils.isInActiveWindow(tree);

            Color foreground = UIManager.getColor(active ? "Tree.selectionForeground" : "Tree.selectionInactiveForeground");
            setTextSelectionColor(foreground);
            setForeground(foreground);

            setBackgroundSelectionColor(null);
            setBackground(null);
            setOpaque(false);
        }

        return this;
    }
}
