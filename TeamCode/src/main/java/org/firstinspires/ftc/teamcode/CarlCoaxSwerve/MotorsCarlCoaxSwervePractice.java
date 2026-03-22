package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorsCarlCoaxSwervePractice {
    private CRServo LeftServo;
    private DcMotor revEncoder1;
    private  DcMotor LeftMotor;
    private CRServo RightServo;
    private DcMotor RightMotor;
    private AnalogInput RightPot;
    private AnalogInput LeftPot;
    double ticksPerRev = 8192;


    public void init(HardwareMap hwMap) {
        LeftServo = hwMap.get(CRServo.class, "LeftServo");
        LeftPot = hwMap.get(AnalogInput.class, "LeftPot");
        //revEncoder1 = hwMap.get(DcMotor.class, "revEncoder1");
        LeftMotor = hwMap.get(DcMotor.class, "LeftMotor");

        RightServo = hwMap.get(CRServo.class, "RightServo");
        RightPot = hwMap.get(AnalogInput.class, "RightPot");
        RightMotor = hwMap.get(DcMotor.class, "RightMotor");

        LeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        RightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // revEncoder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // revEncoder1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runLeftServo (double power) {
        LeftServo.setPower(power);
    }
    public void runRightServo (double power) {
        RightServo.setPower(power);
    }

    public double readRevEncoder () {
        return revEncoder1.getCurrentPosition()/ticksPerRev;
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

}
