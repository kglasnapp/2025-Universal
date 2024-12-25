// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import static frc.robot.utilities.Util.logf;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class DrivetrainSRX extends SubsystemBase {
  public final static double MAX_VELOCITY_METERS_PER_SECOND = .1;
  TalonSRX talonDriveRight = new TalonSRX(Robot.config.driveRight);
  TalonSRX talonDriveLeft = new TalonSRX(Robot.config.driveLeft);
  XboxController driveController;
  private static SlewRateLimiter sLX = new SlewRateLimiter(DrivetrainSRX.MAX_VELOCITY_METERS_PER_SECOND);
  private static SlewRateLimiter sLY = new SlewRateLimiter(DrivetrainSRX.MAX_VELOCITY_METERS_PER_SECOND);

  public DrivetrainSRX(XboxController driveController) {
    
    logf("Start of Drive Train for SRX Subsystem\n");
    this.driveController = driveController;
    talonDriveRight.configFactoryDefault();
    talonDriveRight.setInverted(false); // pick CW versus CCW when motor controller is positive/green
    talonDriveLeft.configFactoryDefault();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Make sure that you declare this subsystem in RobotContainer.java
    double leftStick = -driveController.getLeftY();
    sLX.calculate(leftStick);
    talonDriveLeft.set(ControlMode.PercentOutput, leftStick); 
    sLY.calculate(leftStick);
    double rightStick = driveController.getRightY(); // make forward stick positive
    talonDriveRight.set(ControlMode.PercentOutput, rightStick);
    if (Robot.count % 250 == -1) { // -1 will disable the log, set to 0 to enable log
      logf("Drive stick left:%.2f right:%.2f\n", leftStick, rightStick);
    }
    SmartDashboard.putNumber("Right Stick", rightStick);
    SmartDashboard.putNumber("Left Stick", leftStick);
  }

}