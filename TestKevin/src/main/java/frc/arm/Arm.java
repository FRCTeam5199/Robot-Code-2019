package frc.arm;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.util.SparkMaxPID;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController;

public class Arm {

    private final SparkMaxPID elbowMotor;
    //private final SparkMaxPID wristMotor;
    private final CANEncoder elbowEncoder;
    //private final CANEncoder wristEncoder;
    private final CANPIDController elbowPID;
    //private final CANPIDController wristPID;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
    public double rotations;

    public Arm() {

        kP = 0.1; 
        kI = 1e-4;
        kD = 1; 
        kIz = 0; 
        kFF = 0; 
        kMaxOutput = 1;
        kMinOutput = -1;

        elbowMotor = new SparkMaxPID(RobotMap.elbowMotor, MotorType.kBrushless);
        //wristMotor = new SparkMaxPID(RobotMap.wristMotor, MotorType.kBrushless);
        elbowEncoder = elbowMotor.getEncoder();
        //wristEncoder = wristMotor.getEncoder();

        elbowPID = elbowMotor.getPIDController();
        //wristPID = wristMotor.getPIDController();
        elbowPID.setP(kP);
        elbowPID.setI(kI);
        elbowPID.setD(kD);
        elbowPID.setIZone(kIz);
        elbowPID.setFF(kFF);
        elbowPID.setOutputRange(kMinOutput, kMaxOutput);
    }

    public void initAdjustPID(){
		SmartDashboard.putNumber("Elbow P", 0);
		SmartDashboard.putNumber("Elbow I", 0);
        SmartDashboard.putNumber("Elbow D", 0);

        SmartDashboard.putNumber("Elbow Rotations", 0);

        // SmartDashboard.putNumber("Wrist P", 0);
		// SmartDashboard.putNumber("Wrist I", 0);
		// SmartDashboard.putNumber("Wrist D", 0);
    }
    
    public void adjustPID() {
		elbowPID.setP(SmartDashboard.getNumber("Elbow P", 0));
		elbowPID.setI(SmartDashboard.getNumber("Elbow I", 0));
        elbowPID.setD(SmartDashboard.getNumber("Elbow D", 0));

        rotations = SmartDashboard.getNumber("Elbow Rotations", 0);

	}
    
    public void setElbowMotor(double speed) {
        elbowMotor.set(speed);
    }

    public double getElbowPosition() {
        return elbowEncoder.getPosition();
    }

    public double getElbowVelocity() {
        return elbowEncoder.getVelocity();
    }

    public CANEncoder getElbowEncoder() {
        return elbowEncoder;
    }

    public void setElbowGoal(double r, ControlType c){
        SmartDashboard.putNumber("SetPoint", rotations);
        SmartDashboard.putNumber("ProcessVariable", elbowEncoder.getPosition());
        elbowPID.setReference(r, c);
    }

    // public void setWristMotor(double speed) {
    //     wristMotor.set(speed);
    // }

    // public double getWristPosition() {
    //     return wristEncoder.getPosition();
    // }

    // public double getWristVelocity() {
    //     return wristEncoder.getVelocity();
    // }

    // public CANEncoder getWristEncoder() {
    //     return wristEncoder;
    // }

}