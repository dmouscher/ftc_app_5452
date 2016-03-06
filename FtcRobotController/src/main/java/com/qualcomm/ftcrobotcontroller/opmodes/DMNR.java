// DMNR: Drive to Mountain, Near, Red
// Drives partially up the nearest mountain zone.
// The robot starts out facing the zones at a 45 degree angle from the wall and centered on the border between the 3rd and 4th tiles (from the left.)

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
        moveTarget((int)(42*IN /* 12 ft times the distance you want.*/), 0.8, 5000);
        turn(-135, 0.8, 3000); /* Make sure this turns left */
        moveTarget((int)(18*IN /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 3000);
    }
}
