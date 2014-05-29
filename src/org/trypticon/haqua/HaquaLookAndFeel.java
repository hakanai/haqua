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

import com.apple.laf.AquaLookAndFeel;

import javax.swing.LayoutStyle;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

/**
 * <p>Top-level class for the Haqua look and feel.</p>
 * 
 * <p>Your usual usage of this class is going to look like this:</p>
 * <pre>
    if ("com.apple.laf.AquaLookAndFeel".equals(UIManager.getSystemLookAndFeelClassName())) {
        try {
            UIManager.setLookAndFeel("org.trypticon.haqua.HaquaLookAndFeel");
        } catch (Exception e) {
            // Fallback logic here if you want it, but the default on Mac OS X is already
            // the system look and feel, so you might just be able to ignore it.
        }
    }
 * </pre>
 *
 * @author trejkaz
 */
public class HaquaLookAndFeel extends AquaLookAndFeel {
    @Override
    public LayoutStyle getLayoutStyle() {
        return new AquaLayoutStyle();
    }

    @Override
    protected void initClassDefaults(UIDefaults defaults) {
        super.initClassDefaults(defaults);

        defaults.put("ButtonUI", "org.trypticon.haqua.HaquaButtonUI");
        defaults.put("TextFieldUI", "org.trypticon.haqua.HaquaTextFieldUI");
        defaults.put("ComboBoxUI", "org.trypticon.haqua.HaquaComboBoxUI");
        defaults.put("ProgressBarUI", "org.trypticon.haqua.HaquaProgressBarUI");
        defaults.put("TableUI", "org.trypticon.haqua.HaquaTableUI");
        defaults.put("TreeUI", "org.trypticon.haqua.HaquaTreeUI");
        defaults.put("PanelUI", "org.trypticon.haqua.HaquaPanelUI");
        defaults.put("ToolBarUI", "org.trypticon.haqua.HaquaToolBarUI");
        defaults.put("TabbedPaneUI", "org.trypticon.haqua.HaquaTabbedPaneUI");
        defaults.put("ScrollPaneUI", "org.trypticon.haqua.HaquaScrollPaneUI");
        defaults.put("ViewportUI", "org.trypticon.haqua.HaquaViewportUI");
        defaults.put("PopupMenuUI", "org.trypticon.haqua.HaquaPopupMenuUI");
        defaults.put("OptionPaneUI", "org.trypticon.haqua.HaquaOptionPaneUI");
    }

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        final Object controlSmallFont = defaults.get("IconButton.font");

        // Default titled border is the etched line style, but this one looks more native.
        defaults.put("TitledBorder.border", defaults.get("TitledBorder.aquaVariant"));
        // Native apps use the 11pt variant, not 13pt.
        defaults.put("TitledBorder.font", controlSmallFont);

        // Colour to use when striping. This one was taken from Interface Builder, so I'm not confident it's correct for all OSX.
        defaults.put("Table.alternateRowColor", new ColorUIResource(236, 241, 247));
        // Set margins to 0 to make selections appear properly contiguous.
        defaults.put("Table.rowMargin", 0);
        defaults.put("Table.columnMargin", 0);
        // Paint table backgrounds all the way down the viewport.
        defaults.put("Table.fillsViewportHeight", true);

        // Opaque JPanel looks wrong inside JTabbedPane and probably elsewhere.
        defaults.put("Panel.opaque", false);

        // Opaque JViewport looks wrong when you have a JPanel in it.
        defaults.put("Viewport.opaque", false);
    }

}
