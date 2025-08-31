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
- 📐 Bentuk Asas: `line`, `rect`, `circle`, `ellipse`, `arc`, `polygon`, `point`  
- 🔤 Teks: `text(...)`  
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

## Contoh BouncingBall.java
