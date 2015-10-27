package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class EncoderTesting extends OpMode {
    public EncoderTesting(){}
     DcMotor testmotor;

    @Override
     public void init()
     {
         hardwareMap.logDevices();
         testmotor = hardwareMap.dcMotor.get("test");
         testmotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
         testmotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
     }

    @Override
    public void start()
    {
    }

    @Override
    public void loop()
    {
        telemetry.addData("", testmotor.getCurrentPosition());
        if(testmotor.getCurrentPosition() <= 1440)
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

    }
}
