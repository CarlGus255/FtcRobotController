package org.firstinspires.ftc.teamcode.teamcode;

public class TimerImplement {
    private double startTime;
    double duration;
    private boolean timeStarted;
    private void setStartTime (double runtime) {
        startTime = runtime;
    }

    public void startTimer (double runtime) {
        if (!timeStarted) {
            setStartTime(runtime);
            timeStarted = true;
        }
        duration = runtime - startTime;
    }
    public double getDuration () {
        return duration;
    }
    public void resetTimer () {
        timeStarted = false;
    }
    boolean timeMet;
    public boolean setTimer (double runtime, double setTime) {
        startTimer(runtime);
        if (duration < setTime) {
            timeMet = false;
        } else if (duration >= setTime) {
            timeMet = true;
        }
        return timeMet;
    }
}
