package com.PIDcontrollerSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class UAV {

    private static Image image = null;

    //object variables//
    private static float objectPosX = 0.0f;
    private static float objectPosY = 0.0f;

    private static float objectXVelocity = 0.0f;
    private static float objectYVelocity = 0.0f;

    private static float windSpeed = 0.9f;
    //object variables//

    //PID variables for x//
    private static float pGain = 0.05f;
    private static float iGain = 0.05f;
    private static float dGain = 0.05f;

    private static float pCorrectionX = 0.0f;
    private static float iCorrectionX = 0.0f;
    private static float dCorrectionX = 0.0f;

    private static float slopeX = 0.0f;
    private static float lastErrorX = 0.0f;
    private static float iCumErrorX = 0.0f;
    private static float errorX = 0.0f;
    private static float tempErrorX = 0.0f;
    private static int setPointX = new Random().nextInt(640);
    //PID variables for x//

    //PID variables for y//
    private static float pCorrectionY = 0.0f;
    private static float iCorrectionY = 0.0f;
    private static float dCorrectionY = 0.0f;

    private static float slopeY = 0.0f;
    private static float lastErrorY = 0.0f;
    private static float iCumErrorY = 0.0f;
    private static float errorY = 0.0f;
    private static float tempErrorY = 0.0f;
    private static int setPointY = new Random().nextInt(320);
    //PID variables for y//

    private static boolean noImageFound = false;

    private static float radiusOfSetPoint = 5.0f;

    //text variables//
    private static Font font = new Font("Arial", Font.PLAIN, 12);

    public UAV() {

        try {
            image = ImageIO.read(new File("UAV.png"));
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Can't find the image so using default");
            noImageFound = true;
        }

        objectPosX = new Random().nextInt(640);
        objectPosY = new Random().nextInt(320);

    }

    public void tick(mouseClass MouseClass) {

        objectPosX += objectXVelocity - windSpeed;
        objectPosX = PIDControllerX();

        objectPosY += objectYVelocity - windSpeed;
        objectPosY = PIDControllerY();

        if (distanceToObject()) {

            setPointX = new Random().nextInt(640);
            setPointY = new Random().nextInt(320);
            //setPointX = MouseClass.getMouseX();
            //setPointY = MouseClass.getMouseY();

        }
    }


    public static void render(Graphics g, Dimension size) {

        g.setColor(Color.white);
        g.fillRect(0, 0, size.width, size.height);
        g.setColor(new Color(82, 77, 255));
        g.drawOval(setPointX - (15 / 2), setPointY - (15 / 2), 15, 15);

        if (!noImageFound) {
            int temp = image.getWidth((img, infoflags, x, y, width, height) -> false);
            int uavXPos = (int) (objectPosX - (temp / 2));
            int uavYPos = (int) (objectPosY - (temp / 2));
            g.drawRect(uavXPos, uavYPos, temp + 1, temp);
            g.drawImage(image, uavXPos + 1, uavYPos, null);
        } else {

            g.drawRect((int) objectPosX - (32 / 2), (int) objectPosY - (32 / 2), 32, 32);

        }
        //draw vitals//
        drawText(g);

    }

    private static void drawText(Graphics g) {

        g.setFont(font);
        //display x co-ord//
        g.drawString("SetPointX" + ": " + setPointX, 3, 13);
        g.drawString("objectPosX" + ": " + objectPosX, 3, 25);
        g.drawString("errorX" + ": " + tempErrorX, 3, 37);
        //display x co-ord//

        g.setColor(Color.red);
        //display y co-ord//
        g.drawString("SetPointY" + ": " + setPointY, 3, 52);
        g.drawString("objectPosY" + ": " + objectPosY, 3, 64);
        g.drawString("errorY" + ": " + tempErrorY, 3, 76);
        //display y co-ord//

        g.setColor(Color.green);
        //display PID info//
        g.drawString("pGain" + ": " + pGain, 120, 13);
        g.drawString("iGain" + ": " + iGain, 120, 25);
        g.drawString("dGain" + ": " + dGain, 120, 37);
        //display PID info//

        g.setColor(Color.black);
        g.drawRect(0, 0, 185, 80);

    }

    private static float PIDControllerX() {

        //Proportional//
        errorX = objectPosX - setPointX;
        tempErrorX = errorX;
        pCorrectionX = pGain * errorX;

        //Integral//
        iCumErrorX -= errorX;
        iCorrectionX = iGain * iCumErrorX;

        //Derivative//
        errorX = lastErrorX - errorX;
        dCorrectionX = slopeX * dGain;
        lastErrorX = errorX;

        //give the object its new position
        objectPosX = (int) (pCorrectionX + iCorrectionX + dCorrectionX);
        return objectPosX;
    }//PIDControllerX

    private static float PIDControllerY() {

        //Proportional//
        errorY = objectPosY - setPointY;
        tempErrorY = errorY;
        pCorrectionY = pGain * errorY;

        //Integral//
        iCumErrorY -= errorY;
        iCorrectionY = iGain * iCumErrorY;

        //Derivative//
        errorY = lastErrorY - errorY;
        dCorrectionY = slopeY * dGain;
        lastErrorY = errorY;

        //give the object its new position
        objectPosY = (int) (pCorrectionY + iCorrectionY + dCorrectionY);
        return objectPosY;
    }//PIDControllerX

    private static boolean distanceToObject() {

        double distance = (((setPointX - objectPosX) * (setPointX - objectPosX)) + ((setPointY - objectPosY) * (setPointY - objectPosY)));
        distance = Math.sqrt(distance);

        if (distance <= radiusOfSetPoint) {

            return true;

        } else {

            return false;

        }

    }
}
