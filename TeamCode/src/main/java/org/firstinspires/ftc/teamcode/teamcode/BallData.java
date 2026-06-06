package org.firstinspires.ftc.teamcode.teamcode;


public class BallData {
    public int id;
    public double x;
    public double y;
    public double area;
    public double normalizedX;
    public double getNormalizedX () {
        normalizedX = Math.abs(x/Math.sqrt(area));
        return normalizedX;
    }

    public BallData(double x, double y, double area, int id) {
        this.x = x;
        this.y = y;
        this.area = area;
        this.id = id;
    }

}
