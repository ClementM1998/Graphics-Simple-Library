package com.clay.gsl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public final class Graphics {
    private static JFrame window;
    private static Drawing drawing;
    private static Graphics2D g2d;
    private static Input input = new Input();

    // === Warna Standard ===
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

    // === Kunci ===
    public static final int KEY_HOME = KeyEvent.VK_HOME;
    public static final int KEY_UP = KeyEvent.VK_UP;
    public static final int KEY_PAGE_UP = KeyEvent.VK_PAGE_UP;
    public static final int KEY_LEFT = KeyEvent.VK_LEFT;
    public static final int KEY_RIGHT = KeyEvent.VK_RIGHT;
    public static final int KEY_END = KeyEvent.VK_END;
    public static final int KEY_DOWN = KeyEvent.VK_DOWN;
    public static final int KEY_PAGE_DOWN = KeyEvent.VK_PAGE_DOWN;
    public static final int KEY_INSERT = KeyEvent.VK_INSERT;
    public static final int KEY_DELETE = KeyEvent.VK_DELETE;
    public static final int KEY_F1 = KeyEvent.VK_F1;
    public static final int KEY_F2 = KeyEvent.VK_F2;
    public static final int KEY_F3 = KeyEvent.VK_F3;
    public static final int KEY_F4 = KeyEvent.VK_F4;
    public static final int KEY_F5 = KeyEvent.VK_F5;
    public static final int KEY_F6 = KeyEvent.VK_F6;
    public static final int KEY_F7 = KeyEvent.VK_F7;
    public static final int KEY_F8 = KeyEvent.VK_F8;
    public static final int KEY_F9 = KeyEvent.VK_F9;

    // === Variable ===
    private static int bkcolor = BLACK;
    private static boolean fill = false;

    // === Init Window ===
    public static void initgraph(String title) {
        initgraph(title, 800, 600);
    }

    public static void initgraph(String title, int width, int height) {
        initwindow(title, 0, 0, width, height);
    }

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

        // === Latar belakang BLACK secara lalai ===
        setbkcolor(bkcolor);
        setcolor(WHITE);
    }

    // === Utility ===
    public static void refreshgraph() {
        drawing.repaint();
        g2d.dispose();
    }

    public static void cleargraph() {
        setbkcolor(bkcolor);
    }

    public static void closegraph() {
        if (window != null) {
            window.dispose();
            System.exit(0);
        }
    }

    public static void delay(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {}
    }

    // === Warna & Gaya ===
    public static void setbkcolor(int hex) {
        bkcolor = hex;
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

    public static void setfill(boolean f) {
        fill = f;
    }

    public static void setlinestyle(float width, float ... dash) {
        if (dash.length == 0) g2d.setStroke(new BasicStroke(width));
        else g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, dash, 0));
    }

    // === Bentuk Asas ===
    public static void line(int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void circle(int cx, int cy, int r) {
        if (fill) g2d.fillOval(cx - r, cy - r, r * 2, r * 2);
        else g2d.drawOval(cx - r, cy - r, r * 2, r * 2);
    }

    public static void ellipse(int x, int y, int w, int h) {
        if (fill) g2d.fillOval(x, y, w, h);
        else g2d.drawOval(x, y, w, h);
    }

    public static void rectangle(int x, int y, int w, int h) {
        if (fill) g2d.fillRect(x, y, w - x, h - y);
        else g2d.drawRect(x, y, w - x, h - y);
    }

    public static void polygon(int[] x, int[] y, int n) {
        if (fill) g2d.fillPolygon(x, y, n);
        else g2d.drawPolygon(x, y, n);
    }

    public static void polygon(int num, int[] points) {
        if (points.length < num * 2) throw new IllegalArgumentException("Jumlah koordinat tidak mencukupi untuk polygon");
        int[] x = new int[num];
        int[] y = new int[num];
        for (int i = 0;i < num;i++) {
            x[i] = points[i * 2];
            y[i] = points[i * 2 + 1];
        }
        if (fill) g2d.fillPolygon(x, y, num);
        else g2d.drawPolygon(x, y, num);
    }

    public static void triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] x = { x1, x2, x3 };
        int[] y = { y1, y2, y3 };
        if (fill) g2d.fillPolygon(x, y, 3);
        else g2d.drawPolygon(x, y, 3);
    }

    public static void arc(int x, int y, int w, int h, int start, int sweep) {
        if (fill) g2d.fillArc(x, y, w, h, start, sweep);
        else g2d.drawArc(x, y, w, h, start, sweep);
    }

    public static void putpixel(int x, int y, int color) {
        drawing.buffer.setRGB(x, y, color);
    }

    public static void textxy(String text, int x, int y) {
        textxy(text, x, y, 14);
    }

    public static void textxy(String text, int x, int y, int size) {
        g2d.setFont(new Font("Arial", Font.PLAIN, size));
        g2d.drawString(text, x, y);
    }

    // === Pengandalian Kekunci dan Tetikus
    public static boolean iskeypressed(int key) {
        return input.isKeyPressed(key);
    }

    public static boolean ismousepressed() {
        return input.isMousePressed();
    }

    public static int getmousex() {
        return input.getMouseX();
    }

    public static int getmousey() {
        return input.getMouseY();
    }

    // === Drawing Canvas ===
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

    // === Input Handler ===
    private static class Input implements KeyListener, MouseListener, MouseMotionListener {
        private static HashSet<Integer> keys = new HashSet<>();
        private static boolean mousePressed = false;
        private static int mouseX, mouseY;

        public boolean isKeyPressed(int key) {
            return keys.contains(key);
        }

        public boolean isMousePressed() {
            return mousePressed;
        }

        public int getMouseX() {
            return mouseX;
        }

        public int getMouseY() {
            return mouseY;
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {}

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            keys.add(keyEvent.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            keys.remove(keyEvent.getKeyCode());
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {}

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            mousePressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            mousePressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {}

        @Override
        public void mouseExited(MouseEvent mouseEvent) {}

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        }

    }

}
