package com.qualcomm.ftcrobotcontroller.opmodes.custom;

/**
 * Created by jackiehirsch on 1/6/16.
 */
public class Rnear {

}

{
@Override
public void runOpMode() throws InterruptedException
        {
        DcMotor driveLeft;
        DcMotor driveRight;

        DcMotor armRotate;
        DcMotor armExtend;

        DcMotor plow;

        Servo dropperBase;
        Servo dropperJoint;
        Servo rescueLeft;
        Servo rescueRight;

        motor = hardwareMap.dcMotor.get("motor");

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitForStart();

        telemetry.addData("Phase 1", "");
        moveForward(1440, 0.8, 1000);

        }
        }
