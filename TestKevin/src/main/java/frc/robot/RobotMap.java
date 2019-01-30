package frc.robot;

public class RobotMap {
	private static final int mxpOffset = 10;

	public final static double elevatorIPP = (Math.PI * 1.432d * 2) / (1024d * 9d);
	// public static final double inchesPerPulse = 6 * Math.PI * 3 * 3 / 1024;
	// public static final double inchesPerPulse = 4;

	// Comp Robot
	public static final double wheelEncoderIPP = 3 * 6 * Math.PI / 2048;
	// // Test Robot
	// public static final double wheelEncoderLIPP = 6 * Math.PI / 1024;
	// public static final double wheelEncoderRIPP = 6 * Math.PI / 2048;

	public static final int drivemotorRight = 8;
	public static final int drivemotorLeft = 9;
	public static final int gearboxPiston = 3;
	// public static final int leftEncoderA = 2 + mxpOffset;
	// public static final int leftEncoderB = 3 + mxpOffset;
	// public static final int rightEncoderA = 11 + mxpOffset;
	// public static final int rightEncoderB = 4 + mxpOffset;

	public static final int liftMotor = 0;
	public static final int liftEncoderA = 0 + mxpOffset;
	public static final int liftEncoderB = 1 + mxpOffset;

	public static final int intakeMotorL = 1;
	public static final int intakeMotorR = 2;
	public static final int intakePiston = 2;

	public static final int wristMotor = 4;
	public static final int elbowMotor = 5;

	public static final int armPiston = 7;

	public static final int climberPiston = 1;
	public static final int climberMotor = 3;

	public static final int wheelieBarPiston = 0;

	public static final int ledR = 4;
	public static final int ledG = 5;
	public static final int ledB = 6;

}
