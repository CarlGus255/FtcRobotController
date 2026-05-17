package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp
public class AxonServo4thWireTest extends OpMode {
    private CRServo stiltServo;
    private AnalogInput stiltAnalog;
    double currentAnlogMax = 0;
    double currentAnalogMin =1;
    public double analogMin = 0.006;
    public double analogMax = 3.286;
    public double analogSpread = analogMax - analogMin;
    int inc;
    double currentRotation;
    double currentAnalog;
    double lastAnalog;
    double actualRotation;
    public void hardwareInit (HardwareMap hwMap) {
        stiltServo = hwMap.get(CRServo.class, "stiltServo");
        stiltAnalog =hwMap.get(AnalogInput.class, "stiltAnalog");
        currentAnalog = stiltAnalog.getVoltage();
        lastAnalog = currentAnalog;
    }


    @Override
    public void init() {
        hardwareInit(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Analog data is:", stiltAnalog.getVoltage());
        telemetry.addData("Current Analog Max is:", readCurrentMax());
        telemetry.addData("Current Analog Min is:", readCurrentMin());

        telemetry.addData("Current Rotation Increment is:", inc);
        telemetry.addData("Current Rotation From Start is:", readMultipleRotationFromStart());

        telemetry.addData("Delta Time is:", getDeltaTime());

        runToStiltPosition(changePos(gamepad1.left_stick_y));
        telemetry.addData("Current Change Pos is:", changePos(gamepad1.left_stick_y));
        telemetry.addData("Current Angle Error is:", stiltAngleError);
    }

    public double readCurrentMax () {
    if (stiltAnalog.getVoltage() > currentAnlogMax) {
        currentAnlogMax = stiltAnalog.getVoltage();
        }
    return currentAnlogMax;
    }
    public double readCurrentMin () {
        if (stiltAnalog.getVoltage() < currentAnalogMin) {
            currentAnalogMin = stiltAnalog.getVoltage();
        }
        return currentAnalogMin;
    }
    public void runTiltServo (double power) {
        stiltServo.setPower(power);
    }
    double delta;
    double threshold;
    double normalized;
    public double readMultipleRotationFromStart () {
        currentAnalog = stiltAnalog.getVoltage();

        delta = currentAnalog - lastAnalog;
        threshold = analogSpread * 0.4;

        if (delta > threshold) {
            inc--;
        } else if (delta < -threshold) {
            inc++;
        }

        lastAnalog = currentAnalog;

        normalized = currentAnalog - analogMin;
        actualRotation = normalized + (inc * analogSpread);

        return actualRotation * 360/analogSpread;
    }
    double lastRunTime;
    double currentRunTime;
    double deltaTime;
    public double getDeltaTime () {
        currentRunTime = getRuntime();
        deltaTime = currentRunTime - lastRunTime;
        lastRunTime = currentRunTime;

        return deltaTime;
    }
    double stiltAngleError;
    double stiltErrorPower;
    double stiltDerivative;
    double lastStiltAngleError;
    double stiltIntegral;
    //Divide all the constants by 360 to convert to a more understandable rotations-relative tuning rather then inputing really small numbers for degrees-relative tuning
    public final double stiltKP = 2.1/360;
    public final double stiltKI = 0/360;
    public final double stiltKD = 0.01/360; //Seems small but helps with ocilation, at least without the stilt at the moment.
    public final double stiltKF = 0;

    public void runToStiltPosition (double pos) {
        stiltAngleError = pos - readMultipleRotationFromStart();

        stiltDerivative = (stiltAngleError - lastStiltAngleError)/getDeltaTime();
        stiltIntegral += (stiltAngleError * getDeltaTime());
        stiltIntegral = Math.max(-0.1, Math.min(0.1, stiltIntegral));

        stiltErrorPower = (stiltAngleError * stiltKP) + (stiltDerivative * stiltKD) + (stiltIntegral * stiltKI) + stiltKF;
        lastStiltAngleError = stiltAngleError;


        stiltServo.setPower(Math.max(-0.9, Math.min(0.9, -stiltErrorPower)));
    }
    double seekPos;
    double tune = 3;
    public double changePos (double change) {
        seekPos += (change * tune);
        return seekPos;
    }
}
