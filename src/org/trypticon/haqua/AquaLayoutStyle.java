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


import sun.swing.DefaultLayoutStyle;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Panel;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>An implementation of {@code LayoutStyle} for Mac OS X Tiger.</p>
 *
 * <p>The information used for this layout style comes from:
 *    http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/</p>
 *
 * <p>This code was taken (by Haqua) from the Swing Layout project, which was in turn derived from Quaqua.
 *    The changes made here are as follows:</p>
 * <ul>
 *     <li>Changed to fit the {@link javax.swing.LayoutStyle} API which is now in Swing.</li>
 *     <li>Removed {@code EMPTY_INSETS} constant. {@link Insets} are mutable and the caller was receiving
 *         a shared instance.</li>
 *     <li>Updated code style to fit this project (automatically.)</li>
 * </ul>
 *
 * @author Werner Randelshofer
 */
class AquaLayoutStyle extends DefaultLayoutStyle {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

    /**
     * Mini size style.
     */
    private final static int MINI = 0;

    /**
     * Small size style.
     */
    private final static int SMALL = 1;

    /**
     * Regular size style.
     */
    private final static int REGULAR = 2;


    /**
     * The containerGapDefinitions array defines the preferred insets (child gaps)
     * of a parent container towards one of its child components.
     *
     * Note: As of now, we do not yet specify the preferred gap from a child
     * to its parent. Therefore we may not be able to treat all special cases.
     *
     * This array is used to initialize the containerGaps HashMap.
     *
     * The array has the following structure, which is supposed to be a
     * a compromise between legibility and code size.
     * containerGapDefinitions[0..n] = preferred insets for some parent UI's
     * containerGapDefinitions[][0..m-3] = name of parent UI,
     *                                 optionally followed by a full stop and
     *                                 a style name
     * containerGapDefinitions[][m-2] = mini insets
     * containerGapDefinitions[][m-1] = small insets
     * containerGapDefinitions[][m] = regular insets
     */
    private final static Object[][] containerGapDefinitions = {
            // Format:
            // { list of parent UI's,
            //   mini insets, small insets, regular insets }

            { "TabbedPaneUI",
                    new Insets(6,10,10,10), new Insets(6,10,10,12),
                    new Insets(12,20,20,20)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGLayout/chapter_19_section_3.html#//apple_ref/doc/uid/TP30000360/DontLinkElementID_27
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGLayout/chapter_19_section_3.html#//apple_ref/doc/uid/TP30000360/DontLinkElementID_26
            // note for small and mini size: leave 8 to 10 pixels on top
            // note for regular size: leave only 12 pixel at top if tabbed pane UI
            { "RootPaneUI",
                    new Insets(8,10,10,10), new Insets(8,10,10,12),
                    new Insets(14,20,20,20)
            },

            // These child gaps are used for all other components
            { "default",
                    new Insets(8,10,10,10), new Insets(8,10,10,12),
                    new Insets(14,20,20,20)
            },
    };

    /**
     * The relatedGapDefinitions table defines the preferred gaps
     * of one party of two related components.
     *
     * The effective preferred gap is the maximum of the preferred gaps of
     * both parties.
     *
     * This array is used to initialize the relatedGaps HashMap.
     *
     * The array has the following structure, which is supposed to be a
     * a compromise between legibility and code size.
     * containerGapDefinitions[0..n] = preferred gaps for a party of a two related UI's
     * containerGapDefinitions[][0..m-3] = name of UI
     *                                 optionally followed by a full stop and
     *                                 a style name
     * containerGapDefinitions[][m-2] = mini insets
     * containerGapDefinitions[][m-1] = small insets
     * containerGapDefinitions[][m] = regular insets
     */
    private final static Object[][] relatedGapDefinitions = {
            // Format:
            // { list of UI's,
            //   mini insets, small insets, regular insets }

            // Push Button:
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF104
            { "ButtonUI", "ButtonUI.push", "ButtonUI.text",
                    "ToggleButtonUI.push", "ToggleButtonUI.text",
                    new Insets(8,8,8,8), new Insets(10,10,10,10), new Insets(12,12,12,12)
            },

            // Metal Button
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF187
            { "ButtonUI.metal", "ToggleButtonUI.metal",
                    new Insets(8,8,8,8), new Insets(8,8,8,8), new Insets(12,12,12,12)
            },

            // Bevel Button (Rounded and Square)
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF112
            { "ButtonUI.bevel", "ButtonUI.toggle", "ButtonUI.square",
                    "ToggleButtonUI", "ToggleButtonUI.bevel", "ToggleButtonUI.square",
                    "ToggleButtonUI.toggle",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },

            // Bevel Button (Rounded and Square)
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF112
            { "ButtonUI.bevel.largeIcon", "ToggleButtonUI.bevel.largeIcon",
                    new Insets(8,8,8,8), new Insets(8,8,8,8), new Insets(8,8,8,8)
            },

            // Icon Button
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF189
            { "ButtonUI.icon",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
            { "ButtonUI.icon.largeIcon",
                    new Insets(8,8,8,8), new Insets(8,8,8,8), new Insets(8,8,8,8)
            },

            // Round Button
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF191
            { "ButtonUI.round", "ToggleButtonUI.round",
                    new Insets(12,12,12,12), new Insets(12,12,12,12),
                    new Insets(12,12,12,12)
            },

            // Help Button
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_2.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF193
            { "ButtonUI.help",
                    new Insets(12,12,12,12), new Insets(12,12,12,12),
                    new Insets(12,12,12,12)
            },

            // Segmented Control
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_3.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF196
            { "ButtonUI.toggleCenter", "ToggleButtonUI.toggleCenter",
                    new Insets(8,0,8,0), new Insets(10,0,10,0), new Insets(12,0,12,0)
            },
            { "ButtonUI.toggleEast", "ToggleButtonUI.toggleEast",
                    new Insets(8,0,8,8), new Insets(10,0,10,10), new Insets(12,0,12,12)
            },
            { "ButtonUI.toggleWest", "ToggleButtonUI.toggleWest",
                    new Insets(8,8,8,0), new Insets(10,10,10,0), new Insets(12,12,12,0)
            },

            { "ButtonUI.toolBarTab", "ToggleButtonUI.toolBarTab",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },

            // Color Well Button
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_3.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF213
            { "ButtonUI.colorWell", "ToggleButtonUI.colorWell",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_3.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF198
            // FIXME - The following values are given in the AHIG.
            // In reality, the values further below seem to be more appropriate.
            // Which ones are right?
            //{ "CheckBoxUI", new Insets(7, 5, 7, 5), new Insets(8, 6, 8, 6), new Insets(8, 8, 8, 8) },
            { "CheckBoxUI",
                    new Insets(6, 5, 6, 5), new Insets(7, 6, 7, 6), new Insets(7, 6, 7, 6)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_3.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF198
            { "ComboBoxUI.editable",
                    new Insets(8, 5, 8, 5), new Insets(10, 6, 10, 6),
                    new Insets(12, 8, 12, 8)
            },
            { "ComboBoxUI.uneditable",
                    new Insets(6, 5, 6, 5), new Insets(8, 6, 8, 6),
                    new Insets(10, 8, 10, 8)
            },
            // There is no spacing given for labels.
            // This comes from playing with IB.
            // We use the values here, which is the minimum of the spacing of all
            // other components.
            { "LabelUI",
                    new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8)
            },

            // ? spacing not given
            { "ListUI",
                    new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6)
            },

            // ? spacing not given
            { "PanelUI",
                    new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_5.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF106
            // ? spacing not given
            { "ProgressBarUI",
                    new Insets(8,8,8,8), new Insets(10,10,10,10), new Insets(12,12,12,12)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_3.html#//apple_ref/doc/uid/20000957-TP30000359-BIAHBFAD
            { "RadioButtonUI",
                    new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6)
            },

            //http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_6.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF114
            // ? spacing not given. We use the same as for text fields.
            { "ScrollPaneUI",
                    new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8),
                    new Insets(8, 10, 8, 10)
            },

            //http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_8.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF214
            // ? spacing not given
            //http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGLayout/chapter_19_section_2.html#//apple_ref/doc/uid/20000957-TP30000360-CHDEACGD
            { "SeparatorUI",
                    new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10),
                    new Insets(12, 12, 12, 12)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_4.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF115
            { "SliderUI.horizontal",
                    new Insets(8,8,8,8), new Insets(10,10,10,10), new Insets(12,12,12,12)
            },
            { "SliderUI.vertical",
                    new Insets(8,8,8,8), new Insets(10,10,10,10), new Insets(12,12,12,12)
            },

            //http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_4.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF204
            { "SpinnerUI",
                    new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8),
                    new Insets(8, 10, 8, 10)
            },

            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_7.html#//apple_ref/doc/uid/20000957-TP30000359-CHDDBIJE
            // ? spacing not given
            { "SplitPaneUI",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
            // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_7.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF105
            // ? spacing not given
            { "TabbedPaneUI",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
            { "TableUI",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
            // ? spacing not given
            { "TextAreaUI", "EditorPaneUI", "TextPaneUI",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
            //http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGControls/chapter_18_section_6.html#//apple_ref/doc/uid/20000957-TP30000359-TPXREF225
            { "TextFieldUI", "FormattedTextFieldUI", "PasswordFieldUI",
                    new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8),
                    new Insets(8, 10, 8, 10)
            },

            // ? spacing not given
            { "TreeUI",
                    new Insets(0,0,0,0), new Insets(0,0,0,0), new Insets(0,0,0,0)
            },
    };

    private final static Object[][] unrelatedGapDefinitions = {
            // UI, mini, small, regular
            { "ButtonUI.help",
                    new Insets(24,24,24,24), new Insets(24,24,24,24),
                    new Insets(24,24,24,24)
            },
            { "default",
                    new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12),
                    new Insets(14, 14, 14, 14)
            },
    };

    /**
     * The indentGapDefinitions table defines the preferred indentation
     * for components that are indented after the specified component.
     *
     * This array is used to initialize the indentGaps HashMap.
     *
     * The array has the following structure, which is supposed to be a
     * a compromise between legibility and code size.
     * indentGapDefinitions[0..n] = preferred gaps for a party of a two related UI's
     * indentGapDefinitions[][0..m-3] = name of UI
     *                                 optionally followed by a full stop and
     *                                 a style name
     * indentGapDefinitions[][m-2] = mini insets
     * indentGapDefinitions[][m-1] = small insets
     * indentGapDefinitions[][m] = regular insets
     */
    private final static Object[][] indentGapDefinitions = {
            // UI, mini, small, regular

            // The Aqua L&F does not scale button images of check boxes and radio
            // buttons. Therefore we use to the same horizontal indents for all sizes.
            { "CheckBoxUI", "RadioButtonUI",
                    new Insets(16, 24, 16, 24), new Insets(20, 24, 20, 24),
                    new Insets(24, 24, 24, 24) },

            { "default",
                    new Insets(16, 16, 16, 16), new Insets(20, 20, 20, 20),
                    new Insets(24, 24, 24, 24) },
    };

    /**
     * The visualMarginDefinition table defines the visually perceived
     * margin of the components.
     *
     * This array is used to initialize the visualMargins HashMap.
     *
     * The array has the following structure, which is supposed to be a
     * a compromise between legibility and code size.
     * visualMarginDefinitions[0..n] = preferred gaps for a party of a two related UI's
     * visualMarginDefinitions[][0..m-1] = name of UI
     *                                 optionally followed by a full stop and
     *                                 a style name
     * containerGapDefinitions[][m] = visual margins
     */
    private final static Object[][] visualMarginDefinitions = {
            // UI, regular
            { "ButtonUI", "ButtonUI.text",
                    "ToggleButtonUI", "ToggleButtonUI.text",
                    new Insets(5, 3, 3, 3)
            },
            { "ButtonUI.icon",
                    "ToggleButtonUI.icon",
                    new Insets(5, 2, 3, 2)
            },
            { "ButtonUI.toolbar",
                    "ToggleButtonUI.toolbar",
                    new Insets(0, 0, 0, 0)
            },
            { "CheckBoxUI", new Insets(4, 4, 3, 3) },
            { "ComboBoxUI", new Insets(2, 3, 4, 3) },
            { "DesktopPaneUI", new Insets(0, 0, 0, 0) },
            { "EditorPaneUI", "TextAreaUI", "TextPaneUI",
                    new Insets(0, 0, 0, 0)
            },
            { "FormattedTextFieldUI", "PasswordFieldUI", "TextFieldUI",
                    new Insets(0, 0, 0, 0)
            },
            { "LabelUI", new Insets(0, 0, 0, 0) },
            { "ListUI", new Insets(0, 0, 0, 0) },
            { "PanelUI", new Insets(0, 0, 0, 0) },
            { "ProgressBarUI", "ProgressBarUI.horizontal", new Insets(0, 2, 4, 2) },
            { "ProgressBarUI.vertical", new Insets(2, 0, 2, 4) },
            { "RadioButtonUI", new Insets(4, 4, 3, 3) },
            { "ScrollBarUI", new Insets(0, 0, 0, 0) },
            { "ScrollPaneUI", new Insets(0, 0, 0, 0) },
            { "SpinnerUI", new Insets(0, 0, 0, 0) },
            { "SeparatorUI", new Insets(0, 0, 0, 0) },
            { "SplitPaneUI", new Insets(0, 0, 0, 0) },
            { "SliderUI", "SliderUI.horizontal", new Insets(3, 6, 3, 6) },
            { "SliderUI.vertical", new Insets(6, 3, 6, 3) },
            { "TabbedPaneUI", "TabbedPaneUI.top", new Insets(5, 7, 10, 7) },
            { "TabbedPaneUI.bottom", new Insets(4, 7, 5, 7) },
            { "TabbedPaneUI.left", new Insets(4, 6, 10, 7) },
            { "TabbedPaneUI.right", new Insets(4, 7, 10, 6) },
            { "TableUI", new Insets(0, 0, 0, 0) },
            { "TreeUI", new Insets(0, 0, 0, 0) },
            { "default", new Insets(0, 0, 0, 0) },
    };

    /**
     * The relatedGaps map defines the preferred gaps
     * of one party of two related components.
     */
    private final static Map<String, ComponentInsets> RELATED_GAPS = createInsetsMap(relatedGapDefinitions);

    /**
     * The unrelatedGaps map defines the preferred gaps
     * of one party of two unrelated components.
     */
    private final static Map<String, ComponentInsets> UNRELATED_GAPS = createInsetsMap(unrelatedGapDefinitions);

    /**
     * The containerGaps map defines the preferred insets (child gaps)
     * of a parent component towards one of its children.
     */
    private final static Map<String, ComponentInsets> CONTAINER_GAPS = createInsetsMap(containerGapDefinitions);

    /**
     * The indentGaps map defines the preferred indentation
     * for components that are indented after the specified component.
     */
    private final static Map<String, ComponentInsets> INDENT_GAPS = createInsetsMap(indentGapDefinitions);

    /**
     * The visualMargins map defines the preferred indentation
     * for components that are indented after the specified component.
     */
    private final static Map<String, ComponentInsets> VISUAL_MARGINS = createInsetsMap(visualMarginDefinitions);

    /**
     * Creates a map for the specified definitions array.
     * <p>
     * The key for the map is the name of the UI, for example, ButtonUI, with
     * a value of ComponentInsets.  Each ComponentInsets may have sub styles.
     */
    private static Map<String, ComponentInsets> createInsetsMap(Object[][] definitions) {
        Map<String, ComponentInsets> map = new HashMap<>();
        for (Object[] definition : definitions) {
            int keys = 0;
            while (keys < definition.length && (definition[keys] instanceof String)) {
                keys++;
            }
            Insets[] values = new Insets[definition.length - keys];
            for (int j = keys; j < definition.length; j++) {
                values[j - keys] = (Insets) definition[j];
            }
            for (int j = 0; j < keys; j++) {
                String key = (String) definition[j];
                int subIndex = key.indexOf('.');
                if (subIndex == -1) {
                    ComponentInsets componentInsets = map.get(key);
                    if (componentInsets == null) {
                        map.put(key, new ComponentInsets(values));
                    } else {
                        assert (componentInsets.getInsets() == null);
                        componentInsets.setInsets(values);
                    }
                } else {
                    String subkey = key.substring(subIndex + 1);
                    String parentKey = key.substring(0, subIndex);
                    ComponentInsets componentInsets = map.get(parentKey);
                    if (componentInsets == null) {
                        componentInsets = new ComponentInsets();
                        map.put(parentKey, componentInsets);
                    }
                    componentInsets.addSubInsets(subkey,
                            new ComponentInsets(values));
                }
            }
        }
        return map;
    }

    @Override
    public int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent) {
        // Check args
        super.getPreferredGap(component1, component2, type, position, parent);

        int result;

        // Compute gap
        if (type == ComponentPlacement.INDENT) {
            // Compute gap
            if (position == SwingConstants.EAST || position == SwingConstants.WEST) {
                int gap = getButtonGap(component1, position);
                if (gap != 0) {
                    return gap;
                }
            }
            int sizeStyle = getSizeStyle(component1);
            Insets gap1 = getPreferredGap(component1, type, sizeStyle);
            switch (position) {
                case SwingConstants.NORTH :
                    result = gap1.bottom;
                    break;
                case SwingConstants.SOUTH :
                    result = gap1.top;
                    break;
                case SwingConstants.EAST :
                    result = gap1.left;
                    break;
                case SwingConstants.WEST :
                default :
                    result = gap1.right;
                    break;
            }
            // Compensate for visual margin
            Insets visualMargin2 = getVisualMargin(component2);
            switch (position) {
                case SwingConstants.NORTH :
                    result -= visualMargin2.bottom;
                    break;
                case SwingConstants.SOUTH :
                    result -= visualMargin2.top;
                    break;
                case SwingConstants.EAST :
                    result -= visualMargin2.left;
                    break;
                case SwingConstants.WEST :
                    result -= visualMargin2.right;
                default :
                    break;
            }
        } else {
            // Compute gap
            int sizeStyle = Math.min(getSizeStyle(component1),
                    getSizeStyle(component2));
            Insets gap1 = getPreferredGap(component1, type, sizeStyle);
            Insets gap2 = getPreferredGap(component2, type, sizeStyle);
            switch (position) {
                case SwingConstants.NORTH :
                    result = Math.max(gap1.top, gap2.bottom);
                    break;
                case SwingConstants.SOUTH :
                    result = Math.max(gap1.bottom, gap2.top);
                    break;
                case SwingConstants.EAST :
                    result = Math.max(gap1.right, gap2.left);
                    break;
                case SwingConstants.WEST :
                default :
                    result = Math.max(gap1.left, gap2.right);
                    break;
            }

            // Compensate for visual margin
            Insets visualMargin1 = getVisualMargin(component1);
            Insets visualMargin2 = getVisualMargin(component2);

            switch (position) {
                case SwingConstants.NORTH :
                    result -= visualMargin1.top + visualMargin2.bottom;
                    break;
                case SwingConstants.SOUTH :
                    result -= visualMargin1.bottom + visualMargin2.top;
                    break;
                case SwingConstants.EAST :
                    result -= visualMargin1.right + visualMargin2.left;
                    break;
                case SwingConstants.WEST :
                    result -= visualMargin1.left + visualMargin2.right;
                default :
                    break;
            }
        }

        // Aqua does not support negative gaps, because all its components are
        // opaque
        return Math.max(0, result);
    }

    @SuppressWarnings("StringEquality") // used to compare UI class ID instances, supposedly safe
    private Insets getPreferredGap(JComponent component, ComponentPlacement type, int sizeStyle) {
        Map gapMap;

        switch (type) {
            case INDENT :
                gapMap = INDENT_GAPS;
                break;
            case RELATED :
                gapMap = RELATED_GAPS;
                break;
            case UNRELATED :
            default :
                gapMap = UNRELATED_GAPS;
                break;
        }

        String uid = component.getUIClassID();
        String style = null;
        // == is ok here as Strings from Swing get interned, if for some reason
        // need .equals then must deal with null.
        if (uid == "ButtonUI" || uid =="ToggleButtonUI") {
            style = (String) component.getClientProperty("JButton.buttonType");
        } else if (uid =="ProgressBarUI") {
            style = (((JProgressBar) component).getOrientation() ==
                    JProgressBar.HORIZONTAL) ? "horizontal" : "vertical";
        } else if (uid == "SliderUI") {
            style = (((JSlider) component).getOrientation()
                    == JProgressBar.HORIZONTAL) ? "horizontal" : "vertical";
        } else if (uid == "TabbedPaneUI") {
            switch (((JTabbedPane) component).getTabPlacement()) {
                case JTabbedPane.TOP :
                    style = "top";
                    break;
                case JTabbedPane.LEFT :
                    style = "left";
                    break;
                case JTabbedPane.BOTTOM :
                    style = "bottom";
                    break;
                case JTabbedPane.RIGHT :
                    style = "right";
                    break;
            }
        } else if (uid == "ComboBoxUI") {
            style = ((JComboBox) component).isEditable() ? "editable" : "uneditable";
        }
        return getInsets(gapMap, uid, style, sizeStyle);
    }

    @Override
    public int getContainerGap(JComponent component, int position, Container parent) {
        int result;
        int sizeStyle = Math.min(getSizeStyle(component), getSizeStyle(parent));

        // Compute gap
        Insets gap = getContainerGap(parent, sizeStyle);

        switch (position) {
            case SwingConstants.NORTH :
                result = gap.top;
                break;
            case SwingConstants.SOUTH :
                result = gap.bottom;
                break;
            case SwingConstants.EAST :
                result = gap.right;
                break;
            case SwingConstants.WEST :
            default :
                result = gap.left;
                break;
        }

        // Compensate for visual margin
        Insets visualMargin = getVisualMargin(component);
        switch (position) {
            case SwingConstants.NORTH :
                result -= visualMargin.top;
                break;
            case SwingConstants.SOUTH :
                result -= visualMargin.bottom;
                // Radio buttons in Quaqua are 1 pixel too high, in order
                // to align their baselines with other components, when no
                // baseline aware layout manager is used.
                if (component instanceof JRadioButton) {
                    result--;
                }
                break;
            case SwingConstants.EAST :
                result -= visualMargin.left;
                break;
            case SwingConstants.WEST :
                result -= visualMargin.right;
            default :
                break;
        }

        // Aqua does not support negative gaps, because all its components are
        // opaque
        return Math.max(0, result);
    }


    private Insets getContainerGap(Container container, int sizeStyle) {
        String uid;
        if (container instanceof JComponent) {
            uid = ((JComponent) container).getUIClassID();
        } else if (container instanceof Dialog) {
            uid = "Dialog";
        } else if (container instanceof Frame) {
            uid = "Frame";
        } else if (container instanceof java.applet.Applet) {
            uid = "Applet";
        } else if (container instanceof Panel) {
            uid = "Panel";
        } else {
            uid = "default";
        }

        // FIXME insert style code here for JInternalFrame with palette style
        return getInsets(CONTAINER_GAPS, uid, null,  sizeStyle);
    }

    private Insets getInsets(Map gapMap, String uid, String style,
                             int sizeStyle) {
        if (uid == null) {
            uid = "default";
        }
        ComponentInsets componentInsets = (ComponentInsets)gapMap.get(uid);
        if (componentInsets == null) {
            componentInsets = (ComponentInsets)gapMap.get("default");
            if (componentInsets == null) {
                return EMPTY_INSETS;
            }
        } else if (style != null) {
            ComponentInsets subInsets = componentInsets.getSubInsets(style);
            if (subInsets != null) {
                componentInsets = subInsets;
            }
        }
        return componentInsets.getInsets(sizeStyle);
    }

    @SuppressWarnings("StringEquality") // used to compare UI class ID instances, supposedly safe
    private Insets getVisualMargin(JComponent component) {
        String uid = component.getUIClassID();
        String style = null;
        if (uid == "ButtonUI" || uid == "ToggleButtonUI") {
            style = (String) component.getClientProperty("JButton.buttonType");
        } else if (uid == "ProgressBarUI") {
            style = (((JProgressBar) component).getOrientation()
                    == JProgressBar.HORIZONTAL) ? "horizontal" :
                    "vertical";
        } else if (uid == "SliderUI") {
            style = (((JSlider) component).getOrientation() ==
                    JProgressBar.HORIZONTAL) ? "horizontal"
                    : "vertical";
        } else if (uid == "TabbedPaneUI") {
            switch (((JTabbedPane) component).getTabPlacement()) {
                case JTabbedPane.TOP :
                    style = "top";
                    break;
                case JTabbedPane.LEFT :
                    style = "left";
                    break;
                case JTabbedPane.BOTTOM :
                    style = "bottom";
                    break;
                case JTabbedPane.RIGHT :
                    style = "right";
                    break;
            }
        }
        Insets gap = getInsets(VISUAL_MARGINS, uid, style, 0);
        // Take into account different positions of the button icon
        if (uid == "RadioButtonUI" || uid == "CheckBoxUI") {
            switch (((AbstractButton) component).getHorizontalTextPosition()) {
                case SwingConstants.RIGHT :
                    gap = new Insets(gap.top, gap.right, gap.bottom, gap.left);
                    break;
                case SwingConstants.CENTER :
                    gap = new Insets(gap.top, gap.right, gap.bottom, gap.right);
                    break;
                    /*
                case SwingConstants.LEFT :
                    break;
                     */
                default:
                    gap = new Insets(gap.top, gap.left, gap.bottom, gap.right);
            }
            if (component.getBorder() instanceof EmptyBorder) {
                gap.left -= 2;
                gap.right -= 2;
                gap.top -= 2;
                gap.bottom -= 2;
            }
        }
        return gap;
    }

    /**
     * Returns the size style of a specified component.
     *
     * @return REGULAR, SMALL or MINI.
     */
    private int getSizeStyle(Component c) {
        // Aqua components have a different style depending on the
        // font size used.
        // 13 Point = Regular
        // 11 Point = Small
        //  9 Point = Mini
        if (c == null) {
            return REGULAR;
        }
        Font font = c.getFont();
        if (font == null) {
            return REGULAR;
        }
        int fontSize = font.getSize();
        return (fontSize >= 13) ? REGULAR : ((fontSize > 9) ? SMALL : MINI);
    }


    /**
     * ComponentInsets is used to manage the Insets for a specific Component
     * type.  Each ComponentInsets may also have children (sub) ComponentInsets.
     * Subinsets are used to represent different styles a component may have.
     * For example, a Button may not a set of insets, as well as insets when
     * it has a style of metal.
     */
    private static class ComponentInsets {
        private Map<String, ComponentInsets> children;
        private Insets[] insets;

        public ComponentInsets() {
        }

        public ComponentInsets(Insets[] insets) {
            this.insets = insets;
        }

        public void setInsets(Insets[] insets) {
            this.insets = insets;
        }

        public Insets[] getInsets() {
            return insets;
        }

        public Insets getInsets(int size) {
            if (insets == null) {
                return EMPTY_INSETS;
            }
            return insets[size];
        }

        void addSubInsets(String subKey, ComponentInsets subInsets) {
            if (children == null) {
                children = new HashMap<>(5);
            }
            children.put(subKey, subInsets);
        }

        ComponentInsets getSubInsets(String subKey) {
            return (children == null) ? null : children.get(subKey);
        }
    }
}