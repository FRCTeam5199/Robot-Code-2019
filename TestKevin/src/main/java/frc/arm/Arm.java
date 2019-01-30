package frc.arm;

import frc.robot.Robot;
import frc.robot.RobotMap;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;

public class Arm {

    private final CANSparkMax elbowMotor;
    private final CANSparkMax wristMotor;
    private final CANEncoder elbowEncoder;
    private final CANEncoder wristEncoder;

    public Arm() {
        elbowMotor = new CANSparkMax(RobotMap.elbowMotor, MotorType.kBrushless);
        wristMotor = new CANSparkMax(RobotMap.wristMotor, MotorType.kBrushless);
        elbowEncoder = new CANEncoder(elbowMotor);
        wristEncoder = new CANEncoder(wristMotor);
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

    public void setWristMotor(double speed) {
        wristMotor.set(speed);
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