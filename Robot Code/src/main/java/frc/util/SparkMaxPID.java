package frc.util;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SparkMaxPID extends CANSparkMax implements PIDOutput, PIDSource{

    private PIDSourceType sourceType;

    public SparkMaxPID(int deviceID, com.revrobotics.CANSparkMaxLowLevel.MotorType type) {
        super(deviceID, type);
    }

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		sourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}

	@Override
	public double pidGet() {
		switch (sourceType){
            case kDisplacement:
                return getEncoder().getPosition();
            case kRate:
                return getEncoder().getVelocity();
        }
        return Double.NaN;
    }
    
    public void pidWrite(double output){
        set(output);
    }

}