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
	// still need intake motor, winch motor, and liftdrive motor


}
