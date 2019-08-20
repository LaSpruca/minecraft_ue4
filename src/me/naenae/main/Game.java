package me.naenae.main;

import me.naenae.main.game.Circle;
import me.naenae.main.game.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serializable;
import java.util.Random;

public class Game extends Canvas implements Runnable, Serializable {
    private static final long serialVersionUID = 7698194239351869889L;

    public static final int WIDTH = 1000, HEIGHT = WIDTH / 12 * 9;
    private boolean running;
    private Thread thread;
    public Player player = new Player(50, 70);
    private Handler handler = new Handler();
    private Window w;

    public Game() {
        w = new Window(WIDTH, HEIGHT, "Game", this);
        System.out.println("[INIT]Game Starting");
        handler.addGameObject(player);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
        System.out.println("[INIT]Game Thread Started");
    }

    public synchronized void stop() {
        System.out.println("[INIT]Game Thread Stopped");
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0d;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("[*]FPS: " + String.valueOf(frames));
                frames = 0;
            }
        }

        stop();
    }

    private void tick() {
        Random r = new Random();
        handler.tick();
        handler.gameObjects.add(new Player(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
        handler.gameObjects.add(new me.naenae.main.game.Circle(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
        handler.gameObjects.add(new Player(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
        handler.gameObjects.add(new me.naenae.main.game.Circle(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
        handler.gameObjects.add(new Player(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
        handler.gameObjects.add(new me.naenae.main.game.Circle(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)));
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }


        Graphics g = bs.getDrawGraphics();

        Random r = new Random();

        g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        g.drawRect(0, 0, WIDTH - 7, HEIGHT - 30);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String args[]) {
        System.out.println("[INIT]Beginning Launch");
        new Game();
    }
}