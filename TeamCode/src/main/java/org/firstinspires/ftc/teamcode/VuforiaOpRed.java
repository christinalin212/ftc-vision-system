package org.firstinspires.ftc.teamcode;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.*;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;

import Team4278.Utils.MotorGroup;

@Autonomous(name = "AutomodeRed")
/**
 * Created by William on 9/26/2016.
 */

@Disabled
public class VuforiaOpRed extends LinearOpMode {
    private CHardwareMap robot = new CHardwareMap();
    VuforiaLocalizer vuforia;
    Boolean comp = false;
    Boolean first = true;

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AVktbtv/////AAAAGY1koTqeTUyKsH17S4sxg5FdzjlL4sab4r1TteImHLQZaxsQP96TVimg0LSECJMSTY" +
                "/hMmyl4Ko8WqEFHdESFWl5CNgqDIkVJsLD4ivpj1OAwtHu6z1Me1lnhV/DlBshYL9nsfqWCvVyPPpMkYBj3DRGGI" +
                "6OHwD29CokKIxnknH8sV/k8xdVFSAmsRqBney+t4+c7vmUw39q7qrsE63Pf6wnFxYLkDz4uFvjy3IHbX3/OLojTN" +
                "Gk4/sHOWnME0c8EEVXUZAoXPM/7jJK/ksBrYMPyJTZOeMPhcTMtjsPNMVx54p5yICLcIGjqPwTih1Z88RGDGKIuY" +
                "vIrnSMjUnJNZtshuuqadeAXk2HyGS6DR3K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        VuforiaTrackables Images = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");

        VuforiaTrackable Wheels = Images.get(0);
        Wheels.setName("WheelsImage");  // Wheel Images, Blue #1

        VuforiaTrackable Tools = Images.get(1);
        Tools.setName("ToolsImage");  // Tool Images, Red #2

        VuforiaTrackable Legos = Images.get(2);
        Legos.setName("LegosImage");  // Lego Images, Blue #2

        VuforiaTrackable Gears = Images.get(3);
        Gears.setName("GearsImage");  // Gear Images, Red #1

        ArrayList<VuforiaTrackable> RedImages = new ArrayList<VuforiaTrackable>();
        RedImages.add(Gears);
        RedImages.add(Tools);
        MotorGroup RightMotors = new MotorGroup(robot.RM1, robot.RM2);
        MotorGroup LeftMotors = new MotorGroup(robot.LM1, robot.LM2);
        waitForStart();

        Images.activate();
        while (opModeIsActive()) {
            for (VuforiaTrackable img : RedImages) {
                comp = false;
                if(first){
                    robot.driveStraightForward(47,RightMotors,LeftMotors);
                    robot.turn(CHardwareMap.Side.LEFT,90,RightMotors,LeftMotors);
                }else{
                    robot.driveStraightBackwards(20,RightMotors,LeftMotors);
                    robot.turn(CHardwareMap.Side.RIGHT,40,RightMotors,LeftMotors);
                }
                while (!comp) {
                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) img.getListener()).getPose();
                    if (pose != null) {
                        VectorF translation = pose.getTranslation();
                        telemetry.addData(img.getName() + "translation", translation);
                        //This is for vertical phones
                        //double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(1)));
                        //This is for horizontal phones
                        double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(2),translation.get(1)))+90.0;
                        telemetry.addData(img.getName() + "Degrees", degreesToTurn);
                        double distance = Math.sqrt(Math.pow(translation.get(1), 2) + Math.pow(translation.get(2), 2));
                        telemetry.addData(img.getName() + "Distance", distance);
                        telemetry.update();
                        if (degreesToTurn > 1) {
                            robot.turn(CHardwareMap.Side.RIGHT, 10, RightMotors, LeftMotors);
                        } else if (degreesToTurn < -1) {
                            robot.turn(CHardwareMap.Side.LEFT, 10, RightMotors, LeftMotors);
                        } else {
                            robot.driveStraightForward(10,RightMotors,LeftMotors);
                        }
                        if(distance < 100){
                            comp = true;
                            first = false;
                            //Execute push button code
                        }
                    }
                }
            }
        }
        telemetry.update();
        idle();

    }

}
