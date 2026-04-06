package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import android.util.Size;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class MotorsCarlCoaxSwervePractice {
    private CRServo LeftServo;
    private DcMotorEx revEncoder1;
    private  DcMotorEx LeftMotor;
    private CRServo RightServo;
    private DcMotorEx RightMotor;
    private AnalogInput RightPot;
    private AnalogInput LeftPot;
    double ticksPerRev = 8192;
    private CRServo HoodServo;
    private DigitalChannel TouchSensor;
    private DcMotorEx flyWheel;
    private DcMotorEx intakeMotor;
    private Servo gate;
    private RevColorSensorV3 colorSensor;
    float hue;
    float[] hsv = new float[3];
    boolean isPurple;
    boolean isGreen;

    //Flywheel PIDF values
    double kP = 12;
    double kI = 0;
    double kD = 5;
    double kF = 14; //11.7 I think is technically ideal system, but with friction and such more is needed



    public void init(HardwareMap hwMap) {
        LeftServo = hwMap.get(CRServo.class, "LeftServo");
        LeftPot = hwMap.get(AnalogInput.class, "LeftPot");
        LeftMotor = hwMap.get(DcMotorEx.class, "LeftMotor");

        RightServo = hwMap.get(CRServo.class, "RightServo");
        RightPot = hwMap.get(AnalogInput.class, "RightPot");
        RightMotor = hwMap.get(DcMotorEx.class, "RightMotor");

        LeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        LeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        RightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        revEncoder1 = hwMap.get(DcMotorEx.class, "revEncoder1");
        HoodServo = hwMap.get(CRServo.class, "HoodServo");
        TouchSensor = hwMap.get(DigitalChannel.class, "TouchSensor");

        revEncoder1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        revEncoder1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        flyWheel = hwMap.get(DcMotorEx.class, "flyWheel");
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients pidf = new PIDFCoefficients(kP, kI, kD, kF);
        flyWheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        intakeMotor = hwMap.get(DcMotorEx.class, "IntakeMotor");

        gate = hwMap.get(Servo.class, "gate");

        colorSensor = hwMap.get(RevColorSensorV3.class, "ColorSensor");
    }
    public void updateColorSensor () {
        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsv);
        hue = hsv[0];
    }
    public boolean isColorSensorPurple () {
        updateColorSensor();
        if (hue > 100 && hue < 140 ) {
            isPurple = true;
        } else {
            isPurple = false;
        }
        return isPurple;
    }
    public boolean isColorSensorGreen () {
        updateColorSensor();
        if (hue > 250 && hue < 280 ) {
            isGreen = true;
        } else {
            isGreen = false;
        }
        return isGreen;
    }


    public void setIntakePower (double power) {
        intakeMotor.setPower(power);
    }
    public void setGate (double pos) {
        gate.setPosition(pos);
    }
    public void runLeftServo (double power) {
        LeftServo.setPower(power);
    }
    public void runRightServo (double power) {
        RightServo.setPower(power);
    }

    public double getRevEncoderPos () {
        return (revEncoder1.getCurrentPosition()/ticksPerRev)*360; //Returns the rotation of the hood servo in degrees
    }
    public void resetRevEncoderPos () {
        revEncoder1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        revEncoder1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setHoodServoPower (double power) {
        HoodServo.setPower(power);
    }

    public boolean getTouchSensor () {
        return TouchSensor.getState();
    }


    public void runLeftMotor (double power) {
        LeftMotor.setPower(power);
    }
    public void runRightMotor (double power) {
        RightMotor.setPower(power);
    }

    public double getLeftPot () {
        return LeftPot.getVoltage()*(270/3.13);
    }
    public double getRightPot () {
        return RightPot.getVoltage()*(270/3.13);
    }

    public double getFlyLinVel () {
        return (flyWheel.getVelocity()/28) * 104 * 3.14/1000;
    }
    public double getFlyVel () {
        return  (flyWheel.getVelocity()*60/28); //RPM
    }
    public void setFlyWheelPower (double power) {
        flyWheel.setPower(power);
    }
    public void setFlyWheelVelocity (double Velocity) {
        flyWheel.setVelocity(Velocity*28/60); //RPM
    }


}
