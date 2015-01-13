package org.trypticon.haqua;

import com.apple.laf.AquaScrollBarUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author trejkaz
 */
public class HaquaScrollBarUI extends AquaScrollBarUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaScrollBarUI();
    }

    @Override
    public void update(Graphics graphics, JComponent component) {
        if (fScrollBar.getOrientation() == Adjustable.HORIZONTAL &&
                !fScrollBar.getComponentOrientation().isLeftToRight()) {

            // AquaScrollBarUI paints the scroll bar the wrong way around when oriented right-to-left
            // (it is at the left when value == 0, but should be at the right.)
            Graphics2D graphicsCopy = (Graphics2D) graphics.create();
            try {
                graphicsCopy.translate(fScrollBar.getWidth() - 1, 0);
                graphicsCopy.scale(-1, 1);
                super.update(graphicsCopy, component);
            } finally {
                graphicsCopy.dispose();
            }
        } else {
            super.update(graphics, component);
        }
    }

    @Override
    protected TrackListener createTrackListener() {
        return new FlippedTrackListener();
    }

    // Custom track listener flips the mouse events around so that the logic in AquaScrollBarUI thinks the user
    // is moving the mouse in the opposite direction to what they really are.
    private class FlippedTrackListener extends TrackListener {
        @Override
        public void mouseReleased(MouseEvent event) {
            super.mouseReleased(flipEvent(event));
        }

        @Override
        public void mousePressed(MouseEvent event) {
            super.mousePressed(flipEvent(event));
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            super.mouseDragged(flipEvent(event));
        }

        // These ones aren't used, but I figured it would be sane to override them anyway, just in case.

        @Override
        public void mouseClicked(MouseEvent event) {
            super.mouseClicked(flipEvent(event));
        }

        @Override
        public void mouseEntered(MouseEvent event) {
            super.mouseEntered(flipEvent(event));
        }

        @Override
        public void mouseExited(MouseEvent event) {
            super.mouseExited(flipEvent(event));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            super.mouseWheelMoved(flipEvent(event));
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            super.mouseMoved(flipEvent(event));
        }

        private MouseEvent flipEvent(MouseEvent event) {
            if (fScrollBar.getOrientation() == Adjustable.HORIZONTAL &&
                    !fScrollBar.getComponentOrientation().isLeftToRight()) {

                int x = fScrollBar.getWidth() - 1 - event.getX();
                int y = event.getY();
                Point scrollBarLocationOnScreen = fScrollBar.getLocationOnScreen();
                int xOnScreen = x + scrollBarLocationOnScreen.x;
                int yOnScreen = y + scrollBarLocationOnScreen.y;
                return new MouseEvent(
                        event.getComponent(), event.getID(), event.getWhen(), event.getModifiersEx(),
                        x, y, xOnScreen, yOnScreen,
                        event.getClickCount(), event.isPopupTrigger(), event.getButton());
            } else {
                return event;
            }
        }

        private MouseWheelEvent flipEvent(MouseWheelEvent event) {
            if (fScrollBar.getOrientation() == Adjustable.HORIZONTAL &&
                    !fScrollBar.getComponentOrientation().isLeftToRight()) {

                int x = event.getX();
                x = fScrollBar.getWidth() - 1 - x;
                int y = event.getY();
                Point scrollBarLocationOnScreen = fScrollBar.getLocationOnScreen();
                int xOnScreen = x + scrollBarLocationOnScreen.x;
                int yOnScreen = y + scrollBarLocationOnScreen.y;
                return new MouseWheelEvent(
                        event.getComponent(), event.getID(), event.getWhen(), event.getModifiersEx(),
                        x, y, xOnScreen, yOnScreen,
                        event.getClickCount(), event.isPopupTrigger(),
                        event.getScrollType(), event.getScrollAmount(),
                        event.getWheelRotation(), event.getPreciseWheelRotation());
            } else {
                return event;
            }
        }
    }
}
