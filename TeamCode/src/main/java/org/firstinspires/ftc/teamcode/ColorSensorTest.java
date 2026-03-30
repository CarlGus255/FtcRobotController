package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ColorSensorTest extends OpMode {
    public void updateColorSensor () {
        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsv);
        hue = hsv[0];
    }
    public boolean isColorSensorPurple () {
        updateColorSensor();
        if (hue < 170  && hue > 140) {
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
    private RevColorSensorV3 colorSensor;
    float hue;
    float[] hsv = new float[3];
    boolean isPurple;
    boolean isGreen;

    @Override
    public void init() {
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "ColorSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("Color Sensor Sees Purple:", isColorSensorPurple());
        telemetry.addData("Color Sensor Sees Green:", isColorSensorGreen());
        telemetry.addData("Color Sensor Sees:", hue);
    }
}
