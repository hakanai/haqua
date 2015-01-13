package org.trypticon.haqua;

import com.apple.laf.AquaMenuItemUI;
import org.jetbrains.annotations.NotNull;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @author trejkaz
 */
public class HaquaMenuItemUI extends MenuItemUI {
    private final AquaMenuItemUI delegate;

    public HaquaMenuItemUI(AquaMenuItemUI delegate) {
        this.delegate = delegate;
    }

    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        // Unfortunately the constructor for AquaMenuItemUI is package local, so we can't just inherit from it. :(
        return new HaquaMenuItemUI((AquaMenuItemUI) AquaMenuItemUI.createUI(component));
    }

    @Override
    public void update(Graphics g, JComponent c) {
        if (!c.getComponentOrientation().isLeftToRight()) {

            Graphics2D gCopy = (Graphics2D) g.create();
            try {
                // By painting 9 pixels to the right, 9 pixels on the left of the background are not painted,
                // so we work around that here.
                delegate.paintBackground(g, c, c.getWidth(), c.getHeight());

                // AquaMenuUI paints the item 9 pixels too far left in right-to-left orientation.
                gCopy.translate(9, 0);

                delegate.update(gCopy, c);
            } finally {
                gCopy.dispose();
            }

        } else {
            delegate.update(g, c);
        }
    }

    // Not overriding (nor delegating) paint() at the moment but we overrode update() and that seems to suffice.

    // Everything below is straight delegation.

    @Override
    public void installUI(JComponent c) {
        delegate.installUI(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        delegate.uninstallUI(c);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return delegate.getPreferredSize(c);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return delegate.getMinimumSize(c);
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
        return delegate.getMaximumSize(c);
    }

    @Override
    public boolean contains(JComponent c, int x, int y) {
        return delegate.contains(c, x, y);
    }

    @Override
    public int getBaseline(JComponent c, int width, int height) {
        return delegate.getBaseline(c, width, height);
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
        return delegate.getBaselineResizeBehavior(c);
    }

    @Override
    public int getAccessibleChildrenCount(JComponent c) {
        return delegate.getAccessibleChildrenCount(c);
    }

    @Override
    public Accessible getAccessibleChild(JComponent c, int i) {
        return delegate.getAccessibleChild(c, i);
    }
}
