package com.clay.gsl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public final class Graphics {
    private static JFrame window;
    private static Drawing drawing;
    private static Graphics2D g2d;
    private static Input input = new Input();

    private static boolean fill = false;

    public static final int RED = 0xFFFF0000;
    public static final int GREEN = 0xFF00FF00;
    public static final int BLUE = 0xFF0000FF;
    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int TRANSPARENT = 0x00000000;
    public static final int YELLOW = 0xFFFFFF00;
    public static final int CYAN = 0xFF00FFFF;
    public static final int MAGENTA = 0xFFFF00FF;
    public static final int GRAY = 0xFF808080;
    public static final int SILVER = 0xFFC0C0C0;
    public static final int MAROON = 0xFF800000;
    public static final int OLIVE = 0xFF808000;
    public static final int LIME = 0xFF00FF00;
    public static final int TEAL = 0xFF008080;
    public static final int NAVY = 0xFF000080;
    public static final int PURPLE = 0xFF800080;

    // initgraph(String title) : width=800, height=600
    public static void initgraph(String title) {
        initgraph(title, 800, 600);
    }

    // initgraph(String title, int width, int height)
    public static void initgraph(String title, int width, int height) {
        initwindow(title, 0, 0, width, height);
    }

    // initgraph(String title, int x, int y, int width, int height)
    public static void initwindow(String title, int x, int y, int width, int height) {
        window = new JFrame(title);

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLayout(new BorderLayout());

        drawing = new Drawing(width, height);
        drawing.addKeyListener(input);
        drawing.addMouseListener(input);
        drawing.addMouseMotionListener(input);
        drawing.setFocusable(true);
        drawing.requestFocusInWindow();

        window.add(drawing, BorderLayout.CENTER);
        window.pack();
        window.setLocation(x, y);
        window.setVisible(true);

        g2d = (Graphics2D) drawing.createGraphics2D();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static void refreshgraph() {
        drawing.repaint();
    }

    public static void setbkcolor(int hex) {
        setcolor(hex);
        setfill(true);
        rectangle(0, 0, drawing.getWidth(), drawing.getHeight());
        setfill(false);
    }

    public static void setcolor(int hex) {
        int alpha = (hex >> 24) & 0xFF;
        int red = (hex >> 16) & 0xFF;
        int green = (hex >> 8) & 0xFF;
        int blue = hex & 0xFF;
        if ((hex & 0xFF000000) == 0) alpha = 255; // tiada alpha -> penuh
        g2d.setColor(new Color(red, green, blue, alpha));
    }

    public static void setfill(boolean fill) {
        Graphics.fill = fill;
    }

    public static void arc(int x, int y, int w, int h, int start, int sweep) {
        if (fill) g2d.fillArc(x, y, w - x, h - y, start, sweep);
        else g2d.drawArc(x, y, w - x, h - y, start, sweep);
    }

    public static void circle(int cx, int cy, int cr) {
        if (fill) g2d.fillOval(cx - cr, cy - cr, cr * 2, cr * 2);
        else g2d.drawOval(cx - cr, cy - cr, cr * 2, cr * 2);
    }

    public static void rectangle(int x, int y, int w, int h) {
        if (fill) g2d.fillRect(x, y, w - x, h - y);
        else g2d.drawRect(x, y, w - x, h - y);
    }

    private static class Drawing extends Canvas {
        private BufferedImage buffer;

        public Drawing(int width, int height) {
            buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            setPreferredSize(new Dimension(width, height));
            setSize(width, height);
        }

        public Graphics2D createGraphics2D() {
            return buffer.createGraphics();
        }

        @Override
        public void paint(java.awt.Graphics g) {
            g.drawImage(buffer, 0, 0, null);
        }
    }

    private static class Input implements KeyListener, MouseListener, MouseMotionListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {

        }

    }

}
