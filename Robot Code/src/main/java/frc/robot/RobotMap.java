package frc.robot;

public class RobotMap {
	private static final int mxpOffset = 10;

	//air
	public static final int gearboxPistonB = 0;
	public static final int gearboxPistonA = 1;
	public static final int pokePistons = 3;
	public static final int climbPistons = 4;
	public static final int gripperPiston = 2;

	//CAN bus device ID's
	public static final int wristMotor = 5;
	public static final int eleMotor = 3;
	public static final int elbowMotor = 4; //using this guy for testing drivetrain motors through spark max client
	public static final int driveLeaderRight = 2;
	public static final int driveLeaderLeft = 1;
	public static final int	driveSlaveRightA = 6;
	public static final int	driveSlaveLeftA = 7;
	public static final int driveSlaveRightB = 8;
	public static final int	driveSlaveLeftB = 9;
	public static final int CANGyro = 10;
	public static final int winchMotor = 11;
	public static final int liftDriveMotor = 12;
	public static final int intakeMotor = 13;


	//Digital I/Os
	public static final int liftLimitSwitch = 9;
	// public static final int wristLimitSwitch = 8;
	// public static final int elbowLimitSwitch = 7;
	//there are no limit switches on the arm


	//Control Panel buttons (needs the rest, could use better names)
	public static final int climberUp = 1;
	public static final int climberDown = 2;
	public static final int climberFWD = 3;
	public static final int climberBWD = 4;
	public static final int hatchHigh = 5;
	public static final int hatchMid = 6;
	public static final int hatchLow = 7;
	public static final int hatchGround = 8;
	public static final int cargoHigh = 9;
	public static final int cargoMid = 10;
	public static final int cargoLow = 11;
	public static final int cargoGround = 12;

	// PWM
	public static final int clutchServo = 0;
	
}
