package frc.robot;

public class RobotMap {
	private static final int mxpOffset = 10;

	//inches per encoder pulse
	public final static double elevatorIPP = (Math.PI * 1.432d * 2) / (1024d * 9d);
	// public final static double elbowIPP;
	// public final static double wristIPP;
	public static final double wheelEncoderIPP = 3 * 6 * Math.PI / 2048;

	//air
	public static final int gearboxPiston = 3;
	public static final int pokePistons = 2;
	public static final int gripperPiston = 1;


	//CAN ID's (dont forget to set the controllers to these numbers, PDP is zero, PCM will be 11?)
	public static final int wristMotor = 5;
	public static final int eleMotor = 3;
	public static final int elbowMotor = 4;
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
	public static final int liftLimitSwitch = 0;

	//Control panel buttons
	public static final int climberUp = 1;
	public static final int climberDown = 2;
	public static final int climberFWD = 3;
	public static final int climberBWD = 4;

	public static final int elevatorHatchHigh = 5;
	public static final int elevatorHatchMid = 6;
	public static final int elevatorHatchLow = 7;
	public static final int elevatorHatchGround = 8;
	public static final int elevatorCargoHigh = 9;
	public static final int elevatorCargoMid = 10;
	public static final int elevatorCargoLow = 11;
	public static final int elevatorCargoGround = 12;
}
