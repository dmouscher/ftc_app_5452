package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

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

        waitForStart();

        while(opModeIsActive())telemetry.addData("Headings", gyro);
    }
}
