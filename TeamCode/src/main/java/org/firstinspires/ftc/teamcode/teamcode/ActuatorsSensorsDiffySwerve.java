package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ActuatorsSensorsDiffySwerve {
    DcMotorEx leftTopMotor;
    DcMotorEx leftBottomMotor;
    DcMotorEx rightTopMotor;
    DcMotorEx rightBottomMotor;
    GoBildaPinpointDriver ppo;
    public void init(HardwareMap hwMap) {
        //Initialize Motors
        leftTopMotor = hwMap.get(DcMotorEx.class, "ltm");
        leftTopMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftTopMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftTopMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftBottomMotor = hwMap.get(DcMotorEx.class, "lbm");
        leftBottomMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBottomMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightTopMotor = hwMap.get(DcMotorEx.class, "rtm");
        rightTopMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightTopMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightTopMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightBottomMotor = hwMap.get(DcMotorEx.class, "rbm");
        rightBottomMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBottomMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ppo = hwMap.get(GoBildaPinpointDriver.class, "ppo");
    }

    public void resetLeftEncoders () {
        leftTopMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBottomMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftTopMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetRightEncoders () {
        rightTopMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBottomMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightTopMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double readLeftTopMotorEncoder () {
        return leftTopMotor.getCurrentPosition()/384.5 ;
    }
    public double readLeftBottomMotorEncoder () {
        return leftBottomMotor.getCurrentPosition()/384.5 ;
    }
    public double readRightTopMotorEncoder () {
        return rightTopMotor.getCurrentPosition()/384.5 ;
    }
    public double readRightBottomMotorEncoder () {
        return rightBottomMotor.getCurrentPosition()/384.5 ;
    }
    public void driveLeftTopMotor (double power) {
        leftTopMotor.setPower(power);
    }
    public void driveLeftBottomMotor (double power) {
        leftBottomMotor.setPower(power);
    }
    public void driveRightTopMotor (double power) {
        rightTopMotor.setPower(power);
    }
    public void driveRightBottomMotor (double power) {
        rightBottomMotor.setPower(power);
    }
}
