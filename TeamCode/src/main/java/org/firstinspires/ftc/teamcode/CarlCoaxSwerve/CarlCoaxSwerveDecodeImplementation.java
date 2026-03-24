package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

public class CarlCoaxSwerveDecodeImplementation {
    MotorsCarlCoaxSwervePractice motors = new MotorsCarlCoaxSwervePractice ();
    CarlOdometryExampleImplementation odo = new CarlOdometryExampleImplementation();

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
    double changeXYAcceleration = 10; //Think 10 CM means motor power is set to one, then scales linearly as you approach the desired value.
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
    double xIncrease = 0;
    double seekFieldX; 
    double xVelAcceleration; //Tune to achieve X scaling

    double yIncrease = 0;
    double seekFieldY;
    double yVelAcceleration; //Tune to achieve X scaling


    double yawIncrease = 0;
    double seekFieldYaw;
    double yawVelAcceleration; //Tune to achieve X scaling

    double xPos1 = 0;
    double yPos1 = 0;
    double yawPos1 = -45;


    /*
    public double swerveImplementTransformationX (double fieldXVelocity, double fieldYVelocity, double fieldYawVelocity) {

    }
    */

    public void swerveImplement (double seekFieldX, double seekFieldY, double seekFieldYaw) {

        //Implements odometry
        fieldX = odo.getFieldX();
        fieldY = odo.getFieldY();
        fieldYaw = odo.getFieldYaw();

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

        //Optimization - wheel polarity flipping. Combines overrotation with flipping the direction of wheel spin. I don't prefer, but we can see. It doesn't work for differential swerve very wheel because of chopiness due to fast wheel rotation capabilities, but with the slower wheel rotation with coaxial it's probably better

        /*

        Wheel polarity code

         */
        motors.runLeftMotor(translationLeft);
        motors.runRightMotor(translationRight);
        motors.runLeftServo(fieldChangeHeading);
        motors.runRightServo(fieldChangeHeading);
    }

    /*
    Converting velocity to position for OpMode implementation. It tracks the last position, and depending on the VelAcceleration, it
    changes the position values, to be input to the swerveImplementation. This allows for both TeleOp control, autonomous control, and
    to seek field positions. For deadband, we could instead change the if statement conditions instead of using the magnitude earlier on.
    Example: if (xVelocity > 0.05 ...)
     */
    public double swerveImplementaitonXVelocityToXPos (double xVelocity) {
        if (xVelocity > 0 || xVelocity < 0) {
            xIncrease = xIncrease + (xVelAcceleration * xVelocity);
        }
        seekFieldX = fieldX + xIncrease;
        return seekFieldX;
    }

    public double swerveImplementaitonYVelocityToYPos (double yVelocity) {
        if (yVelocity > 0 || yVelocity < 0) {
            yIncrease = yIncrease + (yVelAcceleration * yVelocity);
        }
        seekFieldY = fieldY + yIncrease;
        return seekFieldY;
    }
    public double swerveImplementaitonYawVelocityToYawPos (double yawVelocity) {
        if (yawVelocity > 0 || yawVelocity < 0) {
            yawIncrease = yawIncrease + (yawVelAcceleration * yawVelocity);
        }
        seekFieldYaw = fieldYaw + yawIncrease;
        return seekFieldYaw;
    }


}