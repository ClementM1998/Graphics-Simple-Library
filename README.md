# 🎨 Graphics Simple Library (GSL)

**GSL** ialah perpustakaan grafik ringkas dalam **Java** yang dibina berasaskan  
`JFrame`, `Canvas` dan `BufferStrategy` yang terinspirasi daripada **Borland Graphics Interface (BGI)**.  

Direka khas untuk:
- Projek latihan  
- Demo grafik  
- Mini game 2D
- Tanpa memerlukan perpustakaan luaran.

---

## ✨ Ciri-ciri

- 🎨 Warna standard tersedia (`RED`, `GREEN`, `BLUE`, dll)  
- ⌨️ Input Keyboard (`iskeydown`, `iskeypressed`, `iskeyreleased`)  
- 🖱 Input Mouse (`getmousex`, `getmousey`, dll)  
- 📐 Bentuk Asas: `line`, `putpixel`, `arc`, `pieslice`, `sector`, `bar`, `bar3`, `ellipse`, `fillellipse`, `circle`, `drawpoly`, `fillpoly`
- 🔤 Teks: `outtextxy(...)`  
- ⚡ Frame Control: `setFrameRate(fps)`, `cleargraph()`, `refreshgraph()`  
- ✨ Anti-aliasing & gaya: `setstrokewidth()`, `setfont()`  
- 🪟 Window Lifecycle: `initgraph()`, `closegraph()`  

---

## 🚀 Cara Guna

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

