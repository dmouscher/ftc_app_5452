// SSPP: Stay still; protract plow    // Our naming conventions are really unwise

package com.qualcomm.ftcrobotcontroller.opmodes;

public class SSPP extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initalize();
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		movePlow(0.75, 7000);
		Thread.sleep(5000);
	}
}