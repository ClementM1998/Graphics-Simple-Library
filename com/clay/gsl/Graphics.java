package com.clay.gsl;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
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

    // Teks
    public static final int FONT_PLAIN = Font.PLAIN;
    public static final int FONT_BOLD = Font.BOLD;
    public static final int FONT_ITALIC = Font.ITALIC;

    // --- window & drawing ---
    private static JFrame window;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;
    private static Graphics2D g2; // current frame graphics
    private static int width = 640; // 800;
    private static int height = 480; // 600;
    private static boolean antialis = true;
    private static int targetFps = 0; // 0 = uncapped
    private static long frameNanos = 0L;

    private static Color backgroundColor = Color.BLACK; // default clear color
    private static Color currentColor = Color.WHITE; // default current color

    // --- text style ---
    public static final int DEFAULT_FONT = 0; // Monospace, bitmap feel
    public static final int TRIPLEX_FONT = 1; // Stroke-like, Serif
    public static final int SMALL_FONT = 2;   // Small monospace
    public static final int SANS_SERIF_FONT = 3;  // SansSerif (Arial-like)
    public static final int GOTHIC_FONT = 4; // Gothic-like, bold Monospaced
    public static final int SCRIPT_FONT = 5; // Script (cursive)
    public static final int SIMPLEX_FONT = 6; // Simplex stroke, thin SansSerif
    public static final int TRIPLEX_SCR_FONT = 7; // Triplex + Script (decorative)
    public static final int COMPLEX_FONT = 8; // Complex Serif

    public static final int HORIZ_DIR = 0;
    public static final int VERT_DIR = 1;

    private static String currentFontName = "Dialog";
    private static int currentFontStyle = FONT_PLAIN;
    private static int currentFontSize = 12;
    private static int currentTextFont = DEFAULT_FONT;
    private static int currentTextDirection = HORIZ_DIR;
    private static int currentCharSize = 1;

    // --- input state ---
    private static final boolean[] keyDown = new boolean[256];
    private static final Set<Integer> keyPressed = new HashSet<>();
    private static final Set<Integer> keyReleased = new HashSet<>();
    private static volatile char lastchar = '\0';

    private static volatile int mousex = 0;
    private static volatile int mousey = 0;
    private static final boolean[] mouseDown = new boolean[8]; // 1..7
    private static final Set<Integer> mousePressed = new HashSet<>();
    private static final Set<Integer> mouseReleased = new HashSet<>();

    // --- sound ---
    private static SourceDataLine soundLine;
    private static Thread soundThread;
    private static volatile boolean soundPlaying = false;

    private static boolean initialized = false;

    // -- line to rel , move to rel
    private static int currentX = 0;
    private static int currentY = 0;
    private static int currentTextX = 0;

    // -- line & fill style state
    public static final int SOLID_LINE = 0;
    public static final int DOTTED_LINE = 1;
    public static final int DASHED_LINE = 2;
    public static final int USERBIT_LINE = 3;

    public static final int NORM_WIDTH = 1;
    public static final int THICK_WIDTH = 3;

    private static int currentLineStyle = SOLID_LINE;
    private static int currentLineThickness = 1;
    private static float[] currentLinePattern = null;

    public static final int SOLID_FILL = 0;
    public static final int LINE_FILL = 1;
    public static final int HATCH_FILL = 2;
    public static final int XHATCH_FILL = 3;
    public static final int WIDE_DOT_FILL = 4;
    public static final int INTERLEAVE_FILL = 5;

    private static int currentFillPattern = SOLID_FILL;
    private static Color currentFillColor = Color.WHITE;
    private static Paint currentFillPaint = Color.WHITE;

    private static final int DEFAULT_PALETTE_SIZE = 256;
    private static int[] palette = new int[DEFAULT_PALETTE_SIZE];

    static {
        for (int i = 0;i < palette.length;i++) palette[i] = 0xFF000000 | i;
        palette[0] = BLACK;
        palette[1] = WHITE;
        palette[2] = RED;
        palette[3] = GREEN;
        palette[4] = BLUE;
        palette[5] = YELLOW;
        palette[6] = CYAN;
        palette[7] = MAGENTA;
        palette[8] = GRAY;
        palette[9] = SILVER;
        palette[10] = MAROON;
        palette[11] = OLIVE;
        palette[12] = LIME;
        palette[13] = TEAL;
        palette[14] = NAVY;
        palette[15] = PURPLE;
    }

    public static final int LEFT_TEXT = 0;
    public static final int CENTER_TEXT = 1;
    public static final int RIGHT_TEXT = 2;

    public static final int TOP_TEXT = 0;
    //public static final int CENTER_TEXT = 1;
    public static final int BOTTOM_TEXT = 2;

    private static int horizJustify = LEFT_TEXT;
    private static int vertJustify = TOP_TEXT;

    private Graphics() {}

    // =========================================
    // Lifecycle
    // =========================================

    // === Init Window ===
    public static void initgraph(String title) {
        initwindow(width, height, title);
    }

    public static void initwindow(int w, int h, String title) {
        initwindow( w, h, title, 1, 1);
    }

    public static void initwindow(int w, int h, String title, int x, int y) {
        if (initialized) return;

        width = Math.max(1, w);
        height = Math.max(1, h);

        Runnable ui = () -> {
            window = new JFrame(title);
            canvas = new Canvas();

            canvas.setPreferredSize(new Dimension(width, height));

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setLayout(new BorderLayout());

            attachInputListeners();

            window.add(canvas, BorderLayout.CENTER);
            window.pack();
            window.setLocation(x, y);
            window.setVisible(true);

            canvas.createBufferStrategy(3);
            bufferStrategy = canvas.getBufferStrategy();
            initialized = true;
        };

        if (SwingUtilities.isEventDispatchThread()) ui.run();
        else try { SwingUtilities.invokeAndWait(ui); } catch (Exception e) {}

        // prime frame timing
        setframerate(targetFps);
        cleargraph();
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

    public static void setframerate(int fps) {
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

    public static Canvas getcanvas() {
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

    public static int getmaxcolor() {
        return palette.length - 1;
    }

    public static int getpalette(int colornum) {
        if (colornum < 0) return TRANSPARENT;
        if (colornum >= palette.length) return palette[palette.length - 1];
        return palette[colornum];
    }

    public static void setpalette(int colornum, int argb) {
        if (colornum < 0 || colornum >= palette.length) return;
        palette[colornum] = argb;
    }

    public static void setrgbpalette(int colornum, int red, int green, int blue) {
        if (colornum < 0 || colornum >= palette.length) return;
        red = clampByte(red);
        green = clampByte(green);
        blue = clampByte(blue);
        palette[colornum] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

    private static int clampByte(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private static int translateColor(int color) {
        if (color >= 0 && color < palette.length) return palette[color];
        return color;
    }

    public static void setstrokewidth(float w) {
        g().setStroke(new BasicStroke(w));
    }

    public static void settextstyle(int font, int direction, int charsize) {
        currentTextFont = font;
        currentTextDirection = direction;
        currentCharSize = Math.max(1, charsize);

        switch (font) {
            case DEFAULT_FONT:
                currentFontName = "Monospaced"; // mirip bitmap, mudah dibaca
                currentFontStyle = FONT_PLAIN;
                break;
            case TRIPLEX_FONT:
                currentFontName = "Serif"; // stroke-like
                currentFontStyle = FONT_PLAIN;
                break;
            case SMALL_FONT:
                currentFontName = "Monospaced"; // kecil
                currentFontStyle = FONT_PLAIN;
                break;
            case SANS_SERIF_FONT:
                currentFontName = "SansSerif"; // Arial-like
                currentFontStyle = FONT_PLAIN;
                break;
            case GOTHIC_FONT:
                currentFontName = "Monospaced"; // Gothic-like
                currentFontStyle = FONT_BOLD;
                break;
            case SCRIPT_FONT:
                currentFontName = "Dialog"; // Java tiada cursive default
                currentFontStyle = FONT_ITALIC;
                break;
            case SIMPLEX_FONT:
                currentFontName = "SansSerif"; // garis tunggal
                currentFontStyle = FONT_PLAIN;
                break;
            case TRIPLEX_SCR_FONT:
                currentFontName = "Serif"; // dekoratif
                currentFontStyle = FONT_ITALIC;
                break;
            case COMPLEX_FONT:
                currentFontName = "Serif"; // kompleks
                currentFontStyle = FONT_BOLD;
                break;
            default:
                currentFontName = "Dialog";
                currentFontStyle = FONT_PLAIN;
                break;
        }

        currentFontSize = 8 * currentCharSize;
    }

    public static void settextjustify(int horiz, int vert) {
        horizJustify = horiz;
        vertJustify = vert;
    }

    public static void setantialis(boolean on) {
        antialis = on;
    }

    public static void setlinestyle(int style, int upattern, int thickness) {
        currentLineStyle = style;
        currentLineThickness = (thickness == THICK_WIDTH ? 2 : 1);
        switch (style) {
            case SOLID_LINE:
                currentLinePattern = null;
                break;
            case DOTTED_LINE:
                currentLinePattern = new float[] {7f, 1f}; // titik
                break;
            case DASHED_LINE:
                currentLinePattern = new float[] {9f, 2f}; // dashed
                break;
            case USERBIT_LINE:
                // Contoh ringkas guna upattern
                if (upattern != 0) {
                    currentLinePattern = new float[] { (float) upattern, (float) upattern };
                } else {
                    currentLinePattern = null;
                }
                break;
        }

        if (currentLinePattern != null) {
            g().setStroke(new BasicStroke(
                    currentLineThickness,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    10f,
                    currentLinePattern,
                    0f
            ));
        } else {
            g().setStroke(new BasicStroke(currentLineThickness));
        }
    }

    public static void setfillstyle(int pattern, int color) {
        currentFillPattern = pattern;
        currentFillColor = new Color(color, true);

        int size = 4; // saiz tile

        if (pattern == HATCH_FILL) size = 8;
        if (pattern == XHATCH_FILL) size = 8;

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setColor(new Color(0, 0, 0, 0)); // transparent bg
        g2d.fillRect(0, 0, size, size);

        g2d.setColor(currentFillColor);

        switch (pattern) {
            case SOLID_FILL:
                g2d.fillRect(0, 0, size, size);
                break;
            case LINE_FILL:
                g2d.drawLine(0, 0, 0, size);
                break;
            case HATCH_FILL:
                g2d.drawLine(0, 0, size, 0);
                g2d.drawLine(0, 0, 0, size);
                break;
            case XHATCH_FILL:
                g2d.drawLine(0, 0, size, size);
                g2d.drawLine(size, 0, 0, size);
                break;
            case WIDE_DOT_FILL:
                g2d.fillOval(0, 0, 2,2);
                break;
            case INTERLEAVE_FILL:
                g2d.drawLine(0, 0, size/2, size/2);
                g2d.drawLine(size/2, 0, size, size);
                break;
        }
        g2d.dispose();
        currentFillPaint = new TexturePaint(img, new Rectangle(0, 0, size, size));
    }

    public static void line(int x1, int y1, int x2, int y2) {
        g().setColor(currentColor);
        g().drawLine(x1, y1, x2, y2);
    }

    public static void moveto(int x, int y) {
        currentX = x;
        currentY = y;
    }

    public static void moverel(int dx, int dy) {
        currentX += dx;
        currentY += dy;
    }

    public static void lineto(int x, int y) {
        if (g() != null) {
            g().setColor(currentColor);
            g().drawLine(currentX, currentY, x, y);
        }
        currentX = x;
        currentY = y;
    }

    public static void linerel(int dx, int dy) {
        int newX = currentX + dx;
        int newY = currentY + dy;
        if (g() != null) {
            g().setColor(currentColor);
            g().drawLine(currentX, currentY, newX, newY);
        }
        currentX = newX;
        currentY = newY;
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
        g().setPaint(currentFillPaint);
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
        g().setPaint(currentFillPaint);
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
        g().setPaint(currentFillPaint);
        g().fillPolygon(x, y, num);
    }

    public static void triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] x = { x1, x2, x3 };
        int[] y = { y1, y2, y3 };
        g().setColor(currentColor);
        g().drawPolygon(x, y, 3);
    }

    public static void filltriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] x = { x1, x2, x3 };
        int[] y = { y1, y2, y3 };
        g().setColor(currentColor);
        g().setPaint(currentFillPaint);
        g().fillPolygon(x, y, 3);
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

    public static void outtextxy(int x, int y, String text) {
        g().setFont(new Font(currentFontName, currentFontStyle, currentFontSize));
        g().setColor(currentColor);

        FontMetrics fm = g().getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();

        int drawX = x;
        int drawY = y;

        // Horizontal justify
        if (horizJustify == CENTER_TEXT) drawX = x - textWidth / 2;
        else if (horizJustify == RIGHT_TEXT) drawX = x - textWidth;

        // Vertical justify
        if (vertJustify == TOP_TEXT) drawY = y + ascent;
        else if (vertJustify == CENTER_TEXT) drawY = y + (ascent - textHeight / 2);
        else if (vertJustify == BOTTOM_TEXT) drawY = y;

        /*
        g().drawString(text, drawX, drawY);

        // Update currentX macam WinBGI
        currentX = drawX + textWidth;
        currentY = drawY;
        */

        if (currentTextDirection == HORIZ_DIR) {
            g().drawString(text, drawX, drawY);
            currentX = drawX + textWidth;
            currentY = drawY;
        } else {
            Graphics2D g2d = g();
            g2d.rotate(-Math.PI / 2, drawX, drawY);
            g2d.drawString(text, drawX, drawY);
            g2d.rotate(Math.PI / 2, drawX, drawY);
            currentX = drawX;
            currentY = drawY + textHeight;
        }
    }

    public static void outtext(String text) {
        g().setFont(new Font(currentFontName, currentFontStyle, currentFontSize));
        FontMetrics fm = g().getFontMetrics();
        g().setColor(currentColor);
        g().drawString(text, currentX, currentY + fm.getAscent());
        int textwidth = fm.stringWidth(text);
        currentX += textwidth;
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
    public static void floodfill(int startx, int starty, int newColor) {
        /*
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
         */

        /*
        if (canvas == null) return;

        BufferedImage img = new BufferedImage(getwindowwidth(), getwindowheight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gimg = img.createGraphics();
        canvas.paint(gimg);
        gimg.dispose();

        int w = img.getWidth();
        int h = img.getHeight();

        if (startx < 0 || starty < 0 || startx >= w || starty >= h) return;

        int targetColor = img.getRGB(startx, starty);
        newColor = translateColor(newColor);
        if (targetColor == newColor) return;

        int max = w * h;
        int[] stackX = new int[max];
        int[] stackY = new int[max];
        int sp = 0;

        // push seed
        stackX[sp] = startx;
        stackY[sp] = starty;
        sp++;

        while (sp > 0) {
            sp--;
            int x = stackX[sp];
            int y = stackY[sp];

            int lx = x;
            while (lx >= 0 && img.getRGB(lx, y) == targetColor) lx--;
            lx++;

            boolean spanAbove = false;
            boolean spanBelow = false;
            int rx = lx;
            while (rx < w && img.getRGB(rx, y) == targetColor) {
                img.setRGB(rx, y, newColor);

                if (y > 0) {
                    if (img.getRGB(rx, y - 1) == targetColor) {
                        if (!spanAbove) {
                            if (sp < max) {
                                stackX[sp] = rx;
                                stackY[sp] = y - 1;
                                sp++;
                            }
                            spanAbove = true;
                        }
                    } else {
                        spanAbove = false;
                    }
                }

                if (y < h - 1) {
                    if (img.getRGB(rx, y + 1) == targetColor) {
                        if (!spanBelow) {
                            if (sp < max) {
                                stackX[sp] = rx;
                                stackY[sp] = y + 1;
                                sp++;
                            }
                            spanBelow = true;
                        }
                    } else {
                        spanBelow = false;
                    }
                }

                rx++;
            }
        }

        g().drawImage(img, 0, 0, null);
         */
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
                lastchar = e.getKeyChar();
                int code = e.getKeyCode() & 0xFF;
                if (!keyDown[code]) keyPressed.add(code);
                keyDown[code] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                lastchar = '\0';
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

    public static boolean isKB() {
        for (boolean kd : keyDown) if (kd) return true;
        if (!keyPressed.isEmpty() || !keyReleased.isEmpty()) return true;
        return false;
    }

    public static char getch() {
        char c = lastchar;
        //lastchar = '\0'; // reset supaya hanya dibaca sekali
        return c;
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

    public static void sound(int freq) {
        if (freq <= 0) return;

        stopSoundInternal(); // pastikan tiada sound sebelum ini

        soundPlaying = true;
        soundThread = new Thread(() -> {
            try {
                float SAMPLE_RATE = 44100;
                byte[] buf = new byte[1];
                AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
                soundLine = AudioSystem.getSourceDataLine(af);
                soundLine.open(af);
                soundLine.start();

                double angle = 0;
                double increment = (2 * Math.PI) * freq / SAMPLE_RATE;

                while (soundPlaying) {
                    buf[0] = (byte) (Math.sin(angle) * 127);
                    soundLine.write(buf, 0, 1);
                    angle += increment;
                    if (angle > 2 * Math.PI) angle -= 2 * Math.PI;
                }

                soundLine.drain();
                soundLine.stop();
                soundLine.close();
            } catch (Exception e) {}
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }

    public static void nosound() {
        stopSoundInternal();
    }

    private static void stopSoundInternal() {
        soundPlaying = false;
        if (soundThread != null) {
            try {
                soundThread.join(50);
            } catch (InterruptedException e) {}
            soundThread = null;
        }
        if (soundLine != null) {
            soundLine.stop();
            soundLine.close();
            soundLine = null;
        }
    }

}
