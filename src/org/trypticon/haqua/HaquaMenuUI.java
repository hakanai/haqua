package org.trypticon.haqua;

import com.apple.laf.AquaMenuUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @author trejkaz
 */
public class HaquaMenuUI extends AquaMenuUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaMenuUI();
    }

    @Override
    protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int textIconGap) {

        if (!c.getComponentOrientation().isLeftToRight()) {

            // AquaMenuUI paints the arrow the wrong way around in right-to-left orientation.
            if (arrowIcon instanceof UIResource) {
                arrowIcon = new FlippedIcon(arrowIcon);
            }

            Graphics2D gCopy = (Graphics2D) g.create();
            try {
                // By painting 9 pixels to the right, 9 pixels on the left of the background are not painted,
                // so we work around that here.
                paintBackground(g, c, c.getWidth(), c.getHeight());

                // AquaMenuUI paints the item 9 pixels too far left in right-to-left orientation.
                gCopy.translate(9, 0);

                super.paintMenuItem(gCopy, c, checkIcon, arrowIcon, background, foreground, textIconGap);
            } finally {
                gCopy.dispose();
            }

        } else {
            super.paintMenuItem(g, c, checkIcon, arrowIcon, background, foreground, textIconGap);
        }
    }

}
