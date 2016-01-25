package com.qualcomm.ftcrobotcontroller.opmodes.custom;


        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.DcMotorController;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.robocol.Telemetry;
        import com.qualcomm.robotcore.util.Range;
        import java.lang.Math;

/**
 * Created by jackiehirsch on 1/11/16.
 */
public class Bnear extends LinearOpMode {
    DcMotor driveLeft;
    DcMotor driveRight;

    DcMotor armRotate;
    DcMotor armExtend;

    DcMotor plow;

    Servo dropperBase;

    Servo rescueLeft;
    Servo rescueRight;

    final double DEG       = 2900/90.0;
    final double IN         = 144.796;
    final double FT         = 12*IN;
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

        driveRight.setDirection(DcMotor.Direction.REVERSE);
        rescueRight.setDirection(Servo.Direction.REVERSE);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        dropperBase.setPosition(0.25);

        waitForStart();



        moveForward((int) (FT*0.45 /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
        turn(-45, 0.8, 1000); // make sure this turns left
        moveForward((int)(FT*6*Math.sqrt(2)), 0.8, 1000);
        moveForward(-FT, -0.7, 1000);
        turn(-45, 0.8, 1000);
    }

    public void moveForward(double dist, double speed, int waitTime) throws InterruptedException // TODO: Make a system that calculates the amount of time the program should wait based on the input speed and the input distance. Why haven't done this yet? Well I want to get some refrence as to what we are using before trying and guessing
    {
        int idist = (int)dist;
        driveRight.setTargetPosition(driveRight.getCurrentPosition() + idist/**TICKS_PER_INCH*/);
        driveLeft.setTargetPosition(driveLeft.getCurrentPosition() + idist/**TICKS_PER_INCH*/);

        driveLeft .setPower(speed);
        driveRight.setPower(speed);
        Thread.sleep(waitTime);
    }

    public void turn(int deg, double speed, int waitTime) throws InterruptedException
    {
        driveLeft .setTargetPosition(driveLeft.getCurrentPosition() + (int) (-1 * deg * DEG));
        driveRight.setTargetPosition(driveRight.getCurrentPosition() + (int)(deg*DEG));

        driveLeft .setPower(-1 * speed);
        driveRight.setPower(speed);

        Thread.sleep(waitTime);
    }

    public void movePlow(double speed, int waitTime) throws InterruptedException
    {
        plow.setPower(speed);
        Thread.sleep(waitTime);
        plow.setPower(0);
    }

}
