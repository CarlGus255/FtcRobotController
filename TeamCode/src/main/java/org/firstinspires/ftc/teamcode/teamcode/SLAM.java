package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SLAM {
    ActuatorsSensorsDiffySwerve motors;
    public SLAM (
            ActuatorsSensorsDiffySwerve motors
    ) {
        this.motors = motors;
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
        motors.ppo.update();
        resetX = motors.ppo.getPosX(DistanceUnit.CM);
        resetY = motors.ppo.getPosY(DistanceUnit.CM);
        resetYaw = motors.ppo.getHeading(AngleUnit.DEGREES);
    }
    public void update() {
        motors.ppo.update();
        x = motors.ppo.getPosX(DistanceUnit.CM);
        y = motors.ppo.getPosY(DistanceUnit.CM);
        yaw = motors.ppo.getHeading(AngleUnit.DEGREES);
        
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
