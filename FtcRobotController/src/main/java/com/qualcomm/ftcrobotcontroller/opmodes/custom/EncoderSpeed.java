package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

public class EncoderSpeed extends Thread {

    public DcMotor motorFL, motorFR, motorBL, motorBR;
    public double speedFL, speedFR, speedBL, speedBR = 0;
    public double lastSpeedFL, lastSpeedFR, lastSpeedBL, lastSpeedBR = 0;
    public enum motorList {motorFL, motorFR, motorBL, motorBR};
    public boolean dontStop = true;

    public EncoderSpeed(DcMotor motorFL, DcMotor motorFR, DcMotor motorBL, DcMotor motorBR) // parametrized constructor
    {
        this.motorFL = motorFL;
        this.motorFR = motorFR;
        this.motorBL = motorBL;
        this.motorBR = motorBR;
    }

    public void terminate() // stops the thread
    {
        dontStop = false;
    }

    public double getRealSpeed(motorList motor)
    {
        switch (motor){
            case motorFL:
                return speedFL;
            case motorFR:
                return speedFR;
            case motorBL:
                return speedBL;
            case motorBR:
                return speedBR;
        }

        return 1.1;
        /**
         * It returns this for two reasons:
         * 1 Java is stupid and needs a return statement because the switch statement might not have returned something (but it always will unless someone messed up the code)
         * 2 If someone messed up in the code, the value will be above 1 and motor.setSpeed() will through an error.
         */
    }

    public void run() // The actuall function were motor speed is calculated
    {
        long lastNum = System.currentTimeMillis();

        while(dontStop)
        {
            while(System.currentTimeMillis() == lastNum){} // wait one ms // May cause lag
            lastNum = System.currentTimeMillis();

            speedFL = ((motorFL.getDirection() == DcMotor.Direction.FORWARD)?(1):(-1)*(Math.abs(motorFL.getCurrentPosition()) - Math.abs(lastSpeedFL)))/1000; // The /1000 is to convert from clicks/ms to clicks/s
            speedFR = ((motorFR.getDirection() == DcMotor.Direction.FORWARD)?(1):(-1)*(Math.abs(motorFR.getCurrentPosition()) - Math.abs(lastSpeedFR)))/1000;
            speedBL = ((motorBL.getDirection() == DcMotor.Direction.FORWARD)?(1):(-1)*(Math.abs(motorBL.getCurrentPosition()) - Math.abs(lastSpeedBL)))/1000;
            speedBR = ((motorBR.getDirection() == DcMotor.Direction.FORWARD)?(1):(-1)*(Math.abs(motorBR.getCurrentPosition()) - Math.abs(lastSpeedBR)))/1000;

            lastSpeedFL = speedFL;
            lastSpeedFR = speedFR;
            lastSpeedBL = speedBL;
            lastSpeedBR = speedBR;
        }
    }
}
