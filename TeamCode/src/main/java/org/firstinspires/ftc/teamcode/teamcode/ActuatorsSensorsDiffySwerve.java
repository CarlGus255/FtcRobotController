package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;


public class ActuatorsSensorsDiffySwerve {
    DcMotorEx leftTopMotor;
    DcMotorEx leftBottomMotor;
    DcMotorEx rightTopMotor;
    DcMotorEx rightBottomMotor;
    GoBildaPinpointDriver ppo;
    Limelight3A limeLight;
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

        limeLight = hwMap.get(Limelight3A.class, "limelight");
        //limeLight.pipelineSwitch(1);
        //limeLight.start();
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




    public double [] target = new double[3];
    private int ballCount = 0;

    public void LimeLightResult() {
        LLResult result = limeLight.getLatestResult();

        if (result.isValid()) {
            List<LLResultTypes.DetectorResult> balls = result.getDetectorResults();

            ballCount = balls.size();

            target[0] = result.getTx();
            target[1] = result.getTy();
            target[2] = result.getTa();
        }
        else {
            ballCount = 0;
        }
    }

    public int getBallCount() {
        return ballCount;
    }

    public double getTarget (int value) {
        if (target != null) {
            return target[value];
        }
        else {
            return 0;
        }
    }
}
