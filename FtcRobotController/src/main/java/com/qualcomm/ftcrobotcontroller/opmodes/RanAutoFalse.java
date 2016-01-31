// Sets Global.ranAutonomous to false in case a manual change is needed

package com.qualcomm.ftcrobotcontroller.opmodes;

public class RanAutoFalse extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		waitForStart();
		com.qualcomm.ftcrobotcontroller.opmodes.Global.ranAutonomous = false;
	}
}