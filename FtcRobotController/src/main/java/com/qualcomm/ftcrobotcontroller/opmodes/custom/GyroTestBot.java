// Op mode used for testing the gyro sensor
// Warning: As this is a testing op mode, the code is very messy

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by mlowery2 on 12/16/2015.
 */
public class GyroTestBot extends LinearOpMode
{
    DcMotor driveLeft;
    DcMotor driveRight;

    DcMotor armRotate;
    DcMotor armExtend;

    DcMotor plow;

    Servo dropperBase;

    Servo rescueLeft;
    Servo rescueRight;

    GyroSensor gyro;

    final double TICKS_PER_DEGREE       = 2900/90.0 ; // Make sure we test these along with testing this program idk if we'll get to do it today
    final double TICKS_PER_INCH         = 1000/6.375;

    @Override
    public void runOpMode() throws InterruptedException {
        driveLeft = hardwareMap.dcMotor.get("left");
        driveRight = hardwareMap.dcMotor.get("right");

        armRotate = hardwareMap.dcMotor.get("rotate");
        armExtend = hardwareMap.dcMotor.get("extend");

        plow = hardwareMap.dcMotor.get("plow");

        dropperBase = hardwareMap.servo.get("base");

        rescueLeft = hardwareMap.servo.get("rql");
        rescueRight = hardwareMap.servo.get("rqr");

        gyro = hardwareMap.gyroSensor.get("gyro"); // Make sure to name the gyro "gyro" in the hardware controller
        gyro.calibrate();

        driveRight.setDirection(DcMotor.Direction.REVERSE);
        rescueRight.setDirection(Servo.Direction.REVERSE);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        while(gyro.isCalibrating()){Thread.sleep(50);} // I put this here so that we know that the gyro is done calibrating
                                                       // if the servos set to their starting position
        dropperBase.setPosition(0.25);

        waitForStart();

        telemetry.addData("Phase 0","");
        turn(90, 0.8);
        telemetry.addData("Phase 4", "");
    }

    public void turn(int deg, double speed) throws InterruptedException // + vals, right
    {                                                                   // NO VALUES BIGGER THAN 360
        //telemetry.addData("Phase 1", "");
        resetGyro();
        //gyro.resetZAxisIntegrator(); // Not sure if this will work
		deg *= 0.9; // Idk im just trying stuff here

        if(deg > 0) // If deg is positive
        {
            //telemetry.addData("Phase 2+", "");
			driveLeft.setPower(speed*-1);
			driveRight.setPower(speed);
            do
            {
                telemetry.addData("H: ", gyro.getHeading());
                //waitOneFullHardwareCycle();
            }while(gyro.getHeading() < deg || gyro.getHeading() > 350); // 350 is a placeholder value because I haven't found
        }                                                               // an efficient way to make the gyro reset in a time efficient manner

        else
        {
            //telemetry.addData("Phase 2-","");
            driveLeft.setPower(speed);
            driveRight.setPower(speed*-1);
            do
            {
                telemetry.addData("H: ", gyro.getHeading());
            }while(gyro.getHeading() > 360-deg || gyro.getHeading() == 0);
        }
        //telemetry.addData("Phase 3", "");
        //halt();
    }

    public void halt() // break and stop were taken
    {
        do {
            driveLeft.setPower(0);
            driveRight.setPower(0);
        }while(driveLeft.getPower() != 0 || driveRight.getPower() != 0);
    }

    public void resetGyro() throws InterruptedException
    {
        if(gyro.getHeading() != 0) //  can turn the if to a while in the case that this is not seeming to work
        {
            gyro.calibrate();
            while(gyro.isCalibrating()) {Thread.sleep(50);}
        }
    }
}