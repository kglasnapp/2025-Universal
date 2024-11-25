// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.utilities.Util.logf;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  public static int count = 0;
  public static RobotContainer robotContainer;
  public static Optional<Alliance> alliance;
  public static boolean debug = true;
  public static Config config = new Config();
  public static double yaw;



  @Override
  public void robotInit() {
    robotContainer = new RobotContainer();
    alliance = DriverStation.getAlliance();
    logf("Start robot with alliance %s\n", alliance.toString());
  }

  @Override
  public void teleopInit() {
    logf("Start Teleop\n");
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    count++;
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void teleopPeriodic() {

  }
}