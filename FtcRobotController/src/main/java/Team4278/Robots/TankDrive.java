package Team4278.Robots;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import Team4278.Utils.MotorGroup;
import Team4278.Utils.RobotComp;

/**
 * Created by William on 10/21/2016.
 */

public class TankDrive {

    HardwareMap HW;

    public DcMotor R1Motor;
    public DcMotor R2Motor;
    public DcMotor L1Motor;
    public DcMotor L2Motor;

    RobotComp Comp = new RobotComp();

    public enum Direction{
        FORWARD, BACKWARD
    }


    public void turn(boolean direction, int degrees,MotorGroup Right,MotorGroup Left){
        Right.resetEncoders();
        Left.resetEncoders();
        int LeftDistance = Comp.computeTurningPos(direction, degrees,false,14.0);
        int RightDistance = Comp.computeTurningPos(direction, degrees, true, 14.0);
        Left.setTargetPosition(LeftDistance);
        Right.setTargetPosition(RightDistance);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setPowers(0.5);
        Right.setPowers(0.5);
        while(Left.isBusy()&&Right.isBusy()){
        }
        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Left.setPowers(0);
        Right.setPowers(0);
    }

    public void init(HardwareMap Map){
        HW = Map;

        R1Motor = Map.dcMotor.get("R1Motor");
        R2Motor = Map.dcMotor.get("R2Motor");
        L1Motor = Map.dcMotor.get("L1Motor");
        L2Motor = Map.dcMotor.get("L2Motor");

        R1Motor.setPower(0);
        R2Motor.setPower(0);
        L1Motor.setPower(0);
        L2Motor.setPower(0);

        R1Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R2Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        L1Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        L2Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        R1Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        R2Motor.setDirection(DcMotorSimple.Direction.REVERSE);

        //1 side will be reversed, we are assuming that it is the right side

//        L1Motor.setDirection(DcMotorSimple.Direction.REVERSE);
//        L2Motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //Drives Forwards or Backwards

    public void driveDir(Direction direction, double distance, MotorGroup Right, MotorGroup Left){
        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        switch(direction){
            case FORWARD:
                Right.setTargetPosition(Comp.computePositionD(Comp.toMeters(distance)));
                Left.setTargetPosition(Comp.computePositionD(Comp.toMeters(distance)));
                break;
            case BACKWARD:
                Right.setTargetPosition(-Comp.computePositionD(Comp.toMeters(distance)));
                Left.setTargetPosition(-Comp.computePositionD(Comp.toMeters(distance)));
        }
        Right.setPowers(0.3);
        Left.setPowers(0.3);
        while(Right.isBusy()&&Left.isBusy()){}
        Right.setPowers(0);
        Left.setPowers(0);
        Right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
}


    private ElapsedTime period = new ElapsedTime();

    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}