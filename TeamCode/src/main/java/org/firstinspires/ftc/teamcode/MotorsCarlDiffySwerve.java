package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorsCarlDiffySwerve {

    private DcMotorEx topMotor;
    private DcMotorEx bottomMotor;

    private DigitalChannel homingSwitch;
    double topMotorTicksPerRev;
    double bottomMotorTicksPerRev;

    public void init(HardwareMap hwMap){
        topMotor = hwMap.get(DcMotorEx.class, "topMotor");
        topMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        bottomMotor = hwMap.get(DcMotorEx.class, "bottomMotor");
        bottomMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        homingSwitch = hwMap.get(DigitalChannel.class, "homingSwitch");
        homingSwitch.setMode(DigitalChannel.Mode.INPUT);

        topMotorTicksPerRev = topMotor.getMotorType().getTicksPerRev();
        bottomMotorTicksPerRev = bottomMotor.getMotorType().getTicksPerRev();
    }

    public void setTopMotor (double speed) {
        topMotor.setPower(speed);
    }

    public void setBottomMotor (double speed) {
        bottomMotor.setPower(speed);
    }

    public double getTopMotorPos () {
        return topMotor.getCurrentPosition() / topMotorTicksPerRev;
    }
    public double getBottomMotorPos () {
        return bottomMotor.getCurrentPosition() / bottomMotorTicksPerRev;
    }

    public boolean getHomingSwitchState () {
        return homingSwitch.getState();
    }

    public void resetTopMotor () {
        topMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        topMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }
    public void resetBottomMotor () {
        bottomMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bottomMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public double getTopMotorVelocity () {
        return (topMotor.getVelocity())*60/topMotorTicksPerRev;
    }

    public double getBottomMotorVelocity () {
        return bottomMotor.getVelocity()*60/bottomMotorTicksPerRev;
    }
}
