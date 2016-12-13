package org.firstinspires.ftc.teamcode;

/**
 * Created by William on 10/5/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Teleop OP")
@Disabled
public class Teleop_Class extends LinearOpMode {
    CHardwareMap Robot = new CHardwareMap();
    @Override
    public void runOpMode() throws InterruptedException{
        Robot.init(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            double left  = gamepad1.left_stick_y + gamepad1.left_stick_x;
            double right = gamepad1.left_stick_y - gamepad1.left_stick_x;
            double max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }
            Robot.RM1.setPower(right);
            Robot.RM2.setPower(right);
            Robot.LM1.setPower(left);
            Robot.LM2.setPower(left);
        }
    }
}
