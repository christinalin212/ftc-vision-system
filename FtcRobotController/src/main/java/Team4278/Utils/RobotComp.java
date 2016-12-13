package Team4278.Utils;

/**
 * Created by William on 10/24/2016.
 */

public class RobotComp {

    //To Radians

    public double toRadians(int degrees){
        return degrees*Math.PI/180.0;
    }

    //This converts inches to meters, helpful for computePosition

    public double toMeters(double inches){
        return inches*0.0254;
    }

    //Computes a position. Takes a distance in meters and converts it to position

    public int computePositionD(double distance) {
        return (int)(distance*(1120/(Math.PI*toMeters(4))));
    }

    //degrees indicates degrees to turn
    //side indicates the side the motors are on, 0 is left, 1 is right
    //direction indicates the direction we turn, 0 is left, 1 is right

    public int computeTurningPos(boolean direction, int degrees, boolean side, double wDistance){
        double Circumference = wDistance*Math.PI;
        double position = computePositionD(toMeters(Circumference*toRadians(degrees)/(2*Math.PI)));
        if(direction){
            if(side){
                return -(int)position;
            }else{
                return (int)position;
            }
        }else{
            if(side){
                return (int)position;
            }else{
                return -(int)position;
            }
        }
    }

}
