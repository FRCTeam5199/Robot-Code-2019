package frc.arm;

import frc.robot.Robot;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANEncoder;
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController;

public class Arm {

    private final CANSparkMax elbowMotor;
    private final CANSparkMax wristMotor;
    private final CANEncoder elbowEncoder;
    private final CANEncoder wristEncoder;
    private final CANPIDController elbowPID;
    private final CANPIDController wristPID;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
    public double rotations, ePos, wPos, wristRatio, elbowRatio;

    public Arm() {

        /* 3 lbs 4 ounces
            Empirical Stall Torque: 2.6 Nm
            Hall-Sensor Encoder Resolution: 42 counts per rev.
            Empirical Motor Kv: 473 Kv
            Empirical Free Speed: 5676 RPM
            Empirical Stall Current: 105 A
            d to center of mass: 19in
            feed forward equation: Arm weight (from motor) * distance to arm cOm / Motor Stall Torque * Number of motors * gear ratio * cos(theta)
        */

        //wrist motor gearbox 9:1
        //arm motor gearbox ?:1
        wristRatio = 9.0;
        //elbowRatio = ;
        kP = 0.1; 
        kI = 1e-4;
        kD = 1; 
        kIz = 0; 
        kFF = ; 
        kMaxOutput = 1;
        kMinOutput = -1;

        elbowMotor = new CANSparkMax(RobotMap.elbowMotor, MotorType.kBrushless);
        wristMotor = new CANSparkMax(RobotMap.wristMotor, MotorType.kBrushless);
        elbowEncoder = elbowMotor.getEncoder();
        wristEncoder = wristMotor.getEncoder();
        elbowPID = elbowMotor.getPIDController();
        wristPID = wristMotor.getPIDController();
        elbowPID.setP(kP);
        elbowPID.setI(kI);
        elbowPID.setD(kD);
        elbowPID.setIZone(kIz);
        elbowPID.setFF(kFF);
        elbowPID.setOutputRange(kMinOutput, kMaxOutput);
        
        //elbowMotor.setParameter(ConfigParameter.kOutputRatio, elbowRatio);
        wristMotor.setParameter(ConfigParameter.kOutputRatio, wristRatio);
    }

    public void initAdjustPID(){
		SmartDashboard.putNumber("Elbow P", kP);
		SmartDashboard.putNumber("Elbow I", kI);
        SmartDashboard.putNumber("Elbow D", kD);

        // SmartDashboard.putNumber("Wrist P", 0);
		// SmartDashboard.putNumber("Wrist I", 0);
        // SmartDashboard.putNumber("Wrist D", 0);
        
    }
    
    public void adjustPID() {
		double p = SmartDashboard.getNumber("Elbow P", 0);
		double i = SmartDashboard.getNumber("Elbow I", 0);
        double d = SmartDashboard.getNumber("Elbow D", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);

        if((p != kP)) { elbowPID.setP(p); kP = p; }
        if((i != kI)) { elbowPID.setI(i); kI = i; }
        if((d != kD)) { elbowPID.setD(d); kD = d; }
        if((iz != kIz)) { elbowPID.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { elbowPID.setFF(ff); kFF = ff; }

        }
        // wristPID.setP(SmartDashboard.getNumber("Wrist P", 0));
		// wristPID.setI(SmartDashboard.getNumber("Wrist I", 0));
        // wristPID.setD(SmartDashboard.getNumber("Wrist D", 0));

	}
    
    public void setElbowPos(double r, ControlType c){
        SmartDashboard.putNumber("Elbow Encoder", elbowEncoder.getPosition());
        SmartDashboard.putNumber("Elbow Rotations", 0);
        rotations = SmartDashboard.getNumber("Elbow Rotations", 0);
        elbowPID.setReference(r, c);    
    }

    public void setWristPos(double r, ControlType c){
        SmartDashboard.putNumber("Wrist Encoder", wristEncoder.getPosition());
        // SmartDashboard.putNumber("Wrist Rotations", 0);
        wristPID.setReference(r,c);
    }

    public void setElbowMotor(double speed) {
        elbowMotor.set(speed);
    }

    public void setWristMotor(double speed) {
        wristMotor.set(speed);
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

    public double getWristPosition() {
        return wristEncoder.getPosition();
    }

    public double getWristVelocity() {
        return wristEncoder.getVelocity();
    }

    public CANEncoder getWristEncoder() {
        return wristEncoder;
    }

}