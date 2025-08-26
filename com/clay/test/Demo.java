package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class Demo {
    public static void main(String[] args) {

        initgraph("Demo");

        setcolor(RED);

        arc(100, 100, 100, 200, 180, 70);

        circle(0, 0, 50);

        rectangle(0, 0, 50, 50);

    }
}
