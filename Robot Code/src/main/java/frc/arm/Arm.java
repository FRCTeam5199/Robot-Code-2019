package frc.arm;

import frc.util.SparkMaxPID;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

public class Arm {

    private VictorSPX intakeMotor;
    private SparkMaxPID elbowMotor;
    private SparkMaxPID wristMotor;
    private SparkMaxPID eleMotor;

    private PIDController elbowPID;
    private PIDController wristPID;
    private PIDController elePID;

    //weight: 
    // Empirical Stall Torque: 2.6 Nm
    // Empirical Motor Kv: 473 Kv
    // Empirical Stall Current: 105 A
    // d to center of mass: 
    // feed forward equation: Arm weight (from motor) * distance to arm cOm / Motor Stall Torque * Number of motors * gear ratio * cos(theta)

    //in units of degrees, degrees, inches
    private double elbowRatio, wristRatio, eleRatio;
    private double kP, kI, kD, kFF;

    public Arm(){
        // these ratios are a little off
        elbowRatio = 720/343;
        wristRatio = 120/49;
        //                    |
        // this shit is wrong v
        //eleRatio = 0.378371;
        //units still arent exactly in inches, what is wrong with ratio?:
        //
        eleRatio = ((1/14.5384615) * 1.751) * Math.PI;
        //1:7 -> 26:54
        //sprocket diam = 1.751
        //eleTop : -31.053, loss @ bottom: 0.622-0.854

        intakeMotor = new VictorSPX(RobotMap.intakeMotor);

        elbowMotor = new SparkMaxPID(RobotMap.elbowMotor, MotorType.kBrushless);
        wristMotor = new SparkMaxPID(RobotMap.wristMotor, MotorType.kBrushless);
        eleMotor = new SparkMaxPID(RobotMap.eleMotor, MotorType.kBrushless);

        elbowPID = new PIDController(.1,0,0,0, elbowMotor, elbowMotor);
        wristPID = new PIDController(.1,0,0, wristMotor, wristMotor);
        elePID = new PIDController(.1,0,0,0, eleMotor, eleMotor);
       
    }

    public void initAdjustPID(){
		SmartDashboard.putNumber("Elbow P", kP);
		SmartDashboard.putNumber("Elbow I", kI);
        SmartDashboard.putNumber("Elbow D", kD);
        // SmartDashboard.putNumber("Elbow FF", kFF);
        // add the rest
        
    }
    
    public void adjustPID() {
		double p = SmartDashboard.getNumber("Elbow P", 0);
		double i = SmartDashboard.getNumber("Elbow I", 0);
        double d = SmartDashboard.getNumber("Elbow D", 0);
        double ff = SmartDashboard.getNumber("Elbow FF", 0);

        if((p != kP)) { elbowPID.setP(p); kP = p; }
        if((i != kI)) { elbowPID.setI(i); kI = i; }
        if((d != kD)) { elbowPID.setD(d); kD = d; }
        if((ff != kFF)) { elbowPID.setF(ff); kFF = ff;}

        //add the rest

    }
    
    public void setArmCoast(){
        elbowMotor.setIdleMode(IdleMode.kCoast);
        wristMotor.setIdleMode(IdleMode.kCoast);
        eleMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setArmBreak(){
        elbowMotor.setIdleMode(IdleMode.kBrake);
        wristMotor.setIdleMode(IdleMode.kBrake);
        eleMotor.setIdleMode(IdleMode.kBrake);
    }

    public void enableArmPID(){
        elePID.enable();
        elbowPID.enable();
        wristPID.enable();
    }

    public void disableArmPID(){
        elePID.disable();
        elbowPID.disable();
        wristPID.disable();
    }

    public void setIntake(double s){
        intakeMotor.set(ControlMode.PercentOutput, s);
    }

    public void moveElbow(double s){
        elbowMotor.set(s);
    }

    public void setElbow(double r){
        elbowPID.pidWrite(r/elbowRatio);
    }

    public void setWrist(double r){
        wristPID.pidWrite(r/wristRatio);
    }

    public void moveWrist(double s){
        wristMotor.set(s);
    }

    public void setEle(double r){
        elePID.pidWrite(r/eleRatio);
    }

    public void moveEle(double s){
        eleMotor.set(s);
    }

    public double getElbowPosition(){
        return elbowMotor.getEncoder().getPosition()*elbowRatio;
    }

    public double getWristPosition(){
       return wristMotor.getEncoder().getPosition()*wristRatio;
    }

    public double getElePosition(){
        return eleMotor.getEncoder().getPosition()*eleRatio;
    }

    public void setHatchIntake(double n) {
        intakeMotor.set(ControlMode.PercentOutput, -n);
    }

    public void setCargoIntake(double n) {
        intakeMotor.set(ControlMode.PercentOutput, n);
    }

}