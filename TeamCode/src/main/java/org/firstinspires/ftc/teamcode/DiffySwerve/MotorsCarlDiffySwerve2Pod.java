package org.firstinspires.ftc.teamcode.DiffySwerve;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorsCarlDiffySwerve2Pod {

        private DcMotorEx LeftTopMotor;
        private DcMotorEx LeftBottomMotor;
        private DcMotorEx RightTopMotor;
        private DcMotorEx RightBottomMotor;
        private DigitalChannel LeftHomingSwitch;
        private DigitalChannel RightHomingSwitch;
        
        double LeftTopMotorTicksPerRev;
        double LeftBottomMotorTicksPerRev;
        double RightTopMotorTicksPerRev;
        double RightBottomMotorTicksPerRev;

        public void init(HardwareMap hwMap){
            LeftTopMotor = hwMap.get(DcMotorEx.class, "LeftTopMotor");
            LeftTopMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            LeftBottomMotor = hwMap.get(DcMotorEx.class, "LeftBottomMotor");
            LeftBottomMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            RightTopMotor = hwMap.get(DcMotorEx.class, "RightTopMotor");
            RightTopMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            RightBottomMotor = hwMap.get(DcMotorEx.class, "RightBottomMotor");
            RightBottomMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            LeftHomingSwitch = hwMap.get(DigitalChannel.class, "LeftHomingSwitch");
            LeftHomingSwitch.setMode(DigitalChannel.Mode.INPUT);

            RightHomingSwitch = hwMap.get(DigitalChannel.class, "RightHomingSwitch");
            RightHomingSwitch.setMode(DigitalChannel.Mode.INPUT);

            LeftTopMotorTicksPerRev = LeftTopMotor.getMotorType().getTicksPerRev();
            LeftBottomMotorTicksPerRev = LeftBottomMotor.getMotorType().getTicksPerRev();
            RightTopMotorTicksPerRev = RightTopMotor.getMotorType().getTicksPerRev();
            RightBottomMotorTicksPerRev = RightBottomMotor.getMotorType().getTicksPerRev();

        }


        public void setLeftTopMotor (double speed) {
            LeftTopMotor.setPower(speed);
        }

        public void setLeftBottomMotor (double speed) {
            LeftBottomMotor.setPower(speed);
        }
        public void setRightTopMotor (double speed) {
            RightTopMotor.setPower(speed);
        }

        public void setRightBottomMotor (double speed) {
            RightBottomMotor.setPower(speed);
        }


        public double getLeftTopMotorPos () {
            return LeftTopMotor.getCurrentPosition() / LeftTopMotorTicksPerRev;
        }
        public double getLeftBottomMotorPos () {
            return LeftBottomMotor.getCurrentPosition() / LeftBottomMotorTicksPerRev;
        }
        public double getRightTopMotorPos () {
            return RightTopMotor.getCurrentPosition() / RightTopMotorTicksPerRev;
        }
        public double getRightBottomMotorPos () {
            return RightBottomMotor.getCurrentPosition() / RightBottomMotorTicksPerRev;
        }


        public boolean getLeftHomingSwitchState () {
            return LeftHomingSwitch.getState();
        }

        public boolean getRightHomingSwitchState () {
            return RightHomingSwitch.getState();
        }


        public void resetLeftTopMotor () {
            LeftTopMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            LeftTopMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        public void resetLeftBottomMotor () {
            LeftBottomMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            LeftBottomMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        public void resetRightTopMotor () {
            RightTopMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            RightTopMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        public void resetRightBottomMotor () {
            RightBottomMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            RightBottomMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }


        public double getLeftTopMotorVelocity () {
            return (LeftTopMotor.getVelocity())*60/LeftTopMotorTicksPerRev;
        }

        public double getLeftBottomMotorVelocity () {
            return LeftBottomMotor.getVelocity()*60/LeftBottomMotorTicksPerRev;
        }
        public double getRightTopMotorVelocity () {
            return (RightTopMotor.getVelocity())*60/LeftTopMotorTicksPerRev;
        }

        public double getRightBottomMotorVelocity () {
            return RightBottomMotor.getVelocity()*60/LeftBottomMotorTicksPerRev;
        }


    }