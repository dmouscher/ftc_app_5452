package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class EncoderTesting extends OpMode {
     DcMotor testmotor, testmotor1;
     double finalEncoderValue;
     double currentEncoderValue;
     boolean firstloop = true;

    public EncoderTesting()
    {
        finalEncoderValue = 0;
        currentEncoderValue = 0;
    }

    @Override
     public void init()
     {
         hardwareMap.logDevices();
         testmotor = hardwareMap.dcMotor.get("motor1");
         testmotor1 = hardwareMap.dcMotor.get("motor2");
         testmotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
     }

    @Override
    public void loop()
    {
        if(firstloop)
        {
            testmotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            firstloop = false;
        }

        testmotor.setTargetPosition(1440);
        testmotor.setPower(1);

        /**
         * So from here untill the end of the loop method, this code is meant for a very specific scenario but might be useful for later reference
         * The scenario is that we want to run a second motor, without an encoder at the same speed as the first.
         * And we are using the older motor controllers that used I2C and none of this fancy USB reading and writing at the same time.
         *
         * If this code is unneeded when using this program, comment out the lines untill the end of the void method and anything to do with testmotor1
         */
        //if(testmotor1.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.values()[4]) // I think this is WRITE_ONLY and 0 is READ_ONLY (according to ftc_app_5452/doc/javadoc/com/qualcomm/robotcore/hardware/DcMotorController.DeviceMode.html)
        //testmotor1.setPower(testmotor.getPower());
    }

    @Override
    public void stop()
    {

    }
}
