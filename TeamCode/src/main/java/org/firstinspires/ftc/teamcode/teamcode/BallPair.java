package org.firstinspires.ftc.teamcode.teamcode;

public class BallPair {
    public BallData ball1;
    public BallData ball2;

    public double dx;
    public double areaRatio;

    public BallPair(
            BallData ball1,
            BallData ball2,
            double dx,
            double areaRatio) {

        this.ball1 = ball1;
        this.ball2 = ball2;
        this.dx = dx;
        this.areaRatio = areaRatio;
    }
}
