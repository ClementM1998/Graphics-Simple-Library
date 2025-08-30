
Graphics Simple Library (GSL)

GSL ialah perpustakaan grafik ringkas dalam Java yang dibina berasaskan JFrame, Canvas dan BufferStrategy yang terinspirasi
dari Borland Graphics Interface (BGI).
Direka khas untuk projek latihan, demo grafik atau mini game 2D tanpa memerlukan perpustakaan luaran.

Ciri-ciri
* Warna Standard tersedia (RED, GREEN, BLUE, dll)
* Input Keyboard (iskeydown, iskeypressed, iskeyreleased)
* Input Mouse (getmousex, getmousey, dll)
* Bentuk Asas (line, rect, circle, ellipse, arc, polygon, point)
* Teks (text)
* Frame Control (setFrameRate(fps), cleargraph, refreshgraph)
* Anti-aliasing (setstrokewidth, setfont)
* Window Lifecycle (initgraph, closegraph)

Cara Guna

1. Masukkan perpustakaan

import com.clay.gsl.Graphics;

2. Contoh Demo Asas

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
