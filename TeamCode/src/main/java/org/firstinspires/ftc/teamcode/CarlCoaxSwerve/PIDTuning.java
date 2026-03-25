package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import org.firstinspires.ftc.teamcode.CarlCoaxSwerve.Practice.CarlCoaxSwerveWheelHeading;

public class PIDTuning {
    CarlCoaxSwerveWheelHeading wheelHeading;

    public PIDTuning (CarlCoaxSwerveWheelHeading wheelHeading) {
        this.wheelHeading = wheelHeading;
    }

    double kP = 0.0015;
    double kI = 0.00;
    double kD = 0.0005;

    private double lastError = 0;
    private double integral = 0;
    private double lastRightError = 0;
    private double rightIntegral = 0;

    private double wrapError(double error) {
        return (error + 180) % 360 - 180;
    }
    public double getLeftServoPower(double targetAngle) {

        double current = wheelHeading.getLeftWheelHeading();

        double error = wrapError(targetAngle - current);

        integral += error;
        double derivative = error - lastError;

        double output = (kP * error) + (kI * integral) + (kD * derivative);

        lastError = error;

        output = Math.max(-1, Math.min(1, output));
        return output;
    }

    public double getRightServoPower(double targetAngle) {

        double current = wheelHeading.getRightWheelHeading();

        double error = wrapError(targetAngle - current);

        rightIntegral += error;
        double derivative = error - lastRightError;

        double output = (kP * error) + (kI * rightIntegral) + (kD * derivative);

        lastRightError = error;

        output = Math.max(-1, Math.min(1, output));
        return output;
    }

}
