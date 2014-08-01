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

import com.apple.laf.AquaOptionPaneUI;

import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author trejkaz
 */
public class HaquaOptionPaneUI extends AquaOptionPaneUI {
    // private fields from the superclass.
    private static final int kOKCancelButtonWidth = 79;
    private static final int kButtonHeight = 23;
    private static final int kDialogSmallPadding = 4;
    private static final int kDialogLargePadding = 23;

    private static final int destructiveOptionSpacing = 26;

    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaOptionPaneUI();
    }

    @Override
    protected Container createButtonArea() {
        Container buttonArea = super.createButtonArea();
        buttonArea.setLayout(new HaquaButtonAreaLayout());
        return buttonArea;
    }

    @Override
    protected void installComponents() {
        Container messageArea = createMessageArea();
        Container buttonArea = createButtonArea();

        GroupLayout layout = new GroupLayout(optionPane);
        optionPane.setLayout(layout);

        Icon icon = getIcon();
        int iconGap = icon == null ? 0 : icon.getIconWidth() + kDialogLargePadding;

        layout.setHorizontalGroup(layout.createParallelGroup()
                                      .addComponent(messageArea)
                                      .addGroup(layout.createSequentialGroup()
                                                    .addGap(iconGap)
                                                    .addComponent(buttonArea)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                                    .addComponent(messageArea)
                                    .addComponent(buttonArea));
    }

    private class HaquaButtonAreaLayout extends AquaButtonAreaLayout {

        public HaquaButtonAreaLayout() {
            super(true, 4); // kDialogSmallPadding = 4
        }

        public void layoutContainer(final Container container) {
            Component[] children = container.getComponents();
            if (children == null || 0 >= children.length) {
                return;
            }

            Integer destructiveOption = (Integer) optionPane.getClientProperty("JOptionPane.destructiveOption");
            if (destructiveOption != null) {
                // Move the destructive option to the end of the array.
                Component destructiveComponent = children[destructiveOption];
                List<Component> list = new ArrayList<>(Arrays.asList(children));
                list.remove(destructiveComponent);
                list.add(destructiveComponent);

                children = list.toArray(new Component[list.size()]);
                destructiveOption = children.length - 1;
            }

            final int numChildren = children.length;

            Dimension[] buttonSizes = computeButtonSizes(children);
            Insets insets = container.getInsets();

            int containerWidth = container.getSize().width;
            int xLocation = containerWidth;
            int yLocation = insets.top;

            for (int i = 0; i < numChildren; i++) {
                if (destructiveOption != null && i == destructiveOption) {
                    xLocation -= destructiveOptionSpacing;
                }

                Dimension buttonSize = buttonSizes[i];
                xLocation -= buttonSize.width;
                children[i].setBounds(xLocation, yLocation, buttonSizes[i].width, buttonSize.height);
                xLocation -= padding;

                // Now xLocation == the right edge of the next button.
            }

            if (!container.getComponentOrientation().isLeftToRight()) {

                // --[Discard Changes]------[Cancel ]-[Save   ]--
                //                                    ^         ^
                //                                    |---------| containerWidth - xLocation

                // --[Save   ]-[Cancel ]------[Discard Changes]--
                // ^ ^
                // |-| containerWidth - (xLocation + buttonWidth)

                for (int i = 0; i < numChildren; i++) {
                    Component child = children[i];
                    xLocation = child.getX();
                    int buttonWidth = child.getWidth();
                    children[i].setBounds(containerWidth - (xLocation + buttonWidth),
                                          child.getY(),
                                          buttonWidth,
                                          child.getHeight());

                }
            }
        }

        @Override
        public Dimension preferredLayoutSize(Container container) {
            Component[] children = container.getComponents();
            Dimension dimension = new Dimension();
            if (children == null || 0 >= children.length) {
                return dimension;
            }

            int numChildren = children.length;

            Dimension[] buttonSizes = computeButtonSizes(children);
            Insets insets = container.getInsets();

            // This fixes Aqua's implementation not giving enough space when there is longer text.
            int width = 0;
            int height = 0;
            for (Dimension buttonSize : buttonSizes) {
                width += buttonSize.width;
                height = Math.max(height, buttonSize.height);
            }
            width += padding * (numChildren - 1) + insets.left + insets.right;
            if (optionPane.getClientProperty("JOptionPane.destructiveOption") != null) {
                width += destructiveOptionSpacing;
            }
            height += insets.top + insets.bottom;

            dimension.width = width;
            dimension.height = height;
            return dimension;
        }

        @Override
        public Dimension minimumLayoutSize(Container container) {
            return preferredLayoutSize(container);
        }

        private Dimension[] computeButtonSizes(Component[] children) {
            Dimension[] buttonSizes = new Dimension[children.length];
            for (int i = 0; i < children.length; i++) {
                Dimension buttonSize = children[i].getPreferredSize();

                // Resizes smaller buttons up to the same as OK and Cancel. But if you look at native apps,
                // they don't expand all buttons to the size of the largest button.
                if (buttonSize.width < kOKCancelButtonWidth) {
                    buttonSize.width = kOKCancelButtonWidth;
                }

                buttonSizes[i] = buttonSize;
            }
            return buttonSizes;
        }
    }

}
