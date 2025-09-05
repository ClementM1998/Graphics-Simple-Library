package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class DemoGetch {
    public static void main(String[] args) {
        initgraph("Demo Getch");
        cleargraph();

        setcolor(RED);
        circle(320, 240, 100);

        setcolor(WHITE);
        outtextxy(250, 450, "Tekan apa-apa key untuk keluar ...");

        while (!isKB()) refreshgraph();

        closegraph();
    }
}
