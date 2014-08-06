package org.trypticon.haqua;

import com.apple.laf.AquaListUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * @author trejkaz
 */
public class HaquaListUI extends AquaListUI {
    @NotNull
    @SuppressWarnings("UnusedDeclaration") // called via reflection
    public static ComponentUI createUI(JComponent component) {
        return new HaquaListUI();
    }

    @Override
    protected void repaintCell(Object paramObject, int paramInt, boolean paramBoolean) {
        // Works around a bug where AquaListUI doesn't check for this and then gets a NullPointerException.
        if (list.getGraphics() != null) {
            super.repaintCell(paramObject, paramInt, paramBoolean);
        }
    }
}
