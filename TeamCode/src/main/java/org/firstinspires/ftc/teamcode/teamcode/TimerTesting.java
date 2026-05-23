package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TimerTesting extends OpMode {

    TimerImplement timer1;

    @Override
    public void init() {
        timer1 = new TimerImplement();
    }

    @Override
    public void loop() {
        timer1.startTimer(getRuntime());
        timer1.setTimer(getRuntime(), 5);

        if (gamepad1.xWasPressed()) {
            timer1.resetTimer();
        }


        telemetry.addData("Duration is:", timer1.getDuration());
        telemetry.addData("It's been 5 seconds:", timer1.timeMet);
    }
}
