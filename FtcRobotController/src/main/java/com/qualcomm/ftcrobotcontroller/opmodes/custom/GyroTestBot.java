package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import java.lang.Math;


/**
 * Created by mlowery2 on 12/16/2015.
 */
public class GyroTestBot extends LinearOpMode {
    DcMotor driveLeft;
    DcMotor driveRight;

    DcMotor armRotate;
    DcMotor armExtend;

    DcMotor plow;

    Servo dropperBase;
    Servo dropperJoint;
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
        dropperJoint = hardwareMap.servo.get("joint");
        rescueLeft = hardwareMap.servo.get("rql");
        rescueRight = hardwareMap.servo.get("rqr");

        gyro = hardwareMap.gyroSensor.get("gyro"); // Make sure to name the gyro "gyro" in the hardware controller
        gyro.calibrate();

        driveRight.setDirection(DcMotor.Direction.REVERSE);
        rescueRight.setDirection(Servo.Direction.REVERSE);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        while(gyro.isCalibrating()){Thread.sleep(50);} // I put this here so that we know that the gyro is done calibrating if the servos set to their starting positions

        dropperBase.setPosition(0.25);
        dropperJoint.setPosition(1.00);

        waitForStart();

        turn(90, 0.8);
    }

    public void turn(int targetDeg, double speed) // + vals, right
    {
        int deg = 0; // the amount of degress turned

        driveRight.setPower(speed*(targetDeg == Math.abs(targetDeg)?-1:1)); // Make sure to test that this turns the right way
        driveLeft.setPower(speed*(targetDeg == Math.abs(targetDeg)?1:-1));

        while(Math.abs(targetDeg) >= Math.abs(deg))
        {
            deg += gyro.rawZ(); // So I have to idea what to put here, so I just quessed. Check telemetry when we run this for the variable the responds to turning the robot.
                                // I'm also assuming that like the Lego gyro, this gyro returns values in degrees per second.

            telemetry.addData("1. x", String.format("%03d", gyro.rawX())); // also try gyro.getRotation()to make sure that we are using the right variable to measure turning.
            telemetry.addData("2. y", String.format("%03d", gyro.rawY())); // but I only think there are four lines for telemetry so I didn't bother adding another line
            telemetry.addData("3. z", String.format("%03d", gyro.rawZ()));
            telemetry.addData("4. h", String.format("%03d", gyro.getHeading()));
        }

        driveLeft.setPower(0);
        driveRight.setPower(0);
    }
}
