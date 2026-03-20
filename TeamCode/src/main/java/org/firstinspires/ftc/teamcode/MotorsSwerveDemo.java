package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorsSwerveDemo {

    private DcMotor SwerveMotor;
    private DcMotor MecanumMotor;

    public void init(HardwareMap hwMap){
        SwerveMotor = hwMap.get(DcMotor.class, "SwerveMotor");
        SwerveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        MecanumMotor = hwMap.get(DcMotor.class, "MecanumMotor");
        MecanumMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void SetSwerveSpeed (double speed) {
        SwerveMotor.setPower(speed);
    }

    public void SetMecanumSpeed (double speed) {
        MecanumMotor.setPower(speed);
    }
}
