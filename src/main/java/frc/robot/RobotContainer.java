package frc.robot;


import static frc.robot.utilities.Util.logf;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DrivetrainSRX;

/**
 * This class is where the bulk of the robot should be declared. Since
 * be declared. Since Command-based is a
 * "declarative" paradigm, very little robot
 * logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).
 * Instead, the structure ofthe robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    public final static CommandXboxController driveController = new CommandXboxController(2);
    public final static CommandXboxController operatorController = new CommandXboxController(3);
    public final static XboxController operatorHID = operatorController.getHID();
    public final static XboxController driveHID = driveController.getHID();
    private static SlewRateLimiter sLX = new SlewRateLimiter(DrivetrainSRX.MAX_VELOCITY_METERS_PER_SECOND);
    private static SlewRateLimiter sLY = new SlewRateLimiter(DrivetrainSRX.MAX_VELOCITY_METERS_PER_SECOND);
    public final static DrivetrainSRX drivetrainSRX = new DrivetrainSRX(driveHID, sLX, sLY);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Set the default Robot Mode to Cube
        switch (Config.robotType) {
            case MiniSRX: // Test mini
                // Use Talon SRX for drive train
                break;
            case Squidward: // Holiday Present Robot
                // Uses Talon SRX for drive train
                break;
            case Kevin: // Ginger Bread Robot
                // Uses Talon SRX for drive train
                break;
            case Wooly: // Big ball shooter
                // Uses Jaguars for drive train and shooter
                // Uses PCM to control shooter tilt and shooter activate
                break;
            case Mando: // Train engine
                // Use SparkMax motors for drive train
                break;
        }
        logf("Creating RobotContainer\n");
        configureButtonBindings();
    }

    public double squareWithSign(double v) {
        return (v < 0) ? -(v * v) : (v * v);
    }

    private void configureButtonBindings() {
       driveHID.getAButton();
    }
}
