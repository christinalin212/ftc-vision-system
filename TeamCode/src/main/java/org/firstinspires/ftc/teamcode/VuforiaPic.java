package org.firstinspires.ftc.teamcode;

import android.graphics.Camera;
import android.hardware.camera2.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.vuforia.*;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;

import Team4278.Utils.MotorGroup;

@Autonomous(name = "VuforiaPic")
/**
 * Created by William on 9/26/2016.
 */

public class VuforiaPic extends LinearOpMode {
    private CHardwareMap robot = new CHardwareMap();
    VuforiaLocalizer vuforia;
    Boolean comp = false;

    //apm
    public Image rgb;
    public State state;
    public Frame myFrame;
    int telemetryCount = 0;
    //apm

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);



        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AVktbtv/////AAAAGY1koTqeTUyKsH17S4sxg5FdzjlL4sab4r1TteImHLQZaxsQP96TVimg0LSECJMSTY" +
                "/hMmyl4Ko8WqEFHdESFWl5CNgqDIkVJsLD4ivpj1OAwtHu6z1Me1lnhV/DlBshYL9nsfqWCvVyPPpMkYBj3DRGGI" +
                "6OHwD29CokKIxnknH8sV/k8xdVFSAmsRqBney+t4+c7vmUw39q7qrsE63Pf6wnFxYLkDz4uFvjy3IHbX3/OLojTN" +
                "Gk4/sHOWnME0c8EEVXUZAoXPM/7jJK/ksBrYMPyJTZOeMPhcTMtjsPNMVx54p5yICLcIGjqPwTih1Z88RGDGKIuY" +
                "vIrnSMjUnJNZtshuuqadeAXk2HyGS6DR3K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        vuforia.setFrameQueueCapacity(6);
        int frameQcapacity = vuforia.getFrameQueueCapacity();
        telemetry.addData("Vuforia frame queue capacity is:", frameQcapacity);
        telemetry.update();
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

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

        ArrayList<VuforiaTrackable> BlueImages = new ArrayList<VuforiaTrackable>();
        BlueImages.add(Wheels);
        BlueImages.add(Legos);
        MotorGroup RightMotors = new MotorGroup(robot.RM1,robot.RM1);
        MotorGroup LeftMotors = new MotorGroup(robot.LM1,robot.LM2);

        telemetry.addData("Motors armed, waiting for start", telemetryCount++);
        telemetry.update();

        waitForStart();

        telemetry.addData("Activating images, creating frame", telemetryCount++);
        telemetry.update();

        Images.activate();

        telemetry.addData("Images activated, entering opModeActive loop", telemetryCount++);
        telemetry.update();

        while (opModeIsActive()) {

            //apm
            if(vuforia.getFrameQueue().peek() != null){
                //myFrame = vuforia.getFrameQueue().remove();
                myFrame = vuforia.getFrameQueue().take();
                telemetry.addData("Removed a frame from the queue:", telemetryCount++);
                telemetry.update();
                sleep(300);

            }
            long num = myFrame.getNumImages();
            telemetry.addData("Number of images in the frame:", num);
            telemetry.update();

            for (int i = 0; i < num; i++){
                if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565){
                    rgb = myFrame.getImage(i);
                    telemetry.addData("got RGB565 frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB888){
                    telemetry.addData("got RGB888 frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.GRAYSCALE){
                    telemetry.addData("got GRAYSCALE frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGBA8888){
                    telemetry.addData("got RGB8888 frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.YUV){
                    telemetry.addData("got YUV frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.UNKNOWN_FORMAT){
                    telemetry.addData("got UNKNOWN_FORMAT frame", i);
                    telemetry.update();
                    sleep(500);

                }
                else if(myFrame.getImage(i).getFormat() == PIXEL_FORMAT.INDEXED){
                    telemetry.addData("got INDEXED frame", i);
                    telemetry.update();
                    sleep(500);

                }
            }

            //color detection code here on the rgb frame

            //apm



            for (VuforiaTrackable img : BlueImages) {
                comp = false;
                while (!comp) {
                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) img.getListener()).getPose();
                    telemetry.addData("pose: ", pose);
                    telemetry.update();
                    if (pose != null) {
                        VectorF translation = pose.getTranslation();
                        telemetry.addData(img.getName() + "translation", translation);
                        //This is for vertical phones
                        double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(1)));
                        //This is for horizontal phones
                        // double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0),translation.get(2)));
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
                            //Execute push button code
                            robot.driveStraightBackwards(10,RightMotors,LeftMotors);
                        }
                    }
                }
            }
        }
        telemetry.update();
        idle();
    }
}
