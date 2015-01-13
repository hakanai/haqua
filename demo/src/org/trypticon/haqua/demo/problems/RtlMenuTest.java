package org.trypticon.haqua.demo.problems;

import javax.swing.*;
import java.awt.*;

public class RtlMenuTest implements Runnable {
    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel(new HaquaLookAndFeel());
        SwingUtilities.invokeLater(new RtlMenuTest());
    }

    @Override
    public void run() {
        JPanel container = new JPanel();
        container.setOpaque(true);
        container.setBackground(Color.WHITE);
        container.setLayout(new GridLayout(4, 1));
        container.add(new JMenuItem("One"));
        container.add(new JCheckBoxMenuItem("Two", true));
        container.add(new JRadioButtonMenuItem("Three", true));
        JMenu subMenu = new JMenu("Sub-menu");
        subMenu.add(new JMenuItem("One"));
        subMenu.add(new JMenuItem("Two"));
        subMenu.add(new JMenuItem("Three"));
        container.add(subMenu);

        JFrame frame = new JFrame();
        frame.setContentPane(container);

        frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
