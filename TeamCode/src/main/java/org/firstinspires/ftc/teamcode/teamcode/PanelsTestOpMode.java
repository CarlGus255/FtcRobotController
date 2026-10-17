package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

@TeleOp
@Disabled
public class PanelsTestOpMode extends OpMode {
    private TelemetryManager telemetryManager;
    @Override
    public void init() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void loop() {
        double time = getRuntime();
        double value = Math.sin(time);

        telemetry.addData("Time", time);
        telemetry.addData("Test Value", value);
        telemetry.update();

        telemetryManager.debug("Test Value:", + value);
        telemetryManager.addData("Test", value + 5);
        telemetryManager.update(telemetry);

    }
}
