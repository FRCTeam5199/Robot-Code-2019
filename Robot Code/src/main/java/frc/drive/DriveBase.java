package frc.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;
import frc.vision.Limelight;
import frc.vision.Limelight.LedMode;

import java.util.Arrays;
import java.util.Collections;

public class DriveBase {

    private final CANSparkMax leaderL, leaderR, slaveL, slaveR, slaveL2, slaveR2;

    private final Solenoid shiftUp, shiftDown;
    private final PigeonIMU gyro;
    private double[] gyroXYZ;
    private PIDController aimPID;
    private Limelight limey;
    private AimPIDOutput aimbot;

    

    public DriveBase() {
        leaderL = new CANSparkMax(RobotMap.driveLeaderLeft, MotorType.kBrushless);
        leaderR = new CANSparkMax(RobotMap.driveLeaderRight, MotorType.kBrushless);
        slaveL = new CANSparkMax(RobotMap.driveSlaveLeftA, MotorType.kBrushless);
        slaveR = new CANSparkMax(RobotMap.driveSlaveRightA, MotorType.kBrushless);
        slaveL2 = new CANSparkMax(RobotMap.driveSlaveLeftB, MotorType.kBrushless);
        slaveR2 = new CANSparkMax(RobotMap.driveSlaveRightB, MotorType.kBrushless);

        slaveL.follow(leaderL);
        slaveL2.follow(leaderL);
        slaveR.follow(leaderR);
        slaveR2.follow(leaderR);

        shiftUp = new Solenoid(RobotMap.gearboxPistonA);
        shiftDown = new Solenoid(RobotMap.gearboxPistonB);

        gyro = new PigeonIMU(RobotMap.CANGyro);
        gyroXYZ = new double[3];

        limey = new Limelight();
        aimbot = new AimPIDOutput(this);

        aimPID = new PIDController(0.009,0,0,limey, aimbot);
        //oh lawd

    }

    public void drive(double left, double right) {
        leaderL.set(-left);
        leaderR.set(right);
        // left is reversed
    }

    public void driveArcade(double move, double turn){
        drive(move-turn, move+turn);
    }

    public void setDriveCoast(){
        leaderL.setIdleMode(IdleMode.kCoast);
        slaveL.setIdleMode(IdleMode.kCoast);
        slaveL2.setIdleMode(IdleMode.kCoast);
        leaderR.setIdleMode(IdleMode.kCoast);
        slaveR.setIdleMode(IdleMode.kCoast);
        slaveR2.setIdleMode(IdleMode.kCoast);
    }

    public void setDriveBrake(){
        leaderL.setIdleMode(IdleMode.kBrake);
        slaveL.setIdleMode(IdleMode.kBrake);
        slaveL2.setIdleMode(IdleMode.kBrake);
        leaderR.setIdleMode(IdleMode.kBrake);
        slaveR.setIdleMode(IdleMode.kBrake);
        slaveR2.setIdleMode(IdleMode.kBrake);
    }

    public void setDriveCurrentMax(){
        leaderL.setClosedLoopRampRate(2.25);
        leaderR.setClosedLoopRampRate(2.25);
    }

    public void setCurrentLimits(int stallAmps, int freeAmps, int freeRpmMin){
        leaderL.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);
        leaderR.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);
        slaveL.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);
        slaveR.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);
        slaveL2.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);
        slaveR2.setSmartCurrentLimit(stallAmps, freeAmps, freeRpmMin);

    }
    //might get in the way of PID

    public void initGyro(){
        // gyro.configTemperatureCompensationEnable(true, 0);
        // how do you configure this thing @ startup? (rebias & etc.)
        
    }

    public double[] getGyro() {
        initGyro();
        gyro.getRawGyro(gyroXYZ);
        //gyro.getAccumGyro(gyroXYZ);
        //System.out.println(gyroXYZ.toString());
        return gyroXYZ;
    }

    public void printGyroVals(){
        System.out.println("Gyro X: " + getGyro()[0]);
        System.out.println("Gyro Y: " + getGyro()[1]);
        System.out.println("Gyro Z: " + getGyro()[2]);
    }

    public double getGyroRate(){
        return getGyro()[2];
        //get the rate from the gyro for robot turning
    }

    public double[] motorTemps(){
        double[] temps = {leaderL.getMotorTemperature(), slaveL.getMotorTemperature(), slaveL2.getMotorTemperature(), leaderR.getMotorTemperature(), slaveR.getMotorTemperature(), slaveR2.getMotorTemperature()};
        return temps;
    }

    public double getMotorTempAvg(){
        return slaveL.getMotorTemperature()*slaveL2.getMotorTemperature()*leaderL.getMotorTemperature()*leaderR.getMotorTemperature()*slaveR.getMotorTemperature()*slaveR2.getMotorTemperature()/6;
        //return avgTemp;
    }

    public double getMotorTempMax(){
          return Arrays.stream(motorTemps()).max().getAsDouble();
    }

    public double getEncoderLPos() {
        return leaderL.getEncoder().getPosition();
    }

    public double getEncoderRPos() {
        return leaderR.getEncoder().getPosition();
    }
    
    public void gearChange(boolean b) {
        shiftUp.set(b);
        shiftDown.set(!b);
        if(b){
            leaderL.getEncoder().setVelocityConversionFactor(9.07/1);
            leaderR.getEncoder().setVelocityConversionFactor(9.07/1);
            leaderL.getEncoder().setPositionConversionFactor(((9.07/1)/(6*Math.PI/60))/12);
            leaderR.getEncoder().setPositionConversionFactor(((9.07/1)/(6*Math.PI/60))/12);
        }
        else{
            leaderL.getEncoder().setVelocityConversionFactor(33.33/1);
            leaderR.getEncoder().setVelocityConversionFactor(33.33/1);
            leaderL.getEncoder().setPositionConversionFactor(((33.33/1)/(6*Math.PI/60))/12);
            leaderR.getEncoder().setPositionConversionFactor(((33.33/1)/(6*Math.PI/60))/12);
        }
    }

    public void enableAimPID(){
        aimPID.enable();
    }
    
    public void disableAimPID(){
        aimPID.disable();
    }

    public void driveAim(double move){
        aimbot.setMove(move);
    }

    public void aimLightsOn(){
        limey.setLEDMode(LedMode.ON);
    }

    public void aimLightsOff(){
        limey.setLEDMode(LedMode.OFF);
    }

    //auton code down here

    public double getRpmsLeft(){
        return leaderL.getEncoder().getVelocity();
    }
    public double getRpmsRight(){
        return leaderR.getEncoder().getVelocity();
    }
    public double getRpmsAvg(){
        return (getRpmsLeft()+getRpmsRight())/2;
    }
    public double getFpsLeft(){
        return (leaderL.getEncoder().getVelocity()/(6*Math.PI/60))/12;
    }
    public double getFpsRight(){
        return (leaderR.getEncoder().getVelocity()/(6*Math.PI/60))/12;
    }
    public double getFpsAvg(){
        return (getFpsLeft()+getFpsRight())/2;
    }
}