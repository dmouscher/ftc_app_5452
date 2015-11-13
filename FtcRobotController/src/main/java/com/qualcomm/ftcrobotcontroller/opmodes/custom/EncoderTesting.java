//This op mode is used to test motor encoders

package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class EncoderTesting extends OpMode {
     DcMotor testmotor1, testmotor2;
     DcMotorController DMC;
     double finalEncoderValue;
     double currentEncoderValue;
     boolean firstloop = true;
     double power = 0;

    public EncoderTesting()
    {
        finalEncoderValue = 0;
        currentEncoderValue = 0;
    }

    @Override
     public void init()
     {
         hardwareMap.logDevices();
         testmotor1 = hardwareMap.dcMotor.get("motor1");
         testmotor2 = hardwareMap.dcMotor.get("motor2");
         testmotor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
     }

    @Override
    public void loop()
    {
        /**
         * While channel modes are usually meant to be set in the init() function, setting this there didn't seem to to work for some reason.
         * So we set it here in the loop function, but just once so not to waste what ever possessing time it takes up.
         *
         * I would like to credit Brendan from team 3785 for finding this fix. May we someday hope the FTC Software Wizards patch it.
         */
        if(firstloop)
        {
            testmotor1.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            firstloop = false;
        }

        testmotor1.setTargetPosition(1440); // Setting the desired position

        /**
         * So from here until the end of the loop method, this code is meant for a very specific scenario but might be useful for later reference
         * The scenario is that we want to run a second motor, without an encoder at the same speed as the first (The first motor has an encoder).
         * And we are using the older motor controllers that used I2C and none of this fancy USB reading and writing at the same time.
         * This last part is more of a programming exercise to better understand the new system.
         *
         * If this code is unneeded, comment out the lines until the end of the void method and anything to do with testmotor1
         */
        if(DMC.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) // The first comparison of the state of the device.
        {                                                                                 // This is done so an error is not thrown
            testmotor2.setPower(power);                                                   // and we can do the things we need.
            testmotor1.setPower(1);

            DMC.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);     // Setting it to the other state to make sure that
        }                                                                                 // when we want to read the power that
                                                                                          // the first motor is set to, we can.
        else if(DMC.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY)
        {
            power = testmotor1.getPower();

            DMC.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        }

        //telemetry.addData(String.toString(DMC.getMotorControllerDeviceMode()));
    }

    @Override
    public void stop()
    {

    }
}
