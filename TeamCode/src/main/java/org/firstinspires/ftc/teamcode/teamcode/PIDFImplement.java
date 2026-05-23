package org.firstinspires.ftc.teamcode.teamcode;


public class PIDFImplement {
    int mode;
    public double kP;
    public double kI;
    public double kD;
    public double kF;
    boolean isKP;
    boolean isKI;
    boolean isKD;
    boolean isKF;
    double scalingFactor;

    public void readMode() {
        if (mode%4 == 0) {
            isKP = true;
        } else {
            isKP = false;
        }
        if (mode%4 == 1) {
            isKI = true;
        } else {
            isKI = false;
        }
        if (mode%4 == 2) {
            isKD = true;
        } else {
            isKD = false;
        }
        if (mode%4 == 3) {
            isKF = true;
        } else {
            isKF = false;
        }

    }
    public void setConstants (double KP, double KI, double KD, double KF, double ScalingFactor) {
        kP = KP;
        kI = KI;
        kD = KD;
        kF = KF;
        scalingFactor = ScalingFactor;
    }
    public void changeMode(boolean increase, boolean decrease) {
        if (increase) {
            mode ++;
        } else if (decrease) {
            mode --;
        }
    }
    public void decreaseConstants(boolean increase, boolean decrease) {
        readMode();
        if (isKP && increase) {
            kP += scalingFactor;
        } else if (isKP && decrease) {
            kP -= scalingFactor;
        }
        if (isKI && increase) {
            kI += scalingFactor;
        } else if (isKI && decrease) {
            kI -= scalingFactor;
        }
        if (isKD && increase) {
            kD += scalingFactor;
        } else if (isKD && decrease) {
            kD -= scalingFactor;
        }
        if (isKF && increase) {
            kF += scalingFactor;
        } else if (isKF && decrease) {
            kF -= scalingFactor;
        }
    }
    public double getkP () {
        return kP;
    }
    public double getkI () {
        return kI;
    }
    public double getkD () {
        return kD;
    }
    public double getkF () {return kF;}
    public int getMode() {
        return mode;
    }
    double proportional;
    double integral;
    double derivative;
    double lastError;
    public double PIDFOutPut(double error, double deltaTime) {
        proportional = error;

        integral += error * deltaTime;

        derivative = (error-lastError)/deltaTime;
        lastError = error;

        return (proportional * kP) + (integral*kI) + (derivative*kD) + (kF);
    }
}
