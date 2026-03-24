package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CarlOdometryExampleImplementation {

    double fieldX;
    double fieldY;
    double fieldYaw;
    double robotX;
    double robotY;
    double robotYaw;
    double aprilX = 141.33;
    double aprilY = 148.19;
    double aprilYaw = -45.0;

    //Used to reset robot XY and Yaw values when homing with camera by making them equal to the current robot XY and Yaw values to offset
    double resetXValue = 0;
    double resetYValue = 0;
    double resetYawValue = 0;

    GoBildaPinpointDriver ppo;
    WebCamCarlCoaxSwerve webcam = new WebCamCarlCoaxSwerve();
    CarlCoaxSwerveDecodeImplementation swerve = new CarlCoaxSwerveDecodeImplementation();

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
        // Centered with the origin at the middle of the field. Values are approximate based on CAD of the field.
        fieldX = aprilX - webcam.getAprilDistance(20)*Math.sin(webcam.getAprilBearing(20)); // 141.33 should be X distance from the middle of the field to the april tag in cm, same goes with the Y
        fieldY = aprilY - webcam.getAprilDistance(20)*Math.cos(webcam.getAprilBearing(20));
        fieldYaw = webcam.getAprilBearing(20) + aprilYaw; // makes it so zero is straight ahead, and I believe it should increase clockwise


        resetXValue = updateRobotX();
        resetYValue = updateRobotY();
        resetYawValue = updateRobotYaw();
    }

    public double updateRobotX () {
        robotX = ppo.getPosX(DistanceUnit.CM) - resetXValue;
        return robotX;
    }
    public double updateRobotY () {
        robotY = ppo.getPosY(DistanceUnit.CM) - resetYValue;
        return robotY;
    }

    public double updateRobotYaw () {
        robotYaw = ppo.getHeading(AngleUnit.DEGREES) - resetYawValue;
        return robotYaw;
    }

    public double getFieldX () {
        fieldX = fieldX + updateRobotX();
        return fieldX;
    }

    public double getFieldY () {
        fieldY = fieldY + updateRobotY();
        return fieldY;
    }

    public double getFieldYaw () {
        fieldYaw = fieldYaw + updateRobotYaw();
        return fieldYaw;
    }


}
