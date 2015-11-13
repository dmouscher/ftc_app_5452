
package com.qualcomm.ftcrobotcontroller.opmodes.custom;

		import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
		import com.qualcomm.robotcore.hardware.DcMotor;
		import com.qualcomm.robotcore.hardware.DcMotorController;

public class Jackie_Skeleton extends LinearOpMode
{
	DcMotor motor;
    boolean firstloop = true;

	@Override
	public void runOpMode() throws InterruptedException
	{
		motor = hardwareMap.dcMotor.get("motor");
        //motor.(DcMotorController.RunMode.RESET_ENCODERS); //I've commented this line out because it's throwing a syntax error. --DM

		waitForStart();
		while(opModeIsActive())
		{
			motor.setPower(0.8);
			if(firstloop)
			{
				//motor.(DcMotorController.RunMode.RUN_TO_POSITION); //I've commented this line out because it's throwing a syntax error. --DM
				firstloop = false;
			}

			motor.setTargetPosition(1440);
		}
	}
}