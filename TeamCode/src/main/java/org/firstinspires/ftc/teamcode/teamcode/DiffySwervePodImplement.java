package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DiffySwervePodImplement {

    private DcMotorEx topMotor;
    private DcMotorEx bottomMotor;

    PIDFImplement rotationPID;

    public DiffySwervePodImplement(
            DcMotorEx topMotor,
            DcMotorEx bottomMotor,
            PIDFImplement rotationPID
    ) {

        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        this.rotationPID = rotationPID;

    }
    private double translation;
    private double rotation;
    private double heading;
    double lastTime;
    double deltaTime;
    double headingScaleFactor = (((double) 40 /60)*((double) 360 /2)/384.5); //Converts from motor encoders to degrees for heading
    double seekHeading;

    public void manualChangeRotation (double change) {
        seekHeading += change;
    }
    public void setTranslation(double change) {
        translation = change;
    }
    public void update (double runTime) {
        //Reads the heading
        heading = (topMotor.getCurrentPosition() + bottomMotor.getCurrentPosition())*headingScaleFactor;

        //Gets the delta time
        deltaTime = runTime - lastTime;
        lastTime = runTime;

        //Drives the heading
        rotation = Math.max(-1, Math.min(1, rotationPID.PIDFOutPut(seekHeading - heading, deltaTime)));

        //Drives the motors
        topMotor.setPower(translation+rotation);
        bottomMotor.setPower(-translation+rotation);
    }
    public double getHeading () {
        return heading;
    }
    public double getSeekHeading() {
        return seekHeading;
    }
    public void setHeading (double setHeading) {
        seekHeading = setHeading;
    }
}
