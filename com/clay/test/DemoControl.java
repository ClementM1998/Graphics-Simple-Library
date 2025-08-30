package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class DemoControl {
    public static void main(String[] args) {
        int x = 400;
        int y = 300;

        initgraph("Demo Control");

        while (true) {
            cleargraph();

            if (iskeydown(KEY_ESCAPE)) closegraph();

            if (iskeydown(KEY_LEFT)) x -= 2;
            if (iskeydown(KEY_UP)) y -= 2;
            if (iskeydown(KEY_RIGHT)) x += 2;
            if (iskeydown(KEY_DOWN)) y += 2;

            setcolor(RED);
            drawcircle(x, y, 40);

            refreshgraph();
        }
    }
}
