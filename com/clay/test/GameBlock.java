package com.clay.test;

import static com.clay.gsl.Graphics.*;

public class GameBlock {
    public static void main(String[] args) {
        // Buka window
        initgraph("GSL Game - Elak Halangan");

        int ballX = 100;
        int ballY = 300;
        int radius = 50;

        int obstacleX = 800;
        int obstacleY = 300;
        int obstacleW = 40;
        int obstacleH = 120;

        boolean running = true;

        while (running) {
            cleargraph();

            // Kawalan keyboard (atas bawah)
            if (iskeydown(KEY_UP)) ballY -= 20;
            if (iskeydown(KEY_DOWN)) ballY += 20;
            if (iskeydown(KEY_ESCAPE)) running = false; // ESC untuk keluar

            // Halangan bergerak ke kiri
            obstacleX -= 5;
            if (obstacleX < -obstacleW) {
                obstacleX = 800;
                obstacleY = 100 + (int) (Math.random() * 400);
            }

            // Lukis bola
            setcolor(BLUE);
            fillellipse(ballX, ballY, radius, radius);

            // Lukis halangan
            setcolor(RED);
            bar(obstacleX, obstacleY, obstacleX + obstacleW, obstacleY + obstacleH);

            // Semak perlanggaran
            if (ballX + radius > obstacleX && ballX - radius < obstacleX + obstacleW && ballY + radius > obstacleY && ballY - radius < obstacleY + obstacleH) {
                while (true) {
                    cleargraph();
                    setcolor(WHITE);
                    outtextxy(350, 250, "GAME OVER!");
                    refreshgraph();
                    delay(2000);
                    break;
                }
                running = false;
            }

            refreshgraph();
            delay(16);
        }

        cleargraph();
        setcolor(RED);
        outtextxy(350, 250, "GAME OVER!");
        refreshgraph();

        delay(3000);

        closegraph();
    }
}
