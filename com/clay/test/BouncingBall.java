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
            if (x < 0 || x + radius > getwindowwidth()) dx = -dx;
            if (y < 0 || y + radius > getwindowheight()) dy = -dy;

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
