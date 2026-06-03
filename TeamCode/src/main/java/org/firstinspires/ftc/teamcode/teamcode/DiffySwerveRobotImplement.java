package org.firstinspires.ftc.teamcode.teamcode;


public class DiffySwerveRobotImplement {

    DiffySwervePodImplement leftPod;
    DiffySwervePodImplement rightPod;
    PIDFImplement yawPIDF;
    SLAM slam;

    public DiffySwerveRobotImplement (
            DiffySwervePodImplement leftPod,
            DiffySwervePodImplement rightPod,
            PIDFImplement yawPIDF,
            SLAM slam
    )
    {
        this.leftPod = leftPod;
        this.rightPod = rightPod;
        this.yawPIDF = yawPIDF;
        this.slam = slam;
    }
    /*
    Plans:
    input is robot direction, velocity, and yaw, then it breaks down to pod (translation rotation)

    controls robot translation and rotation

    right now: vector + yaw input, pod output
     */
    double velocityTransform = 5; // M/s? -> how fast a +1 translation makes it go
    double targetTranslation;
    double yawTransform;
    double leftTranslation;
    double rightTranslation;
    double finalYaw;

    double yawChange;
    double seekYaw;
    double lastTime;
    double direction;
    double yaw;


    public void setForwardVelocity (double velocity) {
        targetTranslation = Math.max(-1, Math.min(1,(velocity/velocityTransform)));
    }
    public void setDirection () {
        leftPod.setHeading(direction);
        rightPod.setHeading(direction);
    }
    public void changeDirection (double change) {
        direction += change;
    }
    public void changeYaw (double change) {
        seekYaw += change;
    }

    public void setYawPIDF (double runTime) {
        yawChange = seekYaw - slam.getFieldYaw();
        yaw = yawPIDF.PIDFOutPut(yawChange, runTime-lastTime);
        lastTime = runTime;
    }
    public void setAdjustedVelocity (double velocity) {
        targetTranslation = Math.max(-1, Math.min(1,(velocity/velocityTransform)));
        finalYaw = Math.max(-0.5, Math.min(0.5, yaw));
        leftTranslation = (targetTranslation - finalYaw);
        rightTranslation = (targetTranslation + finalYaw);
    }
    public void update(double runTime) {
        setDirection();
        setYawPIDF(runTime);
        leftPod.setTranslation(leftTranslation);
        rightPod.setTranslation(rightTranslation);
    }

    //Debugging getters
    public double getFinalYaw () {
        return finalYaw;
    }
    public double getTargetTranslation () {
        return targetTranslation;
    }
}
