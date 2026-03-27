package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import android.util.Size;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
        flyWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor = hwMap.get(DcMotorEx.class, "IntakeMotor");

        gate = hwMap.get(Servo.class, "gate");
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
        return revEncoder1.getCurrentPosition()/ticksPerRev;
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
        return (flyWheel.getVelocity()/28) * 104 * 3.14;
    }
    public double getFlyVel () {
        return  (flyWheel.getVelocity()/(28*60)); //RPM
    }
    public void setFlyWheelPower (double power) {
        flyWheel.setPower(power);
    }
    public void setFlyWheelVelocity (double Velocity) {
        flyWheel.setVelocity(Velocity*28/60); //RPM
    }


}
