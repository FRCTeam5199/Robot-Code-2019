package frc.arm;

import frc.util.SparkMaxPID;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.PIDController;

import java.util.Vector;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Arm {
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

    public Arm(){

        elbowRatio = 720/343;
        wristRatio = 120/49;
        eleRatio = 0.378371;

        elbowMotor = new SparkMaxPID(RobotMap.elbowMotor, MotorType.kBrushless);
        wristMotor = new SparkMaxPID(RobotMap.wristMotor, MotorType.kBrushless);
        eleMotor = new SparkMaxPID(RobotMap.eleMotor, MotorType.kBrushless);

        elbowPID = new PIDController(0,0,0,0, elbowMotor, elbowMotor);
        wristPID = new PIDController(0,0,0,0, elbowMotor, elbowMotor);
        elePID = new PIDController(0,0,0,0, eleMotor, eleMotor);
       
    }

    public void setElbow(double r){
        elbowPID.pidWrite(r/elbowRatio);
    }

    public void setWrist(double r){
        wristPID.pidWrite(r/wristRatio);
    }

    public void setEle(double r){
        elePID.pidWrite(r/eleRatio);
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

}