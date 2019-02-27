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
    private double ekP, ekI, ekD, ekFF, elkP, elkI, elkD, elkFF, wkP, wkI, wkD, wkFF;

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

        intakeMotor = new VictorSPX(RobotMap.intakeMotor);

        elbowMotor = new SparkMaxPID(RobotMap.elbowMotor, MotorType.kBrushless);
        wristMotor = new SparkMaxPID(RobotMap.wristMotor, MotorType.kBrushless);
        eleMotor = new SparkMaxPID(RobotMap.eleMotor, MotorType.kBrushless);

        //all of these need tuning oh boy
        elbowPID = new PIDController(0.1,0.0000005,0.15,.01355871, elbowMotor, elbowMotor);
        // ff: 1% of 1.55697115 (w/ change: .01255871)
        wristPID = new PIDController(0.1,0,0, wristMotor, wristMotor);
        elePID = new PIDController(0.1,0.000001,0.1,.00125, eleMotor, eleMotor);
       
    }

    public void initAdjustPID(){
		SmartDashboard.putNumber("Elbow P", ekP);
		SmartDashboard.putNumber("Elbow I", ekI);
        SmartDashboard.putNumber("Elbow D", ekD);
        SmartDashboard.putNumber("Elbow FF", ekFF);

        SmartDashboard.putNumber("Elevator P", elkP);
		SmartDashboard.putNumber("Elevator I", elkI);
        SmartDashboard.putNumber("Elevator D", elkD);
        SmartDashboard.putNumber("Elevator FF", elkFF);

        SmartDashboard.putNumber("Wrist P", wkP);
		SmartDashboard.putNumber("Wrist I", wkI);
        SmartDashboard.putNumber("Wrist D", wkD);
        SmartDashboard.putNumber("Wrist FF", wkFF);
        // add the rest
        
    }
    
    public void adjustPID() {
		double ep = SmartDashboard.getNumber("Elbow P", 0);
		double ei = SmartDashboard.getNumber("Elbow I", 0);
        double ed = SmartDashboard.getNumber("Elbow D", 0);
        double eff = SmartDashboard.getNumber("Elbow FF", 0);

        if((ep != ekP)) { elbowPID.setP(ep); ekP = ep; }
        if((ei != ekI)) { elbowPID.setI(ei); ekI = ei; }
        if((ed != ekD)) { elbowPID.setD(ed); ekD = ed; }
        if((eff != ekFF)) { elbowPID.setF(eff); ekFF = eff;}

        double elp = SmartDashboard.getNumber("Elevator P", 0);
		double eli = SmartDashboard.getNumber("Elevator I", 0);
        double eld = SmartDashboard.getNumber("Elevator D", 0);
        double elff = SmartDashboard.getNumber("Elevator FF", 0);

        if((elp != elkP)) { elePID.setP(elp); elkP = elp; }
        if((eli != elkI)) { elePID.setI(eli); elkI = eli; }
        if((eld != elkD)) { elePID.setD(eld); elkD = eld; }
        if((elff != elkFF)) { elePID.setF(elff); elkFF = elff;}

        double wp = SmartDashboard.getNumber("Wrist P", 0);
		double wi = SmartDashboard.getNumber("Wrist I", 0);
        double wd = SmartDashboard.getNumber("Wrist D", 0);
        double wff = SmartDashboard.getNumber("Wrist FF", 0);

        if((wp != wkP)) { wristPID.setP(wp); wkP = wp; }
        if((wi != wkI)) { wristPID.setI(wi); wkI = wi; }
        if((wd != wkD)) { wristPID.setD(wd); wkD = wd; }
        if((wff != wkFF)) { wristPID.setF(wff); wkFF = wff;}

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

    public void enableElePID(){
        elePID.enable();
    }

    public void disableElePID(){
        elePID.disable();
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

    public void setPointElbow(double r){
        elbowPID.setSetpoint(r/elbowRatio);
    }

    public void setPointWrist(double r){
        wristPID.setSetpoint(r/wristRatio);
    }

    public void setPointEle(double r){
        elePID.setSetpoint(r/eleRatio);
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

    public double getEleMotorPos(){
        return eleMotor.getEncoder().getPosition();
    }

    public void setHatchIntake(double n) {
        intakeMotor.set(ControlMode.PercentOutput, -n);
    }

    public void setCargoIntake(double n) {
        intakeMotor.set(ControlMode.PercentOutput, n);
    }

}