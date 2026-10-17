package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class AxonServoConfigure extends OpMode {
    CRServo axon;
    AnalogInput axonPot;
    CRServo axon2;
    AnalogInput axon2Pot;
    DigitalChannel touchSensor;
    DataBuffer lowerBoundMultiple;
    DataBuffer upperBoundMultiple;
    TimerImplement timer;
    double analogMin = 1;
    double analogMax = 1;
    double potReading;
    boolean constantMode = false;
    boolean constantModeDirection;
    //Single Touch Homing
    double lowerBound;
    double upperBound;
    boolean homing;
    int direction = 1;
    boolean directionSwitch;
    double homingPower = 0.3;
    //Multiple Touch Sensing
    int amountReadings = 3;
    boolean homingMultiple;
    int upperReadings;
    int lowerReadings;
    boolean homingInterupt;
    boolean finishedInterupt;
    boolean wait;
    double limitUpperTilt = 2.45;
    double limitLowerTilt = 1.8;
    double limitLowerPan = 1.1;
    double limitUpperPan = 1.6;
    double axon1Power;
    double axon2Power;
    double powerScale = 0.5;

    @Override
    public void init() {
        axon = hardwareMap.get(CRServo.class, "tiltAxon");
        axonPot = hardwareMap.get(AnalogInput.class, "tiltPot");
        axon2 = hardwareMap.get(CRServo.class, "panAxon");
        axon2Pot = hardwareMap.get(AnalogInput.class, "panPot");
        touchSensor = hardwareMap.get(DigitalChannel.class, "touchSensor");

        upperBoundMultiple = new DataBuffer(amountReadings);
        lowerBoundMultiple = new DataBuffer(amountReadings);
        timer = new TimerImplement();
    }

    @Override
    public void loop() {
        potReading = axonPot.getVoltage();
        //Compare readings
        if (potReading < analogMin) {
            analogMin = potReading;
        }
        if (potReading > analogMax) {
            analogMax = potReading;
        }

        //switch between constant and manual mode
        if (gamepad2.xWasPressed()) {
            constantMode = !constantMode;
        }


        //Manual rotation code
       if (!constantMode) {
            if (gamepad1.dpadUpWasPressed()) {
                powerScale += 0.1;
            }
            if (gamepad1.dpadDownWasPressed()) {
                powerScale -= 0.1;
            }
           // Check if servos are in or out of range and sets the power
           axon1Power = gamepad1.left_stick_x * powerScale;
           axon2Power = gamepad1.right_stick_x * powerScale;
           if (axonPot.getVoltage() <= limitLowerTilt && axon1Power > 0) {
               axon1Power = 0;
               axon.setPower(0);
           } else if (axonPot.getVoltage() >= limitUpperTilt && axon1Power < 0) {
               axon1Power = 0;
               axon.setPower(0);
           } else {
               axon.setPower(axon1Power);
           }

           if (axon2Pot.getVoltage() <= limitLowerPan && axon2Power > 0) {
               axon2Power = 0;
               axon2.setPower(0);
           } else if (axon2Pot.getVoltage() >= limitUpperPan && axon2Power < 0) {
               axon2Power = 0;
               axon2.setPower(0);
           } else {
               axon2.setPower(axon2Power);
           }
           telemetry.addData("Axon 1 power is:", axon1Power);
           telemetry.addData("Axon 2 power is:", axon2Power);
           telemetry.addData("Power Scale is:", powerScale);
       }
       /*
        //Continuous rotation code
        if (constantMode) {
            if (constantModeDirection){
                axon.setPower(1);
            }
           if (!constantModeDirection) {
               axon.setPower(-1);
           }
           if (gamepad1.bWasPressed()) {
               constantModeDirection = !constantModeDirection;
           }
        }
        //Activate Homing
        if (gamepad1.yWasPressed()) {
            homing = true;
        }
        //Homing - hit sensor once then go to the other side once
        if (homing) {
            if (!directionSwitch) {
                axon.setPower(homingPower * direction);
                if (!touchSensor.getState()) {
                    upperBound = axonPot.getVoltage();
                    directionSwitch = !directionSwitch;
                    direction *= -1;
                }
            }
            if (directionSwitch) {
                axon.setPower(homingPower * direction);
                if (!touchSensor.getState()) {
                    lowerBound = axonPot.getVoltage();
                    directionSwitch = !directionSwitch;
                    direction *= -1;
                    homing = !homing;
                }
            }
        }


        //Activate 3 touch homing
        if (gamepad1.leftTriggerWasPressed()) {
            homingMultiple = true;
            upperReadings = 0;
            lowerReadings = 0;
        }
        // Interupt
        if (homingInterupt) {
            finishedInterupt = timer.setTimer(getRuntime(), 0.5);
            axon.setPower(homingPower * -direction);
            if (finishedInterupt) {
                wait = timer.setTimer(getRuntime(), 0.5);
                if (wait) {
                    homingInterupt = !homingInterupt;
                    wait = !wait;
                }
            }
        }
        // 3 Touch Homing
        if (homingMultiple && !homingInterupt) {
            if (!directionSwitch) {
                axon.setPower(direction * (homingPower  - ((double) 1 /(upperReadings+1))*(upperReadings * homingPower)));
                if (!touchSensor.getState()) {
                    upperReadings += 1;
                    upperBoundMultiple.add(axonPot.getVoltage());
                    homingInterupt = true;
                    wait = true;
                }
                if (upperReadings >= amountReadings) {
                    directionSwitch = !directionSwitch;
                    direction *= -1;
                }
            }
            if (directionSwitch) {
                axon.setPower(direction * (homingPower  - ((double) 1 /(lowerReadings+2))*(lowerReadings * homingPower)));
                if (!touchSensor.getState()) {
                    lowerReadings += 1;
                    lowerBoundMultiple.add(axonPot.getVoltage());
                    homingInterupt = true;
                    wait = true;
                }
                if (lowerReadings >= amountReadings) {
                    directionSwitch = !directionSwitch;
                    direction *= -1;
                    homingMultiple = false;
                    lowerBound = lowerBoundMultiple.getMean();
                    upperBound = upperBoundMultiple.getMean();
                }
            }
        }

        */
        //Telemetry
        telemetry.addData("Analog is:", potReading);
        telemetry.addData("Analog Min is:", analogMin);
        telemetry.addData("Analog Max is:", analogMax);
        telemetry.addData("Analog 2 is:", axon2Pot.getVoltage());
    }
}
