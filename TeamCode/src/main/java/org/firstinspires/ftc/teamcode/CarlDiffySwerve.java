package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="CarlDiffySwerve", group="Test")
public class CarlDiffySwerve extends OpMode {


    @Override
    public void init() {
        double WheelHeading = 90;
        double TopMotorSpeed = 100.2;
        double BottomMotorSpeed = 0;

        telemetry.addData("WheelHeading", WheelHeading);
        telemetry.addData("TopMotorSpeed", TopMotorSpeed);
        telemetry.addData("BottomMotorSpeed", BottomMotorSpeed);
    }

    @Override
    public void loop() {

    }
}





