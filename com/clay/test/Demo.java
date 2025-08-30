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
            text("Press 'ESC' to exit", 0, 0);

            // Tetapkan warna merah 100% opaque
            setcolor(0xFF0000, 255);
            line(50, 50, 200, 50);

            // Hijau dengan alpha 128
            setcolor(0x00FF00, 128);
            drawrect(100, 100, 200, 150);

            // Biru penuh
            setcolor(0x0000FF, 255);
            drawcircle(400, 200, 80);

            // Ungu arc
            setcolor(0x8800FF, 200);
            drawarc(400, 400, 0, 180, 100, 50);

            // Hitam dot
            setcolor(0x000000, 255);
            point(600, 100);

            // Orange ellipse
            setcolor(0xFFA500, 200);
            drawellipse(600, 300, 100, 60);

            // Polygon bentuk bintang
            int[] pts = {100,400, 140,460, 200,460, 150,500, 170,560, 100,520, 30,560, 50,500, 0,460, 60,460};
            setcolor(0xFFD700, 255);
            drawpolygon(pts.length/2, pts);

            refreshgraph();
        }

        // Biarkan window terbuka

    }
}
