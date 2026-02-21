# Graphics Simple Library (GSL)

**GSL (Graphics Simple Library)** ialah perpustakaan grafik ringkas dalam **Java** yang direka untuk memberi pengalaman mudah seperti **WinBGI**, tetapi lebih moden dan **cross-platform** (Windows, Linux, macOS). Dibina berasaskan `JFrame`, `Canvas` dan `BufferStrategy` yang terinspirasi daripada **Borland Graphics Interface (BGI)**. Tujuan utama GSL adalah untuk membantu pelajar, pengajar, dan pembangun membuat **projek latihan grafik, animasi ringkas, atau mini game 2D** tanpa memerlukan perpustakaan tambahan.

Direka khas untuk:
- Projek latihan
- Demo grafik
- Mini game 2D
- Tanpa memerlukan perpustakaan luaran.

Dapat dijalankan pada OS:
- **Windows** (sudah di cuba)
- **Linux** (sudah di cuba)
- **MacOS** (belum di cuba)

---

## 🎯 Ciri Utama
- 🚀 **Cross-platform** – Berjalan di Windows, Linux, dan macOS (selagi ada Java Runtime).  
- 🎨 **Warna standard tersedia** (`RED`, `GREEN`, `BLUE`, dsb.).  
- ✏️ **Fungsi asas grafik**: `circle()`, `line()`, `rectangle()`, `putpixel()`, `setcolor()`.  
- 🖥️ **Kawalan paparan**: `initgraph()`, `cleargraph()`, `delay()`.  
- 🎮 **Input**: sokongan papan kekunci & tetikus (`isKB()`, `getch()`, klik mouse).  
- ⚡ **Ringkas & ringan** – Sesuai untuk pembelajaran dan projek kecil.  

---

## 📦 Pemasangan
1. Pastikan **Java JDK 8+** telah dipasang.  
2. Tambah fail `Graphics.java` atau `Graphics.jar` dalam projek anda.  
3. Import GSL menggunakan:


```java
import static com.clay.gsl.Graphics.*;
```
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
  - DEFAULT_FONT
  - TRIPLEX_FONT
  - SMALL_FONT
  - SANS_SERIF_FONT
  - GOTHIC_FONT
  - SCRIPT_FONT
  - SIMPLEX_FONT
  - TRIPLEX_SCR_FONT
  - COMPLEX_FONT

- Arah Teks
  - HORIZ_DIR
  - VERT_DIR

- Justify Teks
  - Horizontal: LEFT_TEXT, CENTER_TEXT, RIGHT_TEXT
  - Vertical: TOP_TEXT, BOTTOM_TEXT

- Gaya Garisan
  - SOLID_LINE
  - DOTTED_LINE
  - DASHED_LINE
  - USERBIT_LINE
  - NORM_WIDTH
  - THICK_WIDTH

- Gaya Isi
  - SOLID_FILL
  - LINE_FILL
  - HATCH_FILL
  - XHATCH_FILL
  - WIDE_DOT_FILL
  - INTERLEAVE_FILL

#### Fungsi

**(Window & Frame)**
```java
- void initgraph(String title)
- void initgraph(int width, int height, String title)
- void initwindow(int width, int height, String title, int x, int y)
- void closegraph()
- void cleargraph()
- void refreshgraph()
- void setframerate(int fps)
- int getwindowwidth()
- int getwindowheight()
```
**(Warna & Palette)**
```java
- void setbkcolor(int color)
- int getbkcolor()
- void setcolor(int color)
- void setcolor(int color, int alpha)
- int getcolor()
- int getmaxcolor()
- void setpalette(int idx, int argb)
- int getpalette(int idx)
- void setrgbpalette(int idx, int red, int green, int blue)
```
**(Shape Graphics)**
```java
- void line(int x1, int y1, int x2, int y2)
- void moveto(int x, int y)
- void moverel(int dx, int dy)
- void lineto(int x, int y)
- void linerel(int dx, int dy)
- void putpixel(int x, int y, int color)
- void arc(int x, int y, int start, int end, int radius)
- void pieslice(int x, int y, int start, int end, int radius)
- void sector(int x, int y, int start, int end, int xradius, int yradius)
- void rectangle(int x1, int y1, int x2, int y2)
- void bar(int x1, int y1, int x2, int y2)
- void bar3d(int x1, int y1, int x2, int y2, int depth, boolean topflag)
- void ellipse(int x, int y, int w, int h)
- void fillellipse(int x, int y, int w, int h)
- void circle(int cx, int cy, int r)
- void drawpoly(int num, int[] points)
- void fillpoly(int num, int[] points)
- void triangle(int x1, int y1, int x2, int y2, int x3, int y3)
- void filltriangle(int x1, int y1, int x2, int y2, int x3, int y3)
```
**(Images)**
```java
- BufferedImage loadimage(String path)
- BufferedImage loadimage(File file)
- void drawimagepixels(BufferedImage img, int px, int py)
- void putimage(BufferedImage img, int x, int y)
- BufferedImage createimage(int w, int h)
- void saveframe(String path)
```
**(Text)**
```java
- void outtextxy(String text, int x, int y)
- void outtext(String text)
- void settextstyle(int font, int direction, int charsize)
```
**(Others)**
```java
- int getpixel(int x, int y)
- void floodfill(int x, int y, int newcolor)
```
**(Event Handler)**
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
### 3. Jalankan program dalam perintah baris
### 🐧 Linux
```
java -cp .:GSL.jar Demo
```
### 🪟 Windows
```
java -cp .;GSL.jar Demo
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

