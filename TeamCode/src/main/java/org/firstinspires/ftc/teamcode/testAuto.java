package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.opencv.core.Mat;

import Team4278.Utils.MotorGroup;
import Team4278.Utils.RobotComp;
/**
 * Created by William on 10/19/2016.
 */

@Autonomous(name = "testAuto")

public class testAuto extends LinearOpMode{

    CHardwareMap robot = new CHardwareMap();

    @Override
    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);
        MotorGroup Right = new MotorGroup(robot.RM1,robot.RM2);
        MotorGroup Left = new MotorGroup(robot.LM1,robot.LM2);
        waitForStart();
        robot.turn(CHardwareMap.Side.LEFT, 90 ,Right,Left);
    }
}