package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp
public class WinchTest extends OpMode {


    public DcMotorEx WinchMotor;

    public void init(HardwareMap hwMap) {
        WinchMotor = hwMap.get(DcMotorEx.class, "WinchMotor");
        WinchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void init() {
        init(hardwareMap);
    }

    @Override
    public void loop() {
        WinchMotor.setPower(-gamepad1.left_stick_y);
    }
}
