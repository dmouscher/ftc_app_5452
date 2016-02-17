package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by mlowery2 on 2/10/2016.
 *
 * Created to try and find what is causing inaccuracies with the gyro.
 */
public class GyroServoTest2 extends LinearOpMode
{
    Servo servo;
    ModernRoboticsI2cGyro gyro;

    public void runOpMode() throws InterruptedException
    {
        servo = hardwareMap.servo.get("servo");
        gyro  = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
		telemetry.addData("Phase: ", 1);

        servo.setPosition(0);
		telemetry.addData("Phase: ", 2);

        gyro.calibrate();
        while(gyro.isCalibrating())waitOneFullHardwareCycle();
		telemetry.addData("Phase: ", 3);

		waitForStart();

        servo.setPosition(0.5);
		telemetry.addData("Phase: ", 4);

        while(opModeIsActive())
        {
            telemetry.addData("Z-Value", gyro.getIntegratedZValue());
            waitOneFullHardwareCycle();
        }
		telemetry.addData("Phase: ", 5);
    }
}
