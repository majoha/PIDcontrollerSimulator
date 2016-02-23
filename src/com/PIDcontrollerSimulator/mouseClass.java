package com.PIDcontrollerSimulator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class mouseClass implements MouseMotionListener {

    private int mouseX = 0;
    private int mouseY = 0;

    public int getMouseX(){

        return mouseX;

    }

    public int getMouseY(){

        return mouseY;

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();

    }
}
