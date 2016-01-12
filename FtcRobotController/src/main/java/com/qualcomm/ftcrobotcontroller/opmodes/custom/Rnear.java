package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;
/**
 * Created by jackiehirsch on 1/6/16.
 */
public class Rnear extends LinearOpMode {
        DcMotor driveLeft;
        DcMotor driveRight;

        DcMotor armRotate;
        DcMotor armExtend;

        DcMotor plow;

        Servo dropperBase;
        Servo dropperJoint;
        Servo rescueLeft;
        Servo rescueRight;

        final double TICKS_PER_DEGREE = 2900 / 90.0;
        final double TICKS_PER_INCH = 1000 / 6.375;

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

                driveRight.setDirection(DcMotor.Direction.REVERSE);
                rescueRight.setDirection(Servo.Direction.REVERSE);

                driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

                driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

                dropperBase.setPosition(0.25);
                dropperJoint.setPosition(1.00);

                waitForStart();

                moveForward((int) (TICKS_PER_INCH*12*0.45 /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
                turn((int) (45 * TICKS_PER_DEGREE), 0.8, 1000); // make sure this turns left
                moveForward((int)(TICKS_PER_INCH*12*6*Math.sqrt(2)), 0.8, 1000);
                turn((int) (45 * TICKS_PER_DEGREE), 0.8,1000);
        }

        public void moveForward(int dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
        {
                driveRight.setTargetPosition(driveRight.getCurrentPosition() + dist/**TICKS_PER_INCH*/);
                driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + dist/**TICKS_PER_INCH*/);

                driveLeft.setPower(speed);
                driveRight.setPower(speed);

                Thread.sleep(waitTime);
        }

        public void turn(int deg, double speed, int waitTime) throws InterruptedException {
                driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + (int) -1 * deg );
                driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int) (deg));

                driveLeft.setPower(-1 * speed);
                driveRight.setPower(speed);

                Thread.sleep(waitTime);

        }
}

