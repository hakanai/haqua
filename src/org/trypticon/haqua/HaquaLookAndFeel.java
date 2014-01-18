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
import javax.swing.UIManager;

/**
 * Top-level class for the Haqua look and feel.
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

        defaults.put("PanelUI", "org.trypticon.haqua.HaquaPanelUI");
        defaults.put("ScrollPaneUI", "org.trypticon.haqua.HaquaScrollPaneUI");
        defaults.put("ViewportUI", "org.trypticon.haqua.HaquaViewportUI");
    }

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        final Object controlSmallFont = UIManager.get("IconButton.font");

        // Default titled border is the etched line style, but this one looks more native.
        defaults.put("TitledBorder.border", defaults.get("TitledBorder.aquaVariant"));
        // Native apps use the 11pt variant, not 13pt.
        defaults.put("TitledBorder.font", controlSmallFont);

        // Opaque JPanel looks wrong inside JTabbedPane and probably elsewhere.
        defaults.put("Panel.opaque", false);

        // Opaque JViewport looks wrong when you have a JPanel in it.
        defaults.put("Viewport.opaque", false);
    }

}
