package frc.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

public class DriveBase {

    private final CANSparkMax leaderL, leaderR, SlaveL, SlaveR, SlaveL2, SlaveR2;

    private final Solenoid shiftUp, shiftDown;
    private final PigeonIMU gyro;
    private double[] gyroXYZ;

    public DriveBase() {
        leaderL = new CANSparkMax(RobotMap.driveLeaderLeft, MotorType.kBrushless);
        leaderR = new CANSparkMax(RobotMap.driveLeaderRight, MotorType.kBrushless);
        SlaveL = new CANSparkMax(RobotMap.driveSlaveLeftA, MotorType.kBrushless);
        SlaveR = new CANSparkMax(RobotMap.driveSlaveRightA, MotorType.kBrushless);
        SlaveL2 = new CANSparkMax(RobotMap.driveSlaveLeftB, MotorType.kBrushless);
        SlaveR2 = new CANSparkMax(RobotMap.driveSlaveRightB, MotorType.kBrushless);

        SlaveL.follow(leaderL);
        SlaveL2.follow(leaderL);
        SlaveR.follow(leaderR);
        SlaveR2.follow(leaderR);

        shiftUp = new Solenoid(RobotMap.gearboxPistonA);
        shiftDown = new Solenoid(RobotMap.gearboxPistonB);
        
        // create CANEncoder objects from the leader and return WPI encoders in the get
        // encoder methods by either
        // making a new class that extends the CANEncoder by implementing the wpi
        // Encoder
        // OR reading and writing the values

        gyro = new PigeonIMU(RobotMap.CANGyro);
        gyroXYZ = new double[3];
        // dont forget to init & rebias the gyro at startup

    }

    public void drive(double left, double right) {
        leaderL.set(-left);
        leaderR.set(right);
        // left is reversed
   
    }

    public void initGyro(){
        //gyro.configTemperatureCompensationEnable(bTempCompEnable, timeoutMs)
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
        return getGyro()[0];
        //0 is raw gyro rate on 'x' plane (yaw), which I think is what is turning
    }

    public double getEncoderLPos() {
        return leaderL.getEncoder().getPosition();
    }

    public double getEncoderRPos() {
        return leaderR.getEncoder().getPosition();
    }
    //no gear shifting on the practice bot
    public void gearChange(boolean b) {
        shiftUp.set(b);
        shiftDown.set(!b);
    }

}