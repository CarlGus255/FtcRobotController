package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestLimeLight extends OpMode {
    ActuatorsSensorsDiffySwerve motors;
    PIDFImplement tiltPID;
    PIDFImplement panPID;
    double tilt = 0.495;
    double pan = 0.52;
    double lastTime;
    double deltaTime;
    boolean panPIDMode = true;


    @Override
    public void init() {
        motors = new ActuatorsSensorsDiffySwerve();
        tiltPID = new PIDFImplement();
        panPID = new PIDFImplement();

        tiltPID.setConstants(0.0000065, 0, 0, 0, 0.0000001);
        panPID.setConstants(0.00004, 0.000005, 0, 0, 0.000001);

        motors.init(hardwareMap);
        motors.limeLight.start();
        motors.limeLight.pipelineSwitch(1);
        motors.tilt.setPosition(motors.getTilt());
        motors.pan.setPosition(motors.getPan());
    }

    @Override
    public void loop() {
        motors.LimeLightResult();
        motors.createPairs();
        motors.createClusters();
        deltaTime = getRuntime() - lastTime;


        //telemetry.addData("LimeLight Target X is:", motors.getTarget(0));
        //telemetry.addData("LimeLight Target Y is:", motors.getTarget(1));
        //telemetry.addData("LimeLight Target Area is:", motors.getTarget(2));

        telemetry.addData("Ball Count is:", motors.getBallCount());

        int i = 1;
        for (BallData ball : motors.getBalls()) {
            telemetry.addData("Ball " + i + " X", ball.x);
            telemetry.addData("Ball " + i + " Y", ball.y);
            telemetry.addData("Ball " + i + " Area", ball.area);
            telemetry.addData("Ball" + i + "Id", ball.id);
            telemetry.addData("Ball" + i + "Normalized X", ball.getNormalizedX());
            i++;
        }
        /*
        for (BallPair pair : motors.getPairs()) {

            telemetry.addData("Pair", pair.ball1.id + "," + pair.ball2.id);
            telemetry.addData("dx", pair.dx);
            telemetry.addData("area", pair.areaRatio);
        }

         */

        int clusterNum = 1;

        for (BallCluster cluster : motors.getClusters()) {

            telemetry.addData(
                    "Cluster " + clusterNum,
                    cluster.balls.size()
            );

            clusterNum++;
        }
        telemetry.addData("Pairs", motors.getPairs().size());
        int j = 1;

        for (BallCluster cluster : motors.getClusters()) {
            double x = motors.getCenterX(cluster);
            double y = motors.getCenterY(cluster);
            telemetry.addData(
                    "Cluster " + j,
                    "size=%d avgArea=%.3f x=%.3f y=%.3f relX=%.3f relY=%.3f relRadius=%.3f",
                    cluster.balls.size(),
                    motors.getAverageArea(cluster),
                    x,
                    y,
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [0],
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [1],
                    motors.getLLXYDistance(x,y, motors.LLZOffset) [2]
            );

            j++;
        }

        //PID tuning for tilt pan
        if (panPIDMode) {
            panPID.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
            panPID.decreaseConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
            telemetry.addData("Pan PID mode is:", panPID.getMode());
            if (panPID.getMode() % 4 == 0) {
                telemetry.addData("KP is:", "kP=%.6f", panPID.getkP());
            }
            if (panPID.getMode() % 4 == 1) {
                telemetry.addData("KI is:", "kI=%.6f", panPID.getkI());
            }
            if (panPID.getMode() % 4 == 2) {
                telemetry.addData("KD is:", "kD=%.6f", panPID.getkD());
            }
            if (panPID.getMode() % 4 == 3) {
                telemetry.addData("KF is:", "%.6f", panPID.getkF());
            }
        } else {
            tiltPID.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
            tiltPID.decreaseConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
            telemetry.addData("Tilt PID mode is:", tiltPID.getMode());
            if (tiltPID.getMode() % 4 == 0) {
                telemetry.addData("KP is:", "kP=%.6f", tiltPID.getkP());
            }
            if (tiltPID.getMode() % 4 == 1) {
                telemetry.addData("KI is:", tiltPID.getkI());
            }
            if (tiltPID.getMode() % 4 == 2) {
                telemetry.addData("KD is:", tiltPID.getkD());
            }
            if (tiltPID.getMode() % 4 == 3) {
                telemetry.addData("KF is:", tiltPID.getkF());
            }
            
        }
        if (gamepad2.aWasPressed()) {
            panPIDMode = !panPIDMode;
        }
        telemetry.addData("Pan PID Mode", panPIDMode);

        motors.changePan(panPID.PIDFOutPut(-motors.limeLight.getLatestResult().getTx(), deltaTime));
        motors.changeTilt(tiltPID.PIDFOutPut(motors.limeLight.getLatestResult().getTy(), deltaTime));
        motors.pan.setPosition(motors.getPan());
        motors.tilt.setPosition(motors.getTilt());

        lastTime = getRuntime();

        telemetry.addData("Tilt is:", motors.getTilt());
        telemetry.addData("Pan is:", motors.getPan());
        telemetry.addData("Pan PID out is:", panPID.PIDFOutPut(motors.limeLight.getLatestResult().getTx(), deltaTime));
    }



}
