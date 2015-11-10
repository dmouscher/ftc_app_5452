
package com.qualcomm.ftcrobotcontroller.opmodes.custom;

		import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
		import com.qualcomm.robotcore.hardware.DcMotor;

public class Jackie_Skeleton extends LinearOpMode
{
	DcMotor motor;

	@Override
	public void runOpMode() throws InterruptedException
	{
		motor.hardwareMap.dcMotor.get("motor");
		waitForStart();
		motor.setpower(0.8);

	}
}