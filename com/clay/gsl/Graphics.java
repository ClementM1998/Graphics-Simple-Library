package com.clay.gsl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public final class Graphics {

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
    public static final int KEY_ESCAPE = KeyEvent.VK_ESCAPE;
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

    public static final int KEY_A = KeyEvent.VK_A;
    public static final int KEY_B = KeyEvent.VK_B;
    public static final int KEY_C = KeyEvent.VK_C;
    public static final int KEY_D = KeyEvent.VK_D;
    public static final int KEY_E = KeyEvent.VK_E;
    public static final int KEY_F = KeyEvent.VK_F;
    public static final int KEY_G = KeyEvent.VK_G;
    public static final int KEY_H = KeyEvent.VK_H;
    public static final int KEY_I = KeyEvent.VK_I;
    public static final int KEY_J = KeyEvent.VK_J;
    public static final int KEY_K = KeyEvent.VK_K;
    public static final int KEY_L = KeyEvent.VK_L;
    public static final int KEY_M = KeyEvent.VK_M;
    public static final int KEY_N = KeyEvent.VK_N;
    public static final int KEY_O = KeyEvent.VK_O;
    public static final int KEY_P = KeyEvent.VK_P;
    public static final int KEY_Q = KeyEvent.VK_Q;
    public static final int KEY_R = KeyEvent.VK_R;
    public static final int KEY_S = KeyEvent.VK_S;
    public static final int KEY_T = KeyEvent.VK_T;
    public static final int KEY_U = KeyEvent.VK_U;
    public static final int KEY_V = KeyEvent.VK_V;
    public static final int KEY_W = KeyEvent.VK_W;
    public static final int KEY_X = KeyEvent.VK_X;
    public static final int KEY_Y = KeyEvent.VK_Y;
    public static final int KEY_Z = KeyEvent.VK_Z;

    // Tetikus
    public static final int MOUSE_BUTTON_LEFT = MouseEvent.BUTTON1;
    public static final int MOUSE_BUTTON_MIDDLE = MouseEvent.BUTTON2;
    public static final int MOUSE_BUTTON_RIGHT = MouseEvent.BUTTON3;

    // --- window & drawing ---
    private static JFrame window;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;
    private static Graphics2D g2; // current frame graphics
    private static int width = 800;
    private static int height = 600;
    private static boolean antialis = true;
    private static int targetFps = 0; // 0 = uncapped
    private static long frameNanos = 0L;

    private static Color backgroundColor = Color.BLACK; // default clear color
    private static Color currentColor = Color.WHITE; // default current color

    // --- input state ---
    private static final boolean[] keyDown = new boolean[256];
    private static final Set<Integer> keyPressed = new HashSet<>();
    private static final Set<Integer> keyReleased = new HashSet<>();

    private static volatile int mousex = 0;
    private static volatile int mousey = 0;
    private static final boolean[] mouseDown = new boolean[8]; // 1..7
    private static final Set<Integer> mousePressed = new HashSet<>();
    private static final Set<Integer> mouseReleased = new HashSet<>();

    private static boolean initialized = false;

    private Graphics() {}

    // =========================================
    // Lifecycle
    // =========================================

    // === Init Window ===
    public static void initgraph(String title) {
        initgraph(title, width, height);
    }

    public static void initgraph(String title, int w, int h) {
        initwindow(title, 0, 0, w, h);
    }

    public static void initwindow(String title, int x, int y, int w, int h) {
        if (initialized) return;

        width = Math.max(1, w);
        height = Math.max(1, h);

        Runnable ui = () -> {
            window = new JFrame(title);
            canvas = new Canvas();
            window.setSize(width, height);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setLayout(new BorderLayout());

            attachInputListeners();

            window.add(canvas, BorderLayout.CENTER);
            //window.pack();
            window.setLocation(x, y);
            window.setVisible(true);

            canvas.createBufferStrategy(3);
            bufferStrategy = canvas.getBufferStrategy();
            initialized = true;
        };

        if (SwingUtilities.isEventDispatchThread()) ui.run();
        else try { SwingUtilities.invokeAndWait(ui); } catch (Exception e) {}

        // prime frame timing
        setFrameRate(targetFps);
    }

    // ================================
    // Frame control
    // ================================

    private static void beginFrame() {
        ensureBuffer();
        if (g2 != null) return; // already begun
        g2 = (Graphics2D) bufferStrategy.getDrawGraphics();
        if (antialis) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    public static void cleargraph() {
        beginFrame();
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, getwindowwidth(), getwindowheight());
    }

    public static void refreshgraph() {
        if (g2 != null) {
            g2.dispose();
            g2 = null;
        }
        if (bufferStrategy != null) {
            if (!bufferStrategy.contentsLost()) bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        }
        if (frameNanos > 0L) {
            final long start = System.nanoTime();
            final long end = start + frameNanos;
            long now = start;
            while (now < end) {
                long sleepMs = (end - now) / 1000000L;
                if (sleepMs > 0) try { Thread.sleep(Math.min(sleepMs, 2)); } catch (InterruptedException e) {}
                else Thread.onSpinWait();
                now = System.nanoTime();
            }
        }
        keyPressed.clear();
        keyReleased.clear();
        mousePressed.clear();
        mouseReleased.clear();
    }

    public static void closegraph() {
        if (!initialized) return;
        try {
            if (window != null) {
                window.setVisible(false);
                window.dispose();
                System.exit(0);
            }
        } finally {
            window = null;
            canvas = null;
            bufferStrategy = null;
            g2 = null;
            initialized = false;
        }
    }

    public static void setFrameRate(int fps) {
        targetFps = Math.max(0, fps);
        frameNanos = (targetFps <= 0) ? 0L : (1000000000L / targetFps);
    }

    public static int getwindowwidth() {
        return (canvas != null) ? canvas.getWidth() : width;
    }

    public static int getwindowheight() {
        return (canvas != null) ? canvas.getHeight() : height;
    }

    public static JFrame getwindow() {
        return window;
    }

    public static Canvas getcanvasO() {
        return canvas;
    }

    private static void ensureBuffer() {
        if (canvas == null) return;
        if (bufferStrategy == null) {
            canvas.createBufferStrategy(3);
            bufferStrategy = canvas.getBufferStrategy();
        }
    }

    private static Graphics2D g() {
        if (g2 == null) beginFrame();
        return g2;
    }

    public static void delay(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {}
    }

    public static void setbkcolor(int color) {
        backgroundColor = new Color(color, true);
        if (canvas != null) canvas.setBackground(new Color(color, true));
    }

    public static int getbkcolor() {
        return backgroundColor.getRGB();
    }

    public static void setcolor(int color) {
        Color current = new Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
        currentColor = current;
    }

    public static void setcolor(int color, int alpha) {
        Color current = new Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
        int red = current.getRed();
        int green = current.getGreen();
        int blue = current.getBlue();
        currentColor = new Color(red, green, blue, alpha);
    }

    public static int getcolor() {
        return currentColor.getRGB();
    }

    public static void setstrokewidth(float w) {
        g().setStroke(new BasicStroke(w));
    }

    public static void settextstyle(String name, int style, int size) {
        // Tukar font family (Arial, Courier, dsb)
        // Style (plain, bold, italic)
        // Size (px atau pt)
        g().setFont(new Font(name, style, size));
    }

    public static void setantialis(boolean on) {
        antialis = on;
    }

    public static void line(int x1, int y1, int x2, int y2) {
        g().setColor(currentColor);
        g().drawLine(x1, y1, x2, y2);
    }

    // point > putpixel
    public static void putpixel(int x, int y, int color) {
        g().setColor(new Color(color, true));
        g().drawLine(x, y, x, y);
    }

    // drawarc > arc
    // arc versi circle (radius)
    public static void arc(int x, int y, int start, int end, int radius) {
        g().setColor(currentColor);
        g().drawArc(x - radius, y - radius, radius * 2, radius * 2, start, end - start);
    }

    // fillarc > pieslice
    // pieslice (macam arc tapi isi)
    public static void pieslice(int x, int y, int start, int end, int radius) {
        g().setColor(currentColor);
        g().fillArc(x - radius, y - radius, radius * 2, radius * 2, start, end - start);
    }

    // sector (ellipse slice)
    public static void sector(int x, int y, int start, int end, int xradius, int yradius) {
        g().setColor(currentColor);
        g().fillArc(x - xradius, y - yradius, xradius * 2, yradius * 2, start, end - start);
    }

    // drawrect > rectangle
    public static void rectangle(int x1, int y1, int x2, int y2) {
        g().setColor(currentColor);
        g().drawRect(x1, y1, x2 - x1, y2 - y1);
    }

    // fillrect > bar
    public static void bar(int x1, int y1, int x2, int y2) {
        g().setColor(currentColor);
        g().fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    public static void bar3d(int x1, int y1, int x2, int y2, int depth, boolean topflag) {
        int w = x2 - x1;
        int h = y2 - y1;

        g().setColor(currentColor);
        // isi kotak depan
        g().fillRect(x1, y1, w, h);
        // garis outline kotak depan
        g().drawRect(x1, y1, w, h);
        // sisi kanan
        int[] xRight = { x2, x2 + depth, x2 + depth, x2 };
        int[] yRight = { y1, y1 - depth, y2 - depth, y2 };
        g().drawPolygon(xRight, yRight, 4);
        g().fillPolygon(xRight, yRight, 4);
        if (topflag) {
            // sisi atas
            int[] xTop = { x1, x2, x2 + depth, x1 + depth };
            int[] yTop = { y1, y1, y1 - depth, y1 - depth };
            g().drawPolygon(xTop, yTop, 4);
            g().fillPolygon(xTop, yTop, 4);
        }

    }

    public static void ellipse(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().drawOval(x, y, w, h);
    }

    public static void fillellipse(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().fillOval(x, y, w, h);
    }

    public static void circle(int cx, int cy, int r) {
        g().setColor(currentColor);
        g().drawOval(cx - r, cy - r, r * 2, r * 2);
    }

    public static void drawpoly(int num, int[] points) {
        if (points.length < 2 * num) return;
        int[] x = new int[num];
        int[] y = new int[num];
        for (int i = 0;i < num;i++) {
            x[i] = points[2 * i];
            y[i] = points[2 * i + 1];
        }
        g().setColor(currentColor);
        g().drawPolygon(x, y, num);
    }

    public static void fillpoly(int num, int[] points) {
        if (points.length < 2 * num) return;
        int[] x = new int[num];
        int[] y = new int[num];
        for (int i = 0;i < num;i++) {
            x[i] = points[2 * i];
            y[i] = points[2 * i + 1];
        }
        g().setColor(currentColor);
        g().fillPolygon(x, y, num);
    }

    public static BufferedImage loadimage(String path) {
        return loadimage(new File(path));
    }

    public static BufferedImage loadimage(File file) {
        if (file.exists()) {
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void drawimagepixels(BufferedImage img, int px, int py) {
        for (int y = 0;y < img.getHeight();y++) {
            for (int x = 0;x < img.getWidth();x++) {
                int color = img.getRGB(x, y);
                putpixel(px + x, py + y, color);
            }
        }
    }

    public static void putimage(BufferedImage img, int x, int y) {
        g().drawImage(img, x, y, null);
    }

    public static void outtextxy(String text, int x, int y) {
        FontMetrics fm = g().getFontMetrics();
        g().setColor(currentColor);
        g().drawString(text, x, y + fm.getAscent());
    }

    // getpixel (ambil warna pixel dari canvas)
    public static int getpixel(int x, int y) {
        BufferedImage img = new BufferedImage(getwindowwidth(), getwindowheight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gimg = img.createGraphics();
        canvas.paint(gimg);
        gimg.dispose();
        return img.getRGB(x, y);
    }

    // floodfill (simple: isi statu warna dalam polygon tertutup)
    // basic version sahaja, tidak se-efisien WinBGI
    public static void floodfill(int x, int y, int newColor) {
        BufferedImage img = new BufferedImage(getwindowwidth(), getwindowheight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gimg = img.createGraphics();
        canvas.paint(gimg);
        gimg.dispose();

        int targetColor = img.getRGB(x, y);
        if (targetColor == newColor) return;

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            if (p.x < 0 || p.y < 0 || p.x >= img.getWidth() || p.y >= img.getHeight()) continue;
            if (img.getRGB(p.x, p.y) != targetColor) continue;

            img.setRGB(p.x, p.y, newColor);
            stack.push(new Point(p.x + 1, p.y));
            stack.push(new Point(p.x - 1, p.y));
            stack.push(new Point(p.x, p.y + 1));
            stack.push(new Point(p.x, p.y - 1));
        }

        g().drawImage(img, 0, 0, null);
    }

    public static BufferedImage createimage(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public static void saveframe(String path) {
        BufferedImage img = new BufferedImage(getwindowwidth(), getwindowheight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gimg = img.createGraphics();
        canvas.paint(gimg);
        gimg.dispose();
        try {
            ImageIO.write(img, "png", new File(path));
        } catch (Exception e) {}
    }

    private static void attachInputListeners() {
        // Key
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode() & 0xFF;
                if (!keyDown[code]) keyPressed.add(code);
                keyDown[code] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode() & 0xFF;
                keyDown[code] = false;
                keyReleased.add(code);
            }
        });
        // Mouse buttons + motion
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                int b = e.getButton();
                if (b >= 0 && b < mouseDown.length) {
                    if (!mouseDown[b]) mousePressed.add(b);
                    mouseDown[b] = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int b = e.getButton();
                if (b >= 0 && b < mouseDown.length) {
                    mouseDown[b] = false;
                    mouseReleased.add(b);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }
        };

        MouseMotionListener mml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }
        };

        canvas.addMouseListener(ml);
        canvas.addMouseMotionListener(mml);

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    // Key queries
    public static boolean iskeydown(int key) {
        return keyDown[key & 0xFF];
    }

    public static boolean iskeypressed(int key) {
        return keyPressed.contains(key & 0xFF);
    }

    public static boolean iskeyreleased(int key) {
        return keyReleased.contains(key & 0xFF);
    }

    // Mouse queries
    public static int getmousex() {
        return mousex;
    }

    public static int getmousey() {
        return mousey;
    }

    public static boolean ismousedown(int button) {
        return (button >= 0 && button < mouseDown.length) && mouseDown[button];
    }

    public static boolean ismousepressed(int button) {
        return mousePressed.contains(button);
    }

    public static boolean ismousereleased(int button) {
        return mouseReleased.contains(button);
    }

}
