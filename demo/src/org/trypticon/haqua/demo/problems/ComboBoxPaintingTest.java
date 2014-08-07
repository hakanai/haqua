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

package org.trypticon.haqua.demo.problems;

import javax.swing.*;
import java.awt.*;

public class ComboBoxPaintingTest implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ComboBoxPaintingTest());
    }

    @Override
    public void run() {
        JLabel textFieldLabel = new JLabel("Text field:");

        JTextField textField = new JTextField(8);

        JLabel editableLabel = new JLabel("Editable:");

        // Notice that the button positioning is incorrect and that the highlighting does
        // not go all the way around the combo box.
        JComboBox<String> editableComboBox = new JComboBox<>(new String[] { "Item 1", "Item 2", "Item 3" });
        editableComboBox.setEditable(true);

        JLabel nonEditableLabel = new JLabel("Non-editable:");

        JComboBox<String> nonEditableComboBox = new JComboBox<>(new String[] { "Item 1", "Item 2", "Item 3" });

        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        layout.setAlignOnBaseline(true);
        JPanel panel = new JPanel(layout);
        panel.add(textFieldLabel);
        panel.add(textField);
        panel.add(editableLabel);
        panel.add(editableComboBox);
        panel.add(nonEditableLabel);
        panel.add(nonEditableComboBox);

        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
