package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp
public class AxonServo4thWireTest extends OpMode {
    private CRServo leftServo;
    private AnalogInput leftAnalog;

    public void hardwareInit (HardwareMap hwMap) {
        leftServo = hwMap.get(CRServo.class, "LeftServo");
        leftAnalog =hwMap.get(AnalogInput.class, "LeftAnalog");
    }


    @Override
    public void init() {
        hardwareInit(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Analog data is:", leftAnalog.getVoltage());
        leftServo.setPower(-gamepad1.left_stick_y);
    }
}
