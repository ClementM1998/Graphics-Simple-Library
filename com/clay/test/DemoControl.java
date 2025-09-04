package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class DemoControl {
    public static void main(String[] args) {
        int x = 400;
        int y = 300;

        initgraph("Demo Control");

        while (true) {
            cleargraph();

            char k = getch();

            if (iskeydown(KEY_ESCAPE)) closegraph();
            if (k == 'q') closegraph();

            if (iskeydown(KEY_LEFT)) x -= 2;
            if (iskeydown(KEY_UP)) y -= 2;
            if (iskeydown(KEY_RIGHT)) x += 2;
            if (iskeydown(KEY_DOWN)) y += 2;

            if (k == 'a') x -= 2;
            if (k == 'w') y -= 2;
            if (k == 'd') x += 2;
            if (k == 's') y += 2;

            setcolor(RED);
            circle(x, y, 40);

            refreshgraph();
        }
    }
}
