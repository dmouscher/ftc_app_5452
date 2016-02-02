// SSSS: Stay still; salute servo    // Our naming conventions are really unwise

package com.qualcomm.ftcrobotcontroller.opmodes;

public class SSSS extends LinearBase
{
	@Override
	public void runOpMode() throws InterruptedException
	{
		mapHardware();
		initalize();
		waitForStart();

		dropperBase.setPosition(BASE_VERTICAL); // Raise up the climber-dropper
		Thread.sleep(5000);
	}
}