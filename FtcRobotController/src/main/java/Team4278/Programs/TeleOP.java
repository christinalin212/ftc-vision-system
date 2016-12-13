package Team4278.Programs;

import com.qualcomm.robotcore.eventloop.opmode.*;
import Team4278.Robots.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by William on 11/2/2016.
 */

@Disabled
@TeleOp(name = "Team4278TeleOP")
public class TeleOP extends LinearOpMode{

    TankDrive Robot = new TankDrive();

    @Override
    public void runOpMode() throws InterruptedException {
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
            Robot.R1Motor.setPower(right);
            Robot.R2Motor.setPower(right);
            Robot.L1Motor.setPower(left);
            Robot.L2Motor.setPower(left);
        }
    }
}
