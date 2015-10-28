package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class EncoderTesting extends OpMode {
     DcMotor testmotor;
     double finalEncoderValue;
     double currentEncoderValue;

    public EncoderTesting()
    {
        finalEncoderValue = 0;
        currentEncoderValue = 0;
    }

    @Override
     public void init()
     {
         hardwareMap.logDevices();
         testmotor = hardwareMap.dcMotor.get("test");
         //testmotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
         testmotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
     }

    @Override
    public void loop()
    {
        currentEncoderValue = testmotor.getCurrentPosition() - finalEncoderValue; // This is if because the encoder value carrys over between starts and stops of programs. And the only way to fully reset it is to cycle the power. :grin: I think
        telemetry.addData("", currentEncoderValue);
        if(currentEncoderValue <= 1440)
        {
            testmotor.setPower(0.25);
        }
        else
        {
            testmotor.setPower(0);
        }
    }

    @Override
    public void stop()
    {
        finalEncoderValue += testmotor.getCurrentPosition();
    }
}
