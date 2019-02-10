package frc.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.RobotMap;

public class DriveBase{

    private final CANSparkMax leaderL, leaderR, SlaveL, SlaveR, SlaveL2, SlaveR2;

    public DriveBase(){
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

    }

    
    
    
}