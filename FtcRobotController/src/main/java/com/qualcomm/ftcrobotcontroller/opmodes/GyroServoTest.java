package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by mlowery2 on 2/10/2016.
 *
 * Created to try and find what is causing inaccuracies with the gyro.
 */
public class GyroServoTest extends LinearOpMode
{
    Servo servo;
    GyroSensor gyro;

    public void runOpMode() throws InterruptedException
    {
        servo = hardwareMap.servo.get("servo");
        gyro  = hardwareMap.gyroSensor.get("gyro");

        servo.setPosition(0);

        gyro.calibrate();
        while(gyro.isCalibrating())waitOneFullHardwareCycle();

        servo.setPosition(0.5);

        while(opModeIsActive())telemetry.addData("Headings", String.format("Target: %d, Current: %d", 90, gyro.getHeading()));
    }
}
