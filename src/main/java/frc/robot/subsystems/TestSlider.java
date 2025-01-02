// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.utilities.Util.logf;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestSlider extends SubsystemBase {
    private MotorSRX motorSRX;
    // private MotorDef motor;
    private XboxController driveHID;
    private int lastPov = -1;

    private LedSubsystem leds;

    public TestSlider(XboxController driveHID, LedSubsystem leds) {
        this.driveHID = driveHID;
        this.leds = leds;
        logf("Start of Test Slider Subsystem\n");
        motorSRX = new MotorSRX("SRX", 1, -1, false);
        motorSRX.setInverted(true);
        motorSRX.enableLimitSwitch(true, true);
    }

    public void resetPos() {
        motorSRX.setEncoderPosition(0);
    }

    public void periodic() {
        // This method will be called once per scheduler run
        // Make sure that you declare this subsystem in RobotContainer.java     
        SmartDashboard.putNumber("Slide Pos", motorSRX.getPos());
        SmartDashboard.putBoolean("FW Limit", motorSRX.getForwardLimitSwitch());
        SmartDashboard.putBoolean("RV Limit", motorSRX.getReverseLimitSwitch());
        SmartDashboard.putNumber("Analog", motorSRX.getAnalogPos());
        int pov = driveHID.getPOV();
        if (pov == -1) {
            double axisL = driveHID.getLeftTriggerAxis();
            double axisR = driveHID.getRightTriggerAxis();
            lastPov = -1;
            if (Math.abs(axisL) > .05) {
                SmartDashboard.putNumber("SliderSP", axisL);
                motorSRX.setSpeed(axisL);
                leds.setColors(127, 0, 0);
                return;
            } else if (Math.abs(axisR) > .05) {
                SmartDashboard.putNumber("SliderSP", axisR);
                motorSRX.setSpeed(-axisR);
                leds.setColors(127, 0, 0);
                return;
            } else {
                motorSRX.setSpeed(0);
            }
            leds.setColors(0, 127, 0);
            return;
        } else {
            if (pov != lastPov) {
                long begin = RobotController.getFPGATime();
                logf("Slider setEncoder %.2f\n", pov / 90.0);
                motorSRX.setPos(pov / 90);
                lastPov = pov;
                SmartDashboard.putNumber("SetPTO", (RobotController.getFPGATime() - begin) / 1000);
            }
        }
    }
}