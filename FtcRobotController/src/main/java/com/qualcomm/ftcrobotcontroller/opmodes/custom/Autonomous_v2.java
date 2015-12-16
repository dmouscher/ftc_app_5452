package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by jackiehirsch on 12/14/15.
 */
public class Autonomous_v2 extends LinearOpMode {

    enum Direction { LEFT, RIGHT; }

    DcMotor driveLeft;
    DcMotor driveRight;

    DcMotor armRotate;
    DcMotor armExtend;

    DcMotor plow;

    Servo dropperBase;
    Servo dropperJoint;
    Servo rescueLeft;
    Servo rescueRight;

    @Override
    public void runOpMode() throws InterruptedException
    {
        System.exit(0);
        driveLeft  = hardwareMap.dcMotor.get("left" );
        driveRight = hardwareMap.dcMotor.get("right");

        armRotate = hardwareMap.dcMotor.get("rotate");
        armExtend = hardwareMap.dcMotor.get("extend");

        plow = hardwareMap.dcMotor.get("plow");

        dropperBase  = hardwareMap.servo.get("base" );
        dropperJoint = hardwareMap.servo.get("joint");
        rescueLeft   = hardwareMap.servo.get("rql"  );
        rescueRight  = hardwareMap.servo.get("rqr"  );

        driveRight.setDirection(DcMotor.Direction.REVERSE);
        rescueRight.setDirection(Servo.Direction.REVERSE);

        driveLeft .setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        dropperBase .setPosition(0.25);
        dropperJoint.setPosition(1.00);

        driveRight.setTargetPosition(1440);
        driveLeft.setTargetPosition(1440);

        driveLeft.setPower(0.8);
        driveRight.setPower(0.8);


    }
}
