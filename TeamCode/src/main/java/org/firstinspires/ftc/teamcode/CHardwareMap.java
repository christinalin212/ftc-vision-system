package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import Team4278.Utils.MotorGroup;
import Team4278.Utils.RobotComp;
//hi

public class CHardwareMap {

    int LeftDistance;
    int RightDistance;

    RobotComp Comp = new RobotComp();

    DcMotor RM1;
    DcMotor RM2;
    DcMotor LM1;
    DcMotor LM2;

    public enum Side {
        LEFT, RIGHT
    }

    /* local OpMode members. */
    HardwareMap hwMap;
    private ElapsedTime period = new ElapsedTime();

    //A direction of false is left
    //A direction of true is right
    //direction indicates the direction we want to turn
    //BTW you have to calibrate your own constants, for this project it is 14 inches

    public void turn(Side side, int degrees, MotorGroup Right, MotorGroup Left) {
        Right.resetEncoders();
        Left.resetEncoders();
        if (side.equals(Side.RIGHT)) {
            LeftDistance = Comp.computeTurningPos(true, degrees, false, 14.0);
            RightDistance = Comp.computeTurningPos(true, degrees, true, 14.0);
        } else if (side.equals(Side.LEFT)) {
            LeftDistance = Comp.computeTurningPos(false, degrees, false, 14.0);
            RightDistance = Comp.computeTurningPos(false, degrees, true, 14.0);
        }

        Left.setTargetPosition(LeftDistance);
        Right.setTargetPosition(RightDistance);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setPowers(0.3);
        Right.setPowers(0.3);
        while (Left.isBusy() && Right.isBusy()) {
        }
        Left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Left.setPowers(0);
        Right.setPowers(0);
    }

    public void mToSide(Side side, MotorGroup Right, MotorGroup Left, int percent) {

    }

    //distance is in inches

    public void driveStraightForward(int distance, MotorGroup Right, MotorGroup Left) {
        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setTargetPosition(Comp.computePositionD(Comp.toMeters(distance)));
        Left.setTargetPosition(Comp.computePositionD(Comp.toMeters(distance)));
        Right.setPowers(0.3);
        Left.setPowers(0.3);
        while (Left.isBusy() && Right.isBusy()) {
        }
        Right.setPowers(0);
        Left.setPowers(0);
        Right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveStraightBackwards(double distance, MotorGroup Right, MotorGroup Left) {
        Right.resetEncoders();
        Left.resetEncoders();
        Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right.setTargetPosition(-Comp.computePositionD(Comp.toMeters(distance)));
        Left.setTargetPosition(-Comp.computePositionD(Comp.toMeters(distance)));
        Right.setPowers(0.3);
        Left.setPowers(0.3);
        while (Left.isBusy() && Right.isBusy()) {
        }
        Right.setPowers(0);
        Left.setPowers(0);
        Right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map

        hwMap = ahwMap;

        RM1 = ahwMap.dcMotor.get("RightMotor_1");
        RM2 = ahwMap.dcMotor.get("RightMotor_2");
        LM1 = ahwMap.dcMotor.get("LeftMotor_1");
        LM2 = ahwMap.dcMotor.get("LeftMotor_2");
        // Set all motors to zero power
        RM1.setPower(0);
        RM2.setPower(0);
        LM1.setPower(0);
        LM2.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        LM1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LM2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RM1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RM2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Set the right motors to reversed
        RM1.setDirection(DcMotorSimple.Direction.REVERSE);
        RM2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /***
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs Length of wait cycle in mSec.
     * @throws InterruptedException
     */

    private void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
