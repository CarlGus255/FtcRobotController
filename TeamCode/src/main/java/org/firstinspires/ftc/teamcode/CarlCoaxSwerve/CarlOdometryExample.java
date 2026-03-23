package org.firstinspires.ftc.teamcode.CarlCoaxSwerve;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class CarlOdometryExample {
    private LinearOpMode myOp = null;

    double x;
    double y;
    double yaw;
    double fieldX;
    double fieldY;
    double fieldYaw;
    public CarlOdometryExample (LinearOpMode opmode) {
        myOp = opmode;
    }
    GoBildaPinpointDriver ppo;
    public void init () {
        ppo = myOp.hardwareMap.get(GoBildaPinpointDriver.class, "odo");
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
        myOp.telemetry.addData("odometry status:", ppo.getDeviceStatus());
        myOp.telemetry.update();
    }
}
