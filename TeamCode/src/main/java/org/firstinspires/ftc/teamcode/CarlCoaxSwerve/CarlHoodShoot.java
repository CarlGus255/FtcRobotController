package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

public class CarlHoodShoot {

    MotorsCarlCoaxSwervePractice motors;
    CarlOdometryExampleImplementation odo;

    public CarlHoodShoot(MotorsCarlCoaxSwervePractice motors, CarlOdometryExampleImplementation odo) {
        this.motors = motors;
        this.odo = odo;
    }


    double encCountToLaunchAngle = 35.95/1117.84;
    double angleError;
    double lastAngleError;
    double hoodAngle;
    double hoodIntegral;
    double kP = 0.5;
    double kI = 0.00;
    double kD = 0.005;
    double targetHeight = 1200; //Target height for the ball to be at
    double targetDistance;


    public double getHoodServoPowerPID (double seekAngle) {
        hoodAngle = motors.getRevEncoderPos()*encCountToLaunchAngle;
        angleError = seekAngle - hoodAngle;

        double derivative = angleError - lastAngleError;
        hoodIntegral += angleError;

        double out = (kP * angleError) + (kI * hoodIntegral) + (kD * derivative);
        lastAngleError = angleError;

        return out;
    }

    public void setHoodAngle (double seekAngle) {
        motors.setHoodServoPower(getHoodServoPowerPID(seekAngle));
    }
    public double getHoodAngle () {
        return hoodAngle;
    }
    public double getAngleError () {
        return angleError;
    }

    public void homeHooD () {
        if (motors.getTouchSensor()) {
            motors.setHoodServoPower(-0.5);
        } else if (!motors.getTouchSensor()) {
            motors.setHoodServoPower(0);
            motors.resetRevEncoderPos();
        }
    }
    public double getLaunchAngle(double x, double y, double velocity) {
        double g = 9.81;

        //Sets up quadratic
        double a = (g * x * x) / (2 * velocity * velocity);
        double b = -x;
        double c = y + a;

        //Evaluates the squart root part of the quadratic equation
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return Double.NaN; // No valid shot
        }

        double sqrt = Math.sqrt(discriminant);

        // Two possible solutions
        double T1 = (-b + sqrt) / (2 * a);
        double T2 = (-b - sqrt) / (2 * a);

        // Choose the lower angle solution
        double theta1 = Math.atan(T1);
        double theta2 = Math.atan(T2);

        //Returns smallest angle
        return Math.min(theta1, theta2);
    }
}
