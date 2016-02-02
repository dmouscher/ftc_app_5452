package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EncoderRobotTest extends com.qualcomm.ftcrobotcontroller.opmodes.LinearBase
{
	ElapsedTime clock = new ElapsedTime();

	//final int     SMOOTH_LENGTH         = 10        ;
	final boolean TELEMETRY             = true      ; //enables/disables telemetry
	final double TICKS_PER_DEGREE       = 2900/90.0 ;
	final double TICKS_PER_INCH         = 1000/6.375;

	//double lastXLeft [] = new double[SMOOTH_LENGTH];
	//double lastXRight[] = new double[SMOOTH_LENGTH];

	public void runOpMode()
	{
		mapHardware();

		drivetrainSetMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		drivetrainSetMode(DcMotorController.RunMode.RUN_TO_POSITION   );

		driveRight.setTargetPosition(1440);
		driveLeft .setTargetPosition(1440);

		telemetry.addData("Encoder Values", driveRight.getCurrentPosition() + ", " + driveLeft.getCurrentPosition());

		driveRight.setPower(0.8);
		driveLeft .setPower(0.8);
	}
}
