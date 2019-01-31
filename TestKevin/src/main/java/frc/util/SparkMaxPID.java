package frc.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDOutput;

public class SparkMaxPID extends CANSparkMax implements PIDOutput {

    public SparkMaxPID(int deviceNumber, MotorType type) {
        super(deviceNumber, type);
    }

    public void pidWrite(double output) {
        set(output);
    }
}