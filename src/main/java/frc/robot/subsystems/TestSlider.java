// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.utilities.Util.logf;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class TestSlider extends SubsystemBase {
    private MotorSRX motorSRX;
    // private MotorDef motor;
    private XboxController driveHID;
    private int lastPov = -1;
    private PID positionPID;

    private enum STATES {
        IDLE, HOMING, HOMED, MOVE_PID, MOVE_BUTTON,
    };

    private STATES state = STATES.IDLE;
    private LedSubsystem leds;
    private double setPoint = 0;
    private int pidTimeout = 0;

    public TestSlider(XboxController driveHID, LedSubsystem leds) {
        this.driveHID = driveHID;
        this.leds = leds;
        logf("Start of Test Slider Subsystem\n");
        motorSRX = new MotorSRX("SRX", 1, -1, false);
        motorSRX.setInverted(true);
        motorSRX.setSensorPhase(true);
        motorSRX.enableLimitSwitch(true, true);
        positionPID = new PID("Drive", 2, 0, 0, 0, 0, -1, 1, true);
        positionPID.showPID();
        motorSRX.setPositionPID(positionPID, FeedbackDevice.CTRE_MagEncoder_Relative); // set pid for SRX
    }

    public void resetPos() {
        motorSRX.setEncoderPosition(0);
    }

    public void periodic() {
        // This method will be called once per scheduler run
        if (Robot.count % 5 == 0) {
            SmartDashboard.putNumber("Slide Pos", motorSRX.getPos());
            SmartDashboard.putBoolean("FW Limit", motorSRX.getForwardLimitSwitch());
            SmartDashboard.putBoolean("RV Limit", motorSRX.getReverseLimitSwitch());
            SmartDashboard.putNumber("Analog", motorSRX.getAnalogPos());
            SmartDashboard.putNumber("Set Point", setPoint);
            SmartDashboard.putString("State", state.toString());
        }
        if (positionPID.PIDChanged) {
            motorSRX.setPositionPID(positionPID, FeedbackDevice.CTRE_MagEncoder_Relative); // set pid for SRX
            positionPID.PIDChanged = false;
        }
        switch (state) {
            case IDLE:
                state = STATES.HOMING;
                motorSRX.setSpeed(-.3);
                leds.setColors(127, 127, 0);
                break;
            case HOMING:
                if (!motorSRX.getReverseLimitSwitch()) {
                    logf("Slider Homed\n");
                    resetPos();
                    state = STATES.HOMED;
                    leds.setColors(0, 127, 0);
                }
                break;
            case HOMED:
                int pov = driveHID.getPOV();
                if (pov == -1) {
                    lastPov = -1;
                    if (Math.abs(driveHID.getLeftTriggerAxis()) > .05) {
                        leds.setColors(127, 0, 0);
                        state = STATES.MOVE_BUTTON;
                        return;
                    }
                    if (Math.abs(driveHID.getRightTriggerAxis()) > .05) {
                        leds.setColors(127, 0, 0);
                        state = STATES.MOVE_BUTTON;
                        return;
                    }
                } else {
                    if (pov != lastPov) {
                        long begin = RobotController.getFPGATime();
                        setPoint = pov * 80 + 400;
                        logf("Slider setEncoder %.3f\n", setPoint);
                        motorSRX.setPos(setPoint);
                        lastPov = pov;
                        SmartDashboard.putNumber("SetPTO", (RobotController.getFPGATime() - begin) / 1000);
                        state = STATES.MOVE_PID;
                        pidTimeout = 50 * 4;
                        leds.setColors(0, 0, 127);
                    }
                }
                break;
            case MOVE_PID:
                double error = Math.abs(motorSRX.getPos() - setPoint);
                if (error < 20) {
                    state = STATES.HOMED;
                    logf("Reached Set Point error:%.2f\n", error);
                    leds.setColors(0, 127, 0);
                    break;
                }
                pidTimeout--;
                if (pidTimeout < 0) {
                    logf("PID Timed out error:%.2f\n", error);
                    motorSRX.setSpeed(0);
                    state = STATES.HOMED;
                    leds.setColors(0, 127, 0);
                    break;
                }
                break;
            case MOVE_BUTTON:
                double axisL = driveHID.getLeftTriggerAxis();
                double axisR = driveHID.getRightTriggerAxis();
                if (axisL > .05) {
                    motorSRX.setSpeed(axisL);
                    break;
                }
                if (axisR > .05) {
                    motorSRX.setSpeed(-axisR);
                    break;
                }
                motorSRX.setSpeed(0);
                leds.setColors(0, 127, 0);
                state = STATES.HOMED;
                break;
        }
    }
}