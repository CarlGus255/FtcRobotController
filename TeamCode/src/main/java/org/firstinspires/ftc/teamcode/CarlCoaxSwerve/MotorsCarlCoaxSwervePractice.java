package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorsCarlCoaxSwervePractice {
    private CRServo servo1;
    private DcMotor revEncoder1;
    private  DcMotor motor1;
    double ticksPerRev = 8192;

    public void init(HardwareMap hwMap) {
        servo1 = hwMap.get(CRServo.class, "servo1");
        revEncoder1 = hwMap.get(DcMotor.class, "revEncoder1");
        motor1 = hwMap.get(DcMotor.class, "motor1");

        revEncoder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        revEncoder1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runServo1 (double power) {
        servo1.setPower(power);
    }
    public double readRevEncoder () {
        return revEncoder1.getCurrentPosition()/ticksPerRev;
    }
    public void runMotor1 (double power) {
        motor1.setPower(power);
    }
}
