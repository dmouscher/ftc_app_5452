package com.qualcomm.ftcrobotcontroller.opmodes.custom;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by jackiehirsch on 1/6/16.
 */
public class Rnear extends LinearBase
{
        @Override
        public void runOpMode() throws InterruptedException
        {
                mapHardware();

                drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

                dropperBase.setPosition(0.25);

                waitForStart();

                moveTarget((int) (IN * 12 * 0.45 /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
                turn((int) (-45 * DEG), 0.8, 1000); // make sure this turns left
                moveTarget((int) (IN * 12 * 6 * Math.sqrt(2)), 0.8, 1000);
                moveTarget(-FT, -0.7, 1000);
                turn((int) (-45 * DEG), 0.8,1000);
        }
}


