package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Disabled
public class TestRobotCentricControl extends OpMode {
    DiffySwervePodImplement leftPod;
    DiffySwervePodImplement rightPod;

    ActuatorsSensorsDiffySwerve motors;
    SLAM slam;

    PIDFImplement leftRotationPIDF;
    PIDFImplement rightRotationPIDF;
    PIDFImplement yawPIDF;

    DiffySwerveRobotImplement robot;

    @Override
    public void init() {
        leftRotationPIDF = new PIDFImplement();
        rightRotationPIDF = new PIDFImplement();
        yawPIDF = new PIDFImplement();

        leftRotationPIDF.setConstants(0.005, 0, 0, 0, 0, 0.001);
        rightRotationPIDF.setConstants(0.005, 0, 0, 0, 0, 0.001);
        yawPIDF.setConstants(0,0,0,0, 0,0.001);

        motors = new ActuatorsSensorsDiffySwerve();
        motors.init(hardwareMap);

        slam = new SLAM(motors);
        slam.resetPPO();

        leftPod = new DiffySwervePodImplement(motors.leftTopMotor, motors.leftBottomMotor, leftRotationPIDF);
        rightPod = new DiffySwervePodImplement(motors.rightTopMotor, motors.rightBottomMotor, rightRotationPIDF);

        robot = new DiffySwerveRobotImplement(leftPod, rightPod, yawPIDF, slam);
    }

    @Override
    public void loop() {
        slam.update();

        robot.setAdjustedVelocity(-gamepad1.left_stick_y*5);
        robot.changeDirection(gamepad1.left_stick_x*5);
        robot.changeYaw(gamepad1.right_stick_x);
        robot.update(getRuntime());

        leftPod.update(getRuntime());
        rightPod.update(getRuntime());

        //Yaw PIDF tuning and telemetry
        yawPIDF.changeMode(gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());
        yawPIDF.tuneConstants(gamepad1.dpadUpWasPressed(), gamepad1.dpadDownWasPressed());
        if (yawPIDF.getMode() % 4 == 0) {
            telemetry.addData("KP is:", yawPIDF.getkP());
        }
        if (yawPIDF.getMode() % 4 == 1) {
            telemetry.addData("KI is:", yawPIDF.getkI());
        }
        if (yawPIDF.getMode() % 4 == 2) {
            telemetry.addData("KD is:", yawPIDF.getkD());
        }
        if (yawPIDF.getMode() % 4 == 3) {
            telemetry.addData("KF is:", yawPIDF.getkF());
        }

        //Debugging telem
        telemetry.addData("Target Translation is:", robot.getTargetTranslation());
        telemetry.addData("Final Yaw is:", robot.getFinalYaw());
    }

}
