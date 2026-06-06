package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ActuatorsSensorsDiffySwerve {
    DcMotorEx leftTopMotor;
    DcMotorEx leftBottomMotor;
    DcMotorEx rightTopMotor;
    DcMotorEx rightBottomMotor;
    GoBildaPinpointDriver ppo;
    Limelight3A limeLight;
    Servo tilt;
    Servo pan;
    double tiltAngle = 0.495;
    double panAngle = 0.52;

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

        tilt = hwMap.get(Servo.class, "tilt");
        pan = hwMap.get(Servo.class, "pan");
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

    public double getTilt() {
        return tiltAngle;
    }
    public double getPan() {
        return panAngle;
    }

    public void changeTilt(double change) {
        tiltAngle = Math.max(0.34, Math.min( 0.7, tiltAngle += change));
    }
    public void changePan(double change) {
        panAngle = Math.max(0.34, Math.min (0.7, panAngle += change));
    }


    /**
     * Now on is limelight logic
     */

    public double [] target = new double[3];
    private int ballCount = 0;
    private List<BallData> balls = new ArrayList<>();
    double totalArea;


    public void LimeLightResult() {
        int id = 0;
        LLResult result = limeLight.getLatestResult();

        balls.clear();

        if (result != null && result.isValid()) {

            target[0] = result.getTx();
            target[1] = result.getTy();
            target[2] = result.getTa();



            for (LLResultTypes.DetectorResult detector : result.getDetectorResults()) {

                double x = detector.getTargetXDegrees();
                double y = detector.getTargetYDegrees();
                double area = detector.getTargetArea();


                balls.add(new BallData(x, y, area, id));

                id ++;
            }
        }

        ballCount = balls.size();
        for (BallData ball : getBalls()) {
            totalArea += ball.area;
        }
    }
    public List<BallData> getBalls() {
        return balls;
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
    /*
    Pairing algorithm - puts balls into pairs based on relative proximity using the area data from limelight and distance from each other using a delta x. Delta y is currently neglected in favor for area because changes in y can be very volatile

     */
    private List<BallPair> pairs = new ArrayList<>();
    public void createPairs() {
        pairs.clear();
        for (int i = 0; i < balls.size(); i++) {

            BallData ballA = balls.get(i);

            for (int j = i + 1; j < balls.size(); j++) {

                BallData ballB = balls.get(j);

                double areaRatio =
                        Math.min(ballA.area, ballB.area)
                                / Math.max(ballA.area, ballB.area);

                double dx =
                        Math.abs(ballA.x - ballB.x);

                boolean similarArea =
                        areaRatio > 0.75;

                boolean closeX =
                        dx < 25;

                if (similarArea && closeX) {

                    pairs.add(
                            new BallPair(ballA, ballB, dx, areaRatio)
                    );
                }
            }
        }
    }

    public List<BallPair> getPairs() {
        return pairs;
    }
    private List<BallCluster> clusters = new ArrayList<>();
    public List<BallData> getConnectedBalls(BallData ball) {

        List<BallData> connected = new ArrayList<>();

        for (BallPair pair : pairs) {

            if (pair.ball1 == ball) {
                connected.add(pair.ball2);
            }

            if (pair.ball2 == ball) {
                connected.add(pair.ball1);
            }
        }

        return connected;
    }
    public void createClusters() {

        clusters.clear();

        List<BallData> visited = new ArrayList<>();

        for (BallData ball : balls) {

            if (visited.contains(ball)) {
                continue;
            }

            BallCluster cluster = new BallCluster();

            Queue<BallData> queue = new LinkedList<>();

            queue.add(ball);
            visited.add(ball);

            while (!queue.isEmpty()) {

                BallData current = queue.remove();

                cluster.balls.add(current);

                for (BallData neighbor : getConnectedBalls(current)) {

                    if (!visited.contains(neighbor)) {

                        visited.add(neighbor);
                        queue.add(neighbor);

                    }
                }
            }

            clusters.add(cluster);
        }
    }

    public List<BallCluster> getClusters() {
        return clusters;
    }

    public double getAverageArea(BallCluster cluster) {
        double total = 0;

        for (BallData ball : cluster.balls) {
            total += ball.area;
        }

        return total / cluster.balls.size();
    }

    public double getCenterX(BallCluster cluster) {
        double total = 0;

        for (BallData ball : cluster.balls) {
            total += ball.x;
        }

        return total / cluster.balls.size();
    }
    public double getCenterY(BallCluster cluster) {
        double total = 0;

        for (BallData ball : cluster.balls) {
            total += ball.y;
        }

        return total / cluster.balls.size();
    }
    final public double ballRadius = 36;
    double LLZOffset = 100;
    double LLXOffset = 50;
    double LLYOffset = 20;
    double LLPitch = 90;
    double LLYaw = 0;

    //Update yaw and pitch as they change because of the tilt/pan mechanism
    public void changeLLYawPitch (double yaw, double pitch) {
        LLYaw = yaw;
        LLPitch = pitch;
    }

    //Input the angle and offset of object, output relative 2d translational xy coordinates

    public double[] getLLXYDistance (double tx, double ty, double zOffset) {
        double deltaZ = zOffset - ballRadius;

        double deltaY = Math.toRadians(LLPitch - ty);
        double radX = Math.toRadians(tx);

        double radius = deltaZ * Math.atan(deltaY);

        double x = radius * Math.sin(radX);
        double y = radius * Math.cos(radX);

        return new double [] {x, y, radius};
    }

}
