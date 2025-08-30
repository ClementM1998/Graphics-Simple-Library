package com.clay.gsl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

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

    private static Color clearColor = Color.BLACK; // default clear color
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
        g2.setColor(clearColor);
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

    public static void setstrokewidth(float w) {
        g().setStroke(new BasicStroke(w));
    }

    public static void setfont(Font f) {
        g().setFont(f);
    }

    public static void setantialis(boolean on) {
        antialis = on;
    }

    public static void setclearcolor(int color) {
        clearColor = new Color(color, true);
        if (canvas != null) canvas.setBackground(new Color(color, true));
    }

    public static void line(int x1, int y1, int x2, int y2) {
        g().setColor(currentColor);
        g().drawLine(x1, y1, x2, y2);
    }

    public static void point(int x, int y) {
        g().setColor(currentColor);
        g().fillOval(x, y, 1, 1);
    }

    public static void drawarc(int x, int y, int width, int height, int start, int sweep) {
        g().setColor(currentColor);
        g().drawArc(x, y, width, height, start, sweep);
    }

    public static void fillarc(int x, int y, int width, int height, int start, int sweep) {
        g().setColor(currentColor);
        g().fillArc(x, y, width, height, start, sweep);
    }

    public static void drawrect(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().drawRect(x, y, w - x, h - y);
    }

    public static void fillrect(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().fillRect(x, y, w - x, h - y);
    }

    public static void drawellipse(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().drawOval(x, y, w, h);
    }

    public static void fillellipse(int x, int y, int w, int h) {
        g().setColor(currentColor);
        g().fillOval(x, y, w, h);
    }

    public static void drawcircle(int cx, int cy, int r) {
        g().setColor(currentColor);
        g().drawOval(cx - r, cy - r, r * 2, r * 2);
    }

    public static void fillcircle(int cx, int cy, int r) {
        g().setColor(currentColor);
        g().fillOval(cx - r, cy - r, r * 2, r * 2);
    }

    public static void drawpolygon(int[] xpoints, int[] ypoints, int npoint) {
        g().setColor(currentColor);
        g().drawPolygon(xpoints, ypoints, npoint);
    }

    public static void fillpolygon(int[] xpoints, int[] ypoints, int npoint) {
        g().setColor(currentColor);
        g().fillPolygon(xpoints, ypoints, npoint);
    }

    public static void drawpolygon(int num, int[] points) {
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

    public static void fillpolygon(int num, int[] points) {
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

    public static void drawimage(String path, int x, int y) {
        drawimage(path, x, y, 200, 200);
    }

    public static void drawimage(String path, int x, int y, int w, int h) {
        File file = new File(path);
        if (file.exists() && file.getName().endsWith(".png")) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(file);
            } catch (Exception e) {}
            if (image != null) g().drawImage(image, x, y, w, h, null);
        }
    }

    public static void text(String text, int x, int y) {
        FontMetrics fm = g().getFontMetrics();
        g().setColor(currentColor);
        g().drawString(text, x, y + fm.getAscent());
    }

    public static void println(String text) {}

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

    public static void textcentered(String text, int cx, int cy) {
        FontMetrics fm = g().getFontMetrics();
        int x = cx - fm.stringWidth(text) / 2;
        int y = cy - (fm.getAscent() - fm.getDescent()) / 2;
        g().drawString(text, x, y);
    }

    public static BufferedImage createimage(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

}
