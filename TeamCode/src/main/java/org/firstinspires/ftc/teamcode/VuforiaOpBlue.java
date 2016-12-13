package org.firstinspires.ftc.teamcode;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import com.vuforia.*;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;


import Team4278.Utils.MotorGroup;


@Autonomous(name = "AutomodeBlue")
/**
 * Created by William on 9/26/2016.
 */


// 10/18/16- moves robot (movement code may have some bits commented, but I uncommented most of them), older version of color detection


public class VuforiaOpBlue extends LinearOpMode {
    public CHardwareMap robot = new CHardwareMap();
    VuforiaLocalizer vuforia;
    Boolean comp = false;
    Boolean first = true;




    public Image rgb;
    public State state;
    public Frame myFrame;
    int telemetryCount = 0;
    int c = 0;




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


        ArrayList<VuforiaTrackable> BlueImages = new ArrayList<>();
        BlueImages.add(Wheels);
        BlueImages.add(Legos);


        MotorGroup RightMotors = new MotorGroup(robot.RM1, robot.RM2);
        MotorGroup LeftMotors = new MotorGroup(robot.LM1, robot.LM2);


        waitForStart();


        Images.activate();


        while (opModeIsActive()) {
//            if (vuforia.getFrameQueue().peek() == null){
//                telemetry.addData("peek is null", ":(");
//                telemetry.update();
//            }




//color detection code here on the rgb frame
            ByteBuffer b;
            for (VuforiaTrackable img : BlueImages) {
                comp = false;




                if (first) {
                    robot.driveStraightForward(47, RightMotors, LeftMotors);
                    robot.turn(CHardwareMap.Side.RIGHT, 90, RightMotors, LeftMotors);
                } else {
                    robot.driveStraightBackwards(20, RightMotors, LeftMotors);
                    robot.turn(CHardwareMap.Side.LEFT, 40, RightMotors, LeftMotors);
                }
                while (!comp) {




                    if (vuforia.getFrameQueue().peek() != null) {
                        //telemetry.addData("peek is not null", "lol");
                        //telemetry.update();
                        //myFrame = vuforia.getFrameQueue().remove();
                        myFrame = vuforia.getFrameQueue().take();


                        //telemetry.addData("Removed a frame from the queue:", telemetryCount++);
                        //telemetry.update();
                        sleep(300);


                    }


//            telemetry.addData("finished","conditional");
//             telemetry.update();
                    long num = myFrame.getNumImages();
                    //telemetry.addData("Number of images in the frame:", num);
                    //telemetry.update();




                    for (int i = 0; i < num; i++) {
                        if (myFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                            rgb = myFrame.getImage(i);
                            sleep(500);


                        }
                    }


                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) img.getListener()).getPose();
                    b = rgb.getPixels();
                    Bitmap bitmap = Bitmap.createBitmap(new DisplayMetrics(), rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer(b);
                    c++;
                    saveImageToExternalStorage(bitmap,c);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(180);
                    //return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                    robot.driveStraightForward(10, RightMotors, LeftMotors);
                    //telemetry.addData("height",""+height);
                    float avgR = 0;
                    float avgB = 0;


                    //int heightBuffer = 0;
                    for (int i = 0; i < bitmap.getHeight() / 2; i++) {//values between buffers to make it faster
                        for (int q = 0; q < (bitmap.getWidth() - 0); q += 1) {
                            int pix = bitmap.getPixel(q, i);
                            avgR += Color.red(pix);
                            avgB += Color.blue(pix);
                            //leftcount++; //current 1760
                            //arr[i][q] = Color.red(pix);
                            //arb[i][q] = Color.blue(pix);


                        }
                    }
                    avgR = avgR/((bitmap.getHeight()*bitmap.getWidth()));
                    avgB = avgB/((bitmap.getHeight()*bitmap.getWidth()));


                    float redLoc = 0;
                    float blueLoc = 0;
                    float redcount = 0;
                    float bluecount = 0;
                    float neither = 0;
                    float lowRy = Integer.MAX_VALUE;
                    float avgRy = 0;
                    float lowBy = Integer.MAX_VALUE;
                    float avgBy = 0;
                    float[] hsv = new float[3];
                    for (int i = 0; i < bitmap.getHeight() / 3; i++) {//values between buffers to make it faster
                        for (int q = 0; q < (bitmap.getWidth() - 0); q += 1) {
                            int pix = bitmap.getPixel(q, i);
                            Color.colorToHSV(pix,hsv);
                            if ((Color.red(pix) > avgR) & (Color.blue(pix) < avgB)) {// & (Color.green(pix) < 30)){
                                redLoc += q;
                                avgRy += i;
                                if (i < lowRy){
                                    lowRy = i;
                                }
                                redcount++;
                            } else if ((Color.blue(pix) > avgB) & (Color.red(pix) < avgR)) {// & (Color.green(pix) < 30)){
                                blueLoc += q;
                                avgBy += i;
                                if (i < lowBy){
                                    lowBy = i;
                                }
                                bluecount++;
                            } else {
                                neither++;
                            }
                            //leftcount++; //current 1760
                            //arr[i][q] = Color.red(pix);
                            //arb[i][q] = Color.blue(pix);


                        }
                    }


                    redLoc = redLoc / (redcount);
                    blueLoc = blueLoc / (bluecount);
                    avgRy = avgRy / redcount;
                    avgBy = avgBy / bluecount;


//                    telemetry.addData("LeftRed", "" + LR);
//                    telemetry.addData("RightRed", "" + RR);
//                    telemetry.addData("LeftBlue", "" + LB);
//                    telemetry.addData("RightBlue", "" + RB);


                   /*
                   telemetry.addData("avgR: ", avgR);
                   telemetry.addData("avgB: ", avgB);
                   telemetry.addData("Redcount: ", "" + redcount);
                   telemetry.addData("Bluecount: ", "" + bluecount);
                   telemetry.addData("Lowest Red: ", "" + lowRy);
                   telemetry.addData("Lowest Blue: ", "" + lowBy);
                   telemetry.addData("Avg Red Y: ", "" + avgRy); //use to set low bound
                   telemetry.addData("Avg Blue Y: ", "" + avgBy);
                   telemetry.addData("redLoc: ", redLoc);
                   telemetry.addData("blueLoc: ", blueLoc);
                   telemetry.addData("neither: ", neither);
//                        int L = LR - LB;
//                        int R = RR - RB;
                   //telemetry.addData("compiled color LEFT: ", L);
                   //telemetry.addData("compiled color RIGHT: ", R);
                   if ((redLoc > blueLoc)){//flipped bitmap reads weirdly?
                       // telemetry.addData("Height:",bitmap.getHeight());
                       // telemetry.addData("RGB hei",rgb.getHeight());
                       // telemetry.addData("Width",bitmap.getWidth());
                       // telemetry.addData("RGB wid",rgb.getWidth());
                       telemetry.addData("Left", "Side is red");
                       telemetry.update();
                   }
                   else if ((redLoc < blueLoc)) {
                       //telemetry.addData("Height:",bitmap.getHeight());
                       // telemetry.addData("RGB hei",rgb.getHeight());
                       // telemetry.addData("Width",bitmap.getWidth());
                       //telemetry.addData("RGB wid", rgb.getWidth());
                       telemetry.addData("Right", "Side is red");
                       telemetry.update();
                   } else {
                       telemetry.addData("","none?");
                       telemetry.update();
                   }
*/


                    if (pose != null) {//Conditional for if it can't detect first picture
                        VectorF translation = pose.getTranslation();
//                        telemetry.addData(img.getName() + "translation", translation);
                        //This is for vertical phones
                        //double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(1)));
                        //This is for horizontal phones
                        double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(2), translation.get(1))) + 90.0;
//                        telemetry.addData(img.getName() + "Degrees", degreesToTurn);
                        double distance = Math.sqrt(Math.pow(translation.get(1), 2) + Math.pow(translation.get(2), 2));
//                        telemetry.addData(img.getName() + "Distance", distance);
//                        telemetry.update();
                        if (degreesToTurn > 3.0) {
                            telemetry.addData("Turning", "Right");
                            telemetry.addData("Degrees To Turn: ", degreesToTurn);
                            telemetry.update();
                            robot.turn(CHardwareMap.Side.RIGHT, 3, RightMotors, LeftMotors);
                        } else if (degreesToTurn < -3.0) {
                            telemetry.addData("Turning", "Left");
                            telemetry.addData("Degrees To Turn: ", degreesToTurn);
                            telemetry.update();
                            robot.turn(CHardwareMap.Side.LEFT, 3, RightMotors, LeftMotors);
                        } else {
                            //robot.driveStraightForward(10, RightMotors, LeftMotors);
                        }
                        if (distance < 100) {
                            //comp = true;
                            first = false;
                            //Execute push button code
                        }                        }
                }
            }


            telemetry.update();
            idle();
        }


    }
    private void saveImageToExternalStorage(Bitmap finalBitmap, int num) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = "Image-" + num + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Thread.sleep(3);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}