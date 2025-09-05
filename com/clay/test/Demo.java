package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class Demo {
    public static void main(String[] args) {

        initgraph("Demo Graphics", 800, 600);

        while (true) {
            cleargraph();

            if (iskeydown(KEY_ESCAPE)) {
                closegraph();
            }

            setcolor(WHITE);
            outtextxy(0, 0, "Press 'ESC' to exit");

            // Tetapkan warna merah 100% opaque
            setcolor(0xFF0000, 255);
            line(50, 50, 200, 50);

            // Hijau dengan alpha 128
            setcolor(0x00FF00, 128);
            rectangle(100, 100, 200, 150);

            // Biru penuh
            setcolor(0x0000FF, 255);
            circle(400, 200, 80);

            // Ungu arc
            setcolor(0x8800FF, 200);
            arc(400, 400, 100, 50, 100);

            // Hitam dot
            putpixel(600, 100, 0xFF000000);

            // Orange ellipse
            setcolor(0xFFA500, 200);
            ellipse(600, 300, 100, 60);

            // Polygon bentuk bintang
            int[] pts = {100,400, 140,460, 200,460, 150,500, 170,560, 100,520, 30,560, 50,500, 0,460, 60,460};
            setcolor(0xFFD700, 255);
            drawpoly(pts.length/2, pts);

            refreshgraph();
        }

        // Biarkan window terbuka

    }
}
