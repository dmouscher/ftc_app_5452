package com.qualcomm.ftcrobotcontroller.opmodes.custom;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;

/**
 * Created by mlowery2 on 1/6/2016.
 */

/*
0.5 feet forwards
turn 45 deg right
6*sqrt(2) feet forwaars
turn 45 deg right
1 foot forwards
extend climber arm \_______dump
move backwards
 */
public class BFar extends LinearOpMode {
    DcMotor driveLeft;
    DcMotor driveRight;

    DcMotor armRotate;
    DcMotor armExtend;

    DcMotor plow;

    Servo dropperBase;
    Servo dropperJoint;
    Servo rescueLeft;
    Servo rescueRight;

    final double TICKS_PER_DEGREE       = 2900/90.0 ;
    final double TICKS_PER_INCH         = 1000/6.375;

    public void runOpMode() throws InterruptedException
    {
        moveForward((int) (TICKS_PER_DEGREE*12*0.5), 0.8, 1000);
        turn(0.8, 45);
    }
    public void moveForward(int dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
    {
        driveRight.setTargetPosition(driveRight.getCurrentPosition() + dist/**TICKS_PER_INCH*/);
        driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + dist/**TICKS_PER_INCH*/);

        driveLeft .setPower(speed);
        driveRight.setPower(speed);

        Thread.sleep(waitTime);
    }
    public void turn(double speed, int deg) // do this after the gyro is tested
    {

    }
}
