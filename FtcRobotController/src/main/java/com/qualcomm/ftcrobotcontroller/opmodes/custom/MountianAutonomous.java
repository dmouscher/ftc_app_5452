package com.qualcomm.ftcrobotcontroller.opmodes.custom;


        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.DcMotorController;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.robocol.Telemetry;
        import com.qualcomm.robotcore.util.Range;
        import java.lang.Math;

/**
 * Created by jackiehirsch on 1/25/16.
 */
public class MountianAutonomous /* Blue */extends LinearBase {

        @Override
        public void runOpMode() throws InterruptedException
        {

                mapHardware();

                driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

                driveLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                driveRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);


                waitForStart();

                moveForward((int) (FT * 0.45 /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
                turn(90, 0.8, 1000); /* Make sure this turns right */
                moveForward((int) (FT * 0.45 /* 12 ft times the distance you want. Also used 12 to turn inches into feet. */), 0.8, 1000);
        }
}
