package edu.ATA.twolf;

import edu.ATA.commands.BangBangCommand;
import edu.ATA.commands.ShootCommand;
import edu.ATA.main.PortMap;
import edu.ATA.main.Robot;
import edu.ATA.module.actuator.SolenoidModule;
import edu.ATA.module.driving.ArcadeBinding;
import edu.ATA.module.driving.GearShift;
import edu.ATA.module.driving.RobotDriveModule;
import edu.ATA.module.joystick.XboxController;
import edu.ATA.module.sensor.HallEffectModule;
import edu.ATA.module.sensor.PotentiometerModule;
import edu.ATA.module.speedcontroller.SpeedControllerModule;
import edu.ATA.module.speedcontroller.SpikeRelay;
import edu.ATA.module.speedcontroller.SpikeRelayModule;
import edu.ATA.module.subsystems.ShiftingDrivetrain;
import edu.ATA.module.subsystems.Shooter;
import edu.ATA.module.target.BangBangModule;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class TheWolf extends Robot implements PortMap {

    // Setpoint for shooter!
    private static final double SETPOINT = SmartDashboard.getNumber("ShooterSetpoint", 4500);
    // Setpoint for shooter!
    private static TheWolf theWolf;
    private final SpeedControllerModule leftBack = new SpeedControllerModule(new Victor(DRIVE[0])),
            leftFront = new SpeedControllerModule(new Victor(DRIVE[1])),
            rightBack = new SpeedControllerModule(new Victor(DRIVE[2])),
            rightFront = new SpeedControllerModule(new Victor(DRIVE[3]));
    private final SolenoidModule firstGear = new SolenoidModule(new Solenoid(GEAR_1)),
            secondGear = new SolenoidModule(new Solenoid(GEAR_2));
    private final SolenoidModule loader = new SolenoidModule(new Solenoid(LOADER_PORT)),
            reloader = new SolenoidModule(new Solenoid(RELOADER_PORT));
    private final SpeedControllerModule shooterAligner = new SpeedControllerModule(new Victor(SHOOTER_ALIGNMENT_PORT));
    private final PotentiometerModule shooterAngle = new PotentiometerModule(new AnalogChannel(SHOOTER_POSITION));
    private final SpeedControllerModule shooter = new SpeedControllerModule(new Talon(SHOOTER_PORT));
    private final HallEffectModule hallEffect = new HallEffectModule(new DigitalInput(HALLEFFECT_PORT));
    private final RobotDriveModule drive = new RobotDriveModule(new RobotDrive(leftFront, leftBack, rightFront, rightBack));
    private final XboxController WOLF_CONTROL = new XboxController(new Joystick(JOYSTICK_1));
    /*
     * Subsystems 
     */
    private final ShiftingDrivetrain WOLF_DRIVE = new ShiftingDrivetrain(drive, WOLF_CONTROL, firstGear, secondGear);
    private final Shooter WOLF_SHOOT = new Shooter(loader, reloader, shooterAngle, shooterAligner);
    private final BangBangModule WOLF_SHOOTER = new BangBangModule(hallEffect, shooter);

    public static Robot fetchTheHound() {
        return (theWolf == null) ? (theWolf = new TheWolf()) : (theWolf);
    }

    private TheWolf() {
        SmartDashboard.putNumber("ShooterSetpoint", SETPOINT);
    }

    public void robotInit() {
        SpikeRelayModule compressor = new SpikeRelayModule(new Relay(COMPRESSOR));
        compressor.enable();
        compressor.set(SpikeRelay.FORWARD);
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        WOLF_SHOOT.enable();
        WOLF_SHOOTER.enable();
        WOLF_CONTROL.enable();
        WOLF_DRIVE.enable();

        WOLF_CONTROL.removeAllBinds();
        // Driving //
        WOLF_CONTROL.bindAxis(XboxController.RIGHT_X, new ArcadeBinding(drive, ArcadeBinding.ROTATE));
        WOLF_CONTROL.bindAxis(XboxController.LEFT_Y, new ArcadeBinding(drive, ArcadeBinding.FORWARD));
        WOLF_CONTROL.bindWhenPressed(XboxController.LEFT_BUMPER, new GearShift(secondGear, firstGear));
        // Shooting //
        WOLF_CONTROL.bindWhenPressed(XboxController.RIGHT_BUMPER, new ShootCommand(WOLF_SHOOT));
        WOLF_CONTROL.bindWhenPressed(XboxController.A, new BangBangCommand(WOLF_SHOOTER, SETPOINT));
        WOLF_CONTROL.bindWhenPressed(XboxController.B, new BangBangCommand(WOLF_SHOOTER, 0));
        WOLF_SHOOTER.setSetpoint(SETPOINT);
    }

    public void teleopPeriodic() {
        WOLF_CONTROL.doBinds();
    }

    public void testInit() {
    }

    public void testPeriodic() {
    }
}
