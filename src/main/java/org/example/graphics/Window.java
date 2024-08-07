package org.example.graphics;

import org.example.interaction.KeyBoard;
import org.example.state.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;


public class Window extends JFrame implements Runnable {

    public static final int WIDTH = 1300;
    public static final int HEIGHT = 800;
    private double delta = 0;
    private final int FPS = 60;
    private int averageFPS = FPS;
    private double TARGETTIME = 1000000000/FPS;

    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics g;
    private Thread thread;
    private boolean running;

    private GameState gameState;
    private KeyBoard keyBoard;

    public Window() {
        setTitle("Space Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        canvas = new Canvas();
        keyBoard = new KeyBoard();

        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(keyBoard);
    }

    public static void main(String[] args) {
        new Window().start();
    }

    private void update() {
        keyBoard.update();
        gameState.update();
    }

    private void draw() {
        bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, WIDTH, HEIGHT);

        gameState.draw(g);

        g.dispose();
        bs.show();
    }

    @Override
    public void run() {

        long now = 0;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;

        init();

        while (running) {

            now = System.nanoTime();
            delta += (now - lastTime) / TARGETTIME;
            time += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                update();
                draw();
                delta--;
                frames++;
               // System.out.println(frames);
            }

            if (time >= 1000000000) {
                averageFPS = frames;
                frames = 0;
                time = 0;
            }

        }

        stop();
    }

    private void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() {
        try {
            Assets.init();
            gameState = new GameState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}