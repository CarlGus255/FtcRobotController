package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CarlOdometryExampleImplementation {

    double fieldX = 0;
    double fieldY = 0;
    double fieldYaw = 0;
    double robotX = 0;
    double robotY = 0;
    double robotYaw = 0;
    double aprilX = 141.33;
    double aprilY = 148.19;
    double aprilYaw = -45.0;

    //Used to reset robot XY and Yaw values when homing with camera by making them equal to the current robot XY and Yaw values to offset
    double resetXValue = 0;
    double resetYValue = 0;
    double resetYawValue = 0;
    WebCamCarlCoaxSwerve webcam;
    double lastRobotX = 0;
    double lastRobotY = 0;
    double lastRobotYaw = 0;
    GoBildaPinpointDriver ppo;
    public CarlOdometryExampleImplementation(WebCamCarlCoaxSwerve webcam) {
        this.webcam = webcam;
    }

    public void init (HardwareMap hwMap) {
        ppo = hwMap.get(GoBildaPinpointDriver.class, "odo");
        // offsets in inch from center
        //    ppo.setOffsets(1, -0.0,DistanceUnit.INCH);
        // set pinpoint resolution
        // ppo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        ppo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        // Set the direction that each of the two odometry pods count
        ppo.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);
        ppo.resetPosAndIMU();
    }
    public void resetFieldWebcamRedBin () {
        webcam.update();
        Double dist = webcam.getAprilDistance(24);
        Double bearing = webcam.getAprilBearing(24);

        if (dist != null && bearing != null) {
            double bearingRad = Math.toRadians(bearing);

            fieldX = aprilX - dist * Math.sin(bearingRad);
            fieldY = aprilY - dist * Math.cos(bearingRad);
            fieldYaw = bearing + aprilYaw;

            resetXValue = updateRobotX();
            resetYValue = updateRobotY();
            resetYawValue = updateRobotYaw();

        }
    }

    public double updateRobotX () {
        ppo.update();
        robotX = ppo.getPosX(DistanceUnit.CM) - resetXValue;
        return robotX;
    }
    public double updateRobotY () {
        ppo.update();
        robotY = ppo.getPosY(DistanceUnit.CM) - resetYValue;
        return robotY;
    }

    public double updateRobotYaw () {
        ppo.update();
        robotYaw = ppo.getHeading(AngleUnit.DEGREES) - resetYawValue;
        return robotYaw;
    }

    public double getFieldX () {
        double currentX = updateRobotX();
        double deltaX = currentX - lastRobotX;

        fieldX += deltaX;

        lastRobotX = currentX;
        return fieldX;
    }

    public double getFieldY () {
        double currentY = updateRobotY();
        double deltaY = currentY - lastRobotY;

        fieldY += deltaY;

        lastRobotY = currentY;
        return fieldY;
    }
    public double getFieldYaw () {
        double currentYaw = updateRobotYaw();
        double deltaYaw = currentYaw - lastRobotYaw;

        fieldYaw += deltaYaw;

        lastRobotYaw = currentYaw;
        return fieldYaw;
    }
    public double getShootingDistance () {
        double aprilPosX;
        double aprilPosY;
        double aprilDistance;

        aprilPosX = fieldX - aprilX;
        aprilPosY = fieldY - aprilY;

        aprilDistance = Math.hypot(aprilPosX, aprilPosY);
        return aprilDistance;
    }

    public double getShootingX () {
        double aprilPosX;

        aprilPosX = fieldX - aprilX;
        return aprilPosX;
    }
    public double getShootingY () {
        double aprilPosY;

        aprilPosY = fieldY - aprilY;
        return aprilPosY;
    }


}
