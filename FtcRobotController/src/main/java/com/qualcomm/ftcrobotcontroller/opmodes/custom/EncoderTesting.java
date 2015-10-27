package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
     }

    @Override
    public void loop()
    {
        while(testmotor.getCurrentPosition() <= 1440)
        {
            testmotor.setPower(0.25);
            telemetry.addData("", testmotor.getCurrentPosition());
        }
    }

    @Override
    public void stop()
    {

    }
}
