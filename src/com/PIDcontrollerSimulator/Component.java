package com.PIDcontrollerSimulator;

import javax.swing.*;
import java.applet.*;
import java.awt.*;

public class Component extends Applet implements Runnable {

    private static int pixelSize = 2;

    public static Dimension size = new Dimension(640, 320);//(640, 320)//1240, 640
    private static Dimension pixel = new Dimension(size.width / pixelSize, size.height / pixelSize);
    private static boolean isRunning = false;

    private Image screen;

    public static String name = "PID controller simulator";

    public UAV uav;
    public mouseClass mouseClass;

    public Component() {

        setPreferredSize(size);

        uav = new UAV();
        mouseClass = new mouseClass();
        addMouseMotionListener(mouseClass);

    }

    public void start() {

        isRunning = true;
        new Thread(this).start();

    }

    public void stop() {


    }

    private void tick() {

        uav.tick(mouseClass);

    }


    private void render() {

        Graphics2D g = (Graphics2D) screen.getGraphics();

        UAV.render(g, size);
        g = (Graphics2D) getGraphics();
        g.drawImage(screen, 0, 0, size.width, size.height, 0, 0, size.width, size.height, null);
        g.dispose();

    }


    @Override
    public void run() {

        screen = createVolatileImage(pixel.width, pixel.height);

        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 30;
        double delta = 0;

        while (isRunning) {

            long now = System.nanoTime();

            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {

                tick();
                delta--;

            }
            render();
        }
        stop();
    }

    public static void main(String args[]) {

        Component component = new Component();

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.add(component);
        frame.pack();
        frame.setTitle(name);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        component.start();

    }
}
