// To start a new op mode off of LinearBase, copy this

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

public class DMNR extends LinearBase
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        initialize();
        drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitForStart();

        movePlow(0.75, PLOW_EXTEND_LENGTH);
        moveTarget((int)(42*IN /* 12 ft times the distance you want.*/), 0.8, 1000);
        turn(-135, 0.8, 1000); /* Make sure this turns left */
        moveTarget((int)(18*IN /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
    }
}
