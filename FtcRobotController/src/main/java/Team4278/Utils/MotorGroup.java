package Team4278.Utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.ArrayList;

/**
 * Created by William on 10/13/2016.
 * This is a custom class I made to make it easier to group motors
 */

public class MotorGroup {

    ArrayList<DcMotor> dcMotors;

    public MotorGroup(DcMotor... toAdd) {
        dcMotors = new ArrayList<>();
        for (DcMotor motor : toAdd) {
            dcMotors.add(motor);
        }
    }
    //Adds Motors to the group

    public void AddMotors(DcMotor... motors) {
        for (DcMotor motor : motors) {
            dcMotors.add(motor);
        }
    }

    //Resets encoders, hard reset

    public void resetEncoders() {
        for(int i = 0; i < dcMotors.size(); i++) {
            dcMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    //Sets motor powers to 0, soft reset

    public void stop() {
        setPowers(0);
    }

    public String PrintMotors() {
        String motors = "";
        for (int i = 0; i < dcMotors.size(); i++) {
            motors = motors + String.valueOf(dcMotors.get(i));
        }
        return motors;
    }

    public void setMode(DcMotor.RunMode Mode) {
//        if(Mode.equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)){
//            for (int i = 0; i < dcMotors.size(); i++) {
//                dcMotors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            }
//        }else if(Mode.equals(DcMotor.RunMode.RUN_USING_ENCODER)){
//            for (int i = 0; i < dcMotors.size(); i++) {
//                dcMotors.get(i).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            }
//        }else if(Mode.equals(DcMotor.RunMode.RUN_TO_POSITION)){
//            for (int i = 0; i < dcMotors.size(); i++) {
//                dcMotors.get(i).setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }
//        }else{
//            for (int i = 0; i < dcMotors.size(); i++) {
//                dcMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            }
//        }
        switch (Mode) {
            case RUN_WITHOUT_ENCODER:
                for (int i = 0; i < dcMotors.size(); i++) {
                    dcMotors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                break;
            case RUN_USING_ENCODER:
                for (int i = 0; i < dcMotors.size(); i++) {
                    dcMotors.get(i).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                break;
            case RUN_TO_POSITION:
                for (int i = 0; i < dcMotors.size(); i++) {
                    dcMotors.get(i).setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
                break;
            case STOP_AND_RESET_ENCODER:
                for (int i = 0; i < dcMotors.size(); i++) {
                    dcMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
                break;
        }
    }

    //Checks if busy for the last motor

    public boolean isBusy() {
        if (dcMotors.get(dcMotors.size() - 1).isBusy()) {
            return true;
        }
        return false;
    }

    //Sets the target position for each of the motors

    public void setTargetPosition(int position) {
        for (int i = 0; i < dcMotors.size(); i++) {
            dcMotors.get(i).setTargetPosition(position);
        }
    }
    //Sets motor powers

    public void setPowers(double power) {
        for (int i = 0; i < dcMotors.size(); i++) {
            dcMotors.get(i).setPower(power);
        }
    }
}