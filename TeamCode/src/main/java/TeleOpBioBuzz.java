import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.teamcode.CSVLogger;

import java.io.IOException;

@TeleOp
public class TeleOpBioBuzz extends OpMode {
    TelemetryManager telemetryM;
    MotorsBioBuzz motors;
    CSVLogger logger;
    boolean changePIDMode;
    int statePIDChange;
    int numOfPIDs = 1;
    boolean displayColorSensors;

    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        try {
            logger = new CSVLogger(
                    "Shooter Test Motors",
                    "Raw Error RPM", "Raw Shooter RPM", "Raw Set Shooter RPM", "Raw inRage"
            );
        } catch (IOException e) {
            telemetry.addData("Logger Error", e.getMessage());
        }
        motors = new MotorsBioBuzz();
        motors.init(hardwareMap);
        //motors.setHueRange();
    }

    @Override
    public void loop() {
        // Updates for beggining of loop
        motors.updateDeltaTime(getRuntime());
        telemetry.addData("Delta Time", motors.getDeltaTime());

        /*
        * Controls for indexer
        */
        telemetry.addData("Red tape found", motors.colorSensorFoundTape());
        telemetry.addData("Color sensor HSV[0] is", motors.csHSV[0]);
        telemetry.addData("RGB red is", motors.spindexColor.red());
        motors.manualSpindex(gamepad1.left_stick_y);


        /*
         * PID controls
         */

        //Toggle PID tuning on and off
        if (gamepad2.xWasPressed()) {
            changePIDMode = !changePIDMode;
        }

        //PID tuning
        if (changePIDMode) {
            //Select the PID being changed
            if (gamepad2.leftBumperWasPressed()) {
                statePIDChange += 1;
            }
            //Limit it to the number of PIDs so it cycles back
            if (statePIDChange > numOfPIDs) {
                statePIDChange = 0;
            }
            //Wraps around back for more intuitive selection
            if (statePIDChange < numOfPIDs) {
                statePIDChange = numOfPIDs;
            }
            //Turret PID - change constants and display constants
            if (statePIDChange == 0) { 
                motors.turretPID.tuneConstants(gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());
                motors.turretPID.changeMode(gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());
                telemetryM.addData("Pan PID mode is", motors.turretPID.getMode());
                if (motors.turretPID.getMode() % 4 == 0) {
                    telemetryM.addData("KP is", motors.turretPID.getkP());
                }
                if (motors.turretPID.getMode() % 4 == 1) {
                    telemetryM.addData("KI is", motors.turretPID.getkI());
                }
                if (motors.turretPID.getMode() % 4 == 2) {
                    telemetryM.addData("KD is", motors.turretPID.getkD());
                }
                if (motors.turretPID.getMode() % 4 == 3) {
                    telemetryM.addData("KF is", motors.turretPID.getkF());
                }
            }

        }




        // Updates for end of loop
        logger.log(motors.getError(), motors.getShooterVelocityRpm(), motors.getSetShooterVelocityRpm(), motors.flyVelInBounds01());

        motors.updateTurretPower();


        telemetryM.update();
    }
}
