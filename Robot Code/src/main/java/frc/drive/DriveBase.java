package frc.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

public class DriveBase {

    private final CANSparkMax leaderL, leaderR, slaveL, slaveR, slaveL2, slaveR2;

    private final Solenoid shiftUp, shiftDown;
    private final PigeonIMU gyro;
    private double[] gyroXYZ;

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
    }

    public void drive(double left, double right) {
        leaderL.set(-left);
        leaderR.set(right);
        // left is reversed
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

    public double getEncoderLPos() {
        return leaderL.getEncoder().getPosition();
    }

    public double getEncoderRPos() {
        return leaderR.getEncoder().getPosition();
    }
    
    public void gearChange(boolean b) {
        shiftUp.set(b);
        shiftDown.set(!b);
    }

}