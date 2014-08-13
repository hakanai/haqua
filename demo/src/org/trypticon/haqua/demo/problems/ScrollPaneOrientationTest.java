package org.trypticon.haqua.demo.problems;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneOrientationTest implements Runnable {
    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel(new HaquaLookAndFeel());
        SwingUtilities.invokeLater(new ScrollPaneOrientationTest());
    }

    @Override
    public void run() {

        JPanel view = new ViewPanel();
        JScrollPane scrollPane = new JScrollPane(view);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println(scrollPane.getHorizontalScrollBar().getModel().getValue());
    }

    private class ViewPanel extends JPanel implements Scrollable {
        private static final int SIZE = 1000;
        private final Paint paint = new GradientPaint(new Point(0, 0), Color.RED, new Point(SIZE, SIZE), Color.BLUE);

        @Override
        protected void paintComponent(Graphics g) {
            ((Graphics2D) g).setPaint(paint);
            ((Graphics2D) g).fill(new Rectangle(0, 0, SIZE, SIZE));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(SIZE, SIZE);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(400, 400);
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 8;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 400;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
