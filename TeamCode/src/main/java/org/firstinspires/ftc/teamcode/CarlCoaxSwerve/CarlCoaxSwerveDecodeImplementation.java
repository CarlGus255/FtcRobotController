package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

public class CarlCoaxSwerveDecodeImplementation  {
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice();

    //Use the odometry class to get the actual fieldX, fieldY, and Yaw variables.
    double fieldX; //would be equal to the odo field x, and so on for the other variables
    double fieldY;
    double fieldYaw;

    //Heading tracking
    double headingAcceleration = 1;
    double heading; //Track current heading - see CoaxSwerveFieldCentric for pot useage. In degrees
    double seekHeading; //Use to set heading, in degrees
    double changeHeading; //To implement heading changes
    double lastChangeHeading; // For deadband
    double fieldChangeHeading; //Transforms from robot centric to field centric heading before implementation

    //XY locomotion
    double changeXYAcceleration = 36; //Think 36 inches means motor power of one, then scales linearly.
    double changeX; //Makes the robot try to move a certain amount in the X direction
    double changeY; //Makes the robot try to move a certain amount in the Y direction

    //Yaw Change
    final double yawAcceleration = 0.2; //How hard it seeks to the desired yaw. The more aggressive, the faster it seeks, but the slower it translates, so this needs to be tuned
    double changeYaw; //Makes the robot actually seek to the yaw

    //Polar coordinate transoformation from the XY input
    double changeDirection; //Represents the necessary direction for the pod to point
    double changeMagnitude; //Represents how fast it should seek, limited from 0 to 1

    //Implements Yaw change
    double translationLeft; //Yaw change added to left wheel translation
    double translationRight; //Yaw change subtracted from right wheel translation

    /*
    To implement the swerve function which runs to position based on the current design, we need a transition from velocity inputs to
    position inputs. This is done by the three swerve implement transformation functions, with each function returning its own value.
    */

    /*
    public double swerveImplementTransformationX (double fieldXVelocity, double fieldYVelocity, double fieldYawVelocity) {

    }
    */

    public void swerveImplement (double seekFieldX, double seekFieldY, double seekFieldYaw) {

        //These values are how we will manipulate motor power to reach the seek position, using a partial PID (Only P)
        //Change Acceleration scales the change from inches (or whatever field units) to a unitless number for motor power.
        changeX = (seekFieldX - fieldX)/changeXYAcceleration;
        changeY = (seekFieldY - fieldY)/changeXYAcceleration;

        //Implements heading seeking
        changeHeading = (seekHeading - heading) * headingAcceleration;

        //Yaw Acceleratoin is the proportional acceleration value, and determines how much it tries to change the yaw
        //Translation Offset is the variable that will be used to implement a difference in translation to each motor, inducing the yaw change
        //Change Yaw is divided by 360 degrees to convert from degrees to revolutions, which is more meaningful for implementation
        changeYaw = ((seekFieldYaw - fieldYaw)*yawAcceleration)/360;


        //Transform the changeX and changeY variables to a vector value which can be implemented using translation and rotation variables.
        //Change direction we want the positive y direction to be zero. It is in degrees.
        //Magnitude constrained to 0 to 1, but acceleration/proportionality can be changed with changeAcceleration
        changeDirection = Math.toDegrees(Math.atan2(-changeY,changeX));
        changeMagnitude = Math.min(0.0, Math.max(Math.hypot(changeX,changeY), 1.0));

        /*
        These will be motor power values. Right now, it can reach 1.2, but that's not necessarily dangerous, although it does cause
        inconsistent amount of turning while driving. However, I prefer to slow the yaw change than slow the linear velocity.
        */
        translationLeft = changeMagnitude + changeYaw;
        translationRight = changeMagnitude - changeYaw;

        //Implement the heading change using seek heading to seek the directional input
        seekHeading = changeDirection;

        //Deadband - this might need to be reworked a little bit for this particular application. Makes it so the servos don't snap to 0.
        if (changeMagnitude < 0.1) {
            lastChangeHeading = changeHeading;
        }

        //Implements field centric wheel heading
        fieldChangeHeading = lastChangeHeading + fieldYaw;

        //Optimization - overrotation detection. If it's trying to rotate more than 180 degrees, it rotates in the opposite direction instead.
        while (fieldChangeHeading > 180) {
            fieldChangeHeading -= 360;
        }
        while (fieldChangeHeading < -180) {
            fieldChangeHeading += 360;
        }

        //Optimization - wheel polarity flipping. Combines overrotation with flipping the direction of wheel spin. I don't prefer, but we can see.
        /*

        Wheel polarity code

         */
        motors.runLeftMotor(translationLeft);
        motors.runRightMotor(translationRight);
        motors.runLeftServo(fieldChangeHeading);
        motors.runRightServo(fieldChangeHeading);
    }
}
