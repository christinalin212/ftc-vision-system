package org.firstinspires.ftc.teamcode;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Frame;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.State;
import com.vuforia.Vuforia;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


import java.nio.ByteBuffer;


/**
 * Created by William on 10/4/2016.
 */
// 10/18/16 Tester version- doesn't require robot
//Random null pointer exception sometimes occurs, added check statements to help debug those (comment when they block necessary telemetry data)
// when it only sees black?
//changed all pixel formats to rgb888 (better color fidelity) but doesn't seem to work due to the ByteBuffer b not being large enough for pixels, some other way must exist- but the current works fine with rgb565
//Use HSV/HSB instead of RGB? Easy conversion of RGB values to HSV/HSB, and decide a pixel is red/blue if H is red/blue, any S (glare correction), V/B high (bright), easier
//Decrease camera aperture to reduce glare? Last priority since it doesn't seem easily doable with Vuforia
@Autonomous(name = "VuforiaTester")
public class VuforiaTester extends LinearOpMode {
    VuforiaLocalizer vuforia;


    public Image rgb;
    public State state;
    public Frame myFrame;
    ByteBuffer b;


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
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        VuforiaTrackable Tools = Images.get(1);
        Tools.setName("ToolsImage");  // Tool Images, Red #2


        VuforiaTrackable Legos = Images.get(2);
        Legos.setName("LegosImage");  // Lego Images, Blue #2


        VuforiaTrackable Gears = Images.get(3);
        Gears.setName("GearsImage");  // Gear Images, Red #1


        waitForStart();


        vuforia.setFrameQueueCapacity(6);//6);
        int frameQcapacity = vuforia.getFrameQueueCapacity();
        telemetry.addData("Vuforia frame queue capacity is:", frameQcapacity);
        telemetry.update();
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
//        telemetry.addData("check: ", -2);
//        telemetry.update();


        Images.activate();
        while (opModeIsActive()) {


            if (vuforia.getFrameQueue().peek() != null) {
                //         telemetry.addData("peek is not null", "lol");
                //         telemetry.update();
                //myFrame = vuforia.getFrameQueue().remove();
                myFrame = vuforia.getFrameQueue().take();


                //telemetry.addData("Removed a frame from the queue:", telemetryCount++);
                //telemetry.update();
                sleep(300);


            }
            else{
                telemetry.addData("added","framequeue");
                telemetry.update();
            }
            telemetry.addData("check: ",-1.5);
            telemetry.update();


//            telemetry.addData("finished","conditional");
//             telemetry.update();
            long num;
            try {
                num = myFrame.getNumImages();
                telemetry.addData("HERE HERE HERE",num);
                telemetry.update();
            }


            catch(Exception e){
                telemetry.addData("SKIP SKIP SKIP",e);
                telemetry.update();
            }
            stop();
            num = 1;
            //telemetry.addData("Number of images in the frame:", num);
            //telemetry.update();


            telemetry.addData("check: ",-1.3);
            telemetry.update();


            for (int i = 0; i < num; i++) {
                if (myFrame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    rgb = myFrame.getImage(i);
                    sleep(500);


                }
                else{
                    telemetry.addData("not ","rgba8888");
                    telemetry.update();
                }


            }
            telemetry.addData("check: ",-1);
            telemetry.update();


            if (rgb != null) {
                b = rgb.getPixels();
            } else {
                telemetry.addData("rgb == ", "null");
            }
            Bitmap bitmap = Bitmap.createBitmap(new DisplayMetrics(), rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
            bitmap.copyPixelsFromBuffer(b);


            //telemetry.addData("height",""+height);
            float avgR = 0;
            float avgB = 0;
            telemetry.addData("check: ",0);
            telemetry.update();


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
            avgR = avgR / ((bitmap.getHeight() * bitmap.getWidth()));
            avgB = avgB / ((bitmap.getHeight() * bitmap.getWidth()));


            telemetry.addData("check: ",1);
            telemetry.update();


 /*           float redLoc = 0;
            float blueLoc = 0;
            float redcount = 0;
            float bluecount = 0;
            float neither = 0;
            float lowRy = Integer.MAX_VALUE;
            float avgRy = 0;
            float lowBy = Integer.MAX_VALUE;
            float avgBy = 0;
            float leftistR = Integer.MAX_VALUE;
            float rightestB = Integer.MIN_VALUE;
            for (int i = 0; i < bitmap.getHeight() / 3; i++) {//values between buffers to make it faster
                for (int q = 5; q < bitmap.getWidth(); q += 1) {
                    float[] pix = new float[3];
                    Color.colorToHSV(bitmap.getPixel(q, i),pix); //assumed color values: red (0,25)or(340,360), blue(200,250)
                    if (((pix[0] <= 30) || (pix[0] >= 330)) && pix[1] > 0.3 && pix[2] > 0.7) {//assumed saturation > 0.3 (30), value > 0.7 (70)
                        redLoc += q;
                        avgRy += i;
                        if (i < lowRy){
                            lowRy = i;
                        }
                        if (q < leftistR){
                            leftistR = q;
                        }
                        redcount++;
                    } else if (pix[0] >= 200 && pix[0] <= 270 && pix[1] > 0.3 && pix[2] > 0.7) {
                        blueLoc += q;
                        avgBy += i;
                        if (i < lowBy){
                            lowBy = i;
                        }
                        bluecount++;
                        if (q > rightestB){
                            rightestB = q;
                        }
                    } else {
                        neither++;
                    }
                    //leftcount++; //current 1760
                    //arr[i][q] = Color.red(pix);
                    //arb[i][q] = Color.blue(pix);


                }
            }
//            telemetry.addData("check: ",2);
//            telemetry.update();


            redLoc = redLoc / (redcount);
            blueLoc = blueLoc / (bluecount);
            avgRy = avgRy / redcount;
            avgBy = avgBy / bluecount;


//                    telemetry.addData("LeftRed", "" + LR);
//                    telemetry.addData("RightRed", "" + RR);
//                    telemetry.addData("LeftBlue", "" + LB);
//                    telemetry.addData("RightBlue", "" + RB);
//            telemetry.addData("check: ",3);
//            telemetry.update();
//            telemetry.addData("avgR: ", avgR);
            //telemetry.addData("avgB: ", avgB);
            telemetry.addData("leftest R: ", leftistR);
            telemetry.addData("rightest B: ", rightestB);
            telemetry.addData("Redcount: ", "" + redcount);
            telemetry.addData("Bluecount: ", "" + bluecount);
            telemetry.addData("Lowest Red: ", "" + lowRy);
            telemetry.addData("Lowest Blue: ", "" + lowBy);
            telemetry.addData("Avg Red Y: ", "" + avgRy); //use to set low bound
            telemetry.addData("Avg Blue Y: ", "" + avgBy);
            telemetry.addData("redLoc: ", redLoc);
            telemetry.addData("blueLoc: ", blueLoc);
            telemetry.addData("neither: ", neither);
            telemetry.addData("Height: ", bitmap.getHeight());
            telemetry.addData("Width: ", bitmap.getWidth());


//                        int L = LR - LB;
//                        int R = RR - RB;
            //telemetry.addData("compiled color LEFT: ", L);
            //telemetry.addData("compiled color RIGHT: ", R);
            if (redLoc > blueLoc) {
                // telemetry.addData("Height:",bitmap.getHeight());
                // telemetry.addData("RGB hei",rgb.getHeight());
                // telemetry.addData("Width",bitmap.getWidth());
                // telemetry.addData("RGB wid",rgb.getWidth());
                telemetry.addData("Left", "Side is red");
                telemetry.update();
            } else if (redLoc < blueLoc) {
                //telemetry.addData("Height:",bitmap.getHeight());
                // telemetry.addData("RGB hei",rgb.getHeight());
                // telemetry.addData("Width",bitmap.getWidth());
                //telemetry.addData("RGB wid", rgb.getWidth());
                telemetry.addData("Right", "Side is red");
                telemetry.update();
            } else {
                telemetry.addData("", "none?");
                telemetry.update();
            }
*/



            float redLoc = 0;
            float blueLoc = 0;
            float redcount = 0;
            float bluecount = 0;
            float neither = 0;
            float lowRy = Integer.MAX_VALUE;
            float avgRy = 0;
            float lowBy = Integer.MAX_VALUE;
            float avgBy = 0;
            float leftistR = Integer.MAX_VALUE;
            float rightestB = Integer.MIN_VALUE;
            for (int i = 0; i < bitmap.getHeight() / 3; i++) {//values between buffers to make it faster
                for (int q = 0; q < (bitmap.getWidth() - 0); q += 1) {
                    int pix = bitmap.getPixel(q, i);
                    if ((Color.red(pix) > avgR) & (Color.blue(pix) < avgB)) {// & (Color.green(pix) < 30)){
                        redLoc += q;
                        avgRy += i;
                        if (i < lowRy){
                            lowRy = i;
                        }
                        if (q < leftistR){
                            leftistR = q;
                        }
                        redcount++;
                    } else if ((Color.blue(pix) > avgB) & (Color.red(pix) < avgR)) {// & (Color.green(pix) < 30)){
                        blueLoc += q;
                        avgBy += i;
                        if (i < lowBy){
                            lowBy = i;
                        }
                        bluecount++;
                        if (q > rightestB){
                            rightestB = q;
                        }
                    } else {
                        neither++;
                    }
                    //leftcount++; //current 1760
                    //arr[i][q] = Color.red(pix);
                    //arb[i][q] = Color.blue(pix);

                }
            }
//            telemetry.addData("check: ",2);
//            telemetry.update();

            redLoc = redLoc / (redcount);
            blueLoc = blueLoc / (bluecount);
            avgRy = avgRy / redcount;
            avgBy = avgBy / bluecount;

//                    telemetry.addData("LeftRed", "" + LR);
//                    telemetry.addData("RightRed", "" + RR);
//                    telemetry.addData("LeftBlue", "" + LB);
//                    telemetry.addData("RightBlue", "" + RB);
//            telemetry.addData("check: ",3);
//            telemetry.update();
//            telemetry.addData("avgR: ", avgR);
            telemetry.addData("avgB: ", avgB);
            telemetry.addData("leftest R: ", leftistR);
            telemetry.addData("rightest B: ", rightestB);
            telemetry.addData("Redcount: ", "" + redcount);
            telemetry.addData("Bluecount: ", "" + bluecount);
            telemetry.addData("Lowest Red: ", "" + lowRy);
            telemetry.addData("Lowest Blue: ", "" + lowBy);
            telemetry.addData("Avg Red Y: ", "" + avgRy); //use to set low bound
            telemetry.addData("Avg Blue Y: ", "" + avgBy);
            telemetry.addData("redLoc: ", redLoc);
            telemetry.addData("blueLoc: ", blueLoc);
            telemetry.addData("neither: ", neither);
            telemetry.addData("Height: ", bitmap.getHeight());
            telemetry.addData("Width: ", bitmap.getWidth());

//                        int L = LR - LB;
//                        int R = RR - RB;
            //telemetry.addData("compiled color LEFT: ", L);
            //telemetry.addData("compiled color RIGHT: ", R);
            if (redLoc > blueLoc) {
                // telemetry.addData("Height:",bitmap.getHeight());
                // telemetry.addData("RGB hei",rgb.getHeight());
                // telemetry.addData("Width",bitmap.getWidth());
                // telemetry.addData("RGB wid",rgb.getWidth());
                telemetry.addData("Left", "Side is red");
                telemetry.update();
            } else if (redLoc < blueLoc) {
                //telemetry.addData("Height:",bitmap.getHeight());
                // telemetry.addData("RGB hei",rgb.getHeight());
                // telemetry.addData("Width",bitmap.getWidth());
                //telemetry.addData("RGB wid", rgb.getWidth());
                telemetry.addData("Right", "Side is red");
                telemetry.update();
            } else {
                telemetry.addData("", "none?");
                telemetry.update();
            }






           /*


           float redLoc = 0;
           float blueLoc = 0;
           float redcount = 0;
           float bluecount = 0;
           float neither = 0;
           float lowRy = Integer.MAX_VALUE;
           float avgRy = 0;
           float lowBy = Integer.MAX_VALUE;
           float avgBy = 0;
           float leftistR = Integer.MAX_VALUE;
           float rightestB = Integer.MIN_VALUE;
           for (int i = 0; i < bitmap.getHeight() / 3; i++) {//values between buffers to make it faster
               for (int q = 5; q < bitmap.getWidth(); q += 1) {
                   float[] pix = new float[3];
                   Color.colorToHSV(bitmap.getPixel(q, i),pix); //assumed color values: red (0,25)or(340,360), blue(200,250)
                   if (((pix[0] <= 30) || (pix[0] >= 330)) && pix[1] > 0.3 && pix[2] > 0.7) {//assumed saturation > 0.3 (30), value > 0.7 (70)
                       redLoc += q;
                       avgRy += i;
                       if (i < lowRy){
                           lowRy = i;
                       }
                       if (q < leftistR){
                           leftistR = q;
                       }
                       redcount++;
                   } else if (pix[0] >= 200 && pix[0] <= 270 && pix[1] > 0.3 && pix[2] > 0.7) {
                       blueLoc += q;
                       avgBy += i;
                       if (i < lowBy){
                           lowBy = i;
                       }
                       bluecount++;
                       if (q > rightestB){
                           rightestB = q;
                       }
                   } else {
                       neither++;
                   }
                   //leftcount++; //current 1760
                   //arr[i][q] = Color.red(pix);
                   //arb[i][q] = Color.blue(pix);


               }
           }
//            telemetry.addData("check: ",2);
//            telemetry.update();


           redLoc = redLoc / (redcount);
           blueLoc = blueLoc / (bluecount);
           avgRy = avgRy / redcount;
           avgBy = avgBy / bluecount;


//                    telemetry.addData("LeftRed", "" + LR);
//                    telemetry.addData("RightRed", "" + RR);
//                    telemetry.addData("LeftBlue", "" + LB);
//                    telemetry.addData("RightBlue", "" + RB);
//            telemetry.addData("check: ",3);
//            telemetry.update();
//            telemetry.addData("avgR: ", avgR);
           //telemetry.addData("avgB: ", avgB);
           telemetry.addData("leftest R: ", leftistR);
           telemetry.addData("rightest B: ", rightestB);
           telemetry.addData("Redcount: ", "" + redcount);
           telemetry.addData("Bluecount: ", "" + bluecount);
           telemetry.addData("Lowest Red: ", "" + lowRy);
           telemetry.addData("Lowest Blue: ", "" + lowBy);
           telemetry.addData("Avg Red Y: ", "" + avgRy); //use to set low bound
           telemetry.addData("Avg Blue Y: ", "" + avgBy);
           telemetry.addData("redLoc: ", redLoc);
           telemetry.addData("blueLoc: ", blueLoc);
           telemetry.addData("neither: ", neither);
           telemetry.addData("Height: ", bitmap.getHeight());
           telemetry.addData("Width: ", bitmap.getWidth());


//                        int L = LR - LB;
//                        int R = RR - RB;
           //telemetry.addData("compiled color LEFT: ", L);
           //telemetry.addData("compiled color RIGHT: ", R);
           if (redLoc > blueLoc) {
               // telemetry.addData("Height:",bitmap.getHeight());
               // telemetry.addData("RGB hei",rgb.getHeight());
               // telemetry.addData("Width",bitmap.getWidth());
               // telemetry.addData("RGB wid",rgb.getWidth());
               telemetry.addData("Left", "Side is red");
               telemetry.update();
           } else if (redLoc < blueLoc) {
               //telemetry.addData("Height:",bitmap.getHeight());
               // telemetry.addData("RGB hei",rgb.getHeight());
               // telemetry.addData("Width",bitmap.getWidth());
               //telemetry.addData("RGB wid", rgb.getWidth());
               telemetry.addData("Right", "Side is red");
               telemetry.update();
           } else {
               telemetry.addData("", "none?");
               telemetry.update();
           }


            */




        }
    }
}


