# Graphics Simple Library (GSL)

**GSL** ialah perpustakaan grafik ringkas dalam **Java** yang dibina berasaskan  
`JFrame`, `Canvas` dan `BufferStrategy` yang terinspirasi daripada **Borland Graphics Interface (BGI)**.  

Direka khas untuk:
- Projek latihan  
- Demo grafik  
- Mini game 2D
- Tanpa memerlukan perpustakaan luaran.

Dapat dijalankan pada OS:
- Windows (sudah di cuba)
- Linux (sudah di cuba)
- MacOS (belum di cuba)

---

## Ciri-ciri

- Warna standard tersedia (`RED`, `GREEN`, `BLUE`, dll)  
- Input Keyboard (`iskeydown`, `iskeypressed`, `iskeyreleased`)  
- Input Mouse (`getmousex`, `getmousey`, dll)  
- Bentuk Asas: `line`, `putpixel`, `arc`, `pieslice`, `sector`, `bar`, `bar3`, `ellipse`, `fillellipse`, `circle`, `drawpoly`, `fillpoly`
- Teks: `outtextxy`
- Imej: `loadimage`, `drawimagepixels`, `putimage`, `createimage`, `saveframe`
- Frame Control: `setFrameRate(fps)`, `cleargraph()`, `refreshgraph()`  
- Anti-aliasing & gaya: `setstrokewidth()`, `setfont()`  
- Window Lifecycle: `initgraph()`, `closegraph()`  

---

## Fungsi dan Pemboleh ubah tetap

#### Pemboleh ubah tetap
- Warna
  - RED
  - GREEN
  - BLUE
  - BLACK
  - WHITE
  - TRANSPARENT
  - YELLOW
  - CYAN
  - MAGENTA
  - GRAY
  - SILVER
  - MAROON
  - OLIVE
  - LIME
  - TEAL
  - NAVY
  - PURPLE

- Kunci
  - KEY_ESCAPE
  - KEY_HOME
  - KEY_UP
  - KEY_PAGE_UP
  - KEY_LEFT
  - KEY_RIGHT
  - KEY_END
  - KEY_DOWN
  - KEY_PAGE_DOWN
  - KEY_INSERT
  - KEY_DELETE
  - KEY_F1
  - KEY_F2
  - KEY_F3
  - KEY_F4
  - KEY_F5
  - KEY_F6
  - KEY_F7
  - KEY_F8
  - KEY_F9
  - KEY_A
  - KEY_B
  - KEY_C
  - KEY_D
  - KEY_E
  - KEY_F
  - KEY_G
  - KEY_H
  - KEY_I
  - KEY_J
  - KEY_K
  - KEY_L
  - KEY_M
  - KEY_N
  - KEY_O
  - KEY_P
  - KEY_Q
  - KEY_R
  - KEY_S
  - KEY_T
  - KEY_U
  - KEY_V
  - KEY_W
  - KEY_X
  - KEY_Y
  - KEY_Z

- Butang Tetikus
  - MOUSE_BUTTON_LEFT
  - MOUSE_BUTTON_MIDDLE
  - MOUSE_BUTTON_RIGHT

- Teks
  - FONT_PLAIN
  - FONT_BOLD
  - FONT_ITALIC

#### Fungsi

(Init Window)
```java
- initgraph(String title)
- initgraph(String title, int width, int height)
- initwindow(String title, int x, int y, int width, int height)
```
(Control Window)
```java
- cleargraph()
- refreshgraph()
- closegraph()
- setFrameRate(int fps)
- int getwindowwidth()
- int getwindowheight()
- delay(long msec)
- setbkcolor(int color)
- getbkcolor()
- setcolor(int color)
- setcolor(int color, int alpha)
- int getcolor()
- setstrokewidth(float w)
- setantialias(boolean a)
```
(Shape Graphics)
```java
- line(int x1, int y1, int x2, int y2)
- putpixel(int x, int y, int color)
- arc(int x, int y, int start, int end, int radius)
- pieslice(int x, int y, int start, int end, int radius)
- sector(int x, int y, int start, int end, int xradius, int yradius)
- rectangle(int x1, int y1, int x2, int y2)
- bar(int x1, int y1, int x2, int y2)
- bar3d(int x1, int y1, int x2, int y2, int depth, boolean topflag)
- ellipse(int x, int y, int w, int h)
- fillellipse(int x, int y, int w, int h)
- circle(int cx, int cy, int r)
- drawpoly(int num, int[] points)
- fillpoly(int num, int[] points)
- triangle(int x1, int y1, int x2, int y2, int x3, int y3)
- filltriangle(int x1, int y1, int x2, int y2, int x3, int y3)
```
(Images)
```java
- BufferedImage loadimage(String path)
- BufferedImage loadimage(File file)
- drawimagepixels(BufferedImage img, int px, int py)
- putimage(BufferedImage img, int x, int y)
- BufferedImage createimage(int w, int h)
- saveframe(String path)
```
(Text)
```java
- outtextxy(String text, int x, int y)
- settextfss(String name, int style, int size)
- settextfont(String name)
- settextstyle(int style)
- settextsize(int size)
```
(Others)
```java
- int getpixel(int x, int y)
- floodfill(int x, int y, int newcolor)
```
(Event Handler)
```java
- boolean iskeydown(int key)
- boolean iskeypressed(int key)
- boolean iskeyreleased(int key)
- boolean isKB()
- char getch()

- boolean ismousedown(int button)
- boolean ismousepressed(int button)
- boolean ismousereleased(int button)
- int getmousex()
- int getmousey()
```
---

## Cara Guna

### 1. Masukkan perpustakaan
```java
import com.clay.gsl.Graphics;
```

### 2. Contoh Demo.java Asas
```java
import static com.clay.gsl.Graphics.*;

public class Demo {
    public static void main(String[] args) {

        initgraph("Demo GSL");

        while (true) {
            cleargraph();
            // Write code event here

            // Write code shape here

            refreshgraph();
        }
    }
}
```

### Contoh BouncingBall.java
```java
package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class BouncingBall {
    public static void main(String[] args) {
        initgraph("Bouncing Ball Game");

        int x = 100, y = 100;
        int dx = 2, dy = 2;
        int radius = 30;

        while (true) {
            cleargraph();
            // gerakkan bola
            x += dx;
            y += dy;

            // pantulan dinding
            if (x < 0 || x + radius> getwindowwidth()) dx = -dx;
            if (y < 0 || y + radius> getwindowheight()) dy = -dy;

            // lukis bola
            setcolor(RED);
            fillellipse(x, y, radius, radius);

            // tunjuk teks
            setcolor(WHITE);
            outtextxy("Press ESC to exit", 10, 20);

            // input keluar
            if (iskeydown(KEY_ESCAPE)) closegraph();
            refreshgraph();
            delay(5);
        }
    }
}
```

### Demo BouncingBall

![Demo](github-res/demo.gif)

