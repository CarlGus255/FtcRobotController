package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SLAM {
    GoBildaPinpointDriver ppo;
    public SLAM (
            GoBildaPinpointDriver ppo
    ) {
        this.ppo = ppo;
    }
    double x;
    double y;
    double yaw;
    double resetX;
    double resetY;
    double resetYaw;
    double fieldX;
    double fieldY;
    double fieldYaw;
    public void resetPPO () {
        ppo.update();
        resetX = ppo.getPosX(DistanceUnit.CM);
        resetY = ppo.getPosY(DistanceUnit.CM);
        resetYaw = ppo.getHeading(AngleUnit.DEGREES);
    }
    public void update() {
        ppo.update();
        x = ppo.getPosX(DistanceUnit.CM);
        y = ppo.getPosY(DistanceUnit.CM);
        yaw = ppo.getHeading(AngleUnit.DEGREES);
        
        fieldX = x - resetX;
        fieldY = y - resetY;
        fieldYaw = yaw - resetYaw;
    }
    public double getFieldX () {
        return fieldX;
    }
    public double getFieldY () {
        return fieldY;
    }
    public double getFieldYaw () {
        return fieldYaw;
    }
}
