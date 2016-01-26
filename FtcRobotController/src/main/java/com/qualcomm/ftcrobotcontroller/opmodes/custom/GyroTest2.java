package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import java.lang.Math;

/**
 * Created by mlowery2 on 1/18/2016.
 */
public class GyroTest2 extends LinearOpMode {

    DcMotor left;
    DcMotor right;


    GyroSensor gyro;

    @Override
    public void runOpMode() throws InterruptedException
    {
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        right.setDirection(DcMotor.Direction.REVERSE);

        gyro = hardwareMap.gyroSensor.get("gyro");

        while(gyro.isCalibrating()){Thread.sleep(50);}
        telemetry.addData("G:", "Done Calibrating");

        waitForStart();

        turn(90, 0.4);

        halt();
    }

    public void turn(int deg, double speed) throws InterruptedException
    {
        resetGyro();

        deg *= 0.9;

        if(deg == Math.abs(deg)) // If deg is positive
        {
            telemetry.addData("Turning Right", "");

            left.setPower(speed*-1);
            right.setPower(speed);

            while(gyro.getHeading() < deg || gyro.getHeading() > 355)
            {
                telemetry.addData("H:", gyro.getHeading());
                //waitOneFullHardwareCycle();
            }
        }

        else // untested
        {
            telemetry.addData("Turning Left","");

            left.setPower(speed*-1);
            right.setPower(speed);

            while(gyro.getHeading() > 360-deg)
            {
                telemetry.addData("", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }

        telemetry.addData("M: ", "Done Turning");
    }

    public void resetGyro() throws InterruptedException
    {
        telemetry.addData("G:", "Resetting...");
        gyro.calibrate();
        while(gyro.isCalibrating()){Thread.sleep(50);}
        telemetry.addData("G:", "Done Resetting");
    }

    public void halt()
    {
        do {
            left.setPower(0);
            right.setPower(0);
        }while(left.getPower() != 0 && right.getPower() != 0);
    }
}
